package com.kpcode4u.prasanthkumar.moviesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Prasanth kumar on 10/06/2018.
 */

public class ReviewsResponse {
    @SerializedName("id")
    @Expose
    private int id_movie;
    @SerializedName("results")
    @Expose
    private List<Reviews> results;

    public int getId_movie() {
        return id_movie;
    }

    public void setId_movie(int id_movie) {
        this.id_movie = id_movie;
    }

    public List<Reviews> getResults() {
        return results;
    }

    public void setResults(List<Reviews> results) {
        this.results = results;
    }
}
