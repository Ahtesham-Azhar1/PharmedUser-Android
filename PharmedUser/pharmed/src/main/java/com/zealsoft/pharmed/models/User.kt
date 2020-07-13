package com.zealsoft.pharmed.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class User: Serializable{

    @SerializedName("_id")
    @Expose
    var id: String? = null

    @SerializedName("deviceId")
    @Expose
    var deviceId: String? = null

    @SerializedName("icon")
    @Expose
    var icon: String? = null

    @SerializedName("firstName")
    @Expose
    var firstName: String? = null

    @SerializedName("lastName")
    @Expose
    var lastName: String? = null

    @SerializedName("age")
    @Expose
    var age: String? = null

    @SerializedName("bloodGroup")
    @Expose
    var bloodGroup: String? = null

    @SerializedName("gender")
    @Expose
    var gender: String? = null

    @SerializedName("profession")
    @Expose
    var profession: String? = null

    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("addresses")
    @Expose
    var addresses: ArrayList<String>? = null

    @SerializedName("phone")
    @Expose
    var phone: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("place_lat")
    @Expose
    var placeLat: Double = 0.toDouble()

    @SerializedName("place_lng")
    @Expose
    var placeLng: Double = 0.toDouble()

    @SerializedName("datTime")
    @Expose
    var createdAt: String? = null

    @SerializedName("password")
    @Expose
    var password: String? = null

    @SerializedName("forgetPasswordCode")
    @Expose
    var code: String? = null

    @SerializedName("termsAccepted")
    @Expose
    var termsAccepted: Boolean = false
}