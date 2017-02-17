package com.unishop.models;
 import java.util.HashMap;
 import java.util.Map;

public class Login {

    private String sessionToken;
    private String message;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}