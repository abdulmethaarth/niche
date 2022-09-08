package com.affinitity.niche.ui.mainhome;

import com.google.gson.annotations.SerializedName;

public class BaseResponses {

    @SerializedName("status")
    public Boolean status;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
