package com.breunig.jeff.project1.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jkbreunig on 2/2/17.
 */

public class Movie implements Parcelable {
    public String title;
    public String overview;
    public String releaseDate;
    public String userRating;
    public String posterPath;

    public Movie(JSONObject jsonObject) throws JSONException {
        this.title = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.releaseDate = jsonObject.getString("release_date");
        this.userRating = jsonObject.getString("vote_average");
        this.posterPath = jsonObject.getString("poster_path");
    }

    protected Movie(Parcel in) {
        title = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        userRating = in.readString();
        posterPath = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(userRating);
        dest.writeString(posterPath);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
