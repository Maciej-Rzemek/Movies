package com.example.movies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.movies.models.Movie;

public class MovieActivity extends BaseActivity {
    private static final String TAG = "MovieActivity";

    // UI Components

    private ImageView movieImageview;
    private TextView titleTextview, rankTextview, descriptionTextview;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        movieImageview = findViewById(R.id.movie_image);
        titleTextview = findViewById(R.id.movie_title);
        rankTextview = findViewById(R.id.movie_rank);
        descriptionTextview = findViewById(R.id.movie_description);

        getIncomingIntent();
    }

    private void getIncomingIntent() {
        if(getIntent().hasExtra("movie")) {
            Movie movie = getIntent().getParcelableExtra("movie");
            Log.d(TAG, "getIncomingIntent: " + movie.getOriginalTitle());
        }
    }
}
