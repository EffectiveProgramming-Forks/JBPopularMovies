package com.breunig.jeff.project1.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

/**
 * Created by jkbreunig on 2/17/17.
 */

public class MovieReview implements Parcelable {
    public String author;
    public String content;

    public MovieReview(JSONObject jsonObject) throws JSONException {
        this.author = jsonObject.getString("author");
        this.content = Jsoup.parse(jsonObject.getString("content")).text();
    }

    protected MovieReview(Parcel in) {
        author = in.readString();
        content = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieReview> CREATOR = new Parcelable.Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel in) {
            return new MovieReview(in);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };
}
