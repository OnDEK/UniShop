package com.unishop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unishop.models.ApiEndpointInterface;
import com.unishop.models.ErrorResponse;
import com.unishop.models.Register;
import com.unishop.models.RegisterResponse;
import com.unishop.utils.NetworkUtils;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Daniel on 10/30/16.
 */

public class RegisterActivity extends Activity {

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
/*
#DEBUG
        registerEmailEditText.setText("dkinney@unishop.shop");
        registerPasswordEditText.setText("unishop");
        confirmPasswordEditText.setText("unishop");
        firstnameEditText.setText("John");
        lastnameEditText.setText("Smith");
*/
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
                if(currentString.contains("@unishop.shop")) {
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
/*
#DEBUG
 */
                boolean hasUppercase = !currentString.equals(currentString.toLowerCase());
                boolean hasLowercase = !currentString.equals(currentString.toUpperCase());
                boolean isAtLeast8   = currentString.length() >= 7;
                boolean hasNumber   = currentString.matches(".*\\d+.*");
                if(isAtLeast8) {
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
            dialog.cancel();
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setMessage("Passwords Do Not Match").setNegativeButton("Retry", null).create().show();
            return;
        }
        /*if(!email.contains(".edu")) {
            dialog.cancel();
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setMessage("Invalid Email Address (.edu Domain Required)").setNegativeButton("Retry", null).create().show();
            return;
        }*/

        ApiEndpointInterface apiService = NetworkUtils.getApiService();

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
