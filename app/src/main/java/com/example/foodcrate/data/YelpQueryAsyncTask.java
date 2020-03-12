package com.example.foodcrate.data;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

import com.example.foodcrate.CrateActivity;
import com.example.foodcrate.MainActivity;
import com.example.foodcrate.utils.NetworkUtils;
import com.example.foodcrate.utils.YelpUtils;

import java.io.IOException;
import java.util.List;

public class YelpQueryAsyncTask extends AsyncTask <String, Void, String> {
    private Callback mCallback;

    public interface Callback {
        void onQueryFinished(List<YelpItem> queryResults);
    }

    YelpQueryAsyncTask(Callback callback) {
        mCallback = callback;
    }

    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];
        String searchResults = null;
        try {
            searchResults = NetworkUtils.doHttpGet(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResults;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        List<YelpItem> yelpItems = null;
        if (s != null) {
            yelpItems = YelpUtils.parseYelpQueryResults(s);
        }

        mCallback.onQueryFinished(yelpItems);

        /*
        // Get a random item from our list
        int max = yelpItems.size() - 1;
        int rand = (int) ((Math.random() * ((max - 0) + 1)) + 0);

        Intent crateActivityIntent = new Intent(MainActivity.this, CrateActivity.class);
        crateActivityIntent.putExtra(CrateActivity.EXTRA_YELP_ITEM, yelpItems.get(rand));
        startActivity(crateActivityIntent);
        */
    }
}
