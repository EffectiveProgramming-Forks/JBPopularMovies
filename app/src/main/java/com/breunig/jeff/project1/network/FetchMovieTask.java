package com.breunig.jeff.project1.network;

import android.content.Context;
import android.os.AsyncTask;

import com.breunig.jeff.project1.models.Movie;
import com.breunig.jeff.project1.models.MovieSortType;
import com.breunig.jeff.project1.utilities.MovieJsonUtils;
import com.breunig.jeff.project1.utilities.NetworkUtils;

import java.net.URL;

/**
 * Created by jkbreunig on 2/17/17.
 */

public class FetchMovieTask extends AsyncTask<String, Void, Movie[]> {

    private Context mContext;
    private AsyncTaskCompleteListener<Movie[]> mListener;
    private MovieSortType mMovieSortType;

    public FetchMovieTask(Context ctx, AsyncTaskCompleteListener<Movie[]> listener, MovieSortType movieSortType) {
        mContext = ctx;
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Movie[] doInBackground(String... params) {

        URL movieRequestUrl = NetworkUtils.buildMovieListUrl(mMovieSortType);

        try {
            String jsonMovieResponse = NetworkUtils
                    .getResponseFromHttpUrl(movieRequestUrl);
            return MovieJsonUtils.getMoviesFromJson(mContext, jsonMovieResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        super.onPostExecute(movies);
        mListener.onTaskComplete(movies);
    }
}
