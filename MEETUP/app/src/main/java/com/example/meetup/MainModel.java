package com.example.meetup;

public class MainModel {
    String uid,name,email,interest;

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public MainModel(String name, String uid, String email, String interest) {

        this.name = name;
        this.uid = uid;
        this.email = email;
        this.interest = interest;
    }

}
