package com.iyad.sultan.kooraalyawm.Groups;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.iyad.sultan.kooraalyawm.Model.Group;
import com.iyad.sultan.kooraalyawm.R;

import java.util.List;

public class SearchGroupAdapter extends RecyclerView.Adapter<SearchGroupAdapter.GroupPlaceHolder>  {


    OnSelectedGroup mCallBack;
    private List<Group> mSerachGroups;


    // Provide a suitable constructor (depends on the kind of dataset)
    public SearchGroupAdapter(List<Group> myDataset, OnSelectedGroup mCallBack) {
        this.mSerachGroups = myDataset;
        this.mCallBack = mCallBack;


    }

    @NonNull
    @Override
    public GroupPlaceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = (View) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.group_search_row_item, viewGroup, false);

        GroupPlaceHolder vh = new GroupPlaceHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupPlaceHolder groupPlaceHolder, int i) {

        Group group = mSerachGroups.get(i);

       ImageView mGroupLogo = groupPlaceHolder.mGroupIcon;

        //Load Image on Bind View
        String url = group.getGroupLogo();
        Glide.with(mGroupLogo.getContext()).load(url).apply(new RequestOptions().circleCrop().placeholder(R.drawable.ic_player_defualt).error(R.drawable.ic_player_error_loading)).into(mGroupLogo);

        groupPlaceHolder.mGroupName.setText(group.getGroupName());
        groupPlaceHolder.mGroupCurrentNumber.setText(group.getCurrentPlayers()+"");



    }

    @Override
    public int getItemCount() {
        return mSerachGroups.size();
    }


    //Place holder
    public  class GroupPlaceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private ImageView mGroupIcon;
        private TextView mGroupName;
        private  TextView mGroupCurrentNumber;
        private AppCompatButton mJoinGroupBtn;
        public GroupPlaceHolder(@NonNull View itemView) {
            super(itemView);
            mGroupIcon = itemView.findViewById(R.id.img_search_group_icon);
            mGroupName = itemView.findViewById(R.id.txt_search_group_name);
            mGroupCurrentNumber = itemView.findViewById(R.id.txt_search_group_current_number);
            mJoinGroupBtn = itemView.findViewById(R.id.btn_join_group);
            mJoinGroupBtn.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            mCallBack.SelectedGroup(getAdapterPosition());

        }
    }

    //callBack
    public interface OnSelectedGroup{
        void SelectedGroup(int position);
    }


}