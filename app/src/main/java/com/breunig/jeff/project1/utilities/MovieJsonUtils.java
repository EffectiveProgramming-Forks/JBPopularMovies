package com.breunig.jeff.project1.utilities;

import android.content.Context;

import com.breunig.jeff.project1.models.Movie;
import com.breunig.jeff.project1.models.MovieReview;
import com.breunig.jeff.project1.models.MovieReviews;
import com.breunig.jeff.project1.models.MovieTrailer;
import com.breunig.jeff.project1.models.Movies;

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

    public static Movies getMoviesFromJson(Context context, String jsonStr)
            throws JSONException {

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

        int page = moviesJson.getInt("page");
        int totalPages = moviesJson.getInt("total_pages");
        JSONArray moviesJsonArray = moviesJson.getJSONArray("results");
        ArrayList<Movie> moviesList = new ArrayList<Movie>(moviesJsonArray.length());

        for (int i = 0; i < moviesJsonArray.length(); i++) {
            JSONObject movieJsonObject = moviesJsonArray.getJSONObject(i);
            Movie movie = new Movie(movieJsonObject);
            moviesList.add(movie);
        }

        Movies movies = new Movies(moviesList, page, totalPages);
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

    public static MovieTrailer[] getMovieTrailersFromJson(Context context, String jsonStr)
            throws JSONException {

        MovieTrailer[] movieTrailers;

        JSONObject movieTrailersJson = new JSONObject(jsonStr);

        if (movieTrailersJson.has("status_code")) {
            int errorCode = movieTrailersJson.getInt("status_code");

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        JSONArray movieTrailersJsonArray = movieTrailersJson.getJSONArray("results");
        movieTrailers = new MovieTrailer[movieTrailersJsonArray.length()];

        for (int i = 0; i < movieTrailersJsonArray.length(); i++) {
            JSONObject movieTrailerJsonObject = movieTrailersJsonArray.getJSONObject(i);
            MovieTrailer movieTrailer = new MovieTrailer(movieTrailerJsonObject);
            movieTrailers[i] = movieTrailer;
        }

        return movieTrailers;
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
        SimpleDateFormat formatOut = new SimpleDateFormat("MMMM d, yyyy");
        return formatOut.format(date);
    }
}
