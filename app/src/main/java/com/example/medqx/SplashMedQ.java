package com.example.medqx;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashMedQ extends AppCompatActivity {

    private static int SPLASH_SCREEN = 4000;

    Animation topAnim,botAnim;
    ImageView logo,slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_med_q);
        this.setTitle(" ");
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_med_q);

        //Animation
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        botAnim = AnimationUtils.loadAnimation(this, R.anim.bot_animation);

        logo = findViewById(R.id.MedQx);
        slogan = findViewById(R.id.Medqueuer);

        logo.setAnimation(topAnim);
        slogan.setAnimation(botAnim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashIntent = new Intent(SplashMedQ.this, Login.class);
                startActivity(splashIntent);
                finish();
            }
        },SPLASH_SCREEN);

    }
}