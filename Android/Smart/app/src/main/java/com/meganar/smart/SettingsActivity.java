package com.meganar.smart;

import static com.meganar.smart.SessionManager.getThemeColor;
import static com.meganar.smart.SessionManager.setThemeColor;
import static com.meganar.smart.ThemeManager.setCustomizedThemes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.smart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Map;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;

public class SettingsActivity extends AppCompatActivity   {

    ImageView imageView;

  private TextView textView1,textView2,text_logout;
  private CardView cardback,delete,updatedev;
  private  FirebaseAuth firebaseAuth;
  private FirebaseUser  user;
    private final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
    private DatabaseReference database;
    private long pressedTime;
    SharedPreferences.Editor editor;
    private Switch nswitch1;
    ImageView setback;
    String e,p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomizedThemes(this,getThemeColor(this));
        setContentView(R.layout.activity_settings);
       // imageView = findViewById(R.id.settings_back);
        text_logout = findViewById(R.id.quit);
        textView1 =findViewById(R.id.cusername);
        textView2 = findViewById(R.id.cp);
        cardback = findViewById(R.id.back_info_ca3);
        delete = findViewById(R.id.delete_account_ca3);

        setback = findViewById(R.id.settingsback);

        nswitch1 = findViewById(R.id.switch5);

        updatedev = findViewById(R.id.ch_dev_name);

        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openedit();

            }
        });



        database = FirebaseDatabase.getInstance().getReference("users/"+currentFirebaseUser.getUid());

        database.child("Settings").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    String n1 = (String) map.get("Notification");


                    nswitch1.setChecked(n1.equals("ON"));


                    nswitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            if (nswitch1.isChecked()) {

                                dataSnapshot.getRef().child("Notification").setValue("ON");

                            } else {

                                dataSnapshot.getRef().child("Notification").setValue("OFF");

                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User", databaseError.getMessage());
            }
        });





        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();


                    e = map.get("email");
                   p = map.get("Password");



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User", databaseError.getMessage());
            }
        });


        setback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openprofile();
            }
        });

        updatedev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openupdate();
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opencp();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               deleteaccount();
            }
        });

        text_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });



       cardback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openprofile();
            }
        });


    }

    private void deleteaccount1() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
               AuthCredential credential = EmailAuthProvider
                .getCredential(e, p);
              user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SettingsActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                });
    }


    public void chooseColor(View view) {
        DialogManager.showCustomAlertDialog(this ,new ColorDialogCallback() {
            @Override
            public void onChosen(String chosenColor) {
                if(chosenColor.equals(getThemeColor(getApplicationContext()))){
                    Toast.makeText(SettingsActivity.this, "Theme has already chosen", Toast.LENGTH_SHORT).show();
                    return;
                }
                setThemeColor(getApplicationContext(), chosenColor);
                setCustomizedThemes(getApplicationContext(), chosenColor);
                //Toast.makeText(SettingsActivity.this, chosenColor, Toast.LENGTH_SHORT).show();
                recreate();



                database.child("Settings").addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                                        dataSnapshot.getRef().child("themecolor").setValue(chosenColor);

                                    }
                        }
                    //}

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("User", databaseError.getMessage());
                    }
                });
            }
        });
    }

    private void openedit() {
        Intent indent =new Intent(this,EditprofileActivity.class);
        startActivity(indent);
    }

    private void openupdate() {
        Intent indent =new Intent(this,UpdatedeviceActivity.class);
        startActivity(indent);
    }

    private void deleteaccount() {
        MaterialDialog mDialog = new MaterialDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Do you really want to Delete Account?")
                .setCancelable(false)
                .setPositiveButton("Delete", R.mipmap.logout, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(e, p);
                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {

                                    public void onComplete(@NonNull Task<Void> task) {
                                        user.delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(getBaseContext(), "Account Deleted Successfully", Toast.LENGTH_SHORT).show();
                                                          //  DynamicToast.makeSuccess(getApplicationContext(), "Account Deleted Successfully", 10).show();
                                                            firebaseAuth.signOut();
                                                            user.delete();
                                                            dialogInterface.dismiss();
                                                            Intent intent = new Intent(SettingsActivity.this, LoginscreenActivity.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);     //clear activity
                                                            startActivity(intent);
                                                        }
                                                    }
                                                });
                                    }
                                });
                    }
                })

                .setNegativeButton("Cancel", R.mipmap.close, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                       // Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getBaseContext(), "CANCELLED!", Toast.LENGTH_SHORT).show();
                       // DynamicToast.makeError(getApplicationContext(), "CANCELLED", 10).show();
                        dialogInterface.dismiss();
                    }
                })
                .build();
        mDialog.show();
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
                        startActivity(new Intent(SettingsActivity.this, LoginscreenActivity.class));

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

    private void opencp() {
        Intent indent =new Intent(this,ChangePasswordActivity.class);
        startActivity(indent);
    }

    private void openprofile() {
        Intent indent =new Intent(this,ProfileActivity.class);
        startActivity(indent);
    }
}