package com.breunig.jeff.project1.utilities;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

import com.breunig.jeff.project1.models.Movie;

/**
 * Created by jkbreunig on 2/2/17.
 */

public final class MovieJsonUtils {

    public static Movie[] getMoviesFromJson(Context context, String jsonStr)
            throws JSONException {

        final String OWM_MESSAGE_CODE = "cod";

        Movie[] movies = null;

        JSONObject moviesJson = new JSONObject(jsonStr);

        if (moviesJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = moviesJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray moviesJsonArray = moviesJson.getJSONArray("results");
        movies = new Movie[moviesJsonArray.length()];

        for (int i = 0; i < moviesJsonArray.length(); i++) {
            String date;
            String highAndLow;
            Movie movie = new Movie();
            JSONObject movieJsonObject = moviesJsonArray.getJSONObject(i);
            movie.mTitle = movieJsonObject.getString("title");

            movies[i] = movie;
        }

        return movies;
    }

    /**
     * Parse the JSON and convert it into ContentValues that can be inserted into our database.
     *
     * @param context         An application context, such as a service or activity context.
     * @param forecastJsonStr The JSON to parse into ContentValues.
     *
     * @return An array of ContentValues parsed from the JSON.
     */
    public static ContentValues[] getFullWeatherDataFromJson(Context context, String forecastJsonStr) {
        /** This will be implemented in a future lesson **/
        return null;
    }
}
