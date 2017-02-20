package com.breunig.jeff.project1.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.breunig.jeff.project1.database.MovieContract.MovieEntry;

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
}