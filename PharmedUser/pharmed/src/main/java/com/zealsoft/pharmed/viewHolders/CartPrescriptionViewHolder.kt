package com.zealsoft.pharmed.view_holders

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.zealsoft.pharmed.R

class CartPrescriptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {

    var cartItem: ConstraintLayout = itemView.findViewById(R.id.cart_item)
    var deleteCB: CheckBox = itemView.findViewById(R.id.delete_check)
    var itemNo: TextView = itemView.findViewById(R.id.item_no)
    var image: ImageView = itemView.findViewById(R.id.prescription_image)
    var name: TextView = itemView.findViewById(R.id.prescription_label)
    var progress: ProgressBar = itemView.findViewById(R.id.progress)
    var discount: TextView = itemView.findViewById(R.id.discount)
    var amount: TextView = itemView.findViewById(R.id.total_amount)
    var notAvailable: TextView = itemView.findViewById(R.id.not_available)
    var available: CheckBox = itemView.findViewById(R.id.available)
//    var viewPrescription = itemView.findViewById(R.id.)
}