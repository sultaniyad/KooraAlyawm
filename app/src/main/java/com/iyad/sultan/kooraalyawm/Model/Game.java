package com.iyad.sultan.kooraalyawm.Model;

import java.util.Date;
import java.util.Map;

public class Game {


    private String id;
    private String stadium;

    private String location;

    private String requirednumber;

    private String registered;

    private String fees;
    private Map<String,Object> rolling;
    private String canceled;

    private String date;
    private boolean valid;

    private String groupid;
    private String groupname;

    public Game(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRequirednumber() {
        return requirednumber;
    }

    public void setRequirednumber(String requirednumber) {
        this.requirednumber = requirednumber;
    }

    public String getFees() {
        return fees;
    }

    public void setFees(String fees) {
        this.fees = fees;
    }

    public Map<String,Object> getRolling() {
        return rolling;
    }

    public void setRolling(Map<String,Object> rolling) {
        this.rolling = rolling;
    }

    public String getCanceled() {
        return canceled;
    }

    public void setCanceled(String canceled) {
        this.canceled = canceled;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }
    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }


}
