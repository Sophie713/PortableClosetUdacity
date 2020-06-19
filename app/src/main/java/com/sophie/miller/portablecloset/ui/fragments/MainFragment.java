package com.sophie.miller.portablecloset.ui.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.sophie.miller.portablecloset.MainActivity;
import com.sophie.miller.portablecloset.R;
import com.sophie.miller.portablecloset.constants.FragmentCodes;
import com.sophie.miller.portablecloset.databinding.FragmentMainBinding;
import com.sophie.miller.portablecloset.objects.ClothesFilter;
import com.sophie.miller.portablecloset.objects.Colors;
import com.sophie.miller.portablecloset.utils.StringHandler;
import com.sophie.miller.portablecloset.viewModel.MainViewModel;
import com.sophie.miller.portablecloset.viewModel.MainViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private FragmentMainBinding binding;
    private MainActivity activity;
    //colors spinner
    private Colors colorsObject = new Colors();
    private ArrayAdapter<String> colorsAdapter;
    private List<String> colors = new ArrayList<>();
    //styles spinner
    private ArrayAdapter<String> stylesAdapter;
    private List<String> styles = new ArrayList<>();

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentMainBinding.bind(view);
        //view model//view model
        mViewModel = activity.getViewModel();
        mViewModel.listOfStyleNames().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> newStyles) {
                styles.clear();
                styles.add(getString(R.string.all_styles));
                styles.addAll(newStyles);
                stylesAdapter = new ArrayAdapter<String>(activity, R.layout.item_spinner, styles);
                binding.fragmentMainSpinnerStyle.setAdapter(stylesAdapter);
            }
        });

        binding.fragmentMainFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClothesFilter filter = new ClothesFilter();
                //retrieve color if present
                filter.setColorFilter(binding.fragmentMainSpinnerColor.getSelectedItemPosition() - 1);
                //retrieve style if present
                filter.setStyleFilter(getStyleId(binding.fragmentMainSpinnerStyle.getSelectedItemPosition()));
                //retrieve size if present
                filter.setSizeFilter(StringHandler.getText(binding.fragmentMainEdittextSize));
                mViewModel.setFilter(filter);
                activity.setFragment(FragmentCodes.FILTERED_LIST_FRAGMENT);
            }

        });
        binding.fragmentMainFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.setFragment(FragmentCodes.DETAIL_EDIT_FRAGMENT);
            }
        });

        colors.add(getString(R.string.any_color));
        colors.addAll(colorsObject.getAllColors());
        colorsAdapter = new ArrayAdapter<String>(activity, R.layout.item_spinner, colors);
        binding.fragmentMainSpinnerColor.setAdapter(colorsAdapter);
    }


    private int getStyleId(int position) {
        String style = styles.get(position);
        return mViewModel.database.styleDao().getStyleId(style);
    }
}
