package com.example.movies.requests;

import com.example.movies.models.Movie;
import com.example.movies.requests.responses.MovieResponse;
import com.example.movies.requests.responses.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {

    // GET POPULAR MOVIES
    @GET("movie/popular")
    Call<MovieSearchResponse> getPopularMovies(
            @Query("api_key") String key,
            @Query("language") String language,
            @Query("page") int page
    );

    // GET TOP RATED MOVIES
    @GET("movie/top_rated")
    Call<MovieSearchResponse> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    // GET UPCOMING MOVIES
    @GET("movie/upcoming")
    Call<MovieSearchResponse> getUpcomingMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    // SEARCH MOVIES
    @GET("search/movie")
    Call<MovieSearchResponse> getSearchedMovie(
            @Query("api_key") String key,
            @Query("query") String query,
            @Query("page") int page
    );

    // GET MOVIE
    @GET("movie/{id}")
    Call<Movie> getMovieDetails(
            @Path("id") int id,
            @Query("api_key") String key
    );

}
