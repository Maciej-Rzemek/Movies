package com.example.movies;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.movies.models.Movie;
import com.example.movies.requests.MovieApi;
import com.example.movies.requests.ServiceGenerator;
import com.example.movies.requests.responses.GetMoviesCallback;
import com.example.movies.requests.responses.MovieResponse;
import com.example.movies.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends BaseActivity {
    private static final String TAG = "MovieListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testRetrofitRequest();
                /*if(mProgressBar.getVisibility() == View.VISIBLE){
                    showProgressBar(false);
                }
                else{
                    showProgressBar(true);
                }*/
            }
        });
    }

    private void testRetrofitRequest() {
        MovieApi movieApi = ServiceGenerator.getMovieApi();
        Call<MovieResponse> responseCall = movieApi.getPopularMovies(Constants.API_KEY, Constants.LANGUAGE);

        responseCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                Log.d(TAG, "onResponse: server response:" + response.toString());

                if (response.code() == 200) {
                    List<Movie> movies = new ArrayList<>(response.body().getMovieList());
                    for (Movie movie : movies) {
                        Log.d(TAG, "onResponse: " + movie.getOriginalTitle());
                    }
                }
                else {
                    try {
                        Log.d(TAG, "onResponse: " + response.errorBody().string() );
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }
}
