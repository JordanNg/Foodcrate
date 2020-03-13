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
    }
}
