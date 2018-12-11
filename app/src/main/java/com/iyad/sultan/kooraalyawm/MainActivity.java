package com.iyad.sultan.kooraalyawm;

import android.Manifest;
import android.app.Activity;
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
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iyad.sultan.kooraalyawm.HomePage.FragmentGame;
import com.iyad.sultan.kooraalyawm.HomePage.FragmentGroup;
import com.iyad.sultan.kooraalyawm.HomePage.FragmentSearch;
import com.iyad.sultan.kooraalyawm.HomePage.HomePageAdapter;

public class MainActivity extends AppCompatActivity  {

    private static final int PREMISSION_REQUEST_CODE = 101;
    private static final int REQUEST_CODE = 301;

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


        //First Time show Splash
        showSplash();

        //draw UI getting ref
        drawUI();


        //Load  resource background 1 usr not loging Or 2 user already login laod resources


    }




    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "check if auth MainActivity Resume", Toast.LENGTH_SHORT).show();
    }

    private void drawUI(){
        viewPager = (ViewPager) findViewById(R.id.home_page_viewPager);
        tabLayout =(TabLayout) findViewById(R.id.home_page_tabLayout);
        toolbar = (Toolbar) findViewById(R.id.home_page_toolBar);

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
        isUserSign();
        checkPremissin("");
    }

    private boolean isUserSign() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return (user != null);
    }

//Permissions Handling

    public void checkPremissin(String permission){
        String perm = "" + Manifest.permission.WRITE_CALENDAR;
        if (ContextCompat.checkSelfPermission(MainActivity.this, perm)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            //Request it
            String [] prem = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                 Manifest.permission.ACCESS_FINE_LOCATION};
          ActivityCompat.requestPermissions(MainActivity.this,prem,PREMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PREMISSION_REQUEST_CODE){

            for(int i = 0;i< grantResults.length ; i++){

                Toast.makeText(this, "" + permissions[i] + " Status " + grantResults[i], Toast.LENGTH_SHORT).show();
            }


        }
    }
}
