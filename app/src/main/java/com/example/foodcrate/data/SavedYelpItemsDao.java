package com.example.foodcrate.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SavedYelpItemsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(YelpItem business);

    @Delete
    void delete(YelpItem business);

    @Query("SELECT * FROM yelpTable")
    LiveData<List<YelpItem>> getAllYelpItems();
}
