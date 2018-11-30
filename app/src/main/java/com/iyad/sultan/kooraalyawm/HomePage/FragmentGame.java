package com.iyad.sultan.kooraalyawm.HomePage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iyad.sultan.kooraalyawm.Dumb.DumbGen;
import com.iyad.sultan.kooraalyawm.Game.GameAdapter;
import com.iyad.sultan.kooraalyawm.Game.GameDetailsActivity;
import com.iyad.sultan.kooraalyawm.Model.Game;
import com.iyad.sultan.kooraalyawm.R;

import java.util.List;

public class FragmentGame extends Fragment implements GameAdapter.OnSelectedGame {

    private RecyclerView mRecyclerView;
    //Data
    private GameAdapter mAdapter;
    private List<Game> mUserGames;
    public FragmentGame() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.game_fragment,container, false);

        drawUI(v);



        return v;
    }

    private void drawUI(View v) {

        mUserGames = getUserGames();
        mRecyclerView = v.findViewById(R.id.game_rec_short);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new GameAdapter(mUserGames,this);
        mRecyclerView.setAdapter(mAdapter);

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
