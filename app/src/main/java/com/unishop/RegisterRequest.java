package com.unishop;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel on 11/5/16.
 */

public class RegisterRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL = "http://ondektech.com/register.php";
    private Map<String, String> params;

    public RegisterRequest(String email, String password, String fname, String lname, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("firstname", fname);
        params.put("lastname", lname);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
