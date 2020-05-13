package com.example.team24p;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Events implements Serializable {
    String ground;
    String  date;
    String Hour;
    String id;
    String maxP;
    ArrayList<User> users = new ArrayList<>();

    public Events(){
        this.id = "";
        this.ground = "";
        this.date = "";
        this.Hour = "";
        this.maxP = "";
    }

    public Events(String ground, String users, String date,String Hour,String id,String maxP) {
        this.ground = ground;
        this.date = date;
        this.Hour = Hour;
        this.id = id;
        this.maxP = maxP;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
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

    public String getId() {
        return id;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setMaxp(String maxp) {
        this.maxP = maxp;
    }

    public String getMaxP() {
        return maxP;
    }
}
