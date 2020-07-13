package com.zealsoft.pharmed.activities

import android.view.MotionEvent
import android.view.View
import com.zealsoft.pharmed.R
import com.zealsoft.pharmed.Util.Constants
import com.zealsoft.pharmed.Util.Preferences
import com.zealsoft.pharmed.Util.Utills
import com.zealsoft.pharmed.adapters.TimingsListKAdapter
import com.zealsoft.pharmed.models.EventPerformed
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.MediaStoreSignature
import com.zealsoft.pharmed.customWidgets.CustomDialog
import com.zealsoft.pharmed.models.PharmacyDetails
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*


class PharmacyDetailsKActivity : AppCompatActivity(), View.OnClickListener, View.OnTouchListener {

    private lateinit var detailScreen: ConstraintLayout
    private lateinit var title: TextView
    private lateinit var cartCount: TextView
    private lateinit var back: ImageView
    private lateinit var medicineSearch: ImageView
    private lateinit var cartToolbar: ImageView
    private lateinit var logoToolbar: CircleImageView
    private lateinit var pharmacyLogo: CircleImageView
    private lateinit var initials: TextView
    private lateinit var deliveryLimit: TextView
    private lateinit var pharmacyName: TextView
    private lateinit var email: TextView
    private lateinit var contactPerson: TextView
    private lateinit var phone: TextView
    private lateinit var licenseNo: TextView
    private lateinit var address: TextView
    private lateinit var website: TextView
    private lateinit var timingsLabel:TextView
    private lateinit var timings: TextView
    private lateinit var emailIcon: ImageView
    private lateinit var contactPersonIcon: ImageView
    private lateinit var phoneIcon: ImageView
    private lateinit var licenseNoIcon: ImageView
    private lateinit var addressIcon: ImageView
    private lateinit var websiteIcon: ImageView
    private lateinit var timingsIcon: ImageView
    private lateinit var message: ImageView
    private lateinit var call: ImageView
    private lateinit var direction: ImageView
    private lateinit var share: ImageView
    private lateinit var cart: ImageView
    private lateinit var promotions: ImageView
    private lateinit var edit: ImageView
    private lateinit var bottom: View
    private lateinit var selectBtn: Button
    private var mLastClickTime: Long = 0
    private lateinit var animUp: Animation
    private var pharmacyDetails: PharmacyDetails? = null
    private lateinit var eventPerformed: EventPerformed
    private lateinit var timingsRecyclerView: RecyclerView
    private var timingsListAdapter: TimingsListKAdapter? = null
    private var user = false
    private var nightMode: Boolean = false
    private var selectPharmacy = false
    private var customDialog: CustomDialog? = null

    private var open24_7 = false
    private lateinit var open24_7Feature: ImageView
    private lateinit var open24_7FeatureLabel: TextView
    private var onlineOrdersB = false
    private lateinit var onlineOrders: ImageView
    private lateinit var onlineOrdersLabel: TextView
    private var delivery = false
    private lateinit var deliveryFeature: ImageView
    private lateinit var deliveryFeatureLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pharmacy_details_k)

        setViews()
    }

    private fun checkTheme() {
        if (nightMode) {
            detailScreen.setBackgroundResource(R.color.formBackgroundNightMode)
            pharmacyName.setTextColor(resources.getColor(R.color.textColorNightMode))
            email.setTextColor(resources.getColor(R.color.textColorNightMode))
            contactPerson.setTextColor(resources.getColor(R.color.textColorNightMode))
            phone.setTextColor(resources.getColor(R.color.textColorNightMode))
            website.setTextColor(resources.getColor(R.color.textColorNightMode))
            address.setTextColor(resources.getColor(R.color.textColorNightMode))
            timingsLabel.setTextColor(resources.getColor(R.color.textColorNightMode))
            Utills.changeNavigationBarColor(this, Constants.COLOR_BLACK)
        } else {
            Utills.changeNavigationBarColor(this, Constants.COLOR_FORM)
        }
    }

    private fun setViews() {

        title = findViewById(R.id.title)
        title.text = resources.getString(R.string.title_details_screen)
        medicineSearch = findViewById(R.id.med_search_toolbar)
        medicineSearch.setOnClickListener(this)
        cartToolbar = findViewById(R.id.cart_toolbar)
        cartToolbar.setOnClickListener(this)
        cartCount = findViewById(R.id.cart_count)
        logoToolbar = findViewById(R.id.logo_toolbar_pharmed)
        logoToolbar.setOnClickListener(this)
        back = findViewById(R.id.back)
        back.setOnClickListener(this)

        medicineSearch.visibility = View.GONE

        detailScreen = findViewById(R.id.detail_screen)
        pharmacyLogo = findViewById(R.id.pharmacy_logo)
        pharmacyLogo.setOnClickListener(this)
        initials = findViewById(R.id.initials)
        deliveryLimit = findViewById(R.id.delivery_limit)
        pharmacyName = findViewById(R.id.pharmacy_name)
        emailIcon = findViewById(R.id.email_icon)
        emailIcon.setOnClickListener(this)
        email = findViewById(R.id.email)
        email.setOnClickListener(this)
        contactPersonIcon = findViewById(R.id.contact_person_icon)
        contactPerson = findViewById(R.id.contact_person)
        phoneIcon = findViewById(R.id.phone_icon)
        phone = findViewById(R.id.phone)
        licenseNoIcon = findViewById(R.id.license_no_icon)
        licenseNoIcon.setOnClickListener(this)
        licenseNo = findViewById(R.id.license_no)
        licenseNo.setOnClickListener(this)
        addressIcon = findViewById(R.id.address_icon)
        address = findViewById(R.id.address)
        websiteIcon = findViewById(R.id.website_icon)
        websiteIcon.setOnClickListener(this)
        website = findViewById(R.id.website)
        website.setOnClickListener(this)
        message = findViewById(R.id.msg)
        message.setOnClickListener(this)
        call = findViewById(R.id.iv_call)
        call.setOnClickListener(this)
        share = findViewById(R.id.iv_share)
        share.setOnClickListener(this)
        cart = findViewById(R.id.iv_cart)
        cart.setOnClickListener(this)
        direction = findViewById(R.id.iv_directions)
        direction.setOnClickListener(this)
        promotions = findViewById(R.id.iv_promotions)
        promotions.setOnClickListener(this)
        timingsIcon = findViewById(R.id.timings_icon)
        timingsLabel = findViewById(R.id.timings_label)
        timings = findViewById(R.id.timings)
        eventPerformed = EventPerformed()
        timingsRecyclerView = findViewById(R.id.timings_recycler)
        edit = findViewById(R.id.edit)
        edit.setOnClickListener(this)
        bottom = findViewById(R.id.bottom)
        selectBtn = findViewById(R.id.select)
        selectBtn.setOnClickListener(this)

        open24_7Feature = findViewById(R.id.open_24_7)
        open24_7Feature.setOnClickListener(this)
        open24_7Feature.setOnTouchListener(this)
        open24_7FeatureLabel = findViewById(R.id.open_24_7_label)
        onlineOrders = findViewById(R.id.online_orders)
        onlineOrders.setOnClickListener(this)
        onlineOrders.setOnTouchListener(this)
        onlineOrdersLabel = findViewById(R.id.online_orders_label)
        deliveryFeature = findViewById(R.id.delivery)
        deliveryFeature.setOnClickListener(this)
        deliveryFeature.setOnTouchListener(this)
        deliveryFeatureLabel = findViewById(R.id.delivery_label)

        customDialog = CustomDialog(this)

        intent = intent

        pharmacyDetails = intent.getSerializableExtra("pharmacy") as PharmacyDetails
        user = intent.getBooleanExtra("user", false)
        selectPharmacy = intent.getBooleanExtra(Constants.INTENT_SELECT_PHARMACY, false)

        nightMode = Preferences.getNightThemeSelectionFromSharedPreferences(this)


        animUp = AnimationUtils.loadAnimation(this, R.anim.bounce_animation)

        checkTheme()

        if (user) {
            edit.visibility = View.VISIBLE
        } else {
            edit.visibility = View.GONE
        }

        if(selectPharmacy){
            bottom.visibility = View.VISIBLE
            selectBtn.visibility = View.VISIBLE
            Utills.changeNavigationBarColor(this, Constants.COLOR_GREY)
        } else {
            bottom.visibility = View.GONE
            selectBtn.visibility = View.GONE
        }

        if (pharmacyDetails != null) {

            if (Preferences.getPharmacyDataFromSharedPreferences(this) != null) {
                cart.visibility = View.GONE
                promotions.visibility = View.GONE
                if (pharmacyDetails?.id == Preferences.getPharmacyDataFromSharedPreferences(this).id) {
                    edit.visibility = View.VISIBLE
                    emailIcon.visibility = View.VISIBLE
                    email.visibility = View.VISIBLE
                    user = true
                } else {
                    emailIcon.visibility = View.GONE
                    email.visibility = View.GONE
                    licenseNoIcon.visibility = View.GONE
                    licenseNo.visibility = View.GONE
                    user = false
                }
            } else {
                emailIcon.visibility = View.GONE
                email.visibility = View.GONE
                licenseNoIcon.visibility = View.GONE
                licenseNo.visibility = View.GONE
                user = false
            }

            populateDetails()
        }
    }

    private fun populateDetails() {

        disableButton()

        val thread = object : Thread() {
            override fun run() {

                runOnUiThread {
                    message.startAnimation(animUp)
                    call.startAnimation(animUp)
                    direction.startAnimation(animUp)
                    share.startAnimation(animUp)
                    cart.startAnimation(animUp)
                    promotions.startAnimation(animUp)
                }
                super.run()
            }
        }
        thread.start()

        Handler().postDelayed({
            disableButton()
        }, 1000)

        eventPerformed.pharmacyId = pharmacyDetails?.id
        eventPerformed.placeId = pharmacyDetails?.placeId
        eventPerformed.setPlaceLat(pharmacyDetails?.placeLat)
        eventPerformed.setPlaceLng(pharmacyDetails?.placeLng)
        if (Preferences.getDeviceIdFromSharedPreferences(this) != null &&
            Preferences.getDeviceIdFromSharedPreferences(this) != null &&
            Preferences.getDeviceIdFromSharedPreferences(this) != "") {
            eventPerformed.deviceId = Preferences.getDeviceIdFromSharedPreferences(this)
        }

        if (pharmacyDetails?.placeName != null) {
            pharmacyName.text = pharmacyDetails?.placeName

            pharmacyLogo.setImageResource(R.color.initials_background_color)
            initials.visibility = View.VISIBLE
            initials.text = pharmacyDetails?.placeName!!.substring(0,1).capitalize()
        }

        if (pharmacyDetails?.icon != null && pharmacyDetails?.icon != "") {

            val options = RequestOptions()
                .centerCrop()

            Glide
                .with(this)
                .load(Utills.getCompleteUrl(pharmacyDetails?.icon))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .signature(MediaStoreSignature("", Calendar.getInstance().time.time, 0))
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, isFirstResource: Boolean): Boolean {

                        return false
                    }
                    override fun onResourceReady(resource: Drawable?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                        initials.visibility = View.GONE
                        return false
                    }
                })
                .placeholder(R.color.initials_background_color)
                .error(R.color.initials_background_color)
                .into(pharmacyLogo)
//
        } else {

        }

        if(pharmacyDetails?.deliveryAvailable != null && pharmacyDetails?.deliveryAvailable!!) {
            if (pharmacyDetails?.minOrderLimit != null && pharmacyDetails?.minOrderLimit!! > 0) {
                deliveryLimit.text = "Min. Order:\n" + Utills.getCurrencyCode(pharmacyDetails?.currencySymbol) + " " + pharmacyDetails?.minOrderLimit
                deliveryLimit.visibility = View.VISIBLE
            } else
                deliveryLimit.visibility = View.GONE
        } else {
            deliveryLimit.visibility = View.GONE
        }

        if(pharmacyDetails?.contactPerson != null && pharmacyDetails?.contactPerson!!.isNotEmpty()){
            contactPerson.text = pharmacyDetails?.contactPerson
        } else {
            contactPerson.visibility = View.GONE
            contactPersonIcon.visibility = View.GONE
        }

        if(pharmacyDetails?.deliveryAvailable == null || !pharmacyDetails?.deliveryAvailable!!){
            deliveryFeature.setColorFilter(resources.getColor(R.color.grey))
            delivery = false
        } else {
            if(nightMode)
                deliveryFeature.clearColorFilter()
            else
                deliveryFeature.setColorFilter(resources.getColor(R.color.featureIconColor))

            delivery = true
        }

        if(pharmacyDetails?.pharmacyStatus == null || pharmacyDetails?.pharmacyStatus != Constants.PHARMACY_STATUS_REGISTERED){
            onlineOrders.setColorFilter(resources.getColor(R.color.grey))
            onlineOrdersB = false
        } else {
            if(nightMode)
                onlineOrders.clearColorFilter()
            else
                onlineOrders.setColorFilter(resources.getColor(R.color.featureIconColor))

            onlineOrdersB = true
        }

//        if(pharmacyDetails?.openingHours != null && pharmacyDetails?.openingHours!!.size == 7){
//            pharmacyDetails?.openingHoursList = Utills.getHoursFromList(pharmacyDetails?.openingHours)
//            if(Utills.isOpen_24_7(pharmacyDetails?.openingHoursList)){
//                if(nightMode)
//                    open24_7Feature.clearColorFilter()
//                else
//                    open24_7Feature.setColorFilter(resources.getColor(R.color.featureIconColor))
//
//                open24_7 = true
//            } else {
//                open24_7Feature.setColorFilter(resources.getColor(R.color.grey))
//                open24_7 = false
//            }
//        } else {
//            open24_7Feature.setColorFilter(resources.getColor(R.color.grey))
//            open24_7 = false
//        }

        if (pharmacyDetails?.placeEmail != null && pharmacyDetails?.placeEmail != "") {
            email.text = pharmacyDetails?.placeEmail
        } else {
            emailIcon.visibility = View.GONE
            email.visibility = View.GONE
        }

        if (pharmacyDetails?.placeNumber != null && pharmacyDetails?.placeNumber != "") {
            phone.text = pharmacyDetails?.placeNumber
        } else {
            phoneIcon.visibility = View.GONE
            phone.visibility = View.GONE
        }

        if (pharmacyDetails?.placeAddress != null && pharmacyDetails?.placeAddress != "") {
            address.text = pharmacyDetails?.placeAddress
        } else {
            addressIcon.visibility = View.GONE
            address.visibility = View.GONE
        }

        if(pharmacyDetails?.website != null && !pharmacyDetails?.website.equals("")){
            website.text = pharmacyDetails?.website
        } else {
            websiteIcon.visibility = View.GONE
            website.visibility = View.GONE
        }

        if(user) {
            if(pharmacyDetails?.licenseNumber != null && pharmacyDetails?.licenseNumber != null && pharmacyDetails?.licenseNumber != "") {
                licenseNo.text = pharmacyDetails?.licenseNumber
                if(pharmacyDetails?.licenseExpiry != null && pharmacyDetails?.licenseExpiry != "") {
                    licenseNo.text = pharmacyDetails?.licenseNumber + " (" + pharmacyDetails?.licenseExpiry + ")"
                }
                licenseNoIcon.visibility = View.VISIBLE
                licenseNo.visibility = View.VISIBLE
            } else {
                licenseNoIcon.visibility = View.GONE
                licenseNo.visibility = View.GONE
            }
        }

        if (pharmacyDetails?.openingHours != null && pharmacyDetails?.openingHours!!.size > 0) {

            timingsRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            timingsListAdapter = TimingsListKAdapter(this, pharmacyDetails!!.openingHours!!, nightMode)
            timingsRecyclerView.adapter = timingsListAdapter
            timingsRecyclerView.invalidate()

        } else {
            timingsIcon.visibility = View.GONE
            timingsLabel.visibility = View.GONE
            timings.visibility = View.GONE
            timingsRecyclerView.visibility = View.GONE
        }

    }

    private fun disableButton() {

        if (pharmacyDetails?.placeNumber != null) {
            if (pharmacyDetails?.phoneNumberType == Constants.NUMBER_TYPE_MOBILE) {
                call.isClickable = true
                message.isClickable = true
                cart.isClickable = true
                call.setColorFilter(resources.getColor(R.color.white))
                message.setColorFilter(resources.getColor(R.color.white))
                cart.setColorFilter(resources.getColor(R.color.white))
            } else {
                message.isClickable = false
                call.isClickable = true
                cart.isClickable = false
                call.setColorFilter(resources.getColor(R.color.white))
                message.setColorFilter(resources.getColor(R.color.grey))
                cart.setColorFilter(resources.getColor(R.color.grey))
            }
        } else {
            call.isClickable = false
            message.isClickable = false
            cart.isClickable = false
            call.setColorFilter(resources.getColor(R.color.grey))
            message.setColorFilter(resources.getColor(R.color.grey))
            cart.setColorFilter(resources.getColor(R.color.grey))
        }

        if(pharmacyDetails?.pharmacyStatus == Constants.PHARMACY_STATUS_REGISTERED){
            cart.setColorFilter(resources.getColor(R.color.white))
            cart.isClickable = true
        }

        if(pharmacyDetails?.promotionAvailable != null && pharmacyDetails?.promotionAvailable!!){
            promotions.setColorFilter(resources.getColor(R.color.white))
            promotions.isClickable = true
        } else {
            promotions.setColorFilter(resources.getColor(R.color.grey))
            promotions.isClickable = false
        }
    }

    private fun startWebView(){
//        val webView = Intent(this, WebViewKActivity::class.java)
//        webView.putExtra("url", pharmacyDetails?.website)
//        startActivity(webView)
    }

    private fun eventsCall(performedEvent: EventPerformed) {
//        if (Internet.isAvailable(this)) {
//            RestCaller(this, PharmaD.getRestClient().eventsCall(performedEvent), 1)
//        }
    }

    fun selectPharmacy(){

        val returnIntentOk = Intent()
        returnIntentOk.putExtra(Constants.INTENT_SELECT_PHARMACY, pharmacyDetails)
        setResult(Activity.RESULT_OK, returnIntentOk)
        finish()
    }

    private fun viewLicenseImage(){
        if(pharmacyDetails?.licenseURL != null && pharmacyDetails?.licenseURL != ""){
            val viewImage = Intent(this, ViewImageActivity::class.java)
            viewImage.putExtra("imageUrl", pharmacyDetails?.licenseURL)
            viewImage.putExtra("type", Constants.IMAGE_PATH_LICENSE)
            startActivity(viewImage)
        }
    }

    override fun onResume() {
        Handler().postDelayed({
            disableButton()
        }, 200)
        super.onResume()
    }

    override fun onClick(v: View?) {

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        performClick(v)
    }

    private fun performClick(v: View?) {
        when (v?.id) {

            R.id.back ->

                finish()

            R.id.logo_toolbar_pharmed ->
                Utills.goToHome(this)

            R.id.med_search_toolbar -> {

                val searchMedicines = Intent(this, SearchKActivity::class.java)
                searchMedicines.putExtra("searchMedicine", true)
                searchMedicines.putExtra("selectedPharmacy", pharmacyDetails)
                startActivity(searchMedicines)
            }

            R.id.cart_toolbar ->

                startActivity(Intent(this, CartKActivity::class.java))

            R.id.pharmacy_logo -> {

                if(pharmacyDetails?.icon != null && pharmacyDetails?.icon != "" && pharmacyDetails?.icon!!.isNotEmpty()) {

                    val viewImage = Intent(this, ViewImageActivity::class.java)
                    viewImage.putExtra("imageUrl", pharmacyDetails?.icon)
                    viewImage.putExtra("type", Constants.TYPE_PHARMACY)
                    startActivity(viewImage)
                }
            }

            R.id.email_icon ->
                Utills.sendEmail(this, pharmacyDetails?.placeEmail)

            R.id.email ->
                Utills.sendEmail(this, pharmacyDetails?.placeEmail)

            R.id.website_icon -> {
                startWebView()

            }

            R.id.website ->
                startWebView()

            R.id.msg ->

                if (pharmacyDetails?.placeNumber != null && pharmacyDetails?.placeNumber != "") {
                    eventPerformed.eventType = Constants.EVENT_SMS
                    if(Utills.sendSms(this, pharmacyDetails?.placeNumber, "")) {
                        eventsCall(eventPerformed)
                    }
                }

            R.id.iv_call ->

                if (pharmacyDetails?.placeNumber != null && pharmacyDetails?.placeNumber != "") {
                    eventPerformed.eventType = Constants.EVENT_CALL
                    eventsCall(eventPerformed)
                    Utills.makeCall(this, pharmacyDetails?.placeNumber)
                }

            R.id.iv_directions -> {

                eventPerformed.eventType = Constants.EVENT_DIRECTION
                eventsCall(eventPerformed)
                Utills.getDirections(this, pharmacyDetails?.placeLat, pharmacyDetails?.placeLng)
            }

            R.id.iv_share -> {

                eventPerformed.eventType = Constants.EVENT_SHARE
                eventsCall(eventPerformed)
                Utills.shareLocation(this, pharmacyDetails?.placeName, pharmacyDetails?.placeAddress,
                    pharmacyDetails?.placeNumber, pharmacyDetails?.placeLat, pharmacyDetails?.placeLng)
            }

            R.id.iv_cart -> {

                Preferences.addSelectedPharmacyToSharedPreferences(this, pharmacyDetails)
                startActivity(Intent(this, CartKActivity::class.java))
            }

            R.id.iv_promotions -> {
//                val i = Intent(this, PromotionsListActivity::class.java)
//                i.putExtra(Constants.INTENT_PHARMACY_ID, pharmacyDetails?.id)
//                startActivity(i)
            }

            R.id.edit -> {

//                val intent = Intent(this, RegisterPharmacyKActivity::class.java)
//                startActivity(intent)
            }

            R.id.select ->
                selectPharmacy()

            R.id.license_no_icon ->
                viewLicenseImage()

            R.id.license_no ->
                viewLicenseImage()
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when(v?.id){

            R.id.open_24_7 -> {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    var text = ""
                    if(open24_7){
                        text = Constants.FEATURE_24_7_ON
                    } else {
                        text = Constants.FEATURE_24_7_OFF
                    }
                    customDialog?.showHoverTextDialog(nightMode, text)
                    return true
                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    open24_7FeatureLabel.visibility = View.GONE
                    return true
                }
            }

            R.id.online_orders -> {
                if (event?.action == MotionEvent.ACTION_DOWN) {

                    var text = ""
                    if(onlineOrdersB){
                        text = Constants.FEATURE_ONLINE_ORDER_ON
                    } else {
                        text = Constants.FEATURE_ONLINE_ORDER_OFF
                    }
                    customDialog?.showHoverTextDialog(nightMode, text)
                    return true
                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    onlineOrdersLabel.visibility = View.GONE
                    return true
                }
            }

            R.id.delivery -> {
                if (event?.action == MotionEvent.ACTION_DOWN) {

                    var text = ""
                    if(delivery){
                        text = Constants.FEATURE_DELIVERY_ON
                    } else {
                        text = Constants.FEATURE_DELIVERY_OFF
                    }
                    customDialog?.showHoverTextDialog(nightMode, text)
                    return true
                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    deliveryFeatureLabel.visibility = View.GONE
                    return true
                }
            }
        }

        return false
    }

}
