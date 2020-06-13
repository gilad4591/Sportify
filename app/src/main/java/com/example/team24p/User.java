package com.example.team24p;

import java.io.Serializable;

//class for username (not user and pass)

public class User implements Serializable {
    private String userName;
    private String name;
    private String adress;
    private String isAdmin;
    private String id;
    private String age;
    private String phoneNumber;

    //default ctor

    public User() {
        this.userName = "";
        this.name = "";
        this.adress = "";
        this.isAdmin = "false";
        this.id = "";
        this.age = "";
        this.phoneNumber = "";
    }

    //copy c'tor

    public User(String userName, String name, String adress, String isAdmin, String id, String age, String phoneNumber) {
        this.userName = userName;
        this.name = name;
        this.adress = adress;
        this.isAdmin = isAdmin;
        this.id = id;
        this.age = age;
        this.phoneNumber = phoneNumber;
    }

    //--------setters and getters

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setAdmin(String admin) {
        isAdmin = admin;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAge() {
        return age;
    }

    public String isAdmin() {
        return isAdmin;
    }

    public String getId() {
        return id;
    }

    public String getAdress() {
        return adress;
    }

    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }
}
