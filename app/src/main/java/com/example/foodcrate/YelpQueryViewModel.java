package com.example.foodcrate;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.foodcrate.data.Status;
import com.example.foodcrate.data.YelpItem;
import com.example.foodcrate.data.YelpQueryRepository;

import java.util.List;

public class YelpQueryViewModel extends ViewModel {
    private LiveData<List<YelpItem>> mQueryResults;
    private YelpQueryRepository mRepository;
    private LiveData<Status> mLoadingStatus;

    public YelpQueryViewModel() {
        mRepository = new YelpQueryRepository();
        mQueryResults = mRepository.getYelpQueryResults();

        mLoadingStatus = mRepository.getLoadingStatus();
    }

    public void loadQueryResults(String query, String lon, String lat) {
        mRepository.loadQueryResults(query, lon, lat);
    }

    public LiveData<List<YelpItem>> getQueryResults() {
        return mQueryResults;
    }

    public LiveData<Status> getLoadingStatus() {
        return mLoadingStatus;
    }
}
