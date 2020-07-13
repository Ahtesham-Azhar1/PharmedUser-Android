package com.zealsoft.pharmed.viewHolders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.recyclerview.widget.RecyclerView;

import com.zealsoft.pharmed.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CartListViewHolder extends RecyclerView.ViewHolder {

    public CardView cartCard;
    public ConstraintLayout cartItem;
    public CheckBox deleteCB;
    public CircleImageView image;
    public TextView name, type, size, details, dose, quantity, quantityLabel;
    public ImageView removeItem, increaseQuantity, decreaseQuantity;
    public Guideline startGuidline;
    public ProgressBar progressBar;

    public CartListViewHolder(View itemView) {
        super(itemView);
        cartCard = itemView.findViewById(R.id.container);
        cartItem = itemView.findViewById(R.id.cart_item);
        deleteCB = itemView.findViewById(R.id.delete_check);
        image = itemView.findViewById(R.id.medicine_pic);
        removeItem = itemView.findViewById(R.id.remove_item);
        name = itemView.findViewById(R.id.medicine_name);
        type = itemView.findViewById(R.id.type);
        size = itemView.findViewById(R.id.pack_size);
        details = itemView.findViewById(R.id.medicine_details);
        dose = itemView.findViewById(R.id.medicine_dose);
        quantity = itemView.findViewById(R.id.quantity);
        quantityLabel = itemView.findViewById(R.id.quantity_label);
        increaseQuantity = itemView.findViewById(R.id.increase_quantity);
        decreaseQuantity = itemView.findViewById(R.id.decrease_quantity);
        progressBar = itemView.findViewById(R.id.loading_progress);
        startGuidline = itemView.findViewById(R.id.image_margin_guideline);
    }
}
