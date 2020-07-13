package com.zealsoft.pharmed.activities

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import androidx.constraintlayout.widget.ConstraintLayout
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.zealsoft.pharmed.R
import com.zealsoft.pharmed.Util.Constants
import com.zealsoft.pharmed.Util.Preferences
import com.zealsoft.pharmed.Util.Utills

class SetQuantityActivity : AppCompatActivity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private lateinit var root: ConstraintLayout
    private lateinit var setQuantitySection: ConstraintLayout
    private lateinit var setQuantityLabel: TextView
    private lateinit var quantityEt: EditText
    private lateinit var pricingView: ConstraintLayout
    private lateinit var typeBar: ConstraintLayout
    private lateinit var typeAmount: TextView
    private lateinit var typePercentage: TextView
    private lateinit var discCurrencyLabel: TextView
    private lateinit var discountET: EditText
    private lateinit var percentLabel: TextView
    private lateinit var applyAllCB: CheckBox
    private lateinit var currencyLabel: TextView
    private lateinit var price: EditText
    private lateinit var discountLabel: TextView
    private lateinit var discountValueET: EditText
    private lateinit var totalLabel: TextView
    private lateinit var totalValue: EditText
    private lateinit var ok: Button
    private var nightMode: Boolean = false
    private var type = ""
    private var quantity: Int = 1
    private var subPrice: Int = 0
    private var discount: Int = 0
    private var discountValue: Int = 0
    private var position: Int = -1
    private var discountPercentage = true
    private var applyAll = false
    private var toast: Toast? = null
    private lateinit var toastTextView: TextView
    private var currencySymbol: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_quantity)

        setViews()
    }

    private fun setViews() {
        root = findViewById(R.id.root)
        root.setOnClickListener (this)
        setQuantitySection = findViewById(R.id.set_quantity_section)
        setQuantitySection.setOnClickListener(this)
        setQuantityLabel = findViewById(R.id.enter_text_label)
        quantityEt = findViewById(R.id.quantity)
        pricingView = findViewById(R.id.pricing_view)
        percentLabel = findViewById(R.id.percent_label)
        typeBar = findViewById(R.id.discount_type_bar)
        typeAmount = findViewById(R.id.type_amount)
        typeAmount.setOnClickListener(this)
        typePercentage = findViewById(R.id.type_percentage)
        typePercentage.setOnClickListener(this)
        discCurrencyLabel = findViewById(R.id.discount_currency_label)
        discountET = findViewById(R.id.discount)
        applyAllCB = findViewById(R.id.apply_all)
        applyAllCB.setOnCheckedChangeListener(this)
        currencyLabel = findViewById(R.id.currency_label)
        price = findViewById(R.id.price)
        discountLabel = findViewById(R.id.discount_label)
        discountValueET = findViewById(R.id.discount_value)
        totalLabel = findViewById(R.id.total_label)
        totalValue = findViewById(R.id.total_value)
        ok = findViewById(R.id.ok)
        ok.setOnClickListener(this)

        nightMode = Preferences.getNightThemeSelectionFromSharedPreferences(this)

        checkTheme()

        type = intent.getStringExtra("type")
        quantity = intent.getIntExtra("quantity", 1)
        subPrice = intent.getIntExtra(Constants.INTENT_SUB_PRICE, 0)
        discount = intent.getIntExtra(Constants.INTENT_DISCOUNT, 0)
        position = intent.getIntExtra(Constants.INTENT_POSITION, -1)
        currencySymbol = intent.getStringExtra(Constants.INTENT_CURRECY)
        discountPercentage = intent.getBooleanExtra(Constants.INTENT_DISCOUNT_TYPE, true)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        if(type == Constants.SET_QUANTITY) {
            quantityEt.setText(quantity.toString())

            quantityEt.setSelection(quantityEt.text.length)
            openKeyboard(quantityEt)
        }

        if(type == Constants.SET_PRICE || type == Constants.SET_DISCOUNT) {

            // Discount

//            typeBar.visibility = View.VISIBLE
//            applyAllCB.visibility = View.VISIBLE

            pricingView.visibility = View.VISIBLE
            quantityEt.visibility = View.GONE

            setQuantityLabel.text = "Set Discount"

//            if(discountPercentage){
//                discountET.filters = Array<InputFilter>(1) { InputFilter.LengthFilter(3) }
//            } else {
//                discountET.filters = Array<InputFilter>(1) { InputFilter.LengthFilter(Constants.FIELD_PRICE) }
//            }

            discountET.hint = "0"
            if(discount > 0) {
                discountET.setText(discount.toString())
            }

            discountET.setSelection(discountET.text.length)
            openKeyboard(discountET)

            // Pricing

//            setQuantityLabel.text = "Enter Price"
//            currencyLabel.visibility = View.VISIBLE

            currencyLabel.text = Utills.getCurrencyCode(currencySymbol)
            discCurrencyLabel.text = Utills.getCurrencyCode(currencySymbol)

            price.filters = Array<InputFilter>(1) {InputFilter.LengthFilter(Constants.FIELD_PRICE)}
            price.hint = "0"
            if(subPrice > 0) {
                price.setText(subPrice.toString())
                calculateDiscount()
            }

            setDiscountType()

            discountET.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {}

                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {

                    if(discountPercentage) {
                        if(s.isNotEmpty() && s.toString().toInt() > 100){
                            discountET.setText("100")
                            discountET.setSelection(discountET.text.length)
                        }
                    }
                    calculateDiscount()
                }
            })

            price.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable) {}

                override fun beforeTextChanged(s: CharSequence, start: Int,
                                               count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int,
                                           before: Int, count: Int) {
                    calculateDiscount()
                }
            })

        }



    }

    private fun calculateDiscount() {

        if(discountET.text.toString().isNotEmpty()) {
            discount = discountET.text.toString().toInt()
        } else {
            discount = 0
        }

        if(price.text.toString().isNotEmpty()) {
            subPrice = price.text.toString().toInt()
        } else {
            subPrice = 0
        }

        if(discount > 0 && subPrice > 0) {
            if (discountPercentage) {
                var disc = (subPrice.toDouble() / 100) * discount
                discountValue = disc.toInt()
            } else {
                discountValue = discount
            }

            quantity = subPrice - discountValue

            discountValueET.setText(discountValue.toString())
            totalValue.setText(quantity.toString())
        } else {
            discountValueET.setText("0")
            totalValue.setText(subPrice.toString())
        }
    }

    private fun checkTheme(){
        Utills.transparentNavigation(this, true, false)

        if(nightMode){
            setQuantitySection.setBackgroundResource(R.drawable.pd_background_et_round_night_mode)
            setQuantityLabel.setTextColor(resources.getColor(R.color.textColorNightMode))
            quantityEt.setTextColor(resources.getColor(R.color.textColorNightMode))
            typeBar.setBackgroundResource(R.drawable.pd_background_et_round_night_mode)
            applyAllCB.setTextColor(resources.getColor(R.color.textColorNightMode))
            currencyLabel.setTextColor(resources.getColor(R.color.textColorNightMode))
            percentLabel.setTextColor(resources.getColor(R.color.textColorNightMode))
        }
    }

    private fun setDiscountType() {
        if(discountPercentage) {
            typePercentage.setTextColor(resources.getColor(R.color.white))
            typePercentage.setBackgroundResource(R.drawable.pd_background_button_dark)
            typeAmount.setTextColor(resources.getColor(R.color.grey))
            typeAmount.setBackgroundResource(android.R.color.transparent)
            percentLabel.visibility = View.VISIBLE
            discCurrencyLabel.visibility = View.GONE
            discountET.filters = Array<InputFilter>(1) { InputFilter.LengthFilter(3) }
        } else {
            typePercentage.setTextColor(resources.getColor(R.color.grey))
            typePercentage.setBackgroundResource(android.R.color.transparent)
            typeAmount.setTextColor(resources.getColor(R.color.white))
            typeAmount.setBackgroundResource(R.drawable.pd_background_button_dark)
            percentLabel.visibility = View.GONE
            discCurrencyLabel.visibility = View.VISIBLE
            discountET.filters = Array<InputFilter>(1) { InputFilter.LengthFilter(Constants.FIELD_PRICE) }
        }

        calculateDiscount()
    }

    private fun openKeyboard(view: View) {

        view.requestFocus()
        val imgr = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imgr.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun showToast(text: String) {

        if (toast != null) {
            toast?.cancel()
        }

        toast = Toast.makeText(this, text, Toast.LENGTH_LONG)

        val inflater = layoutInflater

        val layout = layoutInflater.inflate(R.layout.custom_toast_design,
                findViewById<ViewGroup>(R.id.toast_root))


        toastTextView = layout.findViewById<View>(R.id.toast_text) as TextView
        toastTextView.text = text

        toast?.duration = Toast.LENGTH_LONG
        toast?.view = layout
        toast?.show()
    }

    override fun onClick(v: View?) {
        when(v?.id){

            R.id.root ->
                finish()

            R.id.type_amount -> {
                discountPercentage = false
                setDiscountType()
            }

            R.id.type_percentage -> {
                discountPercentage = true
                setDiscountType()
            }

            R.id.ok -> {

                if(type == Constants.SET_PRICE || type == Constants.SET_DISCOUNT) {

                    var price = totalValue.text.toString()

                    if (price != null && price != "" && price != "0") {
                        discount = discountET.text.toString().toInt()

                        intent.putExtra(Constants.INTENT_PRICE, price.toInt())
                        intent.putExtra(Constants.INTENT_SUB_PRICE, this.price.text.toString().toInt())
                        intent.putExtra(Constants.INTENT_DISCOUNT, discount)
                        intent.putExtra(Constants.INTENT_POSITION, position)
                        intent.putExtra(Constants.INTENT_DISCOUNT_TYPE, discountPercentage)
                        intent.putExtra(Constants.INTENT_APPLY_ALL, applyAll)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    } else if (price == "0") {
                        showToast("Prince cannot be zero")
                    } else {
                        showToast("Prince cannot be empty")
                    }
                }
//                else if (type == Constants.SET_PRICE) {
//                    if (q != null && q != "" && q != "0") {
//                        quantity = q.toInt()
//
//                        intent.putExtra(Constants.INTENT_PRICE, quantity)
//                        intent.putExtra(Constants.INTENT_SUB_PRICE, price.text.toString().toInt())
//                        intent.putExtra(Constants.INTENT_DISCOUNT, discount)
//                        intent.putExtra(Constants.INTENT_POSITION, position)
//                        intent.putExtra(Constants.INTENT_DISCOUNT_TYPE, discountPercentage)
//                        intent.putExtra(Constants.INTENT_APPLY_ALL, applyAll)
//                        setResult(Activity.RESULT_OK, intent)
//                        finish()
//                    } else if (q == "0") {
//                        showToast("Prince cannot be zero")
//                    } else {
//                        showToast("Prince cannot be empty")
//                    }
//                }
                else {
                    var q = quantityEt.text.toString()

                    if (q != null && q != "" && q != "0") {
                        quantity = q.toInt()

                        intent.putExtra("quantity", quantity)
                        intent.putExtra("position", position)
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    } else if (q == "0") {
                        showToast("Quantity cannot be zero")
                    } else {
                        showToast("Quantity cannot be empty")
                    }
                }
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when(buttonView?.id){

            R.id.apply_all ->
                applyAll = isChecked
        }
    }
}
