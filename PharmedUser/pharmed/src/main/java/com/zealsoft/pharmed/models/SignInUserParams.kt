package com.zealsoft.pharmed.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SignInUserParams {

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("password")
    @Expose
    var password: String? = null

    @SerializedName("fcmToken")
    @Expose
    var fcmToken: String? = null

    @SerializedName("deviceId")
    @Expose
    var deviceId: String? = null
}