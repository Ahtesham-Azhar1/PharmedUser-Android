package com.zealsoft.pharmed.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultResponses {

    @SerializedName("nearby_pharmacies")
    @Expose
    private List<PharmacyDetails> nearbyPharmacies;

    @SerializedName("pharmacy")
    @Expose
    private PharmacyDetails pharmacyAccount;

    @SerializedName("user")
    @Expose
    private User user;

    @SerializedName("medicine_list")
    @Expose
    private List<MedicineDetails> searchedMedicines;

    @SerializedName("cart")
    @Expose
    private MedicineDetails medicineAdded;

    @SerializedName("Order")
    @Expose
    public Order order;

    @SerializedName("order_list")
    @Expose
    public List<Order> ordersList;

    @SerializedName("order_items")
    @Expose
    public List<MedicineDetails> orderItems;

    @SerializedName("attachments_list")
    @Expose
    public List<String> attachmentsList;

    @SerializedName("attachments")
    @Expose
    public List<Prescription> attachments;

    @SerializedName("logOut")
    @Expose
    public Boolean logout;

    @SerializedName("signInCode")
    @Expose
    public String signInCode;

    @SerializedName("AudioNote")
    @Expose
    public String audioNoteURL;

    //----------------------------------------------------------------------------------------------
    // Getters & Setters

    public List<PharmacyDetails> getNearbyPharmacies() {
        return nearbyPharmacies;
    }

    public void setNearbyPharmacies(List<PharmacyDetails> nearbyPharmacies) {
        this.nearbyPharmacies = nearbyPharmacies;
    }

    public PharmacyDetails getPharmacyAccount() {
        return pharmacyAccount;
    }

    public void setPharmacyAccount(PharmacyDetails pharmacyAccount) {
        this.pharmacyAccount = pharmacyAccount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<MedicineDetails> getSearchedMedicines() {
        return searchedMedicines;
    }

    public void setSearchedMedicines(List<MedicineDetails> searchedMedicines) {
        this.searchedMedicines = searchedMedicines;
    }

    public MedicineDetails getMedicineAdded() {
        return medicineAdded;
    }

    public void setMedicineAdded(MedicineDetails medicineAdded) {
        this.medicineAdded = medicineAdded;
    }

}