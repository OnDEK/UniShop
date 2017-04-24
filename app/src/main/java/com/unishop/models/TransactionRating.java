package com.unishop.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kaosp on 4/23/17.
 */

public class TransactionRating {

    @Expose
    @SerializedName("rating")
    private Integer rating;

    @Expose
    @SerializedName("transaction_id")
    private String transactionId;

    public TransactionRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getRating() {
        return this.rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
