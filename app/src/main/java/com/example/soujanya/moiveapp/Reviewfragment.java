package com.example.soujanya.moiveapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


class FetchReviews extends AsyncTask<String, Void, String> {

    String reviews;

    protected String doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String id = params[0];

        try {


            URL url = new URL("http://api.themoviedb.org/3/movie/" + id + "/reviews");

            final String MOVIE_BASEURL = "http://api.themoviedb.org/3/movie/" + id + "/reviews";
            final String API_KEY = "api_key";

            Uri builtUri = Uri.parse(MOVIE_BASEURL).buildUpon().
                    appendQueryParameter(API_KEY, "6682498650772724ef3528371f9a8a87").build();

            URL newUrl = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) newUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {

                return null;
            }
            reviews = buffer.toString();

        } catch (IOException e) {
            Log.e("Reviews", "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("Reviews", "Error closing stream", e);
                }
            }

        }
        return getReviews(reviews);
    }


    public String getReviews(String reviewJson) {

        String review = null;

        try {
            JSONObject jsonObj = new JSONObject(reviewJson);
            JSONArray jsonArr = jsonObj.getJSONArray("results");

            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject jsonData = jsonArr.getJSONObject(i);
                String author = jsonData.getString("author");
                review = jsonData.getString("content");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return review;
    }




}
