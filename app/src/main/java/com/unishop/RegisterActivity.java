package com.unishop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

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

        final Button registerButton = (Button) findViewById(R.id.sendRegisterButton);
        registerEmailEditText.setOnFocusChangeListener(hideKeyboardListener);
        registerPasswordEditText.setOnFocusChangeListener(hideKeyboardListener);
        confirmPasswordEditText.setOnFocusChangeListener(hideKeyboardListener);
        firstnameEditText.setOnFocusChangeListener(hideKeyboardListener);
        lastnameEditText.setOnFocusChangeListener(hideKeyboardListener);

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
