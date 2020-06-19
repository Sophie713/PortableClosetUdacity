package com.sophie.miller.portablecloset.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.sophie.miller.portablecloset.database.ClothesDatabase;


public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final Application application;

    public MainViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(application);
    }
}
