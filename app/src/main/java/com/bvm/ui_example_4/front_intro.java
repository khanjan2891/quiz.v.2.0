package com.bvm.ui_example_4;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class front_intro extends AppCompatActivity {
    private static int Splash_screen=5000;
  Animation topanim,botanim;
  ImageView image;
  TextView appname,tagline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_intro);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        topanim= AnimationUtils.loadAnimation(front_intro.this, R.anim.top_animation);
        botanim= AnimationUtils.loadAnimation(front_intro.this, R.anim.bottom_animmation);
        image= findViewById(R.id.logo);
        appname= findViewById(R.id.app_name);
        tagline= findViewById(R.id.tagline);
        image.setAnimation(topanim);
        appname.setAnimation(botanim);
        tagline.setAnimation(botanim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(front_intro.this, MainActivity.class);
                        startActivity(intent);
                finish();


            }
        },Splash_screen);
    }

}
