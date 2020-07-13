package com.zealsoft.pharmed.viewHolders

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.zealsoft.pharmed.R
import de.hdodenhof.circleimageview.CircleImageView

class OrderUserListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var container: CardView = itemView.findViewById(R.id.container)
    var orderItem: ConstraintLayout = itemView.findViewById(R.id.order_item)
    var orderStatusColor: View = itemView.findViewById(R.id.order_status_color)
    var icon: CircleImageView = itemView.findViewById(R.id.icon)
    var initials: TextView = itemView.findViewById(R.id.initials)
    var dateTime: TextView = itemView.findViewById(R.id.date_time)
    var time: TextView = itemView.findViewById(R.id.time)
    var idLabel: TextView = itemView.findViewById(R.id.id_label)
    var orderId: TextView = itemView.findViewById(R.id.order_id)
    var countLabel: TextView = itemView.findViewById(R.id.items_label)
    var itemsCount: TextView = itemView.findViewById(R.id.items_count)
    var totalLabel: TextView = itemView.findViewById(R.id.total_label)
    var totalAmount: TextView = itemView.findViewById(R.id.total_amount)
    var pharmacyName: TextView = itemView.findViewById(R.id.pharmacy_name)
    var pharmacyAddress: TextView = itemView.findViewById(R.id.pharmacy_address)
    var orderStatus: TextView = itemView.findViewById(R.id.order_status)
    var accept: Button = itemView.findViewById(R.id.accept)
    var cancel: Button = itemView.findViewById(R.id.cancel)
    var viewDetails: TextView = itemView.findViewById(R.id.details)
    var pendingPoint: CircleImageView = itemView.findViewById(R.id.pending_state)
    var processPoint: CircleImageView = itemView.findViewById(R.id.process_state)
    var completePoint: CircleImageView = itemView.findViewById(R.id.complete_state)
    var orderStatusBar: ProgressBar = itemView.findViewById(R.id.order_status_bar)
}