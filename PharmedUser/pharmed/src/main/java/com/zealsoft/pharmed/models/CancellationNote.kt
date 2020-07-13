package com.zealsoft.pharmed.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CancellationNote {

    @SerializedName("orderId")
    @Expose
    var orderId: String? = null

    @SerializedName("status")
    @Expose
    var orderStatus: String? = null

    @SerializedName("cancellationNote")
    @Expose
    var cancellationNote: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("typeChange")
    @Expose
    var typeChange: Boolean? = null

    @SerializedName("fcmToken")
    @Expose
    var fcmToken: String? = null
}