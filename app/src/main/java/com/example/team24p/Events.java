package com.example.team24p;

import java.util.ArrayList;
import java.util.Date;

public class Events {
    String ground;
    String  date;
    String Hour;
    ArrayList<User> users = new ArrayList<>();

    public Events(){
        this.ground = "";
        this.date = "";
        this.Hour = "";
    }

    public Events(String ground, String users, String date,String Hour) {
        this.ground = ground;
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

    public ArrayList<User> getUsername() {
        return users;
    }

    public String getDate() {
        return date;
    }

    public void setGround(String ground) {
        this.ground = ground;
    }

    public void setUsername(ArrayList users) {
        this.users = users;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
