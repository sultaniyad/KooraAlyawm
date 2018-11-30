package com.iyad.sultan.kooraalyawm.Sign;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.iyad.sultan.kooraalyawm.R;

public class SignUp extends AppCompatActivity {

    private TextView mLoginAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        drawUI();
    }

    private void drawUI(){
        mLoginAccount = (TextView) findViewById(R.id.link_login);
        mLoginAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUp.this,SignIn.class);
                startActivity(i);
                finish();

            }
        });
    }
}
