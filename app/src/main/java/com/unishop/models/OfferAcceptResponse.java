package com.unishop.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kaosp on 4/16/17.
 */

public class OfferAcceptResponse {

    @SerializedName("transaction_id")
    @Expose
    private Integer transactionId;

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }
}
