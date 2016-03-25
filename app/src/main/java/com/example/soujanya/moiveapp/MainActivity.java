package com.example.soujanya.moiveapp;


import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;

import com.example.soujanya.moiveapp.fragment.DetailFragment;
import com.example.soujanya.moiveapp.fragment.MainFragment;
import com.example.soujanya.moiveapp.modal.ImageItem;
import com.example.soujanya.moiveapp.provider.FavoritesProvider;
import com.example.soujanya.moiveapp.utils.AppPreferences;
import com.example.soujanya.moiveapp.utils.Constants;

public class MainActivity extends AppCompatActivity implements Constants,MainFragment.Callback{


    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private MenuItem selectedContextItem;
    public AppPreferences prefs;
    private String sortData = "";
    public MainFragment mainFragment;
    CursorLoader cursorLoader;
    Toolbar mToolbar;
    boolean mTwoPane;
    private boolean isTwoPane = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);



        if (findViewById(R.id.container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new DetailFragment(), "Detail")
                        .commit();
            }



        } else {
            mTwoPane = false;
        }

         mainFragment = (MainFragment) fm.findFragmentByTag("List");
        if ( mainFragment == null) {
            mainFragment = new MainFragment();

            fm.beginTransaction().replace(R.id.fragment_container, mainFragment, "List").commit();
        }


        prefs = new AppPreferences(getApplicationContext());

    }



    public void onItemSelected(ImageItem item) {

        Bundle arguments = new Bundle();
        String movieId = item.getMovieId();
        String backgroundImg = item.getBackdropPath();
        String url = item.getImageUrl();
        String overview = item.getPlotSynopsis();
        String releaseDate = item.getReleaseDate();
        String rating = item.getUserRating();
        String title = item.getTitle();

        arguments.putString("movieId", movieId);
        arguments.putString("backgroundImg", backgroundImg);
        arguments.putString("url", url);
        arguments.putString("overview", overview);
        arguments.putString("releaseDate", releaseDate);
        arguments.putString("rating", rating);
        arguments.putString("title", title);


        if (mTwoPane) {

            arguments.putString("from", "tablet");

            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(arguments);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, detailFragment, "Detail")
                        .commit();


        } else {
            Intent detailintent = new Intent(this, DetailActivity.class);
            arguments.putString("from", "mobile");
            detailintent.putExtras(arguments);
            startActivity(detailintent);
        }

    }




    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        menu.addSubMenu(0, 0, Menu.NONE, R.string.sort_by_popular);
        menu.addSubMenu(0, 1, Menu.NONE, R.string.sort_by_rate);
        menu.addSubMenu(0, 2, Menu.NONE, R.string.favorites);


        if (sortData.equals("")) {
            selectedContextItem = menu.findItem(0);
        } else if (sortData.equals(SORT_RATED)) {
            selectedContextItem = menu.findItem(1);
        } else if (sortData.equals(FAVORITES)) {
            selectedContextItem = menu.findItem(2);
        }
        makeItemColored(selectedContextItem, Color.RED);

        return true;
    }

    public void makeItemColored(MenuItem item, int color) {
        if (item != null) {
            String title = item.getTitle().toString();
            Spannable newTitle = new SpannableString(title);
            newTitle.setSpan(new ForegroundColorSpan(color), 0, newTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            item.setTitle(newTitle);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (item.getItemId() != 3) {
            if (selectedContextItem != null) {
                if (selectedContextItem.getItemId() != item.getItemId()) {
                    makeItemColored(item, Color.RED);
                    makeItemColored(selectedContextItem, Color.BLACK);

                }
            } else {
                makeItemColored(item, Color.RED);

            }

            selectedContextItem = item;
        }


        switch (id) {
            case 0:
                sortData = "";
                prefs.addString(SORT_TYPE, "popular");
                mainFragment.doSort("popular", null);


                return true;
            case 1:
                sortData = SORT_RATED;
                prefs.addString(SORT_TYPE, "rating");
                mainFragment.doSort("rating", null);

                return true;

            case 2:
                sortData = FAVORITES;
                prefs.addString(SORT_TYPE, "favorites");

                String URL = "content://com.example.soujanya.movieapp.provider.FavoritesProvider/cte";

                Uri favorites = Uri.parse(URL);
                ContentResolver cr = getContentResolver();
                Cursor c = cr.query(favorites, null, null, null, FavoritesProvider.name);


                if (c != null && c.getCount() > 0) {
                    c.moveToFirst();
                    do {
                        String favId = c.getString(c.getColumnIndex(FavoritesProvider.name));

                    } while (c.moveToNext());
                }
                mainFragment.doSort("favorites", c);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed()
    {

        super.onBackPressed();
    }



}
