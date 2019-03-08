package com.example.movies.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.movies.R;
import com.example.movies.models.Movie;

import java.util.ArrayList;
import java.util.List;

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Movie> mMovies;
    private OnMovieListener mOnMovieListener;

    public MoviesRecyclerAdapter(OnMovieListener mOnMovieListener) {
        this.mOnMovieListener = mOnMovieListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_movie_list_item, viewGroup, false);
        return new MoviesViewHolder(view, mOnMovieListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_background);

        Glide.with(viewHolder.itemView.getContext())
                .setDefaultRequestOptions(requestOptions)
                .load(mMovies.get(i).getPosterPath())
                .into(((MoviesViewHolder)viewHolder).image);

        ((MoviesViewHolder)viewHolder).title.setText(mMovies.get(i).getOriginalTitle());
        ((MoviesViewHolder)viewHolder).rating.setText(String.valueOf(mMovies.get(i).getRating()));
    }

    @Override
    public int getItemCount() {
        if (mMovies != null) {
            return mMovies.size();
        }
        return 0;
    }

    public void setMovies(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }
}
