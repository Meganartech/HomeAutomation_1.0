package com.meganar.smart;

import static com.meganar.smart.SessionManager.getThemeColor;
import static com.meganar.smart.ThemeManager.setCustomizedThemes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //Totally at least 6 characters
                    "$");

    private EditText email1;
    private EditText username1;
    private EditText password1;
    private Button signup1;
    private EditText phone1;
    private CheckBox checkBox;
    private ProgressBar p1;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private DatabaseReference users;
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomizedThemes(this,getThemeColor(this));
        setContentView(R.layout.activity_register);
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));  //action bar color
        email1 = findViewById(R.id.r_email);
        username1 = findViewById(R.id.r_username);
        password1 = findViewById(R.id.r_password);
        signup1 = findViewById(R.id.register);
        checkBox = findViewById(R.id.confirm_button);
        phone1 = findViewById(R.id.phone_number);
        checkBox =findViewById(R.id.confirm_button);
        p1 = findViewById(R.id.p1);
        firebaseAuth = FirebaseAuth.getInstance();

        signup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String inputName = username1.getText().toString().trim();
                final String inputPw = password1.getText().toString().trim();
                final String inputEmail = email1.getText().toString().trim();
                final String inputPhone = phone1.getText().toString().trim();
                if (validateInput(inputName, inputPhone, inputEmail, inputPw))

                {
                    registerUser(inputName, inputPhone, inputEmail, inputPw);
                    signup1.setVisibility(View.GONE);
                    p1.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

    private void registerUser(final String inputName, final String inputPhone, final String inputEmail, final String inputPw) {

        firebaseAuth.createUserWithEmailAndPassword(inputEmail,inputPw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseUser = firebaseAuth.getCurrentUser();
                    String uuid = firebaseUser.getUid();
                    sendUserData(inputName, inputEmail, inputPhone, inputPw);
                    sendVerificationEmail();
                } else {
                    signup1.setVisibility(View.VISIBLE);
                    p1.setVisibility(View.GONE);
                    Toast.makeText(getBaseContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                  //  DynamicToast.makeError(RegisterActivity.this, task.getException().getMessage(), 10).show();
                    startActivity(new Intent(RegisterActivity.this, RegisterActivity.class));
                    finish();
                }
            }
        });
    }

    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().signOut();
                            signup1.setVisibility(View.VISIBLE);
                            p1.setVisibility(View.GONE);
                            Toast.makeText(getBaseContext(), "Check Email For Verification", Toast.LENGTH_SHORT).show();
                           // DynamicToast.makeSuccess(RegisterActivity.this, "Check Email For Verification.", 10).show();
                            startActivity(new Intent(RegisterActivity.this, LoginscreenActivity.class));
                            finish();
                        }
                        else
                        {
                            signup1.setVisibility(View.VISIBLE);
                            p1.setVisibility(View.GONE);
                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(getBaseContext(), "Unable to send Email", Toast.LENGTH_SHORT).show();
                            //DynamicToast.makeError(RegisterActivity.this, "Unable to Send Mail", 10).show();
                            startActivity(new Intent(RegisterActivity.this, RegisterActivity.class));
                            finish();
                        }
                    }
                });
    }

    private void sendUserData(String name, String emailid, String phone, String password) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        users = firebaseDatabase.getReference("users");

        Map<String, String> userData = new HashMap<String, String>();
        userData.put("name", name);
        userData.put("phone", phone);
        userData.put("password", password);
        userData.put("email", emailid);
        //
        Map<String, String> aof = new HashMap<String, String>();
       // aof.put("days", "NA");
        aof.put("ontime", "00:00");
        aof.put("offtime", "00:00");
        //
        Map<String, String> componentsdb = new HashMap<String , String>();
        componentsdb.put("port1", "");
        componentsdb.put("port2", "");
        componentsdb.put("port3", "");
        componentsdb.put("port4", "");

        Map<String, String> messagedb = new HashMap<String , String>();
        messagedb.put("Status","");
        messagedb.put("port1", "");
        messagedb.put("port2", "");
        messagedb.put("port3", "");
        messagedb.put("port4", "");


        Map<String, String> devicedb = new HashMap<String , String>();
        devicedb.put("port1", "");
        devicedb.put("port2", "");
        devicedb.put("port3", "");
        devicedb.put("port4", "");
        Map<String, String> wifidb = new HashMap<String , String>();
        wifidb.put("ssid", "");
        wifidb.put("password", "");
        Map<String, String> settingsdb = new HashMap<String , String>();
        settingsdb.put("themecolor", "");
        settingsdb.put("Notification", "ON");
        Map<String, String> weatherdb = new HashMap<String , String>();
        weatherdb.put("Temperature", "");
        weatherdb.put("Humidity", "");
        weatherdb.put("Wind speed", "");
        weatherdb.put("Pressure", "");
        weatherdb.put("Location", "");
        users.child(firebaseUser.getUid()).setValue(userData);
        users.child(firebaseUser.getUid()).child("AutomaticOnOff").child("port1").setValue(aof);
        users.child(firebaseUser.getUid()).child("AutomaticOnOff").child("port2").setValue(aof);
        users.child(firebaseUser.getUid()).child("AutomaticOnOff").child("port3").setValue(aof);
        users.child(firebaseUser.getUid()).child("AutomaticOnOff").child("port4").setValue(aof);
        users.child(firebaseUser.getUid()).child("Message").setValue(messagedb);
        users.child(firebaseUser.getUid()).child("Devicename").setValue(devicedb);
        users.child(firebaseUser.getUid()).child("Wifi").setValue(wifidb);
        users.child(firebaseUser.getUid()).child("Settings").setValue(settingsdb);
        users.child(firebaseUser.getUid()).child("Weather").setValue(weatherdb);
        users.child(firebaseUser.getUid()).child("components").setValue(componentsdb);
    }


    private boolean validateInput(String inName, String inPw, String inPhone, String inEmail) {

        if (inEmail.isEmpty()) {
            email1.setError("Email is empty.");
            return false;
        }

       else if  (inName.isEmpty()) {
            username1.setError("Name is empty.");
            return false;
        }
        else if (inPw.isEmpty()) {
            password1.setError("Password is empty.");
            return false;
        }
        else if (PASSWORD_PATTERN.matcher(inPw).matches()) {
            password1.setError("Password too weak");
            return false;
        }

        else if (inPhone.isEmpty()) {
            phone1.setError("Mobile Number is empty.");
            return false;
        }
        return true;
    }


}