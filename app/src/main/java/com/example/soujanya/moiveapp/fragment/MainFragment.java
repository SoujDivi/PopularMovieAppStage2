package com.example.soujanya.moiveapp.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.soujanya.moiveapp.MainActivity;
import com.example.soujanya.moiveapp.R;
import com.example.soujanya.moiveapp.adapter.GridViewAdapter;
import com.example.soujanya.moiveapp.modal.ImageItem;
import com.example.soujanya.moiveapp.provider.FavoritesProvider;
import com.example.soujanya.moiveapp.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by soujanya on 10/28/15.
 */
public class MainFragment extends Fragment implements Constants {

    public MainActivity mainActivity;
    private GridViewAdapter adapter;
    private GridView gridView;
    private String movieJsonStr = null;
    private ArrayList<ImageItem> imgList;
    String movieId = null;
    String typeOfSort;
    ArrayList<ImageItem> favList = new ArrayList<ImageItem>();
    ArrayList<ImageItem> unsortedlist;
    int j;
    private final String LOG_TAG = MainFragment.class.getSimpleName();

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    //private OnListItemSelectedListener listener;


    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         *
         * @param movieUri
         */
        void onItemSelected(ImageItem movieUri);
    }


    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (MainActivity) activity;

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.grid_layout, null);
        imgList = new ArrayList<ImageItem>();
        if (adapter == null) {
            FetchMoviesTask moviesTask = new FetchMoviesTask();
            moviesTask.execute();
        }


        gridView = (GridView) rootView.findViewById(R.id.grid_view);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                ImageItem item = adapter.getItem(position);


                ((Callback) getActivity())
                        .onItemSelected(item);

            }
        });
        setHasOptionsMenu(true);
        return rootView;
    }

    public void doSort(String sortBy, Cursor c) {
        if (adapter != null) {
            sortList(sortBy, c);
            adapter.setData(adapter.getData());
            adapter.notifyDataSetChanged();
        }
    }

    private void sortList(String sortBy, Cursor cur) {
        if (sortBy.equals("rating")) {
            setDataToAdapter(imgList, "rating");
            Collections.sort(imgList, new Comparator<ImageItem>() {
                @Override
                public int compare(ImageItem lhs, ImageItem rhs) {
                    Log.d(LOG_TAG, "comoparattoe" + lhs.getUserRating());

                    Float lhsRating = Float.valueOf(lhs.getUserRating().toString());

                    Float rhsRating = Float.valueOf(rhs.getUserRating().toString());

                    return rhsRating.compareTo(lhsRating);
                }

            });
        } else if (sortBy.equals("favorites")) {
            j = 0;
            favList.clear();
            if (cur != null && cur.getCount() > 0) {
                cur.moveToFirst();
                do {
                    //do logic with cursor.
                    String favId = cur.getString(cur.getColumnIndex(FavoritesProvider.name));
                    for (int i = 0; i < imgList.size(); i++) {
                        if (imgList.get(i).getMovieId().contains(favId)) {
                            favList.add(j, imgList.get(i));
                            j++;
                        } else {
                        }
                    }
                } while (cur.moveToNext());
            }
            if (favList.isEmpty()) {
                Toast.makeText(mainActivity, "No movies in Favorite", Toast.LENGTH_LONG).show();
            } else
                setDataToAdapter(favList, "fav");
        }

        else {
            setDataToAdapter(imgList, "popular");
            Collections.sort(imgList, new Comparator<ImageItem>() {
                @Override
                public int compare(ImageItem lhs, ImageItem rhs) {

                    Float lhsPopular = Float.valueOf(lhs.getPopular().toString());

                    Float rhsPopular = Float.valueOf(rhs.getPopular().toString());

                    return rhsPopular.compareTo(lhsPopular);
                }
            });
        }
    }


    public class FetchMoviesTask extends AsyncTask<Void, Void, List<ImageItem>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        private ProgressDialog dialog = new ProgressDialog(getActivity());

        List<ImageItem> result;

        public void onPreExecute() {

            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        public List<ImageItem> doInBackground(Void... params) {

            result = new ArrayList<ImageItem>();

            Log.d(LOG_TAG, "do in background ");

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            try {

                URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc");

                final String MOVIE_BASEURL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_PARAM = "sort_by";
                final String API_KEY = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASEURL).buildUpon().appendQueryParameter(SORT_PARAM, "popularity.desc")
                        .appendQueryParameter(API_KEY, "6682498650772724ef3528371f9a8a87").build();

                URL newUrl = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) newUrl.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                Log.d("inside url connect", " ");

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
                movieJsonStr = buffer.toString();


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

                Log.d("moviejson", " " + movieJsonStr);
                return getMoviesDataFromJson(movieJsonStr);
            }


        }

        private List<ImageItem> getMoviesDataFromJson(String movieJsonStr) {
            final String MOVIE_LIST = "results";
            final String POSTER_PATH = "poster_path";
            String backdropPath;

            try {
                JSONObject jsonObj = new JSONObject(movieJsonStr);
                Log.d("moviejsonstr", " " + movieJsonStr);
                JSONArray jsonArr = jsonObj.getJSONArray(MOVIE_LIST);

                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject jsonData = jsonArr.getJSONObject(i);
                    String posterPath = jsonData.getString("poster_path");
                    backdropPath = jsonData.getString("backdrop_path");

                    String movieId = jsonData.getString("id");

                    String posterUrl = "http://image.tmdb.org/t/p/w185/" + posterPath;
                    backdropPath = "http://image.tmdb.org/t/p/w185/" + backdropPath;

                    String plotSynopsis = jsonData.getString("overview");
                    String userRating = jsonData.getString("vote_average");
                    String title = jsonData.getString("original_title");
                    String releaseDate = jsonData.getString("release_date");
                    String popular = jsonData.getString("popularity");

                    result.add(new ImageItem(posterUrl, movieId, plotSynopsis, title, releaseDate, userRating, popular, backdropPath));

                }
                return result;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(List<ImageItem> result) {

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            typeOfSort = mainActivity.prefs.getString(SORT_TYPE);

            if (result != null) {


                for (ImageItem dayForecastStr : result) {

                    imgList.add(dayForecastStr);
                    unsortedlist = imgList;
                }

                adapter = new GridViewAdapter(mainActivity, imgList);
                gridView.setAdapter(adapter);


            }
        }

    }


    public void fav(Cursor cur) {

        if (cur != null && cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                //do logic with cursor.
                String favId = cur.getString(cur.getColumnIndex(FavoritesProvider.name));
                for (int i = 0; i < imgList.size(); i++) {
                    String movieId = imgList.get(i).getMovieId();

                    if (favId.equals(movieId)) {

                        favList.add(i, imgList.get(i));

                    }

                }

            } while (cur.moveToNext());

        }
    }

    public void setDataToAdapter(ArrayList<ImageItem> result, String type) {

        if (result != null) {


            adapter = new GridViewAdapter(mainActivity, result);
            gridView.setAdapter(adapter);



              }

    }
}
