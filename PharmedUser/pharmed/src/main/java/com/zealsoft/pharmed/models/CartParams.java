package com.zealsoft.pharmed.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartParams {

    @SerializedName("orderId")
    @Expose
    public String orderId;

    @SerializedName("userId")
    @Expose
    public String userId;

    @SerializedName("deviceId")
    @Expose
    public String deviceId;

    @SerializedName("place_lat")
    @Expose
    private String placeLat;

    @SerializedName("place_lng")
    @Expose
    private String placeLng;

    @SerializedName("order_items")
    @Expose
    private List<MedicineDetails> cartList;

    @SerializedName("attachments")
    @Expose
    public List<Prescription> attachments;

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("deliveryCharges")
    @Expose
    public int dCharges;

    @SerializedName("currency")
    @Expose
    public String currnecy;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("typeChange")
    @Expose
    public Boolean typeChange;

    @SerializedName("fcmToken")
    @Expose
    public String fcmToken;

    public String getPlaceLat() {
        return placeLat;
    }

    public void setPlaceLat(String placeLat) {
        this.placeLat = placeLat;
    }

    public String getPlaceLng() {
        return placeLng;
    }

    public void setPlaceLng(String placeLng) {
        this.placeLng = placeLng;
    }

    public List<MedicineDetails> getCartList() {
        return cartList;
    }

    public void setCartList(List<MedicineDetails> cartList) {
        this.cartList = cartList;
    }
}
