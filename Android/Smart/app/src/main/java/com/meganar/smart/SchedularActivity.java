package com.meganar.smart;

import static com.meganar.smart.SessionManager.getThemeColor;
import static com.meganar.smart.ThemeManager.setCustomizedThemes;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SchedularActivity<database> extends AppCompatActivity {
    private TextView startTimeP1, startTimeP2, startTimeP3, startTimeP4;
    private TextView stopTimeP1, stopTimeP2, stopTimeP3, stopTimeP4;
    private Button finalButton1, finalButton2, finalButton3, finalButton4;

    private TimePickerDialog startPicker1, startPicker2, startPicker3, startPicker4,
            stopPicker1, stopPicker2, stopPicker3, stopPicker4;
    String s1,e1,s2,e3,s4,e4,e2,s3,p1,p2,p3,p4,notify1;

    private final FirebaseUser currentFirebaseUser  = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference database;
    private TextView t11,t22,t33,t44;
    private ImageView back;
    MessageActivity additem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomizedThemes(this,getThemeColor(this));
        setContentView(R.layout.activity_schedular);

         additem = new MessageActivity();

        database = FirebaseDatabase.getInstance().getReference("users/"+currentFirebaseUser.getUid());



        back = findViewById(R.id.sechedularback);
        startTimeP1 = findViewById(R.id.startTime1);
        startTimeP2 = findViewById(R.id.startTime2);
        startTimeP3 = findViewById(R.id.startTime3);
        startTimeP4 = findViewById(R.id.startTime4);

        stopTimeP1 = findViewById(R.id.stopTime1);
        stopTimeP2 = findViewById(R.id.stopTime2);
        stopTimeP3 = findViewById(R.id.stopTime3);
        stopTimeP4 = findViewById(R.id.stopTime4);

        finalButton1 = findViewById(R.id.buttonUp1);
        finalButton2 = findViewById(R.id.buttonUp2);
        finalButton3 = findViewById(R.id.buttonUp3);
        finalButton4 = findViewById(R.id.buttonUp4);

        t11 = findViewById(R.id.tv1);
        t22 = findViewById(R.id.tv2);
        t33 = findViewById(R.id.tv3);
        t44 = findViewById(R.id.tv4);

        final Calendar cldr = Calendar.getInstance();
        final int hour = cldr.get(Calendar.HOUR_OF_DAY);
        final int minutes = cldr.get(Calendar.MINUTE);

        startTimeP1.setText(utilTime(hour) + ":" + utilTime(minutes));
        startTimeP2.setText(utilTime(hour) + ":" + utilTime(minutes));
        startTimeP3.setText(utilTime(hour) + ":" + utilTime(minutes));
        startTimeP4.setText(utilTime(hour) + ":" + utilTime(minutes));

        stopTimeP1.setText(utilTime(hour) + ":" + utilTime(minutes));
        stopTimeP2.setText(utilTime(hour) + ":" + utilTime(minutes));
        stopTimeP3.setText(utilTime(hour) + ":" + utilTime(minutes));
        stopTimeP4.setText(utilTime(hour) + ":" + utilTime(minutes));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent indent =new Intent(SchedularActivity.this,HomeActivity.class);
                startActivity(indent);
            }
        });

        startTimeP1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startPicker1 = new TimePickerDialog(SchedularActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                               startTimeP1.setText(utilTime(sHour) + ":" + utilTime(sMinute));
                            }
                        }, hour, minutes, true);
                startPicker1.show();
            }
        });

      startTimeP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startPicker2 = new TimePickerDialog(SchedularActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                startTimeP2.setText(utilTime(sHour) + ":" + utilTime(sMinute));
                            }
                        }, hour, minutes, true);
                startPicker2.show();
            }
        });

        startTimeP3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startPicker3 = new TimePickerDialog(SchedularActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                startTimeP3.setText(utilTime(sHour) + ":" + utilTime(sMinute));
                            }
                        }, hour, minutes, true);
                startPicker3.show();
            }
        });

        startTimeP4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startPicker4 = new TimePickerDialog(SchedularActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                startTimeP4.setText(utilTime(sHour) + ":" + utilTime(sMinute));
                            }
                        }, hour, minutes, true);
                startPicker4.show();
            }
        });

        stopTimeP1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopPicker1 = new TimePickerDialog(SchedularActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                stopTimeP1.setText(utilTime(sHour) + ":" + utilTime(sMinute));
                            }
                        }, hour, minutes, true);
                stopPicker1.show();
            }
        });

       stopTimeP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopPicker2 = new TimePickerDialog(SchedularActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                stopTimeP2.setText(utilTime(sHour) + ":" + utilTime(sMinute));
                            }
                        }, hour, minutes, true);
                stopPicker2.show();
            }
        });

        stopTimeP3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopPicker3 = new TimePickerDialog(SchedularActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                stopTimeP3.setText(utilTime(sHour) + ":" + utilTime(sMinute));
                            }
                        }, hour, minutes, true);
                stopPicker3.show();
            }
        });

        stopTimeP4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopPicker4 = new TimePickerDialog(SchedularActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                stopTimeP4.setText(utilTime(sHour) + ":" + utilTime(sMinute));
                            }
                        }, hour, minutes, true);
                stopPicker4.show();
            }
        });

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

        database.child("Devicename").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                     p1 = (String) map.get("port1");
                     p2 = (String) map.get("port2");
                     p3 = (String) map.get("port3");
                     p4 = (String) map.get("port4");


                    t11.setText(p1) ;
                    t22.setText(p2);
                    t33.setText(p3);
                    t44.setText(p4);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User", databaseError.getMessage());
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();

        finalButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                database.child("AutomaticOnOff/port1").addValueEventListener(new ValueEventListener() {

                    @RequiresApi(api = Build.VERSION_CODES.N)

                    @Override

                    public void onDataChange(final DataSnapshot dataSnapshot) {

                        s1= startTimeP1.getText().toString().trim();
                        e1 = stopTimeP1.getText().toString();

                        if (dataSnapshot.exists()) {
                            HashMap<String,String> mapBt1=new HashMap<String,String>();


                            mapBt1.put("ontime",s1);
                            mapBt1.put("offtime",e1);

                            dataSnapshot.getRef().setValue(mapBt1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void Void) {
                                     //additem.addItem(p1+" Scheduled From: " +s1 +"  To: " +e1);
                                   if(notify1.equals("ON")) {
                                       android.app.Notification notification = new NotificationCompat.Builder(SchedularActivity.this, Notification.CHANNEL_ID)
                                               .setSmallIcon(R.drawable.ic_twotone_home_24)
                                               .setContentTitle("Schedular")
                                               .setContentText(p1 + " Scheduled From: " + s1 + "  To: " + e1)
                                               .build();
                                       NotificationManagerCompat.from(SchedularActivity.this).notify(new Random().nextInt(), notification);
                                   }
                                    Toast.makeText(getBaseContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                                   // DynamicToast.makeSuccess(SchedularActivity.this, "Updated Successfully!", 5).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //DynamicToast.makeError(SchedularActivity.this,e.getMessage(), 10).show();
                                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getBaseContext(), "Error Try again!", Toast.LENGTH_SHORT).show();
                                   // DynamicToast.makeError(SchedularActivity.this, "Error Occured!", 10).show();
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("user----", databaseError.getMessage());
                    }
                });
            }
        });
       finalButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  mp.start();
                database.child("AutomaticOnOff/port2").addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        s2= startTimeP2.getText().toString();
                        e2 =stopTimeP2.getText().toString();
                        if(dataSnapshot.exists()) {
                            HashMap<String,String> mapBt2=new HashMap<String,String>();

                            mapBt2.put("ontime",s2);
                            mapBt2.put("offtime",e2);

                            dataSnapshot.getRef().setValue(mapBt2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    if (notify1.equals("ON")) {
                                        android.app.Notification notification = new NotificationCompat.Builder(SchedularActivity.this, Notification.CHANNEL_ID)
                                                .setSmallIcon(R.drawable.ic_twotone_home_24)
                                                .setContentTitle("Schedular")
                                                .setContentText(p2 + " Scheduled From: " + s2 + "  To: " + e2)
                                                .build();
                                        NotificationManagerCompat.from(SchedularActivity.this).notify(new Random().nextInt(), notification);
                                        Toast.makeText(getBaseContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                   // DynamicToast.makeError(SchedularActivity.this, "Error Occured!", 10).show();
                                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getBaseContext(), "Error Try again!", Toast.LENGTH_SHORT).show();
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


        finalButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.child("AutomaticOnOff/port3").addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        s3= startTimeP3.getText().toString();
                        e3 =stopTimeP3.getText().toString();

                        if (dataSnapshot.exists()) {
                            HashMap<String,String> mapBt3=new HashMap<String,String>();

                            mapBt3.put("ontime",s3);
                            mapBt3.put("offtime",e3);

                            dataSnapshot.getRef().setValue(mapBt3).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    if(notify1.equals("ON")) {
                                        android.app.Notification notification = new NotificationCompat.Builder(SchedularActivity.this, Notification.CHANNEL_ID)
                                                .setSmallIcon(R.drawable.ic_twotone_home_24)
                                                .setContentTitle("Schedular")
                                                .setContentText(p3 + " Scheduled From: " + s3 + "  To: " + e3)
                                                .build();
                                        NotificationManagerCompat.from(SchedularActivity.this).notify(new Random().nextInt(), notification);
                                        Toast.makeText(getBaseContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                                       // DynamicToast.makeSuccess(SchedularActivity.this, "Updated Successfully!", 5).show();
                                    } }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getBaseContext(), "Error Try again!", Toast.LENGTH_SHORT).show();
                                   // DynamicToast.makeError(SchedularActivity.this, "Error Occured!", 5).show();
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


        finalButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                database.child("AutomaticOnOff/port4").addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        s4=startTimeP4.getText().toString();
                        e4=stopTimeP4.getText().toString();
                        if(dataSnapshot.exists()) {
                            HashMap<String,String> mapBt4=new HashMap<String,String>();


                            mapBt4.put("ontime",s4);
                            mapBt4.put("offtime",e4);

                            dataSnapshot.getRef().setValue(mapBt4).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    if(notify1.equals("ON")) {
                                        android.app.Notification notification = new NotificationCompat.Builder(SchedularActivity.this, Notification.CHANNEL_ID)
                                                .setSmallIcon(R.drawable.ic_twotone_home_24)
                                                .setContentTitle("Schedular")
                                                .setContentText(p4 + " Scheduled From: " + s4 + "  To: " + e4)
                                                .build();
                                        NotificationManagerCompat.from(SchedularActivity.this).notify(new Random().nextInt(), notification);
                                        Toast.makeText(getBaseContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                                       // DynamicToast.makeSuccess(SchedularActivity.this, "Updated Successfully!", 5).show();
                                    }

                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getBaseContext(), "Error Try again!", Toast.LENGTH_SHORT).show();
                                   // DynamicToast.makeError(SchedularActivity.this, "Error Occured!", 10).show();
                                }
                            });                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("User", databaseError.getMessage());
                    }
                });
            }
        });

    }
    private static String utilTime(int value) {

        if (value < 10)
            return "0" + value;
        else
            return String.valueOf(value);
    }


}