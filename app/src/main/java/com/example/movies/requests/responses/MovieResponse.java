package com.example.movies.requests.responses;

import com.example.movies.models.Movie;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MovieResponse {

    @SerializedName("body")
    @Expose()
    private Movie movie;

    public Movie getMovie() {
        return movie;
    }

    @Override
    public String toString() {
        return "MovieResponse{" +
                "movie=" + movie +
                '}';
    }
}
