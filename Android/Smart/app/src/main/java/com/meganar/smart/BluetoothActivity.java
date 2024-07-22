package com.meganar.smart;

import static com.meganar.smart.SessionManager.getThemeColor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smart.R;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BluetoothActivity extends AppCompatActivity implements OnBluetoothDeviceClickedListener {

    private static final String PREFS_NAME = "PairedDevicesPrefs";
    private static final String PAIRED_DEVICES_KEY = "paired_devices";

    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

    private static final int PERMISSION_REQUEST_FINE_LOCATION = 2;
    private static final int PERMISSION_REQUEST_BLUETOOTH_SCAN = 3;


    private static final int PERMISSION_REQUEST_BLUETOOTH = 4;
    private static final int PERMISSION_REQUEST_BLUETOOTH_ADMIN = 5;
    private static final int PERMISSION_REQUEST_BLUETOOTH_CONNECT = 6;
    private static final int PERMISSION_REQUEST_BLUETOOTH_ADVERTISE = 7;

    private final int REQUEST_PERMISSION_ACCESS_FINE_LOCATION = 5;


    private static final int REQUEST_BLUETOOTH_PERMISSION = 2;


    private static final int REQUEST_CONNECT = 1;
    public static final String EXTRAS_DEVICE_NAME = "extras_device_name";
    public static final String EXTRAS_DEVICE_ADDRESS = "extras_device_address";
    private String mConnectionState = BluetoothLeService.ACTION_GATT_DISCONNECTED;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 1000 * 10;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;
    private MyBluetoothDeviceAdapter mBluetoothDeviceAdapter;
    private final List<BluetoothDevice> mBluetoothDeviceList = new ArrayList<>();
    private final MyBluetoothScanCallBack mBluetoothScanCallBack = new MyBluetoothScanCallBack();
    private Handler mHandler;
    private BluetoothLeService mBluetoothLeService;
    private String mDeviceName;
    private String mDeviceAddress;
    private AlertDialog dialog;

    EditText wifi_input, wifi_input2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeManager.setCustomizedThemes(this, getThemeColor(this));
        setContentView(R.layout.activity_bluetooth);

        initView();
        requestPermission();
        initData();
        initService();
        alert();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initReceiver();
        scanLeDevice(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("MainActivity", "unregisterReceiver()");
        unregisterReceiver(mGattUpdateReceiver);
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check.
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSION_REQUEST_COARSE_LOCATION);
            }
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_FINE_LOCATION);
            }
            if (this.checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_SCAN},
                        PERMISSION_REQUEST_BLUETOOTH_SCAN);
            }
            if (this.checkSelfPermission(Manifest.permission.BLUETOOTH)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH},
                        PERMISSION_REQUEST_BLUETOOTH);
            }
            if (this.checkSelfPermission(Manifest.permission.BLUETOOTH_ADMIN)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_ADMIN},
                        PERMISSION_REQUEST_BLUETOOTH_ADMIN);
            }
            if (this.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                        PERMISSION_REQUEST_BLUETOOTH_CONNECT);
            }
            if (this.checkSelfPermission(Manifest.permission.BLUETOOTH_ADVERTISE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_ADVERTISE},
                        PERMISSION_REQUEST_BLUETOOTH_ADVERTISE);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(BluetoothActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(BluetoothActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == PERMISSION_REQUEST_BLUETOOTH_SCAN) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(BluetoothActivity.this, "Bluetooth Scan Permission Granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(BluetoothActivity.this, "Bluetooth Scan Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == PERMISSION_REQUEST_BLUETOOTH) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(BluetoothActivity.this, "Bluetooth Permission Granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(BluetoothActivity.this, "Bluetooth Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == PERMISSION_REQUEST_BLUETOOTH_ADMIN) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(BluetoothActivity.this, "Ble Permission Granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(BluetoothActivity.this, "Ble Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == PERMISSION_REQUEST_BLUETOOTH_CONNECT) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(BluetoothActivity.this, "Ble Permission Granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(BluetoothActivity.this, "Ble Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == PERMISSION_REQUEST_BLUETOOTH_ADVERTISE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(BluetoothActivity.this, "Ble Permission Granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(BluetoothActivity.this, "Ble Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void initView() {
        recyclerView = findViewById(R.id.recycler_view);
        swipeRefresh = findViewById(R.id.swipe_refresh);
    }

    private void initService() {
        Log.i("MainActivity", "initService()");

        if (mBluetoothLeService == null) {
            Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
            bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        }
    }

    private void initData() {
        mHandler = new Handler();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        mBluetoothDeviceAdapter = new MyBluetoothDeviceAdapter(mBluetoothDeviceList, this);
        recyclerView.setAdapter(mBluetoothDeviceAdapter);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mBluetoothDeviceList != null) {
                    mBluetoothDeviceList.clear();
                }
                scanLeDevice(true);
            }
        });


    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e("MainActivity", "Unable to initialize Bluetooth");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };


    private void scanLeDevice(boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefresh.setRefreshing(false);
                    BluetoothScan.stopScan();
                }
            }, SCAN_PERIOD);
            swipeRefresh.setRefreshing(true);
            BluetoothScan.startScan(true, mBluetoothScanCallBack);
        } else {
            swipeRefresh.setRefreshing(false);
            BluetoothScan.stopScan();
        }
    }

    @Override
    public void onBluetoothDeviceClicked(String name, String address) {
        Log.i("MainActivity", "Attempt to connect device : " + name + "(" + address + ")");
        mDeviceName = name;
        mDeviceAddress = address;
        savePairedDevice(name, address);

        // Get the Bluetooth device object
        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(mDeviceAddress);

        //    connectToDevice(device);


        // Check if the device is already bonded (paired)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
            // If already bonded, simply connect to the device
            connectToDevice(device);
        } else {
            // If not bonded, initiate the pairing process
            pairDevice(device);

        }
    }


    private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
            connectToDevice(device);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void savePairedDevice(String name, String address) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> pairedDevices = prefs.getStringSet(PAIRED_DEVICES_KEY, new HashSet<>());
        pairedDevices.add(name + "," + address);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(PAIRED_DEVICES_KEY, pairedDevices);
        editor.apply();
    }



    private void connectToDevice(BluetoothDevice device) {
        if (mBluetoothLeService != null) {
            if (mBluetoothLeService.connect(mDeviceAddress)) {
                showMsg("Attempt to connect device : " + mDeviceName);
                mConnectionState = BluetoothLeService.ACTION_GATT_CONNECTING;
                swipeRefresh.setRefreshing(true);
            } else {
                showMsg("Failed to initiate connection to device");
            }
        } else {
            showMsg("BluetoothLeService is null. Make sure it's initialized properly.");
        }
    }


    private void initReceiver() {
        Log.i("MainActivity", "initReceiver()");
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);

        registerReceiver(mGattUpdateReceiver, intentFilter);
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                if (state == BluetoothDevice.BOND_BONDED) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device != null && device.getAddress().equals(mDeviceAddress)) {
                        connectToDevice(device);
                    }
                }
                Log.i("MainActivity", "ACTION_GATT_CONNECTED!!!");
                showMsg("Connected device ..");

                mConnectionState = BluetoothLeService.ACTION_GATT_CONNECTED;
                swipeRefresh.setRefreshing(false);

                //inputMessage();
                dialog.show();
                Log.i("MainActivity", "open message--");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                Log.i("MainActivity", "ACTION_GATT_DISCONNECTED!!!");
                showMsg("disconnected");
                mConnectionState = BluetoothLeService.ACTION_GATT_DISCONNECTED;
                swipeRefresh.setRefreshing(false);
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                Log.i("MainActivity", "Action-grant--");
                mBluetoothLeService.getSupportedGattServices();
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                final byte[] data = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);

                showMsg("Got string : " + new String(data));

                if (data != null && data.length > 0) {
                    final StringBuilder stringBuilder = new StringBuilder(data.length);
                    for (byte byteChar : data) {
                        char b = (char) byteChar;
                        stringBuilder.append(b);
                    }

                    Log.i("MainActivity","Get string : " + stringBuilder);
                }
            }
        }
    };

    private void alert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.add, null);

        builder.setView(view);
        builder.setTitle("Add WIFI")
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String text1 = wifi_input.getText().toString(); // WiFi SSID
                        String text2 = wifi_input2.getText().toString(); // WiFi Password
                        String text3 = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Firebase UID
                        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail(); // Firebase Email

                        // Assuming you have securely stored the user's password and can retrieve it here
                        String password = getUserPassword(); // Implement this function to retrieve the user's password securely

                        Log.i("Bluetooth activity", "wifi-usernamepassword");
                        StringBuilder stringBuilder = new StringBuilder();

                        if (!TextUtils.isEmpty(text1)) {
                            stringBuilder.append(text1).append(",");
                        }
                        if (!TextUtils.isEmpty(text2)) {
                            stringBuilder.append(text2).append(",");
                        }
                        if (!TextUtils.isEmpty(text3)) {
                            stringBuilder.append(text3).append(",");
                        }
                        if (!TextUtils.isEmpty(email)) {
                            stringBuilder.append(email).append(",");
                        }
                        if (!TextUtils.isEmpty(password)) {
                            stringBuilder.append(password);
                        }

                        String mergetext = stringBuilder.toString();

                        Toast.makeText(getBaseContext(), mergetext, Toast.LENGTH_SHORT).show();
                        btSendBytes(mergetext.getBytes());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

        wifi_input = view.findViewById(R.id.wifi_username);
        wifi_input2 = view.findViewById(R.id.wifi_password);

        dialog = builder.create();
    }

    // Implement this method to retrieve the user's password securely
    private String getUserPassword() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("password", ""); // Return the stored password
    }


    public void btSendBytes(byte[] data) {
        Log.i("btsend", "data recieved");
        Log.i("bts", data.toString());
        // mBluetoothLeService.writeCharacteristic(data);
        if (mBluetoothLeService != null &&
                mConnectionState.equals(BluetoothLeService.ACTION_GATT_CONNECTED)) {  //error line
            Log.i("btsend", "data if");
            mBluetoothLeService.writeCharacteristic(data);
            //  Log.i("btsend", data.toString());
        }
        Log.i("btsend", "data recieved");
    }

    private class MyBluetoothScanCallBack implements BluetoothScan.BluetoothScanCallBack {
        @Override
        public void onLeScanInitFailure(int failureCode) {
            Log.i("MainActivity", "onLeScanInitFailure()");
            switch (failureCode) {
                case BluetoothScan.SCAN_FEATURE_ERROR :
                    showMsg("scan_feature_error");
                    break;
                case BluetoothScan.SCAN_ADAPTER_ERROR :
                    showMsg("scan_adapter_error");
                    break;
                default:
                    showMsg("unKnow_error");
            }
        }

        @Override
        public void onLeScanInitSuccess(int successCode) {
            Log.i("MainActivity", "onLeScanInitSuccess()");
            switch (successCode) {
                case BluetoothScan.SCAN_BEGIN_SCAN :
                    Log.i("MainActivity","successCode : " + successCode);
                    break;
                case BluetoothScan.SCAN_NEED_ENADLE :
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    break;
                case BluetoothScan.AUTO_ENABLE_FAILURE :
                    showMsg("auto_enable_bluetooth_error");
                    break;
                default:
                    showMsg("unKnown_error");
            }
        }

        @Override
        public void onLeScanResult(BluetoothDevice device, int rssi, byte[] scanRecord) {
            // if(!mBluetoothDeviceList.contains(device) && device != null)
            if (device != null && device.getName() != null && device.getName().startsWith("ESP") && !mBluetoothDeviceList.contains(device)){
                mBluetoothDeviceList.add(device);
                mBluetoothDeviceAdapter.notifyDataSetChanged();

                Log.i("MainActivity","notifyDataSetChanged() " + "BluetoothName :　" + device.getName() +
                        "  BluetoothAddress :　" + device.getAddress());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_CANCELED) {
                showMsg("enable_bluetooth_error");
                return;
            } else if (resultCode == Activity.RESULT_OK) {
                if (mBluetoothDeviceList != null) {
                    mBluetoothDeviceList.clear();
                }
                scanLeDevice(true);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    static Toast toast = null;

    public static void showMsg(String msg) {
        try {
            if (toast == null) {
                toast = Toast.makeText(MyApplication.context(), msg, Toast.LENGTH_SHORT);
            } else {
                toast.setText(msg);
            }
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        // Create intent to launch PortActivity
        Intent intent = new Intent(this, PortActivity2.class);
        // Set flags to clear back stack and launch PortActivity
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        // Finish current activity
        finish();
    }




}