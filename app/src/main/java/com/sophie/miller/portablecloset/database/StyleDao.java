package com.sophie.miller.portablecloset.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.sophie.miller.portablecloset.objects.ClothingItem;
import com.sophie.miller.portablecloset.objects.Style;

import java.util.List;

@Dao
public interface StyleDao {
    @Query("SELECT * FROM styles ORDER BY id")
    LiveData<List<Style>> loadAllStyles();

    @Query("SELECT name FROM styles ORDER BY id")
    LiveData<List<String>> loadStyleNames();

    @Insert
    void insertStyle(Style style);

    @Delete
    void deleteStyle(Style style);



    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateStyle(Style style);

    @Query("DELETE FROM styles WHERE id = :id")
    void deleteStyleAt(int id);

    @Query("SELECT name FROM styles WHERE id = :id")
    LiveData<String> getStyle(int id);

    @Query("SELECT id FROM styles WHERE name = :name")
    int getStyleId(String name);

    @Query("SELECT COUNT (name) FROM styles WHERE name = :name")
    int checkIfExists(String name);
}
