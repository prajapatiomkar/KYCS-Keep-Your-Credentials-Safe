package com.example.KeepYourCredentialsSafeMadeByOmkar;

public class Credential {
    private String title,userEmailId,password,date;

    public Credential(String title, String userEmailId, String password,String date) {
        this.title = title;
        this.userEmailId = userEmailId;
        this.password = password;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(String userEmailId) {
        this.userEmailId = userEmailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
