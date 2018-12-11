package com.iyad.sultan.kooraalyawm.HomePage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.iyad.sultan.kooraalyawm.Dumb.DumbGen;
import com.iyad.sultan.kooraalyawm.Game.GameAdapter;
import com.iyad.sultan.kooraalyawm.Game.GameDetailsActivity;
import com.iyad.sultan.kooraalyawm.Model.Game;
import com.iyad.sultan.kooraalyawm.R;
import com.iyad.sultan.kooraalyawm.Sign.SignIn;

import java.util.List;

public class FragmentGame extends Fragment implements GameAdapter.OnSelectedGame {

    private FirebaseAuth mAuth;

    private RecyclerView mRecyclerView;
    private View v;
    private AppCompatButton btnLogin;
    //Data
    private GameAdapter mAdapter;
    private List<Game> mUserGames;
    public FragmentGame() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         v = inflater.inflate(R.layout.game_fragment,container, false);

         mAuth = FirebaseAuth.getInstance();

        drawUI();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null)
            goToLogin();

        return v;
    }

    private void drawUI() {


        btnLogin = v.findViewById(R.id.btn_go_to_login2);
        mRecyclerView = v.findViewById(R.id.game_rec_short);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


    }
    private void updateUI(FirebaseUser user){

        //get data from this user
        if(user != null) {
            btnLogin.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

            mUserGames = getUserGames();
            mAdapter = new GameAdapter(mUserGames, this);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            mRecyclerView.setVisibility(View.INVISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
        }
    }
    //When User not signIn and Skip it phase
    private void goToLogin(){


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(v.getContext(), SignIn.class);
                startActivity(i);
            }
        });}

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        updateUI(user);
    }

    //Load Data from FireBase db
    private List<Game> getUserGames() {

        DumbGen g = new DumbGen();
        return g.createGames("player1");
    }


    @Override
    public void getSelectedGame(int position) {
        //pass game key and another  info
        Intent i = new Intent(getContext(),GameDetailsActivity.class);
        startActivity(i);
        Toast.makeText(getContext(), ""+ position, Toast.LENGTH_SHORT).show();
    }
}
