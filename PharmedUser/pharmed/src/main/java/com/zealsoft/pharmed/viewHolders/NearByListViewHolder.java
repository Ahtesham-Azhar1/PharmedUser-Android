package com.zealsoft.pharmed.viewHolders;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zealsoft.pharmed.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by farazqureshi on 03/06/2018.
 */

public class NearByListViewHolder extends RecyclerView.ViewHolder {

    public CardView container;
    public ConstraintLayout pharmacyItem;
    public TextView initials, title, address, number, sendCart, deliveryLimit;
    public View background, actionBar;
    public ImageView iv_call, iv_msg, iv_direction, iv_share, iv_cart, iv_promotions;
    public CircleImageView logo;

    public NearByListViewHolder(View itemView) {
        super(itemView);
        container = itemView.findViewById(R.id.container);
        pharmacyItem = itemView.findViewById(R.id.pharmacy_item);
        logo = itemView.findViewById(R.id.icon);
        initials = itemView.findViewById(R.id.initials);
        background = itemView.findViewById(R.id.background);
        title = itemView.findViewById(R.id.address_title);
        address = itemView.findViewById(R.id.address_name);
        actionBar = itemView.findViewById(R.id.action_bar);
        iv_msg = itemView.findViewById(R.id.msg);
        iv_call = itemView.findViewById(R.id.iv_call);
        iv_direction = itemView.findViewById(R.id.iv_directions);
        iv_share = itemView.findViewById(R.id.iv_share);
        iv_cart = itemView.findViewById(R.id.iv_cart);
        iv_promotions = itemView.findViewById(R.id.iv_promotions);
        number = itemView.findViewById(R.id.number);
        sendCart = itemView.findViewById(R.id.send_cart);
        deliveryLimit = itemView.findViewById(R.id.delivery_limit);
    }
}