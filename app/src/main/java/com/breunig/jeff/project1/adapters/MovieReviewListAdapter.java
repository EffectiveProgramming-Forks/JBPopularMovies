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

public class MovieReviewListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private String mMovieTitle;
    private boolean mShowMovieTitle;
    private MovieReview[] mMovieReviews;

    public MovieReviewListAdapter() {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mAuthorTextView;
        public TextView mContentTextView;

        public ViewHolder(View view) {
            super(view);
            mAuthorTextView = (TextView) view.findViewById(R.id.tv_author);
            mContentTextView = (TextView) view.findViewById(R.id.tv_content);
        }
    }

    public class ViewHolderHeader extends RecyclerView.ViewHolder {
        public TextView mTitleTextView;

        public ViewHolderHeader(View view) {
            super(view);
            mTitleTextView = (TextView) view.findViewById(R.id.tv_title);
        }
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        if (viewType == TYPE_HEADER) {
            int layoutIdForListItem = R.layout.movie_title;
            View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
            return new ViewHolderHeader(view);
        } else {
            int layoutIdForListItem = R.layout.movie_review_list_item;
            View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolderHeader) {
            ViewHolderHeader holder = (ViewHolderHeader) viewHolder;
            holder.mTitleTextView.setText(mMovieTitle);
        } else if (viewHolder instanceof ViewHolder) {
            MovieReview movieReview = getMovieReview(position);
            ViewHolder holder = (ViewHolder) viewHolder;
            holder.mAuthorTextView.setText(movieReview.author);
            holder.mContentTextView.setText(movieReview.content);
        }
    }

    @Override
    public int getItemViewType(int position) {
        boolean isHeader = isPositionHeader(position);
        if (!isHeader) {
            return TYPE_ITEM;
        } else {
            return TYPE_HEADER;
        }
    }

    private boolean isPositionHeader(int position) {
        return (mShowMovieTitle && position == 0);
    }

    private MovieReview getMovieReview(int position) {
        int headerRows = getHeaderRowCount();
        return mMovieReviews[position - headerRows];
    }

    @Override
    public int getItemCount() {
        int headerRows = getHeaderRowCount();
        if (null == mMovieReviews) {
            return headerRows;
        } else {
            return mMovieReviews.length + headerRows;
        }
    }

    private int getHeaderRowCount() {
        return mShowMovieTitle ? 1 : 0;
    }

    public void setMovieReviews(String movieTitle, MovieReview[] movieReviews, boolean showMovieTitle) {
        mMovieTitle = movieTitle;
        mMovieReviews = movieReviews;
        mShowMovieTitle = (showMovieTitle && mMovieTitle != null && !mMovieTitle.isEmpty());
        notifyDataSetChanged();
    }
}
