package com.iyad.sultan.kooraalyawm.Game;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.iyad.sultan.kooraalyawm.Dumb.DumbGen;
import com.iyad.sultan.kooraalyawm.Groups.PlayerAdapter;
import com.iyad.sultan.kooraalyawm.IHelper;
import com.iyad.sultan.kooraalyawm.Model.GameAttendees;
import com.iyad.sultan.kooraalyawm.Model.Player;
import com.iyad.sultan.kooraalyawm.R;

import java.util.List;

public class GameDetailsActivity extends AppCompatActivity  {



    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mTxtExitGroup;
    private FloatingActionButton mBtnCreateGame;

    //Data
    private PlayerGameAdapter mAdapter;
    private List<GameAttendees> mAttendees;
    private static final String GAME_ID = "GAME_ID_UNIQUE";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_deatils_activity);




        //else draw UI getting ref
        drawUI();


      //  Toast.makeText(this, "You passed Group with Id : "+ getIntent().getStringExtra(GROUP_ID), Toast.LENGTH_SHORT).show();

    }


    private void drawUI( ) {

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


    //** Load Data from FireBase **//
    private List<GameAttendees> getPlayerOfThisGame(String GameKey){

        return new DumbGen().getGameAttendees("gameKey");


    }






}
