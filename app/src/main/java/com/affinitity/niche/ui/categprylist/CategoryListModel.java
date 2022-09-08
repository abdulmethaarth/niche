package com.affinitity.niche.ui.categprylist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryListModel {
    @SerializedName("catID")
    @Expose
    private   String catID;
    @SerializedName("categoryName")
    @Expose
    private   String categoryName;
    @SerializedName("videoID")
    @Expose
    private   String videoID;
    @SerializedName("videoName")
    @Expose
    private   String videoName;
    @SerializedName("videoPath")
    @Expose
    private   String videoPath;
    @SerializedName("imageUrl")
    @Expose
    private    String imageUrl;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("type")
    @Expose
    private    String type;

    public CategoryListModel(String videoID, String videoName, String videoPath, String imageUrl,String description,String type) {
        this.videoID = videoID;
        this.videoName = videoName;
        this.videoPath = videoPath;
        this.imageUrl = imageUrl;
        this.description = description;
        this.type = type;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoID() {
        return videoID;
    }

    public String getVideoName() {
        return videoName;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getType() {
        return type;
    }
}
