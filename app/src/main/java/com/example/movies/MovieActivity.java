package com.example.movies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.movies.models.Movie;
import com.example.movies.utils.Constants;
import com.example.movies.viewmodels.MovieViewModel;

public class MovieActivity extends BaseActivity {
    private static final String TAG = "MovieActivity";

    // UI Components

    private ImageView movieImageview;
    private TextView titleTextview, rankTextview, descriptionTextview, releaseDateTextView;
    private MovieViewModel mMovieViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        movieImageview = findViewById(R.id.movie_image);
        titleTextview = findViewById(R.id.movie_title);
        rankTextview = findViewById(R.id.movie_rank);
        descriptionTextview = findViewById(R.id.movie_description);
        releaseDateTextView = findViewById(R.id.movie_release_date);

        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        subscribeObservers();
        getIncomingIntent();
    }

    private void getIncomingIntent() {
        if(getIntent().hasExtra("movie")) {
            Movie movie = getIntent().getParcelableExtra("movie");
            Log.d(TAG, "getIncomingIntent: " + movie.getOriginalTitle());
            mMovieViewModel.searchMovieById(movie.getId());
        }
    }

    private void subscribeObservers() {
        mMovieViewModel.getMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                if (movie != null) {
                    setMovieProperties(movie);
                }
            }
        });
    }

    private void setMovieProperties(Movie movie) {
        if (movie != null) {
            RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_background);
            Glide.with(this)
                    .setDefaultRequestOptions(requestOptions)
                    .load(Constants.IMAGE_BASE_URL + movie.getBackdrop())
                    .into(movieImageview);

            titleTextview.setText(movie.getOriginalTitle());
            descriptionTextview.setText(movie.getDescription());
            rankTextview.setText(String.valueOf(movie.getRating()) + "/10");
            releaseDateTextView.setText(movie.getReleaseDate());
        }
    }
}
