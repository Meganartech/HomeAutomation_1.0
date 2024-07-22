package com.meganar.smart;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.smart.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;

public class ProfileActivity extends AppCompatActivity {
    private SessionManager session;
    private FirebaseAuth firebaseAuth;
    TextView namedisplay,emaildisplay;
    private String n,e;

    private final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference database;
    private long pressedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeManager.setCustomizedThemes(this, SessionManager.getThemeColor(this));
        setContentView(R.layout.activity_profile);

        ImageView right =findViewById(R.id.settings);
        session = new SessionManager(getApplicationContext());


        firebaseAuth = FirebaseAuth.getInstance();

        CardView card =findViewById(R.id.ca4);
        CardView card0 =findViewById(R.id.ca1);
        CardView card2 =findViewById(R.id.ca3);
        CardView card1 =findViewById(R.id.car);
        CardView cardView5 = findViewById(R.id.ca5);
        CardView msg = findViewById(R.id.ca2_msg);

        namedisplay =findViewById(R.id.text_view_name);
      emaildisplay =findViewById(R.id.text_view_email);
        BottomNavigationView bottomNavigationView =findViewById(R.id.prof_nav);

       database = FirebaseDatabase.getInstance().getReference("users/"+currentFirebaseUser.getUid());

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();

                    n =  map.get("name");
                    e = map.get("email");
                    namedisplay.setText(n);
                    emaildisplay.setText(e);

                }

                else{
                    namedisplay.setText("********");
                    emaildisplay.setText("*******");
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User", databaseError.getMessage());
            }
        });


        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                report();
            }
        });
        card0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openweather();
            }
        });
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openedit();
            }
        });

        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opensetting();
            }
        });

     msg.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             openmsg();
         }
     });


        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              logout();

            }
        });

       right.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               opensetting();
           }
       });



        getSupportFragmentManager();
              bottomNavigationView.setSelectedItemId(R.id.profile_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home_menu:
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.smart_menu:
                        startActivity(new Intent(getApplicationContext(),Smart1Activity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile_menu:
                        return true;
                }
                return false;
            }
        });
    }

    private void openedit() {
        Intent indent =new Intent(this,EditprofileActivity.class);
        startActivity(indent);
    }
    private void openweather() {
        Intent indent =new Intent(this,WeatherActivity.class);
        startActivity(indent);
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

    private void openmsg() {
        Intent indent =new Intent(this,MessageActivity.class);
        startActivity(indent);
    }

    private void report() {
        Intent indent =new Intent(this,ReportActivity.class);
        startActivity(indent);
    }

    private void logout() {
        MaterialDialog mDialog = new MaterialDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Do you really want to Logout?")
                .setCancelable(false)
                .setPositiveButton("Logout", R.mipmap.logout, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        firebaseAuth.signOut();
                        finish();

                        dialogInterface.dismiss();
                        startActivity(new Intent(ProfileActivity.this, LoginscreenActivity.class));

                    }

                })
                .setNegativeButton("Cancel", R.mipmap.close, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {

                        dialogInterface.dismiss();
                    }
                })
                .build();

        mDialog.show();
    }

    private void opensetting() {
        Intent indent =new Intent(this,SettingsActivity.class);
        startActivity(indent);

    }
    private void openlogin() {
        Intent indent =new Intent(this,LoginscreenActivity.class);
        startActivity(indent);

    }
}

