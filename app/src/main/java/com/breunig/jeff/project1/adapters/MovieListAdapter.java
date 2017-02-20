package com.breunig.jeff.project1.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.breunig.jeff.project1.R;
import com.breunig.jeff.project1.models.Movie;
import com.breunig.jeff.project1.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListAdapterViewHolder> {

    private Movie[] mMovies;
    private int mColumnWidth;

    private final MovieListAdapter.MovieListAdapterOnClickHandler mClickHandler;

    public interface MovieListAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    public MovieListAdapter(MovieListAdapter.MovieListAdapterOnClickHandler clickHandler, int columnWidth) {
        mClickHandler = clickHandler;
        mColumnWidth = columnWidth;
    }

    public class MovieListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mMovieImageView;

        public MovieListAdapterViewHolder(View view) {
            super(view);
            mMovieImageView = (ImageView) view.findViewById(R.id.iv_poster);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie movie = mMovies[adapterPosition];
            mClickHandler.onClick(movie);
        }
    }

    public MovieListAdapter.MovieListAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieListAdapter.MovieListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieListAdapter.MovieListAdapterViewHolder viewHolder, int position) {
        Movie movie = mMovies[position];
        ImageView imageView = viewHolder.mMovieImageView;
        int imageHeight = (int) (mColumnWidth * 1.5);
        Picasso.with(imageView.getContext())
                .load(NetworkUtils.buildMoviePosterUrlString(movie.posterPath, mColumnWidth))
                .resize(mColumnWidth, imageHeight)
                .centerCrop()
                .into(imageView);
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