package com.zealsoft.pharmed.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.constraintlayout.widget.ConstraintLayout
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.zealsoft.pharmed.R
import com.zealsoft.pharmed.Util.Constants
import com.zealsoft.pharmed.Util.Preferences
import com.zealsoft.pharmed.Util.Utills
import com.zealsoft.pharmed.customWidgets.CustomDialog
import com.zealsoft.pharmed.models.MedicineDetails
import de.hdodenhof.circleimageview.CircleImageView

class MedicineDetailsKActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var medicineDetailScreen: ConstraintLayout
    private lateinit var title: TextView
    private lateinit var cartCount: TextView
    private lateinit var back: ImageView
    private lateinit var cartToolbar:ImageView
    private lateinit var logoToolbar: CircleImageView
    private lateinit var medicineName: TextView
    private lateinit var medicineType:TextView
    private lateinit var medicinePackSize:TextView
    private lateinit var medicineDetailsTv:TextView
    private lateinit var medicineDose:TextView
    private lateinit var webSearchMedicine: TextView
    private lateinit var webSearchMedicineIcon: ImageView
    private lateinit var quantity: EditText
    private lateinit var typeIcon: ImageView
    private lateinit var packSizeIcon:ImageView
    private lateinit var doseIcon:ImageView
    private lateinit var manufacturerIcon:ImageView
    private lateinit var medicineImage: ImageView
    private lateinit var bottom: View
    private lateinit var increaseQuantity:ImageView
    private lateinit var decreaseQuantity:ImageView
    private lateinit var addToCart: Button
    private var medicineDetails: MedicineDetails? = null
    private var nightMode: Boolean = false
    private var toast: Toast? = null
    private var customDialog: CustomDialog? = null
    private var onlyDetails = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine_details_k)

        setViews()
    }

    private fun setViews() {

        title = findViewById(R.id.title)
        title.text = resources.getString(R.string.title_details_screen)
        cartToolbar = findViewById(R.id.cart_toolbar)
        cartToolbar.setOnClickListener(this)
        cartCount = findViewById(R.id.cart_count)
        logoToolbar = findViewById(R.id.logo_toolbar_pharmed)
        logoToolbar.setOnClickListener(this)
        back = findViewById(R.id.back)
        back.setOnClickListener(this)

        medicineDetailScreen = findViewById(R.id.medicine_detail_screen)
        medicineImage = findViewById(R.id.medicine_pic)
        medicineName = findViewById(R.id.medicine_name)
        typeIcon = findViewById(R.id.type_icon)
        medicineType = findViewById(R.id.type)
        packSizeIcon = findViewById(R.id.size_icon)
        medicinePackSize = findViewById(R.id.pack_size)
        doseIcon = findViewById(R.id.dose_icon)
        medicineDose = findViewById(R.id.medicine_dose)
        webSearchMedicine = findViewById(R.id.web_search)
        webSearchMedicine.setOnClickListener(this)
        webSearchMedicineIcon = findViewById(R.id.web_search_icon)
        webSearchMedicineIcon.setOnClickListener(this)
        manufacturerIcon = findViewById(R.id.manufacturer_icon)
        medicineDetailsTv = findViewById(R.id.medicine_details)
        bottom = findViewById(R.id.bottom)
        increaseQuantity = findViewById(R.id.increase_quantity)
        increaseQuantity.setOnClickListener(this)
        quantity = findViewById(R.id.quantity)
        decreaseQuantity = findViewById(R.id.decrease_quantity)
        decreaseQuantity.setOnClickListener(this)
        addToCart = findViewById(R.id.add_to_cart)
        addToCart.setOnClickListener(this)
        customDialog = CustomDialog(this)

        intent = intent

        logoToolbar.visibility = View.GONE
        cartToolbar.visibility = View.VISIBLE
        Utills.setCartCount(this, cartCount)

        medicineDetails = intent.getSerializableExtra(Constants.INTENT_MEDICINE_DETAILS) as MedicineDetails

        onlyDetails = intent.getBooleanExtra(Constants.INTENT_ONLY_DETAILS, false)

        if(Preferences.getPharmacyDataFromSharedPreferences(this) != null &&
                Preferences.getUserDataFromSharedPreferences(this) == null){
            onlyDetails = true
        }

        if(onlyDetails)
            hideCartOptions()
        else {
            Utills.changeNavigationBarColor(this, Constants.COLOR_GREY)
        }

        nightMode = Preferences.getNightThemeSelectionFromSharedPreferences(this)

        checkTheme()

        populateDetails()
    }

    private fun checkTheme() {

        if (nightMode) {
            medicineDetailScreen.setBackgroundResource(R.color.formBackgroundNightMode)
            medicineName.setTextColor(resources.getColor(R.color.textColorNightMode))
            medicineType.setTextColor(resources.getColor(R.color.textColorNightMode))
            medicinePackSize.setTextColor(resources.getColor(R.color.textColorNightMode))
            medicineDetailsTv.setTextColor(resources.getColor(R.color.textColorNightMode))
            medicineDose.setTextColor(resources.getColor(R.color.textColorNightMode))
            quantity.setBackgroundResource(R.drawable.pd_background_et_round_night_mode)
            quantity.setTextColor(resources.getColor(R.color.textColorNightMode))
//            typeIcon.setColorFilter(resources.getColor(R.color.detailsIconsColorNightMode))
//            packSizeIcon.setColorFilter(resources.getColor(R.color.detailsIconsColorNightMode))
//            doseIcon.setColorFilter(resources.getColor(R.color.detailsIconsColorNightMode))
//            manufacturerIcon.setColorFilter(resources.getColor(R.color.detailsIconsColorNightMode))
            if(onlyDetails)
                Utills.changeNavigationBarColor(this, Constants.COLOR_BLACK)
        } else {
            if(onlyDetails)
                Utills.changeNavigationBarColor(this, Constants.COLOR_FORM)
        }


    }

    private fun hideCartOptions(){
        cartToolbar.visibility = View.GONE
        cartCount.visibility = View.GONE
        bottom.visibility = View.GONE
        increaseQuantity.visibility = View.GONE
        decreaseQuantity.visibility = View.GONE
        quantity.visibility = View.GONE
        addToCart.visibility = View.GONE
    }

    private fun populateDetails() {

        medicineName.text = medicineDetails?.getMedicineName()

        if (medicineDetails?.type != null && medicineDetails?.type != "" && medicineDetails?.type != " ") {
            medicineType.text = medicineDetails?.getType()
        } else {
            typeIcon.visibility = View.GONE
            medicineType.visibility = View.GONE
        }

        if (medicineDetails?.dosage != null && medicineDetails?.dosage != "" && medicineDetails?.dosage != "Potency:  "
                && medicineDetails?.dosage != "Volume:  ") {
            medicinePackSize.text = medicineDetails?.dosage
        } else {
            packSizeIcon.visibility = View.GONE
            medicinePackSize.visibility = View.GONE
        }

        if (medicineDetails?.ingredients != null && medicineDetails?.ingredients != "" && medicineDetails?.ingredients != " ") {
            medicineDose.text = medicineDetails?.ingredients
        } else {
            doseIcon.visibility = View.GONE
            medicineDose.visibility = View.GONE
        }

        if (medicineDetails?.manufacturer != null && medicineDetails?.manufacturer != "" && medicineDetails?.manufacturer != " ") {
            medicineDetailsTv.text = medicineDetails?.manufacturer
        } else {
            manufacturerIcon.visibility = View.GONE
            medicineDetailsTv.visibility = View.GONE
        }

        if (medicineDetails?.quantity!! < 1) {
            medicineDetails?.quantity = 1
        }

        quantity.setText(medicineDetails?.quantity.toString() + "")

        getQuantityOnChange()
        setAutoIncrease()
    }

    private fun getQuantityOnChange() {
        quantity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.toString() != "") {
                    try {
                        var qty = Integer.valueOf(s.toString())
                        if (qty > Constants.MAX_QUANTITY) {
                            qty = Constants.MAX_QUANTITY
                            quantity.setText(qty.toString())
                        }
                        medicineDetails?.quantity = qty

                    } catch (e: NumberFormatException) {
                        //                        quantity
                    }

                }
            }
        })
    }

    private fun setAutoIncrease() {
        increaseQuantity.setOnLongClickListener(object : View.OnLongClickListener {

            private val mHandler = Handler()
            private val incrementRunnable = object : Runnable {
                override fun run() {
                    mHandler.removeCallbacks(this)
                    if (increaseQuantity.isPressed) {
                        // increment the counter
                        // display the updated value here, if necessary

                        increaseQuantity()

                        mHandler.postDelayed(this, 100)
                    }
                }
            }

            override fun onLongClick(view: View): Boolean {
                mHandler.postDelayed(incrementRunnable, 0)
                return true
            }
        })

        decreaseQuantity.setOnLongClickListener(object : View.OnLongClickListener {

            private val mHandler = Handler()
            private val decrementRunnable = object : Runnable {
                override fun run() {
                    mHandler.removeCallbacks(this)
                    if (decreaseQuantity.isPressed) {
                        // increment the counter
                        // display the updated value here, if necessary

                        decreaseQuantity()

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

    private fun startWebView(){
//        val webView = Intent(this, WebViewKActivity::class.java)
//        webView.putExtra("url", "https://www.google.com/search?q=" + medicineDetails?.medicineName)
//        startActivity(webView)
    }

    fun showToast(text: String) {

        if (toast != null) {
            toast?.cancel()
        }

        toast = Toast.makeText(this, text, Toast.LENGTH_LONG)

        val inflater = layoutInflater

        val layout = layoutInflater.inflate(R.layout.custom_toast_design,
                findViewById<ViewGroup>(R.id.toast_root))


        val toastTextView = layout.findViewById<View>(R.id.toast_text) as TextView
        toastTextView.text = text

        //        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast?.setDuration(Toast.LENGTH_LONG)
        toast?.setView(layout)
        //        toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast?.show()
    }

    private fun increaseQuantity() {
        medicineDetails?.quantity = medicineDetails?.quantity!! + 1
        if (medicineDetails?.quantity!! > Constants.MAX_QUANTITY) {
            medicineDetails?.quantity = Constants.MAX_QUANTITY
        }

        quantity.clearFocus()
        quantity.setText(medicineDetails?.quantity.toString() + "")
    }

    private fun decreaseQuantity() {

        if (medicineDetails?.quantity!! > 1) {
            medicineDetails?.quantity = medicineDetails?.quantity!! - 1
            quantity.setText(medicineDetails?.quantity.toString() + "")
        }
        quantity.clearFocus()
    }

    public override fun onResume() {

        if(!onlyDetails)
            Utills.setCartCount(this, cartCount)
        super.onResume()
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra(Constants.INTENT_MEDICINE_DETAILS, medicineDetails)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.back ->

                finish()

            R.id.logo_toolbar_pharmed ->
                Utills.goToHome(this)

            R.id.cart_toolbar -> {

                startActivity(Intent(this, CartKActivity::class.java))
            }

            R.id.increase_quantity ->

                increaseQuantity()

            R.id.decrease_quantity ->



                decreaseQuantity()

            R.id.add_to_cart -> {

                if(!Utills.addToCart(this, medicineDetails)) {
                    Utills.setCartCount(this, cartCount)
                    showToast(Constants.ADD_TO_CART)

//                addToCartCall(medicineDetails!!)

                    medicineDetails?.quantity = medicineDetails!!.minQuantity
                    quantity.setText(medicineDetails?.minQuantity.toString())
                    quantity.clearFocus()
                } else {
                    showToast(Constants.ALREADY_ADDED_TO_CART)
                }

                finish()
            }

            R.id.web_search ->
                startWebView()

            R.id.web_search_icon ->
                startWebView()
        }
    }

//    override fun onSuccess(call: Call<*>?, response: Response<*>?, reqCode: Int) {
//
//        val generalResponse = response?.body() as GeneralResponse?
//
//        customDialog?.dismissLoadingDialogue()
//
//        if (generalResponse != null && generalResponse.results != null && generalResponse.results.medicineAdded != null) {
//
//            Utills.addToCart(this, generalResponse.results.medicineAdded)
//
//            showToast(Constants.ADD_TO_CART)
//            Utills.setCartCount(this, cartCount)
//        } else if (generalResponse != null && generalResponse.msg != null && generalResponse.msg == Constants.UPDATED_RESPONSE) {
//            showToast(Constants.UPDATED_QUANTITY)
//        } else {
//            showToast(Constants.ADD_CART_FAILED)
//        }
//    }
//
//    override fun onFailure(call: Call<*>?, error: GenericResponse?, reqCode: Int) {
//        customDialog?.dismissLoadingDialogue()
//        showToast(Constants.ADD_CART_FAILED)
//    }
//
//    override fun onApiCrash(call: Call<*>?, t: Throwable?, reqCode: Int) {
//        customDialog?.dismissLoadingDialogue()
//        showToast(Constants.ADD_CART_FAILED)
//    }
}
