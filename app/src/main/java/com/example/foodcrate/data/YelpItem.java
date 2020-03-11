package com.example.foodcrate.data;

import java.io.Serializable;

public class YelpItem implements Serializable {
    public String name;
    public int reviewCount;
    public boolean isClosed;
    public float rating;
    public String price;
    public String url;

    public String phone;
    public String displayPhone;

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
}
