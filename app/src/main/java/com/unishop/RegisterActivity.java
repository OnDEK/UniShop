package com.unishop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unishop.models.ApiEndpointInterface;
import com.unishop.models.ErrorResponse;
import com.unishop.models.Login;
import com.unishop.models.Register;
import com.unishop.models.RegisterResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import utils.CodeUtils;
/**
 * Created by Daniel on 10/30/16.
 */

public class RegisterActivity extends Activity {

    public static final String BASE_URL = "http://168.61.54.234/api/v1/";
    EditText registerEmailEditText;
    EditText registerPasswordEditText;
    EditText confirmPasswordEditText;
    EditText firstnameEditText;
    EditText lastnameEditText;
    ImageView registerEmailImage;
    ImageView registerPasswordImage;
    ImageView registerPasswordConfirmImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        View.OnFocusChangeListener hideKeyboardListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        };


        registerEmailEditText = (EditText) findViewById(R.id.registerEmailEditText);
        registerPasswordEditText = (EditText) findViewById(R.id.registerPasswordEditText);
        confirmPasswordEditText = (EditText) findViewById(R.id.registerPasswordConfirmEditText);
        firstnameEditText = (EditText) findViewById(R.id.firstnameEditText);
        lastnameEditText = (EditText) findViewById(R.id.lastnameEditText);
        registerEmailImage = (ImageView) findViewById(R.id.registerEmailImage);
        registerPasswordImage = (ImageView) findViewById(R.id.registerPasswordImage);
        registerPasswordConfirmImage = (ImageView) findViewById(R.id.registerConfirmPasswordImage);



        final Button registerButton = (Button) findViewById(R.id.sendRegisterButton);
        registerEmailEditText.setOnFocusChangeListener(hideKeyboardListener);
        registerPasswordEditText.setOnFocusChangeListener(hideKeyboardListener);
        confirmPasswordEditText.setOnFocusChangeListener(hideKeyboardListener);
        firstnameEditText.setOnFocusChangeListener(hideKeyboardListener);
        lastnameEditText.setOnFocusChangeListener(hideKeyboardListener);

        registerEmailEditText.setText("test@knights.ucf.edu");
        registerPasswordEditText.setText("HardPa$$word1");
        confirmPasswordEditText.setText("HardPa$$word1");
        firstnameEditText.setText("John");
        lastnameEditText.setText("Smith");

        registerEmailEditText.addTextChangedListener(new TextWatcher() {
            String currentString;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentString = registerEmailEditText.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(currentString.contains("@knights.ucf.edu")) {
                    registerEmailImage.setImageResource(R.drawable.checkmark);
                }else {
                    registerEmailImage.setImageResource(R.drawable.exclamation_red);
                }
            }
        });

        confirmPasswordEditText.addTextChangedListener(new TextWatcher() {
            String currentString;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentString = confirmPasswordEditText.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(currentString.equals(registerPasswordEditText.getText().toString()) && currentString.length() >= 7) {
                    registerPasswordConfirmImage.setImageResource(R.drawable.checkmark);
                }else {
                    registerPasswordConfirmImage.setImageResource(R.drawable.exclamation_red);
                }
            }
        });

        registerPasswordEditText.addTextChangedListener(new TextWatcher() {
            String currentString;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentString = registerPasswordEditText.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

                boolean hasUppercase = !currentString.equals(currentString.toLowerCase());
                boolean hasLowercase = !currentString.equals(currentString.toUpperCase());
                boolean isAtLeast8   = currentString.length() >= 7;
                boolean hasNumber   = currentString.matches(".*\\d+.*");
                if(hasUppercase && hasLowercase && isAtLeast8 && hasNumber) {
                    registerPasswordImage.setImageResource(R.drawable.checkmark);
                }else {
                    registerPasswordImage.setImageResource(R.drawable.exclamation_red);
                }
            }
        });
    }

    public void handleRegisterRequest(View v) {

        final ProgressDialog dialog = ProgressDialog.show(RegisterActivity.this, "",
                "Registering...", true);
        final String email = registerEmailEditText.getText().toString();
        final String password = registerPasswordEditText.getText().toString();
        final String confirmPassword = confirmPasswordEditText.getText().toString();
        final String fname = firstnameEditText.getText().toString();
        final String lname = lastnameEditText.getText().toString();

        if(!password.equals(confirmPassword)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setMessage("Passwords Do Not Match").setNegativeButton("Retry", null).create().show();
            return;
        }
        if(!email.contains(".edu")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setMessage("Invalid Email Address (.edu Domain Required)").setNegativeButton("Retry", null).create().show();
            return;
        }

        /*Response.Listener<String> responseListener = new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = (response.toString().contains("success"));

                    if(success) {
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        RegisterActivity.this.startActivity(intent);
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("Registration Failed").setNegativeButton("Retry", null).create().show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        fname.concat("" + lname);
        RegisterRequest registerRequest = new RegisterRequest(email, fname, password, responseListener);
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(registerRequest);*/

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ApiEndpointInterface apiService =
                retrofit.create(ApiEndpointInterface.class);
        Register register = new Register(email, password, fname);
        Call<RegisterResponse> call = apiService.register(register);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, retrofit2.Response<RegisterResponse> response) {
                int statusCode = response.code();

                if(statusCode == 200) {
                    dialog.cancel();
                    finish();
                }
                else if(statusCode == 500) {
                    dialog.cancel();

                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("Registration Failed.\nInternal Server Error").setNegativeButton("Okay", null).create().show();
                }
                else{
                    dialog.cancel();
                    Gson gson = new GsonBuilder().create();
                    ErrorResponse error = new ErrorResponse();
                    try {
                        error = gson.fromJson(response.errorBody().string(), ErrorResponse.class);

                    }catch (IOException e) {}

                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage("error " + error.getCode() + ": " + error.getMessage()).setNegativeButton("Okay", null).create().show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                dialog.cancel();
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setMessage("Unable to register.\nPlease check your connection and try again").setNegativeButton("Okay", null).create().show();
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
