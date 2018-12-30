package com.iyad.sultan.kooraalyawm.Game;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iyad.sultan.kooraalyawm.Model.Game;
import com.iyad.sultan.kooraalyawm.R;

import java.text.DateFormat;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.MyViewHolder>  {

    public interface OnSelectedGame{
        void getSelectedGame(int position);
    }

    //interface
   private OnSelectedGame selectedGame;

    private List<Game> mDataset;
    public GameAdapter(List<Game> mDataset,OnSelectedGame mCallback ){
        this.mDataset = mDataset;
        this.selectedGame = mCallback;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = (View)LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.game_row_item,viewGroup,false);

       return  new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {


        Game g = mDataset.get(i);
        if(g != null) {
            myViewHolder.mGroupName.setText(g.getGroupname());
            myViewHolder.mStatium.setText(g.getStadium());
            //  myViewHolder.mGameDate.setText(DateFormat.getDateTimeInstance().format(g.getDate()));
            myViewHolder.mGameDate.setText(g.getDate());

            myViewHolder.mAttends.setText(g.getRolling().size() + "");
            //  myViewHolder.mCanceled.setText(g.getCanceled());
            myViewHolder.mRequired.setText(g.getRequirednumber());
            myViewHolder.mFee.setText(g.getFees() + " SAR");
        }



    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder{

        private CardView mCardView;
        private TextView mGroupName;
        private TextView mRequired;
        private TextView mAttends;
      //  private TextView mCanceled;
        private TextView mGameDate;
        private TextView mStatium;
        private TextView mFee;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mGroupName =  itemView.findViewById(R.id.game_txt_group_name);
            mRequired =  itemView.findViewById(R.id.txt_game_required_number);
            mAttends = itemView.findViewById(R.id.txt_game_attendance);
            mGameDate = itemView.findViewById(R.id.txt_game_date);
        //    mCanceled = itemView.findViewById(R.id.txt_canceled_player);
            mStatium = itemView.findViewById(R.id.stadium_name);
            mFee = itemView.findViewById(R.id.txt_game_fee);

            mCardView = itemView.findViewById(R.id.gameCardView);
            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    selectedGame.getSelectedGame(getAdapterPosition());
                }
            });
        }
    }
}
