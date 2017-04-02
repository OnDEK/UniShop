package com.unishop.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendOffer {

    @SerializedName("amount")
    @Expose
    private Integer amount;

    public SendOffer(Integer amount) {
        super();
        this.amount = amount;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

}