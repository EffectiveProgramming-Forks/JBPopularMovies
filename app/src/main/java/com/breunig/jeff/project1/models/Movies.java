package com.breunig.jeff.project1.models;

import java.util.ArrayList;

/**
 * Created by jkbreunig on 2/22/17.
 */

public class Movies {
    public ArrayList<Movie> results;
    private int page = 1;
    private int total_pages = 1;
    public boolean isLoading;
    public int getPage() {
        return page;
    }

    public Movies() {

    }

    public Movies(ArrayList<Movie> results, int page, int totalPages) {
        this.results = results;
        this.page = page;
        this.total_pages = totalPages;
    }

    public void updatePageResults(Movies movies) {
        if (movies == null) {
            return;
        }
        if (this.results == null) {
            this.results = movies.results;
        } else {
            if (movies.results != null) {
                results.addAll(movies.results);
            }
        }
        page = movies.page + 1;
        total_pages = movies.total_pages;
    }

    public boolean isMoreContent() {

        return page <= total_pages;
    }
}
