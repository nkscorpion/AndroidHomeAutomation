package com.surendra.androidhomeautomation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Splash screen without any delay
        Intent cv = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(cv);
        finish();
        /*
        //A little bit of delay so it is sure that splash screen is shown
        Thread t = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                    Intent cv = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(cv);
                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();*/
    }
}
