package com.example.movies.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.movies.models.Movie;
import com.example.movies.repositories.MovieRepository;


import java.util.List;

import static android.support.constraint.Constraints.TAG;

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
        mIsViewingMovies = true;
        mMovieRepository.searchMoviesApi(query, pageNumber);
    }

    public void searchPopularMoviesApi(int pageNumber) {
        mIsPerformingQuery = true;
        mIsViewingMovies = true;
        mMovieRepository.searchPopularMoviesApi(pageNumber);
    }

    public void searchUpcomingMoviesApi(int pageNumber) {
        mIsPerformingQuery = true;
        mIsViewingMovies = true;
        mMovieRepository.searchUpcomingMoviesApi(pageNumber);
    }
    public void searchTopRatedMovies(int pageNumber) {
        mIsPerformingQuery = true;
        mIsViewingMovies = true;
        mMovieRepository.searchTopRatedMoviesApi(pageNumber);
    }

    public void searchNextPage() {
        if (!mIsPerformingQuery) {
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

    public boolean onBackedPressed() {
        if (mIsViewingMovies) {
            mIsViewingMovies = false;
            return false;
        }
        else {
            return true;
        }
    }
}
