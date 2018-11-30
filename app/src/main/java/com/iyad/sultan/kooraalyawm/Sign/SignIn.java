package com.iyad.sultan.kooraalyawm.Sign;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.iyad.sultan.kooraalyawm.R;

public class SignIn extends AppCompatActivity {

    private TextView mCrateAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        drawUI();
    }

    private void drawUI(){

        mCrateAccount =(TextView)findViewById(R.id.link_signup);
        mCrateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignIn.this,SignUp.class);
                startActivity(i);
                finish();
            }
        });
    }
}
