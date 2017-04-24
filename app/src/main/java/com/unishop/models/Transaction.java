package com.unishop.models;

import android.content.Intent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kaosp on 4/16/17.
 */

public class Transaction {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("item_id")
    @Expose
    private Integer itemId;
    @SerializedName("rating")
    @Expose
    private Intent rating;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Intent getRating() {
        return rating;
    }

    public void setRating(Intent rating) {
        this.rating = rating;
    }

}
