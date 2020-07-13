package com.zealsoft.pharmed.view_holders

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.zealsoft.pharmed.R

class OrderItemsListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var orderItemCard: CardView = itemView.findViewById(R.id.container)
    var orderItemsTile: ConstraintLayout = itemView.findViewById(R.id.order_items_tile)
    var image: ImageView = itemView.findViewById(R.id.medicine_pic)
    var name: TextView = itemView.findViewById(R.id.medicine_name)
    var type: TextView = itemView.findViewById(R.id.type)
    var size: TextView = itemView.findViewById(R.id.pack_size)
    var details: TextView = itemView.findViewById(R.id.medicine_details)
    var dose: TextView = itemView.findViewById(R.id.medicine_dose)
    var quantity: TextView = itemView.findViewById(R.id.quantity)
    var discount: TextView = itemView.findViewById(R.id.discount)
    var amount: TextView = itemView.findViewById(R.id.price)
    var notAvailable: TextView = itemView.findViewById(R.id.not_available)
    var available: CheckBox = itemView.findViewById(R.id.available)
}