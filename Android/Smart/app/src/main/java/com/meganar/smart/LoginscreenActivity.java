package com.meganar.smart;

import static com.meganar.smart.SessionManager.getThemeColor;
import static com.meganar.smart.ThemeManager.setCustomizedThemes;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.SharedPreferences;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import java.util.Map;
import java.util.regex.Pattern;
public class LoginscreenActivity extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //Totally at least 6 characters
                    "$");
    private EditText login_email;
    private EditText login_password;
    private Button login, register, forget;
    private SessionManager session;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private final FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseUser user;
    private DatabaseReference database;
    private ProgressBar progressBar1;
    private  String userID;
    private String curr;

    private SharedPreferences sharedPreferences;

    private long pressedTime;
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCustomizedThemes(this,getThemeColor(this));
        setContentView(R.layout.activity_loginscreen);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);



        login_email = findViewById(R.id.username);
        login_password = findViewById(R.id.password);
        login = findViewById(R.id.signin);
        register = findViewById(R.id.signup);
        forget = findViewById(R.id.forgetpassword);
        progressBar1 = findViewById(R.id.prog1);



        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        if(user != null) {

            startActivity(new Intent(LoginscreenActivity.this, HomeActivity.class));
        }

            session = new SessionManager(getApplicationContext());
            if (session.isLoggedIn() && user != null) {
                Intent intent = new Intent(LoginscreenActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }



        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fro();

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inEmail = login_email.getText().toString().trim();

                String inPassword = login_password.getText().toString().trim();


                if(validateInput(inEmail, inPassword)){
                    signUser(inEmail, inPassword);

                    progressBar1.setVisibility(View.VISIBLE);
                }

            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegister();
            }
        });
    }
    private void fro() {
        Intent indent =new Intent(this,PasswordResetActivity.class);
        startActivity(indent);
    }
    private void openRegister() {
        Intent indent =new Intent(this,RegisterActivity.class);
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
    public void signUser(final String email, final String password){
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user.isEmailVerified()) {
                               FirebaseAuth.getInstance().getCurrentUser();
                                savePassword(password);

                                Intent indent =  new Intent(LoginscreenActivity.this, HomeActivity.class);
                                Toast.makeText(getBaseContext(), "WELCOME", Toast.LENGTH_SHORT).show();
                                                          //   DynamicToast.makeSuccess(LoginscreenActivity.this, "WELCOME", 10).show();
                                startActivity(indent);
                                finish();
                                String uid = firebaseAuth.getUid();
                                //log(password,uid);
                            }
                            else {
                                FirebaseAuth.getInstance().signOut();
                                Toast.makeText(getBaseContext(), "Please Verify Your Email", Toast.LENGTH_SHORT).show();
                                //DynamicToast.makeWarning(LoginscreenActivity.this, "Please Verify Your Email", 10).show();
                                startActivity(new Intent(LoginscreenActivity.this, LoginscreenActivity.class));
                                finish();
                            }
                }
                else{
                    Toast.makeText(getBaseContext(), "Invalid Email Or Password", Toast.LENGTH_SHORT).show();
                    //DynamicToast.makeError(LoginscreenActivity.this, "Invalid Email or Password", 10).show();
                    startActivity(new Intent(LoginscreenActivity.this, LoginscreenActivity.class));
                    finish();
                }
            }
        });
    }

    private void savePassword(String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("password", password);
        editor.apply();
    }

    private void log(String inpassword,String uid) {

           // database = FirebaseDatabase.getUid(login_email);
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

        if(curr.equals(inpassword)) {
            Intent indent =  new Intent(LoginscreenActivity.this, HomeActivity.class);
            Toast.makeText(getBaseContext(), "WELCOME", Toast.LENGTH_SHORT).show();
           // DynamicToast.makeSuccess(LoginscreenActivity.this, "WELCOME", 10).show();
        }
    }

    public boolean validateInput(String inemail, String inpassword){

        if(inemail.isEmpty()){
            login_email.setError("Email field is empty.");
            return false;
        }
        if(inpassword.isEmpty()){
            login_password.setError("Password is empty.");
            return false;
        }
        return true;
    }
}
