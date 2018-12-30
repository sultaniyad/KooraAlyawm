package com.iyad.sultan.kooraalyawm.HomePage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.iyad.sultan.kooraalyawm.Dumb.DumbGen;
import com.iyad.sultan.kooraalyawm.Groups.JoinGroupActivity;
import com.iyad.sultan.kooraalyawm.Groups.SearchGroupAdapter;
import com.iyad.sultan.kooraalyawm.Model.Group;
import com.iyad.sultan.kooraalyawm.Model.Group3;
import com.iyad.sultan.kooraalyawm.R;

import java.nio.file.FileVisitResult;
import java.util.ArrayList;
import java.util.List;

import static com.iyad.sultan.kooraalyawm.Utilities.Constants.GROUP_PATH;

public class FragmentSearch extends Fragment implements SearchGroupAdapter.OnSelectedGroup {

    //Constants
    //Bundle
    private static final String BUNDLE_JOIN = "BUNDLE_JOIN_GROUP";

    //Group
    private static final String GROUP_ID = "GROUP_ID_JOIN";
    private static final String GROUP_NAME = "GROUP_NAME_JOIN";
    private static final String GROUP_PASSWORD = "GROUP_JOINT_PASSWORD";
    private static final String GROUP_ICON = "GROUP_ICON_JOIN";
    private static final String IS_GROUP_PROTECTED = "IS_GROUP_PROTECTED";
    private static final String GROUP_CURRENT_PLAYER = "GROUP_CURRENT_PLAYER";
    //User
    private static final String PLAYER_ID = "PLAYER_ID_JOIN";

    //Data
    private List<Group> mDataset;
    private SearchGroupAdapter mAdapter;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private DatabaseReference rootRef;
    private DatabaseReference groupRef;

    //UI
    private RelativeLayout rootLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;


    public FragmentSearch() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.serach_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
        groupRef = rootRef.child(GROUP_PATH);
        drawUI(v);

        return v;
    }

    private void drawUI(View v) {

        //DrawUI
        mRecyclerView = v.findViewById(R.id.search_rec_short);
        rootLayout = v.findViewById(R.id.root_layout_fragment);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mDataset = new ArrayList<>(); //getSearchGroup("key");
        mAdapter = new SearchGroupAdapter(mDataset, this);
        mRecyclerView.setAdapter(mAdapter);

        //set Toolbar
        setHasOptionsMenu(true);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        try {
            MenuItem mSearch = menu.add("Search");
            mSearch.setIcon(R.drawable.ic_search_white_24dp);

            SearchView sv = new SearchView(getActivity());
            sv.setIconifiedByDefault(false);
            /*check later*/
            sv.setQuery("testttttttt",true);
            sv.setFocusable(true);
            sv.setIconified(false);
            sv.requestFocusFromTouch();

            mSearch.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
                    | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
           // mSearch.ex();
            mSearch.setActionView(sv);

            sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {

                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null)
                    {

                        getSearchQuery(s);
                    }

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {

                    return false;
                }
            });
        } catch (Exception r) {
            Toast.makeText(getActivity(), "Unexpected error ", Toast.LENGTH_SHORT).show();
        }


    }

    private void updateUI() {


        //mDataset = getSearchGroup(key);
      //  mAdapter.updateData(mDataset);
        //Key used to get group id, or group name
        mAdapter.notifyDataSetChanged();

    }


    //Load Data from FireBase
    private void getSearchQuery(String searchGroup) {

        Query query = groupRef.orderByChild("name").startAt(searchGroup).endAt(searchGroup + "\\uf8ff");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mDataset.clear();

                for(DataSnapshot snap : dataSnapshot.getChildren())
                mDataset.add(snap.getValue(Group.class));

if(mDataset.size() == 0)
    showMessage("No result founded");

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //On Group selected (Bundled)
    @Override
    public void SelectedGroup(int position) {

    //    Toast.makeText(getContext(), "Portion group in" + position, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getContext(), JoinGroupActivity.class);

        Group group = mDataset.get(position);

        Bundle bundle = new Bundle();
        bundle.putString(GROUP_ID, group.getId());
        bundle.putString(GROUP_NAME, group.getName());
        bundle.putString(GROUP_ICON, group.getLogo());
        bundle.putBoolean(IS_GROUP_PROTECTED, group.isSecure());
        bundle.putString(GROUP_PASSWORD, group.getPassword());
        if(group.getMembers() == null)
        bundle.putInt(GROUP_CURRENT_PLAYER,0);
        else
            bundle.putInt(GROUP_CURRENT_PLAYER,group.getMembers().size());
        i.putExtra(BUNDLE_JOIN, bundle);

        startActivity(i);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }

    private void showMessage(String meg) {

        Snackbar snackbar = Snackbar.make(rootLayout, meg, Snackbar.LENGTH_LONG);
        snackbar.show();


    }
}
