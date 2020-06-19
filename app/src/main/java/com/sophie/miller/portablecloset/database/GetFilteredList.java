package com.sophie.miller.portablecloset.database;

import androidx.lifecycle.LiveData;

import com.sophie.miller.portablecloset.objects.ClothesFilter;
import com.sophie.miller.portablecloset.objects.ClothingItem;

import java.util.ArrayList;
import java.util.List;

public class GetFilteredList {
    /**
     * takes filters as parameters and returns the filtered data
     */
    public ArrayList<ClothingItem> filterClothes(ClothesDatabase database, ClothesFilter clothesFilter) {
        ArrayList<ClothingItem> filteredClothes = new ArrayList<>();
        String size = clothesFilter.getSizeFilter();
        int style = clothesFilter.getStyleFilter();
        int color = clothesFilter.getColorFilter();
        //booleans for simpler orientation
        boolean hasSizeFilter = (!size.equals(""));
        boolean hasStyleFilter = (style != -1);
        boolean hasColorFilter = (color != -1);
        //filtering process
        if (hasSizeFilter && hasStyleFilter && hasColorFilter) {
            filteredClothes.addAll(database.clothingItemDao().getFilteredClothesBySizeStyleColor(size, style, color));
        } else if (hasSizeFilter && hasStyleFilter) {
             filteredClothes.addAll(database.clothingItemDao().getFilteredClothesBySizeStyle(size, style));
        } else if (hasSizeFilter && hasColorFilter) {
             filteredClothes.addAll(database.clothingItemDao().getFilteredClothesBySizeColor(size, color));
        } else if (hasSizeFilter) {
             filteredClothes.addAll(database.clothingItemDao().getFilteredClothesBySize(size));
        } else if (hasStyleFilter && hasColorFilter) {
             filteredClothes.addAll(database.clothingItemDao().getFilteredClothesByStyleColor(style, color));
        } else if (hasStyleFilter) {
             filteredClothes.addAll(database.clothingItemDao().getFilteredClothesByStyle(style));
        } else if (hasColorFilter) {
             filteredClothes.addAll(database.clothingItemDao().getFilteredClothesByColor(color));
        } else {
             filteredClothes.addAll(database.clothingItemDao().loadAllClothes());
        }
        return filteredClothes;
    }

}
