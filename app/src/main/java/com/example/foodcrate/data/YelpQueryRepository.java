package com.example.foodcrate.data;

import android.location.LocationManager;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.foodcrate.MainActivity;
import com.example.foodcrate.utils.YelpUtils;

import java.util.List;

public class YelpQueryRepository implements YelpQueryAsyncTask.Callback{
    private MutableLiveData<List<YelpItem>> mYelpQueryResults;
    private MutableLiveData<Status> mLoadingStatus;

    public YelpQueryRepository() {
        mYelpQueryResults = new MutableLiveData<>();
        mYelpQueryResults.setValue(null);

        mLoadingStatus = new MutableLiveData<>();
        mLoadingStatus.setValue(Status.SUCCESS);
    }

    public LiveData<List<YelpItem>> getYelpQueryResults() {
        return mYelpQueryResults;
    }

    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }

    @Override
    public void onQueryFinished(List<YelpItem> queryResults) {
        mYelpQueryResults.setValue(queryResults);
        if (queryResults != null) {
            mLoadingStatus.setValue(Status.SUCCESS);
        } else {
            mLoadingStatus.setValue(Status.ERROR);
        }
    }

    public void loadQueryResults(String query, String lon, String lat, String price, boolean open_now, boolean deals) {
        mYelpQueryResults.setValue(null);
        String url = YelpUtils.buildYelpQuery(query, lon, lat, price, open_now, deals);
        Log.d("Executing Query: ", url);
        mLoadingStatus.setValue(Status.LOADING);
        new YelpQueryAsyncTask(this).execute(url);
    }
}
