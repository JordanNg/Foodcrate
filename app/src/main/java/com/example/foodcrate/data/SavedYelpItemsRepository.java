package com.example.foodcrate.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import java.util.List;

public class SavedYelpItemsRepository {
    private SavedYelpItemsDao mDAO;

    public SavedYelpItemsRepository(Application application) {
        YelpItemDatabase db = YelpItemDatabase.getDatabase(application);
        mDAO = db.savedYelpItemsDao();
    }

    public void insertYelpItem(YelpItem yelpItem) {
        new InsertYelpItemAsyncTask(mDAO).execute(yelpItem);
    }

    public void deleteYelpItem(YelpItem yelpItem) {
        new DeleteYelpItemAsyncTask(mDAO).execute(yelpItem);
    }

    public LiveData<List<YelpItem>> getAllYelpItems() {
        return mDAO.getAllYelpItems();
    }

    private static class InsertYelpItemAsyncTask extends AsyncTask<YelpItem, Void, Void> {
        private SavedYelpItemsDao mAsyncTaskDao;

        InsertYelpItemAsyncTask(SavedYelpItemsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(YelpItem... yelpItems) {
            mAsyncTaskDao.insert(yelpItems[0]);
            return null;
        }
    }

    private static class DeleteYelpItemAsyncTask extends AsyncTask<YelpItem, Void, Void> {
        private SavedYelpItemsDao mAsyncTaskDao;

        DeleteYelpItemAsyncTask(SavedYelpItemsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(YelpItem... yelpItems) {
            mAsyncTaskDao.delete(yelpItems[0]);
            return null;
        }
    }
}
