package com.breunig.jeff.project1.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.breunig.jeff.project1.R;
import com.breunig.jeff.project1.models.MovieTrailer;
import com.breunig.jeff.project1.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jkbreunig on 2/19/17.
 */

public class MovieTrailerListAdapter extends RecyclerView.Adapter<MovieTrailerListAdapter.MovieTrailerListAdapterViewHolder> {

    private MovieTrailer[] mMovieTrailers;

    private final MovieTrailerListAdapter.MovieTrailerListAdapterOnClickHandler mClickHandler;

    public interface MovieTrailerListAdapterOnClickHandler {
        void onClick(MovieTrailer movieTrailer);
    }

    public MovieTrailerListAdapter(MovieTrailerListAdapter.MovieTrailerListAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public class MovieTrailerListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_poster) ImageView mMovieImageView;

        public MovieTrailerListAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            MovieTrailer movieTrailer = mMovieTrailers[adapterPosition];
            mClickHandler.onClick(movieTrailer);
        }
    }

    public MovieTrailerListAdapter.MovieTrailerListAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieTrailerListAdapter.MovieTrailerListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieTrailerListAdapter.MovieTrailerListAdapterViewHolder viewHolder, int position) {
        MovieTrailer movieTrailer = mMovieTrailers[position];
        ImageView imageView = viewHolder.mMovieImageView;
        Picasso.with(imageView.getContext())
                .load(NetworkUtils.buildMovieTrailerPosterUrlString(movieTrailer.key))
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        if (null == mMovieTrailers) {
            return 0;
        } else {
            return mMovieTrailers.length;
        }
    }

    public void setMovieTrailers(MovieTrailer[] movieTrailers) {
        mMovieTrailers = movieTrailers;
        notifyDataSetChanged();
    }
}