package com.kpcode4u.prasanthkumar.moviesapp.api;

import com.kpcode4u.prasanthkumar.moviesapp.model.MoviesResponse;
import com.kpcode4u.prasanthkumar.moviesapp.model.ReviewsResponse;
import com.kpcode4u.prasanthkumar.moviesapp.model.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Prasanth kumar on 25/05/2018.
 */

public interface Service {

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    Call<TrailerResponse> getMovieTrailer(@Path("movie_id") int id, @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/reviews")
    Call<ReviewsResponse> getMovieReviews(@Path("movie_id") int id, @Query("api_key") String apiKey);

}
