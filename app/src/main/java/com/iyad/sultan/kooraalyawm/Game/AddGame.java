package com.iyad.sultan.kooraalyawm.Game;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iyad.sultan.kooraalyawm.IHelper;
import com.iyad.sultan.kooraalyawm.Model.Game;
import com.iyad.sultan.kooraalyawm.Model.GameAttendees;
import com.iyad.sultan.kooraalyawm.R;
import com.iyad.sultan.kooraalyawm.Utilities.UiValidator;

import java.security.Key;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.iyad.sultan.kooraalyawm.Utilities.Constants.GAME_DETAILS_PATH;
import static com.iyad.sultan.kooraalyawm.Utilities.Constants.GAME_PATH;
import static com.iyad.sultan.kooraalyawm.Utilities.Constants.GROUP_PATH;
import static com.iyad.sultan.kooraalyawm.Utilities.Constants.PLAYER_PATH;

public class AddGame extends AppCompatActivity {

    private static final String GROUP_ID = "GROUP_ID_UNIQUE";
    private static final String GROUP_NAME = "GROUP_NAME";
    private static final String PLAYER_ICON_FOR_NEW_GAME = "PLAYER_ICON_FOR_NEW_GAME";
    private static final String TAG ="AddGameActivity" ;
    private String groupId;
    private String groupName;
    private String icon;

    //UI elements
    private EditText mStadiumName;
    private TextInputLayout layoutStadiumName;
    private EditText mLocation;
    private TextInputLayout layoutLocation;
    private EditText mRequiredNumber;
    private TextInputLayout layoutRequiredNumber;
    private EditText mFees;
    private TextInputLayout layoutFees;
    private EditText mDateUI;
    private TextInputLayout layoutDate;
    private AppCompatButton btnAddNewGame;
    private ScrollView rootLayout;

    //Listener
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    //Data

    //Time
    private Date mDate;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;

    //Location
    private LatLng mLng;

    //Game Obj
    private Game newGme;

    //Map
    private Place place;
    int PLACE_PICKER_REQUEST;


    //FireBase
    private DatabaseReference mRootRef;
    private DatabaseReference mGameRef;
    private DatabaseReference mGameDeatilsRef;
    private DatabaseReference mGroupRef;
    private DatabaseReference mPlayerRef;
    private DatabaseReference mGroupMembersRef;
    private FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_game);

        //get Group detail to start this game and user has priv to do this action
        groupId = getIntent().getStringExtra(GROUP_ID);
        groupName = getIntent().getStringExtra(GROUP_NAME);
        icon = getIntent().getStringExtra(PLAYER_ICON_FOR_NEW_GAME);
        try {
            if (groupId != null) {
                IHelper helper = new IHelper();
                if (helper.noGameRunning() && helper.isGameDateVeiled()) {
                    //Get Data


                    //Add to FireBase DB
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //Inti
        user = FirebaseAuth.getInstance().getCurrentUser();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mGameRef = mRootRef.child(GAME_PATH);
        mGameDeatilsRef = mRootRef.child(GAME_DETAILS_PATH);
        mGroupRef = mRootRef.child(GROUP_PATH).child(groupId).child("games");
        mGroupMembersRef = mRootRef.child(GROUP_PATH).child(groupId).child("members");
        //   mPlayerRef = mRootRef.child(PLAYER_PATH).child(user.getUid()).child("games");
        mPlayerRef = mRootRef.child(PLAYER_PATH);

        drawUI();
    }

    private void drawUI() {

        rootLayout = findViewById(R.id.rootAddGameLayout);
        //Layout ref
        layoutStadiumName = findViewById(R.id.layout_stadium_name);
        layoutLocation = findViewById(R.id.layout_location);
        layoutRequiredNumber = findViewById(R.id.layout_required_number);
        layoutDate = findViewById(R.id.layout_date);
        layoutFees = findViewById(R.id.layout_fees);

        //UI control
        btnAddNewGame = (AppCompatButton) findViewById(R.id.AddGame_btn_CreateGame);
        mStadiumName = (EditText) findViewById(R.id.AddGame_StadiumName);
        mLocation = (EditText) findViewById(R.id.AddGame_et_stadium_location);
        mLocation.setHint(getResources().getString(R.string.stadium_location));
        mRequiredNumber = (EditText) findViewById(R.id.AddGame_et_required_number);
        mFees = (EditText) findViewById(R.id.AddGame_et_Fees);
        mDateUI = (EditText) findViewById(R.id.AddGame_et_date);
        mDateUI.setHint(getResources().getString(R.string.game_date));

        //set Date Lister
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                //set Selected date (year, month and day))
                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;

                //open TimePicker
                Calendar cal = Calendar.getInstance();
                mHour = cal.get(Calendar.HOUR_OF_DAY);
                mMinute = cal.get(Calendar.MINUTE);

                //Time Picker
                TimePickerDialog mTimeDialog = new TimePickerDialog(AddGame.this, mTimeSetListener, mHour, mMinute, true);
                mTimeDialog.show();
            }
        };


        //set Hours Listener
        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //set selected Date (Hour & minute)
                mHour = hourOfDay;
                mMinute = minute;
//set Date info
                mDateUI.setText(mDay + "-" + (mMonth + 1) + "-" + mYear +
                        " " + mHour + ":" + mMinute);

            }
        };


        //Date Picker
        mDateUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mPickerDialog = new DatePickerDialog(AddGame.this, mDateSetListener, year, month, day);
                mPickerDialog.show();
            }
        });

        //set Location Picker Listener
        mLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placePicker();

            }
        });

        //set Add Game Listener
        btnAddNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitForm();
            }
        });

    }


    //Map methods
    private void placePicker() {


        PLACE_PICKER_REQUEST = 1;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

    private boolean isUserSelectedLocation() {
        return place != null;
    }

    //Place picker status
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace(this, data);
                // String toastMsg = String.format("Place: %s", place.getName());
                // Toast.makeText(this, place.getAddress(), Toast.LENGTH_LONG).show();

                if (isUserSelectedLocation()) {

                    //  mLocation.setText(place.getLatLng().toString());


                    String lol = place.getLatLng().toString();

                    String coordinate = "";

                    coordinate += lol.replace("lat/lng: (", "");
                    coordinate = coordinate.replace(")", "");

                    mLocation.setText(coordinate);

                }


            }
        }
    }


    //ON User submit
    private void submitForm() {

        UiValidator uiValidator = new UiValidator();

        if (!validStadiumName(uiValidator))
            return;
        if (!validLocation(uiValidator))
            return;
        if (!validRequiredNumber(uiValidator))
            return;
        if (!validFees(uiValidator))
            return;
        if (!validDate(uiValidator))
            return;
        if(Integer.parseInt(mRequiredNumber.getText().toString()) < 3)
        {
            showNoenoughPlayers();
            return;
        }
        if (isNotValidGameDate(mDateUI.getText().toString())) {
            showDatePassed();
            return;
        }




//else add game all valid input

        //    Toast.makeText(this, "Thanks you", Toast.LENGTH_SHORT).show();

        String key = mGameRef.push().getKey();
        Game game = new Game();
        game.setId(key);
        game.setGroupid(groupId);
        game.setGroupname(groupName);
        game.setStadium(mStadiumName.getText().toString());
        game.setLocation(mLocation.getText().toString());
        game.setRequirednumber(mRequiredNumber.getText().toString());
        game.setCanceled("0");
        game.setFees(mFees.getText().toString());
        game.setRequirednumber(mRequiredNumber.getText().toString());
        String strDate = mDay + "-" + (mMonth + 1) + "-" + mYear + " " + mHour + ":" + mMinute;
        game.setDate(strDate);
        Map<String, Object> rooling = new HashMap<String, Object>();
        rooling.put(user.getUid(), true);
        game.setRolling(rooling);
        game.setValid(true);

        addGameToHero(game, key);
    }

    private void showNoenoughPlayers() {
        Snackbar snackbar = Snackbar.make(rootLayout, R.string.no_player_required, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void addGameToHero(Game game, String key) {

        String uid = user.getUid();
        //Add Game
        mGameRef.child(key).setValue(game).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful())
                    showResult("Added Successfully");
                else showResult("Try again later");

            }
        });

//Add Game Details

        //get time now


        GameAttendees attendees = new GameAttendees();
        attendees.setPlayerid(uid);
        attendees.setAttend(true);
        attendees.setJoindate(getDateNow());
        attendees.setPlayername(user.getDisplayName());
        attendees.setPlayericon(icon);

        Map<String, Object> gamedetails = new HashMap<String, Object>();
        gamedetails.put(uid, attendees);

        mGameDeatilsRef.child(key).updateChildren(gamedetails).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful())
                    Toast.makeText(AddGame.this, "mmm something went wrong :(", Toast.LENGTH_SHORT).show();

            }
        });
//Add Game to Group Node
        final Map<String, Object> games = new HashMap<String, Object>();
        games.put(key, true);
        mGroupRef.updateChildren(games).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful())
                    Toast.makeText(AddGame.this, "mmm something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        //Add Game to All Player Node
        mGroupMembersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
//snap.getKey is player key of this group
                    if (snap.exists())
                        mPlayerRef.child(snap.getKey()).child("games").updateChildren(games).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful())
                                    Toast.makeText(AddGame.this, "mmm something went wrong D:", Toast.LENGTH_SHORT).show();
                            }
                        });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


    }

    private void showDatePassed(){
        Snackbar snackbar = Snackbar.make(rootLayout, R.string.date_passed, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
    private void showResult(String meg) {

        Snackbar snackbar = Snackbar.make(rootLayout, meg, Snackbar.LENGTH_LONG);
        snackbar.show();
        restValue();

    }

    private void restValue() {

        mStadiumName.setText("");
        mLocation.setText("");
        mLocation.setText("");
        mRequiredNumber.setText("");
        mFees.setText("");
        mDateUI.setText("");
    }

    //validation methods

    private boolean validStadiumName(UiValidator validator) {

        if (validator.isEditTextEmpty(mStadiumName)) {
            layoutStadiumName.setError(getResources().getString(R.string.required_filed));
            return false;
        } else
            layoutStadiumName.setErrorEnabled(false);
        return true;
    }

    private boolean validLocation(UiValidator validator) {

        if (validator.isEditTextEmpty(mLocation)) {
            layoutLocation.setError(getResources().getString(R.string.required_filed));
            return false;
        } else
            layoutLocation.setErrorEnabled(false);
        return true;
    }

    private boolean validRequiredNumber(UiValidator validator) {

        if (validator.isEditTextEmpty(mRequiredNumber)) {
            layoutRequiredNumber.setError(getResources().getString(R.string.required_filed));
            return false;
        } else
            layoutRequiredNumber.setErrorEnabled(false);
        return true;
    }

    private boolean validDate(UiValidator validator) {

        if (validator.isEditTextEmpty(mDateUI)) {
            layoutDate.setError(getResources().getString(R.string.required_filed));
            return false;
        } else
            layoutDate.setErrorEnabled(false);
        return true;
    }

    private boolean validFees(UiValidator validator) {

        if (validator.isEditTextEmpty(mFees)) {
            layoutFees.setError(getResources().getString(R.string.required_filed));
            return false;
        } else
            layoutFees.setErrorEnabled(false);
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //dd-MM-yyyy HH:mm
    private String getDateNow() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);

        return day + "-" + (month + 1) + "-" + year + " " + hour + ":" + min;
    }


    //Date can not be more then month and not pass
    boolean isNotValidGameDate(String date) {

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        try {

            Date mGameDate = format.parse(date);
            Calendar cal = Calendar.getInstance();
            Date currentDate = format.parse(cal.get(Calendar.DAY_OF_MONTH) + "-" + (cal.get(Calendar.MONTH) + 1) +
                    "-" + cal.get(Calendar.YEAR) + " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));

            long timeDiff = mGameDate.getTime() - currentDate.getTime();
            long days =  TimeUnit.DAYS.convert(timeDiff,TimeUnit.MILLISECONDS);

            Log.d(TAG,"Days is " + days);

            if(days >= 1 && days <= 30) {
                return false;

            }

            else
            {
                showDatePassed();
                return true;
            }





// "31-12-2018 11:30"

        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }


    }
}
