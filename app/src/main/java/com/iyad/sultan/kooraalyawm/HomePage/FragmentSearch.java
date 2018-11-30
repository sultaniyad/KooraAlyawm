package com.iyad.sultan.kooraalyawm.HomePage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iyad.sultan.kooraalyawm.Dumb.DumbGen;
import com.iyad.sultan.kooraalyawm.Groups.JoinGroupActivity;
import com.iyad.sultan.kooraalyawm.Groups.SearchGroupAdapter;
import com.iyad.sultan.kooraalyawm.Model.Group;
import com.iyad.sultan.kooraalyawm.R;

import java.util.List;

public class FragmentSearch extends Fragment implements SearchGroupAdapter.OnSelectedGroup {

    //Constants
         //Bundle
    private static final  String BUNDLE_JOIN = "BUNDLE_JOIN_GROUP";

        //Group
    private static final  String GROUP_ID = "GROUP_ID_JOIN";
    private static final  String GROUP_NAME = "GROUP_NAME_JOIN";
    private static final  String GROUP_PASSWORD = "GROUP_JOINT_PASSWORD";
    private static final  String GROUP_ICON = "GROUP_ICON_JOIN";
    private static final  String IS_GROUP_PROTECTED = "IS_GROUP_PROTECTED";
        //User
        private static final  String PLAYER_ID = "PLAYER_ID_JOIN";

    //Data
    private List<Group> mDataset;
    private SearchGroupAdapter mAdapter;

    //UI
    private RecyclerView mRecyclerView ;
    private RecyclerView.LayoutManager mLayoutManager;


    public FragmentSearch() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {




           View v = inflater.inflate(R.layout.serach_fragment,container,false);

        //DrawUI
        mRecyclerView =v.findViewById(R.id.search_rec_short);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mDataset = getSearchGroup("key");
        mAdapter = new SearchGroupAdapter(mDataset,this);
        mRecyclerView.setAdapter(mAdapter);

        //set Toolbar
        setHasOptionsMenu(true);

        //
        return v;
    }

    private void drawUI(){


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {



        try {
            MenuItem mSearch = menu.add("Search");
            mSearch.setIcon(R.drawable.ic_search_white_24dp);

            SearchView sv = new SearchView(getActivity());
            mSearch.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
                    | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
            mSearch.setActionView(sv);

            sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {



                    updateUI(s);


                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {

                    return false;
                }
            });
        }

        catch (Exception r){
            Toast.makeText(getActivity(), "Unexpected error ", Toast.LENGTH_SHORT).show();
        }




    }

    private void updateUI(String key)
    {
        //Key used to get group id, or group name
        Toast.makeText(getContext(), "" + key, Toast.LENGTH_SHORT).show();
    }


    //Load Data from FireBase
    private List<Group> getSearchGroup(String key){

        return new DumbGen().findSearchGroups(""+ key);
    }

    //On Group selected
    @Override
    public void SelectedGroup(int position) {

        Toast.makeText(getContext(), "Portion group in" + position, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getContext(),JoinGroupActivity.class);

        Group group = mDataset.get(position);

        Bundle bundle = new Bundle();
        bundle.putString(GROUP_ID,group.getGroupId());
        bundle.putString(GROUP_NAME,group.getGroupName());
        bundle.putString(GROUP_ICON,group.getGroupLogo());
        bundle.putBoolean(IS_GROUP_PROTECTED,group.isProtected());
        bundle.putString(GROUP_PASSWORD,group.getGroupPassword());
        i.putExtra(BUNDLE_JOIN,bundle);

        startActivity(i);

    }



}
