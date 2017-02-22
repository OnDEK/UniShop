package com.unishop.models;

/**
 * Created by Daniel on 2/22/17.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateResponse {

    @SerializedName("item_id")
    @Expose
    private Integer itemId;
    @SerializedName("session_token")
    @Expose
    private String sessionToken;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
}
