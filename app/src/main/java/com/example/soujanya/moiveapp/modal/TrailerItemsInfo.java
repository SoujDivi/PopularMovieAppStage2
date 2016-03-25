package com.example.soujanya.moiveapp.modal;

import java.util.ArrayList;

/**
 * Created by souji on 12/12/15.
 */
public class TrailerItemsInfo {

    String id;
    ArrayList<TrailerItem> results;

    public ArrayList<TrailerItem> getResults() {
        return results;
    }

    public void setResults(ArrayList<TrailerItem> results) {
        this.results = results;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
