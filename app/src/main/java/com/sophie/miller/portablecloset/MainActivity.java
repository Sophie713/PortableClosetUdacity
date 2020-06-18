package com.sophie.miller.portablecloset;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.sophie.miller.portablecloset.constants.FragmentCodes;
import com.sophie.miller.portablecloset.objects.ClothesFilter;
import com.sophie.miller.portablecloset.ui.fragments.ClothesEditDetailFragment;
import com.sophie.miller.portablecloset.ui.fragments.FilteredItemsFragment;
import com.sophie.miller.portablecloset.ui.fragments.MainFragment;

public class MainActivity extends AppCompatActivity {

    MainFragment mainFragment;
    ClothesEditDetailFragment clothesEditDetailFragment;
    FilteredItemsFragment filteredItemsFragment;
    //saved state keys
    private static final String CURRENT_FILTER = "CURRENT_FILTER";
    //variables to pass between fragments
    public ClothesFilter currentFilter = new ClothesFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent().hasExtra(FragmentCodes.OPEN_FRAGMENT)) {
            setFragment(getIntent().getIntExtra(FragmentCodes.OPEN_FRAGMENT, 0));
        } else if (savedInstanceState == null) {
            setFragment(0);
        } else {
            currentFilter = savedInstanceState.getParcelable(CURRENT_FILTER);
        }
    }

    public void setFragment(int fragmentNumber) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, getFragment(fragmentNumber), getFragmentTag(fragmentNumber))
                .commitNow();
    }

    /**
     * returns required fragment
     *
     * @param fragmentNumber
     * @return
     */
    private Fragment getFragment(int fragmentNumber) {
        switch (fragmentNumber) {
            case FragmentCodes.FILTERED_LIST_FRAGMENT:
                return filteredItemsFragment.newInstance();
            case FragmentCodes.DETAIL_EDIT_FRAGMENT:
                return clothesEditDetailFragment.newInstance();
            default:
                return mainFragment.newInstance();
        }
    }

    /**
     * returns fragment ID as a String
     *
     * @param fragmentNumber
     * @return
     */
    private String getFragmentTag(int fragmentNumber) {
        switch (fragmentNumber) {
            case FragmentCodes.FILTERED_LIST_FRAGMENT:
                return String.valueOf(FragmentCodes.FILTERED_LIST_FRAGMENT);
            case FragmentCodes.DETAIL_EDIT_FRAGMENT:
                return String.valueOf(FragmentCodes.DETAIL_EDIT_FRAGMENT);
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
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CURRENT_FILTER, currentFilter);
    }
}

