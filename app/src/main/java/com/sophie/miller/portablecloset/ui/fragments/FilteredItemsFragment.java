package com.sophie.miller.portablecloset.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.sophie.miller.portablecloset.MainActivity;
import com.sophie.miller.portablecloset.R;
import com.sophie.miller.portablecloset.adapter.ClothesAdapter;
import com.sophie.miller.portablecloset.constants.FragmentCodes;
import com.sophie.miller.portablecloset.database.GetFilteredList;
import com.sophie.miller.portablecloset.databinding.FragmentFilteredItemsBinding;
import com.sophie.miller.portablecloset.objects.ClothesFilter;
import com.sophie.miller.portablecloset.objects.ClothingItem;
import com.sophie.miller.portablecloset.objects.Colors;
import com.sophie.miller.portablecloset.utils.Notifications;
import com.sophie.miller.portablecloset.viewModel.MainViewModel;
import com.sophie.miller.portablecloset.viewModel.MainViewModelFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FilteredItemsFragment#newInstance} factory method to
 * create an instance of this fragment.
 * todo hide keyboard on outside click
 */
public class FilteredItemsFragment extends Fragment {
    private MainViewModel mViewModel;
    private MainActivity activity;
    FragmentFilteredItemsBinding binding;
    //filtered clothes
    private ArrayList<ClothingItem> filteredClothesList = new ArrayList<>();
    private ClothesAdapter clothesAdapter;
    private GetFilteredList filteringUtil = new GetFilteredList();
    //styles spinner
    private ArrayAdapter<String> stylesAdapter;
    private List<String> styles = new ArrayList<>();
    //colors spinner
    private Colors colorsObject = new Colors();
    private ArrayAdapter<String> colorsAdapter;
    private List<String> colors = new ArrayList<>();
    private ClothesFilter currentFilter = new ClothesFilter();
    int firstStart = 0;

    public static FilteredItemsFragment newInstance() {
        return new FilteredItemsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filtered_items, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
        //view model
        mViewModel = activity.getViewModel();
        //obsseve styles to know when my possible styles come
        mViewModel.listOfStyleNames().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> newStyles) {
                Notifications.log("styles came");
                styles.clear();
                styles.add(getString(R.string.all_styles));
                styles.addAll(newStyles);
                stylesAdapter = new ArrayAdapter<String>(activity, R.layout.item_spinner, styles);
                binding.fragmentItemsSpinnerStyle.setAdapter(stylesAdapter);
                //set up color spinner
                colors.add(getString(R.string.any_color));
                colors.addAll(colorsObject.getAllColors());
                colorsAdapter = new ArrayAdapter<String>(activity, R.layout.item_spinner, colors);
                binding.fragmentItemsSpinnerColor.setAdapter(colorsAdapter);
            }
        });
        //observe filter to know when to filter rec.view
        mViewModel.getFilter().observe(this, new Observer<ClothesFilter>() {
            @Override
            public void onChanged(ClothesFilter clothesFilter) {
                firstStart++;
                if (firstStart == 2) {
                    setupUI(clothesFilter);
                }
                Notifications.log("filter came. color: " + clothesFilter.getColorFilter() + " style: " + clothesFilter.getStyleFilter() + " size: " + clothesFilter.getSizeFilter());
                filteredClothesList.clear();
                filteredClothesList.addAll(filteringUtil.filterClothes(mViewModel.getDatabase(), clothesFilter));
                if (filteredClothesList.size() > 0) {
                    binding.fragmentItemsRecView.setVisibility(View.VISIBLE);
                    binding.fragmentItemsNoItems.setVisibility(View.GONE);
                } else {
                    binding.fragmentItemsRecView.setVisibility(View.GONE);
                    binding.fragmentItemsNoItems.setVisibility(View.VISIBLE);
                }
                updateRecView();
            }
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentFilteredItemsBinding.bind(view);
        //set up recyclerview
        binding.fragmentItemsRecView.setLayoutManager(new GridLayoutManager(activity, getNumberOfColumns()));
        clothesAdapter = new ClothesAdapter(filteredClothesList, activity);
        binding.fragmentItemsRecView.setAdapter(clothesAdapter);
        //fab -> edit item
        binding.fragmentItemsFab.setOnClickListener(v -> {
            activity.setFragment(FragmentCodes.DETAIL_EDIT_FRAGMENT);
        });

    }

    /**
     * update recycler view data
     */
    private void updateRecView() {
        clothesAdapter.insertNewData(filteredClothesList);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                clothesAdapter.notifyDataSetChanged();
            }
        });
    }


    /**
     * set number of columns based on screen size
     *
     * @return number of columns
     */
    private int getNumberOfColumns() {
        int columns = 1;
        // make a grid with a certain number of columns
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        if (width > 2000) {
            return 7;
        } else if (width > 1700) {
            return 6;
        } else if (width > 1357) {
            return 4;
        } else if (width > 719) {
            return 3;
        } else if (width > 200) {
            return 2;
        }
        return columns;
    }

    /**
     * set listeners to my filters
     */
    private void setOnChangeListeners() {
        //set up filter listeners
        binding.fragmentItemsSpinnerColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                currentFilter.setColorFilter(position - 1);
                mViewModel.setFilter(currentFilter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        binding.fragmentItemsSpinnerStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position != 0)
                    currentFilter.setStyleFilter(mViewModel.database.styleDao().getStyleId(styles.get(position)));
                else
                    currentFilter.setStyleFilter(-1);
                mViewModel.setFilter(currentFilter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        binding.fragmentItemsEdittextSize.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentFilter.setSizeFilter(s.toString());
                mViewModel.setFilter(currentFilter);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * returns the index of the style in the spinner
     *
     * @return
     */
    private int getStyleIndex(int filterId) {
        String styleName = mViewModel.database.styleDao().getStyleName(filterId);
        for (int i = 0; i < styles.size(); i++) {
            if (styles.get(i).equals(styleName)) {
                Notifications.log(styles.get(i) + " " + styleName);
                return i;
            }
        }
        return 0;
    }

    /**
     * sets UI based on filtered data
     */
    private void setupUI(ClothesFilter clothesFilter) {
        //size
        binding.fragmentItemsEdittextSize.setText(clothesFilter.getSizeFilter());
        //color
        binding.fragmentItemsSpinnerColor.setSelection(clothesFilter.getColorFilter() + 1, true);
        //style
        binding.fragmentItemsSpinnerStyle.setSelection(getStyleIndex(clothesFilter.getStyleFilter()), true);
        currentFilter.setColorFilter(clothesFilter.getColorFilter());
        currentFilter.setSizeFilter(clothesFilter.getSizeFilter());
        currentFilter.setStyleFilter(clothesFilter.getStyleFilter());
        setOnChangeListeners();
    }
    /** todo
     *
     *      * set listeners to my filters
     *      sets UI based on filtered data possible separate
     *      private void setUpUIFilters() {
     *         //set filtered data to the ui //todo make work
     *         binding.fragmentItemsEdittextSize.setText(currentFilter.getSizeFilter());
     *         Notifications.log("setUpUIFilters " + currentFilter.getSizeFilter());
     *         binding.fragmentItemsSpinnerStyle.setSelection(currentFilter.getStyleFilter() + 1, true);
     *         Notifications.log("setUpUIFilters " + currentFilter.getStyleFilter() + 1);
     *         f (firstStart++ < 3) {
     *                     currentFilter = clothesFilter;
     *                     Notifications.log("code got into listofstyle names ");
     *                     setUpUIFilters();
     *                     Notifications.log("setUpUIFilters style " + getStyleIndex());
     *                     setOnChangeListeners();
     *                     binding.fragmentItemsSpinnerStyle.setSelection(getStyleIndex(), true);
     *                     Notifications.log("on Attach:  current filter set  " + currentFilter.getSizeFilter() + " " + clothesFilter.getSizeFilter());
     *
     *                 }
     *     }
     */
}
