package com.example.team24p;

public class UsersAndPasswords {
    private String userName;
    private String password;

    public UsersAndPasswords(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
    public UsersAndPasswords(){
        this.userName = "";
        this.password ="";
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
