package com.breunig.jeff.project1.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jkbreunig on 2/17/17.
 */

public class MovieTrailer {
    public String name;
    public String key;

    public MovieTrailer(JSONObject jsonObject) throws JSONException {
        this.name = jsonObject.getString("name");
        this.key = jsonObject.getString("key");
    }
}
