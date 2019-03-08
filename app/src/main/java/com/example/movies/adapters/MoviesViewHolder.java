package com.example.movies.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.movies.R;

public class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView title, rating;
    AppCompatImageView image;
    OnMovieListener onMovieListener;

    public MoviesViewHolder(@NonNull View itemView, OnMovieListener onMovieListener) {
        super(itemView);
        this.onMovieListener = onMovieListener;
        title = itemView.findViewById(R.id.movie_title);
        rating = itemView.findViewById(R.id.movie_rating);
        image = itemView.findViewById(R.id.movie_poster);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onMovieListener.OnMovieClick(getAdapterPosition());
    }
}
