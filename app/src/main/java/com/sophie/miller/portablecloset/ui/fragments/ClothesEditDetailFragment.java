package com.sophie.miller.portablecloset.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.sophie.miller.portablecloset.MainActivity;
import com.sophie.miller.portablecloset.R;
import com.sophie.miller.portablecloset.constants.IntentCodes;
import com.sophie.miller.portablecloset.databinding.FragmentClothesDetailEditingBinding;
import com.sophie.miller.portablecloset.dialogs.EditStylesDialog;
import com.sophie.miller.portablecloset.objects.ClothingItem;
import com.sophie.miller.portablecloset.utils.BitmapHandler;
import com.sophie.miller.portablecloset.viewModel.MainViewModel;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClothesEditDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClothesEditDetailFragment extends Fragment {
    //data and master classes
    private MainViewModel mViewModel;
    private FragmentClothesDetailEditingBinding binding;
    private MainActivity activity;
    //data
    private Bitmap imageBitmap;

    public static ClothesEditDetailFragment newInstance() {
        return new ClothesEditDetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_clothes_detail_editing, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentClothesDetailEditingBinding.bind(view);
        //set up on clicks
        binding.fragmentDetailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo if image not null ask first?
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, IntentCodes.REQUEST_IMAGE_CAPTURE);
                }

            }
        });
        binding.fragmentDetailFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo switch based on editing
                saveData();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentCodes.REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                binding.fragmentDetailImage.setImageBitmap(imageBitmap);
            } else {
                //todo notify about error
            }
        }
    }

    /**
     * save data to the database
     */
    private void saveData() {//todo check if not empty!!!!!!!!
        byte[] image = new byte[0];
        //todo  LOADING start
        //check if style exists and save  if not to styles, then get id and save to database
        //get name, size and color and save to the object. save object to the database, keep id and data
        if (imageBitmap != null) {
            image = new BitmapHandler().bitmapToByteArray(imageBitmap);
        } else {
            //todo inform user (dialog?)
        }
        ClothingItem clothingItem = new ClothingItem(
                getText(binding.fragmentDetailName),
                image,
                //todo fix
                binding.fragmentDetailColor.getSelectedItemPosition(),
                binding.fragmentDetailStyle.getSelectedItemPosition(),
                getText(binding.fragmentDetailSize),
                getText(binding.fragmentDetailNote)
        );
        mViewModel.getDatabase().clothingItemDao().insertClItem(clothingItem);
        //todo make AsyncTask post to fulfill requirements

    }

    /**
     * gets string from a view or returns ""
     *
     * @param v view to get string from
     * @return
     */
    private String getText(TextView v) {
        CharSequence text = v.getText();
        if (text != null) {
            return text.toString();
        } else {
            return "";
        }
    }

}
