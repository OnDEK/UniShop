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

public class RegisterRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "http://168.61.54.234/api/v1/register";

    JSONObject body = new JSONObject();

    public RegisterRequest(String email, String fname, String password, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        try {
            body.put("email", email);
            body.put("name", fname);
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
