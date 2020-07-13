package com.zealsoft.pharmed.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.MediaStoreSignature
import com.zealsoft.pharmed.models.MedicineDetails
import com.zealsoft.pharmed.models.Prescription
import com.zealsoft.pharmed.view_holders.CartPrescriptionViewHolder
import com.zealsoft.pharmed.view_holders.OrderItemsListViewHolder
import com.zealsoft.pharmed.R
import com.zealsoft.pharmed.Util.Constants
import com.zealsoft.pharmed.Util.Utills
import com.zealsoft.pharmed.activities.MedicineDetailsKActivity
import com.zealsoft.pharmed.activities.PlaceOrderActivity
import com.zealsoft.pharmed.activities.ViewImageActivity
import java.util.*


class OrderItemsListKAdapter  (val activity : Activity, val list : List<MedicineDetails>, val listPre: List<Prescription>, val nightMode: Boolean,
                               private val showDetails: Boolean, val status: String, val currency: String, val url: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEW_TYPE_MEDICINE = 0
    val VIEW_TYPE_PRESCRIPTION = 1

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {

        if(i == VIEW_TYPE_MEDICINE) {
            val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.single_order_items_list_tile, viewGroup, false)
            return OrderItemsListViewHolder(v)
        } else {
            val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.single_cart_prescription_item, viewGroup, false)
            return CartPrescriptionViewHolder(v)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is OrderItemsListViewHolder) {

            val item = list[position]

            if (nightMode) {
                holder.orderItemsTile.setBackgroundResource(R.color.editTextBackgroundNightMode)
                holder.name.setTextColor(activity.resources.getColor(R.color.white))
                holder.type.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
                holder.size.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
                holder.details.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
                holder.dose.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
                holder.quantity.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
                holder.amount.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
                holder.available.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
            }

//            if(activity is PlaceOrderActivity){
//                holder.orderItemCard.cardElevation = 0F
//                if(nightMode){
//                    holder.orderItemsTile.setBackgroundResource(R.color.editTextBackgroundNightMode)
//                } else {
//
//                }
//            }

            if(item.available == null)
                item.available = true

            holder.available.setButtonDrawable(R.drawable.checkbox_selector_colors)
            holder.available.isChecked = item.available

            setAvailable(holder.available.isChecked, holder)

            holder.orderItemsTile.setOnClickListener {
                if (showDetails) {
//                    (activity as SearchKActivity).showMedicineDetails(item, true)
                } else {

                    val details = Intent(activity, MedicineDetailsKActivity::class.java)
                    details.putExtra(Constants.INTENT_MEDICINE_DETAILS, item)
                    details.putExtra(Constants.INTENT_ONLY_DETAILS, true)
                    activity.startActivity(details)
                }
            }


            holder.name.text = (position + 1).toString() + ". " + item.medicineName

            if(item.quantity != null && item.quantity > 0) {
                if (item.type != null && item.type != "") {
                    holder.quantity.text = "Quantity:\n" + item.quantity.toString() + " " + item.type
//                    holder.type.text = "Type:\n" + item.type
                } else {
                    holder.quantity.text = "Quantity:\n" + item.quantity.toString()
//                    holder.type.text = "Type:\n-"
                }
            }

//        holder.size.text = Utills.getTypeInfo(item)

            if (item.dosage != null && item.dosage != "" && item.dosage != "Potency:  " && item.dosage != "Volume:  ") {
                var dose = ""
                dose = item.dosage.substring(0, item.dosage.indexOf(":")+ 1).trim()
                dose += "\n" + item.dosage.substring(item.dosage.indexOf(":")+1).trim()
                holder.size.text = dose
            } else {
                var dose = ""
                dose = Constants.DEFAULT_DOSAGE.split(":")[0] + "\n" + Constants.DEFAULT_DOSAGE.split(":")[1].trim()
                holder.size.text = dose
            }

            if (item.ingredients != null) {
                holder.dose.text = item.ingredients
            }

            if (item.manufacturer != null && item.manufacturer != "") {
                holder.details.text = item.manufacturer
                holder.details.visibility = View.VISIBLE
            } else {
                holder.details.visibility = View.GONE
            }

        }

        //------------------------------------------------------------------------------------------
        // Prescription Items

        else if(holder is CartPrescriptionViewHolder) {
            val itemP = listPre[position - list.size]

            holder.deleteCB.visibility = View.GONE

            if (nightMode) {
                holder.cartItem.setBackgroundResource(R.color.editTextBackgroundNightMode)
                holder.name.setTextColor(activity.resources.getColor(R.color.white))
                holder.itemNo.setTextColor(activity.resources.getColor(R.color.white))
            }

            if(activity is PlaceOrderActivity) {
                holder.name.visibility = View.GONE
            }

            holder.available.setButtonDrawable(R.drawable.checkbox_selector_colors)
            holder.available.isChecked = itemP.available

            setAvailable(holder.available.isChecked, holder)


            holder.cartItem.setOnClickListener {
                val viewImage = Intent(activity, ViewImageActivity::class.java)
                viewImage.putExtra("imageUrl", itemP.path)
                if(url)
                    viewImage.putExtra("type", Constants.IMAGE_PATH_PRESCRIPTION)
                else
                    viewImage.putExtra("type", Constants.PRESCRIPTION_IMAGE)
                activity.startActivity(viewImage)
            }

            holder.itemNo.text = (position + 1).toString() + "."
            holder.itemNo.visibility = View.VISIBLE
            holder.name.text = "Attachment"
            holder.name.visibility = View.GONE


            if(itemP?.path != null && !itemP.path.equals("")) {

                val options = RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.icon_prescription)
                        .error(R.drawable.icon_prescription)
                        .priority(Priority.HIGH)

                holder.progress.max = 100
                holder.progress.visibility = View.VISIBLE

                var path = itemP.path

                if(url)
                    path = Utills.getCompleteUrl(itemP.path)

//                GlideImageLoader(holder.image, holder
//                        .progress).load(path,options)

                Glide
                        .with(activity)
                        .load(path)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .signature(MediaStoreSignature("", Calendar.getInstance().time.time, 0))
                        .placeholder(R.drawable.icon_prescription)
                        .error(R.drawable.icon_prescription)
                        .into(holder.image)
            } else {
                holder.image.setImageResource(R.color.grey)
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size + listPre.size
    }

    override fun getItemViewType(position: Int): Int {

        if(position < list.size) {
            return VIEW_TYPE_MEDICINE
        }

        if(position - list.size < listPre.size) {
            return VIEW_TYPE_PRESCRIPTION
        }

        return -1
    }

    private fun setAvailable(available: Boolean, holder: RecyclerView.ViewHolder) {

        if(holder is OrderItemsListViewHolder) {

            if (available) {
                if (nightMode)
                    holder.notAvailable.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
                else
                    holder.notAvailable.setTextColor(activity.resources.getColor(R.color.grey))

                holder.notAvailable.text = "Available"
            } else {
                holder.notAvailable.setTextColor(activity.resources.getColor(R.color.red))
                holder.notAvailable.text = "Available"
            }
        }

        if(holder is CartPrescriptionViewHolder) {

            if (available) {
                if (nightMode)
                    holder.notAvailable.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
                else
                    holder.notAvailable.setTextColor(activity.resources.getColor(R.color.grey))

                holder.notAvailable.text = "Available"
            } else {
                holder.notAvailable.setTextColor(activity.resources.getColor(R.color.red))
                holder.notAvailable.text = "Available"
            }
        }
    }
}