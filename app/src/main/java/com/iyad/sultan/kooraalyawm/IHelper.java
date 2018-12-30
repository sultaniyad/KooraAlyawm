package com.iyad.sultan.kooraalyawm;

import java.util.Date;

public class IHelper {



    //Only Captain has priv to start a game
    public boolean isPlayerCaptainInThisTeam(String playerKey, String groupKey){


        return true;
    }


    //No new Game running (Game Status ;))
    public boolean noGameRunning(){

        return  false;
    }

    //Verify game date less then a month 30 days
    public boolean isGameDateVeiled( ){

        return false;
    }

    //



}
