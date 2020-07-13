package com.zealsoft.pharmed.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Order : Serializable{

    @SerializedName("_id")
    var id: String? = null

    @SerializedName("checkoutId")
    var checkoutId: String? = null

    @SerializedName("orderDate")
    var dateTime: String? = null

    @SerializedName("itemCount")
    var itemsCount: Int = 1

    @SerializedName("orderTotal")
    var totalAmount: Int = 0

    @SerializedName("status")
    var orderStatus: String? = null

    @SerializedName("orderType")
    var orderType: String? = null

    @SerializedName("userAddress")
    var userAddress: String? = null

    @SerializedName("checkOutNote")
    var orderNote: String? = null

    @SerializedName("cancellationNote")
    var cancellationNote: String? = null

    @SerializedName("order_items")
    var items: List<MedicineDetails>? = null

    @SerializedName("attachments")
    var prescriptions: ArrayList<Prescription>? = null

    @SerializedName("pharmacy")
    var pharmacy: PharmacyDetails? = null

    @SerializedName("user")
    var user: User? = null

    @SerializedName("typeChange")
    var typeChange = false

    @SerializedName("deliveryCharges")
    var deliveryCharges: Int? = null

    @SerializedName("promoCode")
    var promoCode: String? = null

    @SerializedName("audioNote")
    var audioNoteURL: String? = null




    @SerializedName("pharmacyId")
    var pharmacyId: String? = null

    @SerializedName("pharmacyIcon")
    var pharmacyIcon: String? = null

    @SerializedName("pharmacyName")
    var pharmacyName: String? = null

    @SerializedName("pharmacyAddress")
    var pharmacyAddress: String? = null

    @SerializedName("userId")
    var userId: String? = null

    @SerializedName("userIcon")
    var userPicture: String? = null

    @SerializedName("userName")
    var userName: String? = null

    @SerializedName("userPhone")
    var userPhone: String? = null
}