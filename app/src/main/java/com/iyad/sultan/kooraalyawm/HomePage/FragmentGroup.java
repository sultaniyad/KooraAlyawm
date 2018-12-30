package com.iyad.sultan.kooraalyawm.HomePage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iyad.sultan.kooraalyawm.Groups.AddGroupActivity;
import com.iyad.sultan.kooraalyawm.Groups.GroupDetailsActivity;
import com.iyad.sultan.kooraalyawm.Groups.UserGroupAdapter;
import com.iyad.sultan.kooraalyawm.Model.Group;
import com.iyad.sultan.kooraalyawm.Model.Group3;
import com.iyad.sultan.kooraalyawm.R;
import com.iyad.sultan.kooraalyawm.Sign.SignIn;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.iyad.sultan.kooraalyawm.Utilities.Constants.GROUP_PATH;
import static com.iyad.sultan.kooraalyawm.Utilities.Constants.PLAYER_PATH;


public class FragmentGroup extends Fragment implements UserGroupAdapter.UserGroupCallBack {

    private static final String GROUP_ID = "GROUP_ID_UNIQUE";
    private static final String GROUP_NAME = "GROUP_NAME";
    private static final String GROUP_Current_PLAYER = "GROUP_Current_PLAYER";
    private static final String GROUP_Current_Captain = "GROUP_Current_Captain";
    private static final String GROUP_Current_Captain_MAP = "GROUP_Current_Captain_MAP";
    private static final String GROUP_LOGO = "GROUP_LOGO";
  //  private static final String GROUP_NAME = "GROUP_NAME";

    private Boolean isCaptain;
    private int CurrentCaptains;

    //Con
    private static final String IS_PLAYER_CAPTAIN = "IS_PLAYER_CAPTAIN";
    private static final String CURRENT_CAPTAIN = "CURRENT_CAPTAIN";
    private FirebaseAuth mAuth;

    //Views
    private  View v;
    private  AppCompatButton btnLogin;


    List<Group> userGroups;

    //FireBase

    private FirebaseUser user;
    public FragmentGroup() {
    }
    DatabaseReference  mRoot;
    private String[] mDataset;
    private RecyclerView mRecyclerView ;
    private RecyclerView.LayoutManager mLayoutManager;
    private UserGroupAdapter mAdapter;
    private FloatingActionButton mAddGroup;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


            mAuth = FirebaseAuth.getInstance();
            user =  mAuth.getCurrentUser();

          mRoot= FirebaseDatabase.getInstance().getReference();
           v = inflater.inflate(R.layout.group_fragment,container,false);

          drawUI();




        //show login Page if
        if(user == null)
            goToLogin();



        return v;
    }

    //When User not signIn and Skip it phase
    private void goToLogin(){
        btnLogin.setVisibility(View.VISIBLE);
        mAddGroup.hide();
        mRecyclerView.setVisibility(View.INVISIBLE);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(v.getContext(), SignIn.class);
                startActivity(i);
            }
        });

    }


   private void drawUI(){

        mRecyclerView = v.findViewById(R.id.group_rec_short);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAddGroup =  v.findViewById(R.id.btn_add_new_group);
        btnLogin = v.findViewById(R.id.btn_go_to_login);

    }

    //To add new Group
    private void createNewTeam() {

        mAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get user key (keep tracking user id)
                Intent i = new Intent(v.getContext(),AddGroupActivity.class);
                startActivity(i);
            }
        });
    }

    private void updateUI(FirebaseUser user ) {


        if(user != null) {
            //show Rec and Action button
            mRecyclerView.setVisibility(View.VISIBLE);
            mAddGroup.show();
            createNewTeam();
            btnLogin.setVisibility(View.GONE);


            //Load Data from FireBase



        }

    }


    private void loadData(){

        //get data only for this user !!!!!!!!
    final List<Group> list = new ArrayList<>();

    //User group keys

    DatabaseReference  mRef = mRoot.child(GROUP_PATH);


        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            for(DataSnapshot childern : dataSnapshot.getChildren()){


                Group mGroup = childern.getValue(Group.class);
                list.add(mGroup);
            }

            //pass data to adapter
            updateAdapter(user,list);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });




}
    private void loadData2(){

        //get data only for this user !!!!!!!!
        final List<Group> list = new ArrayList<>();

        //User group keys

        final DatabaseReference  mRef = mRoot.child(PLAYER_PATH).child(user.getUid()).child(GROUP_PATH);
        final  DatabaseReference mGropuRef = mRoot.child(GROUP_PATH);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snap : dataSnapshot.getChildren()){
                    final String userGroupKey = snap.getKey();

                    mGropuRef.child(userGroupKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){



                           //    String name = (snapshot.child("name").getValue());

                             //   list.add(mGroup);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    updateAdapter(user,list);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    //
    }

    //Load data when it completed from Firebase
    private void updateAdapter(FirebaseUser user,List<Group> list){

        if(user == null)
            return;
        //important
        userGroups = list;
        mAdapter = new UserGroupAdapter(userGroups, this);
        mRecyclerView.setAdapter(mAdapter);


    }



    private void showGroupDetails(int posistion){

        Bundle bundle = new Bundle();
        Group group = userGroups.get(posistion);
        Intent i = new Intent(getContext(),GroupDetailsActivity.class);
        i.putExtra(GROUP_ID,group.getId());
        i.putExtra(GROUP_NAME,group.getName());
        i.putExtra(GROUP_Current_PLAYER,group.getMembers().size());
        i.putExtra(GROUP_LOGO,group.getLogo());

        //this ca not happened cause no will start a game again
        Map<String,Object> currentcaptain =   group.getPrivilege();
        if(currentcaptain != null)

        i.putExtra(GROUP_Current_Captain,group.getPrivilege().size());
        i.putExtra(GROUP_Current_Captain_MAP, (Serializable) group.getPrivilege());

        startActivity(i);
    }


    //Calls back
    @Override
    public void selectedGroup(int position) {

      //  Toast.makeText(getContext(), "You Clicked group with name " + userGroups.get(position).getName() , Toast.LENGTH_SHORT).show();

        //pass Group ID
       showGroupDetails(position);
       ;

    }






    @Override
    public void onStart() {

        super.onStart();
        //Load data after login or update
        user = mAuth.getCurrentUser();
        updateUI(user);
        if(user != null)
            getGroupIDs();

       // loadData();





    }

//Load data from Firebase
    void getGroupIDs(){

        final  DatabaseReference mRef = mRoot.child(PLAYER_PATH).child(user.getUid()).child(GROUP_PATH);

        final List<String> IDs = new ArrayList<>();

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snap : dataSnapshot.getChildren()){

                    String groupId = snap.getKey();
                    IDs.add(groupId);
                }

                loadGroupOfThisUser(IDs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void loadGroupOfThisUser(List<String> groupIDs){
        //get data only for this user !!!!!!!!
        final List<Group> list = new ArrayList<>();
        //User group keys
        DatabaseReference  mRef = mRoot;
//loop anf get user groups
        for(int i=0 ; i < groupIDs.size() ; i++ )
            mRef.child(GROUP_PATH).child(groupIDs.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                          Group mGroup = dataSnapshot.getValue(Group.class);
                          list.add(mGroup);

                    //pass data to adapter  (check later tp put method out side the loop)
                    updateAdapter(user,list);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });//end loop






    }


}




