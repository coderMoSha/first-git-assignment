package com.example.test1.viewmodels;

import com.example.test1.viewmodels.EnterUserDetails;

public class EnterProductDetails {

    public String titleDB, priceDB, descriptionDB, conditionDB,locationDB,imageurlDB;

    //empty constructor
  //  public EnterProductDetails();



    public EnterProductDetails (String title, String price, String description, String condition, String location, String uriDB){

        this.titleDB = title;
        this.priceDB = price;
        this.descriptionDB = description;
        this.conditionDB = condition;
        this.locationDB = location;
        this.imageurlDB =uriDB;



    }








}
