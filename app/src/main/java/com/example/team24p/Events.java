package com.example.team24p;

import java.util.Date;

public class Events {
    String ground,username;
    Date date;

    public Events(){}
    public Events(String ground, String username, Date date) {
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

    public Date getDate() {
        return date;
    }

    public void setGround(String ground) {
        this.ground = ground;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
