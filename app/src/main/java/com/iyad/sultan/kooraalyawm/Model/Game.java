package com.iyad.sultan.kooraalyawm.Model;

import java.util.Date;

public class Game {

    public String getGameId() {
        return GameId;
    }

    public void setGameId(String gameId) {
        GameId = gameId;
    }

    public String getStadium() {
        return Stadium;
    }

    public void setStadium(String stadium) {
        Stadium = stadium;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public int getRequiredNumber() {
        return RequiredNumber;
    }

    public void setRequiredNumber(int requiredNumber) {
        RequiredNumber = requiredNumber;
    }

    public int getFees() {
        return Fees;
    }

    public void setFees(int fees) {
        Fees = fees;
    }

    public int getRolling() {
        return Rolling;
    }

    public void setRolling(int rolling) {
        Rolling = rolling;
    }

    public int getCanceled() {
        return Canceled;
    }

    public void setCanceled(int canceled) {
        Canceled = canceled;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }


    private String GameId;
    private String Stadium;

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    private String Latitude;
    private String Longitude;
    //delete later
    private String Location;
    private int RequiredNumber;
    private int Fees;
    private int Rolling;
    private int Canceled;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private Date date;
    private boolean isValid;

    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String groupID) {
        GroupID = groupID;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    private String GroupID;
    private String GroupName;

}
