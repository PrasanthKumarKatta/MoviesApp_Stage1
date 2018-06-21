package com.kpcode4u.prasanthkumar.moviesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Prasanth kumar on 07/06/2018.
 */

public class TrailerResponse {

    @SerializedName("id")
    @Expose
    private int id_trailer;
    @SerializedName("results")
    @Expose
    private List<Trailer> results;

    public int getId_trailer() {
        return id_trailer;
    }

    public void setId_trailer(int id_trailer) {
        this.id_trailer = id_trailer;
    }

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }
}
