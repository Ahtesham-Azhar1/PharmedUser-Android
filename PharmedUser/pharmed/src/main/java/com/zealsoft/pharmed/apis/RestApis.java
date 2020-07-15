package com.zealsoft.pharmed.apis;

import com.zealsoft.pharmed.Util.Constants;
import com.zealsoft.pharmed.models.CancellationNote;
import com.zealsoft.pharmed.models.CartParams;
import com.zealsoft.pharmed.models.CheckoutParams;
import com.zealsoft.pharmed.models.GeneralResponse;
import com.zealsoft.pharmed.models.MapParams;
import com.zealsoft.pharmed.models.Params;
import com.zealsoft.pharmed.models.SignInUserParams;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;


public interface RestApis {

    //----------------------------------------------------------------------------------------------
    // Login as User Call

    @Multipart
    @POST("users/signin")
    Call<com.zealsoft.pharmed.models.GeneralResponse> signInUserCall(@Header(Constants.HEADER_AUTH) String authCode,
                                                                     @Part("email") RequestBody email,
                                                                     @Part("password") RequestBody password,
                                                                     @Part("fcmToken") RequestBody fcmToken);

    //----------------------------------------------------------------------------------------------
    // Authentication User Call

    @POST("users/token")
    @Headers("Content-Type: application/json")
    Call<com.zealsoft.pharmed.models.GeneralResponse> tokenUserCall(@Body SignInUserParams signInParams);

    //----------------------------------------------------------------------------------------------
    // Search Medicines

    @POST("medicine/search")
    Call<GeneralResponse> searchMedicines(@Header(Constants.HEADER_AUTH) String authCode,
                                          @Header(Constants.HEADER_CONTENT_TYPE) String content_type,
                                          @Body Params queryP);


    //----------------------------------------------------------------------------------------------
    // Get Cart

    @GET("cart/{id}")
    @Headers("Content-Type: application/json")
    Call<GeneralResponse> getCart(@Path("id") String userId);

    //----------------------------------------------------------------------------------------------
    // Add to Cart

    @Multipart
    @POST("cart/{id}")
    Call<GeneralResponse> addToCart(@Header("Accept") String accept,
                                    @Path("id") String userId,
                                    @Part("productId") RequestBody productId,
                                    @Part("name") RequestBody name,
                                    @Part("price") RequestBody price,
                                    @Part("qty") RequestBody quantity,
                                    @Part("ingrediants") RequestBody ingredients,
                                    @Part("type") RequestBody type,
                                    @Part("pack_size") RequestBody packSize,
                                    @Part("manufacturer") RequestBody manufacturer,
                                    @Part("deleted") RequestBody deleted,
                                    @Part("checkout") RequestBody checkout);

    @Multipart
    @POST("cart/{id}")
    Call<GeneralResponse> addToCart(@Header("Accept") String accept,
                                    @Path("id") String userId,
                                    @Part("productId") String productId,
                                    @Part("name") String name,
                                    @Part("price") String price,
                                    @Part("qty") int quantity,
                                    @Part("ingrediants") String ingredients,
                                    @Part("type") String type,
                                    @Part("pack_size") String packSize,
                                    @Part("manufacturer") String manufacturer,
                                    @Part("deleted") Boolean deleted,
                                    @Part("checkout") Boolean checkout);

    //----------------------------------------------------------------------------------------------
    // Remove all from Cart

    @DELETE("cart/{id}")
    Call<GeneralResponse> removeAllFromCart(@Header("Accept") String accept,
                                            @Path("id") String userId);

    //----------------------------------------------------------------------------------------------
    // Remove from Cart

    @Multipart
    @DELETE("cart/{id}")
    Call<GeneralResponse> removeFromCart(@Header("Accept") String accept,
                                         @Path("id") String userId,
                                         @Part("productId") String productId,
                                         @Part("name") String name,
                                         @Part("price") String price,
                                         @Part("qty") int quantity,
                                         @Part("ingrediants") String ingredients,
                                         @Part("type") String type,
                                         @Part("pack_size") String packSize,
                                         @Part("manufacturer") String manufacturer,
                                         @Part("deleted") Boolean deleted,
                                         @Part("checkout") Boolean checkout);

    //----------------------------------------------------------------------------------------------
    // Remove Selected Cart Items


//    @DELETE("cart/remove/selected/{id}")

//    @FormUrlEncoded
//    @HTTP(method = "DELETE", path = "cart/remove/selected/{id}", hasBody = true)

    @Multipart
    @POST("cart/remove/selected/{id}")
    Call<GeneralResponse> removeSelectedFromCart(@Path("id") String userId,
                                                 @Part("productIds") String productIds);


    //----------------------------------------------------------------------------------------------
    // Send Cart

    @POST("cart/save-cart")
    Call<GeneralResponse> sendCart(@Header(Constants.HEADER_AUTH) String authCode,
                                   @Header(Constants.HEADER_CONTENT_TYPE) String content_type,
                                   @Body CartParams cartParams);

    //----------------------------------------------------------------------------------------------
    // Send Prescriptions

    @Multipart
    @POST("cart/attachments/post")
    Call<GeneralResponse> sendPrescriptions(@Header(Constants.HEADER_AUTH) String authCode,
                                            @Part("userId") RequestBody userId,
                                            @Part("deviceId") RequestBody deviceId,
                                            @Part MultipartBody.Part attachment1,
                                            @Part MultipartBody.Part attachment2,
                                            @Part MultipartBody.Part attachment3);

    //----------------------------------------------------------------------------------------------
    // Get Prescriptions

    @GET("cart/attachments/{id}/{deviceId}")
    Call<GeneralResponse> getPrescriptions(@Path("id") String userId,
                                           @Path("deviceId") String deviceId);

    //----------------------------------------------------------------------------------------------
    // Remove Prescriptions

    @POST("cart/attachments/delete")
    Call<GeneralResponse> removePrescriptions(@Header(Constants.HEADER_AUTH) String authCode,
                                              @Header(Constants.HEADER_CONTENT_TYPE) String content_type,
                                              @Body CartParams cartParams);

    //----------------------------------------------------------------------------------------------
    // Send Audio Note

    @Multipart
    @POST("cart/audioNote")
    Call<GeneralResponse> sendAudioNote(@Header(Constants.HEADER_AUTH) String authCode,
                                        @Part MultipartBody.Part audioNote);

    //----------------------------------------------------------------------------------------------
    // Checkout Call

    @POST("cart/checkout")
    Call<GeneralResponse> checkoutCall(@Header(Constants.HEADER_AUTH) String authCode,
                                       @Header(Constants.HEADER_CONTENT_TYPE) String content_type,
                                       @Body CheckoutParams checkoutParams);

    //----------------------------------------------------------------------------------------------
    // Re-Order Call

    @POST("cart/re-order")
    Call<GeneralResponse> reOrderCall(@Header(Constants.HEADER_AUTH) String authCode,
                                      @Header(Constants.HEADER_CONTENT_TYPE) String content_type,
                                      @Body CheckoutParams checkoutParams);

    //----------------------------------------------------------------------------------------------
    // Get Prescriptions

    @GET("cart/attachments-order/{orderId}")
    Call<GeneralResponse> getAttachments(@Path("orderId") String deviceId);

//    ----------------------------------------------------------------------------------------------
//     Change Order Status Call

    @POST("orders/change-status")
    Call<GeneralResponse> changeOrderStatus(@Header(Constants.HEADER_AUTH) String authCode,
                                            @Header(Constants.HEADER_CONTENT_TYPE) String content_type,
                                            @Body CancellationNote cancellationNote);

    //----------------------------------------------------------------------------------------------
    // Orders Details Call

    @POST("orders/get/order")
    Call<GeneralResponse> getOrderDetails(@Header(Constants.HEADER_AUTH) String authCode,
                                          @Header(Constants.HEADER_CONTENT_TYPE) String content_type,
                                          @Body Params orderId);

    //----------------------------------------------------------------------------------------------
    // User's Orders List Call

    @POST("orders/users")
    Call<GeneralResponse> getUserOrders(@Header(Constants.HEADER_AUTH) String authCode,
                                        @Header(Constants.HEADER_CONTENT_TYPE) String content_type,
                                        @Body Params params);

    //----------------------------------------------------------------------------------------------
    // Search Nearby Pharmacies

    @POST("pharmacy/get")
    Call<GeneralResponse> searchNearbyPharmacies(@Header(Constants.HEADER_AUTH) String authCode,
                                                 @Header(Constants.HEADER_CONTENT_TYPE) String content_type,
                                                 @Body MapParams mapParams);

    //----------------------------------------------------------------------------------------------
    // Get Nearby Pharmacies

    @POST("maps")
    Call<GeneralResponse> getNearbyPharmacies(@Header(Constants.HEADER_AUTH) String authCode,
                                              @Header(Constants.HEADER_CONTENT_TYPE) String content_type,
                                              @Body MapParams mapParams);

    //----------------------------------------------------------------------------------------------
    // User Logout Call

    @Multipart
    @POST("users/signout")
    Call<GeneralResponse> logoutUserCall(@Header(Constants.HEADER_AUTH) String authCode,
                                         @Part("fcmToken") RequestBody email,
                                         @Part("deviceId") RequestBody deviceId);

    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
}