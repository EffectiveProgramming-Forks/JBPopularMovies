package com.breunig.jeff.project1.network;

import android.content.Context;
import android.os.AsyncTask;

import com.breunig.jeff.project1.models.MovieReviews;
import com.breunig.jeff.project1.utilities.MovieJsonUtils;
import com.breunig.jeff.project1.utilities.NetworkUtils;

import java.net.URL;

/**
 * Created by jkbreunig on 2/18/17.
 */

public class FetchMovieReviewTask extends AsyncTask<String, Void, MovieReviews> {

    private Context mContext;
    private AsyncTaskCompleteListener<MovieReviews> mListener;
    private int mMovieId;
    private int mPage;

    public FetchMovieReviewTask(Context ctx, AsyncTaskCompleteListener<MovieReviews> listener, int movieId, int page) {
        mContext = ctx;
        mListener = listener;
        mMovieId = movieId;
        mPage = page;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected MovieReviews doInBackground(String... params) {

        URL movieRequestUrl = NetworkUtils.buildMovieReviewListUrl(mMovieId, mPage);

        try {
            String jsonMovieReviewResponse = NetworkUtils
                    .getResponseFromHttpUrl(movieRequestUrl);
            return MovieJsonUtils.getMovieReviewsFromJson(mContext, jsonMovieReviewResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(MovieReviews movieReviews) {
        super.onPostExecute(movieReviews);
        mListener.onTaskComplete(movieReviews);
    }
}
