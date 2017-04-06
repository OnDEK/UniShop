package com.unishop.models;

/**
 * Created by Daniel on 2/23/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("created")
    @Expose
    private String createdDate;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("modified")
    @Expose
    private String modifiedDate;
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
    @SerializedName("image_paths")
    @Expose
    private String imagePaths;

    public Item(Integer categoryId, String createdDate, String description, Integer id, String modifiedDate, Integer price, String title, Integer viewCount) {
        super();
        this.categoryId = categoryId;
        this.createdDate = createdDate;
        this.description = description;
        this.id = id;
        this.modifiedDate = modifiedDate;
        this.price = price;
        this.title = title;
        this.viewCount = viewCount;
    }

    public String getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(String imagePaths) {
        this.imagePaths = imagePaths;
    }
    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
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

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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

