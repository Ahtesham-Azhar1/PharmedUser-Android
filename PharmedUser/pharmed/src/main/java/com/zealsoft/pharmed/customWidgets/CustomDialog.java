package com.zealsoft.pharmed.customWidgets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.zealsoft.pharmed.R;
import com.zealsoft.pharmed.Util.Constants;
import com.zealsoft.pharmed.Util.Preferences;
import com.zealsoft.pharmed.activities.CartKActivity;
import com.zealsoft.pharmed.activities.OrderDetailsUserActivity;
import com.zealsoft.pharmed.activities.OrdersUserActivity;
import com.zealsoft.pharmed.activities.PlaceOrderActivity;
import com.zealsoft.pharmed.activities.SearchKActivity;

import java.util.ArrayList;

public class CustomDialog {

    private Context context;

    // Dialog to show loading all over in the app
    private Dialog loadingDialogue;

    // Dialog to show options when click on profile pic
    private Dialog setImageDialogue;
    private TextView selectImage;
    private TextView removeImage;

    // Dialog to ask for clearing cart or not
    private ConstraintLayout cleanCartDialog;
    private Dialog askCartCleaning;
    private TextView title, message;
    private Button clean;
    private Button sendOthers;

    // Dialog to ask for deleting any item from cart
    private ConstraintLayout deleteItemDialog;
    private Dialog askDeleteItemDialog;
    private TextView titleDID, messageDID;
    private Button ok;
    private Button cancel;

    // General Dialog
    private ConstraintLayout generalDialogView;
    private Dialog generalDialog;
    private TextView dTitle, dMessage;
    private Button no;
    private Button yes;
    private Button button3;

    // Cancel Order Dialog
    private ConstraintLayout cancelOrderDialogView;
    private Dialog cancelOrderDialog;
    private TextView dCOTitle, dCOMessage, customReason;
    private RadioGroup reasonsGroup;
    private RadioButton reason1, reason2, reason3, reason4, reason5, otherReason, selectedReason;
    private Button oCancel;
    private Button submit;


    // Confirm Order Dialog
    private ConstraintLayout confirmOrderDialogView;
    private Dialog confirmOrderDialog;
    private TextView dConfirmOTitle, dConfirmOMessage;
    private RadioGroup orderTypeGroup;
    private RadioButton type1, type2, selectedType;
    private Button oConfirmCancel;
    private Button confirm;

    // Process Order Dialog
    private ConstraintLayout processOrderDialogView;
    private Dialog processOrderDialog;
    private TextView pCOTitle, pCOMessage;
    private RadioGroup optionsGroup;
    private RadioButton acceptRB, changeRB, deliveryWithChargesRB, cancelRB, selectedOption;
    private EditText deliveryCharges;
    private Button pCancel;
    private Button pSubmit;

    // Hover Text Dialog
    private ConstraintLayout hoverTextDialogView;
    private Dialog hoverTextDialog;
    private TextView hoverText;


    public CustomDialog(Context context){
        this.context = context;
    }

    //----------------------------------------------------------------------------------------------
    // Loading Dialog

    public void setUpLoadingDialogue(){
        if (context != null && loadingDialogue == null) {
            loadingDialogue = new Dialog(context, R.style.CustomDialog);
            loadingDialogue.setContentView(R.layout.custom_loading_progress);
            loadingDialogue.setCancelable(false);
            loadingDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            loadingDialogue.setOnKeyListener(new Dialog.OnKeyListener() {
//
//                @Override
//                public boolean onKey(DialogInterface arg0, int keyCode,
//                                     KeyEvent event) {
//                    // TODO Auto-generated method stub
//                    if (keyCode == KeyEvent.KEYCODE_BACK) {
//
//                        dismissLoadingDialogue();
//                    }
//                    return true;
//                }
//            });
        }
    }

    public void dismissLoadingDialogue(){
        if(loadingDialogue != null && loadingDialogue.isShowing()){
            loadingDialogue.dismiss();
        }
    }

    public void showLoadingDialogue(){
        if(loadingDialogue == null)
            setUpLoadingDialogue();

        if(loadingDialogue !=null && !loadingDialogue.isShowing()){
            loadingDialogue.show();
        }
    }

    //----------------------------------------------------------------------------------------------
    // Cart Clean Dialog

//    public void setCartCleaningDialogue(boolean nightMode){
//        if (context != null && askCartCleaning == null) {
//            askCartCleaning = new Dialog(context, R.style.CustomDialog);
//            askCartCleaning.setContentView(R.layout.custom_dialog_cart_clear);
//            askCartCleaning.setCancelable(true);
//            askCartCleaning.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//            cleanCartDialog = askCartCleaning.findViewById(R.id.cart_clean_item);
//            title = askCartCleaning.findViewById(R.id.dialog_title);
//            message = askCartCleaning.findViewById(R.id.dialog_message);
//            clean = askCartCleaning.findViewById(R.id.clean);
//            sendOthers = askCartCleaning.findViewById(R.id.send_others);
//
//            if (nightMode){
//                cleanCartDialog.setBackgroundResource(R.drawable.background_et_round_night_mode);
//                title.setTextColor(context.getResources().getColor(R.color.white));
//                message.setTextColor(context.getResources().getColor(R.color.textColorNightMode));
//            }
//
//            clean.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    Preferences.removeCartItemsFromSharedPreferences(context);
////                    dismissCartCleaningDialogue();
//
//                    if(context instanceof SearchKActivity){
//                        ((SearchKActivity) context).removeAllFromCart();
//                    }
//                }
//            });
//
//            sendOthers.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dismissCartCleaningDialogue();
//                }
//            });
//        }
//    }
//
//    public void dismissCartCleaningDialogue(){
//        if(askCartCleaning != null && askCartCleaning.isShowing()){
//            askCartCleaning.dismiss();
//        }
//    }
//
//    public void showCartCleaningDialogue(boolean nightMode){
//        if(askCartCleaning == null)
//            setCartCleaningDialogue(nightMode);
//
//        if(askCartCleaning != null && !askCartCleaning.isShowing()){
//            askCartCleaning.show();
//        }
//    }

//    public boolean cartDialogShowing(){
//        if(askCartCleaning != null)
//            return askCartCleaning.isShowing();
//
//        else
//            return false;
//    }


    //----------------------------------------------------------------------------------------------
    // Delete Item Dialog

    public void setDeleteItemDialog(boolean nightMode){
        if (context != null && askDeleteItemDialog == null) {
            askDeleteItemDialog = new Dialog(context, R.style.CustomDialog);
            askDeleteItemDialog.setContentView(R.layout.custom_dialog_delete_item);
            askDeleteItemDialog.setCancelable(true);
            askDeleteItemDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            deleteItemDialog = askDeleteItemDialog.findViewById(R.id.delete_item_dialog);
            titleDID = askDeleteItemDialog.findViewById(R.id.pd_dialog_title);
            messageDID = askDeleteItemDialog.findViewById(R.id.pd_dialog_message);
            ok = askDeleteItemDialog.findViewById(R.id.ok);
            cancel = askDeleteItemDialog.findViewById(R.id.cancel);

            if (nightMode){
                deleteItemDialog.setBackgroundResource(R.drawable.pd_background_et_round_night_mode);
                titleDID.setTextColor(context.getResources().getColor(R.color.white));
                messageDID.setTextColor(context.getResources().getColor(R.color.textColorNightMode));
            }

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissDeleteItemDialog();

                    if(context instanceof CartKActivity){
                        ((CartKActivity) context).removeItems();
                    }
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissDeleteItemDialog();
                }
            });
        }
    }

    public void dismissDeleteItemDialog(){
        if(askDeleteItemDialog != null && askDeleteItemDialog.isShowing()){
            askDeleteItemDialog.dismiss();
        }
    }

    public void showDeleteItemDialog(boolean nightMode){
        if(askDeleteItemDialog == null)
            setDeleteItemDialog(nightMode);

        if(askDeleteItemDialog != null && !askDeleteItemDialog.isShowing()){
            askDeleteItemDialog.show();
        }
    }

    //----------------------------------------------------------------------------------------------
    // General Dialog

    public void setGeneralDialog(boolean nightMode, final String title, String message, String b1Text, String b2Text, String b3Text, Boolean cancellable){
        if (context != null && generalDialog == null || title.equals(Constants.REGISTER_DIALOG_TITLE)) {
            generalDialog = new Dialog(context, R.style.CustomDialog);
            generalDialog.setContentView(R.layout.custom_dialog_general_pharmed);
            generalDialog.setCancelable(cancellable);
            generalDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            generalDialogView = generalDialog.findViewById(R.id.pd_general_dialog);
            dTitle = generalDialog.findViewById(R.id.pd_dialog_title);
            dMessage = generalDialog.findViewById(R.id.pd_dialog_message);
            no = generalDialog.findViewById(R.id.pd_no);
            yes = generalDialog.findViewById(R.id.pd_yes);
            button3 = generalDialog.findViewById(R.id.pd_button_3);

            if (nightMode){
                generalDialogView.setBackgroundResource(R.drawable.pd_background_et_round_night_mode);
                dTitle.setTextColor(context.getResources().getColor(R.color.white));
                dMessage.setTextColor(context.getResources().getColor(R.color.textColorNightMode));
            }

            if(message != null && !message.equals("")){
                dMessage.setText(message);
            }

            if(b1Text != null && !b1Text.equals("")){
                no.setText(b1Text);
            } else {
                no.setVisibility(View.GONE);
            }

            if(b2Text != null && !b2Text.equals("")){
                yes.setText(b2Text);
            } else {
                yes.setVisibility(View.GONE);
            }

            if(b3Text != null && !b3Text.equals("")){
                button3.setVisibility(View.VISIBLE);
                button3.setText(b3Text);
            } else {
                button3.setVisibility(View.GONE);
            }

            if(title != null && !title.equals("")){
                dTitle.setText(title);

                if(title.equals(Constants.REGISTER_DIALOG_TITLE) || title.equals("Registered")) {
                    dTitle.setVisibility(View.GONE);
                    no.setBackgroundResource(R.drawable.pd_background_button_dark);
                } else {
                    no.setBackgroundResource(R.drawable.pd_background_button_dialog_cancel);
                }

                if(title.equals(Constants.LOGOUT_DIALOG_TITLE)) {
                    dTitle.setVisibility(View.GONE);

//                    assert message != null;
//                    String[] logoutMsg = message.split(":");
//
//                    dMessage.setText(Html.fromHtml("<strong>"+ logoutMsg[0] + "</strong>:" + logoutMsg[1]));
                }

                if(title.equals(Constants.CANCEL_ORDER_USER_DIALOG_TITLE)) {
                    dTitle.setVisibility(View.GONE);
                }

                if(title.equals(Constants.LIMIT_USER_DIALOG_TITLE)){
                    dTitle.setVisibility(View.GONE);
                }

                if(title.equals(Constants.LIMIT_PHARMACY_DIALOG_TITLE)){
                    dTitle.setVisibility(View.GONE);
                }

                if(title.equals(Constants.LIMIT_USER_DIALOG_TITLE)){
                    if(no.getText().equals(Constants.LIMIT_CHARGES_USER_DIALOG_BUTTON_1)){
                        no.setBackgroundResource(R.drawable.pd_background_button_dark);
                    }
                }

                if(title.equals(Constants.CANCEL_ORDER_USER_DIALOG_TITLE)) {
                    if (no.getText().equals(Constants.CANCEL_ORDER_USER_DIALOG_BUTTON_PICKUP)) {
                        no.setBackgroundResource(R.drawable.pd_background_button_dark);
                    }
                }
            } else {
                dTitle.setVisibility(View.GONE);
            }

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(context instanceof SearchKActivity || context instanceof CartKActivity || context instanceof PlaceOrderActivity){
                        assert title != null;
                        if(title.equals(Constants.REGISTER_DIALOG_TITLE)) {
//                            Intent intent = new Intent(context, SignInKActivity.class);
//                            intent.putExtra("clear", false);
//                            if(context instanceof SearchKActivity)
//                                ((SearchKActivity) context).startActivityForResult(intent, Constants.INTENT_USER_SIGN_IN);
//                            if(context instanceof CartKActivity)
//                                ((CartKActivity) context).startActivityForResult(intent, Constants.INTENT_USER_SIGN_IN);
//                            if(context instanceof PlaceOrderActivity)
//                                ((PlaceOrderActivity) context).startActivityForResult(intent, Constants.INTENT_USER_SIGN_IN);
                        }

                        if(title.equals(Constants.CONFIRM_ORDER_DIALOG_TITLE)){
//                            ((SearchKActivity) context).placeOrder(false);
                        }
                    }

                    if(title != null && title.equals(Constants.LIMIT_USER_DIALOG_TITLE)){
                        if(no.getText().toString().equals(Constants.LIMIT_CHARGES_USER_DIALOG_BUTTON_1)){
                            if(context instanceof OrdersUserActivity){
                                ((OrdersUserActivity) context).confirmOrder(true);
                            }

                            if(context instanceof OrderDetailsUserActivity){
                                ((OrderDetailsUserActivity) context).confirmOrder(true);
                            }
                        }
                    }

                    if(title != null && title.equals(Constants.CANCEL_ORDER_USER_DIALOG_TITLE)) {
                        if(no.getText().equals(Constants.CANCEL_ORDER_USER_DIALOG_BUTTON_PICKUP)){
                            if(context instanceof OrderDetailsUserActivity)
                                ((OrderDetailsUserActivity) context).confirmOrder(true);

                            if(context instanceof OrdersUserActivity)
                                ((OrdersUserActivity) context).confirmOrder(true);
                        }
                    }

//                    if(context instanceof OrderDetailsUserActivity){
//                        if(title.equals(Constants.LIMIT_USER_DIALOG_TITLE))
//                            ((OrderDetailsUserActivity) context).cancelOrder("");
//                    }
//
//                    if(context instanceof OrdersUserActivity) {
//                        if(title.equals(Constants.LIMIT_USER_DIALOG_TITLE))
//                            ((OrdersUserActivity) context).cancelOrder("");
//                    }

                    dismissGeneralDialog();
                }
            });

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissGeneralDialog();
                    if(context instanceof SearchKActivity || context instanceof CartKActivity || context instanceof PlaceOrderActivity){

                        assert title != null;
                        if(title.equals(Constants.REGISTER_DIALOG_TITLE)) {
//                            Intent intent = new Intent(context, RegisterUserKActivity.class);
//                            intent.putExtra("clear", false);
//                            if(context instanceof SearchKActivity)
//                                ((SearchKActivity) context).startActivityForResult(intent, Constants.INTENT_USER_SIGN_IN);
//                            if(context instanceof CartKActivity)
//                                ((CartKActivity) context).startActivityForResult(intent, Constants.INTENT_USER_SIGN_IN);
//                            if(context instanceof PlaceOrderActivity)
//                                ((PlaceOrderActivity) context).startActivityForResult(intent, Constants.INTENT_USER_SIGN_IN);
                        }

                        if(title.equals(Constants.CONFIRM_ORDER_DIALOG_TITLE)){
//                            ((SearchKActivity) context).placeOrder(true);
                        }

                        if(title.equals(Constants.SMS_ORDER_USER_DIALOG_TITLE)){
//                            if(context instanceof SearchKActivity)
//                                ((SearchKActivity) context).sendSmsOrder();
                            if(context instanceof CartKActivity)
                                ((CartKActivity) context).sendSmsOrder();
                            if(context instanceof PlaceOrderActivity)
                                ((PlaceOrderActivity) context).sendSmsOrder();
                        }

                        dismissGeneralDialog();
                    }

//                    if(context instanceof SetTimingsActivity){
//                        dismissGeneralDialog();
//                        ((SetTimingsActivity) context).finish();
//                    }

//                    if(context instanceof PopupMenuKActivity) {
//                        dismissGeneralDialog();
//                        ((PopupMenuKActivity) context).logoutCall();
//                    }

//                    if(context instanceof SettingsActivity){
//                        dismissGeneralDialog();
//                        ((SettingsActivity) context).logoutCall();
//                    }
//
//                    if(context instanceof MapActivity){
//                        dismissGeneralDialog();
//                        ((MapActivity) context).logoutCall();
//                    }

                    if(context instanceof OrdersUserActivity){
                        dismissGeneralDialog();
                        ((OrdersUserActivity) context).cancelOrder("");
                    }

                    if(context instanceof OrderDetailsUserActivity){
                        dismissGeneralDialog();

                        assert title != null;
                        if(title.equals(Constants.CANCEL_ORDER_USER_DIALOG_TITLE))
                            ((OrderDetailsUserActivity) context).cancelOrder("");

                        if(title.equals(Constants.LIMIT_USER_DIALOG_TITLE))
                            ((OrderDetailsUserActivity) context).confirmOrder(false);
                    }

                    if(context instanceof OrdersUserActivity){
                        if(title.equals(Constants.LIMIT_USER_DIALOG_TITLE))
                            ((OrdersUserActivity) context).confirmOrder(false);
                    }

//                    if(title.equals(Constants.LIMIT_PHARMACY_DIALOG_TITLE)){
//                        if(context instanceof OrderDetailsActivity)
//                            ((OrderDetailsActivity) context).continueOrder();
//
//                        if(context instanceof OrdersActivity)
//                            ((OrdersActivity) context).continueOrder();
//                    }
                }
            });

            button3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(context instanceof SearchKActivity || context instanceof CartKActivity || context instanceof PlaceOrderActivity){
                        if(title.equals(Constants.REGISTER_DIALOG_TITLE)) {
                            if(context instanceof PlaceOrderActivity)
                                ((PlaceOrderActivity) context).sendSmsOrder();
                        }
                    }
                }
            });
        }
    }

    public void dismissGeneralDialog(){
        if(generalDialog != null && generalDialog.isShowing()){
            generalDialog.dismiss();
        }
    }

    public void showGeneralDialog(boolean nightMode, String title, String message, String b1Text, String b2Text, String b3Text, Boolean cancellable){
        if(generalDialog == null || title.equals(Constants.REGISTER_DIALOG_TITLE))
            setGeneralDialog(nightMode, title, message, b1Text, b2Text, b3Text, cancellable);

        if(generalDialog != null && !generalDialog.isShowing()){
            generalDialog.show();
        }
    }

    //----------------------------------------------------------------------------------------------
    // Cancel Order Dialog

    public void setCancelOrderDialog(boolean nightMode, String title, String message, String b1Text, String b2Text, String dLimit, String orderType){
        if (context != null && cancelOrderDialog == null) {
            cancelOrderDialog = new Dialog(context, R.style.CustomDialog);
            cancelOrderDialog.setContentView(R.layout.custom_dialog_cancel_order);
            cancelOrderDialog.setCancelable(true);
            cancelOrderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            cancelOrderDialogView = cancelOrderDialog.findViewById(R.id.cancel_order_dialog);
            dCOTitle = cancelOrderDialog.findViewById(R.id.pd_dialog_title);
            dCOMessage = cancelOrderDialog.findViewById(R.id.pd_dialog_message);
            reasonsGroup = cancelOrderDialog.findViewById(R.id.reasons_radio_group);
            reason1 = cancelOrderDialog.findViewById(R.id.reason_1);
            reason2 = cancelOrderDialog.findViewById(R.id.reason_2);
            reason3 = cancelOrderDialog.findViewById(R.id.reason_3);
            reason4 = cancelOrderDialog.findViewById(R.id.reason_4);
            reason5 = cancelOrderDialog.findViewById(R.id.reason_5);
            otherReason = cancelOrderDialog.findViewById(R.id.other_reason);
            customReason = cancelOrderDialog.findViewById(R.id.custom_reason);
            customReason.setFilters(new InputFilter[] {new InputFilter.LengthFilter(Constants.FIELD_NOTE)});
            oCancel = cancelOrderDialog.findViewById(R.id.cancel);
            submit = cancelOrderDialog.findViewById(R.id.submit);

            if(dLimit != null && !dLimit.equals("")) {
                if(orderType != null && orderType.equals(Constants.ORDER_DELIVERY)) {
                    reason5.setText("Order Value < " + dLimit);
                    reason5.setVisibility(View.VISIBLE);
                } else
                    reason5.setVisibility(View.GONE);
            } else
                reason5.setVisibility(View.GONE);

            reason1.setButtonDrawable(R.drawable.radio_button_selector);
            reason2.setButtonDrawable(R.drawable.radio_button_selector);
            reason3.setButtonDrawable(R.drawable.radio_button_selector);
            reason4.setButtonDrawable(R.drawable.radio_button_selector);
            reason5.setButtonDrawable(R.drawable.radio_button_selector);
            otherReason.setButtonDrawable(R.drawable.radio_button_selector);

            if (nightMode){
                cancelOrderDialogView.setBackgroundResource(R.drawable.pd_background_et_round_night_mode);
                dCOTitle.setTextColor(context.getResources().getColor(R.color.white));
                dCOMessage.setTextColor(context.getResources().getColor(R.color.textColorNightMode));
                reason1.setTextColor(context.getResources().getColor(R.color.textColorNightMode));
                reason2.setTextColor(context.getResources().getColor(R.color.textColorNightMode));
                reason3.setTextColor(context.getResources().getColor(R.color.textColorNightMode));
                reason4.setTextColor(context.getResources().getColor(R.color.textColorNightMode));
                reason5.setTextColor(context.getResources().getColor(R.color.textColorNightMode));
                otherReason.setTextColor(context.getResources().getColor(R.color.textColorNightMode));
                customReason.setTextColor(context.getResources().getColor(R.color.textColorNightMode));
            }

            reasonsGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    View radioButton = group.findViewById(checkedId);
                    int index = group.indexOfChild(radioButton);
                    if (radioButton.getId() == R.id.other_reason) {
                        customReason.setVisibility(View.VISIBLE);
                    } else {
                        customReason.setText(null);
                        customReason.setVisibility(View.GONE);
                    }
                }
            });

            if(title != null && !title.equals("")){
                dCOTitle.setText(title);
            }

            if(message != null && !message.equals("")){
                dCOMessage.setText(message);
            }

            if(b1Text != null && !b1Text.equals("")){
                oCancel.setText(b1Text);
            }

            if(b2Text != null && !b2Text.equals("")){
                submit.setText(b2Text);
            }

            oCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissCancelOrderDialog();
                }
            });

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int selectedId = reasonsGroup.getCheckedRadioButtonId();

                    if(selectedId == -1){

//                        if(context instanceof OrdersActivity)
//                            ((OrdersActivity) context).showToast("Please select one reason");
//
//                        if (context instanceof OrderDetailsActivity)
//                            ((OrderDetailsActivity) context).showToast("Please select one reason");

                    } else {

                        selectedReason = (RadioButton) cancelOrderDialog.findViewById(selectedId);

                        Preferences.addOrdersCountChangeBooleanToSharedPreferences(context, true);

                        String reason = selectedReason.getText().toString();

                        Boolean reasonSelected = false;

                        if(selectedReason.getId() == R.id.other_reason){
                            if(!customReason.getText().toString().isEmpty()){
                                reason = customReason.getText().toString();
                                reasonSelected = true;
                            } else {

                                reasonSelected = false;
//                                if(context instanceof OrdersActivity)
//                                    ((OrdersActivity) context).showToast(Constants.CUSTOM_REASON_EMPTY);
//
//                                if(context instanceof OrderDetailsActivity)
//                                    ((OrderDetailsActivity) context).showToast(Constants.CUSTOM_REASON_EMPTY);
                            }
                        } else {
                            reasonSelected = true;
                        }

                        if(reasonSelected) {
//                            if (context instanceof OrderDetailsActivity) {
//                                ((OrderDetailsActivity) context).cancelOrder(reason);
//                            }
//
//                            if (context instanceof OrdersActivity) {
//                                ((OrdersActivity) context).cancelOrder(reason);
//                            }

                            dismissCancelOrderDialog();
                        }
                    }
                }
            });
        }
    }

    public void dismissCancelOrderDialog(){
        if(cancelOrderDialog != null && cancelOrderDialog.isShowing()){
            cancelOrderDialog.dismiss();
        }
    }

    public void showCancelOrderDialog(boolean nightMode, String title, String message, String b1Text, String b2Text,  String dLimit, String orderType){
        if(cancelOrderDialog == null)
            setCancelOrderDialog(nightMode, title, message, b1Text, b2Text, dLimit, orderType);

        if(cancelOrderDialog != null && !cancelOrderDialog.isShowing()){
            cancelOrderDialog.show();
        }
    }


    //----------------------------------------------------------------------------------------------
    // Confirm Order Dialog

    public void setConfirmOrderDialog(boolean nightMode, String title, String message, String b1Text, String b2Text, Boolean deliveryAvailable, ArrayList<String> addresses){
        if (context != null) {
            confirmOrderDialog = new Dialog(context, R.style.CustomDialog);
            confirmOrderDialog.setContentView(R.layout.custom_dialog_confirm_order);
            confirmOrderDialog.setCancelable(true);
            confirmOrderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            confirmOrderDialogView = confirmOrderDialog.findViewById(R.id.confirm_order_dialog);
            dConfirmOTitle= confirmOrderDialog.findViewById(R.id.pd_dialog_title);
            dConfirmOMessage = confirmOrderDialog.findViewById(R.id.pd_dialog_message);
            orderTypeGroup = confirmOrderDialog.findViewById(R.id.types_radio_group);
            type1 = confirmOrderDialog.findViewById(R.id.type_1);
            type2 = confirmOrderDialog.findViewById(R.id.type_2);
            oConfirmCancel = confirmOrderDialog.findViewById(R.id.cancel);
            confirm = confirmOrderDialog.findViewById(R.id.confirm);

            final View divider = confirmOrderDialog.findViewById(R.id.divider);
            final TextView addressSelectLabel = confirmOrderDialog.findViewById(R.id.select_address_label);
            final RadioGroup addressesGroup = confirmOrderDialog.findViewById(R.id.addresses_group);

            type1.setButtonDrawable(R.drawable.radio_button_selector);
            type2.setButtonDrawable(R.drawable.radio_button_selector);

            if (nightMode){
                confirmOrderDialogView.setBackgroundResource(R.drawable.pd_background_et_round_night_mode);
                dConfirmOTitle.setTextColor(context.getResources().getColor(R.color.white));
                dConfirmOMessage.setTextColor(context.getResources().getColor(R.color.textColorNightMode));
                type1.setTextColor(context.getResources().getColor(R.color.textColorNightMode));
                type2.setTextColor(context.getResources().getColor(R.color.textColorNightMode));
                divider.setBackgroundResource(R.color.textColorNightMode);
                addressSelectLabel.setTextColor(context.getResources().getColor(R.color.textColorNightMode));
            }

            if(title != null && !title.equals("")){
                dConfirmOTitle.setText(title);
            }

            if(message != null && !message.equals("")){
                dConfirmOMessage.setText(message);
            }

            if(b1Text != null && !b1Text.equals("")){
                oConfirmCancel.setText(b1Text);
            }

            if(b2Text != null && !b2Text.equals("")){
                confirm.setText(b2Text);
            }

            if(!deliveryAvailable){
                type1.setChecked(true);
                type2.setEnabled(false);
            } else {
                type2.setEnabled(true);
            }

            for (int i = 0; i < addresses.size(); i++) {
                RadioButton radioButton = new RadioButton(context);
                radioButton.setText(addresses.get(i));
                radioButton.setId(i);
                if(nightMode) {
                    radioButton.setTextColor(context.getResources().getColor(R.color.textColorNightMode));
                }
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(10, 5, 10, 5);
                radioButton.setLayoutParams(params);
                addressesGroup.addView(radioButton);
            }

            orderTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    View radioButton = group.findViewById(checkedId);
                    int index = group.indexOfChild(radioButton);
                    if (radioButton.getId() == R.id.type_2) {
                        divider.setVisibility(View.VISIBLE);
                        addressSelectLabel.setVisibility(View.VISIBLE);
                        addressesGroup.setVisibility(View.VISIBLE);
                    } else {
                        divider.setVisibility(View.GONE);
                        addressSelectLabel.setVisibility(View.GONE);
                        addressesGroup.setVisibility(View.GONE);
                    }
                }
            });

            oConfirmCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (context instanceof SearchKActivity) {
//                        ((SearchKActivity) context).placeOrder(false, "", false, "");
                    }

                    dismissConfirmOrderDialog();
                }
            });

            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int selectedId = orderTypeGroup.getCheckedRadioButtonId();
                    int selectedAddressId = addressesGroup.getCheckedRadioButtonId();
                    RadioButton selectedAddress = new RadioButton(context);

                    boolean typeSelected = false;
                    boolean addressSelected = false;

                    if(selectedId == -1){

                        if (context instanceof SearchKActivity) {
                            ((SearchKActivity) context).showToast("Please select order type");
                        }

                        if(context instanceof CartKActivity) {
                            ((CartKActivity) context).showToast("Please select order type");
                        }

                        typeSelected = false;
                    } else {

                        selectedType = (RadioButton) confirmOrderDialog.findViewById(selectedId);

                        typeSelected = true;

                    }

                    if(selectedAddressId == -1 && selectedType.getText().toString().equals("Delivery")){

                        if (context instanceof SearchKActivity) {
                            ((SearchKActivity) context).showToast("Please select your address");
                        }

                        if(context instanceof CartKActivity) {
                            ((CartKActivity) context).showToast("Please select your address");
                        }

                        addressSelected = false;
                    } else if (selectedAddressId == -1){

                        addressSelected = true;

                    } else {
                        selectedAddress = (RadioButton) confirmOrderDialog.findViewById(selectedAddressId);
                        addressSelected = true;
                    }


                    if(typeSelected && addressSelected){
                        if (context instanceof SearchKActivity) {
//                            ((SearchKActivity) context).placeOrder(true, selectedType.getText().toString(), true, selectedAddress.getText().toString());
                        }

                        if (context instanceof CartKActivity) {
                            ((CartKActivity) context).placeOrder(true, selectedType.getText().toString(), true, selectedAddress.getText().toString());
                        }

                        if(context instanceof PlaceOrderActivity) {
                            ((PlaceOrderActivity) context).populateAddress(selectedAddress.getText().toString());
                        }

                        dismissConfirmOrderDialog();
                    }
                }
            });
        }
    }

    public void dismissConfirmOrderDialog(){
        if(confirmOrderDialog!= null && confirmOrderDialog.isShowing()){
            confirmOrderDialog.dismiss();
        }
    }

    public void showConfirmOrderDialog(boolean nightMode, String title, String message, String b1Text, String b2Text, Boolean deliveryAvailable, ArrayList<String> addresses){
//        if(confirmOrderDialog == null)
            setConfirmOrderDialog(nightMode, title, message, b1Text, b2Text, deliveryAvailable, addresses);

        if(confirmOrderDialog != null && !confirmOrderDialog.isShowing()){
            confirmOrderDialog.show();
        }
    }

    //----------------------------------------------------------------------------------------------
    // Process Order Dialog

    public void setProcessOrderDialog(boolean nightMode, String title, String message, String b1Text, String b2Text){
        if (context != null && processOrderDialog == null) {
            processOrderDialog = new Dialog(context, R.style.CustomDialog);
            processOrderDialog.setContentView(R.layout.custom_dialog_process_order);
            processOrderDialog.setCancelable(true);
            processOrderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            processOrderDialogView = processOrderDialog.findViewById(R.id.process_order_dialog);
            pCOTitle = processOrderDialog.findViewById(R.id.pd_dialog_title);
            pCOMessage = processOrderDialog.findViewById(R.id.pd_dialog_message);
            optionsGroup = processOrderDialog.findViewById(R.id.options_radio_group);
            acceptRB = processOrderDialog.findViewById(R.id.accept);
            changeRB = processOrderDialog.findViewById(R.id.change);
            deliveryWithChargesRB = processOrderDialog.findViewById(R.id.delivery_with_charges);
            cancelRB = processOrderDialog.findViewById(R.id.reject);
            pCancel = processOrderDialog.findViewById(R.id.cancel);
            pSubmit = processOrderDialog.findViewById(R.id.submit);
            deliveryCharges = processOrderDialog.findViewById(R.id.delivery_charges);

            acceptRB.setButtonDrawable(R.drawable.radio_button_selector);
            changeRB.setButtonDrawable(R.drawable.radio_button_selector);
            deliveryWithChargesRB.setButtonDrawable(R.drawable.radio_button_selector);
            cancelRB.setButtonDrawable(R.drawable.radio_button_selector);

            if (nightMode){
                processOrderDialogView.setBackgroundResource(R.drawable.pd_background_et_round_night_mode);
                pCOTitle.setTextColor(context.getResources().getColor(R.color.white));
                pCOMessage.setTextColor(context.getResources().getColor(R.color.textColorNightMode));
                acceptRB.setTextColor(context.getResources().getColor(R.color.textColorNightMode));
                changeRB.setTextColor(context.getResources().getColor(R.color.textColorNightMode));
                deliveryWithChargesRB.setTextColor(context.getResources().getColor(R.color.textColorNightMode));
                cancelRB.setTextColor(context.getResources().getColor(R.color.textColorNightMode));
                deliveryCharges.setBackgroundResource(R.drawable.pd_background_et_round_night_mode);
                deliveryCharges.setTextColor(context.getResources().getColor(R.color.textColorNightMode));
            }

            optionsGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    View radioButton = group.findViewById(checkedId);
                    int index = group.indexOfChild(radioButton);
                    int id = radioButton.getId();
                    if (id == R.id.accept) {
                        deliveryCharges.setVisibility(View.GONE);
                    } else if (id == R.id.change) {
                        deliveryCharges.setVisibility(View.GONE);
                    } else if (id == R.id.delivery_with_charges) {
                        deliveryCharges.setVisibility(View.VISIBLE);
                    } else if (id == R.id.cancel) {
                        deliveryCharges.setVisibility(View.GONE);
                    }
                }
            });

            if(title != null && !title.equals("")){
                pCOTitle.setText(title);
            }

            if(message != null && !message.equals("")){
                pCOMessage.setText(message);
            }

            if(b1Text != null && !b1Text.equals("")){
                pCancel.setText(b1Text);
            }

            if(b2Text != null && !b2Text.equals("")){
                pSubmit.setText(b2Text);
            }

            pCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissProcessOrderDialog();
                }
            });

            pSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int selectedId = optionsGroup.getCheckedRadioButtonId();

                    if(selectedId == -1){

//                        if (context instanceof OrderDetailsActivity)
//                            ((OrderDetailsActivity) context).showToast("Please select on option");

                    } else {

                        selectedOption = (RadioButton) processOrderDialog.findViewById(selectedId);

                        Preferences.addOrdersCountChangeBooleanToSharedPreferences(context, true);

                        int id = selectedOption.getId();
                        if (id == R.id.accept) {
//                            if (context instanceof OrderDetailsActivity) {
//                                ((OrderDetailsActivity) context).sendPricingCall(false, null);
//                            }
                            dismissProcessOrderDialog();
                        } else if (id == R.id.change) {
//                            if (context instanceof OrderDetailsActivity) {
//                                ((OrderDetailsActivity) context).sendPricingCall(true, null);
//                            }
                            dismissProcessOrderDialog();
                        } else if (id == R.id.delivery_with_charges) {
//                            if (context instanceof OrderDetailsActivity) {
//                                if (deliveryCharges.getText().toString().isEmpty()) {
//                                    ((OrderDetailsActivity) context).showToast("Please enter Delivery Charges");
//                                } else {
//                                    ((OrderDetailsActivity) context).sendPricingCall(false, deliveryCharges.getText().toString());
//                                    dismissProcessOrderDialog();
//                                }
//                            }
                        } else if (id == R.id.reject) {
//                            if (context instanceof OrderDetailsActivity) {
//                                ((OrderDetailsActivity) context).cancelOrder("Order Value is less than Minimum Order Limit");
//                            }
                            dismissProcessOrderDialog();
                        }
                    }
                }
            });
        }
    }

    public void dismissProcessOrderDialog(){
        if(processOrderDialog != null && processOrderDialog.isShowing()){
            processOrderDialog.dismiss();
        }
    }

    public void showProcessOrderDialog(boolean nightMode, String title, String message, String b1Text, String b2Text){
        if(cancelOrderDialog == null)
            setProcessOrderDialog(nightMode, title, message, b1Text, b2Text);

        if(processOrderDialog != null && !processOrderDialog.isShowing()){
            processOrderDialog.show();
        }
    }


    //----------------------------------------------------------------------------------------------
    // Hover Text Dialog

    public void setHoverTextDialog(boolean nightMode, String text){
        if (context != null) {
            hoverTextDialog = new Dialog(context, R.style.CustomDialog);
            hoverTextDialog.setContentView(R.layout.custom_dialog_hover_text);
            hoverTextDialog.setCancelable(true);
            hoverTextDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            hoverTextDialogView = hoverTextDialog.findViewById(R.id.hover_text_dialog);
            hoverText = hoverTextDialog.findViewById(R.id.text);

            if (nightMode){
                hoverTextDialogView.setBackgroundResource(R.drawable.pd_background_et_round_night_mode);
                hoverText.setTextColor(context.getResources().getColor(R.color.white));
            }

            if(text != null && !text.equals("")){
                hoverText.setText(text);
            }
        }
    }

    public void dismissHoverTextDialog(){
        if(hoverTextDialog!= null && hoverTextDialog.isShowing()){
            hoverTextDialog.dismiss();
        }
    }

    public void showHoverTextDialog(boolean nightMode, String text){
//        if(confirmOrderDialog == null)
        setHoverTextDialog(nightMode, text);

        if(hoverTextDialog != null && !hoverTextDialog.isShowing()){
            hoverTextDialog.show();
        }
    }
}
