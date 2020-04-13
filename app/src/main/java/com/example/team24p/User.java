package com.example.team24p;

public class User {
    private String userName;
    private String name;
    private String adress;
    private boolean isAdmin;
    private String id;
    private int age;
    private String phoneNumber;

    public User(String userName, String name, String adress, boolean isAdmin, String id, int age, String phoneNumber) {
        this.userName = userName;
        this.name = name;
        this.adress = adress;
        this.isAdmin = isAdmin;
        this.id = id;
        this.age = age;
        this.phoneNumber = phoneNumber;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getAge() {
        return age;
    }

    public boolean isAdmin() {
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
