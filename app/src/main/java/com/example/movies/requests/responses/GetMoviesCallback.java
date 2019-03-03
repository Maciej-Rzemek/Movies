package com.example.movies.requests.responses;

import com.example.movies.models.Movie;

import java.util.List;

public interface GetMoviesCallback {

    void onSuccess(List<Movie> movies);

    void onError();
}
