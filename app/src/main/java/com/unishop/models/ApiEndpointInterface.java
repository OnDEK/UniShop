package com.unishop.models;

import com.google.gson.annotations.JsonAdapter;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by kaosp on 2/13/17.
 */

public interface ApiEndpointInterface {
    @POST("login")
    Call<Login> login(@Body Login login);

    @POST("register")
    Call<RegisterResponse> register(@Body Register register);
}
