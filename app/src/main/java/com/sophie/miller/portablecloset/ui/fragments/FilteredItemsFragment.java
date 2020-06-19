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

    public static FilteredItemsFragment newInstance() {
        return new FilteredItemsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filtered_items, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
        //view model
        mViewModel = activity.getViewModel();
        mViewModel.getFilter().observe(this, new Observer<ClothesFilter>() {
            @Override
            public void onChanged(ClothesFilter clothesFilter) {
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
                currentFilter = clothesFilter;
                //todo set filters UI
            }
        });
        mViewModel.listOfStyleNames().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> newStyles) {
                styles.clear();
                styles.add(getString(R.string.all_styles));
                styles.addAll(newStyles);
                stylesAdapter = new ArrayAdapter<String>(activity, R.layout.item_spinner, styles);
                binding.fragmentItemsSpinnerStyle.setAdapter(stylesAdapter);
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
        //set up color spinner
        colors.add(getString(R.string.any_color));
        colors.addAll(colorsObject.getAllColors());
        colorsAdapter = new ArrayAdapter<String>(activity, R.layout.item_spinner, colors);
        binding.fragmentItemsSpinnerColor.setAdapter(colorsAdapter);
        setUpUIFilters();
        setOnChangeListeners();
        binding.fragmentItemsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setFragment(FragmentCodes.DETAIL_EDIT_FRAGMENT);
            }
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
     * sets UI based on filtered data
     */
    private void setUpUIFilters() {
        //set filtered data to the ui
        binding.fragmentItemsEdittextSize.setText(activity.currentFilter.getSizeFilter());
        binding.fragmentItemsSpinnerStyle.setSelection(activity.currentFilter.getStyleFilter() + 1, true);
        binding.fragmentItemsSpinnerColor.setSelection(activity.currentFilter.getColorFilter() + 1, true);

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
                currentFilter.setColorFilter(position-1);
                mViewModel.setFilter(currentFilter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
        binding.fragmentItemsSpinnerStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                currentFilter.setStyleFilter(position-1);
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
}
