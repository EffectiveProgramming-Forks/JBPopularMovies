package com.breunig.jeff.project1.network;

import android.content.Context;
import android.os.AsyncTask;

import com.breunig.jeff.project1.listeners.AsyncTaskCompleteListener;
import com.breunig.jeff.project1.models.MovieTrailer;
import com.breunig.jeff.project1.utilities.MovieJsonUtils;
import com.breunig.jeff.project1.utilities.NetworkUtils;

import java.net.URL;

/**
 * Created by jkbreunig on 2/19/17.
 */

public class FetchMovieTrailerTask extends AsyncTask<String, Void, MovieTrailer[]> {

    private Context mContext;
    private AsyncTaskCompleteListener<MovieTrailer[]> mListener;
    private int mMovieId;

    public FetchMovieTrailerTask(Context ctx, AsyncTaskCompleteListener<MovieTrailer[]> listener, int movieId) {
        mContext = ctx;
        mListener = listener;
        mMovieId = movieId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected MovieTrailer[] doInBackground(String... params) {

        URL movieRequestUrl = NetworkUtils.buildMovieTrailerListUrl(mMovieId);

        try {
            String jsonMovieTrailerResponse = NetworkUtils
                    .getResponseFromHttpUrl(movieRequestUrl);
            return MovieJsonUtils.getMovieTrailersFromJson(mContext, jsonMovieTrailerResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(MovieTrailer[] movieTrailers) {
        super.onPostExecute(movieTrailers);
        mListener.onTaskComplete(movieTrailers);
    }
}