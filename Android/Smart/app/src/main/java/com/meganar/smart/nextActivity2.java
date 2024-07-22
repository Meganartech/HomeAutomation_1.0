package com.meganar.smart;

import static com.meganar.smart.SessionManager.getThemeColor;
import static com.meganar.smart.ThemeManager.setCustomizedThemes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView; // Added import for ImageView
import com.bumptech.glide.Glide; // Added import for Glide
import com.example.smart.R;

import androidx.appcompat.app.AppCompatActivity;

public class nextActivity2 extends AppCompatActivity {

    private long pressedTime;
    private Button ble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomizedThemes(this, getThemeColor(this));

        setContentView(R.layout.activity_next2); // Corrected layout file name
        ble = findViewById(R.id.button10);

        ImageButton imageButton3 = findViewById(R.id.imageButton3);
        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Go back to the previous activity
            }
        });
        ble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBluetooth();
            }
        });

        // Load GIF into ImageView using Glide
        ImageView imageView = findViewById(R.id.imageView3);
        Glide.with(this).load(R.drawable.bluetoothscan).into(imageView);


    }

    private void openBluetooth() {
        Intent intent = new Intent(this, BluetoothActivity.class);
        startActivity(intent);
    }
}
