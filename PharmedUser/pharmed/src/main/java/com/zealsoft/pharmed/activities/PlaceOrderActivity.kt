package com.zealsoft.pharmed.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.SystemClock
import android.text.InputFilter
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.signature.MediaStoreSignature
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.zealsoft.pharmed.R
import com.zealsoft.pharmed.Util.Constants
import com.zealsoft.pharmed.Util.Internet
import com.zealsoft.pharmed.Util.Preferences
import com.zealsoft.pharmed.Util.Utills
import com.zealsoft.pharmed.adapters.OrderItemsListKAdapter
import com.zealsoft.pharmed.apis.RestApis
import com.zealsoft.pharmed.apis.RetroClient
import com.zealsoft.pharmed.customWidgets.CustomDialog
import com.zealsoft.pharmed.models.*
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class PlaceOrderActivity : AppCompatActivity(), View.OnClickListener, View.OnTouchListener {

    private lateinit var placeOrderScreen: ConstraintLayout
    private lateinit var title: TextView
    private lateinit var back: ImageView
    private lateinit var logoToolbar: CircleImageView
    private lateinit var initials: TextView

    private lateinit var pharmacyCard: CardView
    private lateinit var pharmacyItemView: ConstraintLayout
    private lateinit var pharmacyItem: ConstraintLayout
    private lateinit var pharmacyLabel: TextView
    private lateinit var selectPharmacy: TextView
    private lateinit var pharmacyView: ConstraintLayout
    private lateinit var pharmacyLogo: CircleImageView
    private lateinit var pharmacyInitials: TextView
    private lateinit var pharmacyName: TextView
    private lateinit var pharmacyAddress: TextView
    private lateinit var pharmacyNumber: TextView
    private lateinit var pharmacyDeliveryLimit: TextView
    private lateinit var editPharmacy: ImageView
    private lateinit var actionBar: View

    private lateinit var typeCard: CardView
    private lateinit var typeItem: ConstraintLayout
    private lateinit var typeLabel: TextView
    private lateinit var orderTypesGroup: RadioGroup
    private lateinit var typePickUpRB: RadioButton
    private lateinit var typeDeliveryRB: RadioButton
    private lateinit var selectedType: RadioButton

    private lateinit var addressCard: CardView
    private lateinit var addressItem: ConstraintLayout
    private lateinit var addressLabel: TextView
    private lateinit var userAddress: TextView
    private lateinit var editAddress: ImageView

    private lateinit var orderNoteCard: CardView
    private lateinit var orderNoteItem: ConstraintLayout
    private lateinit var orderNoteLabel: TextView
    private lateinit var orderNoteET: EditText
    private lateinit var recordingSection: ConstraintLayout
    private lateinit var recordingLabel: TextView
    private lateinit var recordingDuration: TextView
    private lateinit var audioNote: ImageButton
    private lateinit var playSection: ConstraintLayout
    private lateinit var playDuration: TextView
    private lateinit var playAudioNote: ImageButton
    private lateinit var resetAudioNote: ImageButton

    private lateinit var promoCodeCard: CardView
    private lateinit var promoCodeItem: ConstraintLayout
    private lateinit var promoCodeLabel: TextView
    private lateinit var promoCodeTV: TextView
    private lateinit var promotionsList: ImageButton

    private lateinit var medicinesCard: CardView
    private lateinit var medicinesItem: ConstraintLayout
    private lateinit var editMedicines: ImageView
    private lateinit var medicinesRecyclerView: RecyclerView
    private lateinit var medicinesListAdapter: OrderItemsListKAdapter

    private lateinit var prescriptionsCard: CardView
    private lateinit var prescriptionsItem: ConstraintLayout
    private lateinit var editPrescriptions: ImageView
    private lateinit var prescriptionsRecyclerView: RecyclerView
    private lateinit var prescriptionsListAdapter: OrderItemsListKAdapter

    private lateinit var bottom: View
    private lateinit var placeOrder: Button
    private var token: String? = null

    private var order: Order? = null

    private var recorder = MediaRecorder()
    private var audioFilePath: String? = null
    private var audioFile: File? = null
    private var mPlayer = MediaPlayer()
    private val REQUEST_RECORD_AUDIO = 12
    private var mHandler = Handler()

    private var mLastClickTime: Long = 0
    private var toast: Toast? = null
    private var reOrder = false
    private var cartSent = false
    private var customDialog: CustomDialog? = null
    private var nightMode: Boolean = false
    private var userLoggedIn = false
    private var recorded = false
    private var stopped = false

    private var REQUEST_SELECT_PHARMACY = 10
    private var REQUEST_SELECT_PROMOTION = 11

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_order)

        setViews()
    }

    override fun onNewIntent(intent: Intent?) {
        if(intent != null) {
            if(intent.hasExtra(Constants.INTENT_ORDER))
                order = intent.getSerializableExtra(Constants.INTENT_ORDER) as Order?

            if (intent.hasExtra(Constants.INTENT_RE_ORDER))
                reOrder = true

            if(order != null)
                populateDetails()
        }
        super.onNewIntent(intent)
    }

    private fun setViews() {

        title = findViewById(R.id.title)
        title.text = resources.getString(R.string.title_place_order_screen)
        logoToolbar = findViewById(R.id.logo_toolbar_pharmed)
        initials = findViewById(R.id.initials_toolbar)
        back = findViewById(R.id.back)
        back.setOnClickListener(this)

        placeOrderScreen = findViewById(R.id.place_order_screen)

        pharmacyCard = findViewById(R.id.pharmacy_card)
        pharmacyItemView = findViewById(R.id.pharmacy_item_view)
        pharmacyItem = findViewById(R.id.pharmacy_item)
        pharmacyLabel = findViewById(R.id.selected_pharmacy_label)
        selectPharmacy = findViewById(R.id.select_pharmacy)
        selectPharmacy.setOnClickListener(this)
        pharmacyView = findViewById(R.id.pharmacy)
        pharmacyLogo = findViewById(R.id.icon)
        pharmacyInitials = findViewById(R.id.initials)
        pharmacyName = findViewById(R.id.address_title)
        pharmacyAddress = findViewById(R.id.address_name)
        pharmacyNumber = findViewById(R.id.number)
        pharmacyDeliveryLimit = findViewById(R.id.delivery_limit)
        actionBar  = findViewById(R.id.action_bar)
        actionBar.visibility = View.GONE

        val sendCart = findViewById<TextView>(R.id.send_cart)
        sendCart.visibility = View.INVISIBLE

        val cardViewPharmacy = findViewById<CardView>(R.id.container)
        cardViewPharmacy.cardElevation = 0F

        pharmacyNumber.visibility = View.VISIBLE
        editPharmacy = findViewById(R.id.edit_pharmacy)
        editPharmacy.setOnClickListener(this)

        typeCard = findViewById(R.id.type_card)
        typeItem = findViewById(R.id.type_item)
        typeLabel = findViewById(R.id.selected_type_label)
        orderTypesGroup = findViewById(R.id.types_radio_group)
        typePickUpRB = findViewById(R.id.type_1)
        typeDeliveryRB = findViewById(R.id.type_2)

        addressCard = findViewById(R.id.address_card)
        addressItem = findViewById(R.id.address_item)
        addressLabel = findViewById(R.id.selected_address_label)
        userAddress = findViewById(R.id.user_address)
        editAddress = findViewById(R.id.edit_address)
        editAddress.setOnClickListener(this)

        orderNoteCard = findViewById(R.id.order_note_card)
        orderNoteItem = findViewById(R.id.order_note_item)
        orderNoteLabel = findViewById(R.id.note_label)
        orderNoteET = findViewById(R.id.order_note)
        recordingSection = findViewById(R.id.recording_section)
        recordingLabel = findViewById(R.id.recording_label)
        recordingDuration = findViewById(R.id.recording_duration)
        audioNote = findViewById(R.id.audio_note)
        audioNote.setOnClickListener(this)
        playSection = findViewById(R.id.play_audio_section)
        playDuration = findViewById(R.id.play_duration)
        playDuration.setOnClickListener(this)
        playAudioNote = findViewById(R.id.play_audio_note)
        playAudioNote.setOnClickListener(this)
        resetAudioNote = findViewById(R.id.reset_audio_note)
        resetAudioNote.setOnClickListener(this)

        promoCodeCard = findViewById(R.id.promo_code_card)
        promoCodeItem = findViewById(R.id.promo_code_item)
        promoCodeLabel = findViewById(R.id.promo_code_label)
        promoCodeTV = findViewById(R.id.promo_code)
        promoCodeTV.setOnClickListener(this)
        promotionsList = findViewById(R.id.promotions_list)
        promotionsList.setOnClickListener(this)

        medicinesCard = findViewById(R.id.medicines_card)
        medicinesItem = findViewById(R.id.medicines_item)
        editMedicines = findViewById(R.id.edit_items)
        editMedicines.setOnClickListener(this)
        medicinesRecyclerView = findViewById(R.id.medicines_recycler)

        prescriptionsCard = findViewById(R.id.prescriptions_card)
        prescriptionsItem = findViewById(R.id.prescriptions_item)
        editPrescriptions = findViewById(R.id.edit_prescriptions)
        editPrescriptions.setOnClickListener(this)
        prescriptionsRecyclerView = findViewById(R.id.prescriptions_recycler)

        bottom = findViewById(R.id.bottom)
        placeOrder = findViewById(R.id.place_order)
        placeOrder.setOnClickListener(this)

        setPic()

        nightMode = Preferences.getNightThemeSelectionFromSharedPreferences(this)

        if(intent.hasExtra(Constants.INTENT_TOKEN))
            token = intent.getStringExtra(Constants.INTENT_TOKEN)

        checkTheme()
        setFieldLimits()

        customDialog = CustomDialog(this)

        if(intent.hasExtra(Constants.INTENT_ORDER))
            order = intent.getSerializableExtra(Constants.INTENT_ORDER) as Order?

        if(intent.hasExtra(Constants.INTENT_RE_ORDER))
            reOrder = true

//        validateUserCall()

        populateDetails()
    }

    private fun setPic() {

        val user = Preferences.getUserDataFromSharedPreferences(this)

        if(user != null) {

            logoToolbar.setImageResource(R.color.initials_background_color)
            initials.visibility = View.VISIBLE
            initials.text = Utills.extractInitialsUser(user.firstName, user.lastName)
            logoToolbar.borderWidth = Constants.BORDER_WIDTH
            logoToolbar.borderColor = resources.getColor(R.color.white)

            if (user?.icon != null && user?.icon != "") {

                Glide
                        .with(this)
                        .load(Utills.getCompleteUrl(user?.icon))
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, isFirstResource: Boolean): Boolean {

                                return false
                            }
                            override fun onResourceReady(resource: Drawable?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                                initials.visibility = View.GONE
                                return false
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.color.initials_background_color)
                        .error(R.color.initials_background_color)
                        .into(logoToolbar)
            } else {

            }
        }
    }

    private fun checkTheme() {

        Utills.changeNavigationBarColor(this, Constants.COLOR_GREY)

        typePickUpRB.setButtonDrawable(R.drawable.radio_button_selector)
        typeDeliveryRB.setButtonDrawable(R.drawable.radio_button_selector)

        if(nightMode){
            placeOrderScreen.setBackgroundResource(R.color.formBackgroundNightMode)
            pharmacyItemView.setBackgroundResource(R.color.editTextBackgroundNightMode)
            pharmacyItem.setBackgroundResource(R.color.editTextBackgroundNightMode)
            typeItem.setBackgroundResource(R.color.editTextBackgroundNightMode)
            addressItem.setBackgroundResource(R.color.editTextBackgroundNightMode)
            orderNoteItem.setBackgroundResource(R.color.editTextBackgroundNightMode)
            promoCodeItem.setBackgroundResource(R.color.editTextBackgroundNightMode)
            medicinesItem.setBackgroundResource(R.color.editTextBackgroundNightMode)
            prescriptionsItem.setBackgroundResource(R.color.editTextBackgroundNightMode)

            pharmacyName.setTextColor(resources.getColor(R.color.white))
            pharmacyAddress.setTextColor(resources.getColor(R.color.textColorNightMode))
            pharmacyNumber.setTextColor(resources.getColor(R.color.textColorNightMode))

            typeDeliveryRB.setTextColor(resources.getColor(R.color.textColorNightMode))
            typePickUpRB.setTextColor(resources.getColor(R.color.textColorNightMode))

            orderNoteET.setTextColor(resources.getColor(R.color.textColorNightMode))

            promoCodeTV.setTextColor(resources.getColor(R.color.textColorNightMode))

            userAddress.setTextColor(resources.getColor(R.color.textColorNightMode))
        }
    }

    private fun setFieldLimits() {

        orderNoteET.filters += InputFilter.LengthFilter(Constants.FIELD_NOTE)
    }

    private fun populateDetails() {
        if(order != null) {

            if(order?.user == null) {
                var user = User()

                if (intent.hasExtra(Constants.INTENT_DEVICE_ID))
                    user.deviceId = intent.getStringExtra(Constants.INTENT_DEVICE_ID)

                if (intent.hasExtra(Constants.INTENT_USER_ID))
                    user.id = intent.getStringExtra(Constants.INTENT_USER_ID)

                order?.user = user
            }

            userLoggedIn = order?.user != null

            setRadioGroup()
            populatePharmacy(order?.pharmacy)
            setDefaultAddress(order?.user)
            populateNote()
            populateMedicinesRecycler(order?.items)
            populatePrescriptionsRecycler(order?.prescriptions)
        }
    }

    private fun populatePharmacy(pharmacy: PharmacyDetails?) {
        if(pharmacy != null) {
            pharmacyLabel.setBackgroundResource(R.color.pdThemeBlueColorL)
            pharmacyView.visibility = View.VISIBLE
            selectPharmacy.visibility = View.GONE

            pharmacyName.text = pharmacy.placeName
            pharmacyAddress.text = pharmacy.placeAddress

            if(pharmacy.placeNumber != null)
                pharmacyNumber.text = pharmacy.placeNumber

            if (pharmacy.placeName != null) {
                pharmacyName.text = pharmacy.placeName

                pharmacyLogo.setImageResource(R.color.initials_background_color)
                pharmacyInitials.visibility = View.VISIBLE
                pharmacyInitials.text = pharmacy.placeName!!.substring(0,1).capitalize()
            }

            if (pharmacy.icon != null && pharmacy.icon != "") {

                Glide
                        .with(this)
                        .load(Utills.getCompleteUrl(pharmacy.icon))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .signature(MediaStoreSignature("", Calendar.getInstance().time.time, 0))
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, isFirstResource: Boolean): Boolean {

                                return false
                            }
                            override fun onResourceReady(resource: Drawable?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                                pharmacyInitials.visibility = View.GONE
                                return false
                            }
                        })
                        .placeholder(R.color.initials_background_color)
                        .error(R.color.initials_background_color)
                        .into(pharmacyLogo)
//
            } else {

            }

            if(!pharmacy.deliveryAvailable) {
                typePickUpRB.isChecked = true
                typeDeliveryRB.isEnabled = false
            } else {
                orderTypesGroup.clearCheck()
                typeDeliveryRB.isEnabled = true
            }

            if(pharmacy.deliveryAvailable != null && pharmacy.deliveryAvailable) {
                if (pharmacy.minOrderLimit != null && pharmacy.minOrderLimit!! > 0) {
                    pharmacyDeliveryLimit.text = "Min. Order:\n" + Utills.getCurrencyCode(pharmacy.currencySymbol) + " " + pharmacy.minOrderLimit
                    pharmacyDeliveryLimit.visibility = View.VISIBLE
                } else
                    pharmacyDeliveryLimit.visibility = View.GONE
            } else {
                pharmacyDeliveryLimit.visibility = View.GONE
            }

            if(pharmacy.pharmacyStatus == Constants.PHARMACY_STATUS_REGISTERED){
                placeOrder.text = Constants.CHECKOUT_ORDER
            } else {
                placeOrder.text = Constants.CHECKOUT_SMS
            }

//            if(userLoggedIn) {
                if (pharmacy.promotionAvailable) {
                    promoCodeCard.visibility = View.VISIBLE
                } else {
                    promoCodeCard.visibility = View.GONE
                }
//
//                audioNote.visibility = View.VISIBLE
//            } else {
//                promoCodeCard.visibility = View.GONE
//                audioNote.visibility = View.GONE
//            }
        } else {
            pharmacyView.visibility = View.GONE
            selectPharmacy.visibility = View.VISIBLE
            openSelectPharmacy()
        }

    }

    private fun openSelectPharmacy() {
        val searchPharmacy = Intent(this, SearchKActivity::class.java)
        searchPharmacy.putExtra("searchPharmacy", true)
        startActivityForResult(searchPharmacy, REQUEST_SELECT_PHARMACY)
    }

    private fun setRadioGroup(){
        orderTypesGroup.setOnCheckedChangeListener( RadioGroup.OnCheckedChangeListener { group, checkedId  ->

            try {
                val radioButton: RadioButton = findViewById(checkedId)
                var index = group.indexOfChild(radioButton)

                when (radioButton.id) {

                    R.id.type_1 -> {
                        addressCard.visibility = View.GONE
                        typeLabel.setBackgroundResource(R.color.pdThemeBlueColorL)
                    }

                    R.id.type_2 -> {

                        if (userLoggedIn)
                            addressCard.visibility = View.VISIBLE
                        else {
                            showLoginDialog(2)
                            typeDeliveryRB.isChecked = false
                        }

                        typeLabel.setBackgroundResource(R.color.pdThemeBlueColorL)
                    }
                }
            } catch (e: Exception){

            }
        })
    }

    private fun setDefaultAddress(user: User?){
        if(user != null) {
            if (user.addresses != null && user.addresses?.size!! > 0) {
                order?.userAddress = user.addresses?.get(0)
            } else {

                if (user.address != null) {
                    order?.userAddress = user.address
                    order?.user?.addresses = ArrayList()
                    order?.user?.addresses?.add(user.address!!)
                } else {
                    addressCard.visibility = View.GONE
                }
            }
            populateAddress(order?.userAddress)
        } else {
            addressCard.visibility = View.GONE

            placeOrder.text = Constants.CHECKOUT_SMS
        }
    }

    fun populateAddress(address: String?){
        if (address != null && address.length > 0) {
            addressLabel.setBackgroundResource(R.color.pdThemeBlueColorL)
            userAddress.text = address
        } else {
            addressCard.visibility = View.GONE
        }
    }

    private fun populateNote(){
        if(order?.orderNote != null && order?.orderNote != ""){
            orderNoteET.setText(order?.orderNote)
        }

        if(order?.promoCode != null && order?.promoCode != ""){
            promoCodeTV.setText(order?.promoCode)
        }
    }

    private fun populateMedicinesRecycler(medicinesList: List<MedicineDetails>?) {

        var medicines = medicinesList
        var prescriptionList: List<Prescription> = ArrayList()

        if(medicines == null) {
            medicines = ArrayList()
        }

        if(medicinesList != null && medicinesList.isNotEmpty()) {
            medicinesRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            medicinesListAdapter = OrderItemsListKAdapter(this, medicines, prescriptionList, nightMode, false, "", "", reOrder)
            medicinesRecyclerView.adapter = medicinesListAdapter
            medicinesCard.visibility = View.VISIBLE
            medicinesListAdapter.notifyDataSetChanged()
            medicinesRecyclerView.invalidate()
        } else {
            medicinesCard.visibility = View.GONE
        }
    }

    private fun populatePrescriptionsRecycler(prescriptionsList: List<Prescription>?) {

        var prescriptions = prescriptionsList
        var medicineList: List<MedicineDetails> = ArrayList()

        if(prescriptions == null)
            prescriptions = ArrayList()

        if(prescriptionsList != null && prescriptionsList.isNotEmpty()) {
            prescriptionsRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            prescriptionsListAdapter = OrderItemsListKAdapter(this, medicineList, prescriptions, nightMode, false, "", "", reOrder)
            prescriptionsRecyclerView.adapter = prescriptionsListAdapter
            prescriptionsCard.visibility = View.VISIBLE
            prescriptionsListAdapter.notifyDataSetChanged()
            prescriptionsRecyclerView.invalidate()
        } else {
            prescriptionsCard.visibility = View.GONE
        }
    }

    fun setOrderPaced() {
        cartSent = true
    }

    fun showSmsDialog(){
        if(order?.prescriptions != null && order?.prescriptions?.size!! > 0) {
            customDialog?.showGeneralDialog(nightMode, Constants.SMS_ORDER_USER_DIALOG_TITLE, Constants.SMS_ORDER_USER_DIALOG_MESSAGE,
                    Constants.SMS_ORDER_USER_DIALOG_BUTTON_1, Constants.SMS_ORDER_USER_DIALOG_BUTTON_2, null, false)
        } else {
            sendSmsOrder()
        }
    }

    fun sendSmsOrder(){
        placeOrder(true, Constants.ORDER_PICK_UP, false, "")
        setOrderPaced()
        Utills.sendSms(this, order?.pharmacy?.placeNumber, Utills.makeMsgFromCart(order?.items, orderNoteET.text.toString()))
    }

    private fun sendAudioNoteCall(send: Boolean, orderType: String, goHome: Boolean, address: String){

        val restApis = RetroClient.getClient().create(RestApis::class.java)

        if(audioFile != null) {

            val audioNote = RequestBody.create(MediaType.parse("audio/*"), audioFile)
            val audioNoteBody = MultipartBody.Part.createFormData("audioNote", audioFile!!.name, audioNote)


            val sendAttachmentsCall = restApis.sendAudioNote(Preferences.getAuthCodeFromSharedPreferences(this),
                    audioNoteBody)
            sendAttachmentsCall.enqueue(object : Callback<GeneralResponse> {
                override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                    if (response.isSuccessful) {
                        val generalResponse = response.body()
                        if (generalResponse != null && generalResponse.isSuccess && generalResponse.results.audioNoteURL != null
                                && generalResponse.results.audioNoteURL.isNotEmpty()) {

                            order?.audioNoteURL = generalResponse.results.audioNoteURL

                            placeOrder(send, orderType, goHome, address)
                        } else {
                            customDialog?.dismissLoadingDialogue()
                        }
                    } else {
                        customDialog?.dismissLoadingDialogue()
                        if (response.code() == 400) {
                            val gson: Gson = GsonBuilder().create()
                            var mError = GeneralResponse()
                            try {
                                mError = gson.fromJson(response.errorBody()!!.string(), GeneralResponse::class.java)
                                val authorization = response?.headers()?.get(Constants.HEADER_AUTH)

                                Utills.invalidTokenLogout(this@PlaceOrderActivity, mError.msg, authorization)
                            } catch (e: IOException) {
                                // handle failure to read error
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    customDialog?.dismissLoadingDialogue()
                    showToast(Constants.CONNECTION_PROBLEM)
                }
            })
        } else {
            placeOrder(send, orderType, goHome, address)
        }
    }

    fun placeOrder(send: Boolean, orderType: String, goHome: Boolean, address: String){
        if (send) {
            if (Internet.isAvailable(this)) {

                customDialog?.showLoadingDialogue()

                var userId = ""

                var user = Preferences.getUserDataFromSharedPreferences(this)

                var checkoutParams = CheckoutParams()

                if (Preferences.getUserDataFromSharedPreferences(this) != null) {
                    userId = order?.user?.id!!
//                    checkoutParams.message = "Order Placed by " + user.firstName + " " + user.lastName
                } else {
                    userId = "-1"
                    checkoutParams.message = "New Order Placed"
                }

                checkoutParams.placeLat = "31.510294"
                checkoutParams.placeLng = "74.350017"
                checkoutParams.pharmacyId = order?.pharmacy?.id
                checkoutParams.order = order
                checkoutParams.orderNote = orderNoteET.text.toString()
                if(userLoggedIn) {
                    if(promoCodeTV.text.toString().isNotEmpty()) {
                        checkoutParams.promoCode = promoCodeTV.text.toString()
                    }

                    if(order?.audioNoteURL != null && order?.audioNoteURL!!.isNotEmpty()){
                        checkoutParams.audioNoteURL = order?.audioNoteURL
                    }
                }

                if(address != null && address != "")
                    checkoutParams.userAddress = address
                else {
                    var user = Preferences.getUserDataFromSharedPreferences(this)
                    if(user?.addresses != null && user.addresses?.size!! > 0)
                        checkoutParams.userAddress = user.addresses?.get(0)
                    else{
                        if(user?.address != null)
                            checkoutParams.userAddress = user.address
                    }
                }

                if (orderType == "Pick Up")
                    checkoutParams.orderType = Constants.ORDER_PICK_UP
                else if (orderType == "Delivery")
                    checkoutParams.orderType = Constants.ORDER_DELIVERY


                val restApis = RetroClient.getClient().create(RestApis::class.java)

                checkoutParams.userId = userId
                checkoutParams.deviceId = order?.user?.deviceId

                var placeOrderCall = restApis.checkoutCall(Preferences.getAuthCodeFromSharedPreferences(this),
                        Constants.HEADER_CONTENT_TYPE_VALUE, checkoutParams)

                if(reOrder){
                    placeOrderCall = restApis.reOrderCall(Preferences.getAuthCodeFromSharedPreferences(this), Constants.HEADER_CONTENT_TYPE_VALUE,
                            checkoutParams)
                }

                placeOrderCall.enqueue(object : Callback<GeneralResponse> {
                    override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                        if (response.isSuccessful) {
                            customDialog?.dismissLoadingDialogue()
                            if (response.body() != null && response.body()!!.isSuccess) {
                                customDialog?.dismissLoadingDialogue()
//                                showDialog()
                                if (goHome) {
                                    removeAllFromCart()
                                    showToast("Order Placed")
                                }
                            }
                        } else {
                            if (response.code() == 400) {
                                val gson: Gson = GsonBuilder().create()
                                var mError = GeneralResponse()
                                try {
                                    mError = gson.fromJson(response.errorBody()!!.string(), GeneralResponse::class.java)
                                    val authorization = response?.headers()?.get(Constants.HEADER_AUTH)

                                    Utills.invalidTokenLogout(this@PlaceOrderActivity, mError.msg, authorization)
                                } catch (e: IOException) {
                                    // handle failure to read error
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                        customDialog?.dismissLoadingDialogue()
                        showToast(Constants.CONNECTION_PROBLEM)
                    }
                })
            } else {

            }
        }
    }

    fun showLoginDialog (type: Int) {
        when (type) {
            1 -> {
                customDialog?.showGeneralDialog(nightMode, Constants.REGISTER_DIALOG_TITLE, Constants.REGISTER_SMS_DIALOG_MESSAGE,
                        Constants.REGISTER_DIALOG_BUTTON_1, Constants.REGISTER_DIALOG_BUTTON_2, Constants.REGISTER_DIALOG_BUTTON_3, true)
            }
            2 -> {
                customDialog?.showGeneralDialog(nightMode, Constants.REGISTER_DIALOG_TITLE, Constants.REGISTER_DELIVERY_DIALOG_MESSAGE,
                        Constants.REGISTER_DIALOG_BUTTON_1, Constants.REGISTER_DIALOG_BUTTON_2, null, true)
            }
            4 -> {
                customDialog?.showGeneralDialog(nightMode, Constants.REGISTER_DIALOG_TITLE, Constants.REGISTER_AUDIO_NOTE_DIALOG_MESSAGE,
                        Constants.REGISTER_DIALOG_BUTTON_1, Constants.REGISTER_DIALOG_BUTTON_2, null, true)
            }
            5 -> {
                customDialog?.showGeneralDialog(nightMode, Constants.REGISTER_DIALOG_TITLE, Constants.REGISTER_PROMO_DIALOG_MESSAGE,
                        Constants.REGISTER_DIALOG_BUTTON_1, Constants.REGISTER_DIALOG_BUTTON_2, null, true)
            }
            else -> {
                customDialog?.showGeneralDialog(nightMode, Constants.REGISTER_DIALOG_TITLE, Constants.REGISTER_DIALOG_MESSAGE,
                        Constants.REGISTER_DIALOG_BUTTON_1, Constants.REGISTER_DIALOG_BUTTON_2, null, true)
            }
        }
    }

    fun removeAllFromCart() {

//        customDialog?.dismissCartCleaningDialogue()

        Preferences.removeCartItemsFromSharedPreferences(this)
        Preferences.removePrescriptionImagePathsFromSharedPreferences(this)
        Preferences.removeSelectedPharmacyFromSharedPreferences(this)


//        Intent goHome = new Intent(context, MapActivity.class)
        var intent = Intent(this, OrdersUserActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra(Constants.INTENT_ORDERS, true)
        startActivity(intent)

//        Utills.goToHome(this)
    }

    fun proceedToOrder(){

        if (order?.pharmacy?.pharmacyStatus != Constants.PHARMACY_STATUS_REGISTERED) {
                            showSmsDialog()
        } else {
            if (Preferences.getUserDataFromSharedPreferences(this) != null) {
                val selectedId: Int = orderTypesGroup.checkedRadioButtonId
                selectedType = findViewById(selectedId)

                if(audioFile != null) {
                    sendAudioNoteCall(true, selectedType.text.toString(), true, userAddress.text.toString())
                } else {
                    placeOrder(true, selectedType.text.toString(), true, userAddress.text.toString())
                }
            } else {
//                val location = LatLng(order?.pharmacy?.placeLat!!.toDouble(), order?.pharmacy?.placeLng!!.toDouble())
//                if (Utills.isValidLandlineNumber(this, location, order?.pharmacy?.placeNumber)) {
//                    showLoginDialog(3)
//                } else {
//                    showLoginDialog(1)
//                }
            }
        }

    }

    private fun showSelectAddressDialog() {

        var user = Preferences.getUserDataFromSharedPreferences(this)

        if(user != null) {
            if(user.addresses == null || user.addresses?.size!! < 1){
                user.addresses = ArrayList()
                if(user.address != null)
                    user.addresses?.add(user.address!!)
                else
                    showToast("No address found.")
            }

            customDialog?.showConfirmOrderDialog(nightMode, "", "", "", "", true, Preferences.getUserDataFromSharedPreferences(this).addresses)
        }
    }

    private fun openCartForEditing(){

        val editCart = Intent(this, CartKActivity::class.java)

        if(reOrder) {

            clearCart()

            if (order?.items != null)
                Preferences.addCartItemsToSharedPreferences(this, order?.items)

            if (order?.prescriptions != null) {
                for (prescription in order?.prescriptions!!) {
                    prescription.urlB = true
                }
                Preferences.addPrescriptionImagePathsToSharedPreferences(this, order?.prescriptions)
            }

            if (order?.pharmacy != null)
                Preferences.addSelectedPharmacyToSharedPreferences(this, order?.pharmacy)

            editCart.putExtra(Constants.INTENT_RE_ORDER, true)
        } else {
            editCart.putExtra(Constants.INTENT_EDIT_CART, true)
        }

        startActivity(editCart)
    }

    private fun clearCart(){
        Preferences.removeCartItemsFromSharedPreferences(this)
        Preferences.removePrescriptionImagePathsFromSharedPreferences(this)
        Preferences.removeSelectedPharmacyFromSharedPreferences(this)
    }

    private fun startRecording(fileName: String) {

        val direct = File(Environment.getExternalStorageDirectory().toString() + "/Pharmed")

        if (!direct.exists()) {
            val mainDirectory = File("/sdcard/Pharmed/")
            mainDirectory.mkdirs()
        }

        val subDirect = File(Environment.getExternalStorageDirectory().toString() + "/Pharmed/AudioNote")

        if (!subDirect.exists()) {
            val subDirectory = File("/sdcard/Pharmed/AudioNote")
            subDirectory.mkdirs()
        }

        val file = File(File("/sdcard/Pharmed/AudioNote"), fileName)
        if (file!!.exists()) {
            file!!.delete()
        }

        try {

//            val compressedImage = Compressor(this)
//                    .setMaxWidth(700)
//                    .setMaxHeight(700)
//                    .setQuality(75)
//                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
//                    .setDestinationDirectoryPath("/sdcard/Pharmed/AudioNote/")
//                    .compressToFile(originalFile)

//            val suc = compressedImage.renameTo(file)

//            prescription.path = "/sdcard/Pharmed/AudioNote/$fileName"
//            prescription.select = false

//            files?.add(file)
//            prescriptionList?.add(prescription)
//            Preferences.addPrescriptionImagePathsToSharedPreferences(this, prescriptionList)
//
//            updateCartBoolean()
//
//            setUploadButton()
//
//            populateList()

            audioFilePath = "/sdcard/Pharmed/AudioNote/$fileName"


            recorder = MediaRecorder()
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            recorder.setOutputFile(audioFilePath)
            recorder.prepare()
            recorder.start()
            startTimer(true)

            recordingSection.visibility = View.VISIBLE

            recorded = true



//            recorder = AudioRecord(MediaRecorder.AudioSource.MIC,
//                    RECORDER_SAMPLERATE, AudioFormat.CHANNEL_IN_MONO, RECORDER_AUDIO_ENCODING, bufferSize)
//
//            recorder.startRecording()


        } catch (e: Exception) {
            e.printStackTrace()

        }

    }

    private fun startTimer(recordingTimer: Boolean) {

        var duration = "0".toLong()

        if(mPlayer != null && !recordingTimer) {
            try {
                duration = mPlayer.duration.toLong()
            } catch (e: java.lang.Exception) {

            }
        }

        runOnUiThread(object : Runnable {
            override fun run() {
                if (mPlayer != null) {
                    if(recordingTimer) {
                        recordingDuration.text = Utills.formatMilliSeconds(duration)
                        duration += 1000

                        if(duration.toInt() >= 180000){
                            stopRecording()
                        }
                    } else {
                        playDuration.text = Utills.formatMilliSeconds(duration - mPlayer.currentPosition + 1000)
                    }
                }
                mHandler.postDelayed(this, 1000)
            }
        })
    }

    private fun setMPListener(){

        mPlayer.setOnCompletionListener {

                stopPlaying()
        }
    }

    private fun stopRecording(){

        if(recorder != null) {
            try {
                recorder.stop()
                recorder.release()

                audioFile = File(audioFilePath)

                stopHandler()
                startPlaying()
                recordingSection.visibility = View.GONE
                audioNote.visibility = View.GONE
                playSection.visibility = View.VISIBLE
                stopped = true
                recorded = true

            } catch (e: IOException) {
//            Log.e(LOG_TAG, "prepare() failed")
            }
        }
    }

    private fun startPlaying() {

        if(audioFilePath != null && audioFilePath != "") {
            try {
//                if(mPlayer != null && mPlayer.isPlaying) {
//                    mPlayer.stop()
//                    mPlayer.release()
//                } else {
                    mPlayer = MediaPlayer()
                    mPlayer.setDataSource(audioFilePath)
                    mPlayer.prepare()
                mPlayer.start()
                setMPListener()
                startTimer(false)
                playAudioNote.setImageResource(R.drawable.icon_stop)
                stopped = false
//                }
            } catch (e: IOException) {
//            Log.e(LOG_TAG, "prepare() failed")
            }
        }
    }

    private fun stopPlaying() {
        if(mPlayer != null) {
            mPlayer.stop()
//            mPlayer.release()
            playAudioNote.setImageResource(R.drawable.icon_play)
            stopped = true
            playDuration.text = "00:00"
            stopHandler()
//        mPlayer =null
        }
    }

    private fun stopHandler(){
        if(mHandler != null)
            mHandler.removeCallbacksAndMessages(null)
    }

    private fun checkPlayStopAudio() {
        if(recorded) {
            if (audioFilePath != null && audioFilePath != "")
                if(stopped){
                    startPlaying()
                } else {
                    stopPlaying()
                }
        }
    }

    private fun callPromoCodeSelect(){

//        if(order != null && order?.pharmacy != null) {
//            val i = Intent(this, PromotionsListActivity::class.java)
//            i.putExtra(Constants.INTENT_PHARMACY_ID, order?.pharmacy?.id)
//            i.putExtra(Constants.INTENT_PROMOTION_SELECT, true)
//            startActivityForResult(i, REQUEST_SELECT_PROMOTION)
//        }
    }

    private fun askRecordingPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_RECORD_AUDIO)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        var result = false
        var audioPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED
        var storagePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED

        if (grantResults.size > 0 && audioPermission && storagePermission)
            result = true
        when (requestCode) {

            REQUEST_RECORD_AUDIO ->

                if (result) {
                    startRecording(Utills.getAudioNoteFileName())
                } else {

                }
        }
    }


    fun showToast(text: String) {

        if (toast != null) {
            toast?.cancel()
        }

        toast = Toast.makeText(this, text, Toast.LENGTH_LONG)

        val layout = layoutInflater.inflate(R.layout.custom_toast_design,
                findViewById<ViewGroup>(R.id.toast_root))


        val toastTextView = layout.findViewById(R.id.toast_text) as TextView
        toastTextView.text = text

        toast?.duration = Toast.LENGTH_LONG
        toast?.view = layout
        toast?.show()
    }

    override fun onResume() {

        populateNote()
        populateMedicinesRecycler(order?.items)
        populatePrescriptionsRecycler(order?.prescriptions)

        if (cartSent) {
            removeAllFromCart()
        }

        super.onResume()
    }

    override fun onPause() {

        order?.orderNote = orderNoteET.text.toString()

        if(userLoggedIn)
            order?.promoCode = promoCodeTV.text.toString()

        super.onPause()
    }

    override fun onDestroy() {

        if(reOrder)
            clearCart()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when(requestCode){
            REQUEST_SELECT_PHARMACY ->{
                if(resultCode == Activity.RESULT_OK){
                    order?.pharmacy = data?.getSerializableExtra(Constants.INTENT_SELECTED_PHARMACY) as PharmacyDetails

                    Preferences.addSelectedPharmacyToSharedPreferences(this, order?.pharmacy)
                    populatePharmacy(order?.pharmacy)
                } else {

                }
            }

            REQUEST_SELECT_PROMOTION -> {
                if(resultCode == Activity.RESULT_OK) {
                    order?.promoCode = data?.getStringExtra(Constants.INTENT_PROMOTION_CODE)
                    promoCodeTV.text = order?.promoCode
                    promotionsList.setImageResource(R.drawable.icon_reset)
                } else {

                }
            }

            Constants.INTENT_USER_SIGN_IN -> {
                if (resultCode == Activity.RESULT_OK) {
                    finish()
                } else {

                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.audio_note -> {

                if(userLoggedIn) {
                    if (!recorded) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                                ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                            askRecordingPermission()
                        } else {
                            startRecording(Utills.getAudioNoteFileName())
                            audioNote.setImageResource(R.drawable.icon_mic_on)
                        }
                    } else {
                        stopRecording()
                        audioNote.setImageResource(R.drawable.icon_mic_off)
                    }
                } else {
                    showLoginDialog(4)
                }
            }

            R.id.play_duration -> {
                checkPlayStopAudio()
            }

            R.id.play_audio_note -> {
                checkPlayStopAudio()
            }

            R.id.reset_audio_note -> {
                stopPlaying()
                audioFilePath = ""
                audioFile = null
                recorded = false
                audioNote.visibility = View.VISIBLE
                playSection.visibility = View.GONE
            }

            else -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return
                }
                mLastClickTime = SystemClock.elapsedRealtime()

                performClick(v)
            }
        }
    }

    private fun performClick(v: View) {
        when (v.id) {

            R.id.back ->
                finish()

            R.id.edit_pharmacy ->
                openSelectPharmacy()

            R.id.select_pharmacy ->
                openSelectPharmacy()

            R.id.edit_address ->
                showSelectAddressDialog()

            R.id.edit_items -> {
                openCartForEditing()
            }

            R.id.edit_prescriptions ->{
                 openCartForEditing()
            }

            R.id.promo_code -> {

                if(userLoggedIn) {
                    if (order?.promoCode == null || order?.promoCode!!.isEmpty()) {
                        callPromoCodeSelect()
                    }
                } else {
                    showLoginDialog(5)
                }
            }

            R.id.promotions_list -> {

                if(userLoggedIn) {
                    if (order?.promoCode != null && order?.promoCode!!.isNotEmpty()) {
                        order?.promoCode = ""
                        promoCodeTV.text = ""
                        promotionsList.setImageResource(R.drawable.icon_action_promotion)
                    } else {
                        callPromoCodeSelect()
                    }
                } else {
                    showLoginDialog(5)
                }
            }

            R.id.place_order -> {

                if(order?.pharmacy != null){
                    if(typeDeliveryRB.isChecked || typePickUpRB. isChecked){
                        if(typeDeliveryRB.isChecked){
                            if(userAddress.text.toString() != ""){
                                proceedToOrder()
                            } else {
                                showToast("Please select the address for delivery")
                                addressLabel.setBackgroundResource(R.color.red)
                            }
                        } else {
                            proceedToOrder()
                        }
                    } else {
                        showToast("Please select the order type")
                        typeLabel.setBackgroundResource(R.color.red)
                    }
                } else {
                    showToast("Please select pharmacy")
                    pharmacyLabel.setBackgroundResource(R.color.red)
                }
            }
        }
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when(v?.id){
            R.id.audio_note -> {
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    if(!recorded) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                                ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                            askRecordingPermission()
                        } else {
                            startRecording(Utills.getAudioNoteFileName())
                            audioNote.setImageResource(R.drawable.icon_mic_on)
                        }
                    }
                }
                if (event?.action == MotionEvent.ACTION_UP) {
                    if(recorded && !stopped) {
                        stopRecording()
                        audioNote.setImageResource(R.drawable.icon_mic_off)
                    }
                }
            }

            else -> {
                stopRecording()
            }
        }

        return false
    }
}