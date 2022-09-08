package com.affinitity.niche.ui.videoportion;

import com.google.gson.annotations.SerializedName;

public class VideoModel {
    @SerializedName("status")
    private boolean  status;
    @SerializedName("videoID")
    private String videoID;
    @SerializedName("homeID")
    private String homeID;
    @SerializedName("videoDESC")
    private String videoDESC;
    @SerializedName("videoFILE")
    private String videoFILE;


    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public String getHomeID() {
        return homeID;
    }

    public void setHomeID(String homeID) {
        this.homeID = homeID;
    }

    public String getVideoDESC() {
        return videoDESC;
    }

    public void setVideoDESC(String videoDESC) {
        this.videoDESC = videoDESC;
    }

    public String getVideoFILE() {
        return videoFILE;
    }

    public void setVideoFILE(String videoFILE) {
        this.videoFILE = videoFILE;
    }
}
