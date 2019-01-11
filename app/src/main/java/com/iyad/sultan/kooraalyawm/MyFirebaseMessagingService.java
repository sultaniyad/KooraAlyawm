package com.iyad.sultan.kooraalyawm;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.signature.ObjectKey;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";


    @Override
    public void onNewToken(String token) {

        Log.d(TAG, "Refreshed token: " + token);
        //endRegistrationToServer(token);
    }


    private void sendRegistrationToServer( String token) {
        // TODO: Implement this method to send token to your app server.

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null)
            return;

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("players").child(user.getUid()).child("pushToken");

        Map<String,Object> pushToken = new HashMap<String, Object>();
        pushToken.put(token,true);

        mRef.updateChildren(pushToken).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(!task.isSuccessful()){
                Log.w(TAG,"Store Token failed" +task.getException());
                return;
                }
                Log.d(TAG,"Store Token successfully to User Info");
            }
        });
  }

    private void getToaken(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {

                if (!task.isSuccessful()) {
                    Log.w(TAG, "getInstanceId failed", task.getException());
                    return;
                }

                //Get a Token
                String token = task.getResult().getToken();

                String meg = "Token ID: " + token;
                Log.d(TAG,meg);



            }
        });
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
    }



}
