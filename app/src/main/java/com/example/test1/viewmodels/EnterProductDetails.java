package com.example.test1.viewmodels;

import com.example.test1.viewmodels.EnterUserDetails;

public class EnterProductDetails {

    public String titleDB, priceDB, descriptionDB, conditionDB,locationDB,imageurlDB;

    //empty constructor
    public EnterProductDetails() {

    }


    public EnterProductDetails (String title, String price, String description, String condition, String location, String uriDB){

        this.titleDB = title;
        this.priceDB = price;
        this.descriptionDB = description;
        this.conditionDB = condition;
        this.locationDB = location;
        this.imageurlDB =uriDB;



    }

    public String getTitleDB()  {
        return titleDB;
    }

    public void setTitleDB(String titleDB) {
        this.titleDB = titleDB;
    }

    public String getPriceDB() {
        return priceDB;
    }

    public void setPriceDB(String priceDB) {
        this.priceDB = priceDB;
    }

    public String getDescriptionDB() {
        return descriptionDB;
    }

    public void setDescriptionDB(String descriptionDB) {
        this.descriptionDB = descriptionDB;
    }

    public String getConditionDB() {
        return conditionDB;
    }

    public void setConditionDB(String conditionDB) {
        this.conditionDB = conditionDB;
    }

    public String getLocationDB() {
        return locationDB;
    }

    public void setLocationDB(String locationDB) {
        this.locationDB = locationDB;
    }

    public String getImageurlDB() {
        return imageurlDB;
    }

    public void setImageurlDB(String imageurlDB) {
        this.imageurlDB = imageurlDB;
    }
}
