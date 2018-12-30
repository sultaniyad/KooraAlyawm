package com.iyad.sultan.kooraalyawm.Groups;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
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
import com.iyad.sultan.kooraalyawm.Game.AddGame;
import com.iyad.sultan.kooraalyawm.IHelper;
import com.iyad.sultan.kooraalyawm.Model.Group;
import com.iyad.sultan.kooraalyawm.Model.Player;
import com.iyad.sultan.kooraalyawm.R;
import com.iyad.sultan.kooraalyawm.Utilities.PlayerCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.iyad.sultan.kooraalyawm.Utilities.Constants.GROUP_PATH;
import static com.iyad.sultan.kooraalyawm.Utilities.Constants.PLAYER_PATH;

public class GroupDetailsActivity extends AppCompatActivity implements PlayerAdapter.CaptianAction, PlayerCallBack.MyCallback {


    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mTxtExitGroup;
    private FloatingActionButton mBtnCreateGame;
    private TextView mTextGroupName;
    private ImageView mLogo;
    private CoordinatorLayout rootLayout;

    //Data
    private PlayerAdapter mAdapter;
    private List<Player> playersList;
    private String groupId;
    private String groupName;
    private String groupLogo;
    private Map<String, Object> groupCaptains;
    private int groupCurrentPlayer;
    private int groupCurrentCaptain;

    //Con
    private static final String GROUP_ID = "GROUP_ID_UNIQUE";
    private static final String GROUP_NAME = "GROUP_NAME";
    private static final String PLAYER_ICON_FOR_NEW_GAME = "PLAYER_ICON_FOR_NEW_GAME";
    private static final String GROUP_LOGO = "GROUP_LOGO";
    private static final String GROUP_Current_PLAYER = "GROUP_Current_PLAYER";
    private static final String GROUP_Current_Captain_MAP = "GROUP_Current_Captain_MAP";
    private static final String GROUP_Current_Captain = "GROUP_Current_Captain";


    private static final String IS_PLAYER_CAPTAIN = "IS_PLAYER_CAPTAIN";
    private static final String CURRENT_CAPTAIN = "CURRENT_CAPTAIN";


    //Firebase
    private DatabaseReference mRoot;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_deatils_activity);


        Intent intent = getIntent();
        //get User
        groupId = intent.getStringExtra(GROUP_ID);
        groupName = intent.getStringExtra(GROUP_NAME);
        groupLogo = intent.getStringExtra(GROUP_LOGO);
        groupCurrentPlayer = intent.getIntExtra(GROUP_Current_PLAYER, 0);
        groupCurrentCaptain = intent.getIntExtra(GROUP_Current_Captain, 0);
        //can not be empty : no will start a game
        groupCaptains = (Map<String, Object>) intent.getSerializableExtra(GROUP_Current_Captain_MAP);

        if (groupId == null)
            finish();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        //check later :)


        mRoot = FirebaseDatabase.getInstance().getReference();

//****************************///
        //   groupId = "group1";
        //else draw UI getting ref
        drawUI(groupId);


    }


    private void drawUI(String mGroupId) {

        rootLayout = findViewById(R.id.main_content);
        mToolbar = (Toolbar) findViewById(R.id.group_details_toolBar);
        mTxtExitGroup = (TextView) findViewById(R.id.group_detail_exit_group);

        mTextGroupName = findViewById(R.id.group_detail_txt_group_name);
        mTextGroupName.setText(groupName);

        mLogo = findViewById(R.id.group_backdrop);
        Glide.with(mLogo.getContext()).load(groupLogo).into(mLogo);

        mBtnCreateGame = (FloatingActionButton) findViewById(R.id.btn_start_new_game);
        mRecyclerView = (RecyclerView) findViewById(R.id.group_details_rec);

        //setToolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        //load
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


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
    private List<Player> getGroupPlayers(String GroupId) {

        return new DumbGen().test();
        //new DumbGen().createMeneberOfGroup(GroupId);

    }

    //interface : Captain Action
    @Override
    public void demotePlayerAsCaptain(final int position) {
        validateDemotePlayer(this, position);
    }

    //validate if player is Captain  Action : Demote
    private void validateDemotePlayer(final PlayerCallBack.MyCallback callBack, final int position) {

        final DatabaseReference mRef = mRoot.child(PLAYER_PATH).child(user.getUid()).child("privilege").child(groupId);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //is captain
                    callBack.demotePlayerAsCaptainCallBack(true, position);

                } else {
                    callBack.demotePlayerAsCaptainCallBack(false, position);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void promotePlayerAsCaptain(final int position) {

        validatePromotePlayer(this, position);


    }


    //Offline work
    void updateDataSetOffline(int position, int requestCode) {

        switch (requestCode) {
            //Promote
            case 111: {
                Player player = playersList.get(position);
                player.getPrivilege().put(groupId, true);
                mAdapter.notifyItemChanged(position);
            }

            return;
            //Demote
            case 112: {
                Player player = playersList.get(position);
                player.getPrivilege().remove(groupId);
                mAdapter.notifyItemChanged(position);
            }
            return;
            //Kik
            case 113: {
                playersList.remove(position);
                mAdapter.notifyItemRemoved(position);
            }
            return;
        }
        playersList.remove(position);
        mAdapter.notifyItemChanged(position);

        // mAdapter.notifyDataSetChanged();

    }

    //On Clicked
    private void startNewGame() {
/*
        final DatabaseReference mRef = mRoot.child(PLAYER_PATH).child(user.getUid()).child("privilege").child(groupId);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    //is captain
                    Intent i = new Intent(GroupDetailsActivity.this, AddGame.class);
                    i.putExtra(GROUP_ID, groupId);
                    i.putExtra(GROUP_NAME,groupName);

                    i.putExtra(PLAYER_ICON_FOR_NEW_GAME,"");
                    startActivity(i);

                } else {GROUP_Current_PLAYER
                    Toast.makeText(GroupDetailsActivity.this, "No Privilege", Toast.LENGTH_SHORT).show();
                }
            }*/
        final DatabaseReference mRef = mRoot.child(PLAYER_PATH).child(user.getUid());
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Map<String,Object> privilege = (Map<String, Object>) dataSnapshot.child("privilege").getValue();
                    if(privilege != null){
                      Object obj = privilege.get(groupId);
                      if(obj != null){

                          //is captain
                          Intent i = new Intent(GroupDetailsActivity.this, AddGame.class);
                          i.putExtra(GROUP_ID, groupId);
                          i.putExtra(GROUP_NAME,groupName);
                           String icon = dataSnapshot.child("icon").getValue(String.class);
                          i.putExtra(PLAYER_ICON_FOR_NEW_GAME,icon);
                          startActivity(i);
                      }
                   }

                   else
                    Toast.makeText(GroupDetailsActivity.this, "No Privilege", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    void getUserIDs() {

        final DatabaseReference mRef = mRoot.child(GROUP_PATH).child(groupId).child("members");

        final List<String> IDs = new ArrayList<>();

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {

                    String playerId = snap.getKey();
                    IDs.add(playerId);
                }

                loadPlayerOfThisGroup(IDs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void loadPlayerOfThisGroup(List<String> playerIDs) {

        //get data only for this group !!!!!!!!
        final List<Player> list = new ArrayList<>();
        //User group keys
        DatabaseReference mRef = mRoot.child(PLAYER_PATH);
//loop and get player of groups
        for (int i = 0; i < playerIDs.size(); i++)

            mRef.child(playerIDs.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Player mPlayer = dataSnapshot.getValue(Player.class);
                    list.add(mPlayer);
                    //pass data to adapter
                    updateAdapter(list, groupId);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            }); //end loop


    }

    private void updateAdapter(List<Player> list, String mGroupId) {


        //Load Data from FireBase
        playersList = list;
        mAdapter = new PlayerAdapter(playersList, this, mGroupId);
        mRecyclerView.setAdapter(mAdapter);

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)
            getUserIDs();
    }


    //validate if player is Captain  Action : Promote
    public void validatePromotePlayer(final PlayerCallBack.MyCallback callback, final int postion) {


        final DatabaseReference mRef = mRoot.child(PLAYER_PATH).child(user.getUid()).child("privilege").child(groupId);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //is captain
                    callback.promotePlayerAsCaptainCallBack(true, postion);


                } else {
                    callback.promotePlayerAsCaptainCallBack(false, postion);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    //Call back
    @Override
    public void promotePlayerAsCaptainCallBack(Boolean isCaptain, final int position) {

        if (!isCaptain) {
           // Toast.makeText(this, "No Privilege founded", Toast.LENGTH_SHORT).show();
            showMessage("No Privilege founded");
            return;
        }

        user = mAuth.getCurrentUser();
        if (user == null)
            return;

        //Promote Player
        //add group key from player Node in privilege (not necessary check first cause fire base will not added if exist)
        // Last admin must exit !!!!
        if (playersList.size() < 1) {
            //Toast.makeText(this, "Error: Player List is empty", Toast.LENGTH_SHORT).show();
            showMessage("Error: Player List is empty");
            return;
        }

        Player player = playersList.get(position);

        //if im here user is Captain

        Map<String, Object> newPrivilege = new HashMap<String, Object>();
        newPrivilege.put(groupId, true);

        //update UI First User experience
        updateDataSetOffline(position, 111);
        final DatabaseReference mRef = mRoot.child(PLAYER_PATH).child(player.getId()).child("privilege");
        mRef.updateChildren(newPrivilege).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                   // Toast.makeText(getApplicationContext(), "you promote player on" + position, Toast.LENGTH_SHORT).show();
                    showMessage("Promoted");
                    //update UI
                    //    getUserIDs();


                }
            }
        });

        Map<String, Object> newPrivilegeG = new HashMap<String, Object>();
        newPrivilegeG.put(player.getId(), true);

        final DatabaseReference mRefG = mRoot.child(GROUP_PATH).child(groupId).child("privilege");
        mRefG.updateChildren(newPrivilegeG).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                   // Toast.makeText(getApplicationContext(), "add to Group privilege promote player on" + position, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void demotePlayerAsCaptainCallBack(Boolean isCaptain, final int position) {

        if (!isCaptain) {
          //  Toast.makeText(this, "no privieleg from demo", Toast.LENGTH_SHORT).show();
            showMessage("No privilege founded");
            return;
        }
        if (groupCaptains == null)
            return;

        if (playersList.size() < 1) {
            Toast.makeText(this, "Error :Player List is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        //remove group key from player Node in privilege

        Player player = playersList.get(position);


        Object isKickedPlayerCaptain = groupCaptains.get(player.getId());
        // != null mean is Captain
        if ((isKickedPlayerCaptain != null && groupCaptains.size() <= 1)) {
           // Toast.makeText(this, "you are last members", Toast.LENGTH_SHORT).show();
            showMessage("you are last members");
            return;
        }


        updateDataSetOffline(position, 112);

        final DatabaseReference mRef = mRoot.child(PLAYER_PATH).child(player.getId()).child("privilege").child(groupId);
        mRef.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                  //  Toast.makeText(getApplicationContext(), "you demote player on" + position, Toast.LENGTH_SHORT).show();
                    showMessage("Demoted");

                }
            }
        });

        final DatabaseReference mRefG = mRoot.child(GROUP_PATH).child(groupId).child("privilege").child(player.getId());
        mRefG.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                   // Toast.makeText(getApplicationContext(), "remove group privilege you demote player on" + position, Toast.LENGTH_SHORT).show();


                }
            }
        });

    }

    // Satr from here to,more
    @Override
    public void kickPlayer(final int position) {
        //remove group key from Node "Player " in both Members and privilege

        if (groupCaptains == null) {
            Toast.makeText(this, "Error Captains List is empty, this should not happened", Toast.LENGTH_SHORT).show();
            return;
        }
        //is Captain
        Object isCaptain = groupCaptains.get(user.getUid());

        if (isCaptain == null) {
           // Toast.makeText(this, "You don't have Privilege", Toast.LENGTH_SHORT).show();
            showMessage("No Privilege founded");
            return;
        }

        if (playersList.size() < 1) {
            Toast.makeText(this, "Player List is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        Player player = playersList.get(position);

        //Verify at least their is one Captain (Kik him self)

        Object isKickedPlayerCaptain = groupCaptains.get(player.getId());
        // != null mean is Captain
        if ((isKickedPlayerCaptain != null && groupCaptains.size() <= 1) && (groupCurrentPlayer > 1)) {
           // Toast.makeText(this, "you can not leave, Sorry :)", Toast.LENGTH_SHORT).show();
            showMessage("You are last Captain this group");
            return;
        }


        updateDataSetOffline(position, 113);


        final DatabaseReference mRefPrivilege = mRoot.child(PLAYER_PATH).child(player.getId()).child("privilege").child(groupId);
        mRefPrivilege.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                //    Toast.makeText(getApplicationContext(), " remove" + position + "from Player privilege Node", Toast.LENGTH_SHORT).show();
                    showMessage("Removed Successfully");
                }
            }
        });
        final DatabaseReference mRefGroup = mRoot.child(PLAYER_PATH).child(player.getId()).child("groups").child(groupId);
        mRefGroup.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //Toast.makeText(getApplicationContext(), " remove " + position + "from Player groups Node ", Toast.LENGTH_SHORT).show();

                }
            }
        });


        //remove player key from Node "group " in  members

        final DatabaseReference mRefGroupMemeber = mRoot.child(GROUP_PATH).child(groupId).child("members").child(player.getId());
        mRefGroupMemeber.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                  //  Toast.makeText(getApplicationContext(), "you remove player on" + position + "from group Node members ", Toast.LENGTH_SHORT).show();

                }
            }
        });

        final DatabaseReference mRefGroupPrivilege = mRoot.child(GROUP_PATH).child(groupId).child("privilege").child(player.getId());
        mRefGroupPrivilege.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                  //  Toast.makeText(getApplicationContext(), "you remove player on" + position + "from group Node Privilege ", Toast.LENGTH_SHORT).show();

                    // updateDataSetOffline(position);
                }
            }
        });


    }


    @Override
    public void kikPlayerCallBack(Boolean isCaptain, int position) {

    }


    private void showMessage(String meg) {
        Snackbar message = Snackbar.make(rootLayout, meg, Snackbar.LENGTH_LONG);
        message.show();
    }

}
