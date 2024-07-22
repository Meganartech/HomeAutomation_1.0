package com.meganar.smart;

import static com.meganar.smart.SessionManager.getThemeColor;
import static com.meganar.smart.ThemeManager.setCustomizedThemes;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;

public class ReportActivity extends AppCompatActivity {

    private SmartMaterialSpinner smartMaterialSpinner;
    private List<String> issuesList;
    private EditText reportData;
    private Button reportBtn;
    private MediaPlayer mp;
    private String issue_question,emailid,nameid,phoneid,issue,u;
    private final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
    private DatabaseReference database;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private long pressedTime;
    private ProgressBar p1;
    ImageView rb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomizedThemes(this,getThemeColor(this));
        setContentView(R.layout.activity_report);
        reportBtn = findViewById(R.id.report_btn);
        reportData = findViewById(R.id.prob_sugg);

        p1 = findViewById(R.id.pro);
        smartMaterialSpinner = findViewById(R.id.spinnerQuestionsOptions);

        database = FirebaseDatabase.getInstance().getReference("users/"+currentFirebaseUser.getUid());
        u = currentFirebaseUser.getUid();
  rb = findViewById(R.id.report_back);

        //DatabaseReference database = FirebaseDatabase.getInstance().getReference("users/");



        issuesList = new ArrayList<>();
        issuesList.add("App not Working");
        issuesList.add("IoT Device not Working");
        issuesList.add("Others");
        smartMaterialSpinner.setHint("Select One:");
        smartMaterialSpinner.setEnableFloatingLabel(false);
        smartMaterialSpinner.setItem(issuesList);



        smartMaterialSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                issue_question = issuesList.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                issue_question = "Others";
            }
        });

        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bck();
            }
        });



        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if (validateInputName(reportData.getText().toString().trim())) {
                      issue =reportData.getText().toString();
                      sendMessage(issue);
                     reportBtn.setVisibility(View.GONE);
                     p1.setVisibility(View.VISIBLE);

                }
                else {
                    Toast.makeText(ReportActivity.this, "Try again!..." , Toast.LENGTH_SHORT).show();

                    onStart();
                }
            }
        });

    }

    private void bck() {
        Intent indent =new Intent(this,ProfileActivity.class);
        startActivity(indent);
    }


    private void sendMessage(String issue) {

        database.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    emailid = (String) map.get("email");
                    Map<String, Object> user = new HashMap<>();
                    user.put("Email",emailid);
                    user.put("UserId",u);
                    user.put("Issue",issue_question);
                    user.put("Problem",issue);
                    db.collection("Issues")
                            .add(user)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    MaterialDialog mDialog = new MaterialDialog.Builder(ReportActivity.this)
                                                .setTitle("report issue")
                                                .setMessage("Your Issue Registered Successfully")
                                                .setCancelable(false)
                                                .setPositiveButton("Ok", new MaterialDialog.OnClickListener() {
                                                    @Override
                                                    public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                                                        finish();
                                                        dialogInterface.dismiss();
                                                        reportBtn.setVisibility(View.VISIBLE);
                                                        p1.setVisibility(View.GONE);
                                                    }
                                                })
                                                .build();
                                        mDialog.show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w( "Error adding document", e);
                                    Toast.makeText(ReportActivity.this, "Try again!", Toast.LENGTH_SHORT).show();
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
    public boolean validateInputName(String a){
        if(a.isEmpty()){
            reportData.setError("This field can't be empty");
            return false;
        }
        return true;
    }
}

