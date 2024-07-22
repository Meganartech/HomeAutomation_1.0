package com.meganar.smart;

import static com.meganar.smart.SessionManager.getThemeColor;
import static com.meganar.smart.ThemeManager.setCustomizedThemes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smart.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private long pressedTime;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomizedThemes(this,getThemeColor(this));
        setContentView(R.layout.activity_home);
        BottomNavigationView bottomNavigationView =findViewById(R.id.home_nav);
        getSupportFragmentManager();
        Button button_add = findViewById(R.id.add1);
        bottomNavigationView.setSelectedItemId(R.id.home_menu);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView imageView = findViewById(R.id.add_schedular);


        Button addbtn2 = findViewById(R.id.add2);

        addbtn2.setOnClickListener(view -> openschedular());

        imageView.setOnClickListener(view -> openschedular());
        button_add.setOnClickListener(view -> openadd());



        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.home_menu:
                    return true;
                case R.id.smart_menu:
                    startActivity(new Intent(getApplicationContext(),Smart1Activity.class));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.profile_menu:
                    startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
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

    private void openadd() {
        Intent indent =new Intent(this,PortActivity2.class);
            startActivity(indent);
    }

    private void openschedular() {
        Intent indent = new Intent(this,SchedularActivity.class);
        startActivity(indent);

    }
}