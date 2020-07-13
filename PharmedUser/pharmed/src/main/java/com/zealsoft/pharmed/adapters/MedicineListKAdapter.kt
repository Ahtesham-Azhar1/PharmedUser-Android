package com.zealsoft.pharmed.adapters

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.MediaStoreSignature
import com.zealsoft.pharmed.R
import com.zealsoft.pharmed.Util.Constants
import com.zealsoft.pharmed.Util.Utills
import com.zealsoft.pharmed.activities.MedicineDetailsKActivity
import com.zealsoft.pharmed.activities.SearchKActivity
import com.zealsoft.pharmed.models.MedicineDetails
import com.zealsoft.pharmed.viewHolders.MedicineListViewHolder
import java.util.*

class MedicineListKAdapter (val activity : Activity, val list : List<MedicineDetails>, val nightMode: Boolean,
                            private val showDetails: Boolean, private val disableCart: Boolean) : androidx.recyclerview.widget.RecyclerView.Adapter<MedicineListViewHolder>() {

//    private PharmaciesAdapterListener listener;

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MedicineListViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.single_medicine_list_item, viewGroup, false)
        return MedicineListViewHolder(v)
    }

    override fun onBindViewHolder(holder: MedicineListViewHolder, i: Int) {

        val item = list[i]

        var added = false

        item.minQuantity = item.quantity

        if (nightMode) {
            holder.medicineItem.setBackgroundResource(R.color.editTextBackgroundNightMode)
            holder.name.setTextColor(activity.resources.getColor(R.color.white))
            holder.type.setTextColor(activity.resources.getColor(R.color.white))
            holder.size.setTextColor(activity.resources.getColor(R.color.white))
            holder.details.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
            holder.dose.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
            holder.quantity.setBackgroundResource(R.drawable.pd_background_et_round_night_mode_light)
            holder.quantity.setTextColor(activity.resources.getColor(R.color.textColorNightMode))
        }

        if(disableCart){
            holder.addToCart.visibility = View.GONE
            holder.increaseQuantity.visibility = View.GONE
            holder.decreaseQuantity.visibility = View.GONE
            holder.quantity.visibility = View.GONE
        }

        if (item.added) {
            holder.addToCart.setImageResource(R.drawable.icon_cart_added)
        } else {
            holder.addToCart.setImageResource(R.drawable.icon_cart_add)
        }

        if (item.url != null && item.url != "") {

            val options = RequestOptions()
                    .centerCrop()
            //.placeholder(R.drawable.placeholder)
            //.error(R.drawable.error)
            //.priority(Priority.HIGH);

            //                    if (image != null) {
            Glide
                    .with(activity)
                    .load(Utills.getCompleteUrl(item.url))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.icon_medicine)
                    .signature(MediaStoreSignature("", Calendar.getInstance().time.time, 0))
                    .error(R.drawable.icon_medicine)
                    .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.icon_medicine)
        }

        holder.name.text = item.medicineName

        if (item.type != null && item.type != "") {
            holder.type.text = "Type: " + item.type
            holder.type.visibility = View.VISIBLE
        } else {
            holder.type.text = ""
            holder.type.visibility = View.GONE
        }

//        holder.size.text = Utills.getTypeInfo(item)

        if (item.dosage != null && item.dosage != "") {
            holder.size.text = item.dosage
            holder.size.visibility = View.VISIBLE
        } else {
            holder.size.visibility = View.GONE
        }

        if (item.ingredients != null && item.ingredients != "") {
            holder.dose.text = item.ingredients
//            holder.dose.visibility = View.VISIBLE
        } else {
//            holder.dose.visibility = View.GONE
        }

        if (item.manufacturer != null && item.manufacturer != "") {
            holder.details.text = item.manufacturer
            holder.details.visibility = View.VISIBLE
        } else {
            holder.details.visibility = View.GONE
        }

        if (item.quantity < 1) {
//            if(item.type.toLowerCase() == "tabs" || item.type.toLowerCase() == "caps"){
//                item.quantity = 10
//            } else {
//                item.quantity = 1
//            }

//            item.quantity = Utills.getMinQuantity(item)
        }

        if (item.quantity > Constants.MAX_QUANTITY) {
            item.quantity = Constants.MAX_QUANTITY
        }


        holder.quantity.text = item.quantity.toString() + ""

        holder.quantity.setOnClickListener {
            (activity as SearchKActivity).setQuantityManually(position = i, quantity = item.quantity)
        }

        holder.medicineItem.setOnClickListener {
            if (showDetails) {
                (activity as SearchKActivity).showMedicineDetails(item, true)
            } else {

                val details = Intent(activity, MedicineDetailsKActivity::class.java)
                details.putExtra(Constants.INTENT_MEDICINE_DETAILS, item)
                activity.startActivityForResult(details, 12)
            }
        }

        holder.addToCart.setOnClickListener {

                //                Utills.addToCart(activity, item);
//            holder.loading.visibility = View.VISIBLE
//            holder.addToCart.isClickable = false
            item.quantity = Integer.valueOf(holder.quantity.text.toString())
//            addToCartCall(holder, item)


            if(!Utills.addToCart(activity, item)) {
                item.added = true
                (activity as SearchKActivity).showToast(Constants.ADD_TO_CART)
                activity.setCartCount()
                activity.setSelectedPharmacy()
                notifyDataSetChanged()

                item.quantity = item.minQuantity
            } else {
                (activity as SearchKActivity).showToast(Constants.ALREADY_ADDED_TO_CART)
            }

            //                if(activity instanceof CartActivity){
            //                    ((CartActivity) activity).removeItem(position);
            //                }
        }

        holder.increaseQuantity.setOnClickListener { increaseQuantity(holder, item) }

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

                        increaseQuantity(holder, item)

                        mHandler.postDelayed(this, 100)
                    }
                }
            }

            override fun onLongClick(v: View): Boolean {
                mHandler.postDelayed(incrementRunnable, 0)
                return true
            }
        })

        holder.decreaseQuantity.setOnClickListener { decreaseQuantity(holder, item) }

        holder.decreaseQuantity.setOnLongClickListener(object : View.OnLongClickListener {

            private val mHandler = Handler()
            private val decrementRunnable = object : Runnable {
                override fun run() {
                    mHandler.removeCallbacks(this)
                    if (holder.decreaseQuantity.isPressed) {
                        // increment the counter
                        // display the updated value here, if necessary

                        decreaseQuantity(holder, item)

                        mHandler.postDelayed(this, 100)
                    }
                }
            }

            override fun onLongClick(v: View): Boolean {
                mHandler.postDelayed(decrementRunnable, 0)
                return true
            }
        })
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun increaseQuantity(holder: MedicineListViewHolder, item: MedicineDetails) {
        if (item.quantity < Constants.MAX_QUANTITY) {
            item.quantity = item.quantity + 1
        }

        holder.quantity.text = item.quantity.toString() + ""
        //        notifyDataSetChanged();
    }

    private fun decreaseQuantity(holder: MedicineListViewHolder, item: MedicineDetails) {
        if (item.quantity > 1) {
            item.quantity = item.quantity - 1
            holder.quantity.text = item.quantity.toString() + ""
            //            notifyDataSetChanged();
        } else {

        }
    }

//    private fun addToCartCall(holder: MedicineListViewHolder, medicineDetails: MedicineDetails) {
//        if (Internet.isAvailable(activity)) {
//
//            val deviceId = RequestBody.create(MediaType.parse("text/plain"), Preferences.getDeviceIdFromSharedPreferences(activity))
//            val id = RequestBody.create(MediaType.parse("text/plain"), medicineDetails.id)
//            val medicineName = RequestBody.create(MediaType.parse("text/plain"), medicineDetails.medicineName)
//            val medicinePrice = RequestBody.create(MediaType.parse("text/plain"), medicineDetails.price)
//            val medicineQuantity = RequestBody.create(MediaType.parse("text/plain"), medicineDetails.quantity.toString())
//            val medicineIngredient = RequestBody.create(MediaType.parse("text/plain"), medicineDetails.ingredients)
//            val medicineType = RequestBody.create(MediaType.parse("text/plain"), medicineDetails.type)
//            val medicineSize = RequestBody.create(MediaType.parse("text/plain"), medicineDetails.packSize)
//            val medicineManufacturer = RequestBody.create(MediaType.parse("text/plain"), medicineDetails.manufacturer)
//            val deleted = RequestBody.create(MediaType.parse("text/plain"), "false")
//            val checkout = RequestBody.create(MediaType.parse("text/plain"), "false")
//
//            var addToCartCall: Call<GeneralResponse>
//            var restApis: RestApis = RetroClient.getClient().create(RestApis::class.java)
//
////            addToCartCall = restApis.addToCart("*/*",
////                    Preferences.getDeviceIdFromSharedPreferences(activity),
////                    medicineDetails.id,
////                    medicineDetails.medicineName,
////                    medicineDetails.price,
////                    medicineDetails.quantity,
////                    medicineDetails.ingredients,
////                    medicineDetails.type,
////                    medicineDetails.packSize,
////                    medicineDetails.manufacturer,
////                    false,
////                    false)
//
//            addToCartCall = restApis.addToCart("*/*",
//                    Preferences.getDeviceIdFromSharedPreferences(activity),
//                    id,
//                    medicineName,
//                    medicinePrice,
//                    medicineQuantity,
//                    medicineIngredient,
//                    medicineType,
//                    medicineSize,
//                    medicineManufacturer,
//                    deleted,
//                    checkout)
//            addToCartCall.enqueue(object : Callback<GeneralResponse> {
//                override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
//                    if (response.isSuccessful) {
//                        if (response.body() != null && response.body()!!.isSuccess) {
//
//                            holder.loading.visibility = View.GONE
//                            holder.addToCart.isClickable = true
//
//                            val generalResponse = response.body()
//
//                            if (generalResponse != null && generalResponse.results != null && generalResponse.results.medicineAdded != null) {
//
//                                Utills.addToCart(activity, generalResponse.results.medicineAdded)
//
//                                (activity as SearchKActivity).showToast(Constants.ADD_TO_CART)
//                                activity.setCartCount()
//                                activity.setSelectedPharmacy()
//                                notifyDataSetChanged()
//                            } else if (generalResponse != null && generalResponse.msg != null && generalResponse.msg == Constants.UPDATED_RESPONSE) {
//                                (activity as SearchKActivity).showToast(Constants.UPDATED_QUANTITY)
//                                notifyDataSetChanged()
//                            } else {
//                                (activity as SearchKActivity).showToast(Constants.ADD_CART_FAILED)
//                            }
//
//                        } else {
//                            holder.loading.visibility = View.GONE
//                            holder.addToCart.isClickable = true
//                            (activity as SearchKActivity).showToast(Constants.ADD_CART_FAILED)
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
//                    holder.loading.visibility = View.GONE
//                    holder.addToCart.isClickable = true
//                    (activity as SearchKActivity).showToast(Constants.ADD_CART_FAILED)
//                }
//            })
//
////            //            Loading.show(activity, false, "Please wait...");
////
////            val id = RequestBody.create(MediaType.parse("text/plain"), medicineDetails.id)
////            val medicineName = RequestBody.create(MediaType.parse("text/plain"), medicineDetails.medicineName)
////            val medicinePrice = RequestBody.create(MediaType.parse("text/plain"), medicineDetails.price)
////            val medicineQuantity = RequestBody.create(MediaType.parse("text/plain"), medicineDetails.quantity.toString())
////            val medicineIngredient = RequestBody.create(MediaType.parse("text/plain"), medicineDetails.ingredients)
////            val medicineType = RequestBody.create(MediaType.parse("text/plain"), medicineDetails.type)
////            val medicineSize = RequestBody.create(MediaType.parse("text/plain"), medicineDetails.packSize)
////            val medicineManufacturer = RequestBody.create(MediaType.parse("text/plain"), medicineDetails.manufacturer)
////
////
////            RestCaller(this, PharmaD.getRestClient().addToCart("*/*",
////                    Preferences.getDeviceIdFromSharedPreferences(activity),
////                    medicineDetails.id,
////                    medicineDetails.medicineName,
////                    medicineDetails.price,
////                    medicineDetails.quantity,
////                    medicineDetails.ingredients,
////                    medicineDetails.type,
////                    medicineDetails.packSize,
////                    medicineDetails.manufacturer,
////                    false,
////                    false), 1)
//        } else {
//            (activity as SearchKActivity).showToast(Constants.NO_INTERNET_CONNECTION)
//        }
//    }

//    override fun onSuccess(call: Call<*>, response: Response<*>, reqCode: Int) {
//
//
//        val generalResponse = response.body() as GeneralResponse?
//
//        if (generalResponse != null && generalResponse.results != null && generalResponse.results.medicineAdded != null) {
//
//            Utills.addToCart(activity, generalResponse.results.medicineAdded)
//
//            (activity as SearchKActivity).showToast(Constants.ADD_TO_CART)
//            activity.setCartCount()
//            activity.setSelectedPharmacy()
//            notifyDataSetChanged()
//        } else if (generalResponse != null && generalResponse.msg != null && generalResponse.msg == Constants.UPDATED_RESPONSE) {
//            (activity as SearchKActivity).showToast(Constants.UPDATED_QUANTITY)
//            notifyDataSetChanged()
//        } else {
//            (activity as SearchKActivity).showToast(Constants.ADD_CART_FAILED)
//        }
//    }
//
//    override fun onFailure(call: Call<*>, error: GenericResponse, reqCode: Int) {
//        (activity as SearchKActivity).showToast(Constants.ADD_CART_FAILED)
//    }
//
//    override fun onApiCrash(call: Call<*>, t: Throwable, reqCode: Int) {
//        (activity as SearchKActivity).showToast(Constants.ADD_CART_FAILED)
//    }
}