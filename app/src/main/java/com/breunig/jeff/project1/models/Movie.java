package com.breunig.jeff.project1.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jkbreunig on 2/2/17.
 */

public class Movie {
    public String mTitle;
    public String mOverview;
    public String mReleaseDate;
    public String mVoteAverage;
    public String mPosterPath;

    public Movie(JSONObject jsonObject) throws JSONException {
        this.mTitle = jsonObject.getString("title");
        this.mOverview = jsonObject.getString("overview");
        this.mReleaseDate = jsonObject.getString("release_date");
        this.mVoteAverage = jsonObject.getString("vote_average");
    }

}
