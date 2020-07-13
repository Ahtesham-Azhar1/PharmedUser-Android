package com.zealsoft.pharmed.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zealsoft.pharmed.R;
import com.zealsoft.pharmed.activities.OrderDetailsUserActivity;
import com.zealsoft.pharmed.activities.SearchKActivity;
import com.zealsoft.pharmed.models.MedicineDetails;
import com.zealsoft.pharmed.models.Order;
import com.zealsoft.pharmed.models.Prescription;
import com.zealsoft.pharmed.models.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class Utills {

    public static Toast toast;
    public static int navigationBarHeight = 0;

    public static void transparentToolbar(Activity activity, boolean showShade) {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);

            if (showShade) {
                activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.blackLight));
            } else {
                activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
        }
    }

    public static void transparentNavigation(Activity activity, boolean transparent, boolean showShadow) {

        if (transparent) {
            if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
                setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
            }
            if (Build.VERSION.SDK_INT >= 19) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false);
                if (showShadow) {
                    activity.getWindow().setNavigationBarColor(activity.getResources().getColor(R.color.blackLight));
                } else {
                    activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                activity.getWindow().setNavigationBarColor(activity.getResources().getColor(R.color.mapSimilarColor));
            }
        }
    }

    public static void changeNavigationBarColor(Activity activity, String color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (color.equals(Constants.COLOR_BLACK)) {
                activity.getWindow().setNavigationBarColor(activity.getResources().getColor(R.color.black));
            } else if(color.equals(Constants.COLOR_WHITE)){
                activity.getWindow().setNavigationBarColor(activity.getResources().getColor(R.color.white));
            } else if(color.equals(Constants.COLOR_FORM)){
                activity.getWindow().setNavigationBarColor(activity.getResources().getColor(R.color.formBackground));
            } else if(color.equals(Constants.COLOR_GREY)){
                activity.getWindow().setNavigationBarColor(activity.getResources().getColor(R.color.grey));
            } else {
                activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
            }
//            activity.getWindow().setNavigationBarColor(activity.getResources().getColor(R.color.green));
        }
    }

    private static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public static String sharePharmacyText(String name, String address, String phoneNo, Double lat, Double lng) {

        String message = "#Pharmed" + "\n\n" + "Pharmacy: " + name + "\n" + "Address: " + address;

        if (phoneNo != null) {
            message = message + "\n" + "Contact: " + phoneNo;
        }

        if(lat != null && lng != null){
            message = message.concat("\n\nGet Direction:\nhttps://www.google.com/maps/search/?api=1&query=" + lat + "," + lng);
//            message = message.concat("\n\nhttp://maps.google.com/?q=" + lat + "," + lng);
//            message = message.concat("\n\nhttp://www.google.com/maps/place/" + lat + "," + lng);
//            message = message.concat("\n\nhttp://maps.google.com/maps?saddr=" + lat + "," + lng);
        }

        return message;
    }

    public static boolean hasNavigationBar(Context context) {

        boolean resourceCheck, keysCheck;

        Resources resources = context.getResources();
        int id = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            resourceCheck = resources.getBoolean(id);
        } else {
            resourceCheck = false;// Check for keys
        }
        boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        boolean hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME);
//        if(!hasMenuKey && !hasBackKey){
        if(hasBackKey && hasHomeKey){
            keysCheck = false;
        } else {
            keysCheck = true;
        }

        if(resourceCheck && keysCheck)
            return true;
        else
            return false;
    }

    public static String getNavigationBarSize(Context context) {
        Point appUsableSize = getAppUsableScreenSize(context);
        Point realScreenSize = getRealScreenSize(context);

        // navigation bar on the side
        if (appUsableSize.x < realScreenSize.x) {
//            return new Point(realScreenSize.x - appUsableSize.x, appUsableSize.y);
            navigationBarHeight = realScreenSize.x - appUsableSize.x;
            return Constants.NAVIGATION_BAR_ON_RIGHT;
        }

        // navigation bar at the bottom
        if (appUsableSize.y < realScreenSize.y) {
//            return new Point(appUsableSize.x, realScreenSize.y - appUsableSize.y);
            navigationBarHeight = realScreenSize.y - appUsableSize.y;
            return Constants.NAVIGATION_BAR_AT_BOTTOM;
        }

        // navigation bar is not present
//        return new Point();
        navigationBarHeight = 0;
        return Constants.NAVIGATION_BAR_INVISIBLE;
    }

    public static Point getAppUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static Point getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= 18) {
            display.getRealSize(size);
        }

        return size;
    }

    public static int getNavigationBarHeight(Context context) {

        // navigation bar height
        int navigationBarHeightt = 0;
        int navigationHeight = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (navigationHeight > 0) {
            navigationBarHeight = context.getResources().getDimensionPixelSize(navigationHeight);
//            float density = context.getResources().getDisplayMetrics().density;
//            density = density * navigationBarHeight;
//            navigationBarHeight = Math.round(density);
            return navigationBarHeight;
        }

//        Point appUsableSize = getAppUsableScreenSize(context);
//        Point realScreenSize = getRealScreenSize(context);
//
//        // navigation bar on the side
//        if (appUsableSize.x < realScreenSize.x) {
//
//            navigationBarHeight = realScreenSize.x - appUsableSize.x;
//
//        }
//
//        // navigation bar at the bottom
//        if (appUsableSize.y < realScreenSize.y) {
//            navigationBarHeight = realScreenSize.y - appUsableSize.y;
//        }

//        if (navigationBarHeight > 0) {
//            navigationBarHeight = context.getResources().getDimensionPixelSize(navigationBarHeight);
//        }

        return navigationBarHeight;
    }

    public static int getStatusBarHeight(Context context) {

        // status bar height
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }

        return statusBarHeight;
    }

    public static void goToHome(Context context){

//        Intent goHome = new Intent(context, MapActivity.class)
    }

    public static void openKeyboard(Context context, View view) {
        InputMethodManager imgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String getCompleteToken(String token){

        return "Bearer " + token;
    }

    public static boolean isLansdlineNumber(String number){
        if (number.startsWith("03") || number.startsWith("+923") || number.startsWith("+92 3")) {
            return false;
        } else {
            return true;
        }

//        final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
//        try {
//            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(number, getCountryCode(activity, location));
//            PhoneNumberUtil.PhoneNumberType phoneNumberType = phoneNumberUtil.getNumberType(phoneNumber);
//            return phoneNumberType == PhoneNumberUtil.PhoneNumberType.FIXED_LINE;
//        } catch (final Exception e) {
//
//        }
//        return true;
    }

    public static boolean makeCall(Context context, String phone) {
        if(phone != null && !phone.equals("")) {
            Intent callIntent = new Intent(Intent.ACTION_VIEW);
            callIntent.setData(Uri.parse("tel:" + phone));
            context.startActivity(callIntent);
            return true;
        } else {
            return false;
        }
    }

    public static boolean sendSms(Context context, String phone, String message){

        if(phone != null && !phone.equals("")) {

//            String s = phone.substring(0, 5);
//            if (isLandlineNumber(phone)) {
//                return false;
//            } else {
                message = "#Pharmed\n\n".concat(message);
                Uri uri = Uri.parse("smsto:" + phone);
                Intent smsIntent = new Intent(Intent.ACTION_VIEW, uri);
//        smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", phone);
                smsIntent.putExtra("sms_body", message);
                context.startActivity(smsIntent);
                return true;
//            }
        } else {
            return false;
        }
    }

    public static void sendMms(Context context, String phone, String message, String imageUrl){

        Intent mmsIntent = new Intent(Intent.ACTION_SEND);
        mmsIntent.putExtra("address", phone);
        mmsIntent.putExtra("sms_body", message);
        mmsIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(imageUrl)));
        mmsIntent.setType("image/gif");
        context.startActivity(mmsIntent);
    }

    public static void shareLocation(Context context, String title, String location, String phone, Double lat, Double lng) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");

        String message = sharePharmacyText(title, location, phone, lat, lng);

        share.putExtra(Intent.EXTRA_TEXT, message);
        share.putExtra(Intent.EXTRA_SUBJECT, "");
        context.startActivity(Intent.createChooser(share, "Share Via"));
    }

    public static void getDirections(Context context, Double lat, Double lng) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.google.com").appendPath("maps").appendPath("dir").appendPath("").appendQueryParameter("api", "1")
                .appendQueryParameter("destination", lat + "," + lng);
        String url = builder.build().toString();
        Log.d("Directions", url);
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }

    public static void openWebsite(Context context, String url){

        if (url != null && !url.equals("")) {
            if(!url.startsWith("http://") && !url.startsWith("https://")){
                url = "http://" + url;
            }

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context.startActivity(i);
        }
    }

    public static void sendEmail(Context context, String emailAddress){

        if(emailAddress != null && !emailAddress.equals("")) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", emailAddress, null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "#Pharmed\n\n");
            context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
        }
    }

    public static void setToast(Activity context){
        LayoutInflater inflater = context.getLayoutInflater();

        View layout = inflater.inflate(R.layout.custom_toast_design,
                (ViewGroup) context.findViewById(R.id.toast_root));

// set a message
        TextView text = (TextView) layout.findViewById(R.id.toast_text);
        text.setText("Button is clicked!");

    }

    public static void showToast(Activity activity, String text){

        if(toast != null){
            toast.cancel();
        }

        toast = new Toast(activity);

        LayoutInflater inflater = activity.getLayoutInflater();

        View layout = inflater.inflate(R.layout.custom_toast_design,
                (ViewGroup) ((Activity) activity).findViewById(R.id.toast_root));
        // set a message
        TextView toastTextView = (TextView) layout.findViewById(R.id.toast_text);
        toastTextView.setText(text);

        //        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

//        toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
//        toast.show();

    }

    public static Calendar getDateFromString(String time){

        Calendar cal = Calendar.getInstance();
        String myFormat = "hh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        try {
            cal.setTime(sdf.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return cal;
    }

    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public static String getStringFromBitmap(Bitmap picture){
        String base64Str = null;
        try{
            if(picture!=null){
                try{
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    picture.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    base64Str = Base64.encodeToString(b, Base64.DEFAULT);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return base64Str;
    }

    public static Bitmap getBitmapFromString(String picture){
        byte[] imageAsBytes = Base64.decode(picture.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public static String getCompleteUrl(String url){

        String prefix = Constants.SERVER_IP;
        prefix = prefix.substring(0, prefix.length() -1);

        if(url != null && !url.equals("")){
            if(url.substring(0,1).equals("/")){
                url = prefix + url;
            }
//            if(!url.substring(0, prefix.length()).equals(prefix)){
//                url = prefix + url;
//            }
        }

        return url;
    }

    public static boolean checkIfDay() {
        float ONE_HOUR_MILLIS = 60 * 60 * 1000;

//        SunriseSunsetCalculator calculator = new SunriseSunsetCalculator(location, "America/New_York");

        // Current timezone and date
        TimeZone timeZone = TimeZone.getDefault();
        Date nowDate = new Date();
//        float offsetFromUtc = timeZone.getOffset(nowDate.getTime()) / ONE_HOUR_MILLIS;

        return timeZone.inDaylightTime(nowDate);
//        // Daylight Saving time
//        if (timeZone.useDaylightTime()) {
//            // DST is used
//            // I'm saving this is preferences for later use
//
//            // save the offset value to use it later
//            float dstOffset = timeZone.getDSTSavings() / ONE_HOUR_MILLIS;
//            // DstOffsetValue = dstOffset
//            // I'm saving this is preferences for later use
//
//            // save that now we are in DST mode
//            if (timeZone.inDaylightTime(nowDate)) {
//                // Now you are in use of DST mode
//                // I'm saving this is preferences for later use
//            } else {
//                // DST mode is not used for this timezone
//                // I'm saving this is preferences for later use
//            }
//        }
    }

    public static boolean addToCart(Activity activity, MedicineDetails item){

        List<MedicineDetails> cartItems = Preferences.getCartItemsFromSharedPreferences(activity);
        if(cartItems == null){
            cartItems = new ArrayList<>();
        }

        boolean found = false;

        for(int i = 0; i < cartItems.size(); i++){
            if(item.getId().equals(cartItems.get(i).getId())){
//                int totalQuantity = 0;
//                boolean exception = false;
//
//                try {
//                    totalQuantity = cartItems.get(i).getQuantity() + item.getQuantity();
//                } catch (NumberFormatException e){
//                    exception = true;
//                }
//
//                if(totalQuantity > Constants.MAX_QUANTITY) {
//                    totalQuantity = Constants.MAX_QUANTITY;
//                }
//
//                cartItems.get(i).setQuantity(totalQuantity);
//
////                cartItems.get(i).setQuantity(item.getQuantity());

                found = true;

                break;
            }
        }
        if(!found){
            cartItems.add(item);
        }

        Preferences.addCartItemsToSharedPreferences(activity, cartItems);
        Preferences.addCartUpdateBooleanToSharedPreferences(activity, true);

        if(activity instanceof SearchKActivity)
            ((SearchKActivity) activity).setCartCount();

        return found;
//        if(activity instanceof CartActivity)
//            ((CartActivity) activity).updateCart();
    }

//    public static int checkCartQuantity(Activity activity, MedicineDetails item){
//
//        List<MedicineDetails> cartItems = Preferences.getCartItemsFromSharedPreferences(activity);
//        if(cartItems == null){
//            cartItems = new ArrayList<>();
//        }
//
//        boolean found = false;
//
//        for(int i = 0; i < cartItems.size(); i++){
//            if(item.getId().equals(cartItems.get(i).getId())){
//                int totalQuantity = 0;
//                boolean exception = false;
//
////                try {
////                    totalQuantity = cartItems.get(i).medicineQuantity + item.medicineQuantity;
////                } catch (NumberFormatException e){
////                    exception = true;
////                }
//
////                if(totalQuantity > Constants.MAX_QUANTITY) {
////                    totalQuantity = Constants.MAX_QUANTITY;
////                }
//
////                cartItems.get(i).medicineQuantity = totalQuantity;
//
////                cartItems.get(i).setQuantity(item.getQuantity());
//
//                if(cartItems.get(i).getQuantity() + item.getQuantity() < 1000){
//
//                }
//
//                found = true;
//
//                break;
//            }
//        }
//        if(!found){
//            cartItems.add(item);
//        }
//
//        Preferences.addCartItemsToSharedPreferences(activity, cartItems);
//        Preferences.addCartUpdateBooleanToSharedPreferences(activity, true);
//
//        if(activity instanceof SearchActivity)
//            ((SearchActivity) activity).setCartCount();
//
////        if(activity instanceof CartActivity)
////            ((CartActivity) activity).updateCart();
//    }

    public static String makeMsgFromCart(List<MedicineDetails> medicines, String orderNote){

        String msg = "";

        if(orderNote != null && !orderNote.equals("")){
            msg = msg + "\nOrder Note: " + orderNote + "\n\n";
        }

        msg = msg + "Medicine Order List: \n\n";

        if(medicines != null) {
            for (int i = 0; i < medicines.size(); i++) {
                String medicine = medicines.get(i).getMedicineName() +" (" + medicines.get(i).getType() + ")" + "\n"
                        + medicines.get(i).getDosage() +"\n"
                        + "Quantity: " + medicines.get(i).getQuantity() + "\n\n";

                msg = msg.concat(medicine);
            }
        }

        return msg;
    }

    public static void setCartCount(Activity activity, TextView cartCount){

        if(cartCount != null) {
            List<MedicineDetails> cartItems = Preferences.getCartItemsFromSharedPreferences(activity);
            List<Prescription> prescription = Preferences.getPrescriptionImagePathsFromSharedPreferences(activity);
            if ((cartItems != null && cartItems.size() > 0) || (prescription != null && prescription.size() > 0)) {

                int count = 0;

                if(cartItems != null) {
                    count = cartItems.size();
                }

                if(prescription != null){
                    count += prescription.size();
                }

                if(count >  99){
                    count = 99;
                }

                cartCount.setText(count + "");
                cartCount.setVisibility(View.VISIBLE);
            } else {
                cartCount.setVisibility(View.GONE);
            }
        }
    }

    public static void setOrdersCount(Activity activity, TextView ordersCountTv){

        if(ordersCountTv != null){
            int count = Preferences.getOrdersCountFromSharedPreferences(activity);

            if(count > 0) {
                ordersCountTv.setText(count + "");

                if (count > 99) {
                    count = 99;
                    ordersCountTv.setText(count + "+");
                }

                ordersCountTv.setVisibility(View.VISIBLE);
            } else {
                ordersCountTv.setVisibility(View.GONE);
            }
        }
    }

//    public static void showDetailsFrag(Activity activity, String fragmentTag, Bundle bundle) {
//        FragmentManager manager = ((AppCompatActivity)activity).getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = manager.beginTransaction();
//        boolean fragmentPopped = manager.popBackStackImmediate(fragmentTag, 0);
//        Fragment fragment = null;
////        if(!fragmentTag.equals(Constants.TAG_NEWS_RELATED_FRAGMENT) && !fragmentTag.equals(Constants.TAG_GROUP_NOTIFICATION_FRAGMENT)) {
////        fragment = manager.findFragmentByTag(fragmentTag);
////        }
//        if (!fragmentPopped && fragment == null) {
//
//            switch (fragmentTag) {
//
//                case Constants.TAG_PHARMACY_DETAILS_FRAGMENT:
//                    fragment = new PharmacyDetailsKFragment();
//                    fragment.setArguments(bundle);
//                    break;
//
//                case Constants.TAG_MEDICINE_DETAILS_FRAGMENT:
//                    fragment = new MedicineDetailsKFragment();
//                    fragment.setArguments(bundle);
//                    break;
//
//            }
////            fragmentTransaction.addToBackStack(fragmentTag);
//        }
//
//        if (fragment != null) {
//            fragmentTransaction.replace(R.id.details_frame, fragment, fragmentTag);
//            fragmentTransaction.commit();
//        }
//    }

    public static void sharePlainText(Activity activity){
        Intent intent2 = new Intent();
                intent2.setAction(Intent.ACTION_SEND);
                intent2.setType("text/plain");
                intent2.putExtra(Intent.EXTRA_TEXT,
                        "FIND YOUR NEAREST PHARMACY.... ANYWHERE IN THE WORLD & CONNECT using PHARMED App, Download Now!.\n\nhttps://play.google.com/store/apps/details?id=com.zealsoft.pharmed");
                activity.startActivity(Intent.createChooser(intent2, "Share via"));
    }

    public static void shareImage(Activity activity) {

        Bitmap b = BitmapFactory.decodeResource(activity.getResources(), R.drawable.share_image);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(),
                b, "Title", null);
        Uri imageUri1 =  Uri.parse(path);
//        Intent share1 = new Intent(Intent.ACTION_SEND);
//        share1.setType("image/jpeg");
//        share1.putExtra(Intent.EXTRA_STREAM, imageUri1);
//        startActivity(Intent.createChooser(share1, "Share Image using"));

        Intent share = new Intent(Intent.ACTION_SEND);

        // If you want to share a png image only, you can do:
        // setType("image/png"); OR for jpeg: setType("image/jpeg");
        share.setType("image/*");

        // Make sure you put example png image named myImage.png in your
        // directory
//        String imagePath = pharmacyImagePath;

//        File imageFileToShare = new File(imagePath);

//        Uri uri = Uri.fromFile(file);
        share.putExtra(Intent.EXTRA_TEXT,
                "FIND YOUR NEAREST PHARMACY.... ANYWHERE IN THE WORLD & CONNECT using PHARMED App, Download Now!.\n\nhttps://play.google.com/store/apps/details?id=com.zealsoft.pharmed");
        share.putExtra(Intent.EXTRA_STREAM, imageUri1);

        activity.startActivity(Intent.createChooser(share, "Share Image!"));
    }

//    public static String getAddress(Activity activity, LatLng location){
//
//        String address = "";
//
//        if(location != null) {
//            Geocoder geocoder;
//            List<Address> addresses;
//            geocoder = new Geocoder(activity, Locale.getDefault());
//
//            try {
//                addresses = geocoder.getFromLocation(location.latitude, location.longitude, 5);
//            } catch (IOException e) {
//                e.printStackTrace();
//                addresses = new ArrayList<>();
//            }
//
//            if (addresses != null && addresses.size() > 0) {
//                if (addresses.get(0).getAddressLine(0) != null) {
//                    address = addresses.get(0).getAddressLine(0);
//                }
//            }
//        }
//
//        return address;
//    }
//
//    public static LocationDetails getLocationDetails(Activity activity, LatLng location){
//
//        LocationDetails locationDetails = new LocationDetails();
//
//        if(location != null) {
//            Geocoder geocoder;
//            List<Address> addresses;
//            geocoder = new Geocoder(activity, Locale.getDefault());
//
//            try {
//                addresses = geocoder.getFromLocation(location.latitude, location.longitude, 5);
//            } catch (IOException e) {
//                e.printStackTrace();
//                addresses = new ArrayList<>();
//            }
//
//            if (addresses != null && addresses.size() > 0) {
//                if (addresses.get(0).getAddressLine(0) != null) {
//                    locationDetails.setCountryName(addresses.get(0).getCountryName());
//                    locationDetails.setCountryCode(addresses.get(0).getCountryCode());
//                    locationDetails.setLocality(addresses.get(0).getLocality());
//                    locationDetails.setSubLocality(addresses.get(0).getSubLocality());
//                }
//            }
//        }
//
//        return locationDetails;
//    }

    public static void getFeedback(Context context){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "support@webpharmed.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Pharmed Feedback");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    public static void contactUs(Context context){
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "support@webpharmed.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Pharmed");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    public static String getTypeInfo(MedicineDetails medicine){

        String text = "";

        String packSize = medicine.getPackSize().toLowerCase();
        boolean volume = true;

        if(packSize.contains("sachets") || packSize.contains("sachet") || packSize.contains("doses") ||
                packSize.contains("hypertonic") || packSize.contains("isotonic") || packSize.contains("vial") ||
                packSize.contains("vials") || packSize.contains("cm")){
            text = packSize;
        } else if(packSize.contains("ml")){
            int end = packSize.indexOf("ml") + 2;
//            if(packSize.contains("x") && (packSize.indexOf("x") < packSize.indexOf("ml"))){
//                text = "Volume: " + packSize.substring(packSize.indexOf("x") +1, end);
//            } else {
                text = packSize.substring(0, end);
//            }
        } else if(packSize.contains("g")){
            int end = packSize.indexOf("g") + 1;
//            if(packSize.contains("x") && (packSize.indexOf("x") < packSize.indexOf("g"))){
//                text = "Volume: " + packSize.substring(packSize.indexOf("x") +1, end);
//            } else {
                text = packSize.substring(0, end);
//            }
        } else if(packSize.contains("kg")){
            int end = packSize.indexOf("kg") + 2;
//            if(packSize.contains("x") && (packSize.indexOf("x") < packSize.indexOf("kg"))){
//                text = "Volume: " + packSize.substring(packSize.indexOf("x") +1, end);
//            } else {
                text = packSize.substring(0, end);
//            }
        } else if(packSize.contains("mg")){
            int end = packSize.indexOf("mg") + 2;
//            if(packSize.contains("x") && (packSize.indexOf("x") < packSize.indexOf("mg"))){
//                text = "Volume: " + packSize.substring(packSize.indexOf("x") +1, end);
//            } else {
                text = packSize.substring(0, end);
//            }
        } else if(packSize.contains("liter")){
            int end = packSize.indexOf("liter") + 5;
//            if(packSize.contains("x") && (packSize.indexOf("x") < packSize.indexOf("liter"))){
//                text = "Volume: " + packSize.substring(packSize.indexOf("x") +1, end);
//            } else {
                text = packSize.substring(0, end);
//            }
        } else if(packSize.contains("l")){
            int end = packSize.indexOf("l") + 1;
//            if(packSize.contains("x") && (packSize.indexOf("x") < packSize.indexOf("l"))){
//                text = "Volume: " + packSize.substring(packSize.indexOf("x") +1, end);
//            } else {
                text = packSize.substring(0, end);
//            }
        } else {
            if (!medicine.getIngredients().equals(" ") && !medicine.getIngredients().equals("") && !medicine.getIngredients().isEmpty()) {
                if (medicine.getIngredients().contains("[") && medicine.getIngredients().contains("]")) {
                    int start = medicine.getIngredients().indexOf("[");
                    int end = medicine.getIngredients().indexOf("]");

                    text = "Potency: " + medicine.getIngredients().substring(start + 1, end);
                    volume = false;
                } else {
                    volume = false;
                }
            } else {
                text = "Potency: N/A";
                volume = false;
            }
        }

        if(volume){
            if(text.contains("x") && text.contains("s")){
                if(text.indexOf("x") > text.indexOf("s")) {
                    text = text.substring(text.indexOf("x") + 1);
                    text = "Volume: " + text;
                } else {
                    text = text.substring(0, text.indexOf("x")) + text.substring(text.indexOf("s")+1);
                    text = "Volume: " + text;
                }
            } else {
                text = "Volume: " + text;
            }
        }

//        if(medicine.getType().equals("Tabs") || medicine.getType().equals("Tabs efr") || medicine.getType().equals("Tabs Chewable")) {
//
//            int start = medicine.getIngredients().indexOf("[");
//            int end = medicine.getIngredients().indexOf("]");
//
//            text = "Potency: " + medicine.getIngredients().substring(start + 1, end);
//        } else if(medicine.getType().equals("Syrup") || medicine.getType().equals("Gel") || medicine.getType().equals("Cream")
//                || medicine.getType().equals("Mouth Wash") || medicine.getType().equals("Cream")){
//            text = "Volume: " + medicine.getPackSize();
//        } else if(medicine.getType().equals("Syrup")){
//
//        }

        return text;
    }

    public static int getMinQuantity(MedicineDetails medicine){

        Boolean volume = true;
        int quantity = 1;

        String packSize = medicine.getPackSize().toLowerCase();

        if(packSize.contains("sachets") || packSize.contains("sachet") || packSize.contains("doses") ||
                packSize.contains("hypertonic") || packSize.contains("isotonic") || packSize.contains("vial") ||
                packSize.contains("vials") || packSize.contains("cm") || packSize.contains("ml") ||
                packSize.contains("g") || packSize.contains("kg") || packSize.contains("mg") ||
                packSize.contains("liter") || packSize.contains("l")){
        }
//        else if(packSize.contains("ml")){
//            int end = packSize.indexOf("ml") + 2;
//            if(packSize.contains("x") && (packSize.indexOf("x") < packSize.indexOf("ml"))){
//
//            } else {
//
//            }
//        } else if(packSize.contains("g")){
//            int end = packSize.indexOf("g") + 1;
//
//        } else if(packSize.contains("kg")){
//            int end = packSize.indexOf("kg") + 2;
//
//        } else if(packSize.contains("mg")){
//            int end = packSize.indexOf("mg") + 2;
//
//        } else if(packSize.contains("liter")){
//            int end = packSize.indexOf("liter") + 5;
//
//        } else if(packSize.contains("l")){
//            int end = packSize.indexOf("l") + 1;
//
//        }
        else {
//            if (!medicine.getIngredients().equals(" ") && !medicine.getIngredients().equals("") && !medicine.getIngredients().isEmpty()) {
//                if (medicine.getIngredients().contains("[") && medicine.getIngredients().contains("]")) {
//                    int start = medicine.getIngredients().indexOf("[");
//                    int end = medicine.getIngredients().indexOf("]");
//
//
//                    volume = false;
//                } else {
//                    volume = false;
//                }
//            } else {
//                volume = false;
//            }

            volume = false;
        }

        try {
            if (packSize.contains("s")) {
                if (packSize.contains("x") && (packSize.indexOf("x") < packSize.indexOf("s"))) {
                    quantity = Integer.valueOf(packSize.substring(packSize.indexOf("x") + 1, packSize.indexOf("s")));
                } else {
                    quantity = Integer.valueOf(packSize.substring(0, packSize.indexOf("s")));
                }
            } else {
                if (!volume) {
                    quantity = Integer.valueOf(packSize);
                }
            }
        } catch (Exception e){

        }

        return quantity;
    }

    public static Boolean checkDateIsLatest(String date1, String date2) {

        try {
//            java.text.DateFormat df = new SimpleDateFormat(Constants.PROMOTION_DATE_FORMAT, Locale.getDefault());
//            String d1 = new SimpleDateFormat(Constants.PROMOTION_DATE_FORMAT).format(date1);
//            String d2 = new SimpleDateFormat(Constants.PROMOTION_DATE_FORMAT).format(date2);

            java.text.DateFormat df = new SimpleDateFormat(Constants.PROMOTION_DATE_FORMAT);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Date formattedDate1 = df.parse(date1);

            Date formattedDate2 = df.parse(date2);

            if(sdf.format(formattedDate1).compareTo(sdf.format(formattedDate2)) > 0) {
                boolean t = true;
            }

            if(formattedDate1.before(formattedDate2) || formattedDate1.equals(formattedDate2)) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    public static String getDateString(Date date, String typeInput, String typeOutput){
        String timeStamp = "";
        try {
//            java.text.DateFormat df = new SimpleDateFormat(typeInput , Locale.getDefault());
//            Date formattedDateTime = df.parse(date);

            timeStamp = new SimpleDateFormat(typeOutput).format(date);
        }
        catch (Exception e) {
            Log.d("Date Exception" , e.toString());
            e.printStackTrace();
        }
        return timeStamp;
    }

    public static String getTimeStamp(String dateTime, String type){
        String timeStamp = "";
        String timeCounter = "";
        try {
            java.text.DateFormat df = new SimpleDateFormat(Constants.SERVER_DATE_FORMAT , Locale.getDefault());
            Date formattedDateTime = df.parse(dateTime);
            formattedDateTime = utcToLocalDate(formattedDateTime);

            timeStamp = new SimpleDateFormat(type).format(formattedDateTime);

//            if(type.equals(Constants.OUR_DATE_FORMAT)) {
//                timeStamp = new SimpleDateFormat(Constants.OUR_DATE_FORMAT).format(formattedDateTime);
//            } else if(type.equals(Constants.OUR_TIME_FORMAT)) {
//                timeStamp = new SimpleDateFormat(Constants.OUR_TIME_FORMAT).format(formattedDateTime);
////                timeStamp = timeStamp + " (" + getCounter(formattedDateTime) + ")";
//            } else if(type.equals(Constants.OUR_DATE_TIME_FORMAT)) {
//                timeStamp = new SimpleDateFormat(Constants.OUR_DATE_TIME_FORMAT).format(formattedDateTime);
////                timeStamp = timeStamp + " (" + getCounter(formattedDateTime) + ")";
//            } else if(type.equals(Constants.OUR_PRESCRIPTION_DATE_FORMAT)) {
//                timeStamp = new SimpleDateFormat(Constants.OUR_PRESCRIPTION_DATE_FORMAT).format(formattedDateTime);
//            }
        }
        catch (Exception e) {
            Log.d("Date Exception" , e.toString());
            e.printStackTrace();
        }

        return timeStamp;
    }

//    public static String getCounter(Date formattedDateTime){
//
//        String timeCounter = "";
//
//        Calendar cal = Calendar.getInstance(Locale.getDefault());
//        //cal.set(Calendar.AM_PM, 0);
//
//        Date currentDate= cal.getTime();
//        long miliseconds = getDateDiff(formattedDateTime , currentDate , TimeUnit.MILLISECONDS);
//        Counter counter = calculateTime(miliseconds);
////            Log.d("note" , notificatonDuration.toString()+"");
//        if(counter.getDays() < 1){
//            if(counter.getHours() < 1){
//                if(counter.getMinutes() < 1){
//
//                    if(counter.getSeconds() > 1)
//                        timeCounter = counter.getSeconds() + " secs";
//                    else
//                        timeCounter = counter.getSeconds() + " sec";
//
//                }
//                else{
//
//                    if(counter.getMinutes() > 1)
//                        timeCounter = counter.getMinutes() + " mins";
//                    else
//                        timeCounter = counter.getMinutes() + " min";
//
//                }
//            }
//            else{
//
//                if(counter.getHours() > 1)
//                    timeCounter = counter.getHours() + " hrs";
//                else
//                    timeCounter = counter.getHours() + " hr";
//            }
//        }
//        else{
////                if(counter.getDays() < 5){
//
//            if (counter.getDays() > 1) {
//                timeCounter = counter.getDays() + " days";
//            }
//            else {
//                timeCounter = counter.getDays() + " day";
//            }
////                } else {
////                }
//        }
//
//        return timeCounter;
//    }
//
//    public static Counter calculateTime(Long timeIn){
//        Counter counter = new Counter();
//        int day = (int) TimeUnit.MILLISECONDS.toDays(timeIn);
//        long hours = TimeUnit.MILLISECONDS.toHours(timeIn) - (day * 24);
//        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeIn) - (hours * 60);
//        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeIn) - (minutes * 60);
//
//        if(day < 0)
//            day = (-1) * day;
//        int iHours = (int)hours;
//        int iMinutes = (int)minutes;
//        int iSeconds = (int)seconds;
//        if(iHours < 0)
//            iHours = (-1)*iHours;
//        if(iMinutes < 0)
//            iMinutes=(-1)*iMinutes;
//        if(iSeconds < 0) {
//            //iSeconds=(-1)*iSeconds;
//        }
//        counter.setDays(day);
//        counter.setHours(iHours);
//        counter.setMinutes(iMinutes);
//        counter.setSeconds(iSeconds);
//
//        String time = String.valueOf(day) + " Days " + String.valueOf(hours) + " Hours " +
//                String.valueOf(minutes) + " Minutes " + String.valueOf(seconds) + " Seconds.";
//
//        return counter;
//    }

    public static String getPrescriptionFileName(){

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("ddMMyyyy_hhmmss");

        String fileName = format1.format(cal.getTime());

        fileName = "prescription_" + fileName + ".jpeg";

        return fileName;
    }

    public static String getPromotionFileName(){

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("ddMMyyyy_hhmmss");

        String fileName = format1.format(cal.getTime());

        fileName = "promotion_" + fileName + ".jpeg";

        return fileName;
    }

    public static String getAudioNoteFileName(){

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format1 = new SimpleDateFormat("ddMMyyyy_hhmmss");

        String fileName = format1.format(cal.getTime());

        fileName = "audio_note_" + fileName + ".3gp";

        return fileName;
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static Date utcToLocalDate(Date date) {

        String timeZone = Calendar.getInstance().getTimeZone().getID();
        Date local = new Date(date.getTime() + TimeZone.getTimeZone(timeZone).getOffset(date.getTime()));
        return local;
    }

    public static String extractInitialsUser(String firstName, String lastName){
        String initials = "";

        if(firstName != null && firstName.length() > 0){
            initials = firstName.substring(0, 1).toUpperCase();

            if(lastName == null){
                String initialSecond = firstName.substring(firstName.indexOf(" ") + 1, firstName.indexOf(" ") + 2);
                if(initialSecond.length() > 0) {
                    initials += initialSecond;
                }
            }
        }

        if(lastName != null && lastName.length() > 0) {
            initials += lastName.substring(0, 1).toUpperCase();
        }

        return initials;
    }

    public static String extractInitialsPharmacy(String name){
        String initials = "";

        if(name != null && name.length() > 1){
            initials = name.substring(0, 1).toUpperCase();
        }

        return initials;
    }

    public static void setPic(Activity activity, CircleImageView picture, String url, String type,
                              final TextView initials, String firstName, String lastName){
        picture.setImageResource(R.color.initials_background_color);
        initials.setVisibility(View.VISIBLE);
        if(type.equals(Constants.TYPE_USER)) {
            initials.setText(extractInitialsUser(firstName, lastName));
        } else if(type.equals(Constants.TYPE_PHARMACY)){
            initials.setText(extractInitialsPharmacy(firstName));
        }
        picture.setBorderWidth(Constants.BORDER_WIDTH);
        picture.setBorderColor(activity.getResources().getColor(R.color.white));

        if (url != null && url != "") {

            Glide
                    .with(activity)
                    .load(Utills.getCompleteUrl(url))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                            initials.setVisibility(View.GONE);
                            return false;
                        }
                    })
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.color.initials_background_color)
                    .error(R.color.initials_background_color)
                    .into(picture);
        } else {

        }
    }

    public static void invalidTokenLogout(Activity activity, String msg, String authorization){

        if(authorization != null) {
            authorization = Utills.getCompleteToken(authorization);
            Preferences.addAuthCodeToSharedPreferences(activity, (authorization));
        }

        if(msg != null && msg.equals(Constants.SESSION_EXPIRED_TOKEN)) {
            if (Preferences.getUserDataFromSharedPreferences(activity) != null) {
                showToast(activity, Constants.SESSION_EXPIRE_LOGIN);
            } else {
                showToast(activity, Constants.SESSION_EXPIRE);
            }

            performLogout(activity);
            goToHome(activity);
        }

        if(msg == null || msg.equals(Constants.SESSION_INVALID_TOKEN) || msg.equals(Constants.SESSION_NO_TOKEN)){

            Preferences.removeAuthCodeFromSharedPreferences(activity);
            performLogout(activity);
            goToHome(activity);
        }
    }

    public static void performLogout(Context context){

        Preferences.removePharmacyDataFromSharedPreferences(context);
        Preferences.removePharmacyImagePathFromSharedPreferences(context);
        Preferences.removeUserDataFromSharedPreferences(context);
        Preferences.removeCartItemsFromSharedPreferences(context);
        Preferences.removePrescriptionImagePathsFromSharedPreferences(context);
        Preferences.addCartUpdateBooleanToSharedPreferences(context, true);
        Preferences.setFcmFlagToSharedPreferences(context, false);
        Preferences.removeSelectedPharmacyFromSharedPreferences(context);
        Preferences.removeOrdersCountChangeBooleanFromSharedPreferences(context);
        Preferences.removeOrdersCountFromSharedPreferences(context);
    }

    public static void updateStatusBar(Context context, String type, String status, ProgressBar orderStatusBar,
                                       CircleImageView pendingPoint, TextView pendingLabel, CircleImageView confirmationPoint,
                                       TextView confirmationLabel, CircleImageView processPoint, TextView processLabel,
                                       CircleImageView enroutePoint, TextView enrouteLabel, CircleImageView completePoint,
                                       TextView completeLabel){

        if(type != null && type.equals(Constants.ORDER_PICK_UP)){
            enrouteLabel.setText("Ready");
        } else {
            enrouteLabel.setText("Enroute");
        }

        Boolean user = context instanceof OrderDetailsUserActivity;

        switch(status) {

            case Constants.STATUS_PENDING:
                orderStatusBar.setProgress(Constants.PERCENT_PENDING);
                if(user) {
                    pendingPoint.setImageResource(R.color.state_waiting_color);
                    pendingLabel.setTextColor(context.getResources().getColor(R.color.state_waiting_color));
                } else {
                    pendingPoint.setImageResource(R.color.state_pending_color);
                    pendingLabel.setTextColor(context.getResources().getColor(R.color.state_pending_color));
                }
                break;

            case Constants.STATUS_CONFIRMATION:
                orderStatusBar.setProgress(Constants.PERCENT_CONFIRMATION);
                pendingPoint.setImageResource(R.color.state_pending_color);
                pendingLabel.setTextColor(context.getResources().getColor(R.color.state_pending_color));
                if(user) {
                    confirmationPoint.setImageResource(R.color.state_confirmation_color);
                    confirmationLabel.setTextColor(context.getResources().getColor(R.color.state_confirmation_color));
                } else {
                    confirmationPoint.setImageResource(R.color.state_waiting_color);
                    confirmationLabel.setTextColor(context.getResources().getColor(R.color.state_waiting_color));
                }
                break;

            case Constants.STATUS_IN_PROGRESS:
//                Utills.changeNavigationBarColor(this, Constants.COLOR_GREY)

                orderStatusBar.setProgress(Constants.PERCENT_IN_PROGRESS);
                pendingPoint.setImageResource(R.color.state_pending_color);
                pendingLabel.setTextColor(context.getResources().getColor(R.color.state_pending_color));
                confirmationPoint.setImageResource(R.color.state_confirmation_color);
                confirmationLabel.setTextColor(context.getResources().getColor(R.color.state_confirmation_color));
                if(user) {
                    processPoint.setImageResource(R.color.state_waiting_color);
                    processLabel.setTextColor(context.getResources().getColor(R.color.state_waiting_color));
                } else {
                    processPoint.setImageResource(R.color.state_in_progress_color);
                    processLabel.setTextColor(context.getResources().getColor(R.color.state_in_progress_color));
                }
                break;


            case Constants.STATUS_ENROUTE:
            case Constants.STATUS_READY:

                orderStatusBar.setProgress(Constants.PERCENT_READY);
                pendingPoint.setImageResource(R.color.state_pending_color);
                pendingLabel.setTextColor(context.getResources().getColor(R.color.state_pending_color));
                confirmationPoint.setImageResource(R.color.state_confirmation_color);
                confirmationLabel.setTextColor(context.getResources().getColor(R.color.state_confirmation_color));
                processPoint.setImageResource(R.color.state_in_progress_color);
                processLabel.setTextColor(context.getResources().getColor(R.color.state_in_progress_color));
                if(user) {
                    enroutePoint.setImageResource(R.color.state_waiting_color);
                    enrouteLabel.setTextColor(context.getResources().getColor(R.color.state_waiting_color));
                } else {
                    enroutePoint.setImageResource(R.color.state_enroute_color);
                    enrouteLabel.setTextColor(context.getResources().getColor(R.color.state_enroute_color));
                }
                break;

            case Constants.STATUS_COMPLETE:

                orderStatusBar.setProgress(Constants.PERCENT_COMPLETE);
                pendingPoint.setImageResource(R.color.state_pending_color);
                pendingLabel.setTextColor(context.getResources().getColor(R.color.state_pending_color));
                confirmationPoint.setImageResource(R.color.state_confirmation_color);
                confirmationLabel.setTextColor(context.getResources().getColor(R.color.state_confirmation_color));
                processPoint.setImageResource(R.color.state_in_progress_color);
                processLabel.setTextColor(context.getResources().getColor(R.color.state_in_progress_color));
                enroutePoint.setImageResource(R.color.state_enroute_color);
                enrouteLabel.setTextColor(context.getResources().getColor(R.color.state_enroute_color));
                completePoint.setImageResource(R.color.state_complete_color);
                completeLabel.setTextColor(context.getResources().getColor(R.color.state_complete_color));
                break;

            case Constants.STATUS_CANCELLED_PENDING:

                orderStatusBar.setProgress(Constants.PERCENT_PENDING);
                pendingPoint.setImageResource(R.color.state_canceled_color);
                pendingLabel.setTextColor(context.getResources().getColor(R.color.state_canceled_color));
                pendingLabel.setText("Cancelled");
                break;

            case Constants.STATUS_REJECTED_PENDING:

                orderStatusBar.setProgress(Constants.PERCENT_PENDING);
                pendingPoint.setImageResource(R.color.state_rejected_color);
                pendingLabel.setTextColor(context.getResources().getColor(R.color.state_rejected_color));
                pendingLabel.setText("Rejected");
                break;

            case Constants.STATUS_REJECTED_PROCESSING:

                orderStatusBar.setProgress(Constants.PERCENT_IN_PROGRESS);
                pendingPoint.setImageResource(R.color.state_pending_color);
                pendingLabel.setTextColor(context.getResources().getColor(R.color.state_pending_color));
                confirmationPoint.setImageResource(R.color.state_confirmation_color);
                confirmationLabel.setTextColor(context.getResources().getColor(R.color.state_confirmation_color));
                processPoint.setImageResource(R.color.state_rejected_color);
                processLabel.setTextColor(context.getResources().getColor(R.color.state_rejected_color));
                processLabel.setText("Rejected");
                break;

            case Constants.STATUS_REJECTED_ENROUTE:
            case Constants.STATUS_REJECTED_READY:

                orderStatusBar.setProgress(Constants.PERCENT_READY);
                pendingPoint.setImageResource(R.color.state_pending_color);
                pendingLabel.setTextColor(context.getResources().getColor(R.color.state_pending_color));
                confirmationPoint.setImageResource(R.color.state_confirmation_color);
                confirmationLabel.setTextColor(context.getResources().getColor(R.color.state_confirmation_color));
                processPoint.setImageResource(R.color.state_in_progress_color);
                processLabel.setTextColor(context.getResources().getColor(R.color.state_in_progress_color));
                enroutePoint.setImageResource(R.color.state_rejected_color);
                enrouteLabel.setTextColor(context.getResources().getColor(R.color.state_rejected_color));
                enrouteLabel.setText("Rejected");
                break;

            case Constants.STATUS_CANCELLED_CONFIRMATION:

                orderStatusBar.setProgress(Constants.PERCENT_CONFIRMATION);
                pendingPoint.setImageResource(R.color.state_pending_color);
                pendingLabel.setTextColor(context.getResources().getColor(R.color.state_pending_color));
                confirmationPoint.setImageResource(R.color.state_canceled_color);
                confirmationLabel.setTextColor(context.getResources().getColor(R.color.state_canceled_color));
                confirmationLabel.setText("Cancelled");
                break;
        }
    }

    public static void updateOrderButtonsPharmacy(Activity activity, String type, String status, View bottom, Button accept, Button reject){

        switch(status){

            case Constants.STATUS_IN_PROGRESS:

                if(type.equals(Constants.ORDER_PICK_UP)) {
                    accept.setText(Constants.BUTTON_READY);
                } else {
                    accept.setText(Constants.BUTTON_ENROUTE);
                }
                reject.setText(Constants.BUTTON_REJECT);
                accept.setVisibility(View.VISIBLE);
                reject.setVisibility(View.VISIBLE);

                if(bottom != null) {
                    bottom.setVisibility(View.VISIBLE);
                    Utills.changeNavigationBarColor(activity, Constants.COLOR_GREY);
                }
                break;

            case Constants.STATUS_CONFIRMATION:
            case Constants.STATUS_COMPLETE:
            case Constants.STATUS_CANCELLED_PENDING:
            case Constants.STATUS_CANCELLED_CONFIRMATION:
            case Constants.STATUS_REJECTED_PENDING:
            case Constants.STATUS_REJECTED_PROCESSING:
            case Constants.STATUS_REJECTED_READY:
            case Constants.STATUS_REJECTED_ENROUTE:
            case Constants.STATUS_OFFLINE:
                accept.setVisibility(View.GONE);
                reject.setVisibility(View.GONE);
                if(bottom != null) {
                    bottom.setVisibility(View.GONE);

                }
                break;

            case Constants.STATUS_ENROUTE:
            case Constants.STATUS_READY:
                accept.setText(Constants.BUTTON_COMPLETE);
                reject.setText(Constants.BUTTON_REJECT);
                accept.setVisibility(View.VISIBLE);
                reject.setVisibility(View.VISIBLE);
                if(bottom != null) {
                    bottom.setVisibility(View.VISIBLE);
                    Utills.changeNavigationBarColor(activity, Constants.COLOR_GREY);
                }
                break;

            default:
                accept.setText(Constants.BUTTON_ACCEPT);
                reject.setText(Constants.BUTTON_REJECT);
                accept.setVisibility(View.VISIBLE);
                reject.setVisibility(View.VISIBLE);
                if(bottom != null) {
                    bottom.setVisibility(View.VISIBLE);
                    Utills.changeNavigationBarColor(activity, Constants.COLOR_GREY);
                }
                break;
        }
    }

    public static void updateOrderButtonsUser(Activity activity, String type, String status, View bottom, Button accept, Button reject){

        switch(status){

            case Constants.STATUS_CONFIRMATION:
                accept.setText(Constants.BUTTON_ACCEPT);
                accept.setVisibility(View.VISIBLE);
                reject.setVisibility(View.VISIBLE);
                if(bottom != null) {
                    bottom.setVisibility(View.VISIBLE);
                    if (activity instanceof OrderDetailsUserActivity) {
                        Utills.changeNavigationBarColor(activity, Constants.COLOR_GREY);
                    }
                }
                break;

            case Constants.STATUS_OFFLINE:
            case Constants.STATUS_CANCELLED_PENDING:
            case Constants.STATUS_CANCELLED_CONFIRMATION:
            case Constants.STATUS_REJECTED_PENDING:
            case Constants.STATUS_REJECTED_PROCESSING:
            case Constants.STATUS_REJECTED_ENROUTE:
            case Constants.STATUS_REJECTED_READY:
            case Constants.STATUS_COMPLETE:
                accept.setText(Constants.BUTTON_REORDER);
                accept.setVisibility(View.VISIBLE);
                reject.setVisibility(View.GONE);
                if(bottom != null) {
                    bottom.setVisibility(View.VISIBLE);
                    if (activity instanceof OrderDetailsUserActivity) {
                        Utills.changeNavigationBarColor(activity, Constants.COLOR_GREY);
                    }
                }
                break;

            default:
                accept.setText(Constants.BUTTON_ACCEPT);
                accept.setVisibility(View.GONE);
                reject.setVisibility(View.GONE);
                if(bottom != null) {
                    bottom.setVisibility(View.GONE);
                    ((OrderDetailsUserActivity) activity).setNavigationColor();
                }
                break;
        }
    }

    public static String updateStatus(Activity activity, String status, TextView statusTV, View statusColor, Boolean nightMode){

        int STATUS_COLOR = R.color.status_pending_color;


        int pendingColor = R.color.status_pending_color;

        if(nightMode)
            pendingColor = R.color.status_pending_night_mode_color;

        switch(status) {

            case Constants.STATUS_CONFIRMATION:
                STATUS_COLOR = R.color.status_confirmation_color;
                break;

            case Constants.STATUS_IN_PROGRESS:
                status = Constants.IN_PROGRESS;
                STATUS_COLOR = R.color.status_in_progress_color;
                break;

            case Constants.STATUS_COMPLETE:
                STATUS_COLOR = R.color.status_complete_color;
                break;

            case Constants.STATUS_REJECTED_PENDING:
            case Constants.STATUS_REJECTED_PROCESSING:
            case Constants.STATUS_REJECTED_READY:
            case Constants.STATUS_REJECTED_ENROUTE:
                status = Constants.STATUS_REJECTED;
                STATUS_COLOR = R.color.status_rejected_color;
                break;

            case Constants.STATUS_CANCELLED_PENDING:
            case Constants.STATUS_CANCELLED_CONFIRMATION:
                status = Constants.STATUS_CANCELLED;
                STATUS_COLOR = R.color.status_canceled_color;
                break;

            case Constants.STATUS_OFFLINE:
                status = Constants.SMS_ORDER;
                STATUS_COLOR = R.color.status_offline_color;
                break;

            case Constants.STATUS_ENROUTE:

                STATUS_COLOR = R.color.status_enroute_color;
                break;

            case Constants.STATUS_READY:

                STATUS_COLOR = R.color.status_ready_color;
                break;

            default:
                status = Constants.STATUS_PENDING;
                STATUS_COLOR = pendingColor;
                break;

        }

        if(activity != null) {
            if(statusTV != null) {
                statusTV.setText(status);
                statusTV.setTextColor(activity.getResources().getColor(STATUS_COLOR));
            }

            if(statusColor != null){
                statusColor.setBackgroundResource(STATUS_COLOR);
            }
        }

        return status;
    }

    public static void getNewOrderMsg(){

    }

    public static String getUpdateStatusMsg(String name, String status){
        String msg = "";

        status = updateStatus(null, status, null, null,  false);

        msg = "Your order to " + name;

        if(status.equals(Constants.STATUS_PENDING))
            msg = msg + " need to be CONFIRMED";
        else
            msg = msg + " is " + status;

//        msg = msg + status;

        return msg;
    }

    public static String getUserAcceptanceMsg(User user, String status){
        String msg = "";

        if(user != null)
            msg = "Order by " + user.getFirstName() + " " + user.getLastName() + " is Confirmed";

        return msg;
    }

    public static String getUserCancellationMsg(User user, String status){
        String msg = "";

        if(user != null)
            msg = "Order by " + user.getFirstName() + " " + user.getLastName() + " is Cancelled";

        return msg;
    }

    public static String getOrderStatus(String buttonText){
        String status = "";
        switch (buttonText) {
            case Constants.BUTTON_ACCEPT:
                status = Constants.STATUS_CONFIRMATION;
                break;
            case Constants.BUTTON_READY:
                status = Constants.STATUS_READY;
                break;
            case Constants.BUTTON_ENROUTE:
                status = Constants.STATUS_ENROUTE;
                break;
            case Constants.BUTTON_COMPLETE:
                status = Constants.STATUS_COMPLETE;
                break;
        }

        return status;
    }

    public static String getCancelStatus(String currentStatus){
        String status = "";

        switch (currentStatus) {
            case Constants.STATUS_PENDING:
                status = Constants.STATUS_REJECTED_PENDING;
                break;
            case Constants.STATUS_IN_PROGRESS:
                status = Constants.STATUS_REJECTED_PROCESSING;
                break;
            case Constants.STATUS_ENROUTE:
                status = Constants.STATUS_REJECTED_ENROUTE;
                break;

            case Constants.STATUS_READY:
                status = Constants.STATUS_REJECTED_READY;
                break;

            default:
                status = Constants.STATUS_REJECTED_PENDING;
                break;
        }

        return status;
    }

//    public static boolean isValidLandlineNumber(Activity activity, LatLng location, String phone) {
//        if (TextUtils.isEmpty(phone))
//            return false;
//        final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
//        try {
//            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(phone, getCountryCode(activity, location));
//            PhoneNumberUtil.PhoneNumberType phoneNumberType = phoneNumberUtil.getNumberType(phoneNumber);
//            if(phoneNumberType == PhoneNumberUtil.PhoneNumberType.MOBILE) {
//                return false;
//            } else {
//                return true;
//            }
//        } catch (final Exception e) {
//        }
//        return true;

//        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
//        TelephonyManager tm = (TelephonyManager)this.getSystemService(this.TELEPHONY_SERVICE); // for device containing sim card
//        String locale = tm.getNetworkCountryIso();// for device containing sim card
//        // Else you need to access locale through GPS
//        try {
//            Phonenumber.PhoneNumber nepalNumber = phoneUtil.parse(nepalNumberStr, locale.toUpperCase());
//            boolean isValid = phoneUtil.isValidNumber(nepalNumber); // returns true
//            if(isValid) {
//                String internationally = phoneUtil.format(nepalNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
//                Toast.makeText(this, nepalNumberStr + " is " + isValid + " and number is :" + internationally + "And is "+ phoneUtil.getNumberType(nepalNumber), Toast.LENGTH_SHORT).show();
//            }else{
//                Toast.makeText(this, "This phone number is not valid for " + locale, Toast.LENGTH_SHORT).show();
//            }
//        } catch (NumberParseException e) {
//            System.err.println("NumberParseException was thrown: " + e.toString());
//        }
//    }

    public static String getCountryCode(String currency){
        if(currency != null) {
            try {
                return currency.split("-")[0];
            } catch (Exception e){
                return "PK";
            }
        }
        else
            return "PK";
    }

    public static String getCurrencyCode(String currency){
        if(currency != null){
            try {
                return currency.split("-")[1];
            } catch (Exception e){
                return "PKR";
            }
        }

        else
            return "PKR";
    }

    public static Order getReOrderObject(Order oldOrder){

        Order order = new Order();

        order.setPharmacy(oldOrder.getPharmacy());
        order.setUser(oldOrder.getUser());
        order.setItems(oldOrder.getItems());
        order.setPrescriptions(oldOrder.getPrescriptions());
        order.setItemsCount(oldOrder.getItemsCount());

        if(order.getItems() != null) {
            for (int i = 0; i < order.getItems().size(); i++) {
                order.getItems().get(i).setPrice(0);
                order.getItems().get(i).available = true;
            }
        }

        if(order.getPrescriptions() != null) {
            for (int i = 0; i < order.getPrescriptions().size(); i++) {
                order.getPrescriptions().get(i).setTotalAmount(0);
                order.getPrescriptions().get(i).setAvailable(true);
            }
        }

        return order;
    }


//    public static ArrayList<Entry> getData(ArrayList<Integer> data){
//
//        ArrayList<Entry> values = new ArrayList<>();
//
//        for(int i = 0; i < data.size(); i++){
//            values.add(new Entry(i, data.get(i)));
//        }
//
//        return values;
//    }
//
//    public static ArrayList<Entry> getBarData(ArrayList<StatsItem> data){
//
//        ArrayList<Entry> values = new ArrayList<>();
//
//        for(int i = 0; i < data.size(); i++){
//            values.add(new Entry(i, data.get(i).getTotalCount()));
//        }
//
//        return values;
//    }

    public static void copyToClipboard(Activity activity, String text) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }

        showToast(activity, "Copied to clipboard");
    }

    public static String formatMilliSeconds(long milliseconds) {
        String finalTimerString = "";
        String minutesString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit

        if(minutes < 10){
            minutesString = "0" + minutes;
        } else {
            minutesString = "" + minutes;
        }

        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutesString + ":" + secondsString;

        return finalTimerString;
    }

    public static String getDuration(String url) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(url);
        String durationStr = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        return formatMilliSeconds(Long.parseLong(durationStr));
    }
}