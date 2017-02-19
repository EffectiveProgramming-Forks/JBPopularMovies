package com.breunig.jeff.project1.models;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jkbreunig on 2/18/17.
 */

public class MovieReviews {
    public ArrayList<MovieReview> results;
    private int page = 1;
    private int total_pages = 1;

    public MovieReviews() {

    }

    public MovieReviews(ArrayList<MovieReview> results, int page, int totalPages) {
        this.results = results;
        this.page = page;
        this.total_pages = totalPages;
    }

    public void updatePageResults(MovieReviews movieReviews) {
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
}
