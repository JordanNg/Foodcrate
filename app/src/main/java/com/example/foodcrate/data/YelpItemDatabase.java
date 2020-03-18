package com.example.foodcrate.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {YelpItem.class}, version = 1)
public abstract class YelpItemDatabase extends RoomDatabase {
    public abstract SavedYelpItemsDao savedYelpItemsDao();
    private static volatile YelpItemDatabase INSTANCE;

    static YelpItemDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (YelpItemDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    YelpItemDatabase.class,
                                    "yelp_item_db"
                            ).build();
                }
            }
        }
        return INSTANCE;
    }
}
