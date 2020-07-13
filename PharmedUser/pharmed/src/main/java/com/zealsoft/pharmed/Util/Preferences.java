package com.zealsoft.pharmed.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zealsoft.pharmed.models.MedicineDetails;
import com.zealsoft.pharmed.models.PharmacyDetails;
import com.zealsoft.pharmed.models.Prescription;
import com.zealsoft.pharmed.models.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Preferences {

    //-----------------------------------------------------------------------------------------------------------
    // Add, Get and Remove User from Shared Preferences

    public static void addAuthCodeToSharedPreferences(Context context, String authCode){
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.AUTH_CODE_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.AUTH_CODE_TAG_MODEL, authCode);
        editor.apply();
    }

    public static String getAuthCodeFromSharedPreferences(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.AUTH_CODE_TAG_SP, Context.MODE_PRIVATE);
        return sharedPref.getString(Constants.AUTH_CODE_TAG_MODEL, null);
    }

    public static void removeAuthCodeFromSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.AUTH_CODE_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(Constants.AUTH_CODE_TAG_MODEL).apply();
    }

    //-----------------------------------------------------------------------------------------------------------
    // Add, Get and Remove User from Shared Preferences

    public static void addDeviceIdToSharedPreferences(Context context, String deviceID){
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.INSTALL_EVENT_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.INSTALL_EVENT_TAG_MODEL, deviceID);
        editor.apply();
    }

    public static String getDeviceIdFromSharedPreferences(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.INSTALL_EVENT_TAG_SP, Context.MODE_PRIVATE);
        return sharedPref.getString(Constants.INSTALL_EVENT_TAG_MODEL, null);
    }

    public static void removeDeviceIdFromSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.INSTALL_EVENT_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(Constants.INSTALL_EVENT_TAG_MODEL).apply();
    }

    //-----------------------------------------------------------------------------------------------------------
    // Add, Get and Remove Pharmacy from Shared Preferences

    public static void addPharmacyDataToSharedPreferences(Context context, PharmacyDetails pharmacyAccount){
        Gson gson = new Gson();
        String jsonString = gson.toJson(pharmacyAccount);
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.PHARMACY_DATA_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.PHARMACY_DATA_TAG_MODEL, jsonString);
        editor.apply();
    }

    public static PharmacyDetails getPharmacyDataFromSharedPreferences(Context context){
        Gson gson = new Gson();
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.PHARMACY_DATA_TAG_SP, Context.MODE_PRIVATE);
        return gson.fromJson(sharedPref.getString(Constants.PHARMACY_DATA_TAG_MODEL, null), PharmacyDetails.class);
    }

    public static void removePharmacyDataFromSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.PHARMACY_DATA_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(Constants.PHARMACY_DATA_TAG_MODEL).apply();
    }

    //-----------------------------------------------------------------------------------------------------------
    // Add, Get and Remove User from Shared Preferences

    public static void addUserDataToSharedPreferences(Context context, User user){
        Gson gson = new Gson();
        String jsonString = gson.toJson(user);
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.USER_DATA_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.USER_DATA_TAG_MODEL, jsonString);
        editor.apply();
    }

    public static User getUserDataFromSharedPreferences(Context context){
        Gson gson = new Gson();
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.USER_DATA_TAG_SP, Context.MODE_PRIVATE);
        return gson.fromJson(sharedPref.getString(Constants.USER_DATA_TAG_MODEL, null), User.class);
    }

    public static void removeUserDataFromSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.USER_DATA_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(Constants.USER_DATA_TAG_MODEL).apply();
    }

    //-----------------------------------------------------------------------------------------------------------
    // Add, Get and Remove Pharmacy Image Path from Shared Preferences

    public static void addPharmacyImagePathToSharedPreferences(Context context, String imagePath){
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.PHARMACY_IMAGE_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.PHARMACY_IMAGE_TAG_MODEL, imagePath);
        editor.apply();
    }

    public static String getPharmacyImagePathFromSharedPreferences(Context context){
        Gson gson = new Gson();
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.PHARMACY_IMAGE_TAG_SP, Context.MODE_PRIVATE);
        return sharedPref.getString(Constants.PHARMACY_IMAGE_TAG_MODEL, null);
    }

    public static void removePharmacyImagePathFromSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.PHARMACY_IMAGE_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(Constants.PHARMACY_IMAGE_TAG_MODEL).apply();
    }

//    //-----------------------------------------------------------------------------------------------------------
//    // Add, Get and Remove User Location from Shared Preferences
//
//    public static void addUserLocationToSharedPreferences(Context context, LatLng location){
//        Gson gson = new Gson();
//        String jsonString = gson.toJson(location);
//        SharedPreferences sharedPref = context.getSharedPreferences(Constants.LOCATION_TAG_SP, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString(Constants.LOCATION_TAG_MODEL, jsonString);
//        editor.apply();
//    }
//
//    public static LatLng getUserLocationFromSharedPreferences(Context context){
//        Gson gson = new Gson();
//        SharedPreferences sharedPref = context.getSharedPreferences(Constants.LOCATION_TAG_SP, Context.MODE_PRIVATE);
//        return gson.fromJson(sharedPref.getString(Constants.LOCATION_TAG_MODEL, null), LatLng.class);
//    }
//
//    public static void removeUserLocationFromSharedPreferences(Context context) {
//        SharedPreferences sharedPref = context.getSharedPreferences(Constants.LOCATION_TAG_SP, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.remove(Constants.LOCATION_TAG_MODEL).apply();
//    }

    //-----------------------------------------------------------------------------------------------------------
    // Add, Get and Remove User's Night Mode selection from Shared Preferences

    public static void addNightThemeSelectionToSharedPreferences(Context context, boolean nightTheme){
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.NIGHT_MODE_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(Constants.NIGHT_MODE_TAG_MODEL, nightTheme);
        editor.apply();
    }

    public static boolean getNightThemeSelectionFromSharedPreferences(Context context){
        Gson gson = new Gson();
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.NIGHT_MODE_TAG_SP, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(Constants.NIGHT_MODE_TAG_MODEL, false);
    }

    public static void removeNightThemeSelectionFromSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.NIGHT_MODE_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(Constants.NIGHT_MODE_TAG_MODEL).apply();
    }

    //-----------------------------------------------------------------------------------------------------------
    // Add, Get and Remove NearByPharmacies from Shared Preferences

    public static void addNearByPharmaciesToSharedPreferencesOld(Context context, List<List<PharmacyDetails>> pharmacies){
        Gson gson = new Gson();
        String jsonString = gson.toJson(pharmacies);
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.NEARBY_PHARMACIES_TAG_SP_OLD, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.NEARBY_PHARMACIES_TAG_MODEL_OLD, jsonString);
        editor.apply();
    }

    public static List<List<PharmacyDetails>> getNearByPharmaciesFromSharedPreferencesOld(Context context){
        Gson gson = new Gson();
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.NEARBY_PHARMACIES_TAG_SP_OLD, Context.MODE_PRIVATE);
        Type type = new TypeToken<List<List<PharmacyDetails>>>() {}.getType();
        return gson.fromJson(sharedPref.getString(Constants.NEARBY_PHARMACIES_TAG_MODEL_OLD, null), type);
    }

    public static void removeNearByPharmaciesFromSharedPreferencesOld(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.NEARBY_PHARMACIES_TAG_SP_OLD, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(Constants.NEARBY_PHARMACIES_TAG_MODEL_OLD).apply();
    }

    //-----------------------------------------------------------------------------------------------------------
    // Add, Get and Remove NearByPharmacies from Shared Preferences

    public static void addNearByPharmaciesToSharedPreferences(Context context, List<PharmacyDetails> pharmacies){
        Gson gson = new Gson();
        String jsonString = gson.toJson(pharmacies);
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.NEARBY_PHARMACIES_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.NEARBY_PHARMACIES_TAG_MODEL, jsonString);
        editor.apply();
    }

    public static List<PharmacyDetails> getNearByPharmaciesFromSharedPreferences(Context context){
        Gson gson = new Gson();
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.NEARBY_PHARMACIES_TAG_SP, Context.MODE_PRIVATE);
        Type type = new TypeToken<List<PharmacyDetails>>() {}.getType();
        return gson.fromJson(sharedPref.getString(Constants.NEARBY_PHARMACIES_TAG_MODEL, null), type);
    }

    public static void removeNearByPharmaciesFromSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.NEARBY_PHARMACIES_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(Constants.NEARBY_PHARMACIES_TAG_MODEL).apply();
    }

    //-----------------------------------------------------------------------------------------------------------
    // Add, Get and Remove User's Night Mode selection from Shared Preferences

    public static void addCartUpdateBooleanToSharedPreferences(Context context, boolean updated){
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.CART_UPDATE_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(Constants.CART_UPDATE_TAG_MODEL, updated);
        editor.apply();
    }

    public static boolean getCartUpdateBooleanFromSharedPreferences(Context context){
        Gson gson = new Gson();
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.CART_UPDATE_TAG_SP, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(Constants.CART_UPDATE_TAG_MODEL, false);
    }

    public static void removeCartUpdateBooleanFromSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.CART_UPDATE_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(Constants.CART_UPDATE_TAG_MODEL).apply();
    }

    //-----------------------------------------------------------------------------------------------------------
    // Add, Get and Remove Cart Items from Shared Preferences

    public static void addCartItemsToSharedPreferences(Context context, List<MedicineDetails> medicinesList){
        Gson gson = new Gson();
        String jsonString = gson.toJson(medicinesList);
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.CART_LIST_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.CART_LIST_TAG_MODEL, jsonString);
        editor.apply();

        addCartMessageToSharedPreferences(context, Utills.makeMsgFromCart(medicinesList, ""));
    }

    public static List<MedicineDetails> getCartItemsFromSharedPreferences(Context context){
        Gson gson = new Gson();
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.CART_LIST_TAG_SP, Context.MODE_PRIVATE);
        Type type = new TypeToken<List<MedicineDetails>>() {}.getType();
        return gson.fromJson(sharedPref.getString(Constants.CART_LIST_TAG_MODEL, null), type);
    }

    public static void removeCartItemsFromSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.CART_LIST_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(Constants.CART_LIST_TAG_MODEL).apply();
        Preferences.addCartUpdateBooleanToSharedPreferences(context, true);
    }

    //-----------------------------------------------------------------------------------------------------------
    // Add, Get and Remove Cart Message from Shared Preferences

    public static void addCartMessageToSharedPreferences(Context context, String message){
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.CART_MESSAGE_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.CART_MESSAGE_TAG_MODEL, message);
        editor.apply();
    }

    public static String getCartMessageFromSharedPreferences(Context context){
        Gson gson = new Gson();
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.CART_MESSAGE_TAG_SP, Context.MODE_PRIVATE);
        return sharedPref.getString(Constants.CART_MESSAGE_TAG_MODEL, null);
    }

    public static void removeCartMessageFromSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.CART_MESSAGE_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(Constants.CART_MESSAGE_TAG_MODEL).apply();
    }

    //-----------------------------------------------------------------------------------------------------------
    // Add, Get and Selected Pharmacy Message from Shared Preferences

    public static void addSelectedPharmacyToSharedPreferences(Context context, PharmacyDetails pharmacy){
        Gson gson = new Gson();
        String jsonString = gson.toJson(pharmacy);
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.SELECTED_PHARMACY_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.SELECTED_PHARMACY_TAG_MODEL, jsonString);
        editor.apply();
    }

    public static PharmacyDetails getSelectedPharmacyFromSharedPreferences(Context context){
        Gson gson = new Gson();
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.SELECTED_PHARMACY_TAG_SP, Context.MODE_PRIVATE);
        return gson.fromJson(sharedPref.getString(Constants.SELECTED_PHARMACY_TAG_MODEL, null), PharmacyDetails.class);
    }

    public static void removeSelectedPharmacyFromSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.SELECTED_PHARMACY_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(Constants.SELECTED_PHARMACY_TAG_MODEL).apply();
    }

    //----------------------------------------------------------------------------------------------
    // Save FCM Token in the Preferences
    public static void saveFcmTokenToSharedPreferences(Context context , String fcmToken){
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.FCM_TOKEN_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.FCM_TOKEN_TAG_MODEL, fcmToken);
        editor.apply();
    }

    //----------------------------------------------------------------------------------------------
    // get FCM Token From Shared Preferences
    public static String getFcmTokenFromSharedPreferences(Context context){
        String fcmToken = "";
        if(context!=null){
            try{
                SharedPreferences sharedPref = context.getSharedPreferences(Constants.FCM_TOKEN_TAG_SP, Context.MODE_PRIVATE);
                fcmToken = sharedPref.getString(Constants.FCM_TOKEN_TAG_MODEL, "");
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return fcmToken;
    }

    //----------------------------------------------------------------------------------------------
    // put the Flag to check whether FCM is already Sent to Server or not
    public static void setFcmFlagToSharedPreferences(Context context, boolean fcmSent){
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.FCM_TOKEN_SENT_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(Constants.FCM_TOKEN_SENT_TAG_MODEL, fcmSent);
        editor.apply();
    }

    //----------------------------------------------------------------------------------------------
    // get FCM Token Sent Flag Value
    public static boolean getFcmFlagFromSharedPreferences(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.FCM_TOKEN_SENT_TAG_SP, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(Constants.FCM_TOKEN_SENT_TAG_MODEL, false);
    }

    //-----------------------------------------------------------------------------------------------------------
    // Add, Get and Remove User's Notifications selection from Shared Preferences

    public static void addNotificationsSelectionToSharedPreferences(Context context, boolean notifications){
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.NOTIFICATIONS_ON_OFF_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(Constants.NOTIFICATIONS_ON_OFF_TAG_MODEL, notifications);
        editor.apply();
    }

    public static boolean getNotificationsSelectionFromSharedPreferences(Context context){
        Gson gson = new Gson();
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.NOTIFICATIONS_ON_OFF_TAG_SP, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(Constants.NOTIFICATIONS_ON_OFF_TAG_MODEL, true);
    }

    public static void removeNotificationsSelectionFromSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.NOTIFICATIONS_ON_OFF_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(Constants.NOTIFICATIONS_ON_OFF_TAG_MODEL).apply();
    }

    //-----------------------------------------------------------------------------------------------------------
    // Add, Get and Remove User's Voice Over selection from Shared Preferences

    public static void setOrdersCountToSharedPreferences(Context context, int count){
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.ORDER_COUNT_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Constants.ORDER_COUNT_TAG_MODEL, count);
        editor.apply();
    }

    public static int getOrdersCountFromSharedPreferences(Context context){
        Gson gson = new Gson();
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.ORDER_COUNT_TAG_SP, Context.MODE_PRIVATE);
        return sharedPref.getInt(Constants.ORDER_COUNT_TAG_MODEL, 0);
    }

    public static void removeOrdersCountFromSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.ORDER_COUNT_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(Constants.ORDER_COUNT_TAG_MODEL).apply();
    }

    //-----------------------------------------------------------------------------------------------------------
    // Add, Get and Remove User's Voice Over selection from Shared Preferences

    public static void addOrdersCountChangeBooleanToSharedPreferences(Context context, boolean change){
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.ORDER_COUNT_CHANGE_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(Constants.ORDER_COUNT_CHANGE_TAG_MODEL, change);
        editor.apply();
    }

    public static boolean getOrdersCountChangeBooleanFromSharedPreferences(Context context){
        Gson gson = new Gson();
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.ORDER_COUNT_CHANGE_TAG_SP, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(Constants.ORDER_COUNT_CHANGE_TAG_MODEL, true);
    }

    public static void removeOrdersCountChangeBooleanFromSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.ORDER_COUNT_CHANGE_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(Constants.ORDER_COUNT_CHANGE_TAG_MODEL).apply();
    }

    //-----------------------------------------------------------------------------------------------------------
    // Add, Get and Remove User's Voice Over selection from Shared Preferences

    public static void addVoiceOverSelectionToSharedPreferences(Context context, boolean audio){
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.VOICE_OVER_ON_OFF_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(Constants.VOICE_OVER_ON_OFF_TAG_MODEL, audio);
        editor.apply();
    }

    public static boolean getVoiceOverSelectionFromSharedPreferences(Context context){
        Gson gson = new Gson();
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.VOICE_OVER_ON_OFF_TAG_SP, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(Constants.VOICE_OVER_ON_OFF_TAG_MODEL, true);
    }

    public static void removeVoiceOverSelectionFromSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.VOICE_OVER_ON_OFF_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(Constants.VOICE_OVER_ON_OFF_TAG_MODEL).apply();
    }

    //-----------------------------------------------------------------------------------------------------------
    // Add, Get and Remove User's Notification Tune selection from Shared Preferences

    public static void addNotificationToneSelectionToSharedPreferences(Context context, boolean systemTune){
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.NOTIFICATION_TUNE_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(Constants.NOTIFICATION_TUNE_TAG_MODEL, systemTune);
        editor.apply();
    }

    public static boolean getNotificationToneSelectionFromSharedPreferences(Context context){
        Gson gson = new Gson();
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.NOTIFICATION_TUNE_TAG_SP, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(Constants.NOTIFICATION_TUNE_TAG_MODEL, false);
    }

    public static void removeNotificationToneSelectionFromSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.NOTIFICATION_TUNE_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(Constants.NOTIFICATION_TUNE_TAG_MODEL).apply();
    }

    //-----------------------------------------------------------------------------------------------------------
    // Add, Get and Remove Prescription Image Path from Shared Preferences

    public static void addPrescriptionImagePathsToSharedPreferences(Context context, ArrayList<Prescription> imagePaths){
        Gson gson = new Gson();
        String jsonString = gson.toJson(imagePaths);
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.PRESCRIPTION_IMAGE_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.PRESCRIPTION_IMAGE_TAG_MODEL, jsonString);
        editor.apply();
    }

    public static ArrayList<Prescription> getPrescriptionImagePathsFromSharedPreferences(Context context){
        Gson gson = new Gson();
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.PRESCRIPTION_IMAGE_TAG_SP, Context.MODE_PRIVATE);
        Type type = new TypeToken<ArrayList<Prescription>>() {}.getType();
        return gson.fromJson(sharedPref.getString(Constants.PRESCRIPTION_IMAGE_TAG_MODEL, null), type);
    }

    public static void removePrescriptionImagePathsFromSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.PRESCRIPTION_IMAGE_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(Constants.PRESCRIPTION_IMAGE_TAG_MODEL).apply();
    }

    //-----------------------------------------------------------------------------------------------------------
    // Add, Get and Remove User's Notification Tune selection from Shared Preferences

    public static void addFirstInstallBooleanToSharedPreferences(Context context, boolean firstInstall){
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.FIRST_INSTALL_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(Constants.FIRST_INSTALL_TAG_MODEL, firstInstall);
        editor.apply();
    }

    public static boolean getFirstInstallBooleanFromSharedPreferences(Context context){
        Gson gson = new Gson();
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.FIRST_INSTALL_TAG_SP, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(Constants.FIRST_INSTALL_TAG_MODEL, true);
    }

    public static void removeFirstInstallBooleanFromSharedPreferences(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.FIRST_INSTALL_TAG_SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(Constants.FIRST_INSTALL_TAG_MODEL).apply();
    }
}