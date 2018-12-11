package com.iyad.sultan.kooraalyawm.Game;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.iyad.sultan.kooraalyawm.IHelper;
import com.iyad.sultan.kooraalyawm.Model.Game;
import com.iyad.sultan.kooraalyawm.R;
import com.iyad.sultan.kooraalyawm.Utilities.UiValidator;

import java.util.Calendar;
import java.util.Date;

public class AddGame extends AppCompatActivity {

    private static final String GROUP_ID = "GROUP_ID_UNIQUE";
    private String groupId;

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

    //Listener
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener  mTimeSetListener;

    //Data

        //Time
    private Date mDate ;
    private  int mYear;
    private  int mMonth;
    private  int mDay;
    private  int mHour;
    private  int mMinute;

        //Location
    private LatLng mLng;

        //Game Obj
    private  Game newGme;

    //Map
    private Place place;
    int PLACE_PICKER_REQUEST;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_game);

        //get Group detail to start this game and user has priv to do this action
        groupId =  getIntent().getStringExtra(GROUP_ID);
        try {
            if(groupId != null){
                IHelper helper = new IHelper();
                if(helper.noGameRunning() && helper.isGameDateVeiled()){
                    //Get Data


                    //Add to FireBase DB
                }
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        drawUI();
    }

    private void drawUI() {

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
                TimePickerDialog mTimeDialog = new TimePickerDialog(AddGame.this,mTimeSetListener,mHour,mMinute,true);
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
               mDateUI.setText(mYear +"/"+ mMonth + "/"+ mDay +
               " " + mHour + ":" + mMinute);

            }
        };


        //Date Picker
        mDateUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
               int  year = cal.get(Calendar.YEAR);
               int  month = cal.get(Calendar.MONTH);
               int  day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mPickerDialog = new DatePickerDialog(AddGame.this,mDateSetListener,year,month,day);
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
    private void placePicker()  {


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
    private boolean isUserSelectedLocation(){
        return place != null;
    }
    //Place picker status
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace( this, data);
               // String toastMsg = String.format("Place: %s", place.getName());
               // Toast.makeText(this, place.getAddress(), Toast.LENGTH_LONG).show();

                if(isUserSelectedLocation()){
                    mLocation.setText(place.getName());
                   LatLng lng = place.getLatLng();

                }
            }
        }
    }


    //ON User submit
    private void submitForm(){

        UiValidator uiValidator = new UiValidator();

        if(!validStadiumName(uiValidator))
            return;
        if(!validLocation(uiValidator))
            return;
        if(!validRequiredNumber(uiValidator))
            return;
        if(!validFees(uiValidator))
            return;
        if(!validDate(uiValidator))
            return;

//else add game all valid input
        Toast.makeText(this, "Thanks you", Toast.LENGTH_SHORT).show();
    }


    //validation methods

    private boolean  validStadiumName(UiValidator validator){

        if( validator.isEditTextEmpty(mStadiumName)){
            layoutStadiumName.setError(getResources().getString(R.string.required_filed));
            return false;
        }
        else
            layoutStadiumName.setErrorEnabled(false);
        return true;
    }
    private boolean  validLocation(UiValidator validator){

        if( validator.isEditTextEmpty(mLocation)){
            layoutLocation.setError(getResources().getString(R.string.required_filed));
            return false;
        }
        else
            layoutLocation.setErrorEnabled(false);
        return true;
    }
    private boolean  validRequiredNumber(UiValidator validator){

        if( validator.isEditTextEmpty(mRequiredNumber)){
            layoutRequiredNumber.setError(getResources().getString(R.string.required_filed));
            return false;
        }
        else
            layoutRequiredNumber.setErrorEnabled(false);
        return true;
    }
    private boolean  validDate(UiValidator validator){

        if( validator.isEditTextEmpty(mDateUI)){
            layoutDate.setError(getResources().getString(R.string.required_filed));
            return false;
        }
        else
            layoutDate.setErrorEnabled(false);
        return true;
    }
    private boolean  validFees(UiValidator validator){

        if( validator.isEditTextEmpty(mFees)){
            layoutFees.setError(getResources().getString(R.string.required_filed));
            return false;
        }
        else
            layoutFees.setErrorEnabled(false);
        return true;
    }


}
