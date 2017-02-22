package com.unishop.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kaosp on 2/22/17.
 */

public class Logout {

    @SerializedName("session_token")
    @Expose
    private String sessionToken;

    public Logout(String token) {
        this.sessionToken = token;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}
