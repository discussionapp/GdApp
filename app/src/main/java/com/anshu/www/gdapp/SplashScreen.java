package com.anshu.www.gdapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EasySplashScreen config=new EasySplashScreen(SplashScreen.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(2000)
                .withBackgroundColor(Color.parseColor("#e5dee2"))
                .withLogo(R.mipmap.ic_launcher_round)
                //.withHeaderText("WELCOME")
                //.withFooterText("copyright 2018")
                .withBeforeLogoText("GD App")
                .withAfterLogoText("communicate and learn");

//        config.getHeaderTextView().setTextColor(Color.BLACK);
        //config.getFooterTextView().setTextColor(Color.BLACK);
        config.getAfterLogoTextView().setTextColor(Color.BLACK);
        config.getBeforeLogoTextView().setTextColor(Color.BLACK);

        View view=config.create();

        setContentView(view);
    }
}
