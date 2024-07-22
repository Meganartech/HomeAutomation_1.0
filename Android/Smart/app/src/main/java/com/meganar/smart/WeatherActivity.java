package com.meganar.smart;

import static com.meganar.smart.SessionManager.getThemeColor;
import static com.meganar.smart.ThemeManager.setCustomizedThemes;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class WeatherActivity extends AppCompatActivity {
    private final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
    private DatabaseReference database;
    String locat,humi,tem,pre,airspeed;
    TextView loc,humidity,temp,press,speed;
    ImageView bw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomizedThemes(this, getThemeColor(this));
        setContentView(R.layout.activity_weather);

        loc =  findViewById(R.id.location);
        temp = findViewById(R.id.temperature);
        humidity = findViewById(R.id.humidity);
        press = findViewById(R.id.presure);
        speed = findViewById(R.id.windspeed);
        bw = findViewById(R.id.weather_back);


        bw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WeatherActivity.this, ProfileActivity.class));
            }
        });
        database = FirebaseDatabase.getInstance().getReference("users/"+currentFirebaseUser.getUid());
        database.child("Weather").addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();

                    tem = String.valueOf(map.get("Temperature"));
                    locat = String.valueOf(map.get("Location"));
                    humi = String.valueOf(map.get("Humidity"));
                    airspeed = String.valueOf(map.get("Wind speed"));
                    pre = String.valueOf(map.get("Pressure"));

                    loc.setText(locat);
                    temp.setText(tem+" °C");
                    humidity.setText(humi+" %");
                    press.setText(pre+" hPa");
                    speed.setText(airspeed+" m/s");
                }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("User", databaseError.getMessage());
                }
            });

    }

}