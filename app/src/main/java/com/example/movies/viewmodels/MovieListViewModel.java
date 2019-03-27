package com.example.movies.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.example.movies.models.Movie;
import com.example.movies.repositories.MovieRepository;


import java.util.List;

public class MovieListViewModel extends ViewModel {

    private MovieRepository mMovieRepository;
    private boolean mIsViewingMovies;
    private boolean mIsPerformingQuery;


    public MovieListViewModel() {
        mMovieRepository = MovieRepository.getInstance();
        mIsPerformingQuery = false;
    }

    public LiveData<List<Movie>> getMovies() {
        return mMovieRepository.getMovies();
    }

    public void searchMoviesApi(String query, int pageNumber) {
        mIsPerformingQuery = true;
        mIsPerformingQuery = true;
        mMovieRepository.searchMoviesApi(query, pageNumber);
    }

    public void searchNextPage() {
        if (!mIsPerformingQuery && mIsViewingMovies) {
            mMovieRepository.searchNextPage();
        }
    }

    public boolean isViewingMovies() {
        return mIsViewingMovies;
    }

    public void setViewingMovies(boolean viewingMovies) {
        mIsViewingMovies = viewingMovies;
    }

    public boolean isPerformingQuery() {
        return mIsPerformingQuery;
    }

    public void setPerformingQuery(boolean performingQuery) {
        mIsPerformingQuery = performingQuery;
    }
}
