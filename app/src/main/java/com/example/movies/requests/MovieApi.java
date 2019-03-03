package com.example.movies.requests;

import com.example.movies.requests.responses.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApi {


    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
            @Query("api_key") String key,
            @Query("language") String language
    );
}
