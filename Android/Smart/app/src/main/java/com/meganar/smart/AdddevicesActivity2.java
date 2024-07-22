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

public class AdddevicesActivity2 extends AppCompatActivity {

    private SwitchCompat switch5, switch6;
    private final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference database;
    private TextView t5, t6;
    private ImageView back1, add_1;

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomizedThemes(this, getThemeColor(this));
        setContentView(R.layout.activity_adddevices2);
        switch5 = findViewById(R.id.switch5);
        switch6 = findViewById(R.id.switch6);
        back1 = findViewById(R.id.adddevback);
        builddialog();
        add_1 = findViewById(R.id.add_dev_1);

        add_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        t5 = findViewById(R.id.port222); // Assuming these are the TextViews for displaying port names
        t6 = findViewById(R.id.p223);

        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdddevicesActivity2.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        database = FirebaseDatabase.getInstance().getReference("users/" + currentFirebaseUser.getUid());
        database.child("components").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {


                    String p5 = dataSnapshot.child("port5").getValue(String.class); // Get the value directly
                    String p6 = dataSnapshot.child("port6").getValue(String.class); // Get the value directly

                    switch5.setChecked("1".equals(p5)); // Check if the port is set to "1"
                    switch6.setChecked("1".equals(p6)); // Check if the port is set to "1"


                    switch5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            dataSnapshot.getRef().child("port5").setValue(isChecked ? "1" : "0"); // Update the value based on switch state
                        }
                    });

                    switch6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            dataSnapshot.getRef().child("port6").setValue(isChecked ? "1" : "0"); // Update the value based on switch state
                        }
                    });

                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User", databaseError.getMessage());
            }
        });

      
    }


    private void builddialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.add, null);

        builder.setView(view);
        builder.setTitle("Add Devices")
                .setPositiveButton("Add Devices", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Handle adding devices here
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        dialog = builder.create();
    }
}
