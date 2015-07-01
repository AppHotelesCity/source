package com.zebstudios.cityexpress;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;


public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);


        LinearLayout li=(LinearLayout)findViewById(R.id.layout_splash);


        if( CompatibilityUtil.isTablet( this ) ){
            //Tablet

            li.setBackgroundDrawable( getResources().getDrawable(R.drawable.splash_tablet_4) );

        }else{
            //Telefono
            // img.setImageResource(R.drawable.splash_1);
        }


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}