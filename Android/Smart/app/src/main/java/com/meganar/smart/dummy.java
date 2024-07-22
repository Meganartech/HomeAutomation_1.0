package com.meganar.smart;

import static com.meganar.smart.SessionManager.getThemeColor;
import static com.meganar.smart.ThemeManager.setCustomizedThemes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.smart.R;

public class dummy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomizedThemes(this, getThemeColor(this));
        setContentView(R.layout.activity_dummy);
    }
}