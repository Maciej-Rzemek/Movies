package com.example.movies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.movies.adapters.MoviesRecyclerAdapter;
import com.example.movies.adapters.OnMovieListener;
import com.example.movies.models.Movie;
import com.example.movies.repositories.MovieRepository;
import com.example.movies.requests.MovieApi;
import com.example.movies.viewmodels.MovieListViewModel;

import java.util.List;


public class MovieListActivity extends BaseActivity implements OnMovieListener {
    private static final String TAG = "MovieListActivity";
    private MovieListViewModel mMovieListViewModel;
    private String sortBy = MovieRepository.POPULAR;
    private RecyclerView mRecyclerView;
    private MoviesRecyclerAdapter mAdapter;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        mRecyclerView = findViewById(R.id.movie_list);

        mMovieListViewModel = ViewModelProviders.of(this).get(MovieListViewModel.class);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        initRecyclerView();
        subscribeObservers();
        initSearchView();
    }

    private void initRecyclerView() {
        mAdapter = new MoviesRecyclerAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!mRecyclerView.canScrollVertically(1)) {
                    // search next page
                    mMovieListViewModel.searchNextPage();
                }
            }
        });
    }

    private void initSearchView() {
        final SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mAdapter.displayLoading();
                mMovieListViewModel.searchMoviesApi(query, 1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void subscribeObservers() {
        mMovieListViewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                if (movies != null) {
                    if(mMovieListViewModel.isViewingMovies()) {
                        mMovieListViewModel.setPerformingQuery(false);
                        mAdapter.setMovies(movies);
                        Log.d(TAG, "onChanged: I took searched movies from subscribeObservers()");

                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sort, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.sort_menu:
                showSortMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSortMenu() {
        PopupMenu sortMenu = new PopupMenu(this, findViewById(R.id.sort_menu));
        sortMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                currentPage = 1;

                switch (item.getItemId()) {
                    case R.id.popular:
                        mMovieListViewModel.searchPopularMoviesApi(1);
                        mAdapter.displayLoading();
                        return true;
                    case R.id.top_rated:
                        mMovieListViewModel.searchTopRatedMovies(1);
                        mAdapter.displayLoading();
                        return true;
                    case R.id.upcoming:
                        mMovieListViewModel.searchUpcomingMoviesApi(1);
                        mAdapter.displayLoading();
                        return true;
                    default:
                        return false;
                }
            }
        });
        sortMenu.inflate(R.menu.menu_categories_sort);
        sortMenu.show();
    }

    @Override
    public void OnMovieClick(int position) {
        Intent intent = new Intent(this, MovieActivity.class);
        intent.putExtra("movie", mAdapter.getSelectedMovie(position));
        startActivity(intent);
    }

    @Override
    public void OnGenreClick(String genre) {

    }

    @Override
    public void onBackPressed() {
        if (mMovieListViewModel.onBackedPressed()) {
            super.onBackPressed();
        }
    }

    private void showError() {
        Toast.makeText(MovieListActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
    }

}
