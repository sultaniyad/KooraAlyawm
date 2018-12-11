package com.iyad.sultan.kooraalyawm.Groups;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.iyad.sultan.kooraalyawm.MainActivity;
import com.iyad.sultan.kooraalyawm.R;
import com.iyad.sultan.kooraalyawm.Utilities.UiValidator;

public class AddGroupActivity extends AppCompatActivity {

    private ImageView mGropuIcon;
    private EditText mEdGroupName;
    private TextInputLayout layoutGroupName;
    private EditText mEdGroupPassword;
    private SwitchCompat mSwitch;
    private AppCompatButton btnAddGroup;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_group);
        drawUI();
        circleImage();
    }

    private void circleImage(){
        Glide.with(AddGroupActivity.this).load("https://www.gra0vatar.com/avatar/98427704d47d9e0c40eb245aa7234ac6.png?s=150&d=identicon&r=g").apply(new RequestOptions().circleCrop()
                .placeholder(R.mipmap.ic_pick_group)).into(mGropuIcon);
    }



    private void drawUI(){
        mGropuIcon = findViewById(R.id.add_group_icon);
        btnAddGroup = findViewById(R.id.btn_add_group);
        mEdGroupName = (EditText) findViewById(R.id.add_group_et_group_name) ;
        layoutGroupName = findViewById(R.id.layout_add_group_name);
        mEdGroupPassword = (EditText) findViewById(R.id.add_group_et_password) ;
        mSwitch = (SwitchCompat) findViewById(R.id.add_group_sw_is_protected);

        //Switch
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

        btnAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });


    }

    private void submitForm() {
        UiValidator validator = new UiValidator();
        if(!validGroupName(validator)){
            return;
        }

        Toast.makeText(this, "Thanks you", Toast.LENGTH_SHORT).show();
    }

    private boolean validGroupName(UiValidator v) {

        if(v.isEditTextEmpty(mEdGroupName)){
            layoutGroupName.setError(getResources().getString(R.string.required_filed));
            return false;
        }
        else
            layoutGroupName.setErrorEnabled(false);

        return true;
    }
}
