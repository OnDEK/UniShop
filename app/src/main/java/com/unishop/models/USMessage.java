package com.unishop.models;

/**
 * Created by Daniel on 4/20/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class USMessage {

    @SerializedName("from_you")
    @Expose
    private Boolean fromYou;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("sent_at")
    @Expose
    private String sentAt;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("transaction_id")
    @Expose
    private Integer transactionId;

    public Boolean getFromYou() {
        return fromYou;
    }

    public void setFromYou(Boolean fromYou) {
        this.fromYou = fromYou;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSentAt() {
        return sentAt;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public boolean equals(Object v) {
        boolean retVal = false;

        if (v instanceof USMessage){
            USMessage ptr = (USMessage) v;
            retVal = ptr.getId() == this.id;
        }

        return retVal;
    }

}
