package com.example.movies;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import com.example.movies.requests.OnGetMoviesCallback;
import com.example.movies.requests.ServiceGenerator;
import com.example.movies.requests.responses.MovieResponse;
import com.example.movies.requests.responses.MovieSearchResponse;
import com.example.movies.utils.Constants;
import com.example.movies.viewmodels.MovieListViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.movies.requests.MovieApiClient.POPULAR;
import static com.example.movies.requests.MovieApiClient.TOP_RATED;
import static com.example.movies.requests.MovieApiClient.UPCOMING;
import static com.example.movies.utils.Constants.LANGUAGE;


public class MovieListActivity extends BaseActivity implements OnMovieListener {
    private static final String TAG = "MovieListActivity";
    private MovieListViewModel mMovieListViewModel;
    private String sortBy = MovieRepository.POPULAR;
    private RecyclerView mRecyclerView;
    private MoviesRecyclerAdapter mAdapter;
    private MutableLiveData<List<Movie>> mMovies;
    private boolean isFetchingMovies;

    OnMovieListener mOnMovieListener;
    MovieRepository mMovieRepository;
    MovieApi api;
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
                    for (Movie movie : movies) {
                        Log.d(TAG, "onChanged: " + movie.getOriginalTitle());
                        mAdapter.setMovies(movies);
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
                        loadPopularMovies();
                        return true;
                    case R.id.top_rated:
                        loadTopRatedMovies();
                        return true;
                    case R.id.upcoming:
                        loadUpcomingMovies();
                        return true;
                    default:
                        return false;
                }
            }
        });

        sortMenu.inflate(R.menu.menu_categories_sort);
        sortMenu.show();
    }

    private void loadPopularMovies() {
        final MovieApi movieApi = ServiceGenerator.getMovieApi();
        final Call<MovieResponse> responseCall = movieApi.getPopularMovies(Constants.API_KEY, Constants.LANGUAGE, currentPage);
        responseCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful())  {
                    List<Movie> moviesList = new ArrayList<>(response.body().getMovieList());
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.setMovies(moviesList);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                showError();
            }
        });
    }

    private void loadTopRatedMovies() {
        final MovieApi movieApi = ServiceGenerator.getMovieApi();
        final Call<MovieResponse> responseCall = movieApi.getTopRatedMovies(Constants.API_KEY, Constants.LANGUAGE, currentPage);
        responseCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful())  {
                    List<Movie> moviesList = new ArrayList<>(response.body().getMovieList());
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.setMovies(moviesList);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                showError();
            }
        });
    }

    private void loadUpcomingMovies() {
        final MovieApi movieApi = ServiceGenerator.getMovieApi();
        final Call<MovieResponse> responseCall = movieApi.getUpcomingMovies(Constants.API_KEY, Constants.LANGUAGE, currentPage);
        responseCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful())  {
                    List<Movie> moviesList = new ArrayList<>(response.body().getMovieList());
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.setMovies(moviesList);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                showError();
            }
        });
    }




    /*private void getMovies(final int page) {
        isFetchingMovies = true;
        mMovieRepository.getMovies(page, sortBy, new OnGetMoviesCallback() {
            @Override
            public void onSuccess(int page, List<Movie> movies) {
                Log.d(TAG, "onSuccess: Current Page " + page);
                if (mAdapter == null) {
                    mAdapter = new MoviesRecyclerAdapter(mOnMovieListener);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    if (page == 1) {
                        mAdapter.clearMovies();
                    }
                    mAdapter.appendMovies(movies);
                }
                currentPage = page;
                isFetchingMovies = false;
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }*/

    @Override
    public void OnMovieClick(int position) {

    }

    @Override
    public void OnGenreClick(String genre) {

    }

    private void showError() {
        Toast.makeText(MovieListActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
    }
}
