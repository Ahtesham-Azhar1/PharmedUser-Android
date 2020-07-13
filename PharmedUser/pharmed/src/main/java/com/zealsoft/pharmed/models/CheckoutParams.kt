package com.zealsoft.pharmed.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CheckoutParams {

    @SerializedName("place_lat")
    @Expose
    var placeLat: String? = null

    @SerializedName("place_lng")
    @Expose
    var placeLng: String? = null

    @SerializedName("pharmacyId")
    @Expose
    var pharmacyId: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("checkOutNote")
    @Expose
    var orderNote: String? = null

    @SerializedName("promoCode")
    @Expose
    var promoCode: String? = null

    @SerializedName("orderType")
    @Expose
    var orderType: String? = null

    @SerializedName("userAddress")
    @Expose
    var userAddress: String? = null

    @SerializedName("order")
    @Expose
    var order: Order? = null

    @SerializedName("userId")
    @Expose
    var userId: String? = null

    @SerializedName("deviceId")
    @Expose
    var deviceId: String? = null

    @SerializedName("audioNote")
    @Expose
    var audioNoteURL: String? = null
}