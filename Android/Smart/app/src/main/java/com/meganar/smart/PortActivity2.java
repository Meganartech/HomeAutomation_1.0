package com.meganar.smart;

import static com.meganar.smart.SessionManager.getThemeColor;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.smart.R;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class PortActivity2 extends AppCompatActivity {

    private static final String PREFS_NAME = "PairedDevicesPrefs";
    private static final String PAIRED_DEVICES_KEY = "paired_devices";
    private static final int REQUEST_BLUETOOTH_PERMISSION = 1;

    private TextView displayTitle;
    private BluetoothAdapter bluetoothAdapter;
    private GridLayout pairedDevicesGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeManager.setCustomizedThemes(this, getThemeColor(this));

        setContentView(R.layout.activity_port2);

        // Initialize BluetoothAdapter for BLE
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager != null) {
            bluetoothAdapter = bluetoothManager.getAdapter();
        }

        // Initialize views
        displayTitle = findViewById(R.id.display_title);
        pairedDevicesGrid = findViewById(R.id.paired_devices_grid);

        // Set initial text for display_title
        displayTitle.setText("Hello!");

        // Check Bluetooth permissions and proceed with Bluetooth functionality
        checkBluetoothPermissions();

        // Add click listener for the back button (assuming the id is 'imageButton2')
        ImageButton imageButton = findViewById(R.id.imageButton2);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Go back to the previous activity
            }
        });

        // Add click listener for the next button (assuming the id is 'imageButton')
        ImageButton imageButton2 = findViewById(R.id.imageButton);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PortActivity2.this, nextActivity1.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveButtonVisibility(); // Save the state of dynamically created buttons
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayPairedDevices(); // Restore the state of dynamically created buttons
    }

    private void saveButtonVisibility() {
        // Save the visibility of dynamically created buttons in SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("buttonCount", pairedDevicesGrid.getChildCount() / 2); // Divide by 2 because each device has 2 buttons
        for (int i = 0; i < pairedDevicesGrid.getChildCount(); i += 2) {
            Button deviceButton = (Button) pairedDevicesGrid.getChildAt(i);
            Button deleteButton = (Button) pairedDevicesGrid.getChildAt(i + 1);
            boolean isVisible = deviceButton.getVisibility() == View.VISIBLE;
            editor.putBoolean("button_" + i, isVisible);
        }
        editor.apply();
    }




    private void deletePairedDevice(String deviceName, String deviceAddress) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> pairedDevicesSet = prefs.getStringSet(PAIRED_DEVICES_KEY, new HashSet<>());

        // Create a new set to store the updated paired devices
        Set<String> updatedPairedDevicesSet = new HashSet<>(pairedDevicesSet);

        // Remove the specified device from the set
        updatedPairedDevicesSet.remove(deviceName + "," + deviceAddress);

        // Update the SharedPreferences with the new set of paired devices
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(PAIRED_DEVICES_KEY, updatedPairedDevicesSet);
        editor.apply();

        // Unpair the device
        unpairDevice(deviceAddress);
    }

    private void unpairDevice(String deviceAddress) {
        // Get the Bluetooth device by its MAC address
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
        try {
            // Use reflection to remove the bond between devices
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayPairedDevices() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> pairedDevicesSet = prefs.getStringSet(PAIRED_DEVICES_KEY, new HashSet<>());

        pairedDevicesGrid.removeAllViews(); // Clear existing views

        for (String pairedDevice : pairedDevicesSet) {
            // Split the paired device string into name and address
            String[] deviceParts = pairedDevice.split(",");
            if (deviceParts.length == 2) {
                String deviceName = deviceParts[0];
                String deviceAddress = deviceParts[1];
                addDeviceButton(deviceName, deviceAddress);
            }
        }
    }

    private void addDeviceButton(final String deviceName, final String deviceAddress) {
        // Create the device button
        Button deviceButton = new Button(this);
        deviceButton.setText(deviceName);
        deviceButton.setBackground(ContextCompat.getDrawable(this, R.drawable.button_background));
        GridLayout.LayoutParams deviceButtonParams = new GridLayout.LayoutParams();
        deviceButtonParams.width = GridLayout.LayoutParams.WRAP_CONTENT;
        deviceButtonParams.height = 150; // Set the height to increase the size
        deviceButtonParams.columnSpec = GridLayout.spec(0, 1, 1f);
        deviceButtonParams.setMargins(8, 8, 8, 8); // Add some margins for spacing
        deviceButton.setLayoutParams(deviceButtonParams);
        deviceButton.setPadding(20, 20, 20, 20); // Increase padding for better touch experience

        // Create the edit button
        Button editButton = new Button(this);
        editButton.setText("Edit");
        editButton.setBackground(ContextCompat.getDrawable(this, R.drawable.button_background));
        GridLayout.LayoutParams editButtonParams = new GridLayout.LayoutParams();
        editButtonParams.width = 20;
        editButtonParams.height = 150; // Set the height to match the device button
        editButtonParams.columnSpec = GridLayout.spec(1, 1, 1f);
        editButtonParams.setMargins(8, 8, 8, 8); // Add some margins for spacing
        editButton.setLayoutParams(editButtonParams);
        editButton.setPadding(20, 20, 20, 20); // Increase padding for better touch experience

        // Create the delete button
        Button deleteButton = new Button(this);
        deleteButton.setText("Delete");
        deleteButton.setBackground(ContextCompat.getDrawable(this, R.drawable.button_background));
        GridLayout.LayoutParams deleteButtonParams = new GridLayout.LayoutParams();
        deleteButtonParams.width = 20;
        deleteButtonParams.height = 150; // Set the height to match the device button
        deleteButtonParams.columnSpec = GridLayout.spec(2, 1, 1f);
        deleteButtonParams.setMargins(8, 8, 8, 8); // Add some margins for spacing
        deleteButton.setLayoutParams(deleteButtonParams);
        deleteButton.setPadding(20, 20, 20, 20); // Increase padding for better touch experience


//        // Set onClickListener for the device button
//        deviceButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int switchCount = getSwitchCount(deviceName);
//
//                Intent intent = new Intent(PortActivity2.this, AdddevicesActivity.class);
//                intent.putExtra("baseLabel", deviceName);
//                intent.putExtra("switchCount", switchCount);
//                startActivity(intent);
//            }
//        });

        // Set onClickListener for the device button
        deviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
                String updatedDeviceName = device.getName();
                int switchCount = getSwitchCount(updatedDeviceName);

                Intent intent = new Intent(PortActivity2.this, AdddevicesActivity.class);
                intent.putExtra("baseLabel", updatedDeviceName);
                intent.putExtra("switchCount", switchCount);
                startActivity(intent);
            }
        });

        // Set onClickListener for edit button
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(deviceName, deviceAddress, deviceButton);
            }
        });


        // Set onClickListener for delete button
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(deviceName, deviceAddress);
            }
        });

        // Add buttons to the grid layout
        pairedDevicesGrid.addView(deviceButton);
        pairedDevicesGrid.addView(editButton);
        pairedDevicesGrid.addView(deleteButton);
    }


    private void showEditDialog(final String deviceName, final String deviceAddress, final Button deviceButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Device Name");

        // Set up the input
        final EditText input = new EditText(this);
        input.setText(deviceName);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newDeviceName = input.getText().toString();
                if (!newDeviceName.isEmpty()) {
                    updateDeviceName(deviceName, deviceAddress, newDeviceName);
                    deviceButton.setText(newDeviceName); // Update button text
                } else {
                    showToast("Device name cannot be empty");
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void updateDeviceName(String oldDeviceName, String deviceAddress, String newDeviceName) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> pairedDevicesSet = prefs.getStringSet(PAIRED_DEVICES_KEY, new HashSet<>());

        // Create a new set to store the updated paired devices
        Set<String> updatedPairedDevicesSet = new HashSet<>(pairedDevicesSet);

        // Remove the old device entry
        updatedPairedDevicesSet.remove(oldDeviceName + "," + deviceAddress);
        // Add the new device entry
        updatedPairedDevicesSet.add(newDeviceName + "," + deviceAddress);

        // Update the SharedPreferences with the new set of paired devices
        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(PAIRED_DEVICES_KEY, updatedPairedDevicesSet);
        editor.apply();
    }


    private int getSwitchCount(String deviceName) {
        if (deviceName.startsWith("ESP1")) {
            return 1;
        } else if (deviceName.startsWith("ESP2")) {
            return 2;
        } else if (deviceName.startsWith("ESP3")) {
            return 3;
        } else {
            return 0;
        }
    }

    private void checkBluetoothPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN},
                    REQUEST_BLUETOOTH_PERMISSION);
        } else {
            // Permission already granted, proceed with Bluetooth functionality
            displayPairedDevices();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with Bluetooth functionality
                displayPairedDevices();
            } else {
                // Permission denied, handle accordingly
                showToast("Bluetooth permission denied");
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showDeleteConfirmationDialog(final String deviceName, final String deviceAddress) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this paired device?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deletePairedDevice(deviceName, deviceAddress);
                        displayPairedDevices(); // Refresh the paired devices list
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked the No button, do nothing
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
