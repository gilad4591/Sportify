package com.example.team24p;

import java.util.Date;

public class Events {
    String ground,username;
    String  date;

    public Events(){}
    public Events(String ground, String username, String date) {
        this.ground = ground;
        this.username = username;
        this.date = date;
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
