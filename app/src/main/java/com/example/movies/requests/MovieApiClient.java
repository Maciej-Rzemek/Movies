package com.example.movies.requests;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.movies.AppExecutors;
import com.example.movies.BuildConfig;
import com.example.movies.models.Movie;
import com.example.movies.repositories.MovieRepository;
import com.example.movies.requests.responses.MovieResponse;
import com.example.movies.requests.responses.MovieSearchResponse;
import com.example.movies.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.example.movies.utils.Constants.LANGUAGE;
import static com.example.movies.utils.Constants.NETWORK_TIMEOUT;

public class MovieApiClient {
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String UPCOMING = "upcoming";

    private static MovieApiClient instance;
    private static MovieApi api;
    private MutableLiveData<List<Movie>> mMovies;
    private MutableLiveData<Movie> mMovie;
    private RetrieveMoviesRunnable mRetrieveMoviesRunnable;

    public static MovieApiClient getInstance() {
        if (instance == null) {
            instance = new MovieApiClient();
        }
        return instance;
    }

    private MovieApiClient() {
        mMovie = new MutableLiveData<>();
        mMovies = new MutableLiveData<>();
    }

    public LiveData<List<Movie>> getMovies() {
        return mMovies;
    }

    public void searchMoviesApi(String query, int pageNumber) {

        if (mRetrieveMoviesRunnable != null) {
            mRetrieveMoviesRunnable = null;
        }
        mRetrieveMoviesRunnable = new RetrieveMoviesRunnable(query, pageNumber);

        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetrieveMoviesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // interrupting background thread if time has passed
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    private class RetrieveMoviesRunnable implements Runnable {

        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getMovies(query, pageNumber).execute();
                if (cancelRequest) {
                    return;
                }
                if (response.code() == 200) {
                    List<Movie> list = new ArrayList<>(((MovieSearchResponse) response.body()).getResults());
                    if (pageNumber == 1) {
                        mMovies.postValue(list);
                    } else {
                        List<Movie> currentMovies = mMovies.getValue();
                        currentMovies.addAll(list);
                        mMovies.postValue(currentMovies);
                    }
                } else {
                    String error = response.errorBody().string();
                    Log.d(TAG, "run: " + error);
                    mMovies.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mMovies.postValue(null);
            }


        }

        private Call<MovieSearchResponse> getMovies(String query, int pageNumber) {
            return ServiceGenerator.getMovieApi().getSearchedMovie(Constants.API_KEY, query, pageNumber);
        }

        private void cancelRequest() {
            Log.d(TAG, "cancelRequest: canceling search request");
        }
    }

    private class RetrieveMovieRunnable implements Runnable {

        private int movieId;
        boolean cancelRequest;

        public RetrieveMovieRunnable(int movieId) {
            this.movieId = movieId;
        }

        @Override
        public void run() {
            try {
                Response response = getMovie(movieId).execute();
                if(cancelRequest) {
                    return;
                }
                if (response.code() == 200) {
                    Movie movie = ((MovieResponse)response.body().getMovie());
                    if(pageNumber == 1) {
                        mMovies.postValue(list);
                    }
                    else {
                        List<Movie> currentMovies = mMovies.getValue();
                        currentMovies.addAll(list);
                        mMovies.postValue(currentMovies);
                    }
                }
                else {
                    String error = response.errorBody().string();
                    Log.d(TAG, "run: " + error);
                    mMovies.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mMovies.postValue(null);
            }


        }

        private Call<MovieResponse> getMovie(int movieId) {
            return ServiceGenerator.getMovieApi().getMovieDetails(movieId, Constants.API_KEY);
        }

        private void cancelRequest() {
            Log.d(TAG, "cancelRequest: canceling search request" );
        }
    }
}




