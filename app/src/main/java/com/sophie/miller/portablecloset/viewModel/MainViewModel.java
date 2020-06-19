package com.sophie.miller.portablecloset.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.sophie.miller.portablecloset.database.ClothesDatabase;
import com.sophie.miller.portablecloset.objects.ClothesFilter;
import com.sophie.miller.portablecloset.objects.ClothingItem;
import com.sophie.miller.portablecloset.objects.Style;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    //database
    public ClothesDatabase database;
    //current clothes filter to get data from
    private ClothesFilter clothesFilter = new ClothesFilter();
    //filtered clothes
    private MutableLiveData<ClothesFilter> filter = new MutableLiveData<>();
    private LiveData<List<ClothingItem>> filteredClothes;
    //styles lists
    private LiveData<List<String>> styleNames;

    MainViewModel(@NonNull Application application) {
        super(application);
        database = ClothesDatabase.getInstance(this.getApplication());
        styleNames = database.styleDao().loadStyleNames();
        //set default filter value
        //filter.postValue(clothesFilter);
        filteredClothes = Transformations.switchMap(filter, f -> filterClothes());
    }

    /**
     * takes filters as parameters and returns the filtered data
     */
    private LiveData<List<ClothingItem>> filterClothes() {
        filteredClothes = null;
        String size = clothesFilter.getSizeFilter();
        int style = clothesFilter.getStyleFilter();
        int color = clothesFilter.getColorFilter();
        //booleans for simpler orientation
        boolean hasSizeFilter = (!size.equals(""));
        boolean hasStyleFilter = (style != -1);
        boolean hasColorFilter = (color != -1);
        //filtering process
        if (hasSizeFilter && hasStyleFilter && hasColorFilter) {
            return database.clothingItemDao().getFilteredClothesBySizeStyleColor(size, style, color);
        } else if (hasSizeFilter && hasStyleFilter) {
            return database.clothingItemDao().getFilteredClothesBySizeStyle(size, style);
        } else if (hasSizeFilter && hasColorFilter) {
            return database.clothingItemDao().getFilteredClothesBySizeColor(size, color);
        } else if (hasSizeFilter) {
            return database.clothingItemDao().getFilteredClothesBySize(size);
        } else if (hasStyleFilter && hasColorFilter) {
            return database.clothingItemDao().getFilteredClothesByStyleColor(style, color);
        } else if (hasStyleFilter) {
            return database.clothingItemDao().getFilteredClothesByStyle(style);
        } else if (hasColorFilter) {
            return database.clothingItemDao().getFilteredClothesByColor(color);
        } else {
            return database.clothingItemDao().loadAllClothes();
        }
    }

    /**
     * @return list of my clothes
     */
    public LiveData<List<ClothingItem>> listOfClothes() {
        return filteredClothes;
    }

    /**
     * @return list of style names ready for a spinner
     */
    public LiveData<List<String>> listOfStyleNames() {
        return styleNames;
    }

    /**
     * @return my database
     */
    public ClothesDatabase getDatabase() {
        if (database == null) {
            database = ClothesDatabase.getInstance(this.getApplication());
        }
        return database;
    }

    /**
     * set filter
     */
    public void setFilter(ClothesFilter filter) {
        this.clothesFilter = filter;
        this.filter.postValue(clothesFilter);
    }
}
