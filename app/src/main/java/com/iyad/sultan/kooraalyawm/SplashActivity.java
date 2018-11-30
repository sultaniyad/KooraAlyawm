package com.iyad.sultan.kooraalyawm;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.iyad.sultan.kooraalyawm.Sign.SignIn;
import com.iyad.sultan.kooraalyawm.Sign.SignUp;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 4000;
    Button signIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callSignIn();
                //close this activity
                finish();
            }
        },SPLASH_TIME_OUT);


    }


    void callSignIn(){
        //Call Sing In


                Intent i = new Intent(SplashActivity.this,SignIn.class);
                startActivity(i);

    }
}
