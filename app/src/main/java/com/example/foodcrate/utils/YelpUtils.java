package com.example.foodcrate.utils;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.foodcrate.MainActivity;
import com.example.foodcrate.data.YelpItem;
import com.example.foodcrate.data.YelpReviewItem;
import com.google.gson.Gson;

import java.net.URI;
import java.util.ArrayList;

public class YelpUtils {
    private final static String BASE_URL = "https://api.yelp.com/v3/businesses/search?";
    private final static String REVIEW_URL = "https://api.yelp.com/v3/businesses/";
    private String BUSINESS_ID;
    //private final static String API_KEY = "n6jgTLaklmxY6pP38tHCYVfh7uzsuPyaDbCVxov_l0XALUFvv9rogrGCCYKD05m0UqFyQ1g1xuchvW--qNKk-mK_FHDOIAVwEYJkFiFTBwxDho8UvvbXM9CTNwFoXnYx";
    private final static String TERM = "term";
    private final static String PRICE = "price";
    private final static String OPEN_NOW = "open_now";
    private final static String LAT_PARAM = "latitude";
    private final static String LON_PARAM = "longitude";

    private final static String LIMIT = "limit";
    // This is the max :(
    private final static String NUMBER_OF_OBJECTS = "50";


    static class YelpQueryResults {
        public YelpQueryListItem[] businesses;
    }

    static class YelpQueryListItem {
        public String id;
        public String alias;
        public String name;
        public String image_url;
        public boolean is_closed;
        public String url;
        public int review_count;
        public float rating;
        public YelpQueryItemCoordinates coordinates;
        public String[] transactions;
        public String price;
        public YelpQueryItemLocation location;
        public String phone;
        public String display_phone;
        public float distance;
    }

    static class YelpQueryItemCoordinates {
        public float latitude;
        public float longitude;
    }

    static class YelpQueryItemLocation {
        public String address1;
        public String address2;
        public String address3;
        public String city;
        public String zip_code;
        public String country;
        public String state;
    }

    static class YelpReviewResults {
        public YelpReviewListItem[] reviews;
    }

    static class YelpReviewListItem {
        public String id;
        public int rating;
        public YelpUserData user;
        public String text;
        public String time_created;
    }

    static class YelpUserData{
        public String name;
        public String image_url;
        public String profile_url;
        public String id;
    }

    public static String buildYelpQuery(String term, String lon, String lat, String price_pref, boolean open_now) {

        return Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(TERM, term)
                .appendQueryParameter(LAT_PARAM, lat)
                .appendQueryParameter(LON_PARAM, lon)
                .appendQueryParameter(PRICE, price_pref)
                .appendQueryParameter(OPEN_NOW, String.valueOf(open_now))
                .appendQueryParameter(LIMIT, NUMBER_OF_OBJECTS)
                .build()
                .toString();
    }

    public static String buildYelpReviewQuery(String id) {
        return REVIEW_URL + id + "/reviews";
    }

    public static ArrayList<YelpItem> parseYelpQueryResults(String yelpJSON) {
        Gson gson = new Gson();
        YelpQueryResults results = gson.fromJson(yelpJSON, YelpQueryResults.class);
        if (results != null && results.businesses != null) {
            ArrayList<YelpItem> yelpItems = new ArrayList<>();

            /*
             * Loop through all results parsed from JSON and condense each one into one
             * single-level Yelp object.
             */
            for (YelpQueryListItem listItem : results.businesses) {
                YelpItem yelpItem = new YelpItem();

                yelpItem.name = listItem.name;
                yelpItem.id = listItem.id;
                yelpItem.reviewCount = listItem.review_count;
                yelpItem.price = listItem.price;
                yelpItem.rating = listItem.rating;
                yelpItem.isClosed = listItem.is_closed;

                yelpItem.imageUrl = listItem.image_url;
                yelpItem.url = listItem.url;

                yelpItem.phone = listItem.phone;
                yelpItem.displayPhone = listItem.display_phone;

                // Have to convert the transactions to one string
                String str = "";
                String strSeparator = ", ";
                for (int i = 0;i<listItem.transactions.length; i++) {
                    if (listItem.transactions[i].equals("restaurant_reservation")) {
                        listItem.transactions[i] = "restaurant reservation";
                    }
                    str = str+listItem.transactions[i];
                    // Do not append comma at the end of last element
                    if(i<listItem.transactions.length-1){
                        str = str+strSeparator;
                    }
                }

                yelpItem.transactions = str;

                yelpItem.longitude = listItem.coordinates.longitude;
                yelpItem.latitude = listItem.coordinates.latitude;

                yelpItem.address1 = listItem.location.address1;
                yelpItem.address2 = listItem.location.address2;
                yelpItem.address3 = listItem.location.address3;
                yelpItem.zipCode = listItem.location.zip_code;
                yelpItem.city = listItem.location.city;
                yelpItem.state = listItem.location.state;
                yelpItem.country = listItem.location.country;

                yelpItem.distance = listItem.distance;

                yelpItems.add(yelpItem);
            }

            return yelpItems;
        } else {
            return null;
        }
    }

    public static ArrayList<YelpReviewItem> parseYelpReviewResults (String yelpJSON) {
        Gson gson = new Gson();
        YelpReviewResults results = gson.fromJson(yelpJSON, YelpReviewResults.class);
        if (results != null && results.reviews != null) {
            ArrayList<YelpReviewItem> yelpReviewItems = new ArrayList<>();

            for (YelpReviewListItem listItem : results.reviews) {
                YelpReviewItem yelpReviewItem = new YelpReviewItem();

                yelpReviewItem.id = listItem.id;
                yelpReviewItem.rating = Integer.toString(listItem.rating);
                yelpReviewItem.text = listItem.text;
                yelpReviewItem.time_created = listItem.time_created;
                yelpReviewItem.name = listItem.user.name;
                yelpReviewItem.image_url = listItem.user.image_url;
                yelpReviewItem.profile_url = listItem.user.profile_url;
                yelpReviewItem.userId = listItem.user.id;

                yelpReviewItems.add(yelpReviewItem);
            }

            return yelpReviewItems;
        } else {
            return null;
        }
    }
}
