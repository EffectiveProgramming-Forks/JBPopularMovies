package com.breunig.jeff.project1.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jkbreunig on 2/17/17.
 */

public class MovieReview {
    String author;
    String content;

    public MovieReview(JSONObject jsonObject) throws JSONException {
        this.author = jsonObject.getString("author");
        this.content = jsonObject.getString("content");
    }
}
