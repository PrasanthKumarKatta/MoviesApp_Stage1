package com.kpcode4u.prasanthkumar.moviesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Prasanth kumar on 25/05/2018.
 */

public class MoviesResponse {

    @SerializedName("page")
    @Expose
    private String page;
    @SerializedName("total_results")
    @Expose
    private String total_Results;
    @SerializedName("total_pages")
    @Expose
    private String total_pages;
    @SerializedName("results")
    @Expose
    private List<Movie> results;

    public MoviesResponse(String page, String total_Results, String total_pages, List<Movie> results) {
        this.page = page;
        this.total_Results = total_Results;
        this.total_pages = total_pages;
        this.results = results;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTotal_Results() {
        return total_Results;
    }

    public void setTotal_Results(String total_Results) {
        this.total_Results = total_Results;
    }

    public String getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(String total_pages) {
        this.total_pages = total_pages;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }
}
