package com.zealsoft.pharmed.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Prescription: Serializable{

    @SerializedName("url")
    var path: String? = null

    @SerializedName("available")
    var available = true

    @SerializedName("price")
    var totalAmount: Int = 0

    @SerializedName("subTotal")
    var subPrice: Int = 0

    @SerializedName("discount")
    var discount: Int = 0

    @SerializedName("discountTypePercent")
    var typePercent: Boolean = false

    var select = false

    var status: String? = null

    var urlB = false
}