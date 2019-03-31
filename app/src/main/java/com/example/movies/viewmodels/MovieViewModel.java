package com.example.movies.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.movies.models.Movie;
import com.example.movies.repositories.MovieRepository;

public class MovieViewModel extends ViewModel {

    private MovieRepository mMovieRepository;

    public MovieViewModel() {
        mMovieRepository = MovieRepository.getInstance();
    }

    public LiveData<Movie> getMovie() {
        return mMovieRepository.getMovie();
    }

    public void searchMovieById(int movieId) {
        mMovieRepository.searchMovieById(movieId);
    }
}
