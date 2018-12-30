package com.iyad.sultan.kooraalyawm.Groups;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iyad.sultan.kooraalyawm.Model.Group;
import com.iyad.sultan.kooraalyawm.Model.Player;
import com.iyad.sultan.kooraalyawm.R;
import com.iyad.sultan.kooraalyawm.Utilities.UiValidator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.iyad.sultan.kooraalyawm.Utilities.Constants.GROUP_PATH;
import static com.iyad.sultan.kooraalyawm.Utilities.Constants.PLAYER_PATH;

public class JoinGroupActivity extends AppCompatActivity {

    //Constants
    //Bundle
    private static final String BUNDLE_JOIN = "BUNDLE_JOIN_GROUP";

    //Group
    private static final String GROUP_ID = "GROUP_ID_JOIN";
    private static final String GROUP_NAME = "GROUP_NAME_JOIN";
    private static final String GROUP_PASSWORD = "GROUP_JOINT_PASSWORD";
    private static final String GROUP_ICON = "GROUP_ICON_JOIN";
    private static final String IS_GROUP_PROTECTED = "IS_GROUP_PROTECTED";

    private static final String GROUP_CURRENT_PLAYER = "GROUP_CURRENT_PLAYER";
    //User
    private static final String PLAYER_ID = "PLAYER_ID_JOIN";
    String groupId;
    String groupName;
    String groupIcon;
    boolean isProtected;
    String groupPassword;
    int groupCurrentPlayer;
    //ui
    private ScrollView rootLayout;
    private ImageView mGroupLogo;
    private TextView mGroupName;
    private EditText etPassword;
    private TextInputLayout layoutPassword;
    private AppCompatButton btnJoin;
    //Data
    private Bundle passBundle;
    //FireBase
    private FirebaseUser user;
    private DatabaseReference rootRef;
    private DatabaseReference mGroupRef;
    private DatabaseReference mPlayerRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        passBundle = getIntent().getBundleExtra(BUNDLE_JOIN);

        if (passBundle != null) {
            drawUI();

            String groupId = passBundle.getString(GROUP_ID);
            if (groupId == null) {
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            user = FirebaseAuth.getInstance().getCurrentUser();

            rootRef = FirebaseDatabase.getInstance().getReference();
            mGroupRef = rootRef.child(GROUP_PATH).child(groupId);
            mPlayerRef = rootRef.child(PLAYER_PATH).child(user.getUid());
            //  Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }


    }

    private void drawUI() {


        rootLayout = findViewById(R.id.join_layout_root);
        mGroupLogo = findViewById(R.id.img_join_group_icon);
        layoutPassword = findViewById(R.id.layout_join_password);
        etPassword = findViewById(R.id.ed_join_group_password);

        mGroupName = findViewById(R.id.txt_join_group_name);
        btnJoin = findViewById(R.id.btn_joining_group);

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //entry Point
                validForm();
            }
        });

        //Load passed data
        groupId = passBundle.getString(GROUP_ID);
        groupName = passBundle.getString(GROUP_NAME);
        groupIcon = passBundle.getString(GROUP_ICON);
        isProtected = passBundle.getBoolean(IS_GROUP_PROTECTED);
        groupPassword = passBundle.getString(GROUP_PASSWORD);
        groupCurrentPlayer = passBundle.getInt(GROUP_CURRENT_PLAYER,0);

        //disable password if  none there
        if (!isProtected)
            etPassword.setVisibility(View.GONE);

        //Show info on page
        Glide.with(mGroupLogo.getContext()).load(groupIcon).apply(new RequestOptions().circleCrop().placeholder(R.mipmap.ic_player_defualt).error(R.mipmap.ic_player_error_loading)).into(mGroupLogo);
        mGroupName.setText(groupName);
    }


    private void validForm() {

        if(isProtected)

        {
            if (!validPassword(new UiValidator())) {
                showMessage("Empty !!!");
                return;
            }
            if (groupPassword.contentEquals(etPassword.getText().toString())) {
                showMessage("You are in :)");
                startJoinGroup();
            } else showMessage("Check Password :( ");
        }

        //group not protected
        else startJoinGroup();


    }

    private void startJoinGroup() {


        //For Group Node
        Map<String, Object> newMember = new HashMap<>();
        newMember.put(user.getUid(), true);

        mGroupRef.child("members").updateChildren(newMember).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    ;
                   // Toast.makeText(JoinGroupActivity.this, "add to Group Node", Toast.LENGTH_SHORT).show();
            }
        });

        //for Player Node
        Map<String, Object> newGroup = new HashMap<>();
        newGroup.put(groupId, true);

        mPlayerRef.child("groups").updateChildren(newGroup).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    ;
                 //   Toast.makeText(JoinGroupActivity.this, "add to Player Node", Toast.LENGTH_SHORT).show();
            }
        });


        if(groupCurrentPlayer == 0) {
            mPlayerRef.child("privilege").updateChildren(newGroup).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                        ;
                    //   Toast.makeText(JoinGroupActivity.this, "add to Player Node", Toast.LENGTH_SHORT).show();
                }
            });

            mGroupRef.child("privilege").updateChildren(newMember);

        }
        Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show();


        finish();
    }

    private void showMessage(String meg) {
        Snackbar message = Snackbar.make(rootLayout, meg, Snackbar.LENGTH_LONG);
        message.show();
    }

    private boolean validPassword(UiValidator validator) {

        if (validator.isEditTextEmpty(etPassword)) {
            layoutPassword.setError(getResources().getString(R.string.required_filed));
            return false;
        } else
            layoutPassword.setErrorEnabled(false);

        return true;
    }


}
