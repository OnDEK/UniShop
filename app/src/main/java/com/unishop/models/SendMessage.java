package com.unishop.models;

/**
 * Created by Daniel on 4/20/17.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendMessage {

    @SerializedName("text")
    @Expose
    private String message;

    public SendMessage(String message) {
        this.message = message;
    }

    public String getFromBuyer() {
        return message;
    }

    public void setFromBuyer(String message) {
        this.message = message;
    }
}
