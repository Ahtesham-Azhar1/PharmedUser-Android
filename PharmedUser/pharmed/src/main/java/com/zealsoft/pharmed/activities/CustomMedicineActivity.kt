package com.zealsoft.pharmed.activities

import android.os.Bundle
import android.os.SystemClock
import android.text.InputFilter
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.zealsoft.pharmed.R
import com.zealsoft.pharmed.Util.Constants
import com.zealsoft.pharmed.Util.Preferences
import com.zealsoft.pharmed.Util.Utills
import com.zealsoft.pharmed.models.MedicineDetails

class CustomMedicineActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    private lateinit var medicineForm: ScrollView
    private lateinit var medicineName: EditText
    private lateinit var medicineTypeHint: TextView
    private lateinit var medicineType: Spinner
    private lateinit var medicinePotency: EditText
    private lateinit var potencyUnitHint: TextView
    private lateinit var potencyUnit: Spinner
    private lateinit var medicineManufacturer: EditText
    private lateinit var medicineQuantity: EditText
    private lateinit var create: Button
    private lateinit var dividerLeft: View
    private lateinit var dividerRight: View
    private lateinit var dividerLabel: TextView
    private var nightMode = false
    private var mLastClickTime: Long = 0
    private var type = ""
    private var unit = ""

    private var etRoundBackground: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_medicine)

        setViews()
    }

    private fun setViews() {

        nightMode = Preferences.getNightThemeSelectionFromSharedPreferences(this)

        medicineForm = findViewById(R.id.medicine_form)
        medicineName = findViewById(R.id.medicine_name)
        medicineTypeHint = findViewById(R.id.medicine_type_hint)
        medicineTypeHint.setOnClickListener(this)
        medicineType = findViewById(R.id.medicine_type)
        medicinePotency = findViewById(R.id.medicine_potency)
        potencyUnitHint = findViewById(R.id.potency_hint)
        potencyUnitHint.setOnClickListener(this)
        potencyUnit = findViewById(R.id.potency)
        medicineManufacturer = findViewById(R.id.medicine_manufacturer)
        medicineQuantity = findViewById(R.id.medicine_quantity)
        create = findViewById(R.id.create)
        create.setOnClickListener(this)
        dividerLeft = findViewById(R.id.divider_left)
        dividerRight = findViewById(R.id.divider_right)
        dividerLabel = findViewById(R.id.optional_divider)

        checkTheme()
        setFieldLimits()
        setTypeSpinner()
        setPotencySpinner()
    }

    private fun checkTheme() {
        if (nightMode) {

            etRoundBackground = R.drawable.pd_background_et_round_night_mode

            medicineForm.setBackgroundResource(R.drawable.pd_background_form_screen_night_mode)
            medicineName.setTextColor(resources.getColor(R.color.textColorNightMode))
            medicineName.setBackgroundResource(etRoundBackground)
            medicineTypeHint.setBackgroundResource(R.drawable.pd_background_dropdown_themed_night_mode)
            medicineType.setBackgroundResource(R.drawable.pd_background_dropdown_themed_night_mode)
            medicinePotency.setTextColor(resources.getColor(R.color.textColorNightMode))
            medicinePotency.setBackgroundResource(etRoundBackground)
            potencyUnitHint.setBackgroundResource(R.drawable.pd_background_dropdown_themed_night_mode)
            potencyUnit.setBackgroundResource(R.drawable.pd_background_dropdown_themed_night_mode)
            medicineManufacturer.setTextColor(resources.getColor(R.color.textColorNightMode))
            medicineManufacturer.setBackgroundResource(etRoundBackground)
            medicineQuantity.setTextColor(resources.getColor(R.color.textColorNightMode))
            medicineQuantity.setBackgroundResource(etRoundBackground)
            medicineManufacturer.setTextColor(resources.getColor(R.color.textColorNightMode))
            medicineManufacturer.setBackgroundResource(etRoundBackground)
//            dividerLeft.setBackgroundResource(R.color.textColorNightMode)
//            dividerRight.setBackgroundResource(R.color.textColorNightMode)
//            dividerLabel.setTextColor(resources.getColor(R.color.textColorNightMode))
        } else {
            etRoundBackground = R.drawable.pd_background_et_round
        }
    }

    private fun setFieldLimits(){
        medicineName.filters += InputFilter.LengthFilter(Constants.FIELD_LINE_ITEM_NAME)
        medicinePotency.filters += InputFilter.LengthFilter(Constants.FIELD_MEDICINE_POTENCY)
        medicineQuantity.filters += InputFilter.LengthFilter(Constants.FIELD_QUANTITY)
        medicineManufacturer.filters += InputFilter.LengthFilter(Constants.FIELD_MANUFACTURER_NAME)
    }

    private fun setTypeSpinner(){

        medicineType.onItemSelectedListener = this

        var filterAdapter = ArrayAdapter(this, R.layout.custom_spinner_item_black_left, Constants.LIST_MEDICINE_TYPES)

        if(nightMode)
            filterAdapter = ArrayAdapter(this, R.layout.custom_spinner_item_white_left, Constants.LIST_MEDICINE_TYPES)

        if(nightMode)
            filterAdapter.setDropDownViewResource(R.layout.custom_spinner_status_dropdown)
        else
            filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        medicineType.adapter = filterAdapter

    }

    private fun setPotencySpinner(){

        potencyUnit.onItemSelectedListener = this

        var filterAdapter = ArrayAdapter(this, R.layout.custom_spinner_item_black_left, Constants.LIST_POTENCY_UNITS)
//                DropDownHintAdapter(this, Constants.LIST_POTENCY_UNITS, R.layout.custom_spinner_item_black_left)

        if(nightMode)
            filterAdapter = ArrayAdapter(this, R.layout.custom_spinner_item_white_left, Constants.LIST_POTENCY_UNITS)

        if(nightMode)
            filterAdapter.setDropDownViewResource(R.layout.custom_spinner_status_dropdown)
        else
            filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        potencyUnit.adapter = filterAdapter

    }

    private fun isValid(): Boolean {
        return medicineName.text.toString().isNotEmpty() && medicineQuantity.text.toString().isNotEmpty()
//                && type != "" && type != "Type" && medicinePotency.text.toString().isNotEmpty()
//                && unit != "" && unit != "Unit"
    }

    private fun setMedicine(){
        var medicine = MedicineDetails()

        medicine.medicineName = medicineName.text.toString()
        medicine.type = type

        if(unit == "ml" || unit == "l")
            medicine.dosage = "Volume: " + medicinePotency.text.toString() + " " + unit
        else
            medicine.dosage = "Potency: " + medicinePotency.text.toString() + " " + unit

        if(medicineManufacturer.text.toString().isNotEmpty())
            medicine.manufacturer = medicineManufacturer.text.toString()

        medicine.quantity = medicineQuantity.text.toString().toInt()
        medicine.available = true
        medicine.price = 0
        medicine.lineItem = true

        var medicines = Preferences.getCartItemsFromSharedPreferences(this)

        if(medicines == null)
            medicines = ArrayList()

        medicines.add(medicine)

        Preferences.addCartItemsToSharedPreferences(this, medicines)
        Preferences.addCartUpdateBooleanToSharedPreferences(this, true)

//        intent.putExtra("med", medicine)
//        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onClick(v: View?) {

        when (v!!.id) {

            R.id.medicine_type_hint -> {
                medicineType.performClick()
            }

            R.id.potency_hint -> {
                potencyUnit.performClick()
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

            R.id.create -> {
                if(isValid()){
                    setMedicine()
                } else {
                    Utills.showToast(this, Constants.FORM_VALIDATION)
                }
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {


    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(parent?.id){
            R.id.medicine_type -> {
                if(position > 0) {
                    type = Constants.LIST_MEDICINE_TYPES[position]
                    medicineTypeHint.visibility = View.GONE
                } else {
                    type = ""
                    medicineTypeHint.visibility = View.VISIBLE
                }
            }

            R.id.potency -> {
                if(position > 0) {
                    unit = Constants.LIST_POTENCY_UNITS[position]
                    potencyUnitHint.visibility = View.GONE
                } else {
                    unit = ""
                    potencyUnitHint.visibility = View.VISIBLE
                }
            }
        }
    }
}