package com.iyad.sultan.kooraalyawm.Model;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private String GroupId;
    private String GroupName;
    private boolean isProtected;
    private String GroupPassword;

    public String getGroupLogo() {
        return GroupLogo;
    }

    public void setGroupLogo(String groupLogo) {
        GroupLogo = groupLogo;
    }

    private String GroupLogo;

    public int getCurrentPlayers() {
        return CurrentPlayers;
    }

    public void setCurrentPlayers(int currentPlayers) {
        CurrentPlayers = currentPlayers;
    }

    private int CurrentPlayers;

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String groupId) {
        GroupId = groupId;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public boolean isProtected() {
        return isProtected;
    }

    public void setProtected(boolean aProtected) {
        isProtected = aProtected;
    }

    public String getGroupPassword() {
        return GroupPassword;
    }

    public void setGroupPassword(String groupPassword) {
        GroupPassword = groupPassword;
    }

    //get list of valid games for this group /*Only one game valid**
    public List<Game> getGames(){
        List<Game> list = new ArrayList<>(10);
        return list;
    }
    //get the last game added to tree then check the date if if expire do'nt show it game (shwow status)
    public Game getLastGame(){

        Game game = new Game();
        return game;
    }
}
