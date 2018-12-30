package com.iyad.sultan.kooraalyawm.Game;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iyad.sultan.kooraalyawm.Dumb.DumbGen;
import com.iyad.sultan.kooraalyawm.Groups.PlayerAdapter;
import com.iyad.sultan.kooraalyawm.IHelper;
import com.iyad.sultan.kooraalyawm.Model.GameAttendees;
import com.iyad.sultan.kooraalyawm.Model.Player;
import com.iyad.sultan.kooraalyawm.R;

import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.iyad.sultan.kooraalyawm.Utilities.Constants.GAME_DETAILS_PATH;
import static com.iyad.sultan.kooraalyawm.Utilities.Constants.GAME_PATH;
import static com.iyad.sultan.kooraalyawm.Utilities.Constants.GROUP_PATH;
import static com.iyad.sultan.kooraalyawm.Utilities.Constants.PLAYER_PATH;

public class GameDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Constant
    public  static final String GAME_LOCATION = "GAME_LOCATION";
    public  static final String GAME_ID = "GAME_ID";
    public  static final String REQURIED_NUMBER = "REQURIED_NUMBER";
    public  static final String Registered = "Registered";
    private static final String PLAYER_ICON_FOR_NEW_GAME = "PLAYER_ICON_FOR_NEW_GAME";

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";



    //UI
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mTxtExitGroup;
    private FloatingActionButton mBtnExitGame;
    private FloatingActionButton btnJoinGame;
    private View rootLayout;

    //Data
    private PlayerGameAdapter mAdapter;
    private List<GameAttendees> mAttendees = new ArrayList<GameAttendees>();
    private String gameId ;
    private String location ;
    private String required ;
    private String current ;
    private String icon;
 //   private static final String GAME_ID = "GAME_ID_UNIQUE";

    //Map
    private MapView mMapView;
    private LatLng mLocation;

    //FireBase
    private DatabaseReference gameDetailsRefr ;
    private FirebaseUser user;

    private DatabaseReference mRootRef;
    private DatabaseReference mGameRef;


    private DatabaseReference mPlayerRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_deatils_activity);

        loadGoogleMap(savedInstanceState);

        //else draw UI getting ref
        drawUI();

        gameId = getIntent().getStringExtra(GAME_ID);
        location = getIntent().getStringExtra(GAME_LOCATION);
        required = getIntent().getStringExtra(REQURIED_NUMBER);
        current = getIntent().getStringExtra(Registered);
        icon = getIntent().getStringExtra(PLAYER_ICON_FOR_NEW_GAME);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mRootRef = FirebaseDatabase.getInstance().getReference();

        gameDetailsRefr = mRootRef.child(GAME_DETAILS_PATH).child(gameId);
        mGameRef = mRootRef.child(GAME_PATH).child(gameId).child("rolling");


        if(gameId != null)
            loaddata();

        //  Toast.makeText(this, "You passed Group with Id : "+ getIntent().getStringExtra(GROUP_ID), Toast.LENGTH_SHORT).show();

    }


    private void drawUI() {

        mToolbar = (Toolbar) findViewById(R.id.game_details_toolBar);
        // mTxtExitGroup = (TextView) findViewById(R.id.game_detail_exit_group);
        rootLayout = findViewById(R.id.root_layout_game_details);
        mRecyclerView = (RecyclerView) findViewById(R.id.game_details_rec);

        mBtnExitGame = findViewById(R.id.exit_game);
        btnJoinGame = findViewById(R.id.join_game);

        //Exit the game

        mBtnExitGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String,Object> status = new HashMap<String,Object>();
                status.put("attend", false);

               DatabaseReference attendRef = gameDetailsRefr.child(user.getUid());
                attendRef.updateChildren(status);
                showResult("Change Status to Unable to attend");
                hideFAB(mBtnExitGame);
            }
        });


        btnJoinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*
                showResult("Change Status to attending");
                Map<String,Object> status = new HashMap<String,Object>();
                status.put("attend", true);
                DatabaseReference attendRef = gameDetailsRefr.child(user.getUid());
                attendRef.updateChildren(status);
                hideFAB(btnJoinGame);
                */

                addPLayerToThisGame();
            }
        });
        //setToolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //load
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());




    }

    private void addPLayerToThisGame() {


        //Add Player to Game Node Rolling
        Map<String,Object> playerId = new HashMap<>();
        playerId.put(user.getUid(),true);

        mGameRef.updateChildren(playerId).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful())
                    Toast.makeText(GameDetailsActivity.this, "mmm something went wrong D:", Toast.LENGTH_SHORT).show();
            }
        });

        //Add Player details to Game Detauls Node

        GameAttendees attendees = new GameAttendees();
        attendees.setPlayerid(user.getUid());
        attendees.setPlayername(user.getDisplayName());
        attendees.setPlayericon(icon);
        attendees.setAttend(true);
        attendees.setJoindate(getDateNow());

        Map<String,Object> player = new HashMap<>();
        player.put(user.getUid(),attendees);


        gameDetailsRefr.updateChildren(player).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    showResult("joined Game");
            }
        });

    }

    private String getDateNow() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);

        return year + "/" + (month+1) +" " + day + " " + hour + ":"+ min;
    }

    private  void isPlayerJoin(List<GameAttendees> list){

        int cuurent = 0;
       // int max =  re
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



        String [] lol = location.split(",");
        double v1 = Double.parseDouble(lol[0]);
        double v2 = Double.parseDouble(lol[1]);
        LatLng stadiumLocation = new LatLng(v1, v2);


       // LatLng latLng =  LatLng.CREATOR
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

    private void  loaddata(){
        //Load Data from FireBase

        gameDetailsRefr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mAttendees.clear();
                for (DataSnapshot snap : dataSnapshot.getChildren())
                {

                    GameAttendees game = snap.getValue(GameAttendees.class);
                    mAttendees.add(game);
                }


//////////////////////////////////
                if(dataSnapshot.child(user.getUid()).child("attend").exists()) {
                   // hideFAB(btnJoinGame);
                  //  showFAB(mBtnExitGame);

                    Boolean isJoin = (Boolean) dataSnapshot.child(user.getUid()).child("attend").getValue();
                    //already exit from game
                    if(!isJoin) {
                        hideFAB(btnJoinGame);
                        hideFAB(mBtnExitGame);
                    }
                    else {
                         hideFAB(btnJoinGame);
                       // hideFAB(mBtnExitGame);
                    }
                }

                else
                {
                    hideFAB(mBtnExitGame);
                    showFAB(btnJoinGame);
                }
              //  Boolean isJoin = (Boolean) dataSnapshot.child(user.getUid()).child("attend").getValue();


                //if (isJoin)
               //     hideFAB(btnJoinGame);

               // else hideFAB(mBtnExitGame);


                mAdapter = new PlayerGameAdapter(mAttendees);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showResult(String meg) {

        Snackbar snackbar = Snackbar.make(rootLayout, meg, Snackbar.LENGTH_LONG);
        snackbar.show();

    }

    @SuppressLint("RestrictedApi")
    private void hideFAB(FloatingActionButton FAB){
        //FAB.hide();
        FAB.setVisibility(View.INVISIBLE);
    }
    @SuppressLint("RestrictedApi")
    private void showFAB(FloatingActionButton FAB){
        //FAB.hide();
        FAB.setVisibility(View.VISIBLE);
    }





    //Call Back


    interface  onGameAttend{
        void onAttendChange();
    }

}
