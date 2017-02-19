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

/**
 * Created by jkbreunig on 2/19/17.
 */

public class MovieTrailerListAdapter extends RecyclerView.Adapter<MovieTrailerListAdapter.MovieTrailerListAdapterViewHolder> {

    private MovieTrailer[] mMovieTrailers;
    private int mColumnWidth;

    private final MovieTrailerListAdapter.MovieTrailerListAdapterOnClickHandler mClickHandler;

    public interface MovieTrailerListAdapterOnClickHandler {
        void onClick(MovieTrailer movieTrailer);
    }

    public MovieTrailerListAdapter(MovieTrailerListAdapter.MovieTrailerListAdapterOnClickHandler clickHandler, int columnWidth) {
        mClickHandler = clickHandler;
        mColumnWidth = columnWidth;
    }

    public class MovieTrailerListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mMovieImageView;

        public MovieTrailerListAdapterViewHolder(View view) {
            super(view);
            mMovieImageView = (ImageView) view.findViewById(R.id.iv_poster);
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
        int imageHeight = (int) (mColumnWidth * 1.5);
        Picasso.with(imageView.getContext())
                .load(NetworkUtils.buildMoviePosterUrlString(movieTrailer.key, mColumnWidth))
                .resize(mColumnWidth, imageHeight)
                .centerCrop()
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        if (null == mMovieTrailers) return 0;
        return mMovieTrailers.length;
    }

    public void setMovieTrailers(MovieTrailer[] movieTrailers) {
        mMovieTrailers = movieTrailers;
        notifyDataSetChanged();
    }
}