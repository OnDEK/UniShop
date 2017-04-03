package com.unishop.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unishop.models.ApiEndpointInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Daniel on 2/22/17.
 */

public class NetworkUtils {

    public static final String BASE_URL = "https://unishop.shop/api/v1/";

    public static final String BASE_URL_FRONTEND = " https://unishop.shop/";

    public static ApiEndpointInterface getFrontApiService() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_FRONTEND)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ApiEndpointInterface apiService =
                retrofit.create(ApiEndpointInterface.class);

        return apiService;
    }

    public static ApiEndpointInterface getApiService() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ApiEndpointInterface apiService =
                retrofit.create(ApiEndpointInterface.class);

        return apiService;
    }

    public static String getSessionToken(Context context) {
        final SharedPreferences sharedPref = context.getSharedPreferences("com.unishop.preference_file", Context.MODE_PRIVATE);
        String sessionToken = sharedPref.getString("session_token", "");
        String bearer = new String("Bearer");
        sessionToken = bearer.concat(" " + sessionToken);
        return sessionToken;
    }

}
