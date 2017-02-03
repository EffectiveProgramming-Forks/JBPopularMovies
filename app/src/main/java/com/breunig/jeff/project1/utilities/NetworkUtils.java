package com.breunig.jeff.project1.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import com.breunig.jeff.project1.models.MovieSortType;

/**
 * Created by jkbreunig on 2/2/17.
 */

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";

    private static final String format = "json";

    final static String API_KEY_PARAM = "api_key";
    final static String API_KEY = "3037ded6b1bfd00afee7eb91f13fdf0c";
    final static String SORT_TYPE_POPULAR = "popular";
    final static String SORT_TYPE_TOP_RATED = "top_rated";

    private static String getMovieSortTypeString(MovieSortType movieSortType) {
        if (movieSortType == MovieSortType.POPULAR) {
            return SORT_TYPE_POPULAR;
        } else {
            return SORT_TYPE_TOP_RATED;
        }
    }

    public static URL buildMovieListUrl(MovieSortType movieSortType) {
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(getMovieSortTypeString(movieSortType))
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
