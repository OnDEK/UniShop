package com.unishop.models;

/**
 * Created by Daniel on 2/23/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemContainer {

    @SerializedName("item")
    @Expose
    private Item item;
    @SerializedName("item_id")
    @Expose
    private Integer itemId;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

}
