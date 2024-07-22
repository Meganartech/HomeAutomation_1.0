package com.meganar.smart;

import static com.meganar.smart.SessionManager.getThemeColor;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class PasswordResetActivity extends AppCompatActivity {
    private EditText resetEmail;
    private Button forgo, logit; //create
    private FirebaseAuth firebaseAuth;
    private MediaPlayer mp;
    private long pressedTime;
    ImageView pback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeManager.setCustomizedThemes(this,getThemeColor(this));
        setContentView(R.layout.activity_password_reset);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().hide();
        }


        firebaseAuth = FirebaseAuth.getInstance();
        resetEmail = findViewById(R.id.reset_pwd);
        forgo = findViewById(R.id.reset_acc);
      //create = findViewById(R.id.register_back);
        logit = findViewById(R.id.login_back);
        pback = findViewById(R.id.resetback);

        pback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PasswordResetActivity.this,LoginscreenActivity.class));
            }
        });


        forgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = resetEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    resetEmail.setError("Please, fill the email field.",null);
                }
                else {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getBaseContext(), "Email Has been Send Successfully", Toast.LENGTH_SHORT).show();
                               // DynamicToast.makeSuccess(PasswordResetActivity.this, "Email has been sent successfully.", 10).show();
                                //finish();
                                startActivity(new Intent(PasswordResetActivity.this, LoginscreenActivity.class));

                            }
                            else {

                             //   DynamicToast.makeError(PasswordResetActivity.this, "Invalid Email Address.", 10).show();
                                Toast.makeText(getBaseContext(), "Invalid Email!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        logit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PasswordResetActivity.this,LoginscreenActivity.class));
            }
        });
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

}