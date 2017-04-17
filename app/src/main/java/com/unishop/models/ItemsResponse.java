package com.unishop.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kaosp on 4/16/17.
 */

public class ItemsResponse {

    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("next_href")
    @Expose
    private String nextHref;
    @SerializedName("prev_href")
    @Expose
    private String prevHref;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getNextHref() {
        return nextHref;
    }

    public void setNextHref(String nextHref) {
        this.nextHref = nextHref;
    }

    public String getPrevHref() {
        return prevHref;
    }

    public void setPrevHref(String prevHref) {
        this.prevHref = prevHref;
    }

}

