package com.iyad.sultan.kooraalyawm.Groups;

import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.iyad.sultan.kooraalyawm.Model.Player;
import com.iyad.sultan.kooraalyawm.R;

import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerPlaceHolder>  {

    //interface
    interface CaptianAction {

        void demotePlayerAsCaptain(int position);

        void promotePlayerAsCaptain(int position);

        void kickPlayer(int position);

    }

    //interface  member
    private CaptianAction mAction;
//Data
    private List<Player> mDataset;
    public PlayerAdapter(List<Player> mDataset, CaptianAction mAdminAction){
        this.mDataset = mDataset;
        this.mAction = mAdminAction;

    }

    @NonNull
    @Override
    public PlayerPlaceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = (View) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.player_row_item, viewGroup, false);

        PlayerPlaceHolder vh = new PlayerPlaceHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerPlaceHolder placeHolder, int i) {

        ImageView playerLogo ;
        placeHolder.mPlayerName.setText(mDataset.get(i).getPlayerName());
        placeHolder.mPlayerPriv.setText(mDataset.get(i).getPrivilege());
        playerLogo = placeHolder.mPlayerIcon;

        //Load Image on Bind View
        String url = mDataset.get(i).getPlayerIcon();

        Glide.with(playerLogo.getContext()).load(url).apply(new RequestOptions().placeholder(R.mipmap.ic_player_defualt).error(R.mipmap.ic_player_error_loading)).into(playerLogo);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    //PLayer PlaceHolder
    class PlayerPlaceHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        ImageView mPlayerIcon;
        ImageView mMoreAction;
        TextView mPlayerName ;
        TextView mPlayerPriv;

        public PlayerPlaceHolder(@NonNull View itemView) {
            super(itemView);
            mPlayerIcon =(ImageView) itemView.findViewById(R.id.img_player_icon);
            mPlayerName =(TextView) itemView.findViewById(R.id.txt_player_name);
            mPlayerPriv =(TextView) itemView.findViewById(R.id.txt_player_priv);
            mMoreAction = (ImageView) itemView.findViewById(R.id.admin_action_on_player);
            mMoreAction.setOnClickListener(this);


        }

        //Laod players Icons
        void loadThumbnail(){

            String imageUrl ;
            imageUrl = mDataset.get(getAdapterPosition()).getPlayerIcon();
            Glide.with(mPlayerIcon.getContext()).load(imageUrl).into(mPlayerIcon);
        }



        @Override
        public void onClick(View v) {

          //  int pos = getAdapterPosition();

           PopupMenu menu = new PopupMenu(mMoreAction.getContext(),itemView);
           menu.inflate(R.menu.admin_menu);

           menu.show();
           menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
               @Override
               public boolean onMenuItemClick(MenuItem menuItem) {

                   switch (menuItem.getItemId()){

                       case R.id.promote_promote_as_captain:

                           mAction.promotePlayerAsCaptain(getAdapterPosition());
                           break;
                       case R.id.action_demote_as_player:
                           mAction.demotePlayerAsCaptain(getAdapterPosition());
                           break;
                       case R.id.action_kick__player:
                           mAction.kickPlayer(getAdapterPosition());
                           break;
                       default:
                           return true;
                   }
                   return false;
               }
           });


        }





    }
}
