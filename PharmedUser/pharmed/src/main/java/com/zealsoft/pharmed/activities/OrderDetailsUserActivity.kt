package com.zealsoft.pharmed.activities

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class OrderDetailsUserActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var orderDetailScreen: ConstraintLayout
    private lateinit var title: TextView
    private lateinit var cartCount: TextView
    private lateinit var back: ImageView
    private lateinit var cartToolbar: ImageView
    private lateinit var logoToolbar: CircleImageView
    private lateinit var initialsToolbar: TextView
    private lateinit var detailsTile: ConstraintLayout
    private lateinit var pharmacyIcon: CircleImageView
    private lateinit var initials: TextView
    private lateinit var dateTime: TextView
    private lateinit var time: TextView
    private lateinit var idLabel: TextView
    private lateinit var orderId: TextView
    private lateinit var countLabel: TextView
    private lateinit var itemsCount: TextView
    private lateinit var divider1: View
    private lateinit var amountLabel: TextView
    private lateinit var amount: TextView
    private lateinit var deliveryChargesLabel: TextView
    private lateinit var deliveryCharges: TextView
    private lateinit var totalLabel: TextView
    private lateinit var totalAmount: TextView
    private lateinit var pharmacyName: TextView
    private lateinit var pharmacyAddress: TextView
    private lateinit var orderStatus: TextView
    private lateinit var promoCodeCard: CardView
    private lateinit var promoCodeItem: ConstraintLayout
    private lateinit var promoCode: TextView
    private lateinit var orderNoteSection: View
    private lateinit var orderNotesView: ConstraintLayout
    private lateinit var orderNoteLabel: TextView
    private lateinit var orderNote: TextView
    private lateinit var audioNoteSection: ConstraintLayout
    private lateinit var playAudioNote: TextView
    private lateinit var audioNoteIcon: ImageButton
    private lateinit var audioNoteDuration: TextView
    private lateinit var notesSection: View
    private lateinit var notesView: ConstraintLayout
    private lateinit var noteLabel: TextView
    private lateinit var note: TextView
    private lateinit var orderItemsRecycler: RecyclerView
    private lateinit var loadingProgress: ProgressBar
    private lateinit var orderState: View
    private lateinit var orderStatusBar: ProgressBar
    private lateinit var pendingPoint: CircleImageView
    private lateinit var pendingLabel: TextView
    private lateinit var confirmationPoint: CircleImageView
    private lateinit var confirmationLabel: TextView
    private lateinit var processPoint: CircleImageView
    private lateinit var processLabel: TextView
    private lateinit var enRoutePoint: CircleImageView
    private lateinit var enRouteLabel: TextView
    private lateinit var completePoint: CircleImageView
    private lateinit var completeLabel: TextView
    private lateinit var orderDetailsCall: Call<GeneralResponse>
    private lateinit var orderPrescriptionListCall: Call<GeneralResponse>
    private lateinit var orderNoteCall: Call<GeneralResponse>
    private lateinit var restApis: RestApis
    private lateinit var bottom: View
    private lateinit var accept: Button
    private lateinit var cancel: Button
    private var orderItemsAdapter: OrderItemsListKAdapter? = null
    private var orderUserDetails: Order? = null
    private var nightMode = false
    private var customDialog: CustomDialog? = null
    private var toast: Toast? = null
    private var statusChanged = false
    private var position = -1
    private var mLastClickTime: Long = 0
    private var mPlayer = MediaPlayer()
    private var playing = false
    private val mHandler = Handler()

    var orderItemsReceived = false
    var orderPrescriptionsReceived = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details_user)

        setViews()
    }

    private fun setViews(){

        orderDetailScreen = findViewById(R.id.order_details_user_screen)
        back = findViewById(R.id.back)
        back.setOnClickListener(this)
        title = findViewById(R.id.title)
        title.text = resources.getString(R.string.title_order_detail_screen)
        logoToolbar = findViewById(R.id.logo_toolbar_pharmed)
        initialsToolbar = findViewById(R.id.initials_toolbar)

        detailsTile = findViewById(R.id.order_details)
        pharmacyIcon = findViewById(R.id.icon)
        initials = findViewById(R.id.initials)
        dateTime = findViewById(R.id.date_time)
        time = findViewById(R.id.time)
        idLabel = findViewById(R.id.id_label)
        orderId = findViewById(R.id.order_id)
        countLabel = findViewById(R.id.items_label)
        itemsCount = findViewById(R.id.items_count)
        divider1 = findViewById(R.id.divider_1)
        amountLabel = findViewById(R.id.amount_label)
        amount = findViewById(R.id.amount)
        deliveryChargesLabel = findViewById(R.id.delivery_charges_label)
        deliveryCharges = findViewById(R.id.delivery_charges)
        totalLabel = findViewById(R.id.total_label)
        totalAmount = findViewById(R.id.total_amount)
        pharmacyName = findViewById(R.id.pharmacy_name)
        pharmacyAddress = findViewById(R.id.pharmacy_address)
        orderStatus = findViewById(R.id.order_status)
        orderItemsRecycler = findViewById(R.id.ordered_medicines)
        loadingProgress = findViewById(R.id.loading_progress)
        promoCodeCard = findViewById(R.id.promo_code_card)
        promoCodeItem = findViewById(R.id.promo_code_item)
        promoCode = findViewById(R.id.promo_code)
        orderNoteSection = findViewById(R.id.order_note_section)
        orderNotesView = orderNoteSection.findViewById(R.id.notes)
        orderNoteLabel = orderNoteSection.findViewById(R.id.note_label)
        orderNoteLabel.text = Constants.NOTE_ORDER
        orderNoteLabel.setTextColor(resources.getColor(R.color.orderNoteColor))
        orderNote = orderNoteSection.findViewById(R.id.note)
        playAudioNote = orderNoteSection.findViewById(R.id.play_audio_note)
        playAudioNote.setOnClickListener(this)
        audioNoteSection = orderNoteSection.findViewById(R.id.audio_note_section)
        audioNoteSection.setOnClickListener(this)
        audioNoteIcon = orderNoteSection.findViewById(R.id.audio_note)
        audioNoteDuration = orderNoteSection.findViewById(R.id.duration)

        notesSection = findViewById(R.id.note_section)
        notesView = notesSection.findViewById(R.id.notes)
        noteLabel = notesSection.findViewById(R.id.note_label)
        noteLabel.text = Constants.NOTE_REJECT
        noteLabel.setTextColor(resources.getColor(R.color.cancellationNoteColor))
        note = notesSection.findViewById(R.id.note)
        bottom = findViewById(R.id.bottom)
        accept = findViewById(R.id.accept)
        accept.setOnClickListener(this)
        cancel = findViewById(R.id.cancel)
        cancel.setOnClickListener(this)
        customDialog = CustomDialog(this)

        orderState = findViewById(R.id.order_state)
        orderStatusBar = findViewById(R.id.order_status_bar)
        pendingPoint = findViewById(R.id.pending_state)
        pendingLabel = findViewById(R.id.pending_label)
        confirmationPoint = findViewById(R.id.user_confirm_state)
        confirmationLabel = findViewById(R.id.user_confirm_label)
        processPoint = findViewById(R.id.process_state)
        processLabel = findViewById(R.id.process_label)
        enRoutePoint = findViewById(R.id.en_route_state)
        enRouteLabel = findViewById(R.id.en_route_label)
        completePoint = findViewById(R.id.complete_state)
        completeLabel = findViewById(R.id.complete_label)

        restApis = RetroClient.getClient().create(RestApis::class.java)

        nightMode = Preferences.getNightThemeSelectionFromSharedPreferences(this)

        checkTheme()

        setPic()

        if(intent.hasExtra(Constants.ORDER_DETAILS))
            orderUserDetails = intent.getSerializableExtra(Constants.ORDER_DETAILS) as Order

        if(orderUserDetails != null)
            populateViews()

        if(intent.hasExtra(Constants.PUSH_NOTIFICATION_ORDER_USER_DETAILS)){
            if(orderUserDetails == null)
                orderUserDetails = Order()

            orderUserDetails?.id = intent.getStringExtra(Constants.PUSH_NOTIFICATION_ORDER_USER_DETAILS)
            getOrderDetailsCall(true)
        }

        position = intent.getIntExtra(Constants.ORDER_POSITION, -1)


        if(orderUserDetails?.prescriptions == null || orderUserDetails?.prescriptions!!.isEmpty())
            orderUserDetails?.prescriptions = ArrayList()

//        validateUserCall()
    }

    private fun checkTheme() {
        if(nightMode){
            orderDetailScreen.setBackgroundResource(R.color.formBackgroundNightMode)
            detailsTile.setBackgroundResource(R.color.editTextBackgroundNightMode)
            dateTime.setTextColor(resources.getColor(R.color.textColorNightMode))
            time.setTextColor(resources.getColor(R.color.textColorNightMode))
            idLabel.setTextColor(resources.getColor(R.color.textColorNightMode))
            orderId.setTextColor(resources.getColor(R.color.white))
            countLabel.setTextColor(resources.getColor(R.color.textColorNightMode))
            itemsCount.setTextColor(resources.getColor(R.color.white))
            amountLabel.setTextColor(resources.getColor(R.color.textColorNightMode))
            amount.setTextColor(resources.getColor(R.color.white))
            deliveryChargesLabel.setTextColor(resources.getColor(R.color.textColorNightMode))
            deliveryCharges.setTextColor(resources.getColor(R.color.white))
            totalLabel.setTextColor(resources.getColor(R.color.textColorNightMode))
            totalAmount.setTextColor(resources.getColor(R.color.white))
            pharmacyName.setTextColor(resources.getColor(R.color.white))
            pharmacyAddress.setTextColor(resources.getColor(R.color.white))
            orderStatus.setTextColor(resources.getColor(R.color.white))
            orderStatus.setBackgroundResource(R.drawable.pd_background_et_round_night_mode)
            orderNotesView.setBackgroundResource(R.color.editTextBackgroundNightMode)
            orderNote.setTextColor(resources.getColor(R.color.textColorNightMode))
            notesView.setBackgroundResource(R.color.editTextBackgroundNightMode)
            note.setTextColor(resources.getColor(R.color.white))
            Utills.changeNavigationBarColor(this, Constants.COLOR_BLACK)
        } else {
            Utills.changeNavigationBarColor(this, Constants.COLOR_FORM)
        }
    }

    private fun setPic(){

        val user = Preferences.getUserDataFromSharedPreferences(this)

        if(user != null){

            logoToolbar.setImageResource(R.color.initials_background_color)
            initialsToolbar.visibility = View.VISIBLE
            initialsToolbar.text = Utills.extractInitialsUser(user.firstName, user.lastName)
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

                                initialsToolbar.visibility = View.GONE
                                return false
                            }
                        })
                        .placeholder(R.color.initials_background_color)
                        .error(R.color.initials_background_color)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(logoToolbar)
            } else {
//                    iv_user.setImageResource(picturePlaceHolderImage)
            }
        }
    }

    private val broadCastReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action

            Preferences.addOrdersCountChangeBooleanToSharedPreferences(this@OrderDetailsUserActivity, true)
            if(action == Constants.PUSH_NOTIFICATION_ORDER_USER_DETAILS){

                if(orderUserDetails?.id == intent?.getStringExtra(Constants.INTENT_NOTIFICATION_ORDER))
                    getOrderDetailsCall(true)
            }
        }
    }


    private fun getOrderDetailsCall(showLoading: Boolean) {

        if (Internet.isAvailable(this)) {

//            customDialog?.showLoadingDialogue()
            if(showLoading)
                loadingProgress.visibility = View.VISIBLE

            var params = Params()

            params.orderId = orderUserDetails?.id

            orderDetailsCall = restApis.getOrderDetails(Preferences.getAuthCodeFromSharedPreferences(this),
                    Constants.HEADER_CONTENT_TYPE_VALUE, params)
            orderDetailsCall.enqueue(object : Callback<GeneralResponse> {
                override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                    if (response.isSuccessful) {
                        loadingProgress.visibility = View.GONE
                        if (response.body() != null && response.body()!!.isSuccess) {
                            orderItemsReceived = true
                            if (response.body()!!.results != null && response.body()!!.results.order != null) {
                                orderUserDetails = response.body()!!.results.order
                                if (orderUserDetails?.items != null && orderUserDetails?.items!!.isNotEmpty()) {

                                } else {

                                }
                                populateViews()
                            } else {
                                orderUserDetails?.items = ArrayList()
                            }
                        }
                    } else {
                        if (response.code() == 400) {
                            val gson: Gson = GsonBuilder().create()
                            var mError = GeneralResponse()
                            try {
                                mError = gson.fromJson(response.errorBody()!!.string(), GeneralResponse::class.java)
                                val authorization = response?.headers()?.get(Constants.HEADER_AUTH)

                                Utills.invalidTokenLogout(this@OrderDetailsUserActivity, mError.msg, authorization)
                            } catch (e: IOException) {
                                // handle failure to read error
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    if (t.message != "Canceled") {
                        loadingProgress.visibility = View.GONE
                        showToast(Constants.CONNECTION_PROBLEM)
                    }
                }
            })

        } else {
            showToast(Constants.NO_INTERNET_CONNECTION)
        }
    }

    private fun cancelOrderDetailsCall() {
        if (::orderDetailsCall.isInitialized && orderDetailsCall != null && orderDetailsCall.isExecuted) {
            orderDetailsCall.cancel()
        }
    }

    private fun showOrderList(){
        if(orderItemsReceived && orderPrescriptionsReceived) {
            loadingProgress.visibility = View.GONE
            populateViews()
        }

    }

    fun confirmOrder(changeType: Boolean) {
        changeOrderStatusCall(Constants.STATUS_IN_PROGRESS, "", changeType)
    }

    private fun showCancelOrderDialog(){
        customDialog = CustomDialog(this)

        var button1 = Constants.CANCEL_ORDER_USER_DIALOG_BUTTON_1

        if(orderUserDetails?.orderStatus == Constants.STATUS_CONFIRMATION && orderUserDetails?.deliveryCharges != null
                && orderUserDetails?.deliveryCharges!! > 0)
            button1 = Constants.CANCEL_ORDER_USER_DIALOG_BUTTON_PICKUP

        customDialog?.showGeneralDialog(nightMode, Constants.CANCEL_ORDER_USER_DIALOG_TITLE, Constants.CANCEL_ORDER_USER_DIALOG_MESSAGE,
                button1, Constants.CANCEL_ORDER_USER_DIALOG_BUTTON_2, null, true)
    }

    fun cancelOrder(reason: String){
        changeOrderStatusCall(Constants.STATUS_CANCELLED_CONFIRMATION, reason, false)
    }

    private fun changeOrderStatusCall(status: String, reason: String, changeType: Boolean) {

        if (Internet.isAvailable(this)) {

            customDialog?.showLoadingDialogue()

            var cancellationNote = CancellationNote()

            if(status == Constants.STATUS_CANCELLED_CONFIRMATION)
                cancellationNote.message = Utills.getUserCancellationMsg(Preferences.getUserDataFromSharedPreferences(this), status)
            else
                cancellationNote.message = Utills.getUserAcceptanceMsg(Preferences.getUserDataFromSharedPreferences(this), status)

            cancellationNote.typeChange = changeType

            if(Preferences.getFcmTokenFromSharedPreferences(this) != null)
                cancellationNote.fcmToken = Preferences.getFcmTokenFromSharedPreferences(this)

            cancellationNote.orderId = orderUserDetails?.id
            cancellationNote.orderStatus = status

            val changeStatusCall = restApis.changeOrderStatus(Preferences.getAuthCodeFromSharedPreferences(this),
                    Constants.HEADER_CONTENT_TYPE_VALUE, cancellationNote)
            changeStatusCall.enqueue(object : Callback<GeneralResponse> {
                override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                    if (response.isSuccessful) {
                        customDialog?.dismissLoadingDialogue()
                        if (response.body() != null && response.body()!!.isSuccess && response.body()!!.msg != null) {

                                if(response.body()!!.results != null && response.body()!!.results.order != null){
                                    orderUserDetails = response.body()!!.results.order
                                }
//                            }
                            populateViews()
                        } else {
                            showToast(Constants.UPDATE_STATUS_FAILED)
                        }
                    } else {
                        if (response.code() == 400) {
                            val gson: Gson = GsonBuilder().create()
                            var mError = GeneralResponse()
                            try {
                                mError = gson.fromJson(response.errorBody()!!.string(), GeneralResponse::class.java)
                                val authorization = response?.headers()?.get(Constants.HEADER_AUTH)

                                Utills.invalidTokenLogout(this@OrderDetailsUserActivity, mError.msg, authorization)
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
            showToast(Constants.NO_INTERNET_CONNECTION)
        }
    }

    private fun populateViews(){

        if(orderUserDetails?.pharmacy != null) {
            if (orderUserDetails?.pharmacy?.placeName != null) {
                pharmacyName.text = orderUserDetails?.pharmacy?.placeName
                pharmacyIcon.setImageResource(R.color.initials_background_color)
                initials.visibility = View.VISIBLE
                initials.text = Utills.extractInitialsPharmacy(orderUserDetails?.pharmacy?.placeName)
                pharmacyIcon.borderWidth = Constants.BORDER_WIDTH
                pharmacyIcon.borderColor = resources.getColor(R.color.initials_color)
            }

            if (orderUserDetails?.pharmacy?.icon != null && orderUserDetails?.pharmacy?.icon != "") {

                Glide
                        .with(this)
                        .load(Utills.getCompleteUrl(orderUserDetails?.pharmacy?.icon))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, isFirstResource: Boolean): Boolean {

                                return false
                            }

                            override fun onResourceReady(resource: Drawable?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {

                                initials.visibility = View.GONE
                                pharmacyIcon.borderWidth = 0
                                return false
                            }
                        })
                        .signature(MediaStoreSignature("", Calendar.getInstance().time.time, 0))
                        .placeholder(R.color.initials_background_color)
                        .error(R.color.initials_background_color)
                        .into(pharmacyIcon)
            } else {
            }

            if (orderUserDetails?.pharmacy?.placeAddress != null) {
                pharmacyAddress.text = orderUserDetails?.pharmacy?.placeAddress
            }
        }

        if(orderUserDetails?.dateTime != null){
            dateTime.text = Utills.getTimeStamp(orderUserDetails?.dateTime, Constants.OUR_DATE_TIME_FORMAT)
        }

        if(orderUserDetails?.checkoutId != null){
            orderId.text = orderUserDetails?.checkoutId
        }

        if(orderUserDetails?.itemsCount != null){
            itemsCount.text = orderUserDetails?.itemsCount.toString()
        }

        if(orderUserDetails?.promoCode != null && orderUserDetails?.promoCode!!.isNotEmpty()){
            promoCodeCard.visibility = View.VISIBLE
            promoCode.text = orderUserDetails?.promoCode
        } else {
            promoCodeCard.visibility = View.GONE
        }

        var total = 0

        if(orderUserDetails?.deliveryCharges != null && orderUserDetails?.deliveryCharges!! > 0){

            amount.text = Utills.getCurrencyCode(orderUserDetails?.pharmacy?.currencySymbol) + " " + orderUserDetails?.totalAmount.toString()
            deliveryCharges.text = Utills.getCurrencyCode(orderUserDetails?.pharmacy?.currencySymbol) + " " + orderUserDetails?.deliveryCharges.toString()

            total = orderUserDetails?.deliveryCharges!!

            divider1.visibility = View.VISIBLE
            amountLabel.visibility = View.VISIBLE
            amount.visibility = View.VISIBLE
            deliveryChargesLabel.visibility = View.VISIBLE
            deliveryCharges.visibility = View.VISIBLE
        } else {
            amountLabel.visibility = View.GONE
            amount.visibility = View.GONE
            deliveryChargesLabel.visibility = View.GONE
            deliveryCharges.visibility = View.GONE
        }

        if (orderUserDetails?.totalAmount != null && orderUserDetails?.totalAmount!! > 0) {

            totalAmount.text = Utills.getCurrencyCode(orderUserDetails?.pharmacy?.currencySymbol) + " " + (orderUserDetails?.totalAmount!! + total).toString()

            divider1.visibility = View.VISIBLE
            totalLabel.visibility = View.VISIBLE
            totalAmount.visibility = View.VISIBLE
        }

        if(orderUserDetails?.orderNote != null && orderUserDetails?.orderNote != "") {
            orderNote.text = orderUserDetails?.orderNote
            orderNoteSection.visibility = View.VISIBLE
        } else {
            orderNote.visibility = View.GONE
            orderNoteSection.visibility = View.GONE
        }

        if(orderUserDetails?.audioNoteURL != null && orderUserDetails?.audioNoteURL != "") {
            orderNoteSection.visibility = View.VISIBLE
            audioNoteSection.visibility = View.VISIBLE
        }

        if(orderUserDetails?.cancellationNote != null && orderUserDetails?.cancellationNote != "") {
            note.text = orderUserDetails?.cancellationNote
            notesSection.visibility = View.VISIBLE
        } else {
            notesSection.visibility = View.GONE
        }

        if(orderUserDetails?.prescriptions == null)
            orderUserDetails?.prescriptions = ArrayList()

        if(orderUserDetails?.items != null && orderUserDetails?.prescriptions != null) {
            orderItemsRecycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            orderItemsRecycler.isNestedScrollingEnabled = false
            orderItemsAdapter = OrderItemsListKAdapter(this, orderUserDetails?.items!!, orderUserDetails?.prescriptions!!,
                    nightMode, false, orderUserDetails?.orderStatus!!, Utills.getCurrencyCode(orderUserDetails?.pharmacy?.currencySymbol), true)
            orderItemsRecycler.adapter = orderItemsAdapter
            orderItemsRecycler.invalidate()
        }

        if(orderUserDetails?.orderStatus != null) {
            orderStatus.text = orderUserDetails?.orderStatus
        }

        setStatus()
    }

    private fun setStatus() {

        orderStatus.text = Utills.updateStatus(this, orderUserDetails?.orderStatus, orderStatus, null, nightMode)

        if(orderUserDetails?.orderStatus == Constants.STATUS_OFFLINE){
            orderState.visibility = View.GONE
            orderStatus.visibility = View.VISIBLE
        } else {
            Utills.updateStatusBar(this, orderUserDetails?.orderType, orderUserDetails?.orderStatus, orderStatusBar, pendingPoint, pendingLabel,
                    confirmationPoint, confirmationLabel, processPoint, processLabel, enRoutePoint, enRouteLabel, completePoint, completeLabel)
        }

        Utills.updateOrderButtonsUser(this, orderUserDetails?.orderType, orderUserDetails?.orderStatus, bottom, accept, cancel)
    }

    private fun showLimitDialog(charges: Boolean) {
        customDialog = CustomDialog(this)

        var message = "Order Value < " + orderUserDetails?.pharmacy?.minOrderLimit
        var button1 = ""
        var button2 = ""

        if(charges) {
            message += Constants.LIMIT_CHARGES_USER_DIALOG_MESSAGE + orderUserDetails?.deliveryCharges
            button1 = Constants.LIMIT_CHARGES_USER_DIALOG_BUTTON_1
            button2 = Constants.LIMIT_CHARGES_USER_DIALOG_BUTTON_2
        } else {
            message += Constants.LIMIT_USER_DIALOG_MESSAGE
            button1 = Constants.LIMIT_USER_DIALOG_BUTTON_1
            button2 = Constants.LIMIT_USER_DIALOG_BUTTON_2
        }

        customDialog = CustomDialog(this)
        customDialog?.showGeneralDialog(nightMode, Constants.LIMIT_USER_DIALOG_TITLE, message,
                    button1, button2, null, true)
    }

    fun setNavigationColor() {
        if (nightMode)
            Utills.changeNavigationBarColor(this, Constants.COLOR_BLACK)
        else
            Utills.changeNavigationBarColor(this, Constants.COLOR_FORM)
    }

    fun showToast(text: String) {

        if (toast != null) {
            toast?.cancel()
        }

        toast = Toast.makeText(this, text, Toast.LENGTH_LONG)

        val inflater = layoutInflater

        val layout = layoutInflater.inflate(R.layout.custom_toast_design,
                findViewById<ViewGroup>(R.id.toast_root))


        val toastTextView = layout.findViewById(R.id.toast_text) as TextView
        toastTextView.text = text

        toast?.duration = Toast.LENGTH_LONG
        toast?.view = layout
        toast?.show()
    }

    private fun setMPListener(){

        mPlayer.setOnCompletionListener {

            stopPlaying()
        }

    }

    private fun startTimer() {

        var maxDuration = mPlayer.duration.toLong()

        runOnUiThread(object : Runnable {
            override fun run() {
                if (mPlayer != null) {
                    audioNoteDuration.text = Utills.formatMilliSeconds(maxDuration - mPlayer.currentPosition + 1000)
                }
                mHandler.postDelayed(this, 1000)
            }
        })
    }

    private fun startPlaying() {

        if(orderUserDetails?.audioNoteURL != null && orderUserDetails?.audioNoteURL != "") {
            try {
//                else {
                mPlayer = MediaPlayer()
                mPlayer.setDataSource(Utills.getCompleteUrl(orderUserDetails?.audioNoteURL))
                mPlayer.setOnPreparedListener{
                    mPlayer.start()
                    playing = true
                    setMPListener()
                    startTimer()
                    audioNoteIcon.setImageResource(R.drawable.icon_stop)
                }
                mPlayer.prepareAsync()
//                Utills.getDuration(Utills.getCompleteUrl(orderUserDetails?.audioNoteURL))
//                }
            } catch (e: IOException) {
//            Log.e(LOG_TAG, "prepare() failed")
            }
        }
    }

    private fun stopPlaying() {
        if(mPlayer != null) {
            mPlayer.stop()
            mPlayer.release()
            playing = false
            audioNoteIcon.setImageResource(R.drawable.icon_play)
            if(mHandler != null)
                mHandler.removeCallbacksAndMessages(null)

            audioNoteDuration.text = "00:00"
//                    mPlayer.release()
        }
    }

    private fun goBack() {

        val intent = Intent()
        intent.putExtra(Constants.ORDER_POSITION, position)
        intent.putExtra(Constants.INTENT_ORDER, orderUserDetails)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onBackPressed() {

        cancelOrderDetailsCall()
        goBack()

        super.onBackPressed()
    }

    override fun onResume() {

        registerReceiver(broadCastReceiver, IntentFilter(Constants.PUSH_NOTIFICATION_ORDER_USER_DETAILS))
        super.onResume()
    }

    override fun onDestroy() {

        unregisterReceiver(broadCastReceiver)
        super.onDestroy()
    }

    override fun onClick(v: View?) {
        when (v?.getId()) {

            R.id.back ->

                finish()

            R.id.play_audio_note -> {
                if(playing){
                    stopPlaying()
                } else {
                    startPlaying()
                }
            }

            R.id.audio_note_section -> {
                if(playing) {
                    stopPlaying()
                } else {
                    startPlaying()
                }
            }

            else -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return
                }
                mLastClickTime = SystemClock.elapsedRealtime()

                performClick(v!!)
            }

        }

    }

    private fun performClick(v: View) {
        when (v.id) {

            R.id.accept -> {
                if(accept.text == Constants.BUTTON_REORDER){

                    val reOrder = Intent(this, PlaceOrderActivity::class.java)
//                    reOrder.putExtra("searchPharmacy", true)
                    reOrder.putExtra(Constants.INTENT_ORDER, Utills.getReOrderObject(orderUserDetails))
//                    Constants.order = Utills.getReOrderObject(orderUserDetails)
                    reOrder.putExtra(Constants.INTENT_RE_ORDER, true)
                    startActivity(reOrder)

                } else {
                    if(orderUserDetails?.typeChange!! && orderUserDetails?.orderStatus == Constants.STATUS_CONFIRMATION)
                        showLimitDialog(false)
                    else if(orderUserDetails?.deliveryCharges != null && orderUserDetails?.deliveryCharges!! > 0)
                        showLimitDialog(true)
                    else
                        changeOrderStatusCall(Constants.STATUS_IN_PROGRESS, "", false)
                }
            }

            R.id.cancel -> {
                showCancelOrderDialog()
            }
        }
    }
}