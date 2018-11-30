package com.iyad.sultan.kooraalyawm.Groups;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iyad.sultan.kooraalyawm.Model.Group;
import com.iyad.sultan.kooraalyawm.R;

import java.util.List;

public class UserGroupAdapter extends RecyclerView.Adapter<UserGroupAdapter.GroupPlaceHolder>  {


    UserGroupCallBack mCallBack;
    private List<Group> mUserGroups;


    // Provide a suitable constructor (depends on the kind of dataset)
    public UserGroupAdapter(List<Group> myDataset, UserGroupCallBack mCallBack) {
        this.mUserGroups = myDataset;
        this.mCallBack = mCallBack;


    }

    @NonNull
    @Override
    public GroupPlaceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = (View) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.group_row_item, viewGroup, false);

        GroupPlaceHolder vh = new GroupPlaceHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupPlaceHolder groupPlaceHolder, int i) {

        Group group = mUserGroups.get(i);
        groupPlaceHolder.mGroupName.setText(group.getGroupName());

    }

    @Override
    public int getItemCount() {
        return mUserGroups.size();
    }


    //Place holder
    public  class GroupPlaceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CardView mCardView;
        public TextView mGroupName;
        public GroupPlaceHolder(@NonNull View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.group_cardview);
            mCardView.setOnClickListener(this);
            mGroupName = (TextView) itemView.findViewById(R.id.txt_group_name);
        }

        @Override
        public void onClick(View v) {

            mCallBack.selectedGroup(getAdapterPosition());

        }
    }




    //callBack
    public interface UserGroupCallBack{
        void selectedGroup(int position);
    }


}