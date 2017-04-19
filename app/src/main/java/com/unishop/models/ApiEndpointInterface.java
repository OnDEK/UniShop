package com.unishop.models;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

/**
 * Created by kaosp on 2/13/17.
 */

public interface ApiEndpointInterface {
    @POST("login")
    Call<LoginResponse> login(@Body Login login);

    @POST("register")
    Call<RegisterResponse> register(@Body Register register);

    @POST("item/create")
    Call<CreateResponse> create(@Body Create create, @Header("Authorization") String token);

    @GET("testauth")
    Call<ResponseBody> testauth(@Header("Authorization") String token);

    @POST("logout")
    Call<ResponseBody> logout(@Header("Authorization") String token);

    @GET("account/items")
    Call<ItemsResponse> accountItems(@Header("Authorization") String token);

    @GET("account/follows")
    Call<List<Item>> getFollows(@Header("Authorization") String token);

    @POST("item/{itemid}/destroy")
    Call<ResponseBody> itemDestroy(@Path("itemid") String itemID, @Header("Authorization") String token);

    @POST("item/{itemid}")
    Call<ResponseBody> itemUpdate(@Body ItemUpdate itemUpdate, @Path("itemid") String itemID, @Header("Authorization") String token);

    @GET("items?sort=newest&limit=100")
    Call<ItemsResponse> unownedItems(@Header("Authorization") String token);

    @POST("item/{itemid}/offer")
    Call<ResponseBody> offer(@Body SendOffer sendOffer, @Path("itemid") String itemID, @Header("Authorization") String token);

    @GET("item/{itemid}/offers")
    Call<List<Offer>> getItemOffers(@Path("itemid") String itemID, @Header("Authorization") String token);

    @GET("account/offers")
    Call<List<Offer>> accountOffers(@Header("Authorization") String token);

    @GET("categories")
    Call<List<Category>> getCategories(@Header("Authorization") String token);

    /**
     * initiated: Transaction has been created
     * completed: Transaction has been completed (item was exchanged)
     * cancelled: One or both parties cancelled the transaction
     * all: Show all transactions (default)
     */
    @GET("account/buying?status={status}")
    Call<List<Transaction>> getBuying(@Header("Authorization") String token, @Path("status") String status);

    @GET("account/selling?status={status}")
    Call<List<Transaction>> getSelling(@Header("Authorization") String token, @Path("status") String status);

    @POST("offer/{offer_id}/accept")
    Call<OfferAcceptResponse> acceptOffer(@Header("Authorization") String token, @Path("offer_id")String offerID, @Body Offer offer);

    @POST("item/{item_id}/follow")
    Call<ResponseBody> followItem(@Path("item_id") String itemID, @Header("Authorization") String token);

    @POST("item/{item_id}/unfollow")
    Call<ResponseBody> unfollowItem(@Path("item_id") String itemID, @Header("Authorization") String token);

    @Multipart
    @POST("item/create")
    Call<CreateResponse> createWImage(@Part("title") RequestBody title,
                                    @Part("price") RequestBody price,
                                    @Part("description") RequestBody desc,
                                    @Part("category_id") RequestBody catID,
                                    @Part List<MultipartBody.Part> file,
                                    @Header("Authorization") String token);



}
