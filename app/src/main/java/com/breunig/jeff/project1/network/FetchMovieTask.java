package com.breunig.jeff.project1.network;

import android.content.Context;
import android.os.AsyncTask;

import com.breunig.jeff.project1.listeners.AsyncTaskCompleteListener;
import com.breunig.jeff.project1.models.MovieSortType;
import com.breunig.jeff.project1.models.Movies;
import com.breunig.jeff.project1.utilities.MovieJsonUtils;
import com.breunig.jeff.project1.utilities.NetworkUtils;

import java.net.URL;

/**
 * Created by jkbreunig on 2/17/17.
 */

public class FetchMovieTask extends AsyncTask<String, Void, Movies> {

    private Context mContext;
    private AsyncTaskCompleteListener<Movies> mListener;
    private MovieSortType mMovieSortType;
    private int mPage;

    public FetchMovieTask(Context ctx, AsyncTaskCompleteListener<Movies> listener, MovieSortType movieSortType, int page) {
        mContext = ctx;
        mListener = listener;
        mMovieSortType = movieSortType;
        mPage = page;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Movies doInBackground(String... params) {

        URL movieRequestUrl = NetworkUtils.buildMovieListUrl(mMovieSortType, mPage);

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
    protected void onPostExecute(Movies movies) {
        super.onPostExecute(movies);
        mListener.onTaskComplete(movies);
    }
}
