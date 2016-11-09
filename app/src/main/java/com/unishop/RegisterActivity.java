package com.unishop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import utils.CodeUtils;
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

        Response.Listener<String> responseListener = new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

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
        RegisterRequest registerRequest = new RegisterRequest(email, password, fname, lname, responseListener);
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(registerRequest);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
