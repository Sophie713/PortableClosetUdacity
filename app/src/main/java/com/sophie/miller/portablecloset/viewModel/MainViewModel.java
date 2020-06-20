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

    public ClothesDatabase database;
    private MutableLiveData<ClothesFilter> filter = new MutableLiveData<>(new ClothesFilter());
    private LiveData<List<String>> styleNames;

    MainViewModel(@NonNull Application application) {
        super(application);
        database = ClothesDatabase.getInstance(this.getApplication());
        styleNames = database.styleDao().loadStyleNames();
    }

    /**
     * @return list of my clothes
     */
    public LiveData<ClothesFilter> getFilter() {
        return filter;
    }

    /**
     * setFilter
     * @param filter new clothes filter
     */
    public void setFilter(ClothesFilter filter) {
        this.filter.postValue(filter);
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
}
