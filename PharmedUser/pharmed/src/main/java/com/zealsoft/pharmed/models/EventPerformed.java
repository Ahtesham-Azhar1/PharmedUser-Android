package com.zealsoft.pharmed.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventPerformed {

    @SerializedName("pharmacy_id")
    @Expose
    private String pharmacyId;

    @SerializedName("eventType")
    @Expose
    private String eventType;

    @SerializedName("place_id")
    @Expose
    private String placeId;

    @SerializedName("place_lat")
    @Expose
    private double placeLat;

    @SerializedName("place_lng")
    @Expose
    private double placeLng;

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("deviceId")
    @Expose
    private String deviceId;

    //----------------------------------------------------------------------------------------------
    // Getters & Setters

    public String getPharmacyId() {
        return pharmacyId;
    }

    public void setPharmacyId(String pharmacyId) {
        this.pharmacyId = pharmacyId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public double getPlaceLat() {
        return placeLat;
    }

    public void setPlaceLat(Double placeLat) {
        this.placeLat = placeLat;
    }

    public double getPlaceLng() {
        return placeLng;
    }

    public void setPlaceLng(Double placeLng) {
        this.placeLng = placeLng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
