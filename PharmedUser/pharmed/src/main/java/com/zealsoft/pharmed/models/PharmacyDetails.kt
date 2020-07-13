package com.zealsoft.pharmed.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.ArrayList

class PharmacyDetails: Serializable {

    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("icon")
    @Expose
    var icon: String? = null

    @SerializedName("place_id")
    @Expose
    var placeId: String? = null

    @SerializedName("place_name")
    @Expose
    var placeName: String? = null

    @SerializedName("contact_person")
    @Expose
    var contactPerson: String? = null

    @SerializedName("place_address")
    @Expose
    var placeAddress: String? = null

    @SerializedName("place_phone")
    @Expose
    var placeNumber: String? = null

    @SerializedName("phoneNumberType")
    @Expose
    var phoneNumberType: String? = null

    @SerializedName("emailAddress")
    @Expose
    var placeEmail: String? = null

    @SerializedName("place_lat")
    @Expose
    var placeLat: Double? = null

    @SerializedName("place_lng")
    @Expose
    var placeLng: Double? = null

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null

    @SerializedName("website")
    @Expose
    var website: String? = null

    @SerializedName("min_order_limit")
    @Expose
    var minOrderLimit: Int? = null

    @SerializedName("close_hr")
    @Expose
    var closeHr: String? = null

    @SerializedName("start_hr")
    @Expose
    var openingHr: String? = null

    @SerializedName("opening_hours")
    @Expose
    var openingHours: List<String>? = null

    @SerializedName("user_id")
    @Expose
    var userId: Any? = null

    @SerializedName("password")
    @Expose
    var password: String? = null

    @SerializedName("count")
    @Expose
    var count: Any? = null

    @SerializedName("forgetPasswordCode")
    @Expose
    var code: String? = null

    @SerializedName("pharmacyStatus")
    @Expose
    var pharmacyStatus: String? = null

    @SerializedName("delivery")
    @Expose
    var deliveryAvailable: Boolean = false

    @SerializedName("currency")
    @Expose
    var currencySymbol: String? = null

    @SerializedName("licenceNo")
    @Expose
    var licenseNumber: String? = null

    @SerializedName("licenceExpiryDate")
    @Expose
    var licenseExpiry: String? = null

    @SerializedName("licencePhoto")
    @Expose
    var licenseURL: String? = null

    @SerializedName("termsAccepted")
    @Expose
    var termsAccepted: Boolean = false

    @SerializedName("verified")
    @Expose
    var verfied: Boolean = false

    @SerializedName("promotionAvailable")
    @Expose
    var promotionAvailable: Boolean = false

//    var identifiedNumber = false
//    var landLineNumber = false
}