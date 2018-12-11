package com.iyad.sultan.kooraalyawm.Model;

public class StadiumLocation {

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
}
