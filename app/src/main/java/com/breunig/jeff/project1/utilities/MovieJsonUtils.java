package com.breunig.jeff.project1.utilities;

import android.content.Context;

import com.breunig.jeff.project1.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
