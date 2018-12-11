package com.iyad.sultan.kooraalyawm.Game;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.iyad.sultan.kooraalyawm.Dumb.DumbGen;
import com.iyad.sultan.kooraalyawm.Groups.PlayerAdapter;
import com.iyad.sultan.kooraalyawm.IHelper;
import com.iyad.sultan.kooraalyawm.Model.GameAttendees;
import com.iyad.sultan.kooraalyawm.Model.Player;
import com.iyad.sultan.kooraalyawm.R;

import java.util.List;

public class GameDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Constant
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";


    //UI
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mTxtExitGroup;
    private FloatingActionButton mBtnCreateGame;

    //Data
    private PlayerGameAdapter mAdapter;
    private List<GameAttendees> mAttendees;
    private static final String GAME_ID = "GAME_ID_UNIQUE";

    //Map
    private MapView mMapView;
    private LatLng mLocation;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_deatils_activity);

        loadGoogleMap(savedInstanceState);


        //else draw UI getting ref
        drawUI();


        //  Toast.makeText(this, "You passed Group with Id : "+ getIntent().getStringExtra(GROUP_ID), Toast.LENGTH_SHORT).show();

    }


    private void drawUI() {

        mToolbar = (Toolbar) findViewById(R.id.game_details_toolBar);
        // mTxtExitGroup = (TextView) findViewById(R.id.game_detail_exit_group);

        mRecyclerView = (RecyclerView) findViewById(R.id.game_details_rec);

        //setToolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //load
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //Load Data from FireBase

        mAttendees = getPlayerOfThisGame("");
        mAdapter = new PlayerGameAdapter(mAttendees);
        mRecyclerView.setAdapter(mAdapter);


    }


    private void loadGoogleMap(Bundle savedInstanceState) {

        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    //Call Back
    @Override
    public void onMapReady(GoogleMap map) {

        LatLng stadiumLocation = new LatLng(-33.852, 151.211);

        map.addMarker(new MarkerOptions().position(stadiumLocation).title("Stadium"));
        //zoom level
        map.moveCamera(CameraUpdateFactory.newLatLng(stadiumLocation));
/*
        map.addMarker(new MarkerOptions()
                .position(new LatLng(-27.47093, 153.0235)).title("Perth"));*/

    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    //** Load Data from FireBase **//
    private List<GameAttendees> getPlayerOfThisGame(String GameKey){

        return new DumbGen().getGameAttendees("gameKey");


    }






}
