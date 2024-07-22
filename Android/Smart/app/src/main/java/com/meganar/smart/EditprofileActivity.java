package com.meganar.smart;

import static com.meganar.smart.SessionManager.getThemeColor;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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


import java.util.Map;

public class EditprofileActivity extends AppCompatActivity {

    private EditText ename;
     private TextView eemail;
    private Button btnEdit;
    private String n,e;

    private MediaPlayer mp;
    private final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference database;
    private long pressedTime;
    ImageView backedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeManager.setCustomizedThemes(this,getThemeColor(this));
        setContentView(R.layout.activity_editprofile);

        ename=findViewById(R.id.ed_name);
        eemail=findViewById(R.id.ed_email);
        btnEdit=findViewById(R.id.BEDemail);
        backedit = findViewById(R.id.editback);

        database = FirebaseDatabase.getInstance().getReference("users/"+currentFirebaseUser.getUid());

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();

                    n =  map.get("name");
                    e =  map.get("email");
                    ename.setText(n);
                    eemail.setText(e);
                }
                else{
                    ename.setText("NA");
                    eemail.setText("NA");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User", databaseError.getMessage());
            }
        });


       backedit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               opensettings();
           }
       });


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!ename.getText().toString().equals(n)) {
                    database.child("name").addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                if (validateInputName(ename.getText().toString().trim())) {
                                    dataSnapshot.getRef().setValue(ename.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getBaseContext(), "Name Updated Successfully!", Toast.LENGTH_SHORT).show();
                                           // DynamicToast.makeSuccess(EditprofileActivity.this, "Name Updated Successfully!", 10).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getBaseContext(), "Error Try again!", Toast.LENGTH_SHORT).show();
                                            //DynamicToast.makeError(EditprofileActivity.this, "Error Occured!", 10).show();
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

            }
        });






    }

    private void opensettings() {
        Intent indent =new Intent(this,SettingsActivity.class);
        startActivity(indent);
    }



    public boolean validateInputName(String a){

        if(a.isEmpty()){
            ename.setError("This field can't be empty");
            return false;
        }



        return true;
    }
}