#include <Arduino.h>
#include <Preferences.h>
#if defined(ESP32)
#include <WiFi.h>
#elif defined(ESP8266)
#include <ESP8266WiFi.h>
#elif defined(ARDUINO_RASPBERRY_PI_PICO_W)
#include <WiFi.h>
#endif

#include <Firebase_ESP_Client.h>
#include <BLEDevice.h>
#include <BLEServer.h>
#include <BLEUtils.h>
#include <BLE2902.h>
#include <addons/TokenHelper.h>
#include <addons/RTDBHelper.h>

#define API_KEY "AIzaSyAsEJN5sRmEUG2RLz3242hSzc3PaZNtmsM"
#define DATABASE_URL "https://smarthome-2bdd6-default-rtdb.firebaseio.com/"

FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;

unsigned long sendDataPrevMillis = 0;
unsigned long count = 0;

bool bluetooth = false;
bool wifiConnected = false;
bool firebaseInitialized = false;

int con = 0;

String uid;
String email;
String password;

Preferences preferences;

String openWeatherMapApiKey = "9e9ed4db93beada08dc35f1e82e631ef";
String city = "Madurai";
String countryCode = "IN";
String formattedDate;

String timeStamp, withoutsec;

BLEServer* pServer = NULL;
BLECharacteristic* pCharacteristic = NULL;
bool deviceConnected = false;
bool oldDeviceConnected = false;
uint8_t value = 0;

#define SERVICE_UUID        "4fafc201-1fb5-459e-8fcc-c5c9c331914b"
#define CHARACTERISTIC_UUID "8ce255c0-223a-11e0-ac64-0803450c9a66"

class MyServerCallbacks: public BLEServerCallbacks {
    void onConnect(BLEServer* pServer) {
      deviceConnected = true;
    };

    void onDisconnect(BLEServer* pServer) {
      deviceConnected = false;
    }
};

class MyCallbacks: public BLECharacteristicCallbacks {
    void onWrite(BLECharacteristic *pCharacteristic) {
        std::string rxValue = pCharacteristic->getValue();
        char buf[256];
        strcpy(buf, rxValue.c_str());
        Serial.print(buf);

        int i = 0;
        char *p = strtok(buf, ",");
        char *array[5];

        while (p != NULL) {
            array[i++] = p;
            p = strtok(NULL, ",");
        }

        if (i == 5) {
            Serial.println("Received data:");
            Serial.println("*********");
            Serial.printf("SSID: %s\n", array[0]);
            Serial.printf("Password: %s\n", array[1]);
            Serial.printf("UID: %s\n", array[2]);
            Serial.printf("Email: %s\n", array[3]);
            Serial.printf("Firebase Password: %s\n", array[4]);
            Serial.println("*********");

            char* WIFI_SSID = array[0];
            char* WIFI_PASSWORD = array[1];
            uid = array[2];
            email = array[3];
            password = array[4];
            preferences.begin("wifi", false); // Ensure preferences storage is properly initialized

            preferences.putString("WIFI_SSID", WIFI_SSID);
            preferences.putString("WIFI_PASSWORD", WIFI_PASSWORD);
            preferences.putString("uid", uid);
            preferences.putString("email", email);
            preferences.putString("password", password);
            preferences.end();

            Serial.println("Connecting to Wi-Fi...");
            WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
            while (WiFi.status() != WL_CONNECTED) {
                Serial.print(".");
                vTaskDelay(300 / portTICK_PERIOD_MS);
            }
            Serial.println();
            Serial.print("Connected with IP: ");
            Serial.println(WiFi.localIP());
            wifiConnected = true;
            con = 1;
        }
    }
};

const int led1 = 2;
const int motorPin = 33;
const int led3 = 32;
const int led4 = 26;
const int switchPin = 4;   // Replace with your button pin
int switchState = 0;         // variable for reading the switch status
int previousSwitchState = 0; // variable to store the previous switch state
int switchState2 = 0;         // variable for reading the switch status
int previousSwitchState2 = 0; // variable to store the previous switch state
int pressCount = 0;          // counter for the number of switch presses
int pressCount2 =0;
unsigned long lastPressTime = 0;
unsigned long lastPressTime2 = 0;
int switchState3 = 0;         // variable for reading the switch status
int previousSwitchState3 = 0; // variable to store the previous switch state
int switchState4 = 0;         // variable for reading the switch status
int previousSwitchState4 = 0; // variable to store the previous switch state
int pressCount3 = 0;          // counter for the number of switch presses
int pressCount4 =0;
unsigned long lastPressTime3 = 0;
unsigned long lastPressTime4 = 0;
bool bluetoothInitialized = false;

void ble() {
    if (con == 0) {
        BLEDevice::init("ESP1");
        pServer = BLEDevice::createServer();
        pServer->setCallbacks(new MyServerCallbacks());
        BLEService *pService = pServer->createService(SERVICE_UUID);
        pCharacteristic = pService->createCharacteristic(
                              CHARACTERISTIC_UUID,
                              BLECharacteristic::PROPERTY_READ |
                              BLECharacteristic::PROPERTY_WRITE |
                              BLECharacteristic::PROPERTY_NOTIFY |
                              BLECharacteristic::PROPERTY_INDICATE
                          );
        pCharacteristic->addDescriptor(new BLE2902());
        pService->start();
        pServer->getAdvertising()->start();
        Serial.println("Waiting for a client connection to notify...");
        bluetoothInitialized = true;
    }
}

void initFirebase() {
    Serial.printf("Firebase Client v%s\n\n", FIREBASE_CLIENT_VERSION);
        preferences.begin("wifi", false); // Ensure preferences storage is properly initialized

    uid = preferences.getString("uid", "");
    email = preferences.getString("email", "");
    password = preferences.getString("password", "");
    preferences.end();


    Serial.println("Debug Info:");
    Serial.printf("UID: %s\n", uid.c_str());
    Serial.printf("Email: %s\n", email.c_str());
    Serial.printf("Password: %s\n", password.c_str());
    Serial.printf("API_KEY: %s\n", API_KEY);
    Serial.printf("DATABASE_URL: %s\n", DATABASE_URL);

    config.api_key = API_KEY;
    auth.user.email = email.c_str();
    auth.user.password = password.c_str();
    config.database_url = DATABASE_URL;
    config.token_status_callback = tokenStatusCallback;
    Firebase.begin(&config, &auth);
    Firebase.reconnectWiFi(true);
    Firebase.setDoubleDigits(5);
    firebaseInitialized = true;
}

void taskBLE(void * parameter) {
    while (!bluetooth) {
      switchState = analogRead(switchPin);
    switchState2 = analogRead(switchPin);
    switchState3 = analogRead(switchPin);
    switchState4 = analogRead(switchPin);

  //  Serial.println(switchState);
 //   Serial.println(switchState2);
 //   Serial.println(switchState3);
 //   Serial.println(switchState4);

   // delay(500);

    

    
  if (switchState >=700 && previousSwitchState <=700 ) {
    // Check if 10 seconds have passed since the last press
    if (millis() - lastPressTime >= 10000) {
      pressCount = 1;  // Reset press count to 1
      Serial.println("Press count reset to 1");
    } else {
      pressCount++;  // Increment press count
      Serial.print("Press count: ");
      Serial.println(pressCount);

      // Check if the button is pressed three times within 10 seconds
      
    }
    lastPressTime = millis();  // Update last press time

    delay(1000);
  } else {
    // Check if 10 seconds have passed since the last press
    if (millis() - lastPressTime >= 10000 && pressCount > 0) {
      pressCount = 0;  // Reset press count to 0
     // digitalWrite(ledPin, LOW);  // Turn off the LED
      Serial.println("Press count reset to 0");
     // Serial.println("LED turned off");
    }

    // Your other code goes here
  }
   if (switchState2 >=2400 && previousSwitchState2 <=2400 ) {
    // Check if 10 seconds have passed since the last press
    if (millis() - lastPressTime2 >= 10000) {
      pressCount2 = 1;  // Reset press count to 1
      Serial.println("Press count reset to 1");
    } else {
      pressCount2++;  // Increment press count
      Serial.print("Press count2: ");
      Serial.println(pressCount2);

      // Check if the button is pressed three times within 10 seconds
      
    }
    lastPressTime2 = millis();  // Update last press time

    delay(1000);
  } else {
    // Check if 10 seconds have passed since the last press
    if (millis() - lastPressTime >= 10000 && pressCount > 0) {
      pressCount2 = 0;  // Reset press count to 0
     // digitalWrite(ledPin, LOW);  // Turn off the LED
     Serial.println("Press count reset to 0");
     // Serial.println("LED turned off");
    }

    // Your other code goes here
  }
   if (switchState3 >=4000 && previousSwitchState3 <=4000 ) {
    // Check if 10 seconds have passed since the last press
    if (millis() - lastPressTime3 >= 10000) {
      pressCount3 = 1;  // Reset press count to 1
      Serial.println("Press count reset to 1");
    } else {
      pressCount3++;  // Increment press count
      Serial.print("Press count3: ");
      Serial.println(pressCount3);

      // Check if the button is pressed three times within 10 seconds
      
    }
    lastPressTime3 = millis();  // Update last press time

    delay(1000);
  } else {
    // Check if 10 seconds have passed since the last press
    if (millis() - lastPressTime3 >= 10000 && pressCount3 > 0) {
      pressCount3 = 0;  // Reset press count to 0
     // digitalWrite(ledPin, LOW);  // Turn off the LED
      Serial.println("Press count reset to 0");
     // Serial.println("LED turned off");
    }

    // Your other code goes here
  }
     if (switchState4 >=4050 && previousSwitchState3 <=4050 ) {
    // Check if 10 seconds have passed since the last press
    if (millis() - lastPressTime4 >= 10000) {
      pressCount4 = 1;  // Reset press count to 1
      Serial.println("Press count reset to 1");
    } else {
      pressCount4++;  // Increment press count
      Serial.print("Press count4: ");
      Serial.println(pressCount4);

      // Check if the button is pressed three times within 10 seconds
      
    }
    lastPressTime4 = millis();  // Update last press time

    delay(1000);
  } else {
    // Check if 10 seconds have passed since the last press
    if (millis() - lastPressTime4 >= 10000 && pressCount4 > 0) {
      pressCount4 = 0;  // Reset press count to 0
     // digitalWrite(ledPin, LOW);  // Turn off the LED
      Serial.println("Press count reset to 0");
     // Serial.println("LED turned off");
    }

    // Your other code goes here
  }


  if (pressCount == 3 || pressCount2 ==3||pressCount3 == 3 || pressCount4 ==3) {
            ble();
            bluetooth = true;
        }

     previousSwitchState = switchState;
     previousSwitchState2 = switchState2;
     previousSwitchState3 = switchState3;
     previousSwitchState4 = switchState4;
        vTaskDelay(10 / portTICK_PERIOD_MS);
    }

    vTaskDelete(NULL);
}

void taskFirebase(void * parameter) {
    while (!wifiConnected) {
        if (con == 0) {
            if (deviceConnected) {
                Serial.println("Sending Bluetooth data...");
                pCharacteristic->setCallbacks(new MyCallbacks());
                String s = "ESP";
                pCharacteristic->setValue(s.c_str());
                pCharacteristic->notify();
                value++;
                vTaskDelay(7000 / portTICK_PERIOD_MS);
            }
            if (!deviceConnected && oldDeviceConnected) {
                vTaskDelay(500 / portTICK_PERIOD_MS);
                pServer->startAdvertising();
                Serial.println("Start advertising");
                oldDeviceConnected = deviceConnected;
            }
            if (deviceConnected && !oldDeviceConnected) {
                oldDeviceConnected = deviceConnected;
            }
        }

        vTaskDelay(100 / portTICK_PERIOD_MS);
    }

    initFirebase();

    while (1) {
         // initFirebase();

        if (Firebase.ready() && (millis() - sendDataPrevMillis > 500 || sendDataPrevMillis == 0)) {
            Serial.println("-----------------------");
            sendDataPrevMillis = millis();

            String p1;
            Serial.printf("Get bool ref... %s\n", Firebase.RTDB.getString(&fbdo, "users/" + uid + "/components/ESP3_1", &p1) ? p1 ? "1" : "0" : fbdo.errorReason().c_str());
            String p2;
            Serial.printf("Get bool ref... %s\n", Firebase.RTDB.getString(&fbdo, "users/" + uid + "/components/ESP3_2", &p2) ? p2 ? "1" : "0" : fbdo.errorReason().c_str());
            String p3;
            Serial.printf("Get bool ref... %s\n", Firebase.RTDB.getString(&fbdo, "/users/" + uid + "/components/ESP3_3", &p3) ? p3 ? "1" : "0" : fbdo.errorReason().c_str());
            String p4;
            Serial.printf("Get bool ref... %s\n", Firebase.RTDB.getString(&fbdo, "/users/" + uid + "/components/ESP3_4", &p4) ? p4 ? "1" : "0" : fbdo.errorReason().c_str());

            digitalWrite(led1, p1 == "1" ? HIGH : LOW);
            digitalWrite(led3, p2 == "1" ? HIGH : LOW);
            digitalWrite(led4, p3 == "1" ? HIGH : LOW);
            digitalWrite(motorPin, p4 == "1" ? HIGH : LOW);

            String device1, device2, device3, device4;
            Firebase.RTDB.getString(&fbdo, "users/" + uid + "/Devicename/port1", &device1);
            Firebase.RTDB.getString(&fbdo, "users/" + uid + "/Devicename/port2", &device2);
            Firebase.RTDB.getString(&fbdo, "users/" + uid + "/Devicename/port3", &device3);
            Firebase.RTDB.getString(&fbdo, "users/" + uid + "/Devicename/port4", &device4);
        }

        vTaskDelay(10 / portTICK_PERIOD_MS);
    }

    vTaskDelete(NULL);
}

void setup() {
    Serial.begin(115200);
    delay(1000);
    pinMode(switchPin, INPUT);
    pinMode(motorPin, OUTPUT);
    pinMode(led3, OUTPUT);
    pinMode(led4, OUTPUT);
    pinMode(led1, OUTPUT);

    preferences.begin("wifi", false);
    String WIFI_SSID = preferences.getString("WIFI_SSID", "");
    String WIFI_PASSWORD = preferences.getString("WIFI_PASSWORD", "");
    uid = preferences.getString("uid", "");
    email = preferences.getString("email", "");
    password = preferences.getString("firebase_password", "");

    if (WIFI_SSID.length() > 0 && WIFI_PASSWORD.length() > 0) {
        const char* ssid = WIFI_SSID.c_str();
        const char* Password = WIFI_PASSWORD.c_str();
        Serial.println("Connecting to Wi-Fi...");
        unsigned long startAttemptTime = millis();

        WiFi.begin(ssid, Password);
        while (WiFi.status() != WL_CONNECTED && millis() - startAttemptTime < 10000) {
            Serial.print(".");
            vTaskDelay(300 / portTICK_PERIOD_MS);
        }
        Serial.println();
        if (WiFi.status() == WL_CONNECTED) {

        Serial.print("Connected with IP: ");
        Serial.println(WiFi.localIP());
        wifiConnected = true;
        con = 1;
        }
        else{
          ble();
        }
      
    } else {
        Serial.println("No stored credentials. Please set up Wi-Fi.");
    }
    

    xTaskCreatePinnedToCore(
        taskBLE,
        "BLETask",
        20000,
        NULL,
        1,
        NULL,
        0);

    xTaskCreatePinnedToCore(
        taskFirebase,
        "FirebaseTask",
        20000,
        NULL,
        1,
        NULL,
        1);
}

void loop() {
}
