package com.zealsoft.pharmed.adapters

import android.app.Activity
import android.content.Intent
import android.os.Handler
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.MediaStoreSignature
import com.zealsoft.pharmed.R
import com.zealsoft.pharmed.Util.Constants
import com.zealsoft.pharmed.Util.Preferences
import com.zealsoft.pharmed.Util.Utills
import com.zealsoft.pharmed.activities.*
import com.zealsoft.pharmed.models.MedicineDetails
import com.zealsoft.pharmed.models.Prescription
import com.zealsoft.pharmed.viewHolders.CartListViewHolder
import com.zealsoft.pharmed.view_holders.CartPrescriptionViewHolder
import java.util.*


class CartListKAdapter (val activity : Activity, val list : List<MedicineDetails>, val listPre : List<Prescription>, val nightMode: Boolean,
                        private val showDetails: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEW_TYPE_MEDICINE = 0
    val VIEW_TYPE_PRESCRIPTION = 1

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {

        if(i == VIEW_TYPE_MEDICINE) {
            val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.single_cart_item, viewGroup, false)
            return CartListViewHolder(v)
        } else {
            val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.single_cart_prescription_item, viewGroup, false)
            return CartPrescriptionViewHolder(v)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is CartListViewHolder) {

            val item = list[position]

            if (nightMode) {
                holder.cartItem.setBackgroundResource(R.color.editTextBackgroundNightMode)
                holder.name.setTextColor(activity.resources.getColor(R.color.white))
                holder.type.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
                holder.size.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
                holder.details.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
                holder.dose.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
                holder.quantity.setBackgroundResource(R.drawable.pd_background_et_round_night_mode_light)
                holder.quantity.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
                holder.quantityLabel.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
                holder.deleteCB.setButtonDrawable(R.drawable.checkbox_selector_colors)
            }

//        if(activity is OrderDetailsUserActivity || activity is OrderDetailsActivity){
//            holder.increaseQuantity.visibility = View.GONE
//            holder.decreaseQuantity.visibility = View.GONE
//            holder.deleteCB.visibility = View.GONE
//            holder.startGuidline.visibility = View.GONE
//            holder.quantity.visibility = View.GONE
//            holder.quantityLabel.visibility = View.VISIBLE
//        }

            holder.deleteCB.isChecked = item.select

            //        if(item.url != null && !item.url.equals("")){
            //
            //            RequestOptions options = new RequestOptions()
            //                    .centerCrop();
            //            //.placeholder(R.drawable.placeholder)
            //            //.error(R.drawable.error)
            //            //.priority(Priority.HIGH);
            //
            ////                    if (image != null) {
            //            Glide
            //                    .with(activity)
            //                    .load(Utills.getCompleteUrl(item.url))
            //                    .diskCacheStrategy(DiskCacheStrategy.NONE)
            //                    .placeholder(R.drawable.medicine_icon)
            //                    .signature(new MediaStoreSignature("", Calendar.getInstance().getTime().getTime(), 0))
            //                    .error(R.drawable.medicine_icon)
            //                    .into(holder.image);
            //        } else {
            //            holder.image.setImageResource(R.drawable.medicine_icon);
            //        }

            holder.name.text = item.medicineName

            if (item.type != null && item.type != "") {
                holder.type.text = "Type: " + item.type
            } else {
                holder.type.text = "Type: -"
            }

//        holder.size.text = Utills.getTypeInfo(item)

            if (item.dosage != null && item.dosage != "" && item.dosage != "Potency:  " && item.dosage != "Volume:  ") {
                holder.size.text = item.dosage
            } else {
                holder.size.text = Constants.DEFAULT_DOSAGE
            }

            if (item.ingredients != null) {
                holder.dose.text = item.ingredients
            }

            if (item.manufacturer != null && item.manufacturer != "") {
                holder.details.text = item.manufacturer
            } else {
                holder.details.visibility = View.GONE
            }

            if (item.quantity < 1) {
                item.quantity = 1
            }

            holder.quantity.text = item.quantity.toString() + ""

            if (activity is OrderDetailsUserActivity) {
                holder.quantityLabel.text = "Quantity: " + item.quantity.toString()
            }

            holder.quantity.setOnClickListener{
                (activity as CartKActivity).setQuantityManually(position, item.quantity)
            }

//        holder.quantity.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                if (s != null && s.toString().trim { it <= ' ' }.length > 1) {
//                    var q = s.toString()
//                    item.quantity = q.toInt()
//                    updateQuantity(item, position, true, onlyUpdate = true)
//                } else {
//                    item.quantity = 1
//                    updateQuantity(item, position, true, onlyUpdate = true)
//                }
//            }
//        })

            holder.cartItem.setOnClickListener {
                if (showDetails) {
                    if (activity is CartKActivity)
                        activity.showMedicineDetails(item)
                } else {
                    //                    Intent details = new Intent(activity, MedicineDetailsActivity.class);
                    //                    details.putExtra("medicine", item);
                    //                    activity.startActivity(details);
                }
            }

            holder.deleteCB.setOnClickListener {
                if (holder.deleteCB.isChecked) {
                    (activity as CartKActivity).selectItemToDelete(position, -1,true)
                }
                else {
                    (activity as CartKActivity).selectItemToDelete(position,  -1, false)
                }
            }


            holder.removeItem.setOnClickListener {
                if (activity is CartKActivity) {
                    activity.removeItem(position)
                }
            }

            holder.increaseQuantity.setOnClickListener {
                updateQuantity(item, position, increase = true, onlyUpdate = false)
            }

            holder.increaseQuantity.setOnLongClickListener(object : View.OnLongClickListener {

                private val mHandler = Handler()
                private val incrementRunnable = object : Runnable {
                    override fun run() {
                        mHandler.removeCallbacks(this)
                        if (holder.increaseQuantity.isPressed) {
                            // increment the counter
                            // display the updated value here, if necessary

                            if (item.quantity < Constants.MAX_QUANTITY) {
                                item.quantity = item.quantity + 1
                            }

                            increaseDecreaseQuantity(holder, item, true)

                            mHandler.postDelayed(this, 100)
                        } else {
                            updateQuantity(item, position, true, onlyUpdate = true)
                        }
                    }
                }

                override fun onLongClick(v: View): Boolean {
                    mHandler.postDelayed(incrementRunnable, 0)
                    return true
                }
            })

            holder.decreaseQuantity.setOnClickListener {
                updateQuantity(item, position, false, onlyUpdate = false)
            }

            holder.decreaseQuantity.setOnLongClickListener(object : View.OnLongClickListener {

                private val mHandler = Handler()
                private val decrementRunnable = object : Runnable {
                    override fun run() {
                        mHandler.removeCallbacks(this)
                        if (holder.decreaseQuantity.isPressed) {
                            // increment the counter
                            // display the updated value here, if necessary

                            increaseDecreaseQuantity(holder, item, false)

                            mHandler.postDelayed(this, 100)
                        } else {
                            updateQuantity(item, position, false, onlyUpdate = true)
                        }
                    }
                }

                override fun onLongClick(v: View): Boolean {
                    mHandler.postDelayed(decrementRunnable, 0)
                    return true
                }
            })
        }

        else if(holder is CartPrescriptionViewHolder){
            val itemP = listPre[position - list.size]

            if (nightMode) {
                holder.cartItem.setBackgroundResource(R.color.editTextBackgroundNightMode)
                holder.name.setTextColor(activity.resources.getColor(R.color.white))
                holder.deleteCB.setButtonDrawable(R.drawable.checkbox_selector_colors)
            }

            holder.deleteCB.isChecked = itemP.select

            var path = ""

            if(itemP.path != null){
                path = if(itemP.urlB)
                    Utills.getCompleteUrl(itemP.path)
                else
                    itemP.path!!
            }

            if(path != null && path != ""){

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

            holder.deleteCB.setOnClickListener {
                if (holder.deleteCB.isChecked)
                    (activity as CartKActivity).selectItemToDelete(-1, position - (list.size) ,true)
                else
                    (activity as CartKActivity).selectItemToDelete(-1, position - (list.size),false)
            }

            holder.cartItem.setOnClickListener{
                val viewImage = Intent(activity, ViewImageActivity::class.java)
                viewImage.putExtra("imageUrl", itemP.path)
                viewImage.putExtra("type", Constants.PRESCRIPTION_IMAGE)
                activity.startActivity(viewImage)
            }
        }

    }

    override fun getItemCount(): Int {
//        if(list != null && listPre != null) {
            return list.size + listPre.size
//        } else if(list != null) {
//            return list.size
//        } else if (listPre != null){
//            return listPre.size
//        } else {
//            return 0
//        }
    }

    override fun getItemViewType(position: Int): Int {

        if(position < list.size){
            return VIEW_TYPE_MEDICINE
        }

        if(position - list.size < listPre.size){
            return VIEW_TYPE_PRESCRIPTION
        }

        return -1
    }

    private fun increaseDecreaseQuantity(holder: CartListViewHolder, item: MedicineDetails, increase: Boolean) {
        if(increase) {
            if (item.quantity < Constants.MAX_QUANTITY) {
                item.quantity = item.quantity + 1
            }

            holder.quantity.text = item.quantity.toString() + ""
        } else {
            if (item.quantity > 1) {
                item.quantity = item.quantity - 1
                holder.quantity.text = item.quantity.toString() + ""
                //            notifyDataSetChanged();
            } else {

            }
        }
    }

    private fun updateQuantity(item : MedicineDetails, position: Int, increase : Boolean, onlyUpdate: Boolean){
        if (activity is CartKActivity) {

            if(onlyUpdate){
                activity.setQuantity(position, item.quantity)
                updateCartBooleanStatus()
            } else {
                if (increase) {
                    if (item.quantity < Constants.MAX_QUANTITY) {
                        item.quantity += 1
                        activity.setQuantity(position, item.quantity)
                        updateCartBooleanStatus()
                    }
                } else {
                    if (item.quantity > 1) {
                        item.quantity -= 1
                        activity.setQuantity(position, item.quantity)
                        updateCartBooleanStatus()
                    } else {

                    }
                }
            }
        }
    }

    private fun updateCartBooleanStatus() {

        Preferences.addCartUpdateBooleanToSharedPreferences(activity, true)
    }

//    private fun updateCart(holder: CartListViewHolder, position: Int, medicineDetails: MedicineDetails, increase: Boolean) {
//
//        if (Internet.isAvailable(activity)) {
//
//            holder.increaseQuantity.isClickable = false
//            holder.decreaseQuantity.isClickable = false
//            holder.progressBar.visibility = View.VISIBLE
//
//            var quantity = 0
//
//            if (increase) {
//                quantity = 1
//            } else {
//                quantity = -1
//            }
//
//            val searchCall: Call<GeneralResponse>
//            val restApis = RetroClient.getClient().create(RestApis::class.java)
//
//            val id = RequestBody.create(MediaType.parse("text/plain"), medicineDetails.productId)
//            val medicineName = RequestBody.create(MediaType.parse("text/plain"), medicineDetails.medicineName)
//            val medicinePrice = RequestBody.create(MediaType.parse("text/plain"), medicineDetails.price)
//            val medicineQuantity = RequestBody.create(MediaType.parse("text/plain"), quantity.toString())
//            val medicineIngredient = RequestBody.create(MediaType.parse("text/plain"), medicineDetails.ingredients)
//            val medicineType = RequestBody.create(MediaType.parse("text/plain"), medicineDetails.type)
//            val medicineSize = RequestBody.create(MediaType.parse("text/plain"), medicineDetails.packSize)
//            val medicineManufacturer = RequestBody.create(MediaType.parse("text/plain"), medicineDetails.manufacturer)
//            val delete = RequestBody.create(MediaType.parse("text/plain"), "false")
//            val checkout = RequestBody.create(MediaType.parse("text/plain"), "false")
//
//            //            new RestCaller(this, PharmaD.getRestClient().addToCart("*/*",
//            //                    Preferences.getDeviceIdFromSharedPreferences(activity),
//            //                    id,
//            //                    medicineName,
//            //                    medicinePrice,
//            //                    medicineQuantity,
//            //                    medicineIngredient,
//            //                    medicineType,
//            //                    medicineSize,
//            //                    medicineManufacturer,
//            //                    delete,
//            //                    checkout), 2);
//
//            searchCall = restApis.addToCart("*/*",
//                    Preferences.getDeviceIdFromSharedPreferences(activity),
//                    id,
//                    medicineName,
//                    medicinePrice,
//                    medicineQuantity,
//                    medicineIngredient,
//                    medicineType,
//                    medicineSize,
//                    medicineManufacturer,
//                    delete,
//                    checkout)
//
//            searchCall.enqueue(object : Callback<GeneralResponse> {
//                override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
//                    if (response.isSuccessful) {
//
//                        holder.increaseQuantity.isClickable = true
//                        holder.decreaseQuantity.isClickable = true
//                        holder.progressBar.visibility = View.GONE
//
//                        if (response.body() != null && response.body()!!.isSuccess) {
//
//                            val generalResponse = response.body()
//
//                            if (generalResponse != null && generalResponse.results != null && generalResponse.results.medicineAdded != null) {
//
//                                (activity as CartKActivity).setQuantity(position, Integer.valueOf(generalResponse.results.medicineAdded.quantity))
//                                activity.showToast(Constants.UPDATED_QUANTITY)
//                            } else if (generalResponse != null && generalResponse.msg != null && generalResponse.msg == Constants.UPDATED_RESPONSE) {
//                                (activity as CartKActivity).showToast(Constants.UPDATED_QUANTITY)
//                                activity.updateQuantity(position)
//                            } else {
//                                (activity as CartKActivity).showToast(Constants.UPDATED_QUANTITY_FAILED)
//                            }
//
//                        } else {
//                            (activity as CartKActivity).showToast(Constants.UPDATED_QUANTITY_FAILED)
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
//                    holder.increaseQuantity.isClickable = true
//                    holder.decreaseQuantity.isClickable = true
//                    holder.progressBar.visibility = View.GONE
//                    (activity as CartKActivity).showToast(Constants.UPDATED_QUANTITY_FAILED)
//                }
//            })
//
//        }
//    }

}