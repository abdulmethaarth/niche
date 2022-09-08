package com.affinitity.niche.ui.mainhome;

import com.google.gson.annotations.SerializedName;

public class ResultsResponse{

    @SerializedName("homeID")
    public String homeID;

    @SerializedName("homeData")
    public String homeData;

    @SerializedName("visible")
    public boolean visible;

    public String getHomeID() {
        return homeID;
    }

    public void setHomeID(String homeID) {
        this.homeID = homeID;
    }

    public String getHomeData() {
        return homeData;
    }

    public void setHomeData(String homeData) {
        this.homeData = homeData;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
