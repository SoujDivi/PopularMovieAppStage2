package com.example.soujanya.moiveapp.modal;

import java.util.ArrayList;

/**
 * Created by soujanya on 11/3/15.
 */
public class ItemsInfo {

    String page;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public ArrayList<ImageItem> getResults() {
        return results;
    }

    public void setResults(ArrayList<ImageItem> results) {
        this.results = results;
    }


    ArrayList<ImageItem> results;
}
