package com.meganar.smart;

import static com.meganar.smart.SessionManager.getThemeColor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.example.smart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;


public class OtpVerificationActivity extends AppCompatActivity {


    private PinView pinFromUser;
    private String emailid,pwd;
    private TextView mob_No;
    private Button submit_btn,resend_btn;
    private Random rOtp;
    private char[] otp;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeManager.setCustomizedThemes(this,getThemeColor(this));
        setContentView(R.layout.activity_otp_verification);

        if(getSupportActionBar()!=null) {
            getSupportActionBar().hide();
        }

      //  mp = MediaPlayer.create(OtpVerificationActivity.this, R.raw.buttonsound);

        rOtp = new Random();
        final String numbers = "0123456789";
        otp = new char[6];

        for (int i = 0; i < 6; i++)
        {
            otp[i] = numbers.charAt(rOtp.nextInt(numbers.length()));
        }

        emailid = getIntent().getStringExtra("emailfromlogin");
        pwd = getIntent().getStringExtra("pwdfromlogin");



       pinFromUser = findViewById(R.id.OTPPinView);
       mob_No = findViewById(R.id.mob_no);
        resend_btn = findViewById(R.id.resend_otp);
        submit_btn = findViewById(R.id.submit_otp);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

    
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
    public void signUser(String email, String password) {

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            if (user.isEmailVerified()) {
                              //  DynamicToast.makeSuccess(OtpVerificationActivity.this, "Successfully logged in", 10).show();
                                startActivity(new Intent(OtpVerificationActivity.this, HomeActivity.class));
                                finish();
                            } else {
                                FirebaseAuth.getInstance().signOut();
                               // DynamicToast.makeWarning(OtpVerificationActivity.this, "Please Verify Your Email", 10).show();
                                startActivity(new Intent(OtpVerificationActivity.this, LoginscreenActivity.class));
                                finish();
                            }
                        } else {
                          //  DynamicToast.makeError(OtpVerificationActivity.this, "Invalid Email or Password", 10).show();
                            startActivity(new Intent(OtpVerificationActivity.this, LoginscreenActivity.class));
                            finish();
                        }
                    }
                });
            }
}