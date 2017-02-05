package com.unishop;


import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel on 11/5/16.
 */

public class LoginRequest extends StringRequest {

    private static final String LOGIN_REQUEST_URL = "http://168.61.54.234/api/v1/login";
   // http://ondektech.com/login.php
    JSONObject body = new JSONObject();
    public LoginRequest(String email, String password, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        try {
            body.put("email", email);
            body.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return body.toString().getBytes();
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
    }
}
