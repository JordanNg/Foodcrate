package com.example.foodcrate.utils;

import android.net.Uri;

import java.net.URI;

public class YelpUtils {
    private final static String BASE_URL = "https://api.yelp.com/v3/businesses/search?";
    //private final static String API_KEY = "n6jgTLaklmxY6pP38tHCYVfh7uzsuPyaDbCVxov_l0XALUFvv9rogrGCCYKD05m0UqFyQ1g1xuchvW--qNKk-mK_FHDOIAVwEYJkFiFTBwxDho8UvvbXM9CTNwFoXnYx";
    private final static String TERM = "term";
    private final static String LAT_PARAM = "latitude";
    final static String LAT = "37.786882";
    private final static String LON_PARAM = "longitude";
    final static String LON = "-122.399972";


    public static String buildYelpQuery(String term) {
        return Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(TERM, term)
                .appendQueryParameter(LAT_PARAM, LAT)
                .appendQueryParameter(LON_PARAM, LON)
                .build()
                .toString();
    }
}
