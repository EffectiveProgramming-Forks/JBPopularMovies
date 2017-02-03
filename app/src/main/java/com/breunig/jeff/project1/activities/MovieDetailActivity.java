package com.breunig.jeff.project1.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.breunig.jeff.project1.R;
import com.breunig.jeff.project1.models.Movie;
import com.breunig.jeff.project1.utilities.MovieJsonUtils;
import com.breunig.jeff.project1.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {
    private Movie mMovie;
    private int mPosterWidth;
    private ImageView mPosterImageView;
    private TextView mTitleTextView;
    private TextView mOverviewTextView;
    private TextView mReleaseDateTextView;
    private TextView mUserRatingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.movie_detail));

        setContentView(R.layout.activity_movie_detail);

        mMovie = (Movie) getIntent().getSerializableExtra("MOVIE");
        mPosterWidth = getIntent().getIntExtra("POSTER_WIDTH", 0);
        mPosterImageView = (ImageView) findViewById(R.id.iv_poster);
        Picasso.with(this).load(NetworkUtils.buildMoviePosterUrlString(mMovie.posterPath, mPosterWidth))
                .into(mPosterImageView);

        mTitleTextView = (TextView) findViewById(R.id.tv_title);
        mTitleTextView.setText(mMovie.title);

        mOverviewTextView = (TextView) findViewById(R.id.tv_overview);
        mOverviewTextView.setText(mMovie.overview);

        mReleaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
        String formattedReleaseDate = MovieJsonUtils.formatDateString(mMovie.releaseDate);
        if (formattedReleaseDate != null && !formattedReleaseDate.isEmpty()) {
            mReleaseDateTextView.setText(getString(R.string.release_date) + ": " + formattedReleaseDate);
        } else {
            mReleaseDateTextView.setText(getString(R.string.release_date) + getString(R.string.not_available));
        }

        mUserRatingTextView = (TextView) findViewById(R.id.tv_user_rating);
        String userRating = mMovie.userRating;
        if (userRating != null) {
            mUserRatingTextView.setText(getString(R.string.user_rating) + ": " + userRating + "/ 10");
        } else {
            mUserRatingTextView.setText(getString(R.string.user_rating) + getString(R.string.not_available));
        }
    }
}