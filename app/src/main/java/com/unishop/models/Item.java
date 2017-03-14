package com.unishop.models;

/**
 * Created by Daniel on 2/23/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("category")
    @Expose
    private Category category;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("seller_rating")
    @Expose
    private Integer sellerRating;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("view_count")
    @Expose
    private Integer viewCount;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getSellerRating() {
        return sellerRating;
    }

    public void setSellerRating(Integer sellerRating) {
        this.sellerRating = sellerRating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

}

