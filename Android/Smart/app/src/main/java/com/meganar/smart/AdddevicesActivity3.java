package com.meganar.smart;

import static com.meganar.smart.SessionManager.getThemeColor;
import static com.meganar.smart.ThemeManager.setCustomizedThemes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

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

public class AdddevicesActivity3 extends AppCompatActivity {

    private SwitchCompat switch7;
    private final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference database;
    private TextView t7;
    private ImageView back1, add_1;

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomizedThemes(this, getThemeColor(this));
        setContentView(R.layout.activity_adddevices3); // Changed to the correct layout file name
        switch7 = findViewById(R.id.switch7);
        back1 = findViewById(R.id.adddevback);
        builddialog();
        add_1 = findViewById(R.id.add_dev_1);

        add_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        t7 = findViewById(R.id.port333); // Changed to the correct TextView ID

        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdddevicesActivity3.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        database = FirebaseDatabase.getInstance().getReference("users/" + currentFirebaseUser.getUid());
        database.child("components").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String p7 = dataSnapshot.child("port7").getValue(String.class); // Get the value directly
                    switch7.setChecked("1".equals(p7)); // Check if the port is set to "1"

                    switch7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            dataSnapshot.getRef().child("port7").setValue(isChecked ? "1" : "0"); // Update the value based on switch state
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User", databaseError.getMessage());
            }
        });

        // No need for ValueEventListener to display the port value as it's already set in the layout file
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
