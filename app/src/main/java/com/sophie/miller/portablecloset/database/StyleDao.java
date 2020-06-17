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
    void insertStyle(ClothingItem product);

    @Delete
    void deleteStyle(ClothingItem product);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateStyle(ClothingItem product);

    @Query("SELECT name FROM styles WHERE id = :id")
    LiveData<String> getStyle(int id);
}
