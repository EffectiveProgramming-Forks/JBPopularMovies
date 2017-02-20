package com.breunig.jeff.project1.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.breunig.jeff.project1.database.MovieContract.MovieEntry;
import com.breunig.jeff.project1.models.Movie;

/**
 * Created by jkbreunig on 2/19/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";

    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIE_TABLE =

                "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +

                        MovieEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        MovieEntry.COLUMN_MOVIE_ID       + " INTEGER NOT NULL, "  +
                        MovieEntry.COLUMN_OVERVIEW   + " REAL NOT NULL, "       +
                        MovieEntry.COLUMN_RELEASE_DATE   + " REAL NOT NULL, "   +
                        MovieEntry.COLUMN_USER_RATING   + " REAL NOT NULL, "    +
                        MovieEntry.COLUMN_POSTER_PATH   + " REAL NOT NULL, "    +

                        " UNIQUE (" + MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public static boolean insert(Context context, Movie movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieEntry.COLUMN_MOVIE_ID, movie.movieId);
        contentValues.put(MovieEntry.COLUMN_OVERVIEW, movie.overview);
        contentValues.put(MovieEntry.COLUMN_RELEASE_DATE, movie.releaseDate);
        contentValues.put(MovieEntry.COLUMN_USER_RATING, movie.userRating);
        contentValues.put(MovieEntry.COLUMN_POSTER_PATH, movie.posterPath);
        Uri uri = context.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
        return uri != null;
    }

    public static boolean delete(Context context, int movieId) {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(String.valueOf(movieId)).build();
        int value = context.getContentResolver().delete(uri, null, null);
        return value != 0;
    }

    public static boolean query(Context context, int movieId) {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(String.valueOf(movieId)).build();
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        return (cursor != null && cursor.getCount() > 0) ? true : false;
    }
}