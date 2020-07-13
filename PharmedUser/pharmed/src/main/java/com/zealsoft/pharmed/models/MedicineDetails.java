package com.zealsoft.pharmed.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MedicineDetails implements Serializable {

    public String url;;

//    public String medicineDetails;
//    public String medicineDose;
//    public int medicineQuantity;

    public boolean select;

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("productId")
    @Expose
    private String productId;

    @SerializedName("name")
    @Expose
    private String medicineName;

    @SerializedName("price")
    @Expose
    private Integer price;

    @SerializedName("subTotal")
    @Expose
    public Integer subPrice;

    @SerializedName("quantity")
    @Expose
    private int quantity;

    private int minQuantity;

    @SerializedName("ingrediants")
    @Expose
    private String ingredients;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("pack_size")
    @Expose
    private String packSize;

    @SerializedName("manufacturer")
    @Expose
    private String manufacturer;

    @SerializedName("dosage")
    @Expose
    private String dosage;

    @SerializedName("deleted")
    @Expose
    private Boolean deleted;

    @SerializedName("checkout")
    @Expose
    private Boolean checkout;

    @SerializedName("lineItem")
    @Expose
    public Boolean lineItem = false;

    @SerializedName("userId")
    @Expose
    private String userId;

    @SerializedName("status")
    @Expose
    public String status;

    @SerializedName("available")
    @Expose
    public Boolean available;

    @SerializedName("discount")
    @Expose
    public Integer discount;

    @SerializedName("discountTypePercent")
    @Expose
    public Boolean typePercent;

    public boolean added;

    //----------------------------------------------------------------------------------------------
    // Getters & Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPackSize() {
        return packSize;
    }

    public void setPackSize(String packSize) {
        this.packSize = packSize;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getCheckout() {
        return checkout;
    }

    public void setCheckout(Boolean checkout) {
        this.checkout = checkout;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
