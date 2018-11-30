package com.iyad.sultan.kooraalyawm.HomePage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iyad.sultan.kooraalyawm.Dumb.DumbGen;
import com.iyad.sultan.kooraalyawm.Groups.AddGroupActivity;
import com.iyad.sultan.kooraalyawm.Groups.GroupDetailsActivity;
import com.iyad.sultan.kooraalyawm.Groups.UserGroupAdapter;
import com.iyad.sultan.kooraalyawm.Model.Group;
import com.iyad.sultan.kooraalyawm.R;

import java.util.List;

public class FragmentGroup extends Fragment implements UserGroupAdapter.UserGroupCallBack {

    private static final String GROUP_ID = "GROUP_ID_UNIQUE";

    List<Group> userGroups;
    public FragmentGroup() {
    }

    private String[] mDataset;
    private RecyclerView mRecyclerView ;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton mAddGroup;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.group_fragment,container,false);

        //Load Data from FireBase
        userGroups = getUserGroup();
        mRecyclerView = v.findViewById(R.id.group_rec_short);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(new UserGroupAdapter(userGroups,this));

        mAddGroup = (FloatingActionButton) v.findViewById(R.id.btn_add_new_group);
        mAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get user key (keep tracking user id)
                Intent i = new Intent(v.getContext(),AddGroupActivity.class);
                startActivity(i);
            }
        });
        return v;
    }





    private void showGroupDetails(String groupId){

        Intent i = new Intent(getContext(),GroupDetailsActivity.class);
        i.putExtra(GROUP_ID,groupId);
        startActivity(i);
    }


    //Calls back
    @Override
    public void selectedGroup(int position) {

        Toast.makeText(getContext(), "You Clicked group with name " + userGroups.get(position).getGroupId() , Toast.LENGTH_SHORT).show();

        //pass Group ID
        showGroupDetails(userGroups.get(position).getGroupId());

    }

    //Load Data from FireBase
    private List<Group> getUserGroup(){
        //connect to google Firebase

        //Dumb Data
        DumbGen gen = new DumbGen();
        return gen.createGroups50();
    }

}


