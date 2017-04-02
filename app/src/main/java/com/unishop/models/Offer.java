package com.unishop.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Offer {

    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("buyer_rating")
    @Expose
    private Integer buyerRating;
    @SerializedName("highest_competing_bid")
    @Expose
    private Integer highestCompetingBid;
    @SerializedName("item")
    @Expose
    private Item item;
    @SerializedName("seller_rating")
    @Expose
    private Integer sellerRating;

    /**
     * No args constructor for use in serialization
     *
     */
    public Offer() {
    }

    /**
     *
     * @param sellerRating
     * @param amount
     * @param highestCompetingBid
     * @param buyerRating
     * @param item
     */
    public Offer(Integer amount, Integer buyerRating, Integer highestCompetingBid, Item item, Integer sellerRating) {
        super();
        this.amount = amount;
        this.buyerRating = buyerRating;
        this.highestCompetingBid = highestCompetingBid;
        this.item = item;
        this.sellerRating = sellerRating;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getBuyerRating() {
        return buyerRating;
    }

    public void setBuyerRating(Integer buyerRating) {
        this.buyerRating = buyerRating;
    }

    public Integer getHighestCompetingBid() {
        return highestCompetingBid;
    }

    public void setHighestCompetingBid(Integer highestCompetingBid) {
        this.highestCompetingBid = highestCompetingBid;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getSellerRating() {
        return sellerRating;
    }

    public void setSellerRating(Integer sellerRating) {
        this.sellerRating = sellerRating;
    }

}