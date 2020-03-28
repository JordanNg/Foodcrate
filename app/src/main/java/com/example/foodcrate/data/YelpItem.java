package com.example.foodcrate.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "yelpTable")
public class YelpItem implements Serializable {
    public String name;

    @PrimaryKey
    @NonNull
    public String id;
    public int reviewCount;
    public boolean isClosed;
    public float rating;
    public String price;
    public String imageUrl;
    public String url;

    public String phone;
    public String displayPhone;

    public String transactions;

    public float latitude;
    public float longitude;

    public String address1;
    public String address2;
    public String address3;
    public String city;
    public String state;
    public String zipCode;
    public String country;

    public float distance;

    public String date;
}
