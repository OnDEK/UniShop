package com.unishop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Authenticator;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unishop.models.ApiEndpointInterface;
import com.unishop.models.Authentication;
import com.unishop.models.ErrorResponse;
import com.unishop.models.Login;
import com.unishop.models.LoginResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    EditText userEmail, userPassword;
    public static final String BASE_URL = "http://168.61.54.234/api/v1/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("com.unishop.preference_file", Context.MODE_PRIVATE);
        String token = sharedPref.getString("session_token", "");

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        final ApiEndpointInterface apiService =
                retrofit.create(ApiEndpointInterface.class);
        Authentication auth = new Authentication(token);
        Call<ResponseBody> call = apiService.testauth(auth);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                int statusCode = response.code();
                if(statusCode == 200) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    LoginActivity.this.startActivity(intent);
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Session expired. Please log in again").setNegativeButton("Okay", null).create().show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        View.OnFocusChangeListener hideKeyboardListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        };

        userEmail = (EditText) findViewById(R.id.emailEditText);
        userPassword = (EditText) findViewById((R.id.passwordEditText));

        userEmail.setOnFocusChangeListener(hideKeyboardListener);
        userPassword.setOnFocusChangeListener(hideKeyboardListener);

        boolean TEST_MODE = true;
        if(TEST_MODE) {
            userEmail.setText("test@knights.ucf.edu");
            userPassword.setText("HardPa$$word1");
        }

    }

    @Override
    protected void onRestart() {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("com.unishop.preference_file", Context.MODE_PRIVATE);

        String token = sharedPref.getString("session_token", "");

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        final ApiEndpointInterface apiService =
                retrofit.create(ApiEndpointInterface.class);
        Authentication auth = new Authentication(token);
        Call<ResponseBody> call = apiService.testauth(auth);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                int statusCode = response.code();

                if(statusCode == 200){
                    Intent i = new Intent(Intent.ACTION_MAIN);
                    i.addCategory(Intent.CATEGORY_HOME);
                    startActivity(i);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void handleLogin(View v) {

        final ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "",
                "Authenticating...", true);
        /**
         * This is where logging in will be handled
         */
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        /*Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String success = jsonResponse.getString("session_token");

                    if(success.length()>0) {
                        /**
                         * if login succeeds you can request initial account information here

                        String fname = jsonResponse.getString("firstname");
                        String lname = jsonResponse.getString("lastname");
                         intent.putExtra("firstname", fname);
                         intent.putExtra("lastname", lname);

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);


                        LoginActivity.this.startActivity(intent);


                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage("Login Failed").setNegativeButton("Retry", null).create().show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };*
        LoginRequest loginRequest = new LoginRequest(email,password, responseListener);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(loginRequest);
        */
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        final ApiEndpointInterface apiService =
                retrofit.create(ApiEndpointInterface.class);
        Login login = new Login(email, password);
        Call<LoginResponse> call = apiService.login(login);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                int statusCode = response.code();
                if(statusCode == 200) {
                    LoginResponse res = response.body();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    dialog.cancel();
                    LoginActivity.this.startActivity(intent);

                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("com.unishop.preference_file", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("session_token", res.getSessionToken());
                    editor.commit();
                }
                else{
                    Gson gson = new GsonBuilder().create();
                    ErrorResponse error = new ErrorResponse();
                    try {
                        error = gson.fromJson(response.errorBody().string(), ErrorResponse.class);

                    }catch (IOException e) {}
                    dialog.cancel();
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("error " + error.getCode() + ": " + error.getMessage()).setNegativeButton("Okay", null).create().show();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                dialog.cancel();
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("Unable to process your request.\nPlease try again later.").setNegativeButton("Okay", null).create().show();

            }
        });
    }

    public void handleRegisterButton(View v) {

        /**
         * This is where logging in will be handled
         */
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        return;
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
