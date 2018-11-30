package com.iyad.sultan.kooraalyawm.Game;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.iyad.sultan.kooraalyawm.IHelper;
import com.iyad.sultan.kooraalyawm.Model.Game;
import com.iyad.sultan.kooraalyawm.R;

import java.util.Calendar;
import java.util.Date;

public class AddGame extends AppCompatActivity {

    private static final String GROUP_ID = "GROUP_ID_UNIQUE";
    private String groupId;

    //UI elements
    private EditText mStadiumName;
    private EditText mLocation;
    private EditText mRequiredNumber;
    private EditText mFees;
    private EditText mDateUI;

    //Listener
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener  mTimeSetListener;

    //Data
    private Date mDate ;
    private  int mYear;
    private  int mMonth;
    private  int mDay;
    private  int mHour;
    private  int mMinute;

    private  Game newGme;
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
                    Game game =   getGameDetail();

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

        mStadiumName = (EditText) findViewById(R.id.AddGame_StadiumName);
        mLocation = (EditText) findViewById(R.id.AddGame_et_stadium_location);
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

               mDateUI.setHint(mYear +"/"+ mMonth + "/"+ mDay +
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


    }


    private void placePicker(){
        /*
        int PLACE_PICKER_REQUEST = 1;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        */
    }

    //Load get Data from UI
    private Game getGameDetail(){

        Game mGame = new Game();

        return mGame;



    }
}
