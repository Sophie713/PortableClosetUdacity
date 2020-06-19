package com.sophie.miller.portablecloset.ui.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sophie.miller.portablecloset.MainActivity;
import com.sophie.miller.portablecloset.R;
import com.sophie.miller.portablecloset.constants.FragmentCodes;
import com.sophie.miller.portablecloset.databinding.FragmentClothesDetailInfoBinding;
import com.sophie.miller.portablecloset.objects.ClothingItem;
import com.sophie.miller.portablecloset.objects.Colors;
import com.sophie.miller.portablecloset.utils.BitmapHandler;
import com.sophie.miller.portablecloset.utils.Notifications;
import com.sophie.miller.portablecloset.viewModel.MainViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClothesDetailInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClothesDetailInfoFragment extends Fragment {
    //data and master classes
    private MainViewModel mViewModel;
    private MainActivity activity;
    private FragmentClothesDetailInfoBinding binding;
    //id of the item
    private long itemsId = -1;
    private static final String ITEMS_ID = "ITEMS_ID";
    //current clothing item
    ClothingItem item;
    private static final String ITEM = "ITEM";
    //deleting
    private boolean isDeleting = false;

    public static ClothesDetailInfoFragment newInstance() {
        return new ClothesDetailInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            item = savedInstanceState.getParcelable(ITEM);
            itemsId = savedInstanceState.getLong(ITEMS_ID);
        } else {
            if (activity != null) {
                itemsId = activity.getDetailId();
            }
        }
        return inflater.inflate(R.layout.fragment_clothes_detail_info, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
        //view model
        mViewModel = activity.getViewModel();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentClothesDetailInfoBinding.bind(view);
        //set on clicks
        binding.fragmentDetailMainLayout.setOnClickListener(v-> activity.hideKeyboard());
        binding.fragmentDetailFab.setOnClickListener(v -> {
            activity.setFragment(FragmentCodes.DETAIL_EDIT_FRAGMENT);
        });
        binding.fragmentDetailDelete.setOnClickListener(v -> {
            if (!isDeleting) {
                Notifications.makeToast(activity, getString(R.string.confirm_delete));
                isDeleting = true;
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isDeleting = false;
                    }
                }, 2000);
            } else {
                mViewModel.database.clothingItemDao().deleteClItem(item);
                activity.setFragment(FragmentCodes.FILTERED_LIST_FRAGMENT);
            }
        });
        fillUI();
    }

    /**
     * fill UI with the items details
     */
    private void fillUI() {
        if (itemsId > -1) {
            if (item == null) {
                item = mViewModel.database.clothingItemDao().getClItemById(itemsId);
            }
            binding.fragmentDetailName.setText(item.getName());
            Bitmap image = new BitmapHandler().byteArrayToBitmap(item.getImage());
            binding.fragmentDetailImage.setImageBitmap(image);
            String style = mViewModel.database.styleDao().getStyleName(item.getStyle());
            Notifications.log("info fill UI: "+style);
            binding.fragmentDetailColor.setText(style);
            String color = new Colors().colorsMap.get(item.getColor());
            Notifications.log("info fill UI: "+color);
            binding.fragmentDetailStyle.setText(color);
            binding.fragmentDetailSize.setText(item.getSize());
            binding.fragmentDetailNote.setText(item.getNote());
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ITEM, item);
        outState.putLong(ITEMS_ID, itemsId);
    }
}
