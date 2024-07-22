package com.meganar.smart;

import static com.meganar.smart.SessionManager.getThemeColor;
import static com.meganar.smart.ThemeManager.setCustomizedThemes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smart.R;


public class nextActivity1 extends AppCompatActivity {

    private long pressedTime;
    private Button ble;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomizedThemes(this,getThemeColor(this));

        setContentView(R.layout.activity_next1);
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
                openbluetooth();
            }
        });


    }


    private void openbluetooth(){
        Intent indent = new Intent(this,nextActivity2.class);
        startActivity(indent);
    }
}