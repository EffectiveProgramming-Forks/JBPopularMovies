package com.breunig.jeff.project1.utilities;

import android.net.Uri;
import android.util.Log;

import com.breunig.jeff.project1.models.MovieSortType;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Scanner;

/**
 * Created by jkbreunig on 2/2/17.
 */

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String MOVIE_POSTER_BASE_URL = "http://image.tmdb.org/t/p/";

    private static final String format = "json";

    private final static String API_KEY_PARAM = "api_key";
    private final static String API_KEY = "3037ded6b1bfd00afee7eb91f13fdf0c"; //TODO: Add API Key https://www.themoviedb.org
    private final static String SORT_TYPE_POPULAR_PARAM = "popular";
    private final static String SORT_TYPE_TOP_RATED_PARAM = "top_rated";

    private static String getMovieSortTypeString(MovieSortType movieSortType) {
        if (movieSortType == MovieSortType.POPULAR) {
            return SORT_TYPE_POPULAR_PARAM;
        } else {
            return SORT_TYPE_TOP_RATED_PARAM;
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

        Log.v(TAG, "Movie list url " + url);

        return url;
    }

    public static String buildMoviePosterUrlString(String posterPath, int itemWidth) {
        Uri builtUri = Uri.parse(MOVIE_POSTER_BASE_URL).buildUpon()
                .appendPath(getPosterWidthParam(itemWidth))
                .appendPath(posterPath)
                .build();

        String urlString = null;
        try {
            urlString = URLDecoder.decode(builtUri.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.d(TAG, "Movie poster url string exception " + e.getLocalizedMessage());
        }
        return urlString;
    }

    private static String getPosterWidthParam(int itemWidth) {
        String widthParam;
        if (itemWidth <= 92) {
            widthParam = "w92";
        } else if (itemWidth <= 154) {
            widthParam = "w154";
        } else if (itemWidth <= 185) {
            widthParam = "w185";
        } else if (itemWidth <= 342) {
            widthParam = "w342";
        } else if (itemWidth <= 500) {
            widthParam = "w500";
        } else {
            widthParam = "w780";
        }
        return widthParam;
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
