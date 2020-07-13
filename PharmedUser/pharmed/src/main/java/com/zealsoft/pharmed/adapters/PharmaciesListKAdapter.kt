package com.zealsoft.pharmed.adapters

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.signature.MediaStoreSignature
import com.zealsoft.pharmed.R
import com.zealsoft.pharmed.Util.Constants
import com.zealsoft.pharmed.Util.Preferences
import com.zealsoft.pharmed.Util.Utills
import com.zealsoft.pharmed.activities.*
import com.zealsoft.pharmed.customWidgets.CustomDialog
import com.zealsoft.pharmed.models.EventPerformed
import com.zealsoft.pharmed.models.PharmacyDetails
import com.zealsoft.pharmed.viewHolders.NearByListViewHolder
import com.zealsoft.pharmed.view_holders.OthersListViewHolder
import java.util.*

class PharmaciesListKAdapter(val activity : Activity, val items : List<PharmacyDetails>, val nightMode : Boolean, val sendCart : Boolean,
                             val showDetails: Boolean, val performSelect: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var eventPerformed = EventPerformed()
    private var calendar = Calendar.getInstance()
    private var phone : String = ""
    private var customLoading = CustomDialog(activity)
    private var registeredPha = false
    private val VIEW_TYPE_PHARMACY = 0
    private val VIEW_TYPE_OTHERS = 1

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): RecyclerView.ViewHolder {

//        return NearByListViewHolder(LayoutInflater.from(activity).inflate(R.layout.single_nearby_list_item, p0, false))

        if(p1 == VIEW_TYPE_PHARMACY) {
            val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.single_nearby_list_item, viewGroup, false)
            return NearByListViewHolder(v)
        } else {
            val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.single_others_list_item, viewGroup, false)
            return OthersListViewHolder(v)
        }
    }


    override fun getItemCount(): Int {

//        return if (activity is RegisterPharmacyKActivity)
//            items.size + 1
//        else
            return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(position < items.size){
            VIEW_TYPE_PHARMACY
        } else {
            VIEW_TYPE_OTHERS
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, p1: Int) {

        if(holder is NearByListViewHolder) {
            val item: PharmacyDetails = items[p1]
//        if (position.length() > 0) {
//            if (i == Integer.parseInt(position)) {
//                holder.background.setBackgroundColor(activity.getResources().getColor(R.color.green));
//            }
//        }

            if (nightMode) {
                holder.pharmacyItem.setBackgroundResource(R.color.editTextBackgroundNightMode)
                holder.title.setTextColor(activity.resources.getColor(R.color.white))
                holder.address.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
                holder.number.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
                holder.sendCart.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
                holder.sendCart.setBackgroundResource(R.drawable.pd_background_button_dark)
            }

            if (sendCart) {
//                holder.iv_call.visibility = View.GONE
//                holder.iv_msg.visibility = View.GONE
//                holder.iv_direction.visibility = View.GONE
//                holder.iv_share.visibility = View.GONE
//                holder.iv_cart.visibility = View.GONE
                holder.actionBar.visibility = View.GONE
                holder.number.visibility = View.VISIBLE
                holder.sendCart.visibility = View.VISIBLE
            } else {
                holder.actionBar.visibility = View.VISIBLE
                if (Preferences.getPharmacyDataFromSharedPreferences(activity) != null) {
                    holder.iv_cart.visibility = View.GONE
                    holder.iv_promotions.visibility = View.GONE
                } else {
                    holder.iv_cart.visibility = View.VISIBLE
                    holder.iv_promotions.visibility = View.VISIBLE
                }
            }

            if (performSelect) {
                holder.actionBar.visibility = View.GONE
            }

            var registered = false

            registered = item.pharmacyStatus != null || item.pharmacyStatus == Constants.PHARMACY_STATUS_REGISTERED

            eventPerformed.pharmacyId = item.id
            eventPerformed.placeId = item.placeId
            eventPerformed.setPlaceLat(item.placeLat)
            eventPerformed.setPlaceLng(item.placeLng)
            if (Preferences.getDeviceIdFromSharedPreferences(activity) != null &&
                    Preferences.getDeviceIdFromSharedPreferences(activity) != null &&
                    Preferences.getDeviceIdFromSharedPreferences(activity) != "") {
                eventPerformed.deviceId = Preferences.getDeviceIdFromSharedPreferences(activity)
            }

            if (item.placeName != null) {
                holder.logo.setImageResource(R.color.initials_background_color)
                holder.initials.visibility = View.VISIBLE
                holder.initials.text = Utills.extractInitialsPharmacy(item.placeName)
                holder.logo.borderWidth = Constants.BORDER_WIDTH
                holder.logo.borderColor = activity.resources.getColor(R.color.initials_color)
            }

            if (item.icon != null && item.icon != "") {

                //                    if (image != null) {
                Glide
                        .with(activity)
                        .load(Utills.getCompleteUrl(item.icon))
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, isFirstResource: Boolean): Boolean {

                                return false
                            }

                            override fun onResourceReady(resource: Drawable?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                                holder.initials.visibility = View.GONE
                                holder.logo.borderWidth = 0
                                return false
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.color.initials_background_color)
                        .signature(MediaStoreSignature("", calendar.time.time, 0))
                        .error(R.color.initials_background_color)
                        .into(holder.logo)
            } else {
//            holder.logo.setImageResource(R.drawable.icon_pharmacy_general)
            }

            holder.title.text = item.placeName
            holder.address.text = item.placeAddress

            if(item.deliveryAvailable != null && item.deliveryAvailable) {
                if (item.minOrderLimit != null && item.minOrderLimit!! > 0) {
                    holder.deliveryLimit.visibility = View.VISIBLE
                    holder.deliveryLimit.text = "Min. Order:\n" + Utills.getCurrencyCode(item.currencySymbol) + " " + item.minOrderLimit.toString()
                } else {
                    holder.deliveryLimit.visibility = View.GONE
                }
            } else {
                holder.deliveryLimit.visibility = View.GONE
            }

//        getAddress(Double.parseDouble(item.getPlaceLat()), Double.parseDouble(item.getPlaceLng()), holder.address);

            holder.pharmacyItem.setOnClickListener{
                if (showDetails) {
                    if (activity is SearchKActivity) {
                        (activity).showPharmacyDetails(item, true)
                    }
                } else {
                    if (performSelect) {

                    } else {
                        val intent = Intent(activity, PharmacyDetailsKActivity::class.java)
                        intent.putExtra("pharmacy", item)
                        if(sendCart)
                            intent.putExtra(Constants.INTENT_SELECT_PHARMACY, true)
                        activity.startActivityForResult(intent, Constants.INT_CODE_SELECT_PHARMACY)
                    }
                }
            }

            holder.iv_call.setOnClickListener {
                if (item.placeNumber != null && item.placeNumber != "") {
                    phone = item.placeNumber!!
                    eventPerformed.eventType = Constants.EVENT_CALL
                    eventsCall(eventPerformed)
                    Utills.makeCall(activity, phone)
                }
            }

            holder.iv_msg.setOnClickListener {
                if (item.placeNumber != null && item.placeNumber != "") {
                    phone = item.placeNumber!!
                    eventPerformed.eventType = Constants.EVENT_SMS
                    if (Utills.sendSms(activity, phone, "")) {
                        eventsCall(eventPerformed)
                    }
                }
            }


            holder.iv_direction.setOnClickListener {
                eventPerformed.eventType = Constants.EVENT_DIRECTION
                eventsCall(eventPerformed)
                Utills.getDirections(activity, item.placeLat, item.placeLng)
            }

            holder.iv_share.setOnClickListener {
                eventPerformed.eventType = Constants.EVENT_SHARE
                eventsCall(eventPerformed)
                Utills.shareLocation(activity, item.placeName, item.placeAddress, item.placeNumber, item.placeLat,
                        item.placeLng)
            }

            holder.iv_cart.setOnClickListener{
                Preferences.addSelectedPharmacyToSharedPreferences(activity, item)
//                val goToCart = Intent(activity, CartKActivity::class.java)
//                goToCart.putExtra(Constants.INTENT_SELECTED_PHARMACY, item)
                activity.startActivity(Intent(activity, CartKActivity::class.java))
            }

            holder.sendCart.setOnClickListener {
                if (item.placeNumber != null) {

                    if(activity is SearchKActivity)
                        activity.selectPharmacy(item)

//                    phone = item.placeNumber!!
////
//                    if (!registered) {
//                        if(activity is SearchKActivity)
//                            activity.showSmsDialog(item.id!!, phone)
//                    } else {
//                        if (Preferences.getUserDataFromSharedPreferences(activity) != null) {
////                            customLoading.showLoadingDialogue()
//                            if(activity is SearchKActivity)
//                                activity.showConfirmOrderDialog(item, item.deliveryAvailable)
//                        } else {
//
//                            val location = LatLng(item.placeLat!!.toDouble(), item.placeLng!!.toDouble())
//                            if (activity is SearchKActivity) {
//                                if (Utills.isValidLandlineNumber(activity, location, item.placeNumber)) {
//                                    activity.showLoginDialog(false)
//                                } else {
//                                    activity.showLoginDialog(true)
//                                }
//                            }
////                        activity.showToast(Constants.PLACE_ORDER_REGISTER)
//                        }
//
//                    }

                    registeredPha = registered
                }
            }

            holder.iv_promotions.setOnClickListener {
//                    val i = Intent(activity, PromotionsListActivity::class.java)
//                    i.putExtra(Constants.INTENT_PHARMACY_ID, item.id)
//                    activity.startActivity(i)
            }

            if(holder.actionBar.visibility == View.VISIBLE) {
                    if (item.placeNumber != null) {
//                        val location = LatLng(item.placeLat!!.toDouble(), item.placeLng!!.toDouble())

//                        var landLine = true

//                        if(!item.identifiedNumber) {
//                            landLine = Utills.isValidLandlineNumber(activity, location, item.placeNumber)
//                            items[p1].landLineNumber = landLine
//                        } else {
//                            landLine = item.landLineNumber
//                        }

                        if (item.phoneNumberType == Constants.NUMBER_TYPE_MOBILE) {
                            holder.iv_call.isEnabled = true
                            holder.iv_msg.isEnabled = true
                            holder.iv_cart.isEnabled = true
                            holder.iv_call.clearColorFilter()
                            holder.iv_msg.clearColorFilter()
                            holder.iv_cart.clearColorFilter()
                            holder.sendCart.setBackgroundResource(R.drawable.pd_background_button_dark)
                        } else {
                            holder.iv_msg.isEnabled = false
                            holder.iv_call.isEnabled = true
                            holder.iv_cart.isEnabled = false
                            holder.iv_msg.setColorFilter(activity.resources.getColor(R.color.grey))
                            holder.iv_call.clearColorFilter()
                            holder.iv_cart.setColorFilter(activity.resources.getColor(R.color.grey))
                            holder.sendCart.setBackgroundResource(R.drawable.pd_background_button_disabled_night)
                        }

                        holder.number.text = item.placeNumber

                    } else {
                        holder.iv_call.isEnabled = false
                        holder.iv_msg.isEnabled = false
                        holder.iv_cart.isEnabled = false
                        holder.iv_call.setColorFilter(activity.resources.getColor(R.color.grey))
                        holder.iv_msg.setColorFilter(activity.resources.getColor(R.color.grey))
                        holder.iv_cart.setColorFilter(activity.resources.getColor(R.color.grey))
                        holder.sendCart.setBackgroundResource(R.drawable.pd_background_button_disabled_night)

                        holder.number.visibility = View.GONE
                    }
//                    items[p1].identifiedNumber = true
            } else {
                if (item.placeNumber != null) {
                    holder.number.text = item.placeNumber
                    holder.number.visibility = View.VISIBLE
                } else {
                    holder.number.visibility = View.GONE
                }
            }

            if(item.pharmacyStatus == Constants.PHARMACY_STATUS_REGISTERED){
                holder.iv_cart.clearColorFilter()
                holder.iv_cart.isEnabled = true
                holder.sendCart.setBackgroundResource(R.drawable.pd_background_button_dark)
            } else {

            }

            if(item.promotionAvailable) {
                holder.iv_promotions.clearColorFilter()
                holder.iv_promotions.isEnabled = true
            } else {
                holder.iv_promotions.setColorFilter(activity.resources.getColor(R.color.grey))
                holder.iv_promotions.isEnabled = false
            }
        } else if (holder is OthersListViewHolder){

            if(nightMode){
                holder.others.setBackgroundResource(R.color.editTextBackgroundNightMode)
                holder.others.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
            }

            holder.others.setOnClickListener {
//                if (activity is RegisterPharmacyKActivity)
//                    activity.removeSelectedPharmacy()
            }
        }
    }

    private fun eventsCall(performedEvent: EventPerformed) {
//        if (Internet.isAvailable(activity)) {
//            //            Loading.show(activity, false, "Please wait...");
//            RestCaller(this, PharmaD.getRestClient().eventsCall(performedEvent), 1)
//        } else{
//
//        }
    }

}