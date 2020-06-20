package com.sophie.miller.portablecloset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.sophie.miller.portablecloset.constants.FragmentCodes;
import com.sophie.miller.portablecloset.objects.ClothesFilter;
import com.sophie.miller.portablecloset.ui.fragments.ClothesDetailInfoFragment;
import com.sophie.miller.portablecloset.ui.fragments.ClothesEditDetailFragment;
import com.sophie.miller.portablecloset.ui.fragments.FilteredItemsFragment;
import com.sophie.miller.portablecloset.ui.fragments.MainFragment;
import com.sophie.miller.portablecloset.viewModel.MainViewModel;
import com.sophie.miller.portablecloset.viewModel.MainViewModelFactory;

public class MainActivity extends AppCompatActivity {

    //saved state keys
    private static final String CURRENT_FILTER = "CURRENT_FILTER";
    //variables to pass between fragments
    private long detailId = -1;
    //view model
    MainViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //view model
        MainViewModelFactory factory = new MainViewModelFactory(this.getApplication());
        mViewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);

        if (getIntent().hasExtra(FragmentCodes.OPEN_FRAGMENT)) {
            setFragment(getIntent().getIntExtra(FragmentCodes.OPEN_FRAGMENT, 0));
        } else if (savedInstanceState == null) {
            setFragment(0);
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    public void setFragment(int fragmentNumber) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, getFragment(fragmentNumber), getFragmentTag(fragmentNumber))
                .commitNow();
    }

    /**
     * returns required fragment
     *
     * @param fragmentNumber number of sragment
     * @return Fragment instance
     */
    private Fragment getFragment(int fragmentNumber) {
        switch (fragmentNumber) {
            case FragmentCodes.FILTERED_LIST_FRAGMENT:
                new FilteredItemsFragment();
                return FilteredItemsFragment.newInstance();
            case FragmentCodes.DETAIL_EDIT_FRAGMENT:
                new ClothesEditDetailFragment();
                return ClothesEditDetailFragment.newInstance();
            case FragmentCodes.DETAIL_INFO_FRAGMENT:
                new ClothesDetailInfoFragment();
                return ClothesDetailInfoFragment.newInstance();
            default:
                new MainFragment();
                return MainFragment.newInstance();
        }
    }

    /**
     * returns fragment ID as a String
     *
     * @param fragmentNumber fragment int id
     * @return id as string
     */
    private String getFragmentTag(int fragmentNumber) {
        switch (fragmentNumber) {
            case FragmentCodes.FILTERED_LIST_FRAGMENT:
                return String.valueOf(FragmentCodes.FILTERED_LIST_FRAGMENT);
            case FragmentCodes.DETAIL_EDIT_FRAGMENT:
                return String.valueOf(FragmentCodes.DETAIL_EDIT_FRAGMENT);
            case FragmentCodes.DETAIL_INFO_FRAGMENT:
                return String.valueOf(FragmentCodes.DETAIL_INFO_FRAGMENT);
            default:
                return String.valueOf(FragmentCodes.MAIN_FRAGMENT);
        }
    }

    /**
     * back button leads to the main fragment
     */
    @Override
    public void onBackPressed() {
        setFragment(FragmentCodes.MAIN_FRAGMENT);
        detailId = -1;
    }

    /**
     * set detail id
     */
    public void setDetailId(long id) {
        this.detailId = id;
    }

    /**
     * get current filter
     */
    public long getDetailId() {
        return detailId;
    }

    /**
     * get view model
     */
    public MainViewModel getViewModel(){
        if(mViewModel==null){
            MainViewModelFactory factory = new MainViewModelFactory(this.getApplication());
            mViewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);
        }
        return mViewModel;
    }

    /**
     * hide keyboard
     */
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * set toolbar
     */
    public void setToolbarTitle(String title){
        setTitle(title);
    }
}


