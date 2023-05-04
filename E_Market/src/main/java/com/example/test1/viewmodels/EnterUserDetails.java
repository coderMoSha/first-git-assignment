package com.example.test1.viewmodels;

public class EnterUserDetails {

    public String fnameDB ,lnameDB , dobDB, genderDB, userID;

    //public EnterUserDetails();

    public EnterUserDetails() {
    }

    public EnterUserDetails(String fnameDB, String lnameDB, String dobDB, String genderDB, String userID) {
        this.fnameDB = fnameDB;
        this.lnameDB = lnameDB;
        this.dobDB = dobDB;
        this.genderDB = genderDB;
        this.userID = userID;
    }

    public String getFnameDB() {
        return fnameDB;
    }

    public void setFnameDB(String fnameDB) {
        this.fnameDB = fnameDB;
    }

    public String getLnameDB() {
        return lnameDB;
    }

    public void setLnameDB(String lnameDB) {
        this.lnameDB = lnameDB;
    }

    public String getDobDB() {
        return dobDB;
    }

    public void setDobDB(String dobDB) {
        this.dobDB = dobDB;
    }

    public String getGenderDB() {
        return genderDB;
    }

    public void setGenderDB(String genderDB) {
        this.genderDB = genderDB;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

}
