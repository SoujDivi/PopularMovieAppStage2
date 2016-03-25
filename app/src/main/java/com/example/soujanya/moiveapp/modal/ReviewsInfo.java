package com.example.soujanya.moiveapp.modal;

import java.util.ArrayList;

/**
 * Created by souji on 12/1/16.
 */

public class ReviewsInfo {

    String id;
    String page;
    ArrayList<ReviewItem> results;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public ArrayList<ReviewItem> getResults() {
        return results;
    }

    public void setResults(ArrayList<ReviewItem> results) {
        this.results = results;
    }
}
