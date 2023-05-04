package com.example.test1.viewmodels;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.test1.viewmodels.EnterUserDetails;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class EnterProductDetails implements Serializable {

    public String id,userId, titleDB, priceDB, descriptionDB, conditionDB,locationDB,imageurlDB;
    HashMap<String,Image> images;


    //empty constructor
    public EnterProductDetails() {

    }


    public EnterProductDetails (String id,String userId,String title, String price, String description, String condition, String location){

        this.titleDB = title;
        this.priceDB = price;
        this.descriptionDB = description;
        this.conditionDB = condition;
        this.locationDB = location;
        this.userId=userId;
        this.id=id;
       // this.imageurlDB =uriDB;



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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public HashMap<String, Image> getImages() {
        return images;
    }

    public void setImages(HashMap<String, Image> images) {
        this.images = images;
    }

}
