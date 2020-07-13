package com.zealsoft.pharmed.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Params {

    //----------------------------------------------------------------------------------------------
    // Orders Call

    @SerializedName("userId")
    @Expose
    var userId: String? = null

    @SerializedName("deviceId")
    @Expose
    var deviceId: String? = null

    @SerializedName("orderId")
    @Expose
    var orderId: String? = null

    @SerializedName("status")
    @Expose
    var orderStatus: String? = null

    @SerializedName("pageNumber")
    @Expose
    var orderPageNumber: Int? = null

    @SerializedName("pageSize")
    @Expose
    var orderPageSize: Int? = null

    //----------------------------------------------------------------------------------------------
    // Medicine Search Call

    @SerializedName("search")
    @Expose
    var medicineSearch: String? = null

    //----------------------------------------------------------------------------------------------
    // Event Call Params

    @SerializedName("pharmacyId")
    @Expose
    var pharmacyId: String? = null

    @SerializedName("eventType")
    @Expose
    var eventType: String? = null

    @SerializedName("year")
    @Expose
    var eventYear: String? = null

    @SerializedName("month")
    @Expose
    var eventMonth: String? = null


    //----------------------------------------------------------------------------------------------
    // Promotions Params

    @SerializedName("promotionId")
    @Expose
    var promotionId: String? = null

    //----------------------------------------------------------------------------------------------
    // Emergency Number Params

    @SerializedName("countryCode")
    @Expose
    var countryCode: String? = null
}