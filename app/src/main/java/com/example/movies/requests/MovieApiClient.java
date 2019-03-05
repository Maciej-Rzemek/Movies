package com.example.movies.requests;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.movies.AppExecutors;
import com.example.movies.models.Movie;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.example.movies.utils.Constants.NETWORK_TIMEOUT;

public class MovieApiClient {

    private static MovieApiClient instance;
    private MutableLiveData<List<Movie>> mMovies;


    public static MovieApiClient getInstance() {
        if (instance == null) {
            instance = new MovieApiClient();
        }
        return instance;
    }

    private MovieApiClient() {
        mMovies = new MutableLiveData<>();
    }

    public LiveData<List<Movie>> getMovies() {
        return mMovies;
    }

    public void searchMoviesApi() {
        final Future handler = AppExecutors.getInstance().networkIO().submit();

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
            cancleRequest = false;
        }

        @Override
        public void run() {
            if(cancleRequest) {
                return;
            }
        }
    }
}
