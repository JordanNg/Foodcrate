package com.example.foodcrate.utils;

import android.net.Uri;

import com.example.foodcrate.data.YelpItem;
import com.google.gson.Gson;

import java.net.URI;
import java.util.ArrayList;

public class YelpUtils {
    private final static String BASE_URL = "https://api.yelp.com/v3/businesses/search?";
    //private final static String API_KEY = "n6jgTLaklmxY6pP38tHCYVfh7uzsuPyaDbCVxov_l0XALUFvv9rogrGCCYKD05m0UqFyQ1g1xuchvW--qNKk-mK_FHDOIAVwEYJkFiFTBwxDho8UvvbXM9CTNwFoXnYx";
    private final static String TERM = "term";
    private final static String PRICE = "price";
    private final static String LAT_PARAM = "latitude";
    final static String LAT = "37.786882";
    private final static String LON_PARAM = "longitude";
    final static String LON = "-122.399972";

    private final static String LIMIT = "limit";
    private final static String NUMBER_OF_OBJECTS = "10";


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


    public static String buildYelpQuery(String term) {
        return Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(TERM, term)
                .appendQueryParameter(LAT_PARAM, LAT)
                .appendQueryParameter(LON_PARAM, LON)
                .appendQueryParameter(LIMIT, NUMBER_OF_OBJECTS)
                .build()
                .toString();
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
                yelpItem.reviewCount = listItem.review_count;
                yelpItem.price = listItem.price;
                yelpItem.rating = listItem.rating;
                yelpItem.isClosed = listItem.is_closed;
                yelpItem.url = listItem.url;

                yelpItem.phone = listItem.phone;
                yelpItem.displayPhone = listItem.display_phone;

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
}
