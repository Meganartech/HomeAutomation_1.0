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


public class UpdatedeviceActivity extends AppCompatActivity {
    private EditText et1,et2,et3,et4;
    private Button btn1,btn2,btn3,btn4;
    ImageView ub;
    private final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
    private DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomizedThemes(this,getThemeColor(this));
        setContentView(R.layout.activity_updatedevice);
        et1 = findViewById(R.id.port1update);
        et2 = findViewById(R.id.port2update);
        et3 = findViewById(R.id.port3update);
        et4 = findViewById(R.id.port4update);
        btn1 = findViewById(R.id.setport1);
        btn2 = findViewById(R.id.setport2);
        btn3 = findViewById(R.id.setport3);
        btn4 = findViewById(R.id.setport4);

        ub = findViewById(R.id.update_device_back);
        database = FirebaseDatabase.getInstance().getReference("users/"+currentFirebaseUser.getUid());
        ub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opensettings();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.child("Devicename").addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                et1.getText().toString().trim();
                                    dataSnapshot.getRef().child("port1").setValue(et1.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getBaseContext(), "Name Updated Successfully", Toast.LENGTH_SHORT).show();
                                            //DynamicToast.makeSuccess(UpdatedeviceActivity.this, "Name Updated Successfully!", 10).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            Toast.makeText(getBaseContext(), "Try Again!", Toast.LENGTH_SHORT).show();
                                            //DynamicToast.makeError(UpdatedeviceActivity.this, "Error Occured!", 10).show();
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
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.child("Devicename").addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            et2.getText().toString().trim();
                                dataSnapshot.getRef().child("port2").setValue(et2.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {

                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(getBaseContext(), "Name Updated Successfully", Toast.LENGTH_SHORT).show();
                                      //  DynamicToast.makeSuccess(UpdatedeviceActivity.this, "Name Updated Successfully!", 10).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        Toast.makeText(getBaseContext(), "Try Again!", Toast.LENGTH_SHORT).show();
                                        //DynamicToast.makeError(UpdatedeviceActivity.this, "Error Occured!", 10).show();
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
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database.child("Devicename").addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            et3.getText().toString().trim();
                                dataSnapshot.getRef().child("port3").setValue(et3.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {

                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(getBaseContext(), "Name Updated Successfully", Toast.LENGTH_SHORT).show();
                                        //DynamicToast.makeSuccess(UpdatedeviceActivity.this, "Name Updated Successfully!", 10).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        Toast.makeText(getBaseContext(), "Try Again!", Toast.LENGTH_SHORT).show();
                                        //DynamicToast.makeError(UpdatedeviceActivity.this, "Error Occured!", 10).show();
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
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database.child("Devicename").addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            et4.getText().toString().trim();
                                dataSnapshot.getRef().child("port4").setValue(et4.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {

                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(getBaseContext(), "Name Updated Successfully!", Toast.LENGTH_SHORT).show();
                                        //DynamicToast.makeSuccess(UpdatedeviceActivity.this, "Name Updated Successfully!", 10).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        Toast.makeText(getBaseContext(), "Try Again!", Toast.LENGTH_SHORT).show();
                                      //  DynamicToast.makeError(UpdatedeviceActivity.this, "Error Occured!", 10).show();
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
        });
    }

    private void opensettings() {
        Intent indent =new Intent(this,SettingsActivity.class);
        startActivity(indent);
    }


}