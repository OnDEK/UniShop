package com.unishop.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Daniel on 2/17/17.
 */

public class RegisterResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("session_token")
    @Expose
    private String sessionToken;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}
