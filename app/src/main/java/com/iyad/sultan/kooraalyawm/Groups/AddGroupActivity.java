package com.iyad.sultan.kooraalyawm.Groups;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iyad.sultan.kooraalyawm.Model.Group;
import com.iyad.sultan.kooraalyawm.R;
import com.iyad.sultan.kooraalyawm.Utilities.UiValidator;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.util.HashMap;
import java.util.Map;

import static com.iyad.sultan.kooraalyawm.Utilities.Constants.GROUP_PATH;
import static com.iyad.sultan.kooraalyawm.Utilities.Constants.PLAYER_PATH;

public class AddGroupActivity extends AppCompatActivity {

    private static final int IMAGE_INTNET_RESULT = 7;

    private ImageView mGropuIcon;
    private EditText mEdGroupName;
    private TextInputLayout layoutGroupName;
    private TextInputLayout layoutGroupPassword;
    private EditText mEdGroupPassword;
    private SwitchCompat mSwitch;
    private AppCompatButton btnAddGroup;
    private ProgressBar mProgressBar;

    //
    CropImage.ActivityResult result;

    private static String DEFAULT_ICON = "android.resource://com.iyad.sultan.kooraalyawm/mipmap/ic_group_default";
    //Indicators
    private Boolean isPreotected = false;

    //Firebase ;
    private DatabaseReference mRef;
    private FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_group);
        drawUI();
       // circleImage();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null)
            finish();

        mRef = FirebaseDatabase.getInstance().getReference(GROUP_PATH);


    }

    private void circleImage() {
        Glide.with(AddGroupActivity.this).load("android.resource://com.iyad.sultan.kooraalyawm/mipmap/ic_player_defualt").apply(new RequestOptions().circleCrop()
        ).into(mGropuIcon);
    }


    private void drawUI() {
        mGropuIcon = findViewById(R.id.add_group_icon);
        mGropuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGroupIcon();
            }
        });
        btnAddGroup = findViewById(R.id.btn_add_group);
        mEdGroupName = (EditText) findViewById(R.id.add_group_et_group_name);
        layoutGroupName = findViewById(R.id.layout_add_group_name);
        mEdGroupPassword = (EditText) findViewById(R.id.add_group_et_password);
        layoutGroupPassword = findViewById(R.id.layout_add_group_password);
        mSwitch = (SwitchCompat) findViewById(R.id.add_group_sw_is_protected);
        mProgressBar = findViewById(R.id.addGroupIndeterminateBar);

        //Switch
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    mEdGroupPassword.setVisibility(View.VISIBLE);
                    isPreotected = true;

                } else {
                    mEdGroupPassword.setVisibility(View.INVISIBLE);
                    isPreotected = false;

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
        if (!validGroupName(validator)) {
            return;
        }
        if (!validPassword(validator))
            return;


        //upload image and add group to FireBase
        uploadImageToFireStorage(result);


    }

    private void addGroupToFireBaseWithDefaultIcon() {
        ///Inti data

        String groupKey = mRef.push().getKey();
        String uid = user.getUid();

        Map<String, Object> memeber = new HashMap<String, Object>();
        //Add user creator of group By default (Group Profile)
        memeber.put(uid, true);

        Map<String, Object> privilege = new HashMap<>();
        privilege.put(uid, true);


        Group group = new Group();
        group.setId(groupKey);
        group.setName(mEdGroupName.getText().toString());
        //
        group.setLogo(DEFAULT_ICON);
        group.setSecure(isPreotected);
        group.setPassword(mEdGroupPassword.getText().toString());
        group.setMembers(memeber);
        group.setCurrentgames(null);
        group.setPrivilege(privilege);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showProgressDialog();
            }
        }, 3000);

        //Add Group
        mRef.child(groupKey).setValue(group).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Toast.makeText(AddGroupActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();

                else Toast.makeText(AddGroupActivity.this, "Failed !!!", Toast.LENGTH_SHORT).show();
            }

        });

        //Add Admin Group/Privalge Tree to this group as Admin (PLayer Profile)
        Map<String, Object> privileges = new HashMap<>();
        privileges.put(groupKey, true);

        DatabaseReference playerRef = FirebaseDatabase.getInstance().getReference(PLAYER_PATH);

        playerRef.child(uid).child("privilege").updateChildren(privileges);
        playerRef.child(uid).child("groups").updateChildren(privileges);

        //close
        finish();
    }

    private void selectGroupIcon() {

        Intent imageIntent = new Intent();
        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
        imageIntent.setType("image/*");
        startActivityForResult(imageIntent, IMAGE_INTNET_RESULT);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (IMAGE_INTNET_RESULT == requestCode && resultCode == RESULT_OK && data != null) {

            Uri imageUri = data.getData();


            // start picker to get image for cropping and then use the image in cropping activity
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);

        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            /*Here important part*/
            result = CropImage.getActivityResult(data);

            Glide.with(AddGroupActivity.this).load(result.getUri()).apply(new RequestOptions().circleCrop()).into(mGropuIcon);


        }


    }

    private void uploadImageToFireStorage(CropImage.ActivityResult result) {

        if (result != null) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageRef.child("images/" + result.getUri().getLastPathSegment());


            UploadTask uploadTask = imageRef.putFile(result.getUri());


            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    addGroupInfoToFireBase("");
                    Toast.makeText(AddGroupActivity.this, "Failed Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {


                    taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            // taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                            //   Toast.makeText(AddGroupActivity.this, "Image Uploaded"  + uri.getPath(), Toast.LENGTH_SHORT).show();
                            addGroupInfoToFireBase(uri.toString());
                        }
                    });


                }
            });


        } else
            //Add Group with default icon
            addGroupToFireBaseWithDefaultIcon();

    }

    private void addGroupInfoToFireBase(String profileUrl) {
        ///Inti data

        String groupKey = mRef.push().getKey();
        String uid = user.getUid();

        Map<String, Object> memeber = new HashMap<String, Object>();
        //Add user creator of group By default (Group Profile)
        memeber.put(uid, true);

        Map<String, Object> privilege = new HashMap<>();
        privilege.put(uid, true);


        Group group = new Group();
        group.setId(groupKey);
        group.setName(mEdGroupName.getText().toString());
        //
        group.setLogo(profileUrl);
        group.setSecure(isPreotected);
        group.setPassword(mEdGroupPassword.getText().toString());
        group.setMembers(memeber);
        group.setCurrentgames(null);
        group.setPrivilege(privilege);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showProgressDialog();
            }
        }, 3000);

        //Add Group
        mRef.child(groupKey).setValue(group).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Toast.makeText(AddGroupActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();

                else Toast.makeText(AddGroupActivity.this, "Failed !!!", Toast.LENGTH_SHORT).show();
            }

        });

        //Add Admin Group/Privalge Tree to this group as Admin (PLayer Profile)
        Map<String, Object> privileges = new HashMap<>();
        privileges.put(groupKey, true);

        DatabaseReference playerRef = FirebaseDatabase.getInstance().getReference(PLAYER_PATH);

        playerRef.child(uid).child("privilege").updateChildren(privileges);
        playerRef.child(uid).child("groups").updateChildren(privileges);

        //close
        finish();
    }

    private boolean validGroupName(UiValidator v) {

        if (v.isEditTextEmpty(mEdGroupName)) {
            layoutGroupName.setError(getResources().getString(R.string.required_filed));
            return false;
        } else
            layoutGroupName.setErrorEnabled(false);

        return true;
    }

    private boolean validPassword(UiValidator v) {

        if (v.isEditTextEmpty(mEdGroupPassword) && mEdGroupPassword.isShown()) {
            layoutGroupPassword.setError(getResources().getString(R.string.required_filed));
            return false;
        } else
            layoutGroupName.setErrorEnabled(false);

        return true;
    }

    private void hideProgressDialog() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private void showProgressDialog() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

}



/*
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("players");

        String key = ref.push().getKey();

        Map<String,Boolean> usergroup = new HashMap<String,Boolean>();
        Map<String,Boolean> admin = new HashMap<String,Boolean>();


        usergroup.put("initiation",true);
        admin.put("initiation",true);


      //  Player player = new Player(key,"ahmied","https://fifadataba.com/images/players/183108.jpg", usergroup,admin);


      //   ref.child("-LTW0ZIEN6CSUz2FK0RP").setValue(player);
       // Map<String,Object> taskMap = new HashMap<String,Object>();
      //  taskMap.put("group  Id 2 xxx", true);

    //   ref.child("-LTW0ZIEN6CSUz2FK0RP").child("userGroup").child("group  Id 2 xxx").setValue(null);



*/