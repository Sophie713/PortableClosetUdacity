package com.sophie.miller.portablecloset.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sophie.miller.portablecloset.MainActivity;
import com.sophie.miller.portablecloset.R;
import com.sophie.miller.portablecloset.constants.FragmentCodes;
import com.sophie.miller.portablecloset.objects.ClothingItem;
import com.sophie.miller.portablecloset.utils.BitmapHandler;
import com.sophie.miller.portablecloset.viewholders.HolderClItem;

import java.util.ArrayList;

public class ClothesAdapter extends RecyclerView.Adapter<HolderClItem> {

    private ArrayList<ClothingItem> filteredClothes = new ArrayList<>();
    private MainActivity activity;
    private BitmapHandler bitmapHandler = new BitmapHandler();

    public ClothesAdapter(final ArrayList<ClothingItem> filteredClothes, MainActivity activity) {
        this.filteredClothes.clear();
        this.filteredClothes.addAll(filteredClothes);
        this.activity = activity;
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
        final ClothingItem item = filteredClothes.get(position);
        holder.image.setImageBitmap(bitmapHandler.byteArrayToBitmap(item.getImage()));
        holder.title.setText(item.getName());
        //display photo or placeholder
        holder.card.setOnClickListener(v -> {
            activity.setDetailId(item.getId());
            activity.setFragment(FragmentCodes.DETAIL_INFO_FRAGMENT);
        });
    }


    @Override
    public int getItemCount() {
        if (filteredClothes != null) return filteredClothes.size();
        else return 0;
    }

    public void insertNewData(ArrayList<ClothingItem> filteredClothes) {
        this.filteredClothes = filteredClothes;
    }
}
