package com.iyad.sultan.kooraalyawm.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Group3 {

    private String groupId;
    private String groupName;
    private boolean isProtected;
    private String groupPassword;

    private Map<String,Object> members;
    private Map<String,Object> currentGames;

    public Group3(){}

    public Group3(String groupId, String groupName, Boolean isProtected, String groupPassword, String groupLogo,
                  Map<String,Object> members, Map<String,Object> currentGames){

        this.groupId = groupId;
        this.groupName = groupName;
        this.isProtected = isProtected;
        this.groupPassword = groupPassword;
        this.groupLogo = groupLogo;
        this.members = members;
        this.currentGames = currentGames;
    }



    public Map<String, Object> getMembers() {
        return members;
    }

    public void setMembers(Map<String, Object> members) {
        this.members = members;
    }

    public Map<String, Object> getCurrentGames() {
        return currentGames;
    }

    public void setCurrentGames(Map<String, Object> currentGames) {
        this.currentGames = currentGames;
    }


    public String getGroupLogo() {
        return groupLogo;
    }

    public void setGroupLogo(String groupLogo) {
        groupLogo = groupLogo;
    }

    private String groupLogo;

    public int getCurrentPlayers() {
        return CurrentPlayers;
    }

    public void setCurrentPlayers(int currentPlayers) {
        CurrentPlayers = currentPlayers;
    }

    private int CurrentPlayers;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        groupName = groupName;
    }

    public boolean isProtected() {
        return isProtected;
    }

    public void setProtected(boolean aProtected) {
        isProtected = aProtected;
    }

    public String getGroupPassword() {
        return groupPassword;
    }

    public void setGroupPassword(String groupPassword) {
        groupPassword = groupPassword;
    }

/*
    //get list of valid games for this group /*Only one game valid**
    public List<Game> getGames(){
        List<Game> list = new ArrayList<>(10);
        return list;
    }
    //get the last game added to tree then check the date if if expire do'nt show it game (shwow status)
    public Game getLastGame(){

        Game game = new Game();
        return game;
    }*/
}
