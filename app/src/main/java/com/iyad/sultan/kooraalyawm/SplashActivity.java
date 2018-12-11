package com.iyad.sultan.kooraalyawm;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.iyad.sultan.kooraalyawm.Sign.SignIn;
import com.iyad.sultan.kooraalyawm.Sign.SignUp;
import android.content.SharedPreferences;



public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 5000;
    private static final String USER_UID = "USER_UID";
    private static final String IS_USER_LOGGED_BEFORE = "USER_LOGGED_BEFORE";


    private SharedPreferences preferences;
    Button signIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                //close this activity

                    callSignIn();

                    finish();
            }
        },SPLASH_TIME_OUT);


    }

    //not used for now
    private void openUserHomePage() {

        Intent i = new Intent(SplashActivity.this, MainActivity.class);
        i.putExtra("isUserLoggedBefore",true);


    }


   private void callSignIn(){
        //Call Sing In


                Intent i = new Intent(SplashActivity.this,SignIn.class);
                startActivity(i);

    }

    public boolean isLoggedBefore(){

        preferences = getSharedPreferences(USER_UID,MODE_PRIVATE);
        return preferences.getBoolean(USER_UID,false);
    }
}
