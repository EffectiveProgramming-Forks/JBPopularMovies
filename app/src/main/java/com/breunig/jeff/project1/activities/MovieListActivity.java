package com.breunig.jeff.project1.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.breunig.jeff.project1.R;
import com.breunig.jeff.project1.adapters.MovieListAdapter;
import com.breunig.jeff.project1.adapters.MovieListAdapter.MovieListAdapterOnClickHandler;
import com.breunig.jeff.project1.adapters.MovieListCursorAdapter;
import com.breunig.jeff.project1.database.MovieContract;
import com.breunig.jeff.project1.models.Movie;
import com.breunig.jeff.project1.models.MovieSortType;
import com.breunig.jeff.project1.network.AsyncTaskCompleteListener;
import com.breunig.jeff.project1.network.FetchMovieTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.breunig.jeff.project1.R.string.movies;

/**
 * Created by jkbreunig on 2/2/17.
 */

public class MovieListActivity extends AppCompatActivity implements MovieListAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor>, MovieListCursorAdapter.MovieListAdapterOnClickHandler  {
    private int mColumnWidth;
    private MovieListAdapter mMovieListAdapter;
    private MovieListCursorAdapter mFavoriteMovieListAdapter;
    private int mPosition = RecyclerView.NO_POSITION;
    private MovieSortType mMovieSortType = MovieSortType.POPULAR;
    private static final String MOVIE_SORT_TYPE_KEY = "movieSortType";
    private static final int MOVIE_LOADER_ID = 0;
    private static final String TAG = MovieListActivity.class.getSimpleName();
    @BindView(R.id.recyclerview_movie_list) RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message_display) TextView mErrorMessageDisplay;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;

    private int calculateColumnWidth(Context context, int numberOfColumns) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int width = (int) (displayMetrics.widthPixels / numberOfColumns);
        return width;
    }

    private int calculateNumberOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int numberOfColumns = (int) (dpWidth / 120);
        return numberOfColumns;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);

        int numberOfColumns = calculateNumberOfColumns(mRecyclerView.getContext());
        mColumnWidth = calculateColumnWidth(mRecyclerView.getContext(), numberOfColumns);

        GridLayoutManager layoutManager
                = new GridLayoutManager(this, numberOfColumns);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mMovieListAdapter = new MovieListAdapter(this, mColumnWidth);

        mFavoriteMovieListAdapter = new MovieListCursorAdapter(this, this, mColumnWidth);

        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIE_SORT_TYPE_KEY)) {
            mMovieSortType = MovieSortType.fromInt(savedInstanceState
                    .getInt(MOVIE_SORT_TYPE_KEY));
            if (mMovieSortType == null) {
                mMovieSortType = MovieSortType.POPULAR;
            }
        }

        updateMovieSortType(mMovieSortType);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(MOVIE_SORT_TYPE_KEY, mMovieSortType.getIntValue());
        super.onSaveInstanceState(outState);
    }

    private void updateMovieSortType(MovieSortType movieSortType) {
        mMovieSortType = movieSortType;
        updateTitle();
        mMovieListAdapter.setMovies(null);
        if (mMovieSortType == MovieSortType.FAVORITES) {
            mRecyclerView.setAdapter(mFavoriteMovieListAdapter);
            loadFavoriteMovieData();
        } else {
            mRecyclerView.setAdapter(mMovieListAdapter);
            loadMovieData();
        }
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
        Intent intent = new Intent(context, destinationClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("MOVIE", movie);
        intent.putExtra("POSTER_WIDTH", mColumnWidth);
        startActivity(intent);
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
        } else if (id == R.id.action_favorites) {
            updateMovieSortType(MovieSortType.FAVORITES);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateTitle() {
        String sortTypeTitle;
        if (mMovieSortType == MovieSortType.POPULAR) {
            sortTypeTitle = "Popular";
        } else if (mMovieSortType == MovieSortType.TOP_RATED) {
            sortTypeTitle = "Top rated";
        } else {
            sortTypeTitle = "Favorite";
        }
        setTitle(sortTypeTitle + " " + getString(movies));
    }

    /**
     * This method is called after this activity has been paused or restarted.
     * Often, this is after new data has been inserted through an AddTaskActivity,
     * so this restarts the loader to re-query the underlying data for any changes.
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (mMovieSortType == MovieSortType.FAVORITES) {
            loadFavoriteMovieData();
        }
    }

    private void loadFavoriteMovieData() {
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
    }

    /**
     * Instantiates and returns a new AsyncTaskLoader with the given ID.
     * This loader will return task data as a Cursor or null if an error occurs.
     *
     * Implements the required callbacks to take care of loading data at all stages of loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mTaskData = null;

            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    deliverResult(mTaskData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {

                try {
                    return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            MovieContract.MovieEntry.COLUMN_MOVIE_ID);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mFavoriteMovieListAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
        if (data.getCount() != 0) showMoviesView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFavoriteMovieListAdapter.swapCursor(null);
    }
}