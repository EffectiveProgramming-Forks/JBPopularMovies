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

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {
    private Movie mMovie;
    private int mPosterWidth;
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
            mReleaseDateTextView.setText(getString(R.string.release_date) + ": " + formattedReleaseDate);
        } else {
            mReleaseDateTextView.setText(getString(R.string.release_date) + getString(R.string.not_available));
        }

        String userRating = mMovie.userRating;
        if (userRating != null) {
            mUserRatingTextView.setText(getString(R.string.user_rating) + ": " + userRating + "/ 10");
        } else {
            mUserRatingTextView.setText(getString(R.string.user_rating) + getString(R.string.not_available));
        }
    }
}