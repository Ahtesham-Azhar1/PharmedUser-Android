package com.zealsoft.pharmed.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import androidx.constraintlayout.widget.ConstraintLayout
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.zealsoft.pharmed.R
import com.zealsoft.pharmed.Util.Constants
import com.zealsoft.pharmed.Util.Internet
import com.zealsoft.pharmed.Util.Preferences
import com.zealsoft.pharmed.Util.Utills
import com.zealsoft.pharmed.adapters.CartListKAdapter
import com.zealsoft.pharmed.apis.RestApis
import com.zealsoft.pharmed.apis.RetroClient
import com.zealsoft.pharmed.customWidgets.CustomDialog
import com.zealsoft.pharmed.models.*
import de.hdodenhof.circleimageview.CircleImageView
import id.zelory.compressor.Compressor
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.util.ArrayList

class CartKActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var cartScreen: ConstraintLayout
    private lateinit var noCart: ConstraintLayout
    private lateinit var back: ImageView
    private lateinit var medSearchToolbar:ImageView
    private lateinit var logoToolbar: CircleImageView
    private lateinit var title: TextView
    private lateinit var totalItems: TextView
    private lateinit var searchMedicine: TextView
    private lateinit var cartListRecycler: RecyclerView
    private lateinit var uploadPrescription: Button
    private lateinit var attachPrescription: ImageButton
    private lateinit var searchMedicines: ImageButton
    private lateinit var createMedicine: ImageButton
    private lateinit var done: Button
    private lateinit var deleteBtn: ImageView
    private lateinit var selectAll: CheckBox
    private lateinit var actionBar: View
    private lateinit var selectionLabel: ConstraintLayout
    private lateinit var selectedPharmacyLabel: TextView
    private lateinit var cancelSelection: ImageView
    private var medicinesList: MutableList<MedicineDetails>? = null
    private lateinit var cartListAdapter: CartListKAdapter
    private var mLastClickTime: Long = 0
    private var nightMode = false
    private var toast: Toast? = null
    private var detailsFrame: FrameLayout? = null
    private var showDetails = false
    private var populated = false
    private var selectAllPressed = true
    private var customDialog: CustomDialog? = null
    private var position: Int = 0
    private var quantity = 0
    private var selectedPharmacy: PharmacyDetails? = null

    private var cartSent = false
    private var prescriptionsSent = false
    private var smsOrderSent = false
    private var reOrder = false
    private var forEdit = false
    private var deviceId: String? = null
    private var userId: String? = null
    private var token: String? = null

    private var files: MutableList<File>? = null
    private var prescriptionList: ArrayList<Prescription>? = null
    private var base64Icon: String? = null
    private val REQUEST_READ_WRITE_PERMISSION = 12
    private val REQUEST_CREATE_MEDICINE = 13
    private val REQUEST_PLACE_ORDER = 14

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_k)

        setViews()
    }

    override fun onNewIntent(intent: Intent?) {

        if(intent != null && intent.hasExtra(Constants.INTENT_EDIT_CART))
            forEdit = intent.getBooleanExtra(Constants.INTENT_EDIT_CART, false)

        super.onNewIntent(intent)
    }

    private fun setViews() {

        cartScreen = findViewById(R.id.cart_screen)
        back = findViewById(R.id.back)
        back.setOnClickListener(this)
        title = findViewById(R.id.title)
        title.text = resources.getString(R.string.title_cart_screen)
        medSearchToolbar = findViewById(R.id.med_search_toolbar)
        medSearchToolbar.setOnClickListener(this)
        logoToolbar = findViewById(R.id.logo_toolbar_pharmed)
        logoToolbar.setOnClickListener(this)

        cartListRecycler = findViewById(R.id.cart_recycler)
        totalItems = findViewById(R.id.total_items)
        uploadPrescription = findViewById(R.id.prescription)
        uploadPrescription.setOnClickListener(this)
        uploadPrescription.isSelected = true
        attachPrescription = findViewById(R.id.attach_prescription)
        attachPrescription.setOnClickListener(this)
        searchMedicines = findViewById(R.id.search_meds)
        searchMedicines.setOnClickListener(this)
        createMedicine = findViewById(R.id.create_meds)
        createMedicine.setOnClickListener(this)
        done = findViewById(R.id.done)
        done.setOnClickListener(this)
        done.isSelected = true
        noCart = findViewById(R.id.no_cart)
        searchMedicine = findViewById(R.id.search_medicine)
        searchMedicine.setOnClickListener(this)
        deleteBtn = findViewById(R.id.remove_icon)
        deleteBtn.setOnClickListener(this)
        selectAll = findViewById(R.id.select_all)
        actionBar = findViewById(R.id.functions_bar)

        selectionLabel = findViewById(R.id.selection_label)
        selectedPharmacyLabel = findViewById(R.id.selected_pharmacy_label)
        cancelSelection = findViewById(R.id.cancel_selection)
        cancelSelection.setOnClickListener(this)

        medicinesList = ArrayList()
        prescriptionList = ArrayList()
        files = ArrayList()

        customDialog = CustomDialog(this)

        medicinesList = Preferences.getCartItemsFromSharedPreferences(this)

        if(intent.hasExtra(Constants.INTENT_DEVICE_ID))
            deviceId = intent.getStringExtra(Constants.INTENT_DEVICE_ID)

        if(intent.hasExtra(Constants.INTENT_USER_ID))
            userId = intent.getStringExtra(Constants.INTENT_USER_ID)

        if(intent.hasExtra(Constants.INTENT_TOKEN))
            token = intent.getStringExtra(Constants.INTENT_TOKEN)

        if(intent.hasExtra(Constants.INTENT_MEDICINES))
            medicinesList = intent.getSerializableExtra(Constants.INTENT_MEDICINES) as ArrayList<MedicineDetails>

        selectedPharmacy = Preferences.getSelectedPharmacyFromSharedPreferences(this)

//        Preferences.addAuthCodeToSharedPreferences(this, Utills.getCompleteToken(token))

        if(intent.hasExtra(Constants.INTENT_RE_ORDER))
            reOrder = intent.getBooleanExtra(Constants.INTENT_RE_ORDER, false)

        setDoneButton()

        if(medicinesList == null){
            medicinesList = ArrayList()
        }

        nightMode = Preferences.getNightThemeSelectionFromSharedPreferences(this)

        if (showDetails) {
            showMedicineDetails(null)
        }

        selectAll.setOnCheckedChangeListener { buttonView, isChecked ->

            if(buttonView.isPressed) {
                if (populated && selectAllPressed) {
                    selectUnselectAll(isChecked)
                } else {
                    selectAllPressed = true
                }
            }
        }

        prescriptionList = Preferences.getPrescriptionImagePathsFromSharedPreferences(this)

        if(!reOrder)
            createPrescriptionsFromPaths()

        setUploadButton()

        populateList()

        if(cartListRecycler.visibility == View.GONE){
            if(Preferences.getVoiceOverSelectionFromSharedPreferences(this)) {

                val stopMediaPlayer = Intent(this, MediaPlayer::class.java)
                stopService(stopMediaPlayer)

                val playMediaPlayer = Intent(this, MediaPlayer::class.java)
                if(selectedPharmacy != null){
                    playMediaPlayer.putExtra("play", Constants.PLAY_CART_SELECTED)
                } else {
                    playMediaPlayer.putExtra("play", Constants.PLAY_CART)
                }

                startService(playMediaPlayer)
            }
        }

        checkTheme()
    }

    private fun checkTheme() {

        Utills.changeNavigationBarColor(this, Constants.COLOR_GREY)

        if (nightMode) {
            cartScreen.setBackgroundResource(R.color.formBackgroundNightMode)
            selectAll.setButtonDrawable(R.drawable.checkbox_selector_colors)
        } else{

        }
    }

    private fun setDoneButton(){

        if(selectedPharmacy != null){
            if(selectedPharmacy?.pharmacyStatus == Constants.PHARMACY_STATUS_REGISTERED) {
                attachPrescription.isClickable = true
                attachPrescription.clearColorFilter()
            }
            else {
                attachPrescription.isClickable = false
                attachPrescription.setColorFilter(resources.getColor(R.color.grey))
            }

            done.text = "Proceed"

            selectedPharmacyLabel.text = "Selected Pharmacy: " + selectedPharmacy?.placeName
            selectionLabel.visibility = View.VISIBLE
        } else {
            done.text = "Select Pharmacy"
            selectionLabel.visibility = View.GONE
            attachPrescription.isClickable = true
            attachPrescription.clearColorFilter()
        }
    }

    private fun createPrescriptionsFromPaths(){

        files = ArrayList()

        if(prescriptionList != null && prescriptionList?.size!! > 0){
            for(i in prescriptionList!!.indices) {
                files?.add(File(prescriptionList!![i].path))
            }
//            populateImage(file)
        } else {
            prescriptionList = ArrayList()
        }
    }

    private fun getCartCall() {

        if (Internet.isAvailable(this)) {

            customDialog?.showLoadingDialogue()

        } else {
            showToast(Constants.NO_INTERNET_CONNECTION)
        }
    }

    private fun populateList() {

        if ((medicinesList == null || medicinesList?.size!! < 1) && (files == null || files?.size!! < 1)) {
            cartListRecycler.visibility = View.GONE
            noCart.visibility = View.VISIBLE
            setTotalItems(0)
            Preferences.addCartItemsToSharedPreferences(this, medicinesList)
            Preferences.addPrescriptionImagePathsToSharedPreferences(this, prescriptionList)
            selectAll.visibility = View.GONE
            deleteBtn.visibility = View.GONE
            totalItems.visibility = View.GONE
            actionBar.visibility = View.GONE
        } else {
            noCart.visibility = View.GONE
            cartListRecycler.visibility = View.VISIBLE
            selectAll.visibility = View.VISIBLE
            totalItems.visibility = View.VISIBLE
            deleteBtn.visibility = View.VISIBLE
            actionBar.visibility = View.VISIBLE

            Preferences.addCartItemsToSharedPreferences(this, medicinesList)
            Preferences.addPrescriptionImagePathsToSharedPreferences(this, prescriptionList)
            populated = true
            populateCartRecycler()
        }
    }

    private fun populateCartRecycler() {

        if(prescriptionList == null)
            prescriptionList = ArrayList()

        if(medicinesList == null)
            medicinesList = ArrayList()

        cartListRecycler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.VERTICAL, false) as RecyclerView.LayoutManager?
        cartListAdapter = CartListKAdapter(this, medicinesList!!, prescriptionList!!, nightMode, showDetails)
        cartListRecycler.adapter = cartListAdapter
        cartListAdapter.notifyDataSetChanged()
        cartListRecycler.invalidate()
        setTotalItems(medicinesList!!.size + prescriptionList!!.size)
    }

    fun removeItem(position: Int) {

        medicinesList?.removeAt(position)
        Preferences.addCartItemsToSharedPreferences(this, medicinesList)
        updateCart()

        if (medicinesList == null || medicinesList!!.size < 1) {
            cartListRecycler.visibility = View.GONE
            noCart.visibility = View.VISIBLE
            setTotalItems(0)
        } else {
            setTotalItems(medicinesList!!.size + prescriptionList!!.size)
        }
    }

    private fun removeSelectedCartItems(productIds: String) {

//        if (Internet.isAvailable(this)) {
//            customDialog?.showLoadingDialogue()
//
//            RestCaller(this, PharmaD.getRestClient().removeSelectedFromCart(
//                    Preferences.getDeviceIdFromSharedPreferences(this),
//                    productIds), 4)
//        } else {
//            showToast(Constants.NO_INTERNET_CONNECTION)
//        }
    }

    fun setQuantity(position: Int, quantity: Int) {
        var position = position
        var quantity = quantity

        if (quantity > Constants.MAX_QUANTITY) {
            quantity = Constants.MAX_QUANTITY
        }

        if (position >= 0) {
            val medicineDetails = Preferences.getCartItemsFromSharedPreferences(this)

            medicinesList!![position].quantity = quantity
            medicineDetails!![position].quantity = quantity

            //        for(int i = 0; i < medicineDetails.size(); i++){
            //            if(medicineDetails.get(i).id.equals(medicinesList.get(position).id)){
            //                medicineDetails.get(i).medicineQuantity = medicinesList.get(position).medicineQuantity;
            //                break;
            //            }
            //        }

            Preferences.addCartItemsToSharedPreferences(this, medicineDetails)
            updateCartBoolean()

            position = -1

            updateCart()
        }
    }

    private fun setTotalItems(items: Int) {
        totalItems.text = "Total Items: $items"
    }

    fun selectItemToDelete(position: Int, positionPrescription: Int, select: Boolean) {

        if(position > -1){
            medicinesList!![position].select = select
        }

        if(positionPrescription > -1){
            prescriptionList!![positionPrescription].select = select
        }

        if (select) {
            var allSelected = true
            for (i in medicinesList!!.indices) {
                if (!medicinesList!![i].select) {
                    allSelected = false
                    break
                }
            }

            for (i in prescriptionList!!.indices) {
                if(!prescriptionList!![i].select){
                    allSelected = false
                    break
                }
            }

            if (allSelected) {
                selectAllPressed = false
                selectAll.isChecked = true
                selectAllPressed = true
            }
        } else {
            selectAllPressed = false
            selectAll.isChecked = false
            selectAllPressed = true
        }


        cartListAdapter.notifyDataSetChanged()
        cartListRecycler.invalidate()
    }

    private fun selectUnselectAll(select: Boolean) {
        if (medicinesList != null) {
            for (i in medicinesList!!.indices) {
                medicinesList!![i].select = select
            }
        }

        if (prescriptionList != null) {
            for (i in prescriptionList!!.indices) {
                prescriptionList!![i].select = select
            }
        }

        updateCart()
    }

    private fun updateCart() {
        cartListAdapter.notifyDataSetChanged()
        cartListRecycler.invalidate()
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

        //        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast?.duration = Toast.LENGTH_LONG
        toast?.view = layout
        //        toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast?.show()
    }

    fun showMedicineDetails(medicineDetails: MedicineDetails?) {

        if (showDetails) {
            val bundle = Bundle()
            bundle.putSerializable("medicineDetails", medicineDetails)

//            Utills.showDetailsFrag(this, Constants.TAG_MEDICINE_DETAILS_FRAGMENT, bundle)
        }
    }


    fun getSelectedItemIds() {

        var productIds = ""

        if (medicinesList != null && medicinesList!!.size > 0) {
            for (i in medicinesList!!.indices) {
                if (medicinesList!![i].select) {
                    productIds = productIds + (medicinesList!![i].productId + ",")
                }
            }
        }

        removeSelectedCartItems(productIds)
    }

    fun removeItems() {

        if (medicinesList != null && medicinesList!!.size > 0) {
            for (i in medicinesList!!.indices.reversed()) {
                if (medicinesList!![i].select) {
                    medicinesList!!.removeAt(i)
                }
            }

            selectAll.isChecked = false

            updateCartBoolean()
        }

        if(prescriptionList != null && prescriptionList!!.size > 0){
            for (i in prescriptionList!!.indices.reversed()) {
                if(prescriptionList!![i].select){
                    if(!prescriptionList!![i].urlB) {
                        removePrescription(prescriptionList!![i].path!!, files!![i].name)
                        files!!.removeAt(i)
                    }
                    prescriptionList!!.removeAt(i)
                }
            }

            selectAll.isChecked = false

            setUploadButton()

            updateCartBoolean()
        }

        showToast(Constants.DELETE_FROM_CART)
        populateList()
    }

    private fun updateCartBoolean(){
        Preferences.addCartUpdateBooleanToSharedPreferences(this, true)
        cartSent = false
        prescriptionsSent = false
    }

    private fun setUploadButton(){
        if(files?.size!! < 3){
            uploadPrescription.setBackgroundResource(R.drawable.pd_background_button_dark)
            uploadPrescription.isSelected = true
        } else {
            uploadPrescription.setBackgroundResource(R.drawable.pd_background_et_round_night_mode_light)
            uploadPrescription.isSelected = false
        }
    }

    private fun searchMedicines() {
        val searchMedicines = Intent(this, SearchKActivity::class.java)
        searchMedicines.putExtra("searchMedicine", true)
        searchMedicines.putExtra("cart", true)
        startActivity(searchMedicines)
    }

    fun updateQuantity(position: Int) {
        medicinesList!![position].quantity = medicinesList!![position].quantity + quantity
        Preferences.addCartItemsToSharedPreferences(this, medicinesList)
        updateCart()
    }

    private fun openPlaceOrder(){

        val order = Order()

        if(selectedPharmacy != null)
            order.pharmacy = selectedPharmacy

        order.items = medicinesList
        order.prescriptions = prescriptionList

        val placeOrder = Intent(this, PlaceOrderActivity::class.java)

        if(reOrder)
            placeOrder.putExtra(Constants.INTENT_RE_ORDER, true)

        placeOrder.putExtra(Constants.INTENT_ORDER, order)
        placeOrder.putExtra(Constants.INTENT_TOKEN, Preferences.getAuthCodeFromSharedPreferences(this))
        placeOrder.putExtra(Constants.INTENT_USER_ID, userId)
        placeOrder.putExtra(Constants.INTENT_DEVICE_ID, deviceId)
        startActivityForResult(placeOrder, REQUEST_PLACE_ORDER)
    }

    private fun sendCartCall() {

        if (Internet.isAvailable(this)) {

            customDialog?.showLoadingDialogue()

            var cartParams = CartParams()

            cartParams.placeLat = "31.510294"
            cartParams.placeLng = "74.350017"
            cartParams.cartList = medicinesList

            var user = Preferences.getUserDataFromSharedPreferences(this)

//            if(user == null)
//                Constants.DEFAULT_USER_ID
//            }

            if(user != null) {
                cartParams.userId = user.id
                cartParams.deviceId = user.deviceId
            }

            val restApis = RetroClient.getClient().create(RestApis::class.java)

            val sendCartCall = restApis.sendCart(Preferences.getAuthCodeFromSharedPreferences(this),
                    Constants.HEADER_CONTENT_TYPE_VALUE, cartParams)
            sendCartCall.enqueue(object : Callback<GeneralResponse> {
                override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                    if (response.isSuccessful) {
                        val generalResponse = response.body()
                        if (generalResponse != null && generalResponse?.isSuccess &&
                                (generalResponse?.msg == "Item Added to Cart" || generalResponse?.msg == "Items Added to Cart")
                                && generalResponse?.results.orderItems != null) {

                            cartSent = true

                            goToCheckout()

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

                                Utills.invalidTokenLogout(this@CartKActivity, mError.msg, authorization)
                            } catch (e: IOException) {
                                // handle failure to read error
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    cartSent = false
                    customDialog?.dismissLoadingDialogue()
                    showToast(Constants.CONNECTION_PROBLEM)
                }
            })

            if(Preferences.getUserDataFromSharedPreferences(this) != null)
                sendAttachmentsCall(user.id!!)

        } else {
            showToast(Constants.NO_INTERNET_CONNECTION)
        }
    }

    private fun sendAttachmentsCall(userId: String){

        val restApis = RetroClient.getClient().create(RestApis::class.java)
//        val deviceId = Preferences.getDeviceIdFromSharedPreferences(this)

        if(files != null && files!!.size > 0) {

            var attachmentBody1: MultipartBody.Part? = null
            var attachmentName1: RequestBody? = null
            var attachmentBody2: MultipartBody.Part? = null
            var attachmentName2: RequestBody? = null
            var attachmentBody3: MultipartBody.Part? = null
            var attachmentName3: RequestBody? = null

            if(files!![0] != null){
                attachmentName1 = RequestBody.create(MediaType.parse("image/*"), files!![0])
                attachmentBody1 = MultipartBody.Part.createFormData("attachment1", files!![0].name, attachmentName1)
            }

            if(files!!.size > 1){
                attachmentName2 = RequestBody.create(MediaType.parse("image/*"), files!![1])
                attachmentBody2 = MultipartBody.Part.createFormData("attachment2", files!![1].name, attachmentName2)
            }

            if(files!!.size > 2){
                attachmentName3 = RequestBody.create(MediaType.parse("image/*"), files!![2])
                attachmentBody3 = MultipartBody.Part.createFormData("attachment3", files!![2].name, attachmentName3)
            }

            var name = RequestBody.create(MediaType.parse("text/plain"), "name")

            var attachmentsName: MutableList<RequestBody> = ArrayList()
            var attachmentsBody: MutableList<MultipartBody.Part> = ArrayList()

            var cartParams = CartParams()

            var userIdRB = RequestBody.create(MediaType.parse("text/plain"), userId)
            var deviceIdRB = RequestBody.create(MediaType.parse("text/plain"), Preferences.getUserDataFromSharedPreferences(this).deviceId)
//            cartParams.userId = userId
//            cartParams.deviceId = deviceId

            val sendAttachmentsCall = restApis.sendPrescriptions(Preferences.getAuthCodeFromSharedPreferences(this),
                    userIdRB, deviceIdRB, attachmentBody1, attachmentBody2, attachmentBody3)
            sendAttachmentsCall.enqueue(object : Callback<GeneralResponse> {
                override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                    if (response.isSuccessful) {
                        val generalResponse = response.body()
                        if (generalResponse != null && generalResponse?.isSuccess && generalResponse?.results.attachments != null) {

                            if(reOrder) {
                                val attachments = generalResponse.results.attachments

                                if (prescriptionList != null && prescriptionList?.size!! > 0) {
                                    val index = prescriptionList?.size?.minus(1)

                                    for (i in attachments.indices.reversed()) {
                                        prescriptionList?.set(index!!, attachments[i])
                                    }
                                } else {
                                    prescriptionList = attachments as ArrayList<Prescription>?
                                }
                            }
//
                            prescriptionsSent = true

                            goToCheckout()
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

                                Utills.invalidTokenLogout(this@CartKActivity, mError.msg, authorization)
                            } catch (e: IOException) {
                                // handle failure to read error
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    prescriptionsSent = false
                    customDialog?.dismissLoadingDialogue()
                    showToast(Constants.CONNECTION_PROBLEM)
                }
            })
        } else {

            var cartParams = CartParams()

            cartParams.userId = userId
            cartParams.deviceId = Preferences.getUserDataFromSharedPreferences(this).deviceId

            val removeAttachmentsCall = restApis.removePrescriptions(Preferences.getAuthCodeFromSharedPreferences(this),
                    Constants.HEADER_CONTENT_TYPE_VALUE, cartParams)
            removeAttachmentsCall.enqueue(object : Callback<GeneralResponse> {
                override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                    if (response.isSuccessful) {
                        val generalResponse = response.body()
                        if (generalResponse != null && generalResponse?.isSuccess) {
//
                            prescriptionsSent = true

                            goToCheckout()
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

                                Utills.invalidTokenLogout(this@CartKActivity, mError.msg, authorization)
                            } catch (e: IOException) {
                                // handle failure to read error
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    prescriptionsSent = false
                    customDialog?.dismissLoadingDialogue()
                    showToast(Constants.CONNECTION_PROBLEM)
                }
            })
        }
    }

    private fun goToCheckout(){

        if(Preferences.getUserDataFromSharedPreferences(this) != null) {
            if (cartSent && prescriptionsSent) {
                checkout()
            }
        } else {
            if (cartSent) {
                checkout()
            }
        }
    }

    private fun checkout(){
        customDialog?.dismissLoadingDialogue()
        openPlaceOrder()
        Preferences.addCartUpdateBooleanToSharedPreferences(this@CartKActivity, false)
    }

    fun sendSmsOrder() {
        placeOrder(true, Constants.ORDER_PICK_UP, false, "")
        smsOrderSent = true
        Utills.sendSms(this@CartKActivity, selectedPharmacy?.placeNumber, Preferences.getCartMessageFromSharedPreferences(this@CartKActivity))
    }

    private fun showSmsDialog(){
        customDialog?.showGeneralDialog(nightMode, Constants.SMS_ORDER_USER_DIALOG_TITLE, Constants.SMS_ORDER_USER_DIALOG_MESSAGE,
                Constants.SMS_ORDER_USER_DIALOG_BUTTON_1, Constants.SMS_ORDER_USER_DIALOG_BUTTON_2, null, false)
    }

    fun setQuantityManually(position: Int, quantity: Int){
        var intent = Intent(this, SetQuantityActivity::class.java)
        intent.putExtra("type", Constants.SET_QUANTITY)
        intent.putExtra("position", position)
        intent.putExtra("quantity", quantity)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when(requestCode) {

            1 -> {
                if (resultCode == Activity.RESULT_OK) {
                    var quantity = data?.getIntExtra("quantity", 1)
                    var position = data?.getIntExtra("position", -1)

                    if (position != null && position > -1) {
                        if (quantity != null) {
//                        medicinesList!!.get(position).quantity = quantity
//                        updateCart()
                            setQuantity(position, quantity)
                        }
                    }
                } else {

                }
            }

            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    val imageUri = result.uri

//                val thread = object : Thread() {
//                    override fun run() {
                    createDirectoryAndSaveFile(imageUri!!, Utills.getPrescriptionFileName())
//                    }
//                }
//                thread.start()
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    val error = result.error
                }
            }

            REQUEST_CREATE_MEDICINE -> {
                if (resultCode == Activity.RESULT_OK) {
                    var customMedicine = data?.getSerializableExtra("med") as MedicineDetails

                    if (customMedicine != null) {

                        if(medicinesList == null)
                            medicinesList = ArrayList()

                        medicinesList?.add(customMedicine)
                    }
                } else {

                }
            }

            REQUEST_PLACE_ORDER -> {
                if(resultCode == Activity.RESULT_OK){
                    var intent = Intent(this, OrdersUserActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        var result = false
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            result = true
        when (requestCode) {

            REQUEST_READ_WRITE_PERMISSION ->

                if (result) {
                    startPickerActivity()
                } else {

                }
        }
    }

    public override fun onResume() {

        if (Preferences.getCartUpdateBooleanFromSharedPreferences(this) ||
                Preferences.getPrescriptionImagePathsFromSharedPreferences(this) != null) {
            medicinesList = Preferences.getCartItemsFromSharedPreferences(this)
            prescriptionList = Preferences.getPrescriptionImagePathsFromSharedPreferences(this)

            populateList()
        }

        selectedPharmacy = Preferences.getSelectedPharmacyFromSharedPreferences(this)
        setDoneButton()

        if(Preferences.getCartUpdateBooleanFromSharedPreferences(this)){
            cartSent = false
            prescriptionsSent = false
        }

        if(smsOrderSent)
            removeAllFromCart()

        super.onResume()

    }

    fun removeAllFromCart() {

//        customDialog?.dismissCartCleaningDialogue()

        Preferences.removeCartItemsFromSharedPreferences(this)
        Preferences.removePrescriptionImagePathsFromSharedPreferences(this)
        Preferences.removeSelectedPharmacyFromSharedPreferences(this)
//        finish()

        Utills.goToHome(this)
    }

    private fun showConfirmOrderDialog() {

//        customDialog.showGeneralDialog(nightMode, Constants.CONFIRM_ORDER_DIALOG_TITLE, Constants.CONFIRM_ORDER_DIALOG_MESSAGE,
//                Constants.CONFIRM_ORDER_DIALOG_BUTTON_1, Constants.CONFIRM_ORDER_DIALOG_BUTTON_2)

        customDialog?.showConfirmOrderDialog(nightMode, "", "", "", "", selectedPharmacy?.deliveryAvailable, Preferences.getUserDataFromSharedPreferences(this).addresses)
    }

    fun placeOrder(send: Boolean, orderType: String, goHome: Boolean, address: String){
        if(send){
            if (Internet.isAvailable(this)) {
                //            Loading.show(activity, false, "Please wait...");

                customDialog?.showLoadingDialogue()

                var userId = ""

                var user = Preferences.getUserDataFromSharedPreferences(this)

                var checkoutParams = CheckoutParams()

                if(user != null) {
                    userId = user.id!!
                    checkoutParams.message = "Order Placed by " + user.firstName + " " + user.lastName
                } else {
                    userId = "-1"
                    checkoutParams.message = "New Order Placed"
                }

                checkoutParams.placeLat = Constants.CURRENT_LAT
                checkoutParams.placeLng = Constants.CURRENT_LNG
                checkoutParams.pharmacyId = selectedPharmacy?.id

                if(address != null && address != "")
                    checkoutParams.userAddress = address
                else {
                    var user = Preferences.getUserDataFromSharedPreferences(this)
                    if(user?.addresses != null && user.addresses?.size!! > 0)
                        checkoutParams.userAddress = user.addresses?.get(0)
                }

                if(orderType == "Pick Up")
                    checkoutParams.orderType = Constants.ORDER_PICK_UP
                else if(orderType == "Delivery")
                    checkoutParams.orderType = Constants.ORDER_DELIVERY

                checkoutParams.userId = userId
                checkoutParams.deviceId = Preferences.getDeviceIdFromSharedPreferences(this)


                val restApis = RetroClient.getClient().create(RestApis::class.java)

                val placeOrderCall = restApis.checkoutCall(Preferences.getAuthCodeFromSharedPreferences(this),
                        Constants.HEADER_CONTENT_TYPE_VALUE, checkoutParams)
                placeOrderCall.enqueue(object : Callback<GeneralResponse> {
                    override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                        if (response.isSuccessful) {
                            customDialog?.dismissLoadingDialogue()
                            if (response.body() != null && response.body()!!.isSuccess) {
                                customDialog?.dismissLoadingDialogue()
//                                showCartCleaningDialog()
                                if(goHome) {
                                    removeAllFromCart()
                                    showToast("Order Placed")
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                        showToast(Constants.CONNECTION_PROBLEM)
                    }
                })
            } else {

            }
        }

        Preferences.removeSelectedPharmacyFromSharedPreferences(this)
    }

    private fun startPickerActivity() {

        CropImage.activity()
                .setShowCropOverlay(true)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAllowRotation(true)
                .setCropMenuCropButtonTitle("Done")
                .start(this)
    }

    private fun createDirectoryAndSaveFile(imageToSave: Uri, fileName: String) {

        val originalFile = File(imageToSave.path)

        val direct = File(Environment.getExternalStorageDirectory().toString() + "/PharmedSK")

        if (!direct.exists()) {
            val mainDirectory = File("/sdcard/PharmedSK/")
            mainDirectory.mkdirs()
        }

        val subDirect = File(Environment.getExternalStorageDirectory().toString() + "/PharmedSK/Prescriptions")

        if (!subDirect.exists()) {
            val subDirectory = File("/sdcard/PharmedSK/Prescriptions")
            subDirectory.mkdirs()
        }

        val file = File(File("/sdcard/PharmedSK/Prescriptions"), fileName)
        if (file!!.exists()) {
            file!!.delete()
        }

        try {

            val compressedImage = Compressor(this)
                    .setMaxWidth(700)
                    .setMaxHeight(700)
                    .setQuality(75)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath("/sdcard/PharmedSK/Prescriptions/")
                    .compressToFile(originalFile)

             val suc = compressedImage.renameTo(file)

            var prescription = Prescription()

            prescription.path = "/sdcard/PharmedSK/Prescriptions/$fileName"
            prescription.select = false

            files?.add(file)
            prescriptionList?.add(prescription)
            Preferences.addPrescriptionImagePathsToSharedPreferences(this, prescriptionList)

            updateCartBoolean()

            setUploadButton()

            populateList()

        } catch (e: Exception) {
            e.printStackTrace()

        }

    }

    private fun removePrescription(path: String, name: String) {

            var file = File(File("/sdcard/PharmedSK/Prescriptions"), name)
            if (file!! != null) {
                if (file!!.exists()) {
                    file!!.delete()
                }
            }
    }

    private fun viewPrescription() {
    }

    private fun askCallPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_READ_WRITE_PERMISSION)
    }

    private fun showLoginDialog(sendSMS: Boolean) {

        if(sendSMS) {
            customDialog?.showGeneralDialog(nightMode, Constants.REGISTER_DIALOG_TITLE, Constants.REGISTER_SMS_DIALOG_MESSAGE,
                    Constants.REGISTER_DIALOG_BUTTON_1, Constants.REGISTER_DIALOG_BUTTON_2, Constants.REGISTER_DIALOG_BUTTON_3, false)
        } else {
            customDialog?.showGeneralDialog(nightMode, Constants.REGISTER_DIALOG_TITLE, Constants.REGISTER_DIALOG_MESSAGE,
                    Constants.REGISTER_DIALOG_BUTTON_1, Constants.REGISTER_DIALOG_BUTTON_2, null, true)
        }
    }

    private fun showLoginDialogAttachment () {
        customDialog?.showGeneralDialog(nightMode, Constants.REGISTER_DIALOG_TITLE, Constants.REGISTER_ATTACHMENT_DIALOG_MESSAGE,
                Constants.REGISTER_DIALOG_BUTTON_1, Constants.REGISTER_DIALOG_BUTTON_2, null, true)
    }

    override fun onClick(v: View?) {

        when (v?.id) {

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

            R.id.back ->

                finish()

            R.id.logo_toolbar_pharmed ->
                Utills.goToHome(this)

            R.id.med_search_toolbar ->

                searchMedicines()

            R.id.search_medicine ->

                searchMedicines()

            R.id.remove_icon -> {

                var selected = false

                for (i in medicinesList!!.indices) {
                    if (medicinesList!![i].select) {
                        selected = true
                        break
                    }
                }

                for (i in prescriptionList!!.indices) {
                    if (prescriptionList!![i].select) {
                        selected = true
                        break
                    }
                }

                if (selected) {
                    customDialog?.showDeleteItemDialog(nightMode)
                } else {
                    showToast("No item selected.")
                }
            }

            R.id.prescription_image ->
                viewPrescription()

            R.id.view_prescription ->
                viewPrescription()

            R.id.prescription ->

                if(userId != null && userId != "") {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        askCallPermission()
                    } else {
                        if (files?.size!! < 3) {
                            startPickerActivity()
                        } else {
                            showToast("Max 3 Prescriptions accepted")
                        }
                    }
                } else {
                    showLoginDialogAttachment()
                }

            R.id.attach_prescription ->

                if(Preferences.getUserDataFromSharedPreferences(this) != null) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        askCallPermission()
                    } else {
                        if (prescriptionList?.size!! < 3) {
                            startPickerActivity()
                        } else {
                            showToast("Max 3 Prescriptions accepted")
                        }
                    }
                } else {
                    showLoginDialogAttachment()
                }

            R.id.search_meds ->

                searchMedicines()

            R.id.create_meds ->{

                startActivity(Intent(this, CustomMedicineActivity::class.java))
            }

            R.id.cancel_selection -> {

                Preferences.removeSelectedPharmacyFromSharedPreferences(this)
                selectedPharmacy = null
                setDoneButton()
            }

            R.id.done ->

                if ((medicinesList != null && medicinesList!!.size > 0) || (prescriptionList != null && prescriptionList!!.size > 0)) {

                    if(Preferences.getCartUpdateBooleanFromSharedPreferences(this)){
                        sendCartCall()
                    } else{
                        openPlaceOrder()
                    }


                } else {
                    showToast("Cart is Empty")
                }
        }
    }

    override fun onPause() {
        val stopMediaPlayer = Intent(this, MediaPlayer::class.java)
        stopService(stopMediaPlayer)

        super.onPause()
    }

    override fun onDestroy() {

        val stopMediaPlayer = Intent(this, MediaPlayer::class.java)
        stopService(stopMediaPlayer)

        super.onDestroy()
    }

    override fun onBackPressed() {

        if(forEdit) {
            forEdit = false
            openPlaceOrder()
        } else {
            super.onBackPressed()
        }
    }
}