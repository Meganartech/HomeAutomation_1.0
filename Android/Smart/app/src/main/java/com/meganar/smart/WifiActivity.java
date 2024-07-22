package com.meganar.smart;

import static com.meganar.smart.SessionManager.getThemeColor;
import static com.meganar.smart.ThemeManager.setCustomizedThemes;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.example.smart.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.HashMap;
import java.util.Map;

public class WifiActivity extends AppCompatActivity {

    private EditText wifi_name,wifi_pass;
    private Button connect;
    private  String wifi,pass,notify1;
    private NotificationManagerCompat notificationManager;
    private final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
    private DatabaseReference database;
    ImageView wifibck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomizedThemes(this, getThemeColor(this));
        setContentView(R.layout.activity_wifi);

        wifi_name = findViewById(R.id.wifi_name);
        wifi_pass = findViewById(R.id.wifi_password);
        connect = findViewById(R.id.wifi_nxt);
        wifibck = findViewById(R.id.wifiback);
        database = FirebaseDatabase.getInstance().getReference("users/"+currentFirebaseUser.getUid());
        database.child("Settings").addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                    notify1 = map.get("Notification");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User", databaseError.getMessage());
            }
        });
        wifibck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifiback();
            }
        });
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.child("Wifi").addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            wifi = wifi_name.getText().toString();
                            pass = wifi_pass.getText().toString();
                            // if (validateInputName(wifi, pass)) {
                                HashMap<String, String> mapconnect = new HashMap<String, String>();
                                mapconnect.put("wifi", wifi);
                                mapconnect.put("password", pass);
                                dataSnapshot.getRef().setValue(mapconnect).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if (validateInputName(wifi, pass)) {
                                            Toast.makeText(getBaseContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                                            //DynamicToast.makeSuccess(WifiActivity.this, "updated", 5).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        Toast.makeText(getBaseContext(), "Try Again!", Toast.LENGTH_SHORT).show();
                                        //DynamicToast.makeError(WifiActivity.this, "Try again!", 5).show();
                                    }
                                });
                            //}
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("User", databaseError.getMessage());
                    }
                });
            }
        });
    }
    private void wifiback() {
        Intent indent =new Intent(this,Smart1Activity.class);
        startActivity(indent);
    }
    private boolean validateInputName(String wifi, String pass) {
        if(wifi.isEmpty()){
            wifi_name.setError("This field can't be empty");
            return false;
        }
        if(pass.isEmpty()){
            wifi_pass.setError("This field can't be empty");
            return false;
        }
        return true;
    }
}