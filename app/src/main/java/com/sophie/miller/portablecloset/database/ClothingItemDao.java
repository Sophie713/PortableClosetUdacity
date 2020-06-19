package com.sophie.miller.portablecloset.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.sophie.miller.portablecloset.objects.ClothingItem;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ClothingItemDao {

    @Query("SELECT * FROM clothing_items WHERE id = :id")
    ClothingItem getClItemById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertClItem(ClothingItem product);

    @Delete
    void deleteClItem(ClothingItem product);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateClItem(ClothingItem product);

    @Query("UPDATE clothing_items SET style = -1 WHERE id = :id")
    void updateStyle(long id);

    @Query("SELECT * FROM clothing_items ORDER BY id")
    List<ClothingItem> loadAllClothes();

    @Query("SELECT * FROM clothing_items WHERE color = :color OR color = -1")
    List<ClothingItem> getFilteredClothesByColor(int color);

    @Query("SELECT * FROM clothing_items WHERE style = :style OR style = -1")
    List<ClothingItem> getFilteredClothesByStyle(long style);

    @Query("SELECT * FROM clothing_items WHERE size = :size OR size = ''")
    List<ClothingItem> getFilteredClothesBySize(String size);

    @Query("SELECT * FROM clothing_items WHERE (size = :size OR size = '') AND (style = :style OR style = -1)")
    List<ClothingItem> getFilteredClothesBySizeStyle(String size, int style);

    @Query("SELECT * FROM clothing_items WHERE (size = :size OR size = '') AND (color = :color OR color = -1)")
    List<ClothingItem> getFilteredClothesBySizeColor(String size, int color);

    @Query("SELECT * FROM clothing_items WHERE (style = :style OR style = -1) AND (color = :color OR color = -1)")
    List<ClothingItem> getFilteredClothesByStyleColor(long style, int color);

    @Query("SELECT * FROM clothing_items WHERE (size = :size OR size = '') AND (style = :style OR style = -1) AND (color = :color OR color = -1)")
    List<ClothingItem> getFilteredClothesBySizeStyleColor(String size, long style, int color);

}
