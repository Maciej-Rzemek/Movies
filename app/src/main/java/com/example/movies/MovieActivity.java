package com.example.movies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.movies.models.Movie;
import com.example.movies.models.Trailer;
import com.example.movies.utils.Constants;
import com.example.movies.viewmodels.MovieViewModel;

import java.util.List;

public class MovieActivity extends BaseActivity {
    private static final String TAG = "MovieActivity";

    // UI Components

    private ImageView movieImageview, thumbnailImageview;
    private RatingBar movieRating;
    private TextView titleTextview, descriptionTextview, releaseDateTextView;
    private MovieViewModel mMovieViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        movieImageview = findViewById(R.id.movie_image);
        titleTextview = findViewById(R.id.movie_title);
        movieRating = findViewById(R.id.movie_rank);
        descriptionTextview = findViewById(R.id.movie_description);
        releaseDateTextView = findViewById(R.id.movie_release_date);
        thumbnailImageview = findViewById(R.id.trailer_imageView);

        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        showProgressBar(true);
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
                    if (movie.getId() == mMovieViewModel.getMovieId()) {
                        setMovieProperties(movie);
                        Log.d(TAG, "onChanged: TRAILER" + movie.getOriginalTitle());
                        Log.d(TAG, "onChanged: TRAILER" + movie.getPosterPath());
                    }
                }
            }
        });

        mMovieViewModel.getTrailers().observe(this, new Observer<List<Trailer>>() {
            @Override
            public void onChanged(@Nullable List<Trailer> trailers) {
                if (trailers != null) {

                    for (Trailer trailer : trailers) {
                        trailer.getKey();
                        Log.d(TAG, "onChanged: TRAILER" + trailer.getKey());
                        Log.d(TAG, "onChanged: TRAILER" + trailers.get(550).getKey());
                    }
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
            movieRating.setVisibility(View.VISIBLE);
            movieRating.setRating((float) (movie.getRating() / 2));
            releaseDateTextView.setText(movie.getReleaseDate());
        }
        showProgressBar(false);
    }
}
