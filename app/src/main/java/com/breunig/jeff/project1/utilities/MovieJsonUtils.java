package com.breunig.jeff.project1.utilities;

import android.content.Context;

import com.breunig.jeff.project1.models.Movie;
import com.breunig.jeff.project1.models.MovieReview;
import com.breunig.jeff.project1.models.MovieReviews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jkbreunig on 2/2/17.
 */

public final class MovieJsonUtils {

    public static Movie[] getMoviesFromJson(Context context, String jsonStr)
            throws JSONException {

        Movie[] movies;

        JSONObject moviesJson = new JSONObject(jsonStr);

        if (moviesJson.has("status_code")) {
            int errorCode = moviesJson.getInt("status_code");

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        JSONArray moviesJsonArray = moviesJson.getJSONArray("results");
        movies = new Movie[moviesJsonArray.length()];

        for (int i = 0; i < moviesJsonArray.length(); i++) {
            JSONObject movieJsonObject = moviesJsonArray.getJSONObject(i);
            Movie movie = new Movie(movieJsonObject);
            movies[i] = movie;
        }

        return movies;
    }

    public static MovieReviews getMovieReviewsFromJson(Context context, String jsonStr)
            throws JSONException {

        JSONObject movieReviewsJson = new JSONObject(jsonStr);

        if (movieReviewsJson.has("status_code")) {
            int errorCode = movieReviewsJson.getInt("status_code");

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        int page = movieReviewsJson.getInt("page");
        int totalPages = movieReviewsJson.getInt("total_pages");
        JSONArray movieReviewsJsonArray = movieReviewsJson.getJSONArray("results");
        ArrayList<MovieReview> reviews = new ArrayList<MovieReview>(movieReviewsJsonArray.length());

        for (int i = 0; i < movieReviewsJsonArray.length(); i++) {
            JSONObject movieReviewJsonObject = movieReviewsJsonArray.getJSONObject(i);
            MovieReview movieReview = new MovieReview(movieReviewJsonObject);
            reviews.add(movieReview);
        }

        MovieReviews movieReviews = new MovieReviews(reviews, page, totalPages);
        return movieReviews;
    }

    public static String formatDateString(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        SimpleDateFormat formatOut = new SimpleDateFormat("MMM d, yyyy");
        return formatOut.format(date);
    }
}
