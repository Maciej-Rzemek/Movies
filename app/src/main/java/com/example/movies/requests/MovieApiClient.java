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

    private static MovieApiClient instance;
    private MutableLiveData<List<Movie>> mMovies;
    private MutableLiveData<Movie> mMovie;
    private RetrieveMoviesRunnable mRetrieveMoviesRunnable;
    private RetrieveMovieRunnable mRetrieveMovieRunnable;
    private RetrieveUpcomingMoviesRunnable mRetrieveUpcomingMoviesRunnable;
    private RetrieveTopRatedMoviesRunnable mRetrieveTopRatedMoviesRunnable;
    private RetrievePopularMoviesRunnable mRetrievePopularMoviesRunnable;


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

    public LiveData<Movie> getMovie() {
        return mMovie;
    }


    // Searching movies by Query
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

    // Searching movies by ID
    public void searchMovieById(int movieId) {
        if (mRetrieveMovieRunnable != null) {
            mRetrieveMoviesRunnable = null;
        }
        mRetrieveMovieRunnable = new RetrieveMovieRunnable(movieId);

        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetrieveMovieRunnable);
        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    // Searching POPULAR movies
    public void searchPopularMoviesApi(int pageNumber) {

        if (mRetrievePopularMoviesRunnable != null) {
            mRetrievePopularMoviesRunnable = null;
        }
        mRetrievePopularMoviesRunnable = new RetrievePopularMoviesRunnable(pageNumber);

        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetrievePopularMoviesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // interrupting background thread if time has passed
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    // Searching Top Rated movies
    public void searchTopRatedMoviesApi(int pageNumber) {

        if (mRetrieveTopRatedMoviesRunnable != null) {
            mRetrieveTopRatedMoviesRunnable = null;
        }
        mRetrieveTopRatedMoviesRunnable = new RetrieveTopRatedMoviesRunnable(pageNumber);

        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetrieveTopRatedMoviesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // interrupting background thread if time has passed
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    // Searching Upcoming movies
    public void searchUpcomingMoviesApi(int pageNumber) {

        if (mRetrieveUpcomingMoviesRunnable != null) {
            mRetrieveUpcomingMoviesRunnable = null;
        }
        mRetrieveUpcomingMoviesRunnable = new RetrieveUpcomingMoviesRunnable(pageNumber);

        final Future handler = AppExecutors.getInstance().networkIO().submit(mRetrieveUpcomingMoviesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // interrupting background thread if time has passed
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    // Retrieve list of Top Rated movies
    private class RetrieveTopRatedMoviesRunnable implements Runnable {

        private int pageNumber;
        boolean cancelRequest;

        public RetrieveTopRatedMoviesRunnable(int pageNumber) {
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getMovies(pageNumber).execute();
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

        private Call<MovieSearchResponse> getMovies(int pageNumber) {
            Log.d(TAG, "getMovies: I took movies from the MovieApi");
            return ServiceGenerator.getMovieApi().getTopRatedMovies(Constants.API_KEY, pageNumber);
        }

        private void cancelRequest() {
            Log.d(TAG, "cancelRequest: canceling search request");
            if (mRetrieveMoviesRunnable != null) {
                mRetrieveMoviesRunnable.cancelRequest();
            }
        }
    }

    // Retrieve Upcoming Movies
    private class RetrieveUpcomingMoviesRunnable implements Runnable {

        private int pageNumber;
        boolean cancelRequest;

        public RetrieveUpcomingMoviesRunnable(int pageNumber) {
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getMovies(pageNumber).execute();
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

        private Call<MovieSearchResponse> getMovies(int pageNumber) {
            Log.d(TAG, "getMovies: I took movies from the MovieApi");
            return ServiceGenerator.getMovieApi().getUpcomingMovies(Constants.API_KEY, pageNumber);
        }

        private void cancelRequest() {
            Log.d(TAG, "cancelRequest: canceling search request");
            if (mRetrieveMoviesRunnable != null) {
                mRetrieveMoviesRunnable.cancelRequest();
            }
        }
    }

    // Retrieve list of Popular movies
    private class RetrievePopularMoviesRunnable implements Runnable {

        private int pageNumber;
        boolean cancelRequest;

        public RetrievePopularMoviesRunnable(int pageNumber) {
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getMovies(pageNumber).execute();
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

        private Call<MovieSearchResponse> getMovies(int pageNumber) {
            Log.d(TAG, "getMovies: I took movies from the MovieApi");
            return ServiceGenerator.getMovieApi().getPopularMovies(Constants.API_KEY, pageNumber);
        }

        private void cancelRequest() {
            Log.d(TAG, "cancelRequest: canceling search request");
            if (mRetrieveMoviesRunnable != null) {
                mRetrieveMoviesRunnable.cancelRequest();
            }
        }
    }

    // Retrieve list of searched movies
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
            if (mRetrieveMoviesRunnable != null) {
                mRetrieveMoviesRunnable.cancelRequest();
            }
        }
    }

    // Single Movie Retrieve
    private class RetrieveMovieRunnable implements Runnable {

        private int id;
        boolean cancelRequest;

        public RetrieveMovieRunnable(int id) {
            this.id = id;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getMovie(id).execute();
                if(cancelRequest) {
                    return;
                }
                if (response.code() == 200) {
                    Movie movie = ((Movie)response.body());
                    mMovie.postValue(movie);
                }
                else {
                    String error = response.errorBody().string();
                    Log.d(TAG, "run: " + error);
                    mMovie.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mMovie.postValue(null);
            }
        }

        private Call<Movie> getMovie(int movieId) {
            return ServiceGenerator.getMovieApi().getMovieDetails(movieId, Constants.API_KEY);
        }

        private void cancelRequest() {
            Log.d(TAG, "cancelRequest: canceling search request" );
            if (mRetrieveMoviesRunnable != null) {
                mRetrieveMoviesRunnable.cancelRequest();
            }
            if (mRetrieveMovieRunnable != null) {
                mRetrieveMovieRunnable.cancelRequest();
            }
        }
    }
}




