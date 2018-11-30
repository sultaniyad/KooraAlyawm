package com.iyad.sultan.kooraalyawm;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.iyad.sultan.kooraalyawm.HomePage.FragmentGame;
import com.iyad.sultan.kooraalyawm.HomePage.FragmentGroup;
import com.iyad.sultan.kooraalyawm.HomePage.FragmentSearch;
import com.iyad.sultan.kooraalyawm.HomePage.HomePageAdapter;

public class MainActivity extends AppCompatActivity  {
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
        Toast.makeText(this, "check if auth", Toast.LENGTH_SHORT).show();
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
        startActivity(new Intent(MainActivity.this,SplashActivity.class));
    }



}
