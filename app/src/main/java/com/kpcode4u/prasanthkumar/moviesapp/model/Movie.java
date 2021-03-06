package com.kpcode4u.prasanthkumar.moviesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Prasanth kumar on 25/05/2018.
 */

public class Movie {
    @SerializedName("original_title")
    @Expose
    private String original_title;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("video")
    @Expose
    private Boolean video;
    @SerializedName("backdrop_path")
    @Expose
    private String backdrop_path;


    public Movie(String original_title, String posterPath, String overview, Double voteAverage, String releaseDate, Integer id,String backdrop_path) {
        this.original_title = original_title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.id = id;
        this.backdrop_path = backdrop_path;
    }

    public Movie() {
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getPosterPath() {
        return "http://image.tmdb.org/t/p/w185" + posterPath;
    }

    public String getOriginalPath(){
        return "http://image.tmdb.org/t/p/original".concat(posterPath);
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getVoteAverage() {
        return voteAverage ;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBackdrop_path() {
        return "https://image.tmdb.org/t/p/w500"+backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }
}
