package com.zealsoft.pharmed.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.zealsoft.pharmed.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MedicineListViewHolder extends RecyclerView.ViewHolder {

    public ConstraintLayout medicineItem;
    public CircleImageView image;
    public TextView name, type, size, details, dose, quantity;
    public ImageView addToCart, increaseQuantity, decreaseQuantity;
    public ProgressBar loading;

    public MedicineListViewHolder(View itemView) {
        super(itemView);
        medicineItem = itemView.findViewById(R.id.medicine_item);
        image = itemView.findViewById(R.id.medicine_pic);
        addToCart = itemView.findViewById(R.id.add_cart);
        name = itemView.findViewById(R.id.medicine_name);
        type = itemView.findViewById(R.id.type);
        size = itemView.findViewById(R.id.pack_size);
        details = itemView.findViewById(R.id.medicine_details);
        dose = itemView.findViewById(R.id.medicine_dose);
        quantity = itemView.findViewById(R.id.quantity);
        increaseQuantity = itemView.findViewById(R.id.increase_quantity);
        decreaseQuantity = itemView.findViewById(R.id.decrease_quantity);
        loading = itemView.findViewById(R.id.loading);
    }
}
