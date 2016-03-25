package com.example.soujanya.moiveapp.asyncTask;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.soujanya.moiveapp.fragment.DetailFragment;
import com.example.soujanya.moiveapp.modal.TrailerItem;
import com.example.soujanya.moiveapp.modal.TrailerItemsInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by souji on 12/12/15.
 */
public class FetchTrailersTask extends AsyncTask<String, Void, ArrayList<TrailerItem>> {

    private String trailerJsonStr = null;
    public DetailFragment context;
    public ProgressDialog dialog;

    private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();

    public FetchTrailersTask(DetailFragment context) {

        this.context = context;

    }


    @Override
    protected ArrayList<TrailerItem> doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String id = params[0];

        try {


            URL url = new URL("http://api.themoviedb.org/3/movie/" + id + "/videos");

            final String MOVIE_BASEURL = "http://api.themoviedb.org/3/movie/" + id + "/videos";
            final String API_KEY = "api_key";

            Uri builtUri = Uri.parse(MOVIE_BASEURL).buildUpon().
                    appendQueryParameter(API_KEY, "api_key").build();

            URL newUrl = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) newUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
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
            trailerJsonStr = buffer.toString();
            Log.d(LOG_TAG, " ======" + trailerJsonStr);

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }

            return getTrailerDataFromJson(trailerJsonStr);
        }

    }

    private ArrayList<TrailerItem> getTrailerDataFromJson(String trailerJsonStr) {

        Type trailerItemsList = new TypeToken<TrailerItemsInfo>() {
        }.getType();
        TrailerItemsInfo trailers = new Gson().fromJson(trailerJsonStr, trailerItemsList);

        ArrayList<TrailerItem> results = trailers.getResults();

        return results;

    }

    protected void onPostExecute(ArrayList<TrailerItem> result) {

        context.setDataToAdapter(result);

    }
}
