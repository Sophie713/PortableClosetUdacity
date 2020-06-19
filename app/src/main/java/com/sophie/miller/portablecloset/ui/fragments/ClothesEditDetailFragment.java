package com.sophie.miller.portablecloset.ui.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.sophie.miller.portablecloset.MainActivity;
import com.sophie.miller.portablecloset.R;
import com.sophie.miller.portablecloset.constants.FragmentCodes;
import com.sophie.miller.portablecloset.constants.IntentCodes;
import com.sophie.miller.portablecloset.databinding.DialogEditStylesBinding;
import com.sophie.miller.portablecloset.databinding.FragmentClothesDetailEditingBinding;
import com.sophie.miller.portablecloset.dialogs.EditStylesDialog;
import com.sophie.miller.portablecloset.objects.ClothingItem;
import com.sophie.miller.portablecloset.objects.Colors;
import com.sophie.miller.portablecloset.utils.BitmapHandler;
import com.sophie.miller.portablecloset.utils.StringHandler;
import com.sophie.miller.portablecloset.viewModel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

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
    //colors spinner
    private Colors colorsObject = new Colors();
    private ArrayAdapter<String> colorsAdapter;
    private List<String> colors = new ArrayList<>();
    //styles spinner
    private ArrayAdapter<String> stylesAdapter;
    private List<String> styles = new ArrayList<>();
    //monitor dialog to retrieve on oriantation change
    private boolean isDialogOpened = false;
    private final String DIALOG_OPENED = "DIALOG_OPENED";
    private int dialogTheme = android.R.style.Theme_Light;
    //if editing not adding
    private long editingId = -1;
    private static final String EDITING_ID = "EDITING_ID";
    //Clothing item
    private ClothingItem item;

    public static ClothesEditDetailFragment newInstance() {
        return new ClothesEditDetailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            editingId = savedInstanceState.getLong(EDITING_ID);
            isDialogOpened = savedInstanceState.getBoolean(DIALOG_OPENED);
            if (isDialogOpened)
                try {
                    new EditStylesDialog(this, mViewModel, dialogTheme);
                } catch (Exception ignored) {
                }
        } else {
            if (activity != null) {
                editingId = activity.getDetailId();
            }
        }
        return inflater.inflate(R.layout.fragment_clothes_detail_editing, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
        mViewModel = activity.getViewModel();
        mViewModel.listOfStyleNames().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> newStyles) {
                styles.clear();
                styles.add(getString(R.string.all_styles));
                styles.addAll(newStyles);
                styles.add(getString(R.string.edit_styles));
                stylesAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, styles);
                binding.fragmentDetailStyle.setAdapter(stylesAdapter);
            }
        });
        if (item != null) {
            binding.fragmentDetailStyle.setSelection(item.getStyle() + 1);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentClothesDetailEditingBinding.bind(view);
        //set on clicks
        binding.fragmentDetailImage.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, IntentCodes.REQUEST_IMAGE_CAPTURE);
            }
        });
        binding.fragmentDetailFab.setOnClickListener(v -> {
            saveData();
            activity.setDetailId(editingId);
            activity.setFragment(FragmentCodes.DETAIL_INFO_FRAGMENT);
        });
        colors.add(getString(R.string.any_color));
        colors.addAll(colorsObject.getAllColors());
        colorsAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, colors);
        binding.fragmentDetailColor.setAdapter(colorsAdapter);
        setStylesListener();
        fillUI();
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
    private void saveData() {
        byte[] image;
        if (imageBitmap != null) {
            image = new BitmapHandler().bitmapToByteArray(imageBitmap);
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.sadface);
            image = new BitmapHandler().bitmapToByteArray(bitmap);
        }
        ClothingItem clothingItem = new ClothingItem(
                StringHandler.getText(binding.fragmentDetailName),
                image,
                (binding.fragmentDetailColor.getSelectedItemPosition() - 1),
                (binding.fragmentDetailStyle.getSelectedItemPosition() - 1),
                StringHandler.getText(binding.fragmentDetailSize),
                StringHandler.getText(binding.fragmentDetailNote)
        );
        editingId = mViewModel.getDatabase().clothingItemDao().insertClItem(clothingItem);
        //todo make AsyncTask post to fulfill requirements

    }

    /**
     * styles on item selected listener for the Edit Styles Option
     */
    private void setStylesListener() {
        binding.fragmentDetailStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == styles.size() - 1) {
                    try {
                        new EditStylesDialog(ClothesEditDetailFragment.this, mViewModel, dialogTheme);
                        isDialogOpened = true;
                    } catch (Exception ignored) {
                    }
                    binding.fragmentDetailStyle.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(DIALOG_OPENED, isDialogOpened);
        outState.putLong(EDITING_ID, editingId);
    }

    /**
     * set boolean when dialog is closed
     */
    public void dialogClosed() {
        isDialogOpened = false;
    }

    /**
     * fill info if editing existing item
     */
    private void fillUI() {
        if (editingId > -1) {
            item = mViewModel.database.clothingItemDao().getClItemById(editingId);
            binding.fragmentDetailName.setText(item.getName());
            imageBitmap = new BitmapHandler().byteArrayToBitmap(item.getImage());
            binding.fragmentDetailImage.setImageBitmap(imageBitmap);
            binding.fragmentDetailColor.setSelection(item.getColor(), true);
            binding.fragmentDetailStyle.setSelection(item.getStyle() + 1, true);
            binding.fragmentDetailSize.setText(item.getSize());
            binding.fragmentDetailNote.setText(item.getNote());
        }
    }

}
