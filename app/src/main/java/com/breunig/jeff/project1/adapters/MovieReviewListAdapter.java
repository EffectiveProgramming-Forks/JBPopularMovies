package com.breunig.jeff.project1.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.breunig.jeff.project1.R;
import com.breunig.jeff.project1.models.MovieReview;

/**
 * Created by jkbreunig on 2/18/17.
 */

public class MovieReviewListAdapter extends RecyclerView.Adapter<MovieReviewListAdapter.MovieReviewListAdapterViewHolder> {

    private MovieReview[] mMovieReviews;

    public MovieReviewListAdapter() {
    }

    public class MovieReviewListAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView mAuthorTextView;
        public final TextView mContentTextView;

        public MovieReviewListAdapterViewHolder(View view) {
            super(view);
            mAuthorTextView = (TextView) view.findViewById(R.id.tv_author);
            mContentTextView = (TextView) view.findViewById(R.id.tv_content);
        }
    }

    public MovieReviewListAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieReviewListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieReviewListAdapterViewHolder viewHolder, int position) {
        MovieReview movieReview = mMovieReviews[position];
        viewHolder.mAuthorTextView.setText(movieReview.author);
        viewHolder.mContentTextView.setText(movieReview.content);
    }

    @Override
    public int getItemCount() {
        if (null == mMovieReviews) return 0;
        return mMovieReviews.length;
    }

    public void setMovies(MovieReview[] movieReviews) {
        mMovieReviews = movieReviews;
        notifyDataSetChanged();
    }
}
