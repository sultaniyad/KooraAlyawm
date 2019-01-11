package com.iyad.sultan.kooraalyawm.HomePage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.nsd.NsdManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iyad.sultan.kooraalyawm.Dumb.DumbGen;
import com.iyad.sultan.kooraalyawm.Game.GameAdapter;
import com.iyad.sultan.kooraalyawm.Game.GameDetailsActivity;
import com.iyad.sultan.kooraalyawm.Groups.UserGroupAdapter;
import com.iyad.sultan.kooraalyawm.Model.Game;
import com.iyad.sultan.kooraalyawm.Model.Group;
import com.iyad.sultan.kooraalyawm.Model.Player;
import com.iyad.sultan.kooraalyawm.R;
import com.iyad.sultan.kooraalyawm.Sign.SignIn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.iyad.sultan.kooraalyawm.Utilities.Constants.GAME_PATH;
import static com.iyad.sultan.kooraalyawm.Utilities.Constants.GROUP_PATH;
import static com.iyad.sultan.kooraalyawm.Utilities.Constants.PLAYER_PATH;

public class FragmentGame extends Fragment implements GameAdapter.OnSelectedGame {

    public static final String GAME_LOCATION = "GAME_LOCATION";
    public static final String GAME_ID = "GAME_ID";
    public static final String REQURIED_NUMBER = "REQURIED_NUMBER";
    public static final String Registered = "Registered";
    private static final String PLAYER_ICON_FOR_NEW_GAME = "PLAYER_ICON_FOR_NEW_GAME";
    private static final String GAME_FEES = "GAME_FEES";
    private FirebaseAuth mAuth;

    private RecyclerView mRecyclerView;
    private View v;
    private AppCompatButton btnLogin;
    //Data
    private GameAdapter mAdapter;

    //Firebase
    //FireBase
    private DatabaseReference mRootRef;
    private DatabaseReference mGroupRef;
    private DatabaseReference mGameRef;
    private DatabaseReference mPlayerRef;
    private FirebaseUser user;

    //Data
    private Player mCurrentPlayer;
    private List<String> gameIDs = new ArrayList<String>();
    private List<Game> mDataSet = new ArrayList<>();

    public FragmentGame() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.game_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user != null) {

            mRootRef = FirebaseDatabase.getInstance().getReference();
            //   mGroupRef = mRootRef.child(GROUP_PATH);
            mGameRef = mRootRef.child(GAME_PATH);
            mPlayerRef = mRootRef.child(PLAYER_PATH).child(user.getUid());

            getPlayerGames();
        }

        //Inti
        drawUI();

        if (user == null)
            goToLogin();

        return v;
    }

    private void getPlayerGames() {

        mPlayerRef.child("games").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gameIDs.clear();
                mDataSet.clear();

                mCurrentPlayer = dataSnapshot.getValue(Player.class);
                //This contains a list of group keys associated to the user;
                Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                for(DataSnapshot gameID : iterable){

                    mGameRef.child(gameID.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Game game = dataSnapshot.getValue(Game.class);

                            assert game != null;
                            if(isValidGame(game.getDate())) {
                                mDataSet.add(game);
                                mAdapter.notifyDataSetChanged();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    boolean isValidGame(String date){

       @SuppressLint("SimpleDateFormat")
       SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        try {

            Date mGameDate = format.parse(date);

            Calendar cal = Calendar.getInstance();
            Date currentDate = format.parse( cal.get(Calendar.DAY_OF_MONTH) + "-" + (cal.get(Calendar.MONTH)+1) +
                    "-" + cal.get(Calendar.YEAR)  + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));




            return currentDate.before(mGameDate);


// "31-12-2018 11:30"

        } catch (ParseException e) {
            e.printStackTrace();
            return  false;
        }


     }
    private void drawUI() {


        btnLogin = v.findViewById(R.id.btn_go_to_login2);
        mRecyclerView = v.findViewById(R.id.game_rec_short);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new GameAdapter(mDataSet, this);
        mRecyclerView.setAdapter(mAdapter);


    }

    private void updateUI(FirebaseUser user) {

        //get data from this user
        if (user != null) {
            btnLogin.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            ///here
        } else {
            mRecyclerView.setVisibility(View.INVISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
        }
    }

    //When User not signIn and Skip it phase
    private void goToLogin() {


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(v.getContext(), SignIn.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        updateUI(user);
        if (user != null)
            //loadData();
            ;
    }

    //Load Data from FireBase db

    @Override
    public void getSelectedGame(int position) {
        //pass game key and another  info
        Intent i = new Intent(getContext(), GameDetailsActivity.class);
        Game game = mDataSet.get(position);
        i.putExtra(GAME_ID, game.getId());
        i.putExtra(GAME_LOCATION, game.getLocation());
        i.putExtra(REQURIED_NUMBER, game.getRequirednumber());
        i.putExtra(Registered, game.getRegistered());
        i.putExtra(GAME_FEES,game.getFees());

        i.putExtra(PLAYER_ICON_FOR_NEW_GAME, mCurrentPlayer.getIcon());
        startActivity(i);
      //  Toast.makeText(getContext(), "" + position, Toast.LENGTH_SHORT).show();
    }


    private void loadGameData(List<String> gameIDs) {


      //  mDataSet.clear();

        for (String key : gameIDs)
            mGameRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Game game = dataSnapshot.getValue(Game.class);
                    mDataSet.add(game);
                    //end loop call update UI
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


    }









}
