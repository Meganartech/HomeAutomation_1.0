package com.meganar.smart;

import static com.meganar.smart.SessionManager.getThemeColor;
import static com.meganar.smart.ThemeManager.setCustomizedThemes;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.smart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

public class MessageActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private NotificationManagerCompat notificationManager;

    private final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference database;

    String d1,d2,d3,d4,notify1;

    static ListView listView;

    static ListViewAdapter adapter;
    static ArrayList<String> items;
    static Context context;
    ImageView mb;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomizedThemes(this, getThemeColor(this));
        setContentView(R.layout.activity_message);

        // notificationManager = NotificationManagerCompat.from(this);

        firebaseAuth = FirebaseAuth.getInstance();



        listView = findViewById(R.id.list);



        context = getApplicationContext();


        items = new ArrayList<>();


          mb= findViewById(R.id.back_message);


        listView.setLongClickable(true);
        adapter = new ListViewAdapter(this, items);
        listView.setAdapter(adapter);




        mb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msgback();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                removeItem(i);
                return false;
            }
        });
        database = FirebaseDatabase.getInstance().getReference("users/" + currentFirebaseUser.getUid());

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

                    d1 = (String) map.get("port1");
                    d2 = (String) map.get("port2");
                    d3 = (String) map.get("port3");
                    d4 = (String) map.get("port4");

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User", databaseError.getMessage());
            }
        });
        notification();

        loadContent();
    }

    private void msgback() {
        Intent indent =new Intent(this,ProfileActivity.class);
        startActivity(indent);
    }



    private void loadContent() {
        File path = getApplicationContext().getFilesDir();
        File readFrom = new File(path, "list.txt");
        byte[] content = new byte[(int) readFrom.length()];

        FileInputStream stream = null;
        try {
            stream = new FileInputStream(readFrom);
            stream.read(content);

            String s = new String(content);

            s = s.substring(1, s.length() - 1);
            String[] split = s.split(", ");

            // There may be no items in the list.
            if (split.length == 1 && split[0].isEmpty())
                items = new ArrayList<>();
            else items = new ArrayList<>(Arrays.asList(split));

            adapter = new ListViewAdapter(this, items);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void notification() {
        database.child("Message").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                    String s1 = map.get("Status");
                    String p1 = map.get("port1");
                    String p2 =  map.get("port2");
                    String p3 = map.get("port3");
                    String p4 =  map.get("port4");

                    if(s1.isEmpty()) {

                    }

                    else {

                        addItem(s1);
                        if(notify1.equals("ON")) {
                            android.app.Notification notification = new NotificationCompat.Builder(MessageActivity.this, Notification.CHANNEL_ID)
                                    .setSmallIcon(R.drawable.ic_twotone_home_24)
                                    .setContentTitle("Wi-Fi Connectivity ESP32")
                                    .setContentText(s1)
                                    .build();
                            NotificationManagerCompat.from(MessageActivity.this).notify(new Random().nextInt(), notification);
                        }
                    }

                    if(p1.isEmpty()) {



                    }

                    else {

                        addItem(d1+p1);
                        if(notify1.equals("ON")) {
                            android.app.Notification notification = new NotificationCompat.Builder(MessageActivity.this, Notification.CHANNEL_ID)
                                    .setSmallIcon(R.drawable.ic_twotone_home_24)
                                    .setContentTitle("Schedular.")
                                    .setContentText(d1 + p1)
                                    .build();
                            NotificationManagerCompat.from(MessageActivity.this).notify(new Random().nextInt(), notification);
                        }
                    }



                    if(p3.isEmpty()) {

                    }



                    else
                    {

                        addItem(d2+p2);
                        if(notify1.equals("ON")) {
                            android.app.Notification notification = new NotificationCompat.Builder(MessageActivity.this, Notification.CHANNEL_ID)
                                    .setSmallIcon(R.drawable.ic_twotone_home_24)
                                    .setContentTitle("Schedular..")
                                    .setContentText(d2 + p2)
                                    .build();
                            NotificationManagerCompat.from(MessageActivity.this).notify(new Random().nextInt(), notification);
                        }
                    }



                    if(p3.isEmpty()) {

                    }

                    else {

                        addItem(d3+p3);
                        if(notify1.equals("ON")) {
                        android.app.Notification notification = new NotificationCompat.Builder(MessageActivity.this, Notification.CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_twotone_home_24)
                                .setContentTitle("Schedular...")
                                //.setLargeIcon()
                                .setContentText(d3+p3)
                                .build();
                        NotificationManagerCompat.from(MessageActivity.this).notify(new Random().nextInt(), notification);
                    }}


                    if(p4.isEmpty()) {

                    }
                   else{
                        addItem(d4+p4);
                        {
                        android.app.Notification notification = new NotificationCompat.Builder(MessageActivity.this, Notification.CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_twotone_home_24)
                                .setContentTitle("Schedular....")
                                .setContentText(d4+p4)
                                .build();
                        NotificationManagerCompat.from(MessageActivity.this).notify(new Random().nextInt(), notification);
                    }}

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User", databaseError.getMessage());
            }


        });

            }

    @Override
    protected void onDestroy() {
        File path = getApplicationContext().getFilesDir();
        try {
            FileOutputStream writer = new FileOutputStream(new File(path, "list.txt"));
            writer.write(items.toString().getBytes());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    // function to remove an item given its index in the grocery list.
    public static void removeItem(int i) {
        makeToast("Removed: " + items.get(i));
        items.remove(i);
        listView.setAdapter(adapter);
    }

    // function to add an item given its name.
    public static void addItem(String item) {

        items.add(item);
        listView.setAdapter(adapter);

    }

    // function to make a Toast given a string
    static Toast t;

    private static void makeToast(String s) {
        if (t != null) t.cancel();
        t = Toast.makeText(context, s, Toast.LENGTH_SHORT);
        t.show();
    }

}