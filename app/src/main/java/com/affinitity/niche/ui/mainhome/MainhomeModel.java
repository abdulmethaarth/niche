package com.affinitity.niche.ui.mainhome;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MainhomeModel extends BaseResponses {

    @SerializedName("results")
    private ArrayList<ResultsResponse> results;


    public ArrayList<ResultsResponse> getResults() {
        return results;
    }

    public void setResults(ArrayList<ResultsResponse> results) {
        this.results = results;
    }
}
