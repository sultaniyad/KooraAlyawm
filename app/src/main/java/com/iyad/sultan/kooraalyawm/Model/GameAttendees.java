package com.iyad.sultan.kooraalyawm.Model;

public class GameAttendees {

    public GameAttendees(Player player,boolean isAttend){

        this.player = player;
        this.isAttend = isAttend;
    }
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean getIsAttend() {
        return isAttend;
    }

    public void setIsAttend(boolean isAttend) {
        this.isAttend = isAttend;
    }

    private Player player;
    private boolean isAttend;
}
