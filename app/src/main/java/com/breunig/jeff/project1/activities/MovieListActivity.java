package com.breunig.jeff.project1.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.breunig.jeff.project1.R;
import com.breunig.jeff.project1.adapters.MovieListAdapter;
import com.breunig.jeff.project1.adapters.MovieListAdapter.MovieListAdapterOnClickHandler;
import com.breunig.jeff.project1.models.Movie;
import com.breunig.jeff.project1.models.MovieSortType;
import com.breunig.jeff.project1.utilities.MovieJsonUtils;
import com.breunig.jeff.project1.utilities.NetworkUtils;

import java.net.URL;

/**
 * Created by jkbreunig on 2/2/17.
 */

public class MovieListActivity extends AppCompatActivity implements MovieListAdapterOnClickHandler  {
    public static final int numberOfColumns = 2;
    private int mColumnWidth;
    private MovieSortType movieSortType = MovieSortType.POPULAR;
    private RecyclerView mRecyclerView;
    private MovieListAdapter mMovieListAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private int calculateColumnWidth(Context context) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int width = (int) (displayMetrics.widthPixels / MovieListActivity.numberOfColumns);
        return width;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movie_list);
        mColumnWidth = calculateColumnWidth(mRecyclerView.getContext());

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        GridLayoutManager layoutManager
                = new GridLayoutManager(this, numberOfColumns);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieListAdapter = new MovieListAdapter(this, mColumnWidth);

        mRecyclerView.setAdapter(mMovieListAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        loadMovieData();
    }

    private void loadMovieData() {
        showMovieDataView();

        new com.breunig.jeff.project1.activities.MovieListActivity.FetchMovieTask().execute();
    }

    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Class destinationClass = MovieDetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("MOVIE", movie);
        intentToStartDetailActivity.putExtra("POSTER_WIDTH", mColumnWidth);
        startActivity(intentToStartDetailActivity);
    }

    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected Movie[] doInBackground(String... params) {

            URL movieRequestUrl = NetworkUtils.buildMovieListUrl(movieSortType);

            try {
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestUrl);
                return MovieJsonUtils.getMoviesFromJson(MovieListActivity.this, jsonMovieResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movies != null) {
                showMovieDataView();
                mMovieListAdapter.setMovies(movies);
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort) {
            mMovieListAdapter.setMovies(null);

            loadMovieData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}