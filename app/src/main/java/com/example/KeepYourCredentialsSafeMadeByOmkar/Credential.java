package com.example.KeepYourCredentialsSafeMadeByOmkar;

public class Credential {
    String title,account,password,date;

    Credential(){

    }

    public Credential(String title, String account, String password,String date) {
        this.title = title;
        this.account = account;
        this.password = password;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
