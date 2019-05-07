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
import com.example.movies.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int MOVIE_TYPE = 1;
    private static final int LOADING_TYPE = 2;
    private List<Movie> mMovies;
    private OnMovieListener mOnMovieListener;


    public MoviesRecyclerAdapter(OnMovieListener mOnMovieListener) {
        this.mOnMovieListener = mOnMovieListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = null;

        switch (i) {
            case MOVIE_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_movie_list_item, viewGroup, false);
                return new MoviesViewHolder(view, mOnMovieListener);
            }
            case LOADING_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_loading_list_item, viewGroup, false);
                return new LoadingViewHolder(view);
            }
            default: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_movie_list_item, viewGroup, false);
                return new MoviesViewHolder(view, mOnMovieListener);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        int itemViewType = getItemViewType(i);
        if (itemViewType == MOVIE_TYPE) {

            RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.ic_launcher_background);

            Glide.with(viewHolder.itemView)
                    .load(Constants.IMAGE_BASE_URL + mMovies.get(i).getPosterPath())
                    .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                    .into(((MoviesViewHolder)viewHolder).image);

            ((MoviesViewHolder)viewHolder).title.setText(mMovies.get(i).getOriginalTitle());
            ((MoviesViewHolder)viewHolder).rank.setText(String.valueOf(mMovies.get(i).getRating()));
        }


    }

    @Override
    public int getItemViewType(int position) {
        if (mMovies.get(position).getOriginalTitle().equals("LOADING...")) {
            return LOADING_TYPE;
        } else if (position == mMovies.size() - 1 && position != 0) {
            return LOADING_TYPE;
        } else {
            return MOVIE_TYPE;
        }
    }

    public void displayLoading() {
        if (!isLoading()) {
            Movie movie = new Movie();
            movie.setOriginalTitle("LOADING...");
            List<Movie> loadingList = new ArrayList<>();
            loadingList.add(movie);
            mMovies = loadingList;
            notifyDataSetChanged();
        }
    }

    private boolean isLoading() {
        if (mMovies != null) {
            if (mMovies.size() > 0) {
                if (mMovies.get(mMovies.size() - 1).getOriginalTitle().equals("LOADING..."))
                    return true;
            }
        }
        return false;
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

    public Movie getSelectedMovie(int position) {
        if (mMovies != null) {
            if(mMovies.size() > 0) {
                return mMovies.get(position);
            }
        }
        return null;
    }
}
