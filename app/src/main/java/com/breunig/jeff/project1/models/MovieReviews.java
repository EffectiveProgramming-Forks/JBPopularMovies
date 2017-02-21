package com.breunig.jeff.project1.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jkbreunig on 2/18/17.
 */

public class MovieReviews implements Parcelable {
    public int movieId;
    public ArrayList<MovieReview> results;
    private int page = 1;
    private int total_pages = 1;

    public MovieReviews(int movieId) {
        this.movieId = movieId;
    }

    public MovieReviews(ArrayList<MovieReview> results, int page, int totalPages) {
        this.results = results;
        this.page = page;
        this.total_pages = totalPages;
    }

    public void updatePageResults(MovieReviews movieReviews) {
        if (movieReviews == null) {
            return;
        }
        if (this.results == null) {
            this.results = movieReviews.results;
        } else {
            Collections.addAll(movieReviews.results);
        }
        page = movieReviews.page;
        total_pages = movieReviews.total_pages;
    }

    public boolean isMoreContent() {
        return page <= total_pages;
    }

    protected MovieReviews(Parcel in) {
        movieId = in.readInt();
        if (in.readByte() == 0x01) {
            results = new ArrayList<MovieReview>();
            in.readList(results, MovieReview.class.getClassLoader());
        } else {
            results = null;
        }
        page = in.readInt();
        total_pages = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        if (results == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(results);
        }
        dest.writeInt(page);
        dest.writeInt(total_pages);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieReviews> CREATOR = new Parcelable.Creator<MovieReviews>() {
        @Override
        public MovieReviews createFromParcel(Parcel in) {
            return new MovieReviews(in);
        }

        @Override
        public MovieReviews[] newArray(int size) {
            return new MovieReviews[size];
        }
    };
}
