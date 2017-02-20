package com.breunig.jeff.project1.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jkbreunig on 2/2/17.
 */

public class Movie implements Parcelable {
    public int movieId;
    public String title;
    public String overview;
    public String releaseDate;
    public String userRating;
    public String posterPath;
    public boolean isFavorite;

    public Movie(JSONObject jsonObject) throws JSONException {
        this.movieId = jsonObject.getInt("id");
        this.title = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.releaseDate = jsonObject.getString("release_date");
        this.userRating = jsonObject.getString("vote_average");
        this.posterPath = jsonObject.getString("poster_path");
    }

    protected Movie(Parcel in) {
        movieId = in.readInt();
        title = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        userRating = in.readString();
        posterPath = in.readString();
        isFavorite = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(userRating);
        dest.writeString(posterPath);
        dest.writeByte((byte) (isFavorite ? 0x01 : 0x00));
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