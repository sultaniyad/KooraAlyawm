package com.iyad.sultan.kooraalyawm.Groups;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.iyad.sultan.kooraalyawm.Model.Group;
import com.iyad.sultan.kooraalyawm.R;

public class JoinGroupActivity extends AppCompatActivity {

    //Constants
    //Bundle
    private static final  String BUNDLE_JOIN = "BUNDLE_JOIN_GROUP";

    //Group
    private static final  String GROUP_ID = "GROUP_ID_JOIN";
    private static final  String GROUP_NAME = "GROUP_NAME_JOIN";
    private static final  String GROUP_PASSWORD = "GROUP_JOINT_PASSWORD";
    private static final  String GROUP_ICON = "GROUP_ICON_JOIN";
    private static final String IS_GROUP_PROTECTED = "IS_GROUP_PROTECTED";
    //User
    private static final  String PLAYER_ID = "PLAYER_ID_JOIN";

    //UI
    private ImageView mGroupLogo;
    private TextView mGroupName;
    private EditText etPassword;
    private AppCompatButton btnJoin;


    //Data
    private  Bundle passBundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        passBundle =  getIntent().getBundleExtra(BUNDLE_JOIN);

       if(passBundle != null){

           drawUI();
         //  Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
       }


    }

    private void drawUI() {

        mGroupLogo = findViewById(R.id.img_join_group_icon);
        etPassword = findViewById(R.id.ed_join_group_password);
        mGroupName = findViewById(R.id.txt_join_group_name);
        btnJoin = findViewById(R.id.btn_joining_group);


        String groupId = passBundle.getString(GROUP_ID);
        String groupName = passBundle.getString(GROUP_NAME);
        String groupIcon = passBundle.getString(GROUP_ICON);
        boolean isProtected = passBundle.getBoolean(IS_GROUP_PROTECTED);
        String groupPassword = passBundle.getString(GROUP_PASSWORD);

        //disable password if  none there
        if (!isProtected)
            etPassword.setVisibility(View.INVISIBLE);

        //Show info on page
        Glide.with(mGroupLogo.getContext()).load(groupIcon).apply(new RequestOptions().circleCrop().placeholder(R.mipmap.ic_player_defualt).error(R.mipmap.ic_player_error_loading)).into(mGroupLogo);
        mGroupName.setText(groupName);
    }



        private void startJoinGroup(){


    }
}
