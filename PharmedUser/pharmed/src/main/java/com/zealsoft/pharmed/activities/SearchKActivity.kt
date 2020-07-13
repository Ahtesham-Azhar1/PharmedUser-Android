package com.zealsoft.pharmed.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.zealsoft.pharmed.R
import com.zealsoft.pharmed.Util.Constants
import com.zealsoft.pharmed.Util.Internet
import com.zealsoft.pharmed.Util.Preferences
import com.zealsoft.pharmed.Util.Utills
import com.zealsoft.pharmed.adapters.MedicineListKAdapter
import com.zealsoft.pharmed.adapters.PharmaciesListKAdapter
import com.zealsoft.pharmed.apis.RestApis
import com.zealsoft.pharmed.apis.RetroClient
import com.zealsoft.pharmed.customWidgets.CustomDialog
import com.zealsoft.pharmed.models.*
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.ArrayList

class SearchKActivity : AppCompatActivity(), View.OnClickListener {

    var isShown: Boolean = false
    private lateinit var statusBg: View
    private lateinit var toolbar: View
    private lateinit var title: TextView
    private lateinit var cartCountToolbar:TextView
    private lateinit var back: ImageView
    private lateinit var cartToolbar:ImageView
    private lateinit var logoToolbar: CircleImageView
    private lateinit var searchScreen: ConstraintLayout
    private lateinit var cartCountSearch: TextView
    private lateinit var cartSearch: ImageView
    private lateinit var root: ConstraintLayout
    private lateinit var panelBg: View
    private lateinit var searchPanel: ConstraintLayout
    private lateinit var searchIcon: ImageView
    private lateinit var openKeyboard:ImageView
    private lateinit var searchEt: EditText
    private lateinit var cancel: TextView
    private lateinit var searchPharmacies:TextView
    private lateinit var searchMedicines:TextView
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var loadingProgress: ProgressBar
    private lateinit var notFoundPrompt: TextView
    private lateinit var searchPharmaciesAdapter: PharmaciesListKAdapter
    private lateinit var searchMedicinesAdapter: MedicineListKAdapter
    private lateinit var searchedPharmacies: MutableList<PharmacyDetails>
    private lateinit var searchedMedicines: List<MedicineDetails>
    private lateinit var searchCall: Call<GeneralResponse>
    private lateinit var restApis: RestApis
    private var searchType: Int = 0
    private var searchString: String? = ""
    private var toast: Toast? = null
    private lateinit var customDialog: CustomDialog
    private var cartSent = false
    private var showingCartDialog = false
    private var showLoading = true
    private var disableCart = false
    private var nightMode = false

    private var detailsFrame: FrameLayout? = null
    private var showDetailFrag = false

    private var mLastClickTime: Long = 0
    private val eventPerformed: EventPerformed? = null
    private var searchPharmacyOnly = false
    private var searchMedicinesOnly = false
    private var searchNearestOnly = false
    private var fromCart = false
    private var orientationChanged = false
    private var selectedPharmacy: PharmacyDetails? = null

    private lateinit var manager: FragmentManager
    private var fragment: Fragment? = null
    private var backPressed: Boolean = false
    private var fromMap = false
    private var show = true

    private var orderToID: String = ""
    private var orderTophone: String = ""
    private var prescriptionAttached = false
    private var order: Order? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_k)

        setViews()
    }

    private fun setViews() {

        statusBg = findViewById(R.id.status_bg)
        toolbar = findViewById(R.id.top)
        title = findViewById(R.id.title)
        title.text = resources.getString(R.string.title_search_screen)
        cartToolbar = findViewById(R.id.cart_toolbar)
        cartToolbar.setOnClickListener(this)
        cartCountToolbar = findViewById(R.id.cart_count)
        logoToolbar = findViewById(R.id.logo_toolbar_pharmed)
        logoToolbar.setOnClickListener(this)
        back = findViewById(R.id.back)
        back.setOnClickListener(this)

        cartSearch = findViewById(R.id.cart_search)
        cartSearch.setOnClickListener(this)
        cartCountSearch = findViewById(R.id.cart_count_search)

        searchScreen = findViewById(R.id.search_screen)
        root = findViewById(R.id.root)
        root.setOnClickListener(this)
        panelBg = findViewById(R.id.panel_bg)
        panelBg.setOnClickListener(this)
        searchPanel = findViewById(R.id.search_panel)
        searchIcon = findViewById(R.id.search_icon)
        searchEt = findViewById(R.id.search)
        cancel = findViewById(R.id.cancel)
        cancel.setOnClickListener(this)
        searchPharmacies = findViewById(R.id.search_pharmacies)
        searchPharmacies.setOnClickListener(this)
        searchMedicines = findViewById(R.id.search_medicines)
        searchMedicines.setOnClickListener(this)
        loadingProgress = findViewById(R.id.loading_progress)
        notFoundPrompt = findViewById(R.id.not_found_prompt)
        searchRecyclerView = findViewById(R.id.searchedData)
        openKeyboard = findViewById(R.id.open_keyboard)
        openKeyboard.setOnClickListener(this)
        customDialog = CustomDialog(this)

        if(intent.hasExtra("attachments")){
            prescriptionAttached = intent.getBooleanExtra("attachments", false)
        }

        if(intent.hasExtra(Constants.INTENT_RE_ORDER)){
            order = intent.getSerializableExtra(Constants.INTENT_RE_ORDER) as Order
        }

        searchedPharmacies = ArrayList()
        searchedMedicines = ArrayList()

        nightMode = Preferences.getNightThemeSelectionFromSharedPreferences(this)

        restApis = RetroClient.getClient().create(RestApis::class.java)

        searchType = 1

//        intent = intent

        searchPharmacyOnly = intent.getBooleanExtra("searchPharmacy", false)
        searchMedicinesOnly = intent.getBooleanExtra("searchMedicine", false)
        searchNearestOnly = intent.getBooleanExtra("searchNearest", false)
        fromMap = intent.getBooleanExtra("map", false)

        fromCart = intent.getBooleanExtra("cart", false)
//        selectedPharmacy = intent.getSerializableExtra("selectedPharmacy") as PharmacyDetails

        if(Preferences.getPharmacyDataFromSharedPreferences(this) != null &&
                Preferences.getUserDataFromSharedPreferences(this) == null){
            cartSearch.visibility = View.GONE
            cartCountSearch.visibility = View.GONE
            disableCart = true
            cancel.visibility = View.VISIBLE
        }

        checkToolbarAndTopBar()
        setViewAccordingToSearchMode()

        openKeyboard.visibility = View.GONE

        if(fromMap){
            updateConstraints(R.layout.activity_search_k)
            runOnUiThread {
                val paramsRoot = root.layoutParams as ConstraintLayout.LayoutParams
                var density = resources.displayMetrics.density
                paramsRoot.setMargins(0, (density * 60).toInt(), 0, 0)
                root.requestLayout()
                searchPharmacies.visibility = View.GONE
                searchMedicines.visibility = View.GONE
                cartSearch.visibility = View.GONE
                cartCountSearch.visibility = View.GONE
                cancel.visibility = View.GONE
                show = false
                animate(true)
            }
        } else {
            root.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_down))
        }

        setTextWatcher()
        setScrollListener()

        checkTheme()
    }

    private fun checkTheme(){
        if(nightMode){
            searchPanel.setBackgroundResource(R.drawable.pd_background_et_round_night_mode)
            searchEt.setTextColor(resources.getColor(R.color.white))
        }
    }

    private fun checkToolbarAndTopBar(){

        Utills.transparentToolbar(this, false)
        val statusBarH = Utills.getStatusBarHeight(this)

        if(!searchPharmacyOnly && !searchMedicinesOnly){


            val paramsSearchPanel = searchPanel.layoutParams as ConstraintLayout.LayoutParams

            var density = resources.displayMetrics.density

            if(fromMap){
                density = 0 * density
            } else {
                density = 20 * density
            }

            paramsSearchPanel.setMargins(10, statusBarH + density.toInt(), 10, 0)

            searchPanel.requestLayout()
        } else {

            statusBg.visibility = View.VISIBLE

            val paramsStatusBg = statusBg.layoutParams as ConstraintLayout.LayoutParams

            paramsStatusBg.height = statusBarH

            statusBg.requestLayout()
        }



        Utills.changeNavigationBarColor(this, Constants.COLOR_BLACK)
    }

    private fun animate(start: Boolean) {

        var density = resources.displayMetrics.density
        if(start) {
            density *= -60
        } else {
            density *= 64
        }

        var startDelay = 0

        val animationEndConstraints = ConstraintSet().apply {
            clone(root)
            clear(root.id, ConstraintSet.BOTTOM)
            connect(root.id, ConstraintSet.BOTTOM, searchScreen.id, ConstraintSet.BOTTOM, 0)
        }

        ViewCompat.animate(root)
                .translationY(density)
                .setStartDelay(startDelay.toLong())
                .setDuration(500.toLong()).setInterpolator(
                        DecelerateInterpolator(1.2f)).withEndAction(Runnable {

                    if(start) {

                        val paramsRoot = root.layoutParams as ConstraintLayout.LayoutParams
                        val density = resources.displayMetrics.density
                        paramsRoot.setMargins(0, (density * 10).toInt(), 0, 0)
                        root.requestLayout()
                        updateConstraints(R.layout.activity_search_k)

                        val fadeIn = AlphaAnimation(0f, 1f)
                        fadeIn.interpolator = DecelerateInterpolator() //add this
                        fadeIn.duration = 300

                        searchPharmacies.visibility = View.VISIBLE
                        searchPharmacies.startAnimation(fadeIn)
                        searchMedicines.visibility = View.VISIBLE
                        searchMedicines.startAnimation(fadeIn)

                        if(Preferences.getPharmacyDataFromSharedPreferences(this) != null &&
                                Preferences.getUserDataFromSharedPreferences(this) == null) {
                            cancel.visibility = View.VISIBLE
                            cancel.startAnimation(fadeIn)
                        } else {
                            if(searchType == 2) {
                                cartSearch.visibility = View.VISIBLE
                                cartSearch.startAnimation(fadeIn)
                                setCartCount()
                                show = true
                            }
                        }

                        openKeyboard()
                    } else {
                        finish()
                        overridePendingTransition(R.anim.stay_still, R.anim.stay_still)
                    }
                }).start()
    }

    private fun updateConstraints(@LayoutRes id: Int) {
        val newConstraintSet = ConstraintSet()
        newConstraintSet.clone(this, id)
        newConstraintSet.applyTo(searchScreen)
    }

    private fun setViewAccordingToSearchMode() {
        if (searchPharmacyOnly) {
            toolbar.visibility = View.VISIBLE
            searchPharmacies.visibility = View.GONE
            searchMedicines.visibility = View.GONE
            title.text = "Select Pharmacy"
            searchEt.hint = "Search Pharmacies"
            cartSearch.visibility = View.GONE
            cartCountSearch.visibility = View.GONE
            cancel.visibility = View.VISIBLE
            selectedPharmacy = Preferences.getSelectedPharmacyFromSharedPreferences(this)
            Utills.hideKeyboardFrom(this, searchEt)

            if(searchedPharmacies != null && searchedPharmacies.size > 0){
                cancelSearchCallNoLoading()
                populatePharmaciesRecycler()
            } else if(order != null && order?.pharmacy != null){
                searchedPharmacies = ArrayList()
                searchedPharmacies.add(order?.pharmacy!!)
                populatePharmaciesRecycler()
            } else if (Constants.cartNearbyList != null && Constants.cartNearbyList.size > 0) {
                searchNearby()
            }

            showPharmacyDetails(null, false)

        } else if (searchMedicinesOnly) {
            toolbar.visibility = View.VISIBLE
            searchPharmacies.visibility = View.GONE
            searchMedicines.visibility = View.GONE
            title.text = "Search Medicines"
            searchEt.hint = "Search Medicines"
            cartSearch.visibility = View.VISIBLE
            setCartCount()
            cancel.visibility = View.GONE
            searchType = 2
            openKeyboard()
            showMedicineDetails(null, false)

            if(searchedMedicines != null && searchedMedicines.size > 0){
                cancelSearchCallNoLoading()
                populateMedicinesRecycler()
            }

        } else {

            if(Preferences.getVoiceOverSelectionFromSharedPreferences(this)) {

                val stopMediaPlayer = Intent(this, MediaPlayer::class.java)
                stopService(stopMediaPlayer)

                val playMediaPlayer = Intent(this, MediaPlayer::class.java)
                playMediaPlayer.putExtra("play", Constants.PLAY_SEARCH)
                startService(playMediaPlayer)
            }

            setCartCount()
            showPharmacyDetails(null, false)
            if (orientationChanged) {
                setViewAccordingToSearchType(searchType)
            }
        }
    }

    private fun openKeyboard() {

        openKeyboard.visibility = View.GONE
        searchEt.requestFocus()
        val imgr = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imgr.showSoftInput(searchEt, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun setScrollListener(){
        searchRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                when {
                    dy > 0 ->
                        Utills.hideKeyboardFrom(this@SearchKActivity, searchEt)
                }
            }
        })
    }
    private fun setViewAccordingToSearchType(type: Int) {

        if (type == 1) {

            searchType = 1
            searchEt.hint = "Search Pharmacies"
            cartSearch.visibility = View.GONE
            cartCountSearch.visibility = View.GONE
            searchPharmacies.setBackgroundResource(R.drawable.pd_background_button_dark)
            searchMedicines.setBackgroundResource(R.drawable.pd_background_button_disabled)
            if(!orientationChanged) {
                renewOnChangeType()
            } else{
                cancelSearchCallNoLoading()
                if(searchedPharmacies != null && searchedPharmacies.isNotEmpty()){

                } else {
                    showPharmaciesNotFound()
                }
            }
            if (showDetailFrag) {
                showPharmacyDetails(null, false)
            }
            if (searchString != null && searchString != "" && !orientationChanged) {
                searchApi(searchString!!)
            } else {
                populatePharmaciesRecycler()
            }
        } else if (type == 2) {

            searchType = 2
            searchEt.hint = "Search Medicines"
            if(Preferences.getPharmacyDataFromSharedPreferences(this) == null) {
                cartSearch.visibility = View.VISIBLE
                setCartCount()
            }
            searchPharmacies.setBackgroundResource(R.drawable.pd_background_button_disabled)
            searchMedicines.setBackgroundResource(R.drawable.pd_background_button_dark)
            if(!orientationChanged) {
                renewOnChangeType()
            } else{
                cancelSearchCallNoLoading()
                if(searchedMedicines != null && searchedMedicines.size > 0){

                } else {
                    showMedicinesNotFound()
                }
            }

            if (showDetailFrag) {
                showMedicineDetails(null, false)
            }
            if (searchString != null && searchString != "" && !orientationChanged) {
                searchApi(searchString!!)
            } else {
                populateMedicinesRecycler()
            }
        }

        orientationChanged = false
    }

    private fun setTextWatcher() {
        searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.toString().trim { it <= ' ' }.length > 2) {
                    cancelSearchCall()
                    searchString = s.toString()
                    searchApi(searchString!!)
                } else {
                    searchString = ""
                    cancelSearchCall()
                    searchRecyclerView.visibility = View.GONE
                    loadingProgress.visibility = View.GONE
                    notFoundPrompt.visibility = View.GONE
                    if (searchPharmacyOnly) {
                        searchedPharmacies = Constants.cartNearbyList
                        populatePharmaciesRecycler()
                    }
                }
            }
        })
    }

    private fun searchApi(query: String) {

        if (Internet.isAvailable(this)) {

            loadingProgress.visibility = View.VISIBLE

            if (searchType == 1) {

                var params = MapParams()

                params.placeLat = Constants.LAT.toDouble()
                params.placeLon = Constants.LNG.toDouble()
                params.queryString = query

                searchCall = restApis.searchNearbyPharmacies(
                        Preferences.getAuthCodeFromSharedPreferences(this),
                        Constants.HEADER_CONTENT_TYPE_VALUE,
                        params)
                searchCall.enqueue(object : Callback<GeneralResponse> {
                    override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                        if (response.isSuccessful) {
                            if (response.body() != null && response.body()!!.isSuccess) {
                                if (response.body()!!.results != null && response.body()!!.results.nearbyPharmacies != null
                                        && response.body()!!.results.nearbyPharmacies.size > 0) {

                                    loadingProgress.visibility = View.GONE
                                    notFoundPrompt.visibility = View.GONE
                                    searchRecyclerView.visibility = View.VISIBLE
                                    searchedPharmacies = response.body()!!.results.nearbyPharmacies

                                    if(searchedPharmacies.size > 50)
                                        searchedPharmacies = searchedPharmacies.subList(0,50)

                                    if(searchPharmacyOnly)
                                        searchedPharmacies = filterPharmacies(searchedPharmacies as MutableList<PharmacyDetails>)

                                    populatePharmaciesRecycler()
                                } else {
                                    showPharmaciesNotFound()
                                }
                            } else {
                                showPharmaciesNotFound()
                            }
                        } else {
                            if (response.code() == 400) {
                                val gson: Gson = GsonBuilder().create()
                                var mError = GeneralResponse()
                                try {
                                    mError = gson.fromJson(response.errorBody()!!.string(), GeneralResponse::class.java)
                                    val authorization = response?.headers()?.get(Constants.HEADER_AUTH)

                                    Utills.invalidTokenLogout(this@SearchKActivity, mError.msg, authorization)
                                } catch (e: IOException) {
                                    // handle failure to read error
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                        loadingProgress.visibility = View.GONE

                        if (searchCall.isExecuted && showLoading) {
                            if (searchType == 1) {
                                loadingProgress.visibility = View.VISIBLE
                            }

                        }
                    }
                })
            } else if (searchType == 2) {

                var params = Params()

                params.medicineSearch = query

                searchCall = restApis.searchMedicines(Preferences.getAuthCodeFromSharedPreferences(this),
                        Constants.HEADER_CONTENT_TYPE_VALUE, params)

                searchCall.enqueue(object : Callback<GeneralResponse> {
                    override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                        if (response.isSuccessful) {
                            if (response.body() != null && response.body()!!.isSuccess) {
                                if (response.body()!!.results != null && response.body()!!.results.searchedMedicines != null
                                        && response.body()!!.results.searchedMedicines.size > 0) {

                                    loadingProgress.visibility = View.GONE
                                    notFoundPrompt.visibility = View.GONE
                                    searchRecyclerView.visibility = View.VISIBLE
                                    searchedMedicines = response.body()!!.results.searchedMedicines

                                    populateMedicinesRecycler()

                                } else {
                                    showMedicinesNotFound()
                                }
                            } else {
                                showMedicinesNotFound()
                            }
                        } else {
                            if (response.code() == 400) {
                                val gson: Gson = GsonBuilder().create()
                                var mError = GeneralResponse()
                                try {
                                    mError = gson.fromJson(response.errorBody()!!.string(), GeneralResponse::class.java)
                                    val authorization = response?.headers()?.get(Constants.HEADER_AUTH)

                                    Utills.invalidTokenLogout(this@SearchKActivity, mError.msg, authorization)
                                } catch (e: IOException) {
                                    // handle failure to read error
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                        loadingProgress.visibility = View.GONE

                        if (searchCall.isExecuted && showLoading) {

                            if (searchType == 2) {
                                loadingProgress.visibility = View.VISIBLE
                            }
                        }
                    }
                })
            }

        } else {
            notFoundPrompt.text = Constants.NO_INTERNET_CONNECTION
            notFoundPrompt.visibility = View.VISIBLE
        }
    }

    private fun cancelSearchCall() {
        if (::searchCall.isInitialized && searchCall != null && searchCall.isExecuted) {
            searchCall.cancel()
            loadingProgress.visibility = View.GONE
            showLoading = true
        }
    }

    private fun cancelSearchCallNoLoading() {
        if (::searchCall.isInitialized && searchCall != null && searchCall.isExecuted) {
            searchCall.cancel()
            loadingProgress.visibility = View.GONE
            showLoading = false
        }
    }

    private fun searchNearby() {
        if (Internet.isAvailable(this)) {

            loadingProgress.visibility = View.VISIBLE

            var userID = ""
            var fcm = ""

            if(Preferences.getUserDataFromSharedPreferences(this) != null) {
                userID = Preferences.getUserDataFromSharedPreferences(this).id!!
                if(Preferences.getFcmTokenFromSharedPreferences(this) != null)
                    fcm = Preferences.getFcmTokenFromSharedPreferences(this)

                var mapParams = MapParams()

                mapParams.placeLat = Constants.LAT.toDouble()
                mapParams.placeLon = Constants.LNG.toDouble()
                mapParams.userId = userID
                mapParams.fcmToken = fcm

                searchCall = restApis.getNearbyPharmacies(Preferences.getAuthCodeFromSharedPreferences(this),
                        Constants.HEADER_CONTENT_TYPE_VALUE, mapParams)
            } else {

                var mapParams = MapParams()

                mapParams.placeLat = Constants.LAT.toDouble()
                mapParams.placeLon = Constants.LNG.toDouble()

                searchCall = restApis.getNearbyPharmacies(Preferences.getAuthCodeFromSharedPreferences(this),
                        Constants.HEADER_CONTENT_TYPE_VALUE, mapParams)
            }

            searchCall.enqueue(object : Callback<GeneralResponse> {
                override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                    if (response.isSuccessful) {
                        if (response.body() != null && response.body()!!.isSuccess) {
                            if (response.body()!!.results != null) {
                                if (response.body()!!.results.nearbyPharmacies != null
                                        && response.body()!!.results.nearbyPharmacies.size > 0) {

                                    loadingProgress.visibility = View.GONE

                                    val generalResponse = response.body()

                                    if (generalResponse != null && generalResponse.results != null
                                            && generalResponse.results.nearbyPharmacies != null
                                            && generalResponse.results.nearbyPharmacies.size > 0) {

                                        searchedPharmacies = filterPharmacies(generalResponse.results.nearbyPharmacies as MutableList<PharmacyDetails>)

                                        populatePharmaciesRecycler()
                                    } else {
                                        loadingProgress.visibility = View.GONE
                                    }

                                } else {
                                    loadingProgress.visibility = View.GONE
                                }

                                if (response.body()!!.results!!.logout) {
                                    showToast(Constants.ANOTHER_LOGIN)
                                    performLogout()
                                }

                            }
                        } else {
                            loadingProgress.visibility = View.GONE
                        }
                    }
                }

                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    loadingProgress.visibility = View.GONE
                }
            })
        } else {
            notFoundPrompt.text = Constants.NO_INTERNET_CONNECTION
            notFoundPrompt.visibility = View.VISIBLE
        }
    }

    private fun performLogout() {

//        Utills.performLogout(this)
//
//        val i = Intent(this, MapActivity::class.java)
//        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        startActivity(i)
//        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
    }

    private fun showPharmaciesNotFound(){
        loadingProgress.visibility = View.GONE
        searchRecyclerView.visibility = View.GONE
        notFoundPrompt.text = Constants.NO_PHARMACY_FOUND
        notFoundPrompt.visibility = View.VISIBLE
        searchedPharmacies = ArrayList()
        Constants.cSearchedPharmacies = ArrayList()
    }

    private fun showMedicinesNotFound(){
        loadingProgress.visibility = View.GONE
        searchRecyclerView.visibility = View.GONE
        notFoundPrompt.text = Constants.NO_MEDICINE_FOUND
        notFoundPrompt.visibility = View.VISIBLE
        searchedMedicines = ArrayList()
        Constants.cSearchedMedicines = ArrayList()
    }

    private fun filterPharmacies(pharmacies: MutableList<PharmacyDetails>): MutableList<PharmacyDetails> {

        for (i in pharmacies.size - 1 downTo 0) {
            val number = pharmacies[i].placeNumber

            if(pharmacies[i].pharmacyStatus != Constants.PHARMACY_STATUS_REGISTERED) {
                if (number != null && number != "") {
                        if (searchPharmacyOnly) {
                            if(pharmacies[i].phoneNumberType != Constants.NUMBER_TYPE_MOBILE)
                                pharmacies.removeAt(i)
                        }
                } else {
                    if (searchPharmacyOnly)
                        pharmacies.removeAt(i)
                }
            }
        }

        return pharmacies
    }

    private fun populatePharmaciesRecycler() {
        searchRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        searchPharmaciesAdapter = PharmaciesListKAdapter(this, searchedPharmacies, nightMode, searchPharmacyOnly, showDetailFrag, false)
        searchRecyclerView.adapter = searchPharmaciesAdapter
        searchRecyclerView.invalidate()
        searchPharmaciesAdapter.notifyDataSetChanged()
        searchRecyclerView.visibility = View.VISIBLE
    }

    private fun populateMedicinesRecycler() {
        searchRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        searchMedicinesAdapter = MedicineListKAdapter(this, searchedMedicines, nightMode, showDetailFrag, disableCart)
        searchRecyclerView.adapter = searchMedicinesAdapter
        searchRecyclerView.invalidate()
    }

    fun hideKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun renewOnChangeType() {
        searchedPharmacies = ArrayList()
        searchedMedicines = ArrayList()

        searchRecyclerView.visibility = View.GONE

        notFoundPrompt.visibility = View.GONE
        cancelSearchCall()
        loadingProgress.visibility = View.GONE
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

    fun showPharmacyDetails(pharmacyDetails: PharmacyDetails?, hideKeyboard: Boolean) {

        if (showDetailFrag) {
            val bundle = Bundle()
            bundle.putSerializable("pharmacyDetails", pharmacyDetails)

            if (hideKeyboard) {
                Utills.hideKeyboardFrom(this, searchEt)
            }
            showDetailsFrag(Constants.TAG_PHARMACY_DETAILS_FRAGMENT, bundle)
        }
    }

    fun showMedicineDetails(medicineDetails: MedicineDetails?, hideKeyboard: Boolean) {

        if (showDetailFrag) {
            val bundle = Bundle()
            bundle.putSerializable("medicineDetails", medicineDetails)

            if (hideKeyboard) {
                Utills.hideKeyboardFrom(this, searchEt)
            }

            showDetailsFrag(Constants.TAG_MEDICINE_DETAILS_FRAGMENT, bundle)
        }
    }

    private fun showDetailsFrag(fragmentTag: String, bundle: Bundle) {
//        manager = supportFragmentManager
//        val fragmentTransaction = manager.beginTransaction()
//        val fragmentPopped = manager.popBackStackImmediate(fragmentTag, 0)
//        fragment = null
//        if (!fragmentPopped && fragment == null) {
//
//            when (fragmentTag) {
//
//                Constants.TAG_PHARMACY_DETAILS_FRAGMENT -> {
//                    fragment = PharmacyDetailsKFragment()
//                    fragment?.arguments = bundle
//                }
//
//                Constants.TAG_MEDICINE_DETAILS_FRAGMENT -> {
//                    fragment = MedicineDetailsKFragment()
//                    fragment?.arguments = bundle
//                }
//            }
//        }
//
//        if (fragment != null) {
//            fragmentTransaction.replace(R.id.details_frame, fragment!!, fragmentTag)
//            fragmentTransaction.commit()
//        }
    }

    fun setCartCount() {
        if(::cartCountSearch.isInitialized)
            Utills.setCartCount(this, cartCountSearch)
    }

    fun setCartSent() {
        cartSent = true
    }

    fun finishOnOrder(){
        showToast("Order Placed")
        finish()
    }

    fun setSelectedPharmacy() {
        if (selectedPharmacy != null) {
            Preferences.addSelectedPharmacyToSharedPreferences(this, selectedPharmacy)
        }
    }

    fun setQuantityManually(position: Int, quantity: Int){
        val intent = Intent(this, SetQuantityActivity::class.java)
        intent.putExtra("type", Constants.SET_QUANTITY)
        intent.putExtra("position", position)
        intent.putExtra("quantity", quantity)
        startActivityForResult(intent, Constants.INTENT_SET_QUANTITY)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {
            12 ->

                if (resultCode == Activity.RESULT_OK) {
                    val medicineDetails = data!!.getSerializableExtra("medicine") as MedicineDetails
                    for (i in searchedMedicines.indices) {
                        if (medicineDetails.id == searchedMedicines.get(i).id) {
                            searchedMedicines.get(i).quantity = medicineDetails.quantity
                            searchMedicinesAdapter.notifyDataSetChanged()
                            searchRecyclerView.invalidate()
                            break
                        }
                    }
                } else {
                }

            Constants.INTENT_SET_QUANTITY ->

                if(resultCode == Activity.RESULT_OK){
                    val quantity = data?.getIntExtra("quantity", 1)
                    val position = data?.getIntExtra("position", -1)

                    if(position != null && position > -1){
                        if (quantity != null) {

                            searchedMedicines[position].quantity = quantity

                            searchMedicinesAdapter.notifyDataSetChanged()
                            searchRecyclerView.invalidate()
                        }
                    }
                } else {

                }

            Constants.INTENT_USER_SIGN_IN -> {
                if (resultCode == Activity.RESULT_OK) {
                    finish()
                } else {

                }
            }

            Constants.INT_CODE_SELECT_PHARMACY -> {
                if(resultCode == Activity.RESULT_OK){
                    selectPharmacy(data!!.getSerializableExtra(Constants.INTENT_SELECT_PHARMACY) as PharmacyDetails)
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    public override fun onResume() {

        if (!searchPharmacyOnly && show) {
            setCartCount()
        }

        if (searchPharmacyOnly) {
            if (cartSent) {
                removeAllFromCart()
            }
        }

        super.onResume()
    }

    override fun onBackPressed() {
        if(!backPressed) {
            finishMenu()
        }
    }

    fun showDialog (){
//        customDialog.showCartCleaningDialogue(nightMode)
        cartSent = false
    }

    fun selectPharmacy(pharmacy: PharmacyDetails){
        val returnIntentOk = Intent()
        returnIntentOk.putExtra(Constants.INTENT_SELECTED_PHARMACY, pharmacy)
        setResult(Activity.RESULT_OK, returnIntentOk)

        finishMenu()
    }

    private fun finishMenu() {
        backPressed = true

        if(fromMap){
            val fadeOut = AlphaAnimation(1f, 0f)
            fadeOut.interpolator = DecelerateInterpolator() //add this
            fadeOut.duration = 300
            searchMedicines.startAnimation(fadeOut)
            searchPharmacies.startAnimation(fadeOut)
            searchRecyclerView.startAnimation(fadeOut)

            if(Preferences.getPharmacyDataFromSharedPreferences(this) != null &&
                    Preferences.getUserDataFromSharedPreferences(this) == null) {
                cancel.startAnimation(fadeOut)
            } else {
                cartCountSearch.startAnimation(fadeOut)
                cartSearch.startAnimation(fadeOut)
            }
            searchEt.text = null
            searchEt.hint = "Search"

            Handler().postDelayed({
//                runOnUiThread {
                searchPharmacies.visibility = View.GONE
                searchMedicines.visibility = View.GONE
                searchRecyclerView.visibility = View.GONE
                cartCountSearch.visibility = View.GONE
                cartSearch.visibility = View.GONE
                cancel.visibility = View.GONE

                animate(false)
//                }
            }, 300)
        } else {
            root.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_up_fast))
            Handler().postDelayed({
                root.visibility = View.GONE
                finish()
                overridePendingTransition(R.anim.stay_still, R.anim.stay_still)
            }, 200)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Save the state of item position
        outState.putBoolean("orientationChanged", true)
        outState.putInt("searchType", searchType)
        outState.putBoolean("pharmacyOnly", searchPharmacyOnly)
        outState.putBoolean("medicineOnly", searchMedicinesOnly)
//        outState.putBoolean("dialogShowing", customDialog.cartDialogShowing())
        outState.putBoolean("showDetailFrag", showDetailFrag)

        val gson = Gson()

        if(searchType == 1) {
            Constants.cSearchedPharmacies = ArrayList()
            Constants.cSearchedPharmacies = searchedPharmacies

        }

        if(searchType == 2) {
            Constants.cSearchedMedicines = ArrayList()
            Constants.cSearchedMedicines = searchedMedicines

        }

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        // Read the state of item position
        orientationChanged = savedInstanceState.getBoolean("orientationChanged")
        searchType = savedInstanceState.getInt("searchType")
        searchPharmacyOnly = savedInstanceState.getBoolean("pharmacyOnly", false)
        searchMedicinesOnly = savedInstanceState.getBoolean("medicineOnly", false)
        showingCartDialog = savedInstanceState.getBoolean("dialogShowing", false)
        showDetailFrag = savedInstanceState.getBoolean("showDetailFrag", false)

        if(searchType == 1){

            searchedPharmacies = Constants.cSearchedPharmacies

        }

        if(searchType == 2){

            searchedMedicines = Constants.cSearchedMedicines
        }

        setViewAccordingToSearchMode()

//        if (showingCartDialog) {
//            customDialog.showCartCleaningDialogue(nightMode)
//            showingCartDialog = false
//        }
    }

    fun removeAllFromCart() {

//        customDialog.dismissCartCleaningDialogue()

        Preferences.removeCartItemsFromSharedPreferences(this)
        Preferences.removePrescriptionImagePathsFromSharedPreferences(this)
        Preferences.removeSelectedPharmacyFromSharedPreferences(this)

        Utills.goToHome(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.search_pharmacies ->

                if (searchType != 1) {

                    setViewAccordingToSearchType(1)
                }

            R.id.search_medicines ->

                if (searchType != 2) {

                    setViewAccordingToSearchType(2)
                }

            R.id.root -> {
                if(!backPressed) {
                    finishMenu()
                }
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

    private fun performClick(v: View?) {

        when (v?.id) {

            R.id.back ->

                finish()

            R.id.logo_toolbar_pharmed ->
                Utills.goToHome(this)

            R.id.cart_toolbar -> {

                startActivity(Intent(this, CartKActivity::class.java))
            }

            R.id.cart_search -> {
                    startActivity(Intent(this, CartKActivity::class.java))
            }

            R.id.open_keyboard ->

                openKeyboard()

            R.id.cancel ->

                if(!backPressed) {
                    finishMenu()
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
}