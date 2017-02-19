package com.breunig.jeff.project1.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.breunig.jeff.project1.R;
import com.breunig.jeff.project1.adapters.MovieReviewListAdapter;
import com.breunig.jeff.project1.models.MovieReview;
import com.breunig.jeff.project1.models.MovieReviews;
import com.breunig.jeff.project1.network.AsyncTaskCompleteListener;
import com.breunig.jeff.project1.network.FetchMovieReviewTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieReviewListActivity extends AppCompatActivity {
    private MovieReviews mMovieReviews;
    private MovieReviewListAdapter mMovieReviewListAdapter;
    @BindView(R.id.tv_title) TextView mTitleTextView;
    @BindView(R.id.recyclerview_movie_review_list) RecyclerView mReviewsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.movie_reviews));

        setContentView(R.layout.activity_movie_review_list);
        ButterKnife.bind(this);

        mMovieReviews = (MovieReviews) getIntent().getParcelableExtra("MOVIE_REVIEWS");
        String title = getIntent().getStringExtra("MOVIE_TITLE");
        mTitleTextView.setText(title);
        setupMovieReviews();
        loadMovieReviewData();
    }

    private void setupMovieReviews() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        LinearLayout header = (LinearLayout) findViewById(R.layout.movie_title);
        mReviewsRecyclerView.setLayoutManager(layoutManager);
        mReviewsRecyclerView.setNestedScrollingEnabled(false);

        mMovieReviewListAdapter = new MovieReviewListAdapter();

        mReviewsRecyclerView.setAdapter(mMovieReviewListAdapter);
    }

    private void loadMovieReviewData() {
        //showMoviesView();
        //mLoadingIndicator.setVisibility(View.VISIBLE);
        new FetchMovieReviewTask(this, new MovieReviewListActivity.FetchMovieReviewTaskCompleteListener(), mMovieReviews.movieId).execute();
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
}
