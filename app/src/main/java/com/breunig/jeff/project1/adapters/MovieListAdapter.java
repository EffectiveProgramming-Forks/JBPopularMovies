package com.breunig.jeff.project1.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.breunig.jeff.project1.R;
import com.breunig.jeff.project1.models.Movie;

public class MovieListAdapter extends RecyclerView.Adapter<com.breunig.jeff.project1.adapters.MovieListAdapter.MovieListAdapterViewHolder> {

    private Movie[] mMovies;

    private final com.breunig.jeff.project1.adapters.MovieListAdapter.MovieListAdapterOnClickHandler mClickHandler;

    public interface MovieListAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public MovieListAdapter(com.breunig.jeff.project1.adapters.MovieListAdapter.MovieListAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class MovieListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mMovieTextView;

        public MovieListAdapterViewHolder(View view) {
            super(view);
            mMovieTextView = (TextView) view.findViewById(R.id.tv_movie_data);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMovies[adapterPosition];
            mClickHandler.onClick(movie);
        }
    }

    public com.breunig.jeff.project1.adapters.MovieListAdapter.MovieListAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new com.breunig.jeff.project1.adapters.MovieListAdapter.MovieListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(com.breunig.jeff.project1.adapters.MovieListAdapter.MovieListAdapterViewHolder movieListAdapterViewHolder, int position) {
        Movie movie = mMovies[position];
        movieListAdapterViewHolder.mMovieTextView.setText(movie.title);
    }

    @Override
    public int getItemCount() {
        if (null == mMovies) return 0;
        return mMovies.length;
    }

    public void setMovies(Movie[] movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }
}