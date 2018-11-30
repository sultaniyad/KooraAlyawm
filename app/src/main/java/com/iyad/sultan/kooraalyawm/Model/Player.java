package com.iyad.sultan.kooraalyawm.Model;

import java.util.List;

public class Player {

    public String getPlayerId() {
        return PlayerId;
    }

    public void setPlayerId(String playerId) {
        PlayerId = playerId;
    }

    public String getPlayerName() {
        return PlayerName;
    }

    public void setPlayerName(String playerName) {
        PlayerName = playerName;
    }

    public String getPlayerIcon() {
        return PlayerIcon;
    }

    public void setPlayerIcon(String playerIcon) {
        PlayerIcon = playerIcon;
    }

    public String getPrivilege() {
        return Privilege;
    }

    public void setPrivilege(String privilege) {
        Privilege = privilege;
    }

    private String PlayerId;
    private String PlayerName;
    private String PlayerIcon;
    private String Privilege;

    public List<Group> getUserGroup() {
        return UserGroup;
    }

    public void setUserGroup(List<Group> userGroup) {
        UserGroup = userGroup;
    }

    private List<Group> UserGroup;

}
