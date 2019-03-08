package com.example.movies.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import com.example.movies.models.Movie;
import com.example.movies.repositories.MovieRepository;


import java.util.List;

public class MovieListViewModel extends ViewModel {

    private MovieRepository mMovieRepository;


    public MovieListViewModel() {
        mMovieRepository = MovieRepository.getInstance();
    }

    public LiveData<List<Movie>> getMovies() {
        return mMovieRepository.getMovies();
    }

    public void searchMoviesApi(String query, int pageNumber) {
        mMovieRepository.searchMoviesApi(query, pageNumber);
    }
}
