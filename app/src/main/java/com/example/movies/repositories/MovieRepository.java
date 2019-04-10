package com.example.movies.repositories;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.movies.models.Movie;
import com.example.movies.requests.MovieApiClient;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class MovieRepository {
    public static final String POPULAR = "popular";

    private static MovieRepository instance;
    private MovieApiClient mMovieApiClient;
    private String mQuery;
    private int mPageNumber;

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

    public LiveData<Movie> getMovie() {
        return mMovieApiClient.getMovie();
    }

    public void searchMovieById(int movieId) {
        mMovieApiClient.searchMovieById(movieId);
    }

    public void searchMoviesApi(String query, int pageNumber) {
        if (pageNumber == 0) {
            pageNumber = 1;
        }
        mQuery = query;
        mPageNumber = pageNumber;
        mMovieApiClient.searchMoviesApi(query, pageNumber);
    }

    public void searchPopularMoviesApi(int pageNumber) {
        if (pageNumber == 0) {
            pageNumber = 1;
        }
        mPageNumber = pageNumber;
        mMovieApiClient.searchPopularMoviesApi(pageNumber);
    }

    public void searchUpcomingMoviesApi(int pageNumber) {
        if (pageNumber == 0) {
            pageNumber = 1;
        }
        mPageNumber = pageNumber;
        mMovieApiClient.searchUpcomingMoviesApi(pageNumber);
    }

    public void searchTopRatedMoviesApi(int pageNumber) {
        if (pageNumber == 0) {
            pageNumber = 1;
        }
        mPageNumber = pageNumber;
        mMovieApiClient.searchTopRatedMoviesApi(pageNumber);
    }

    public void searchNextPage() {
        searchMoviesApi(mQuery, mPageNumber + 1);
        searchPopularMoviesApi(mPageNumber + 1);
        searchTopRatedMoviesApi(mPageNumber + 1);
        searchUpcomingMoviesApi(mPageNumber + 1);
    }

}
