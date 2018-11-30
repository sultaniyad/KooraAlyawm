package com.iyad.sultan.kooraalyawm.Game;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.iyad.sultan.kooraalyawm.Model.GameAttendees;
import com.iyad.sultan.kooraalyawm.R;

import java.util.List;

public class PlayerGameAdapter extends RecyclerView.Adapter<PlayerGameAdapter.MyViewHolder> {

    //Data
    private List<GameAttendees> mDataset;

    public PlayerGameAdapter(List<GameAttendees> mDataset){
        this.mDataset = mDataset;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.player_game_row_item,viewGroup,false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {


        GameAttendees player = mDataset.get(i);
        myViewHolder.mPlayerName.setText(player.getPlayer().getPlayerName());

        //color green
        if(player.getIsAttend()){
            TextView tv = myViewHolder.isAttend;
            tv.setTextColor(Color.GREEN);
            tv.setText("Attend");
        }
        //color red
        else {

            TextView tv = myViewHolder.isAttend;
            tv.setTextColor(Color.RED);
            tv.setText("Canceled");
        }

        //set Images
        ImageView mPlayerIcon = myViewHolder.mPlayerIcon;
        Glide.with(mPlayerIcon.getContext()).load(player.getPlayer().getPlayerIcon()).apply(new RequestOptions().circleCrop().placeholder(R.drawable.game_icon_tab).error(R.drawable.ic_player_error_loading)).into(mPlayerIcon);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView mPlayerIcon ;
        private TextView mPlayerName;
        private TextView isAttend;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mPlayerIcon = itemView.findViewById(R.id.game_img_player_game_icon);
            mPlayerName = itemView.findViewById(R.id.game_txt_player_name);
            isAttend = itemView.findViewById(R.id.game_txt_player_confirm_attendance);
        }
    }
}
