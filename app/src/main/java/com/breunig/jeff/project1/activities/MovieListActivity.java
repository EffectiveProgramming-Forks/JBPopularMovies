package com.breunig.jeff.project1.activities;

import android.content.Context;
import android.content.Intent;
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
import com.breunig.jeff.project1.network.AsyncTaskCompleteListener;
import com.breunig.jeff.project1.network.FetchMovieTask;

import static com.breunig.jeff.project1.R.string.movies;

/**
 * Created by jkbreunig on 2/2/17.
 */

public class MovieListActivity extends AppCompatActivity implements MovieListAdapterOnClickHandler  {
    private int mColumnWidth;
    private MovieSortType mMovieSortType;
    private RecyclerView mRecyclerView;
    private MovieListAdapter mMovieListAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private int calculateColumnWidth(Context context, int numberOfColumns) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int width = (int) (displayMetrics.widthPixels / numberOfColumns);
        return width;
    }

    private int calculateNumberOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int numberOfColumns = (int) (dpWidth / 180);
        return numberOfColumns;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movie_list);
        int numberOfColumns = calculateNumberOfColumns(mRecyclerView.getContext());
        mColumnWidth = calculateColumnWidth(mRecyclerView.getContext(), numberOfColumns);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        GridLayoutManager layoutManager
                = new GridLayoutManager(this, numberOfColumns);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieListAdapter = new MovieListAdapter(this, mColumnWidth);

        mRecyclerView.setAdapter(mMovieListAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        updateMovieSortType(MovieSortType.POPULAR);
    }

    private void updateMovieSortType(MovieSortType movieSortType) {
        mMovieSortType = movieSortType;
        updateTitle();
        mMovieListAdapter.setMovies(null);
        loadMovieData();
    }

    private void loadMovieData() {
        showMoviesView();
        mLoadingIndicator.setVisibility(View.VISIBLE);
        new FetchMovieTask(this, new FetchMovieTaskCompleteListener(), mMovieSortType).execute();
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

    private void showMoviesView() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchMovieTaskCompleteListener implements AsyncTaskCompleteListener<Movie[]> {

        @Override
        public void onTaskComplete(Movie[] movies) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movies != null) {
                showMoviesView();
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

        if (id == R.id.action_popular) {
            updateMovieSortType(MovieSortType.POPULAR);
            return true;
        } else if (id == R.id.action_top_rated) {
            updateMovieSortType(MovieSortType.TOP_RATED);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateTitle() {
        String sortTypeTitle = mMovieSortType == MovieSortType.POPULAR ? getString(R.string.popular) : getString(R.string.top_rated);
        setTitle(sortTypeTitle + " " + getString(movies));
    }
}