package com.breunig.jeff.project1.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.breunig.jeff.project1.R;
import com.breunig.jeff.project1.adapters.MovieReviewListAdapter;
import com.breunig.jeff.project1.adapters.MovieTrailerListAdapter;
import com.breunig.jeff.project1.models.Movie;
import com.breunig.jeff.project1.models.MovieReview;
import com.breunig.jeff.project1.models.MovieReviews;
import com.breunig.jeff.project1.models.MovieTrailer;
import com.breunig.jeff.project1.network.AsyncTaskCompleteListener;
import com.breunig.jeff.project1.network.FetchMovieReviewTask;
import com.breunig.jeff.project1.network.FetchMovieTrailerTask;
import com.breunig.jeff.project1.utilities.MovieJsonUtils;
import com.breunig.jeff.project1.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity implements MovieTrailerListAdapter.MovieTrailerListAdapterOnClickHandler {
    private Movie mMovie;
    private MovieReviews mMovieReviews;
    private MovieTrailer[] mMovieTrailers;
    private MovieTrailerListAdapter mMovieTrailerListAdapter;
    private int mPosterWidth;
    private MovieReviewListAdapter mMovieReviewListAdapter;
    @BindView(R.id.recyclerview_movie_review_list) RecyclerView mReviewsRecyclerView;
    @BindView(R.id.recyclerview_movie_trailer_list) RecyclerView mTrailersRecyclerView;
    @BindView(R.id.iv_poster) ImageView mPosterImageView;
    @BindView(R.id.tv_title) TextView mTitleTextView;
    @BindView(R.id.tv_overview) TextView mOverviewTextView;
    @BindView(R.id.tv_release_date) TextView mReleaseDateTextView;
    @BindView(R.id.tv_user_rating) TextView mUserRatingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.movie_detail));

        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        mMovie = (Movie) getIntent().getParcelableExtra("MOVIE");
        mPosterWidth = getIntent().getIntExtra("POSTER_WIDTH", 0);
        Picasso.with(this).load(NetworkUtils.buildMoviePosterUrlString(mMovie.posterPath, mPosterWidth))
                .into(mPosterImageView);

        mTitleTextView.setText(mMovie.title);

        mOverviewTextView.setText(mMovie.overview);

        String formattedReleaseDate = MovieJsonUtils.formatDateString(mMovie.releaseDate);
        if (formattedReleaseDate != null && !formattedReleaseDate.isEmpty()) {
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
        //showMoviesView();
        //mLoadingIndicator.setVisibility(View.VISIBLE);
        new FetchMovieReviewTask(this, new FetchMovieReviewTaskCompleteListener(), mMovie.movieId).execute();
    }

    public class FetchMovieReviewTaskCompleteListener implements AsyncTaskCompleteListener<MovieReviews> {

        @Override
        public void onTaskComplete(MovieReviews movieReviews) {
            //mLoadingIndicator.setVisibility(View.INVISIBLE);
            mMovieReviews.updatePageResults(movieReviews);
            if (mMovieReviews.results != null) {
                //showMoviesView();
                MovieReview[] movieReviewArray = mMovieReviews.results.toArray(new MovieReview[(mMovieReviews.results.size())]);
                mMovieReviewListAdapter.setMovies(movieReviewArray);
            } else {
                //showErrorMessage();
            }
        }
    }

    public void onClickMoreReviews(View view) {
        Toast.makeText(this, "did click", Toast.LENGTH_LONG).show();
        Context context = this;
        Class destinationClass = MovieReviewListActivity.class;
        Intent intent = new Intent(context, destinationClass);
        intent.putExtra("MOVIE_REVIEWS", mMovieReviews);
        intent.putExtra("MOVIE_TITLE", mMovie.title);
        startActivity(intent);
    }

    private void setupMovieTrailers() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        mTrailersRecyclerView.setLayoutManager(layoutManager);
        mTrailersRecyclerView.setHasFixedSize(true);

        mMovieTrailerListAdapter = new MovieTrailerListAdapter(this, 200);

        mTrailersRecyclerView.setAdapter(mMovieTrailerListAdapter);
    }

    private void loadMovieTrailerData() {
        //showMoviesView();
        //mLoadingIndicator.setVisibility(View.VISIBLE);
        new FetchMovieTrailerTask(this, new FetchMovieTrailerTaskCompleteListener(), mMovie.movieId).execute();
    }

    public class FetchMovieTrailerTaskCompleteListener implements AsyncTaskCompleteListener<MovieTrailer[]> {

        @Override
        public void onTaskComplete(MovieTrailer[] movieTrailers) {
            //mLoadingIndicator.setVisibility(View.INVISIBLE);
            mMovieTrailers = movieTrailers;
            if (mMovieTrailers != null) {
                //showMoviesView();
                mMovieTrailerListAdapter.setMovieTrailers(movieTrailers);
            } else {
                //showErrorMessage();
            }
        }
    }

    @Override
    public void onClick(MovieTrailer movieTrailer) {
        Uri uri = NetworkUtils.buildMovieTrailerWatchUrl(movieTrailer.key);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}