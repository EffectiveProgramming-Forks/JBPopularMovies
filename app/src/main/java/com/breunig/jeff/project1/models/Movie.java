package com.breunig.jeff.project1.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by jkbreunig on 2/2/17.
 */

public class Movie implements Serializable {
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

}
