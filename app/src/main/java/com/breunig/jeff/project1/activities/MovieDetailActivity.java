package com.breunig.jeff.project1.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.breunig.jeff.project1.R;
import com.breunig.jeff.project1.adapters.MovieReviewListAdapter;
import com.breunig.jeff.project1.adapters.MovieTrailerListAdapter;
import com.breunig.jeff.project1.database.MovieDbHelper;
import com.breunig.jeff.project1.listeners.AsyncTaskCompleteListener;
import com.breunig.jeff.project1.models.Movie;
import com.breunig.jeff.project1.models.MovieReview;
import com.breunig.jeff.project1.models.MovieReviews;
import com.breunig.jeff.project1.models.MovieTrailer;
import com.breunig.jeff.project1.network.FetchMovieReviewTask;
import com.breunig.jeff.project1.network.FetchMovieTrailerTask;
import com.breunig.jeff.project1.utilities.MovieJsonUtils;
import com.breunig.jeff.project1.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity implements MovieTrailerListAdapter.MovieTrailerListAdapterOnClickHandler {
    private Movie mMovie;
    private MovieReviews mMovieReviews;
    private MovieTrailer[] mMovieTrailers;
    private MovieTrailerListAdapter mMovieTrailerListAdapter;
    private int mPosterWidth;
    private MovieReviewListAdapter mMovieReviewListAdapter;
    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    private static int mReviewsToDisplayCount = 1;
    @BindView(R.id.recyclerview_movie_review_list) RecyclerView mReviewsRecyclerView;
    @BindView(R.id.recyclerview_movie_trailer_list) RecyclerView mTrailersRecyclerView;
    @BindView(R.id.iv_poster) ImageView mPosterImageView;
    @BindView(R.id.tv_title) TextView mTitleTextView;
    @BindView(R.id.tv_overview) TextView mOverviewTextView;
    @BindView(R.id.tv_release_date) TextView mReleaseDateTextView;
    @BindView(R.id.tv_user_rating) TextView mUserRatingTextView;
    @BindView(R.id.tv_more_reviews) TextView mMoreReviewsTextView;
    @BindView(R.id.tv_reviews_title) TextView mReviewsTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.movie_details));

        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        mMovie = (Movie) getIntent().getParcelableExtra(getString(R.string.EXTRA_MOVIE));
        mPosterWidth = getIntent().getIntExtra(getString(R.string.EXTRA_POSTER_WIDTH), 0);
        Picasso.with(this).load(NetworkUtils.buildMoviePosterUrlString(mMovie.posterPath, mPosterWidth))
                .into(mPosterImageView);

        mTitleTextView.setText(mMovie.title);

        mOverviewTextView.setText(mMovie.overview);

        if (mMovie.releaseDate != null) {
            String formattedReleaseDate = MovieJsonUtils.formatDateString(mMovie.releaseDate);
            mReleaseDateTextView.setText(formattedReleaseDate);
        }

        String userRating = mMovie.userRating;
        if (userRating != null) {
            mUserRatingTextView.setText(userRating + "/ 10");
        }

        setupMovieReviews();
        loadMovieReviewData();

        setupMovieTrailers();
        loadMovieTrailerData();

        boolean isFavorite = MovieDbHelper.query(this, mMovie.movieId);
        mMovie.isFavorite = isFavorite;
        invalidateOptionsMenu();
    }

    private void setupMovieReviews() {
        mMovieReviews = new MovieReviews(mMovie.movieId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mReviewsRecyclerView.setLayoutManager(layoutManager);
        mReviewsRecyclerView.setNestedScrollingEnabled(false);
        mMovieReviewListAdapter = new MovieReviewListAdapter();

        mReviewsRecyclerView.setAdapter(mMovieReviewListAdapter);
    }

    private void loadMovieReviewData() {
        new FetchMovieReviewTask(this, new FetchMovieReviewTaskCompleteListener(), mMovie.movieId, mMovieReviews.getPage()).execute();
    }

    public class FetchMovieReviewTaskCompleteListener implements AsyncTaskCompleteListener<MovieReviews> {

        @Override
        public void onTaskComplete(MovieReviews movieReviews) {
            mMovieReviews.updatePageResults(movieReviews);
            if (mMovieReviews.results != null) {
                updateReviewLabelVisibility(mMovieReviews.results.size());
                int resultCountToDisplay = Math.min(mMovieReviews.results.size(), mReviewsToDisplayCount);
                ArrayList<MovieReview> displayList = new ArrayList<MovieReview>(mMovieReviews.results.subList(0, resultCountToDisplay));
                MovieReview[] movieReviewArray = displayList.toArray(new MovieReview[resultCountToDisplay]);
                mMovieReviewListAdapter.setMovieReviews(mMovie.title, movieReviewArray, false);
            } else {
                updateReviewLabelVisibility(0);
            }
        }
    }

    private void updateReviewLabelVisibility(int reviewCount) {
        if (reviewCount == 0) {
            mReviewsTitleTextView.setVisibility(View.GONE);
            mMoreReviewsTextView.setVisibility(View.GONE);
        } else if (reviewCount <= mReviewsToDisplayCount) {
            mReviewsTitleTextView.setVisibility(View.VISIBLE);
            mMoreReviewsTextView.setVisibility(View.GONE);
        } else {
            mReviewsTitleTextView.setVisibility(View.VISIBLE);
            mMoreReviewsTextView.setVisibility(View.VISIBLE);
        }
    }

    public void onClickMoreReviews(View view) {
        Context context = this;
        Class destinationClass = MovieReviewListActivity.class;
        Intent intent = new Intent(context, destinationClass);
        intent.putExtra(getString(R.string.EXTRA_MOVIE_REVIEWS), mMovieReviews);
        intent.putExtra(getString(R.string.EXTRA_MOVIE_TITLE), mMovie.title);
        startActivity(intent);
    }

    private void setupMovieTrailers() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        mTrailersRecyclerView.setLayoutManager(layoutManager);
        mTrailersRecyclerView.setHasFixedSize(false);

        mMovieTrailerListAdapter = new MovieTrailerListAdapter(this);

        mTrailersRecyclerView.setAdapter(mMovieTrailerListAdapter);
    }

    private void loadMovieTrailerData() {
        new FetchMovieTrailerTask(this, new FetchMovieTrailerTaskCompleteListener(), mMovie.movieId).execute();
    }

    public class FetchMovieTrailerTaskCompleteListener implements AsyncTaskCompleteListener<MovieTrailer[]> {

        @Override
        public void onTaskComplete(MovieTrailer[] movieTrailers) {
            mMovieTrailers = movieTrailers;
            if (mMovieTrailers != null) {
                mMovieTrailerListAdapter.setMovieTrailers(movieTrailers);
            }
        }
    }

    @Override
    public void onClick(MovieTrailer movieTrailer) {
        Uri uri = NetworkUtils.buildMovieTrailerWatchUrl(movieTrailer.key);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_detail, menu);
        MenuItem favoriteMenuItem = menu.findItem(R.id.action_favorite);
        if (favoriteMenuItem != null) {
            updateMenuIconColor(favoriteMenuItem);
        }
        return true;
    }

    private static void tintMenuIcon(Context context, MenuItem item, @ColorRes int color) {
        Drawable drawable = item.getIcon();
        Drawable wrapDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrapDrawable, context.getResources().getColor(color));

        item.setIcon(wrapDrawable);
    }

    private void updateMenuIconColor(MenuItem item) {
        int color = mMovie.isFavorite ? R.color.colorAccent : android.R.color.white;
        tintMenuIcon(this, item, color);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_favorite) {
            mMovie.isFavorite = !mMovie.isFavorite;
            updateMenuIconColor(item);
            if (mMovie.isFavorite) {
                boolean didInsert = MovieDbHelper.insert(this, mMovie);
                if (didInsert == false) {
                    mMovie.isFavorite = false;
                    updateMenuIconColor(item);
                }
            } else {
                boolean didDelete = MovieDbHelper.delete(this, mMovie.movieId);
                updateMenuIconColor(item);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}