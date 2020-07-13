package com.zealsoft.pharmed.adapters

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.MediaStoreSignature
import com.zealsoft.pharmed.R
import com.zealsoft.pharmed.Util.Constants
import com.zealsoft.pharmed.Util.Utills
import com.zealsoft.pharmed.activities.*
import com.zealsoft.pharmed.models.Order
import com.zealsoft.pharmed.viewHolders.OrderUserListViewHolder
import java.util.*

class OrdersListUserKAdapter (val activity : Activity, val list : List<Order>, val nightMode: Boolean,
                              private val showDetails: Boolean) : androidx.recyclerview.widget.RecyclerView.Adapter<OrderUserListViewHolder>() {

    private var calendar = Calendar.getInstance()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): OrderUserListViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.single_order_user_list_item, viewGroup, false)
        return OrderUserListViewHolder(v)
    }

    override fun onBindViewHolder(holder: OrderUserListViewHolder, position: Int) {

        val item = list[position]

        var textColor = activity.resources.getColor(R.color.black)

        if (nightMode) {
            holder.orderItem.setBackgroundResource(R.color.editTextBackgroundNightMode)
            holder.dateTime.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
            holder.time.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
            holder.idLabel.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
            holder.orderId.setTextColor(activity.resources.getColor(R.color.white))
            holder.countLabel.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
            holder.itemsCount.setTextColor(activity.resources.getColor(R.color.white))
            holder.totalLabel.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
            holder.totalAmount.setTextColor(activity.resources.getColor(R.color.white))
            holder.pharmacyName.setTextColor(activity.resources.getColor(R.color.white))
            holder.pharmacyAddress.setTextColor(activity.resources.getColor(R.color.white))
            holder.orderStatus.setTextColor(activity.resources.getColor(R.color.white))
            holder.orderStatus.setBackgroundResource(R.drawable.pd_background_et_round_night_mode)
            textColor = activity.resources.getColor(R.color.white)
//            holder.viewDetails.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
//            holder.viewDetails.setBackgroundResource(R.drawable.background_et_round_night_mode)
        }

//        if(item.orderStatus == Constants.STATUS_CONFIRMATION){
//            holder.accept.visibility = View.VISIBLE
//            holder.cancel.visibility = View.VISIBLE
//        } else {
//            holder.accept.visibility = View.GONE
//            holder.cancel.visibility = View.GONE
//        }

        holder.orderStatus.isSelected = true

        holder.dateTime.text = Utills.getTimeStamp(item.dateTime, Constants.OUR_DATE_FORMAT)
        holder.time.text = Utills.getTimeStamp(item.dateTime, Constants.OUR_TIME_FORMAT)

        if(item.pharmacy != null) {
            if (item.pharmacy?.placeName != null) {
                holder.pharmacyName.text = item.pharmacy?.placeName
                holder.icon.setImageResource(R.color.initials_background_color)
                holder.initials.visibility = View.VISIBLE
                holder.initials.text = Utills.extractInitialsPharmacy(item.pharmacy?.placeName)
                holder.icon.borderWidth = Constants.BORDER_WIDTH
                holder.icon.borderColor = activity.resources.getColor(R.color.initials_color)
            }

            if (item.pharmacy?.icon != "") {

                val options = RequestOptions()
                        .centerCrop()
                //.placeholder(R.drawable.placeholder)
                //.error(R.drawable.error)
                //.priority(Priority.HIGH);

                //                    if (image != null) {
                Glide
                        .with(activity)
                        .load(Utills.getCompleteUrl(item.pharmacy?.icon))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, isFirstResource: Boolean): Boolean {

                                return false
                            }

                            override fun onResourceReady(resource: Drawable?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                                holder.initials.visibility = View.GONE
                                holder.icon.borderWidth = 0
                                return false
                            }
                        })
                        .signature(MediaStoreSignature("", calendar.time.time, 0))
                        .placeholder(R.color.initials_background_color)
                        .error(R.color.initials_background_color)
                        .into(holder.icon)
                //
            } else {
//            holder.icon.setImageResource(R.drawable.icon_pharmacy_general)
            }

            if (item.pharmacy?.placeAddress != null) {
                holder.pharmacyAddress.text = item.pharmacy?.placeAddress
            }

        }

        if (item.checkoutId != null) {
            holder.orderId.text = item.checkoutId
        } else {

        }

        if (item.itemsCount != null) {
            holder.itemsCount.text = item.itemsCount.toString()
        }

        if(item.orderStatus != null){
            holder.orderStatus.text = Utills.updateStatus(activity, item.orderStatus, holder.orderStatus, holder.orderStatusColor, nightMode)
            Utills.updateOrderButtonsUser(activity, item.orderType, item.orderStatus, null, holder.accept, holder.cancel)
        }

        if(item.totalAmount != null) {

            var total = item.totalAmount
            if(item.deliveryCharges != null && item.deliveryCharges!! > 0){
                total += item.deliveryCharges!!
            }
            holder.totalAmount.text = Utills.getCurrencyCode(item.pharmacy?.currencySymbol) + " " + total
            holder.totalAmount.visibility = View.VISIBLE
            holder.totalLabel.visibility = View.VISIBLE
        } else {
            holder.totalAmount.visibility = View.GONE
            holder.totalLabel.visibility = View.GONE
        }

//        when {
//            item.orderStatus.equals(Constants.STATUS_COMPLETE) ->
//                holder.orderStatus.setTextColor(activity.resources.getColor(R.color.status_complete_color))
//            item.orderStatus.equals(Constants.STATUS_CANCELLED) ->
//                holder.orderStatus.setTextColor(activity.resources.getColor(R.color.status_canceled_color))
//            item.orderStatus.equals(Constants.STATUS_REJECTED) ->
//                holder.orderStatus.setTextColor(activity.resources.getColor(R.color.status_rejected_color))
//            item.orderStatus.equals(Constants.STATUS_OFFLINE) -> {
//                holder.orderStatus.setTextColor(activity.resources.getColor(R.color.status_offline_color))
//                holder.orderStatus.text = Constants.SMS_ORDER
//            }
//            item.orderStatus.equals(Constants.STATUS_IN_PROGRESS) -> {
//                holder.orderStatus.setTextColor(activity.resources.getColor(R.color.status_in_progress_color))
//                holder.orderStatus.text = Constants.IN_PROGRESS
//            }
//            item.orderStatus.equals(Constants.STATUS_READY) -> {
//                holder.orderStatus.setTextColor(activity.resources.getColor(R.color.status_ready_color))
//                holder.orderStatus.text = Constants.PICK_UP
//            }
//            item.orderStatus.equals(Constants.STATUS_ENROUTE) -> {
//                holder.orderStatus.setTextColor(activity.resources.getColor(R.color.status_enroute_color))
//                holder.orderStatus.text = Constants.PICK_UP
//            }
//            else -> {
//                holder.orderStatus.setTextColor(textColor)
//            }
//        }

//        when {
//            item.orderStatus.equals(Constants.STATUS_IN_PROGRESS) -> {
//                holder.orderStatusBar.progress = 50
//                holder.processPoint.setImageResource(R.color.themeCenterColorL)
//            }
//            item.orderStatus.equals(Constants.STATUS_COMPLETE) -> {
//                holder.orderStatusBar.progress = 0
//                holder.processPoint.setImageResource(R.color.themeCenterColorL)
//                holder.completePoint.setImageResource(R.color.themeGreenColorL)
//            }
//            item.orderStatus.equals(Constants.STATUS_CANCELLED) -> {
//                holder.orderStatusBar.progress = 50
//                holder.processPoint.setImageResource(R.color.red)
//            }
//            item.orderStatus.equals(Constants.STATUS_REJECTED) -> {
//                holder.orderStatusBar.progress = 50
//                holder.processPoint.setImageResource(R.color.red)
//            }
//            item.orderStatus.equals(Constants.STATUS_OFFLINE) -> {
//                holder.pendingPoint.setImageResource(R.color.grey)
//            }
//            else -> {
//
//            }
//        }

        holder.container.setOnClickListener {
                val details = Intent(activity, OrderDetailsUserActivity::class.java)
                details.putExtra(Constants.ORDER_DETAILS, item)
                details.putExtra(Constants.ORDER_POSITION, position)
                activity.startActivityForResult(details, Constants.INTENT_ORDER_DETAILS)
        }

        holder.accept.setOnClickListener {

            if (activity is OrdersUserActivity) {
                if (holder.accept.text == Constants.BUTTON_REORDER) {

                    val reOrder = Intent(activity, PlaceOrderActivity::class.java)
//                    reOrder.putExtra("searchPharmacy", true)
                    reOrder.putExtra(Constants.INTENT_ORDER, Utills.getReOrderObject(item))
//                    Constants.order = Utills.getReOrderObject(item)
                    reOrder.putExtra(Constants.INTENT_RE_ORDER, true)
                    activity.startActivity(reOrder)
                } else {
                    if(item.typeChange && item.orderStatus == Constants.STATUS_CONFIRMATION) {
                        activity.showLimitDialog(item.id!!, position, false)
                    } else if (item.deliveryCharges != null && item.deliveryCharges!! > 0) {
                        activity.showLimitDialog(item.id!!, position, true)
                    } else
                        activity.changeOrderStatusCall(item.id!!, Constants.STATUS_IN_PROGRESS, position, "", false)
                }
            }
        }

        holder.cancel.setOnClickListener {

            if(activity is OrdersUserActivity)
                activity.showCancelOrderDialog(item.id!!, position)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}