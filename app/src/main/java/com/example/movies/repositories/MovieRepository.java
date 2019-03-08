package com.example.movies.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.movies.models.Movie;
import com.example.movies.requests.MovieApiClient;

import java.util.List;

public class MovieRepository {

    private static MovieRepository instance;
    private MovieApiClient mMovieApiClient;

    public static MovieRepository getInstance() {
        if (instance == null) {
            instance = new MovieRepository();
        }
        return instance;
    }

    private MovieRepository() {
        mMovieApiClient = MovieApiClient.getInstance();

    }

    public LiveData<List<Movie>> getMovies() {
        return mMovieApiClient.getMovies();
    }

    public void searchMoviesApi(String query, int pageNumber) {
        mMovieApiClient.searchMoviesApi(query, pageNumber);
    }
}
