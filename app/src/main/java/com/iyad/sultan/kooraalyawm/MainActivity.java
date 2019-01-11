package com.iyad.sultan.kooraalyawm;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.iyad.sultan.kooraalyawm.HomePage.FragmentGame;
import com.iyad.sultan.kooraalyawm.HomePage.FragmentGroup;
import com.iyad.sultan.kooraalyawm.HomePage.FragmentSearch;
import com.iyad.sultan.kooraalyawm.HomePage.HomePageAdapter;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity  {

    private static final int PREMISSION_REQUEST_CODE = 101;
    private static final int REQUEST_CODE = 301;
    private static final String TAG = "MainActivity" ;

    //UI
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private HomePageAdapter adapter;
    private Toolbar toolbar;
    //Auth
    private static boolean isLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkGooglePlayServices();

        //First Time show Splash
        showSplash();

        //draw UI getting ref
        drawUI();


        getToaken();
        //Load  resource background 1 usr not loging Or 2 user already login laod resources


    }




    @Override
    protected void onResume() {
        super.onResume();

    }

    private void drawUI(){
        viewPager = (ViewPager) findViewById(R.id.home_page_viewPager);
        viewPager.setOffscreenPageLimit(3);
        tabLayout =(TabLayout) findViewById(R.id.home_page_tabLayout);
        toolbar = (Toolbar) findViewById(R.id.home_page_toolBar);
     //   toolbar.setNavigationIcon(R.mipmap.ic_player_defualt);

        //set Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //CreateAdapter
        adapter = new HomePageAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentGroup(), getResources().getString(R.string.fragment_group));
        adapter.addFragment(new FragmentGame(), getResources().getString(R.string.fragment_game));
        adapter.addFragment(new FragmentSearch(), getResources().getString(R.string.fragment_search));
        viewPager.setAdapter(adapter);
        //set tab with view pager
        tabLayout.setupWithViewPager(viewPager);

    }
    private void showSplash()
    {
        startActivityForResult(new Intent(MainActivity.this,SplashActivity.class),REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //check permission
            checkPermission();

    }

       private boolean isUserSign() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return (user != null);
    }

//Permissions Handling

    public void checkPermission( ){

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted ?

            String [] requestedPermission = { Manifest.permission.ACCESS_FINE_LOCATION};
          ActivityCompat.requestPermissions(MainActivity.this,requestedPermission,PREMISSION_REQUEST_CODE);
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted ?

            String [] requestedPermission = { Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(MainActivity.this,requestedPermission,PREMISSION_REQUEST_CODE);
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted ?

            String [] requestedPermission = { Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(MainActivity.this,requestedPermission,PREMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PREMISSION_REQUEST_CODE){
            int count = 0;
           for(int result : grantResults){


               if(result == 0){
                  count ++;
               }
           }//loop
            if(count > 0)
                showAlert();


        }
    }

    private void showAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Permissions needed, PLease Exit App ant try again ");
        alertDialog.create();
    }



    private void checkGooglePlayServices(){

        GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(!task.isSuccessful()) {
                    Log.w(TAG, task.getException());
                    return;
                }

                Log.d(TAG, "Google Play Service Available");
            }
        });

    }



    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null)
            return;

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("players").child(user.getUid()).child("pushToken");



        mRef.setValue(token).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(!task.isSuccessful()){
                    Log.w(TAG,"Store Token failed" +task.getException());
                    return;
                }
                Log.d(TAG,"Store Token successfully to User Info");
            }
        });
    }
    private void getToaken(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {

                if (!task.isSuccessful()) {
                    Log.w(TAG, "getInstanceId failed", task.getException());
                    return;
                }

                //Get a Token
                String token = task.getResult().getToken();

                String meg = "Token ID: " + token;
                Log.d(TAG,meg);
                sendRegistrationToServer(token);



            }
        });
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
    }
}
