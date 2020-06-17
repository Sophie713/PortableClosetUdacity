package com.sophie.miller.portablecloset.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.sophie.miller.portablecloset.MainActivity;
import com.sophie.miller.portablecloset.R;
import com.sophie.miller.portablecloset.objects.ClothingItem;
import com.sophie.miller.portablecloset.utils.BitmapHandler;
import com.sophie.miller.portablecloset.viewModel.MainViewModel;
import com.sophie.miller.portablecloset.viewmodels.HolderClItem;

import java.util.ArrayList;
import java.util.List;

public class ClothesAdapter extends RecyclerView.Adapter<HolderClItem> {

    private ArrayList<ClothingItem> filteredClothes = new ArrayList<>();
    private Context context;
    private BitmapHandler bitmapHandler = new BitmapHandler();

    public ClothesAdapter(final ArrayList<ClothingItem> filteredClothes, Context context) {
        this.filteredClothes.clear();
        this.filteredClothes.addAll(filteredClothes);
        this.context = context;
    }

    @NonNull
    @Override
    public HolderClItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        HolderClItem clothingItem = new HolderClItem(view);
        return clothingItem;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderClItem holder, int position) {
        final int positionF = position;
        final ClothingItem item = filteredClothes.get(position);
        holder.image.setImageBitmap(bitmapHandler.byteArrayToBitmap(item.getImage()));
        holder.title.setText(item.getName());
        //display photo or placeholder
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "id: " + item.getId(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return filteredClothes.size();
    }

    public void insertNewData(ArrayList<ClothingItem> filteredClothes) {
        this.filteredClothes = filteredClothes;
    }
}