package com.meganar.smart;

import static com.meganar.smart.SessionManager.getThemeColor;
import static com.meganar.smart.ThemeManager.setCustomizedThemes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smart.R;

public class MainActivity extends AppCompatActivity {
    private static final int  SPLASH_SCREEN =3000;  //3second
    Animation topanim;
    TextView appimg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setCustomizedThemes(this,getThemeColor(this));
        setContentView(R.layout.activity_main);
        topanim = AnimationUtils.loadAnimation(this,R.anim.top_animation);

        appimg =findViewById(R.id.img_app);
        appimg.setAnimation(topanim);
        
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent indent = new Intent(MainActivity.this,LoginscreenActivity.class);
                    //Intent indent = new Intent(MainActivity.this,HomeActivity.class);
                startActivity(indent);
                finish();
            }
        },SPLASH_SCREEN);


    }

}



