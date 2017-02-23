package com.unishop.models;

/**
 * Created by Daniel on 2/23/17.
 */


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountItems {

    @SerializedName("items")
    @Expose
    private List<ItemContainer> items = null;

    public List<ItemContainer> getItems() {
        return items;
    }

    public void setItems(List<ItemContainer> items) {
        this.items = items;
    }

}
