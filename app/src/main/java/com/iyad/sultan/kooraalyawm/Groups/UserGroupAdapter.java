package com.iyad.sultan.kooraalyawm.Groups;

import android.net.Uri;
import android.support.annotation.NonNull;
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
import com.iyad.sultan.kooraalyawm.Model.Group3;
import com.iyad.sultan.kooraalyawm.R;

import java.util.List;

public class UserGroupAdapter extends RecyclerView.Adapter<UserGroupAdapter.GroupPlaceHolder> {


    UserGroupCallBack mCallBack;
    private List<Group> mUserGroups;


    // Provide a suitable constructor (depends on the kind of dataset)
    public UserGroupAdapter(List<Group> myDataset, UserGroupCallBack mCallBack) {
        this.mUserGroups = myDataset;
        this.mCallBack = mCallBack;

    }

    public void updateAdapterSet(List<Group> set) {
        this.mUserGroups = set;
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

        if (group != null) {
            groupPlaceHolder.mGroupName.setText(group.getName());
            groupPlaceHolder.mCurrenPlayer.setText(mUserGroups.get(i).getMembers().size() + "");
            ImageView garoupIcon = groupPlaceHolder.mIcon;
            Uri uri = Uri.parse(group.getLogo());
            Glide.with(garoupIcon.getContext()).load(uri.toString()).
                    apply(new RequestOptions().circleCrop().placeholder(R.drawable.ic_group).error(R.mipmap.ic_player_error_loading)).into(garoupIcon);

        }

    }

    @Override
    public int getItemCount() {
        return mUserGroups.size();
    }


    //Place holder
    public class GroupPlaceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CardView mCardView;
        private TextView mGroupName;
        private TextView mCurrenPlayer;
        private ImageView mIcon;

        public GroupPlaceHolder(@NonNull View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.group_cardview);
            mCardView.setOnClickListener(this);
            mGroupName = (TextView) itemView.findViewById(R.id.txt_group_name);
            mCurrenPlayer = itemView.findViewById(R.id.txt_group_current_number);
            mIcon = itemView.findViewById(R.id.img_group_thumbnail);
        }

        @Override
        public void onClick(View v) {

            mCallBack.selectedGroup(getAdapterPosition());

        }
    }


    //callBack
    public interface UserGroupCallBack {
        void selectedGroup(int position);
    }


}