package com.example.movies.repositories;

import android.arch.lifecycle.LiveData;


import com.example.movies.models.Movie;
import com.example.movies.requests.MovieApi;
import com.example.movies.requests.MovieApiClient;
import com.example.movies.requests.OnGetMoviesCallback;
import com.example.movies.requests.responses.MovieResponse;
import com.example.movies.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.movies.requests.MovieApiClient.POPULAR;
import static com.example.movies.requests.MovieApiClient.TOP_RATED;
import static com.example.movies.requests.MovieApiClient.UPCOMING;
import static com.example.movies.utils.Constants.LANGUAGE;

public class MovieRepository {
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String UPCOMING = "upcoming";

    private static MovieRepository instance;
    private MovieApiClient mMovieApiClient;
    private MovieApi api;
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


    public void searchMoviesApi(String query, int pageNumber) {
        if (pageNumber == 0) {
            pageNumber = 1;
        }
        mQuery = query;
        mPageNumber = pageNumber;
        mMovieApiClient.searchMoviesApi(query, pageNumber);
    }

    public void searchNextPage() {
        searchMoviesApi(mQuery, mPageNumber + 1);
    }

    public void getMovies(int page, String sortBy, final OnGetMoviesCallback callback) {
        Callback<MovieResponse> call = new Callback<MovieResponse>() {

            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    MovieResponse moviesResponse = response.body();
                    if (moviesResponse != null && moviesResponse.getMovieList() != null) {
                        callback.onSuccess(moviesResponse.getPage(), moviesResponse.getMovieList());
                    } else {
                        callback.onError();
                    }
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                callback.onError();
            }
        };

        switch (sortBy) {
            case TOP_RATED:
                api.getTopRatedMovies(Constants.API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
            case UPCOMING:
                api.getUpcomingMovies(Constants.API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
            case POPULAR:
            default:
                api.getPopularMovies(Constants.API_KEY, LANGUAGE, page)
                        .enqueue(call);
                break;
        }
    }
}
