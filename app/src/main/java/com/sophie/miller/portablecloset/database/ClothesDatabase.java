package com.sophie.miller.portablecloset.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sophie.miller.portablecloset.objects.ClothingItem;
import com.sophie.miller.portablecloset.objects.Style;

@Database(entities = {ClothingItem.class, Style.class}, version = 1)
public abstract class ClothesDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "database";
    private static ClothesDatabase database;
    private static final Object OBJECT = new Object();

    public static ClothesDatabase getInstance(Context context) {
        if (database == null) {
            synchronized (OBJECT) {
                database = Room.databaseBuilder(context.getApplicationContext(), ClothesDatabase.class, ClothesDatabase.DATABASE_NAME).allowMainThreadQueries().build();
            }
        }
        return database;
    }

    public abstract ClothingItemDao clothingItemDao();

    public abstract StyleDao styleDao();
}
