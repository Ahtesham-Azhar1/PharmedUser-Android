package com.zealsoft.pharmed.activities

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.zealsoft.pharmed.R
import com.zealsoft.pharmed.Util.Constants
import com.zealsoft.pharmed.Util.Internet
import com.zealsoft.pharmed.Util.Preferences
import com.zealsoft.pharmed.Util.Utills
import com.zealsoft.pharmed.adapters.OrdersListUserKAdapter
import com.zealsoft.pharmed.apis.RestApis
import com.zealsoft.pharmed.apis.RetroClient
import com.zealsoft.pharmed.customWidgets.CustomDialog
import com.zealsoft.pharmed.models.*
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import kotlin.collections.ArrayList

class OrdersUserActivity : AppCompatActivity(), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemSelectedListener {

    private lateinit var title: TextView
    private lateinit var cartCount: TextView
    private lateinit var back: ImageView
    private lateinit var cartToolbar: ImageView
    private lateinit var logoToolbar: CircleImageView
    private lateinit var initials: TextView
    private lateinit var ordersScreen: ConstraintLayout
    private lateinit var ordersRecyclerView: RecyclerView
    private lateinit var adapter: OrdersListUserKAdapter
    private lateinit var refreshLayout: SwipeRefreshLayout
    private var ordersUser: MutableList<Order>? = null
    private lateinit var filterBar: ConstraintLayout
    private lateinit var selectStatus: Spinner
    private lateinit var setFilter: ImageView
    private lateinit var orderTabs: TabLayout
    private lateinit var orderViewPager: ViewPager
    private var id = ""
    private var position = -1
    private lateinit var noOrdersYet: TextView
    private lateinit var placeOrder: TextView
    private var nightMode = false
    private var showDetails = false
    private var customDialog: CustomDialog? = null
    private var toast: Toast? = null
    private var orientationChanged = false
    private lateinit var ordersCall: Call<GeneralResponse>
    private lateinit var restApis: RestApis
    private var statuses: List<String>? = null
    private var selectedStatus: String? = null
    private var deviceId: String? = null
    private var userId: String? = null
    private var token: String? = null

    private lateinit var manager: FragmentManager
    private var fragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders_user)

        if(savedInstanceState != null) {
            ordersUser = Constants.cOrdersList
        }

        setViews()
    }

    private fun setViews() {
        title = findViewById(R.id.title)
        title.text = resources.getString(R.string.title_orders_list_screen)
        cartToolbar = findViewById(R.id.cart_toolbar)
        cartToolbar.setOnClickListener(this)
        cartCount = findViewById(R.id.cart_count)
        logoToolbar = findViewById(R.id.logo_toolbar_pharmed)
        initials = findViewById(R.id.initials_toolbar)
        back = findViewById(R.id.back)
        back.setOnClickListener(this)

        ordersScreen = findViewById(R.id.orders_list_user_screen)
        ordersRecyclerView = findViewById(R.id.orders_recycler)
        noOrdersYet = findViewById(R.id.no_orders_prompt)
        placeOrder = findViewById(R.id.place_order)
        placeOrder.setOnClickListener(this)
        filterBar = findViewById(R.id.actions_bar)
        selectStatus = findViewById(R.id.select_status)
        setFilter = findViewById(R.id.set_filter)
        setFilter.setOnClickListener(this)
        refreshLayout = findViewById(R.id.refresh_layout)
        refreshLayout.setOnRefreshListener(this)
        refreshLayout.setColorSchemeResources(R.color.pdThemeBlueColorL)
        customDialog = CustomDialog(this)

        restApis = RetroClient.getClient().create(RestApis::class.java)

        nightMode = Preferences.getNightThemeSelectionFromSharedPreferences(this)

        if(intent.hasExtra(Constants.INTENT_DEVICE_ID))
            deviceId = intent.getStringExtra(Constants.INTENT_DEVICE_ID)

        if(intent.hasExtra(Constants.INTENT_USER_ID))
            userId = intent.getStringExtra(Constants.INTENT_USER_ID)

        if(intent.hasExtra(Constants.INTENT_TOKEN))
            token = intent.getStringExtra(Constants.INTENT_TOKEN)

        Preferences.addAuthCodeToSharedPreferences(this, token)

        if (showDetails) {
            showOrdersDetails(null)
        }

        if((ordersUser == null || ordersUser?.size!! == 0) && !orientationChanged)
            getOrdersCall(null, true)
        else
            populateOrder()

        checkTheme()

        setPic()

        if(Preferences.getUserDataFromSharedPreferences(this) == null) {
            filterBar.visibility = View.GONE
        } else {
            setSpinner()
        }
    }

    private fun checkTheme() {
        if (nightMode) {
            ordersScreen.setBackgroundResource(R.color.formBackgroundNightMode)
            noOrdersYet.setBackgroundResource(R.color.formBackgroundNightMode)
            noOrdersYet.setTextColor(resources.getColor(R.color.textColorNightMode))
            Utills.changeNavigationBarColor(this, Constants.COLOR_BLACK)
        } else {
            Utills.changeNavigationBarColor(this, Constants.COLOR_FORM)
        }
    }


    private fun setPic(){

        val user = Preferences.getUserDataFromSharedPreferences(this)

        if(user != null){

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
//                    iv_user.setImageResource(picturePlaceHolderImage)
            }
        }
    }

    private fun setSpinner(){

        statuses = listOf("ALL", Constants.STATUS_PENDING, Constants.STATUS_CONFIRMATION, Constants.IN_PROGRESS, Constants.STATUS_READY, Constants.STATUS_ENROUTE, Constants.STATUS_COMPLETE, Constants.STATUS_REJECTED, Constants.STATUS_OFFLINE)

        selectStatus.onItemSelectedListener = this

        val filterAdapter = ArrayAdapter(this, R.layout.custom_spinner_item_white, statuses!!)

        if(nightMode)
            filterAdapter.setDropDownViewResource(R.layout.custom_spinner_status_dropdown)
        else
            filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        selectStatus.adapter = filterAdapter

    }

    private val broadCastReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action

            Preferences.addOrdersCountChangeBooleanToSharedPreferences(this@OrdersUserActivity, true)
            if(action == Constants.PUSH_NOTIFICATION_NEW_ORDER){

                if(selectedStatus == Constants.STATUS_PENDING || selectedStatus == null) {
                    getOrdersCall(selectedStatus, false)
                }
            }

            if(action == Constants.PUSH_NOTIFICATION_ORDER_USER_DETAILS){
                if(intent != null && intent.hasExtra(Constants.INTENT_NOTIFICATION_ORDER)){
                    var id = intent.getStringExtra(Constants.INTENT_NOTIFICATION_ORDER)
//                    var found = false
//                    var

                    if(ordersUser != null) {
                        for (i in ordersUser!!.indices) {
                            if(ordersUser!![i].id == id){
//                                found = true
                                getOrderDetailsCall(id, i)
                                break
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getOrdersCall(status: String? = null, showLoading: Boolean) {

        if (Internet.isAvailable(this)) {

            if(showLoading) {
                if (!refreshLayout.isRefreshing)
                    customDialog?.showLoadingDialogue()
            }

//            val user = Preferences.getUserDataFromSharedPreferences(this)
//            val deviceId = Preferences.getDeviceIdFromSharedPreferences(this)

//            var userId = ""

//            userId = if(user != null){
//                user.id.toString()
//            } else {
//                Constants.DEFAULT_USER_ID
//            }

            val restApis = RetroClient.getClient().create(RestApis::class.java)

            var params = Params()

            params.userId = userId
            params.deviceId = deviceId
            params.orderStatus = status
            params.orderPageNumber = 1
            params.orderPageSize = 100

            val getOrdersCall = restApis.getUserOrders(token,
                    Constants.HEADER_CONTENT_TYPE_VALUE, params)
            getOrdersCall.enqueue(object : Callback<GeneralResponse> {
                override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                    if (response.isSuccessful) {
                        customDialog?.dismissLoadingDialogue()
                        stopRefresh()
                        if (response.body() != null && response.body()!!.isSuccess && response.body()!!.results != null
                                && response.body()!!.results.ordersList != null) {
                            ordersUser = response.body()!!.results.ordersList
                            if(ordersUser != null && ordersUser?.size!! > 0) {
                                    populateOrder()
                            } else {
                                noOrdersPrompt()
                            }
                        } else {
                            noOrdersPrompt()
                        }
                    } else {
                        if (response.code() == 400) {
                            val gson: Gson = GsonBuilder().create()
                            var mError = GeneralResponse()
                            try {
                                mError = gson.fromJson(response.errorBody()!!.string(), GeneralResponse::class.java)
                                val authorization = response?.headers()?.get(Constants.HEADER_AUTH)

                                Utills.invalidTokenLogout(this@OrdersUserActivity, mError.msg, authorization)
                            } catch (e: IOException) {
                                // handle failure to read error
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    customDialog?.dismissLoadingDialogue()
                    stopRefresh()
                    showToast(Constants.CONNECTION_PROBLEM)
                    internetProblemPrompt()
                }
            })
        } else {
            showToast(Constants.NO_INTERNET_CONNECTION)
            internetProblemPrompt()
        }
    }

    fun showLimitDialog(id: String, position: Int, charges: Boolean){
        this.id = id
        this.position = position

        var order = ordersUser!![position]

        if(order.id == id) {

            var message = "Order Value < " + order.pharmacy?.minOrderLimit
            var button1 = ""
            var button2 = ""

            if (charges) {
                message += Constants.LIMIT_CHARGES_USER_DIALOG_MESSAGE + order.deliveryCharges
                button1 = Constants.LIMIT_CHARGES_USER_DIALOG_BUTTON_1
                button2 = Constants.LIMIT_CHARGES_USER_DIALOG_BUTTON_2
            } else {
                message += Constants.LIMIT_USER_DIALOG_MESSAGE
                button1 = Constants.LIMIT_USER_DIALOG_BUTTON_1
                button2 = Constants.LIMIT_USER_DIALOG_BUTTON_2
            }

            customDialog?.showGeneralDialog(nightMode, Constants.LIMIT_USER_DIALOG_TITLE, message,
                    button1, button2, null, true)
        }
    }

    fun showCancelOrderDialog(id: String, position: Int){
        this.id = id
        this.position = position

        var button1 = Constants.CANCEL_ORDER_USER_DIALOG_BUTTON_1

        customDialog = CustomDialog(this)

        if(ordersUser?.get(position)?.orderStatus == Constants.STATUS_CONFIRMATION && ordersUser?.get(position)?.deliveryCharges != null
                && ordersUser?.get(position)?.deliveryCharges!! > 0)
            button1 = Constants.CANCEL_ORDER_USER_DIALOG_BUTTON_PICKUP

        customDialog?.showGeneralDialog(nightMode, Constants.CANCEL_ORDER_USER_DIALOG_TITLE, Constants.CANCEL_ORDER_USER_DIALOG_MESSAGE,
                button1, Constants.CANCEL_ORDER_USER_DIALOG_BUTTON_2, null, true)
    }

    fun confirmOrder(changeType: Boolean){
        changeOrderStatusCall(id, Constants.STATUS_IN_PROGRESS, position, "", changeType)
    }

    fun cancelOrder(reason: String){
        changeOrderStatusCall(id, Constants.STATUS_CANCELLED_CONFIRMATION, position, reason, false)
    }

    fun changeOrderStatusCall(id: String, status: String, position: Int, reason: String, typeChange: Boolean) {

        if (Internet.isAvailable(this)) {

            customDialog?.showLoadingDialogue()

            this.position = position

            val restApis = RetroClient.getClient().create(RestApis::class.java)

            var cancellationNote = CancellationNote()

            if(status == Constants.STATUS_CANCELLED_CONFIRMATION)
                cancellationNote.message = Utills.getUserCancellationMsg(Preferences.getUserDataFromSharedPreferences(this), status)
            else
                cancellationNote.message = Utills.getUserAcceptanceMsg(Preferences.getUserDataFromSharedPreferences(this), status)

            cancellationNote.typeChange = typeChange

            if(Preferences.getFcmTokenFromSharedPreferences(this) != null)
                cancellationNote.fcmToken = Preferences.getFcmTokenFromSharedPreferences(this)

            cancellationNote.orderId = id
            cancellationNote.orderStatus = status

            val changeStatusCall = restApis.changeOrderStatus(Preferences.getAuthCodeFromSharedPreferences(this),
                    Constants.HEADER_CONTENT_TYPE_VALUE, cancellationNote)
            changeStatusCall.enqueue(object : Callback<GeneralResponse> {
                override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                    if (response.isSuccessful) {
                        customDialog?.dismissLoadingDialogue()
                        if (response.body() != null && response.body()!!.isSuccess && response.body()!!.msg != null) {

                                if(response.body()!!.results != null && response.body()!!.results.order != null){
                                    ordersUser!![position] = response.body()!!.results.order
                                }

                                adapter.notifyDataSetChanged()
                                ordersRecyclerView.invalidate()

                                this@OrdersUserActivity.id = ""
                                this@OrdersUserActivity.position = -1

//                            }
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
                                Utills.invalidTokenLogout(this@OrdersUserActivity, mError.msg, authorization)
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

    private fun populateOrder(){

        noOrdersYet.visibility = View.GONE
        ordersRecyclerView.visibility = View.VISIBLE
        ordersRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        adapter = OrdersListUserKAdapter(this, ordersUser!!, nightMode, showDetails)
        ordersRecyclerView.adapter = adapter
        ordersRecyclerView.invalidate()
    }

    private fun notifyChange(){
        if(noOrdersYet.visibility == View.VISIBLE){
            populateOrder()
        } else {
            if(ordersRecyclerView != null){
                adapter.notifyDataSetChanged()
                ordersRecyclerView.invalidate()
            }
        }
    }

    private fun showOrdersDetails(pharmacyDetails: PharmacyDetails?) {

        if (showDetails) {
            val bundle = Bundle()
            bundle.putSerializable("pharmacyDetails", pharmacyDetails)

            showDetailsFrag(Constants.TAG_PHARMACY_DETAILS_FRAGMENT, bundle)
        }
    }

    private fun getOrderDetailsCall(id: String, position: Int) {

        if (Internet.isAvailable(this)) {

            val restApis = RetroClient.getClient().create(RestApis::class.java)

            var params = Params()

            params.orderId = id

            var orderDetailsCall = restApis.getOrderDetails(Preferences.getAuthCodeFromSharedPreferences(this),
                    Constants.HEADER_CONTENT_TYPE_VALUE, params)
            orderDetailsCall.enqueue(object : Callback<GeneralResponse> {
                override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                    if (response.isSuccessful) {
                        if (response.body() != null && response.body()!!.isSuccess) {
                            if (response.body()!!.results != null && response.body()!!.results.order != null) {

                                if(ordersUser != null) {
                                    ordersUser!![position] = response.body()!!.results.order
                                    adapter.notifyDataSetChanged()
                                    ordersRecyclerView.invalidate()
                                }

                            } else {
                            }
                        }
                    } else {
                        if (response.code() == 400) {
                            val gson: Gson = GsonBuilder().create()
                            var mError = GeneralResponse()
                            try {
                                mError = gson.fromJson(response.errorBody()!!.string(), GeneralResponse::class.java)
                                val authorization = response?.headers()?.get(Constants.HEADER_AUTH)

                                Utills.invalidTokenLogout(this@OrdersUserActivity, mError.msg, authorization)
                            } catch (e: IOException) {
                                // handle failure to read error
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    if (t.message != "Canceled") {
//                        showToast(Constants.CONNECTION_PROBLEM)
                    }
                }
            })
        }
    }

    private fun showDetailsFrag(fragmentTag: String, bundle: Bundle) {

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

    private fun noOrdersPrompt(){
        ordersRecyclerView.visibility = View.GONE

        if(Preferences.getUserDataFromSharedPreferences(this) != null){
            noOrdersYet.text = Constants.NO_ORDERS_YET_USER
        } else {
            noOrdersYet.text = Constants.NO_ORDERS_YET
        }

        noOrdersYet.visibility = View.VISIBLE
    }

    private fun internetProblemPrompt(){
        ordersRecyclerView.visibility = View.GONE
        noOrdersYet.text = Constants.NO_INTERNET_CONNECTION
        noOrdersYet.visibility = View.VISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode ==  Constants.INTENT_ORDER_DETAILS){
            if(resultCode == Activity.RESULT_OK){
                val position = data?.getIntExtra(Constants.ORDER_POSITION, -1)
                val order = data?.getSerializableExtra(Constants.INTENT_ORDER) as Order
                ordersUser!![position!!] = order
                adapter.notifyDataSetChanged()
                ordersRecyclerView.invalidate()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Save the state of item position
        outState.putBoolean("orientationChanged", true)

        Constants.cOrdersList = ArrayList()
        Constants.cOrdersList = ordersUser
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        // Read the state of item position
        orientationChanged = savedInstanceState.getBoolean("orientationChanged")

    }

    public override fun onResume() {
        val intentFilters = IntentFilter()
        intentFilters.addAction(Constants.PUSH_NOTIFICATION_NEW_ORDER)
        intentFilters.addAction(Constants.PUSH_NOTIFICATION_ORDER_USER_DETAILS)

        registerReceiver(broadCastReceiver, IntentFilter(intentFilters))
        super.onResume()
    }

    override fun onDestroy() {
        unregisterReceiver(broadCastReceiver)
        super.onDestroy()
    }

    override fun onClick(v: View?) {

        when(v?.id){

            R.id.back -> finish()

            R.id.cart_toolbar -> startActivity(Intent(this, CartKActivity::class.java))
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedStatus = statuses!![position]

        if(selectedStatus == "ALL"){
            selectedStatus = null
        }

        if(selectedStatus == Constants.IN_PROGRESS)
            selectedStatus = Constants.STATUS_IN_PROGRESS

        if(selectedStatus == Constants.STATUS_REJECTED)
            selectedStatus = Constants.STATUS_CANCELLED

        getOrdersCall(selectedStatus, true)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onRefresh() {

        getOrdersCall(selectedStatus, true)
    }

    private fun stopRefresh() {
        if(refreshLayout.isRefreshing)
            refreshLayout.isRefreshing = false
    }
}