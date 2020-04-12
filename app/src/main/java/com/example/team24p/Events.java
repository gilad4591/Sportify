package com.example.team24p;

import java.util.Date;

public class Events {
    String ground,username;
    String  date;
    String Hour;

    public Events(){
        this.ground = "";
        this.username = "";
        this.date = "";
        this.Hour = "";
    }

    public Events(String ground, String username, String date,String Hour) {
        this.ground = ground;
        this.username = username;
        this.date = date;
        this.Hour = Hour;
    }

    public void setHour(String hour) {
        Hour = hour;
    }

    public String getHour() {
        return Hour;
    }

    public String getGround() {
        return ground;
    }

    public String getUsername() {
        return username;
    }

    public String getDate() {
        return date;
    }

    public void setGround(String ground) {
        this.ground = ground;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
