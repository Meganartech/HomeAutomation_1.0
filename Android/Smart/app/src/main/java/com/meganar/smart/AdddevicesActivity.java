package com.meganar.smart;

import static com.meganar.smart.SessionManager.getThemeColor;

import static com.meganar.smart.ThemeManager.setCustomizedThemes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.smart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class AdddevicesActivity extends AppCompatActivity {

    private DatabaseReference database;
    private FirebaseUser currentFirebaseUser;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomizedThemes(this, getThemeColor(this));
        setContentView(R.layout.activity_adddevices);

        // Initialize layout and Firebase reference
        linearLayout = findViewById(R.id.dynamic_layout);
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference("users/" + currentFirebaseUser.getUid());

        // Get base label and switch count from intent
        String baseLabel = getIntent().getStringExtra("baseLabel");
        int switchCount = getIntent().getIntExtra("switchCount", 0);

        // Create switches dynamically
        for (int i = 1; i <= switchCount; i++) {
            SwitchCompat aSwitch = new SwitchCompat(this);
            aSwitch.setText(baseLabel + "." + i);
            linearLayout.addView(aSwitch);

            // Set listener for each switch
            final String portKey = createValidFirebaseKey(baseLabel + "_" + i);
            aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> updatePortValue(portKey, isChecked));
        }

        // Back button logic
        findViewById(R.id.adddevback).setOnClickListener(view -> {
            Intent intent = new Intent(AdddevicesActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        // Add device button logic
        findViewById(R.id.add_dev_1).setOnClickListener(view -> {
            // Handle adding a new device
            buildDialog().show();
        });
    }

    private void updatePortValue(String portKey, boolean value) {
        database.child("components").child(portKey).setValue(value ? "1" : "0");
    }

    private String createValidFirebaseKey(String input) {
        return input.replace(".", "_").replace("#", "_")
                .replace("$", "_").replace("[", "_")
                .replace("]", "_");
    }

    private AlertDialog buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.add, null);
        builder.setView(view)
                .setTitle("Add Devices")
                .setPositiveButton("Add Devices", (dialogInterface, i) -> {
                    // Handle adding a device
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    // Handle cancel action
                });
        return builder.create();
    }
}