package com.example.movies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.movies.adapters.MoviesRecyclerAdapter;
import com.example.movies.adapters.OnMovieListener;
import com.example.movies.models.Movie;
import com.example.movies.requests.MovieApi;
import com.example.movies.requests.ServiceGenerator;
import com.example.movies.requests.responses.MovieResponse;
import com.example.movies.utils.Constants;
import com.example.movies.viewmodels.MovieListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends BaseActivity implements OnMovieListener {
    private static final String TAG = "MovieListActivity";
    private MovieListViewModel mMovieListViewModel;
    private RecyclerView mRecyclerView;
    private MoviesRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        mRecyclerView = findViewById(R.id.movie_list);

        mMovieListViewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);

        initRecyclerView();
        subscribeObservers();
        testRetrofitRequest();
    }

    private void initRecyclerView() {
        mAdapter = new MoviesRecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void subscribeObservers() {
        mMovieListViewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {

                if (movies != null) {
                    for (Movie movie : movies) {
                        Log.d(TAG, "onChanged: " + movie.getOriginalTitle());
                        mAdapter.setMovies(movies);
                    }
                }
            }
        });
    }

    private void searchMoviesApi(String query, int pageNumber) {
        mMovieListViewModel.searchMoviesApi(query, pageNumber);
    }

    private void testRetrofitRequest() {
        searchMoviesApi("Star Wars", 1);
    }

    @Override
    public void OnMovieClick(int position) {

    }

    @Override
    public void OnGenreClick(String genre) {

    }
}
