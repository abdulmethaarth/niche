package com.affinitity.niche.ui.categprylist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryResponse {
//    @SerializedName("message")
//    @Expose
//    private String message;
//    @SerializedName("results")
//    @Expose


    private boolean  error;
    private  CategoryListModel[] response;

    public CategoryListModel[] getResponse() {
        return response;
    }

    public boolean isError() {
        return error;
    }
}
