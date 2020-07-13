package com.zealsoft.pharmed.Util;

import com.zealsoft.pharmed.models.MedicineDetails;
import com.zealsoft.pharmed.models.Order;
import com.zealsoft.pharmed.models.PharmacyDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ahtesham on 03/05/2019.
 */

public class Constants {
//    public static final String KEY = "AIzaSyAiTFG3KiPlIH3-aW9zraJ749cEU8yu";
//    public static final String SERVER_IP = "https://webpharmed.com/";
//    public static final String SERVER_IP = "http://10.2.1.167:3000/";
        public static final String SERVER_IP = "http://3.16.123.176:3000/";


    // New Server:      http://18.224.229.2:3000/
    // New Server:      http://18.224.229.2:3000/
    // Old server:      http://webpharmed.com/pharmacy/index.php/

    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String WEB = "web";
    public static final String CONTACT = "contact";
    public static final String START_HR = "start_hr";
    public static final String CLOSE_HR = "close_hr";
    public static final String LOCATION = "location";
    public static final String USER_ID = "user_id";
    public static String LAT = "lat";
    public static String LNG = "lng";
    public static String CURRENT_LAT = "";
    public static String CURRENT_LNG = "";
    public static float DEFAULT_ZOOM_LEVEL = 14.5f;
    public static float MOVING_ZOOM_LEVEL = 14.5f;
    public static int MAP_MIN_DISTANCE = 550;

    public static String COMFORTAA_FONT_PATH = "fonts/Comfortaa-Bold.ttf";

    public static final String AUTH_CODE_TAG_SP = "AUTH_CODE_TAG_SP";
    public static final String INSTALL_EVENT_TAG_SP = "INSTALL_EVENT_TAG_SP";
    public static final String PHARMACY_DATA_TAG_SP = "PHARMACY_DATA_TAG_SP";
    public static final String USER_DATA_TAG_SP = "USER_DATA_TAG_SP";
    public static final String PHARMACY_IMAGE_TAG_SP = "PHARMACY_IMAGE_TAG_SP";
    public static final String NIGHT_MODE_TAG_SP = "NIGHT_MODE_TAG_SP";
    public static final String LOCATION_TAG_SP = "LOCATION_TAG_SP";
    public static final String NEARBY_PHARMACIES_TAG_SP_OLD = "NEARBY_PHARMACIES_TAG_SP_OLD";
    public static final String NEARBY_PHARMACIES_TAG_SP = "NEARBY_PHARMACIES_TAG_SP";
    public static final String CART_UPDATE_TAG_SP = "CART_UPDATE_TAG_SP";
    public static final String CART_LIST_TAG_SP = "CART_LIST_TAG_SP";
    public static final String CART_MESSAGE_TAG_SP = "CART_MESSAGE_TAG_SP";
    public static final String SELECTED_PHARMACY_TAG_SP = "SELECTED_PHARMACY_TAG_SP";
    public static final String MOVE_FIND_DATA_TAG_SP = "MOVE_FIND_DATA_TAG_SP";
    public static final String PHARMACIES_ACTION_TAG_SP = "PHARMACIES_ACTION_TAG_SP";
    public static final String FCM_TOKEN_TAG_SP = "FCM_TOKEN_SP";
    public static final String FCM_TOKEN_SENT_TAG_SP = "FCM_TOKEN_SENT_SP";
    public static final String NOTIFICATIONS_ON_OFF_TAG_SP = "NOTIFICATIONS_ON_OFF_TAG_SP";
    public static final String VOICE_OVER_ON_OFF_TAG_SP = "VOICE_OVER_ON_OFF_TAG_SP";
    public static final String PRESCRIPTION_IMAGE_TAG_SP = "PRESCRIPTION_IMAGE_TAG_SP";
    public static final String NOTIFICATION_TUNE_TAG_SP = "NOTIFICATION_TUNE_TAG_SP";
    public static final String FIRST_INSTALL_TAG_SP = "FIRST_INSTALL_TAG_SP";
    public static final String ORDER_COUNT_TAG_SP = "ORDER_COUNT_TAG_SP";
    public static final String ORDER_COUNT_CHANGE_TAG_SP = "ORDER_COUNT_CHANGE_TAG_SP";

    public static final String AUTH_CODE_TAG_MODEL = "AUTH_CODE_TAG_MODEL";
    public static final String INSTALL_EVENT_TAG_MODEL = "INSTALL_EVENT_TAG_MODEL";
    public static final String PHARMACY_DATA_TAG_MODEL = "PHARMACY_DATA_TAG_MODEL";
    public static final String USER_DATA_TAG_MODEL = "USER_DATA_TAG_MODEL";
    public static final String PHARMACY_IMAGE_TAG_MODEL = "PHARMACY_IMAGE_TAG_MODEL";
    public static final String NIGHT_MODE_TAG_MODEL = "NIGHT_MODE_TAG_MODEL";
    public static final String LOCATION_TAG_MODEL = "LOCATION_TAG_MODEL";
    public static final String NEARBY_PHARMACIES_TAG_MODEL_OLD = "NEARBY_PHARMACIES_TAG_MODEL_OLD";
    public static final String NEARBY_PHARMACIES_TAG_MODEL = "NEARBY_PHARMACIES_TAG_MODEL";
    public static final String CART_UPDATE_TAG_MODEL = "CART_UPDATE_TAG_MODEL";
    public static final String CART_LIST_TAG_MODEL = "CART_LIST_TAG_MODEL";
    public static final String CART_MESSAGE_TAG_MODEL = "CART_MESSAGE_TAG_MODEL";
    public static final String SELECTED_PHARMACY_TAG_MODEL = "SELECTED_PHARMACY_TAG_MODEL";
    public static final String MOVE_FIND_DATA_TAG_MODEL = "MOVE_FIND_DATA_TAG_MODEL";
    public static final String PHARMACIES_ACTION_TAG_MODEL = "PHARMACIES_ACTION_TAG_MODEL";
    public static final String FCM_TOKEN_TAG_MODEL="FCM_TOKEN";
    public static final String FCM_TOKEN_SENT_TAG_MODEL="FCM_TOKEN_SENT";
    public static final String NOTIFICATIONS_ON_OFF_TAG_MODEL = "NOTIFICATIONS_ON_OFF_TAG_MODEL";
    public static final String VOICE_OVER_ON_OFF_TAG_MODEL = "VOICE_OVER_ON_OFF_TAG_MODEL";
    public static final String PRESCRIPTION_IMAGE_TAG_MODEL = "PRESCRIPTION_IMAGE_TAG_MODEL";
    public static final String NOTIFICATION_TUNE_TAG_MODEL = "NOTIFICATION_TUNE_TAG_MODEL";
    public static final String FIRST_INSTALL_TAG_MODEL = "FIRST_INSTALL_TAG_MODEL";
    public static final String ORDER_COUNT_TAG_MODEL = "ORDER_COUNT_TAG_MODEL";
    public static final String ORDER_COUNT_CHANGE_TAG_MODEL = "ORDER_COUNT_CHANGE_TAG_MODEL";


    // Toast messages

    public static String NO_PHARMACY_FOUND = "No Pharmacy Found";
    public static String NO_MEDICINE_FOUND = "No Medicine Found";
    public static String NO_NEAREST_PHARMACY_FOUND = "No pharmacy found nearby";
    public static String NO_ORDERS_YET = "Sign In to view your Orders List";
    public static String NO_ORDERS_YET_USER = "No Orders in the List";
    public static String NO_INTERNET_CONNECTION = "No Internet Connection";
    public static String CONNECTION_PROBLEM = "Connection problem try again";

    public static final String EMPTY_NAME = "Pharmacy Name cannot be empty";
    public static final String PASSWORD_MATCH = "Password do not match";
    public static final String PASSWORD_LENGTH = "Password should be at least 8 characters long";
    public static final String PHONE_NUMBER_INVALID = "Invalid Phone Number";
    public static final String EMAIL_INVALID = "Invalid Email Address";
    public static final String EMPTY_ADDRESS = "Address cannot be empty";
    public static final String WEBSITE_INVALID = "Website should start with Http:// or Https://";
    public static final String EMPTY_TIME = "Opening Hours cannot be empty";
    public static final String LICENSE_IMAGE_MUST = "Please upload picture of your pharmacy license";
    public static final String TERMS_CONDITIONS_MUST = "Please accept the Terms & Conditions to continue";
    public static final String FORM_VALIDATION = "Please fill all fields";
    public static final String VERIFICATION_CODE_MATCH = "Verification Code is invalid";
    public static final String SELECT_CATEGORY = "Please select your Category";
    public static final String ANOTHER_LOGIN = "You have login in on another device.";


    public static final String ADD_TO_CART = "Added to Cart";
    public static final String ALREADY_ADDED_TO_CART = "Already Added to Cart";
    public static final String ADD_CART_FAILED = "Fail to Add. Try Again";
    public static final String UPDATED_QUANTITY = "Cart Updated";
    public static final String UPDATED_QUANTITY_FAILED = "Update Failed";
    public static final String DELETE_FROM_CART = "Item(s) Deleted";

    public static final String UPDATED_RESPONSE = "Cart Quantity Updated";
    public static final String REMOVED_RESPONSE = "Medicine removed";

    public static final String ITEMS_PRICES = "Enter Prices of all items or mark Not Available";
    public static final String ITEMS_AVAILABLE = "At least one item should be available";

    public static final String UPDATE_STATUS_FAILED = "Action Failed";

    public static final String PLACE_ORDER_REGISTER = "Register yourself to place order";

    public static final String CUSTOM_REASON_EMPTY = "Please provide the reason to reject";
    public static final String PERMISSION_LOCATION = "Permission Required to get Nearby Pharmacies";
    public static final String CURRENT_LOCATION_REQUIRED = "Current location required to get nearest pharmacy";
    public static final String GPS_REQUIRED = "GPS need to be On for current location";

    public static final String PROMOTION_CURRENT_DATE = "Cannot be before current date";
    public static final String PROMOTION_START_DATE = "Start date cannot be after end date";
    public static final String PROMOTION_END_DATE = "End date cannot be before start date";

    public static final String NO_PROMO_CODE = "No promo code found";

    public static final String EXIT_MESSAGE = "Press again to exit";

    public static List<String> LIST_MEDICINE_TYPES = Arrays.asList(
            "Type",
            "Aerosol",
            "Balm",
            "Bubble Gum",
            "Caplet",
            "Caps SR",
            "Caps",
            "Comp Inj",
            "Comp Tabs",
            "Cream",
            "Dragees",
            "Drops",
            "Dry Susp",
            "E and E Drops",
            "Ear Drops",
            "Elixir",
            "Emul",
            "Enema",
            "Expc",
            "Eye Drops",
            "Eye Gel",
            "Eye Oint",
            "Eye Susp",
            "Gel",
            "Granules",
            "Inf",
            "Inhaler",
            "Inj CS",
            "Inj IM/IV",
            "Inj SC",
            "Inj SR",
            "Inj",
            "Inj-IM",
            "Inj-IV",
            "Linctus",
            "Liniment",
            "Liquid",
            "Lotion",
            "Lozenges",
            "Mixture",
            "Mouth Spray",
            "Mouth Wash",
            "Nasal Drops",
            "Nasal Spray",
            "Nebuliser",
            "Oil",
            "Oint",
            "Oral Soln",
            "Paste",
            "Patches",
            "Powder",
            "Rota Caps",
            "Sachet",
            "Scrub",
            "Shampoo",
            "Soap",
            "Soft Caps",
            "Soln",
            "Spinal Inj",
            "Spray",
            "Suppositories",
            "Susp DS",
            "Susp",
            "Syringe",
            "Syrup",
            "Tab Enteric coated",
            "Tabs Chewable",
            "Tabs DS",
            "Tabs efr",
            "Tabs SL",
            "Tabs SR",
            "Tabs",
            "Tinc",
            "Tooth Paste",
            "Vag Cream",
            "Vag Ovule",
            "Vag Tabs",
            "Vag. Pessaries");

    public static List<String> LIST_POTENCY_UNITS = Arrays.asList("Unit", "g", "l", "mg", "ml");

    public static List<String> LIST_BLOOD_GROUP = Arrays.asList("Blood Group", "A+", "A-","B+", "B-", "AB+", "AB-", "O+", "O-");
    public static List<String> LIST_PROFESSIONS = Arrays.asList("Profession", "Business","Home Maker", "Retired", "Student", "Salaried");
    public static List<String> LIST_MONTHS = Arrays.asList("Year", "January", "February","March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
    public static List<String> LIST_YEARS = Arrays.asList("2019", "2020");
    public static List<String> LIST_STATS_TYPES = Arrays.asList(Constants.DATA_ORDERS, Constants.DATA_VIEWS, Constants.DATA_CALLS,
            Constants.DATA_MSGS, Constants.DATA_DIRECTIONS, Constants.DATA_SHARES);
    public static List<String> LIST_STATS_TYPES_SHOW = Arrays.asList("Orders", "Views", "Calls", "Messages", "Directions", "Share");


    // Fields Character limits

    public static final Integer FIELD_FIRST_NAME = 50;
    public static final Integer FIELD_LAST_NAME = 50;
    public static final Integer FIELD_PHARMACY_NAME = 60;
    public static final Integer FIELD_EMAIL_ADDRESS = 70;
    public static final Integer FIELD_ADDRESS = 170;
    public static final Integer FIELD_LINE_ITEM_NAME = 50;
    public static final Integer FIELD_DELIVERY_LIMIT = 5;
    public static final Integer FIELD_PASSWORD = 250;
    public static final Integer FIELD_QUANTITY = 3;
    public static final Integer FIELD_PRICE = 6;
    public static final Integer FIELD_TIME = 10;
    public static final Integer FIELD_MEDICINE_NAME = 80;
    public static final Integer FIELD_MEDICINE_POTENCY = 5;
    public static final Integer FIELD_MANUFACTURER_NAME = 80;
    public static final Integer FIELD_NOTE = 200;


    // Intent Strings

    public static final String ORDER_DETAILS = "order_details";
    public static final String ORDER_POSITION = "order_position";
    public static final String ORDER_STATUS = "order_status";
    public static final String SET_QUANTITY = "set_quantity";
    public static final String SET_PRICE = "set_price";
    public static final String SET_DISCOUNT = "set_discount";
    public static final String INTENT_SELECTED_PHARMACY = "SELECTED_PHARMACY";
    public static final String INTENT_NEAREST_PHARMACIES = "NEAREST_PHARMACIES";
    public static final String INTENT_CURRECY = "CURRENCY";
    public static final String INTENT_RE_ORDER = "RE_ORDER";
    public static final String INTENT_NOTIFICATION_ORDER = "order_id";
    public static final String INTENT_ORDER = "order";
    public static final String INTENT_ORDERS = "orders";
    public static final String INTENT_MEDICINE_DETAILS = "medicine";
    public static final String INTENT_ONLY_DETAILS = "only_details";
    public static final String INTENT_SELECT_PHARMACY = "select_pharmacy";
    public static final String INTENT_EDIT_CART = "edit_cart";
    public static final String INTENT_GRAPH_TYPE = "intent_graph_type";
    public static final String INTENT_CODE_USER_SIGN_IN = "singin_params";
    public static final String INTENT_REGISTERED = "registered";
    public static final String INTENT_UPDATE_USER = "update_user";
    public static final String INTENT_UPDATE_PHARMACY = "update_pharmacy";
    public static final String INTENT_PHARMACY_ID = "pharmacy_id";
    public static final String INTENT_PROMOTION = "promotion";
    public static final String INTENT_PROMOTION_SELECT = "promotion_select";
    public static final String INTENT_PROMOTION_POSITION = "promotion_position";
    public static final String INTENT_PROMOTION_EDIT = "promotion_edit";
    public static final String INTENT_PROMOTION_DELETE = "promotion_delete";
    public static final String INTENT_PROMOTION_CODE = "promotion_code";

    public static final String INTENT_SUB_PRICE = "sub_price";
    public static final String INTENT_PRICE = "price";
    public static final String INTENT_DISCOUNT = "discount";
    public static final String INTENT_POSITION = "position";
    public static final String INTENT_CURRENCY = "currency";
    public static final String INTENT_DISCOUNT_TYPE = "discount_type";
    public static final String INTENT_APPLY_ALL = "apply_all";


    // Intent Req Codes

    public static final Integer INTENT_ORDER_DETAILS = 11;
    public static final Integer INTENT_SET_QUANTITY = 1;
    public static final Integer INTENT_SET_DISCOUNT = 2;
    public static final Integer INTENT_SET_PRICING = 5;
    public static final Integer INTENT_USER_SIGN_IN = 3;
    public static final Integer INT_CODE_SELECT_PHARMACY = 4;


    // Temp Lists & Objects

    public static List<List<PharmacyDetails>> list = new ArrayList<>();
    public static List<PharmacyDetails> cList = new ArrayList<>();
    public static List<PharmacyDetails> cartNearbyList = new ArrayList<>();
    public static List<PharmacyDetails> nearestPharmaciesList = new ArrayList<>();
    public static List<PharmacyDetails> cSearchedPharmacies = new ArrayList<>();
    public static List<MedicineDetails> cSearchedMedicines = new ArrayList<>();
    public static List<Order> cOrdersList = new ArrayList<>();
    public static Order order;

    public static String Address = "address";
    public static final int MAX_QUANTITY = 999;
    public static final String DEFAULT_USER_ID = "-1";
    public static final int BORDER_WIDTH = 2;
    public static final String PRESCRIPTION_IMAGE = "PRESCRIPTION_IMAGE";
    public static final String IMAGE_PATH_PRESCRIPTION = "PRESCRIPTION_IMAGE_PATH";
    public static final String IMAGE_LICENSE = "LICENSE_IMAGE";
    public static final String IMAGE_PATH_LICENSE = "LICENSE_IMAGE_PATH";
    public static final String IMAGE_PATH_PROMOTION = "PROMOTION_IMAGE_PATH";
    public static final String LABEL_ORDER_ITEM_PRICE = "Total Price:";
    public static final String LABEL_ORDER_ITEM_DISCOUNT = "Discount:";

    public static List<PharmacyDetails> new_suggestions = new ArrayList<>();

    // Pharmacy Features Texts

    public static final String FEATURE_24_7_ON = "Pharmacy remains open 24hrs a day and 7 days a week";
    public static final String FEATURE_24_7_OFF = "Pharmacy is open for specific timings";
    public static final String FEATURE_ONLINE_ORDER_ON = "Pharmacy receives and process online orders";
    public static final String FEATURE_ONLINE_ORDER_OFF = "Pharmacy do not receive online orders";
    public static final String FEATURE_DELIVERY_ON = "Pharmacy provides delivery";
    public static final String FEATURE_DELIVERY_OFF = "Pharmacy do not provide delivery";


    // User Types

    public static final String TYPE_USER = "USER";
    public static final String TYPE_PHARMACY = "PHARMACY";


    // Gender

    public static final String GENDER_MALE = "Male";
    public static final String GENDER_THIRD = "Other";
    public static final String GENDER_FEMALE = "Female";


    // Pharmacy Status

    public static final String PHARMACY_STATUS_REGISTERED = "REGISTERED";


    // Checkout Buttons

    public static final String CHECKOUT_ORDER = "Checkout";
    public static final String CHECKOUT_SMS = "Send SMS";


    // Note Types

    public static final String NOTE_ORDER = "Order Note:";
    public static final String NOTE_REJECT = "Rejection Note:";


    // Number Type
    public static final String NUMBER_TYPE_MOBILE = "MOBILE";
    public static final String NUMBER_TYPE_LANDlINE = "FIXED_LINE";


    // Default

    public static final String DEFAULT_CURRENCY = "PK-PKR";
    public static final String DEFAULT_DOSAGE = "Volume: -";
    public static final String AUTHENTICATION_HEADER = "authorization";

    // Headers

    public static final String HEADER_AUTH = "authorization";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_CONTENT_TYPE_VALUE = "application/json";


    // SESSION

    public static final String SESSION_EXPIRE = "Session Expired";
    public static final String SESSION_EXPIRE_LOGIN = "You are logged in on another device";
    public static final String SESSION_INVALID_TOKEN = "Invalid token";
    public static final String SESSION_EXPIRED_TOKEN = "token has been expired !!!";
    public static final String SESSION_NO_TOKEN = "Access denied.No token provided.";



    // Action Types

    public static final String EVENT_VIEW = "VIEW";
    public static final String EVENT_CALL = "CALL";
    public static final String EVENT_SMS = "SMS";
    public static final String EVENT_SHARE = "SHARE";
    public static final String EVENT_DIRECTION = "DIRECTION";
    public static final String EVENT_ORDER = "ORDER";

    // Order Types

    public static final String ORDER_DELIVERY = "DELIVERY";
    public static final String ORDER_PICK_UP = "PICK_UP";


    // Order Status

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_CONFIRMATION = "CONFIRMATION";
    public static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String STATUS_ENROUTE = "ENROUTE";
    public static final String STATUS_READY = "READY";
    public static final String STATUS_COMPLETE = "COMPLETE";
    public static final String STATUS_CANCELLED_PENDING = "CANCELLED_PENDING";
    public static final String STATUS_CANCELLED_CONFIRMATION = "CANCELLED_CONFIRMATION";
    public static final String STATUS_REJECTED_PENDING = "REJECTED_PENDING";
    public static final String STATUS_REJECTED_PROCESSING = "REJECTED_PROCESSING";
    public static final String STATUS_REJECTED_READY = "REJECTED_READY";
    public static final String STATUS_REJECTED_ENROUTE = "REJECTED_ENROUTE";
    public static final String STATUS_CANCELLED = "CANCELLED";
    public static final String STATUS_REJECTED = "REJECTED";
    public static final String STATUS_OFFLINE = "OFFLINE";


    public static final String PICK_UP = "READY";
    public static final String IN_PROGRESS = "IN PROGRESS";
    public static final String SMS_ORDER = "SMS Order";


    // Order Status Bar Percent

    public static final Integer PERCENT_PENDING = 100;
    public static final Integer PERCENT_CONFIRMATION = 75;
    public static final Integer PERCENT_IN_PROGRESS = 50;
    public static final Integer PERCENT_READY = 25;
    public static final Integer PERCENT_ENROUTE = 25;
    public static final Integer PERCENT_COMPLETE = 0;
    public static final Integer PERCENT_CANCELED = 66;
    public static final Integer PERCENT_REJECTED = 66;


    // Dashboard Stats Types

    public static final String DATA_ORDERS = "ORDER";
    public static final String DATA_VIEWS = "VIEW";
    public static final String DATA_CALLS = "CALL";
    public static final String DATA_MSGS = "SMS";
    public static final String DATA_DIRECTIONS = "DIRECTION";
    public static final String DATA_SHARES = "SHARE";


    // Order Buttons

    public static final String BUTTON_ACCEPT = "Accept";
    public static final String BUTTON_READY = "Ready";
    public static final String BUTTON_ENROUTE = "Enroute";
    public static final String BUTTON_COMPLETE = "Complete";
    public static final String BUTTON_CANCEL = "Cancel";
    public static final String BUTTON_REJECT = "Reject";
    public static final String BUTTON_REORDER = "Re-Order";


    // Date Formats

    public static String SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static String OUR_DATE_FORMAT = "dd MMM', 'yyyy";
    public static String OUR_TIME_FORMAT = "EEE', 'hh:mm a";
    public static String OUR_DATE_TIME_FORMAT = "dd MMM', 'yyyy' ('EEE') 'hh:mm a";
    public static String OUR_PRESCRIPTION_DATE_FORMAT = "ddMMyy'_'hhmm";
    public static String PROMOTION_DATE_FORMAT = "MM-dd-yyyy";


    //

    public static final String COLOR_BLACK = "BLACK";
    public static final String COLOR_WHITE = "WHITE";
    public static final String COLOR_GREY = "GREY";
    public static final String COLOR_FORM = "FORM";
    public static final String COLOR_SHADOW = "SHADOW";

    public static final String NAVIGATION_BAR_INVISIBLE = "NAVIGATION_BAR_INVISIBLE";
    public static final String NAVIGATION_BAR_AT_BOTTOM = "NAVIGATION_BAR_AT_BOTTOM";
    public static final String NAVIGATION_BAR_ON_RIGHT = "NAVIGATION_BAR_ON_RIGHT";


    // Fragment Tags

    public static final String TAG_PHARMACY_DETAILS_FRAGMENT = "TAG_PHARMACY_DETAILS_FRAGMENT";
    public static final String TAG_MEDICINE_DETAILS_FRAGMENT = "TAG_MEDICINE_DETAILS_FRAGMENT";

    public static final String PHARMACY_LOGO_NAME = "logo.jpeg";
    public static final String PHARMACY_LICENSE_NAME = "license.jpeg";
    public static final String USER_PROFILE_PIC_NAME = "profile_picture.jpeg";

    public static final String FOR_ABOUT_US = "FOR_ABOUT_US";
    public static final String FOR_DISCLAIMERS = "FOR_DISCLAIMERS";
    public static final String FOR_CHANGE_PASSWORD = "FOR_CHANGE_PASSWORD";
    public static final String PHARMACY_LOGIN = "PHARMACY_LOGIN";
    public static final String USER_LOGIN = "USER_LOGIN";

    public static String PUSH_NOTIFICATION_KEY_PAIR = "version_code";
    public static String PUSH_NOTIFICATION_NEW_ORDER = "newOrder";
    public static String PUSH_NOTIFICATION_ORDER_USER_DETAILS = "order_user";
    public static String PUSH_NOTIFICATION_ORDER_PHARMACY_DETAILS = "order_pharmacy";
    public static String PUSH_NOTIFICATION_GENERAL = "general";


    // Register Dialog

    public static final String REGISTER_DIALOG_TITLE = "Register";
    public static final String REGISTER_DIALOG_MESSAGE = "Sign In / Register to Place the Order";
    public static final String REGISTER_DELIVERY_DIALOG_MESSAGE = "Sign In / Register to get delivery.";
    public static final String REGISTER_SMS_DIALOG_MESSAGE = "Sign In / Register to Place Online Order or Send Order via SMS";
    public static final String REGISTER_ATTACHMENT_DIALOG_MESSAGE = "Sign In / Register to Attach Prescription(s)";
    public static final String REGISTER_AUDIO_NOTE_DIALOG_MESSAGE = "Sign In / Register to send Audio Note";
    public static final String REGISTER_PROMO_DIALOG_MESSAGE = "Sign In / Register to get promotions";
    public static final String REGISTER_DIALOG_BUTTON_1 = "Sign In";
    public static final String REGISTER_DIALOG_BUTTON_2 = "Register";
    public static final String REGISTER_DIALOG_BUTTON_3 = "Send SMS";


    // Order limit less User Dialog

    public static final String LIMIT_USER_DIALOG_TITLE = "Less than Order Limit";
    public static final String LIMIT_USER_DIALOG_MESSAGE = "\nYou have to pick your order.";
    public static final String LIMIT_CHARGES_USER_DIALOG_MESSAGE = "\nYou have to pay delivery charges: ";
    public static final String LIMIT_USER_DIALOG_BUTTON_1 = "Cancel";
    public static final String LIMIT_USER_DIALOG_BUTTON_2 = "Confirm";
    public static final String LIMIT_CHARGES_USER_DIALOG_BUTTON_1 = "Pickup";
    public static final String LIMIT_CHARGES_USER_DIALOG_BUTTON_2 = "Delivery";


    // Order limit less Pharmacy Dialog

    public static final String LIMIT_PHARMACY_DIALOG_TITLE = "Less than Order Limit Pharmacy";
    public static final String LIMIT_PHARMACY_DIALOG_MESSAGE = "This order is less than the minimum limit set by you do you want to proceed with this order.";
    public static final String LIMIT_PHARMACY_DIALOG_BUTTON_1 = "Cancel";
    public static final String LIMIT_PHARMACY_DIALOG_BUTTON_2 = "Confirm";


    // Logout Dialog

    public static final String LOGOUT_DIALOG_TITLE = "Come Back Soon";
    public static final String LOGOUT_DIALOG_MESSAGE = " Are you sure you want to logout ?";
    public static final String LOGOUT_DIALOG_BUTTON_1 = "No";
    public static final String LOGOUT_DIALOG_BUTTON_2 = "Yes";


    // Confirm Order Dialog

    public static final String CONFIRM_ORDER_DIALOG_TITLE = "Pick Up";
    public static final String CONFIRM_ORDER_DIALOG_MESSAGE = "Pick up only, delivery is not available";
    public static final String CONFIRM_ORDER_DIALOG_BUTTON_1 = "Cancel";
    public static final String CONFIRM_ORDER_DIALOG_BUTTON_2 = "Confirm";


    // Cancel Order User Dialog

    public static final String CANCEL_ORDER_USER_DIALOG_TITLE = "Cancel Order";
    public static final String CANCEL_ORDER_USER_DIALOG_MESSAGE = "Are you sure to cancel order ?";
    public static final String CANCEL_ORDER_USER_DIALOG_BUTTON_1 = "No";
    public static final String CANCEL_ORDER_USER_DIALOG_BUTTON_2 = "Yes";
    public static final String CANCEL_ORDER_USER_DIALOG_BUTTON_PICKUP = "Pickup";


    // SMS Order Dialog

    public static final String SMS_ORDER_USER_DIALOG_TITLE = "SMS Order";
    public static final String SMS_ORDER_USER_DIALOG_MESSAGE = "Prescription(s) can not be attached in SMS Order";
    public static final String SMS_ORDER_USER_DIALOG_BUTTON_1 = "Cancel";
    public static final String SMS_ORDER_USER_DIALOG_BUTTON_2 = "Ok";


    // Audio File Tags

    public static String PLAY_WELCOME = "PLAY_WELCOME";
    public static String PLAY_HOME_USER = "PLAY_HOME_USER";
    public static String PLAY_HOME_PHARMACY = "PLAY_HOME_PHARMACY";
    public static String PLAY_SEARCH = "PLAY_SEARCH";
    public static String PLAY_CART = "PLAY_CART";
    public static String PLAY_CART_SELECTED = "PLAY_CART_SELECTED";
    public static String PLAY_MOVE_ON_MAP = "PLAY_MOVE_ON_MAP";
    public static String PLAY_REGISTER_USER = "PLAY_REGISTER_USER";
    public static String PLAY_REGISTER_PHARMACY = "PLAY_REGISTER_PHARMACY";
    public static String PLAY_SEARCH_PHARMACY = "PLAY_SEARCH_PHARMACY";
    public static String PLAY_SEARCH_MEDICINE = "PLAY_SEARCH_MEDICINE";


    // Played

    public static Boolean PLAYED_WELCOME = false;
}