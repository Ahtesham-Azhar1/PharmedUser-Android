package com.zealsoft.pharmed.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MapParams {

    @SerializedName("lat")
    @Expose
    var placeLat = 0.0

    @SerializedName("lon")
    @Expose
    var placeLon = 0.0

    @SerializedName("userId")
    @Expose
    var userId: String? = null

    @SerializedName("fcmToken")
    @Expose
    var fcmToken: String? = null

//    queryString
    @SerializedName("queryString")
    @Expose
    var queryString: String? = null
}