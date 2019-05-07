package com.example.movies.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.movies.models.Movie;
import com.example.movies.models.Trailer;
import com.example.movies.repositories.MovieRepository;

import java.util.List;

public class MovieViewModel extends ViewModel {

    private MovieRepository mMovieRepository;
    private int movieId;


    public MovieViewModel() {
        mMovieRepository = MovieRepository.getInstance();
    }

    public LiveData<Movie> getMovie() {
        return mMovieRepository.getMovie();
    }

    public LiveData<List<Trailer>> getTrailers() {
        return mMovieRepository.getTrailers();
    }

    public void searchMovieById(int movieId) {
        this.movieId = movieId;
        mMovieRepository.searchMovieById(movieId);
    }

    public void searchMovieTrailers(int movieId) {
        this.movieId = movieId;
        mMovieRepository.searchMovieTrailers(movieId);

    }

    public int getMovieId() {
        return movieId;
    }

}
