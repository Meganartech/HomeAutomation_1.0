package com.meganar.smart;

import static com.meganar.smart.SessionManager.getThemeColor;
import static com.meganar.smart.ThemeManager.setCustomizedThemes;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText oldpass, newpass, up;
    private Button cp;
    private ProgressBar progressBar;
    private final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference database;
    private long pressedTime;
    ImageView backbtn;
    String curr;

    @Override



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomizedThemes(this, getThemeColor(this));
        setContentView(R.layout.activity_change_password);

        database = FirebaseDatabase.getInstance().getReference("users/" + currentFirebaseUser.getUid());


        firebaseAuth = FirebaseAuth.getInstance();
        oldpass = findViewById(R.id.old_pass);
        newpass = findViewById(R.id.new_pass);
        cp = findViewById(R.id.cpbtn);
        progressBar = findViewById(R.id.prog2);

        backbtn = findViewById(R.id.back_cp);

        oldpass.setHint("Current Password");
        newpass.setHint("New Password");

        oldpass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    oldpass.setHint("");
                else
                    oldpass.setHint("Current Password");
            }
        });


        newpass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    newpass.setHint("");
                else
                    newpass.setHint("New Password");
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ChangePasswordActivity.this, SettingsActivity.class));
            }
        });


        database = FirebaseDatabase.getInstance().getReference("users/" + currentFirebaseUser.getUid());


        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();

                    curr = map.get("password");

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User", databaseError.getMessage());
            }
        });
        cp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  if(oldpass.getText().toString().equals(curr)){

                if (!TextUtils.isEmpty(oldpass.getText().toString().trim()) && !TextUtils.isEmpty(newpass.getText().toString().trim())) {
                    firebaseAuth.getCurrentUser().updatePassword(newpass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if (!newpass.getText().toString().equals(curr)) {
                                    database.child("password").addValueEventListener(new ValueEventListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.N)
                                        @Override
                                        public void onDataChange(final DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.exists()) {
                                                newpass.getText().toString().trim();
                                                {
                                                    dataSnapshot.getRef().setValue(newpass.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                       // @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(getBaseContext(), "Password Updated Successfully", Toast.LENGTH_SHORT).show();
                                                           // DynamicToast.makeSuccess(ChangePasswordActivity.this, "Password Updated Successfully!", 10).show();
                                                            firebaseAuth.signOut();
                                                            startActivity(new Intent(ChangePasswordActivity.this, LoginscreenActivity.class));
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getBaseContext(), "Something Went Wrong Try Again !", Toast.LENGTH_SHORT).show();
                                                            //DynamicToast.makeError(ChangePasswordActivity.this, "Error Occured!", 10).show();
                                                        }
                                                    });
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Log.d("User", databaseError.getMessage());
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(getBaseContext(), "Please Logout and Login Again", Toast.LENGTH_SHORT).show();
                               // DynamicToast.makeWarning(ChangePasswordActivity.this,"Please Logout and Login Again!", 10).show();

                            }
                        }
                    });
                }

            }
        });


    }
}
