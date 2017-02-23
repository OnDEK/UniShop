package com.unishop.models;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

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
    Call<AccountItems> accountItems(@Header("Authorization") String token);
}
