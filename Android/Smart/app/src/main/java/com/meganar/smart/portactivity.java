package com.meganar.smart;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import com.example.smart.R;

public class portactivity extends AppCompatActivity {
    private long pressedTime;

    private TextView displayTitle;
    private Button kitchen_btn;
    private Button hall_btn;
    private Button room_btn;
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_port_activity);

        displayTitle = findViewById(R.id.display_title);
        kitchen_btn = findViewById(R.id.button);
        hall_btn = findViewById(R.id.button2);
        room_btn = findViewById(R.id.button3);

        // Setting up Bluetooth adapter
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        // Setting click listeners
        ImageView adddevback = findViewById(R.id.adddevback);
        adddevback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to home activity
                Intent intent = new Intent(portactivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        kitchen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AddDevice2 activity
                Intent intent = new Intent(portactivity.this, AdddevicesActivity2.class);
                startActivity(intent);
            }
        });

        hall_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AddDevice activity
                Intent intent = new Intent(portactivity.this, AdddevicesActivity.class);
                startActivity(intent);
            }
        });

        room_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AddDevice3 activity
                Intent intent = new Intent(portactivity.this, AdddevicesActivity3.class);
                startActivity(intent);
            }
        });
    }

    // Handle back button press
    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finishAffinity();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }
}
