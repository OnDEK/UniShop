package com.unishop.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unishop.models.ApiEndpointInterface;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Daniel on 2/22/17.
 */

public class NetworkUtils {

    public static final String BASE_URL = "https://unishop.shop/api/v1/";

    public static final String BASE_URL_FRONTEND = " https://unishop.shop/";


    // set your desired log level

    public static ApiEndpointInterface getFrontApiService() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // add your other interceptors …

        // add logging as last interceptor
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .connectionSpecs(Collections.singletonList(spec))
                .followRedirects(false)
                .followSslRedirects(false)
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_FRONTEND)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        ApiEndpointInterface apiService =
                retrofit.create(ApiEndpointInterface.class);

        return apiService;
    }

    public static ApiEndpointInterface getApiService() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

         // add your other interceptors …
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .connectionSpecs(Collections.singletonList(spec))
                .followRedirects(false)
                .followSslRedirects(false)
                .build();
        // add logging as last interceptor
         // <-- this is the important line!
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
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
