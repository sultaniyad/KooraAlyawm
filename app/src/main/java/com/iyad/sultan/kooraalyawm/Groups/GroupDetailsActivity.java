package com.iyad.sultan.kooraalyawm.Groups;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.iyad.sultan.kooraalyawm.Dumb.DumbGen;
import com.iyad.sultan.kooraalyawm.Game.AddGame;
import com.iyad.sultan.kooraalyawm.IHelper;
import com.iyad.sultan.kooraalyawm.Model.Player;
import com.iyad.sultan.kooraalyawm.R;

import java.util.List;

public class GroupDetailsActivity extends AppCompatActivity implements PlayerAdapter.CaptianAction {



    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mTxtExitGroup;
    private FloatingActionButton mBtnCreateGame;

    //Data
    private PlayerAdapter mAdapter;
    private List<Player> playersList;
    private static final String GROUP_ID = "GROUP_ID_UNIQUE";
    private String groupId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_deatils_activity);

        groupId = getIntent().getStringExtra(GROUP_ID);
        //check later :)
        if(groupId == null)
            finish();


        //else draw UI getting ref
        drawUI(groupId);


      //  Toast.makeText(this, "You passed Group with Id : "+ getIntent().getStringExtra(GROUP_ID), Toast.LENGTH_SHORT).show();

    }


    private void drawUI(String mGroupId){

        mToolbar = (Toolbar) findViewById(R.id.group_details_toolBar);
        mTxtExitGroup = (TextView) findViewById(R.id.group_detail_exit_group);
        mBtnCreateGame = (FloatingActionButton) findViewById(R.id.btn_start_new_game);
        mRecyclerView =(RecyclerView) findViewById(R.id.group_details_rec);

        //setToolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //load
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //Load Data from FireBase
        playersList = getGroupPlayers(mGroupId);
        mAdapter = new PlayerAdapter(playersList,this);
        mRecyclerView.setAdapter(mAdapter);

        //On Click exit from Group
        mTxtExitGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GroupDetailsActivity.this, "Are sure want Exit Group ?", Toast.LENGTH_SHORT).show();
            }
        });

        //Start a new Game
        mBtnCreateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame();
            }
        });

    }


    //** Load Data from FireBase **//
    private List<Player> getGroupPlayers(String GroupId){

        return new DumbGen().test();
                //new DumbGen().createMeneberOfGroup(GroupId);

    }

    //interface : Captain Action
    @Override
    public void demotePlayerAsCaptain(int position) {

        Toast.makeText(this, "you demote player on" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void promotePlayerAsCaptain(int position) {
        Toast.makeText(this, "you promote player on" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void kickPlayer(int position) {
        Toast.makeText(this, "you Kick player on" + position, Toast.LENGTH_SHORT).show();
    }


    //On Clicked
    private void startNewGame(){
        IHelper helper = new IHelper();
       boolean isPlayerCaptain =  helper.isPlayerCaptainInThisTeam("playerKey","groupKey");

       if(isPlayerCaptain){
           Toast.makeText(this, "start a game", Toast.LENGTH_SHORT).show();
           //Start a new Game
         Intent i = new Intent(GroupDetailsActivity.this,AddGame.class);
         i.putExtra(GROUP_ID,groupId);
          startActivity(i);

       }
       else
           Toast.makeText(this, getResources().getString(R.string.no_priv_to_create_game), Toast.LENGTH_SHORT).show();
    }


}
