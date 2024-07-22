package com.meganar.smart;

import static com.meganar.smart.SessionManager.getThemeColor;
import static com.meganar.smart.ThemeManager.setCustomizedThemes;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smart.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Smart1Activity extends AppCompatActivity {
    private Button wifibtn;
    private Button ble;
    private long pressedTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomizedThemes(this,getThemeColor(this));
        setContentView(R.layout.activity_smart1);
        wifibtn = findViewById(R.id.wifi);
        ble = findViewById(R.id.bluetooth);
        wifibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openwifi();
            }
        });
        ble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openbluetooth();
            }
        });

        BottomNavigationView bottomNavigationView =findViewById(R.id.sma_nav);
        bottomNavigationView.setSelectedItemId(R.id.smart_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home_menu:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.smart_menu:

                        return true;
                    case R.id.profile_menu:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            this.finishAffinity();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }
    private void openbluetooth(){
        Intent indent = new Intent(this,BluetoothActivity.class);
        startActivity(indent);
    }
    private void openwifi() {
        Intent indent =new Intent(this,WifiActivity.class);
        startActivity(indent);
    }
}