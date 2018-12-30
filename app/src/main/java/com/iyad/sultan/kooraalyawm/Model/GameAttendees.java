package com.iyad.sultan.kooraalyawm.Model;

public class GameAttendees {

    private String playerid;
    private String playername;
    private String playericon;
    private boolean attend;
    private String joindate;

    public GameAttendees(){}

    public GameAttendees(String playerid,boolean attend){

        this.playerid = playerid;
        this.attend = attend;
    }

    public String getPlayerid() {
        return playerid;
    }

    public void setPlayerid(String playerid) {
        this.playerid = playerid;
    }


    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }

    public String getPlayericon() {
        return playericon;
    }

    public void setPlayericon(String playericon) {
        this.playericon = playericon;
    }

    public boolean isAttend() {
        return attend;
    }

    public void setAttend(boolean attend) {
        this.attend = attend;
    }

    public String getJoindate() {
        return joindate;
    }

    public void setJoindate(String joindate) {
        this.joindate = joindate;
    }
}
