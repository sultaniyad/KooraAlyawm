package com.iyad.sultan.kooraalyawm.Groups;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.iyad.sultan.kooraalyawm.R;

public class AddGroupActivity extends AppCompatActivity {

    private EditText mEdGroupName;
    private EditText mEdGroupPassword;
    private SwitchCompat mSwitch;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_group);

        drawUI();
    }




    private void drawUI(){
        mEdGroupName = (EditText) findViewById(R.id.add_group_et_group_name) ;
        mEdGroupPassword = (EditText) findViewById(R.id.add_group_et_password) ;
        mSwitch = (SwitchCompat) findViewById(R.id.add_group_sw_is_protected);

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    mEdGroupPassword.setVisibility(View.VISIBLE);

                }
                else {
                    mEdGroupPassword.setVisibility(View.INVISIBLE);

                }
            }
        });

    }
}
