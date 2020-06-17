package com.sophie.miller.portablecloset.viewmodels;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sophie.miller.portablecloset.R;

public class HolderClItem  extends RecyclerView.ViewHolder {

    public ImageView image;
    public CardView card;
    public TextView title;

    public HolderClItem(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.item_card_image);
        card = itemView.findViewById(R.id.item_card_card);
        title = itemView.findViewById(R.id.item_card_name);
    }
}