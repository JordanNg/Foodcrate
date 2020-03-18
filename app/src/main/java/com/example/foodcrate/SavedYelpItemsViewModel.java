package com.example.foodcrate;

import android.app.Application;
import android.app.ListActivity;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.foodcrate.data.SavedYelpItemsRepository;
import com.example.foodcrate.data.YelpItem;

import java.util.List;

public class SavedYelpItemsViewModel extends AndroidViewModel {
    private SavedYelpItemsRepository mSavedYelpItemsRepository;

    public SavedYelpItemsViewModel(Application application) {
        super(application);
        mSavedYelpItemsRepository = new SavedYelpItemsRepository(application);
    }

    public void insertYelpItem(YelpItem yelpItem) {
        mSavedYelpItemsRepository.insertYelpItem(yelpItem);
    }

    public void deleteYelpItem(YelpItem yelpItem) {
        mSavedYelpItemsRepository.deleteYelpItem(yelpItem);
    }

    public LiveData<List<YelpItem>> getAllYelpItems() {
        return mSavedYelpItemsRepository.getAllYelpItems();
    }
}
