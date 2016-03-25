package com.example.soujanya.moiveapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.soujanya.moiveapp.fragment.DetailFragment;
import com.example.soujanya.moiveapp.modal.TrailerItem;

import java.util.ArrayList;

/**
 * Created by soujanya on 10/29/15.
 */
public class DetailActivity extends AppCompatActivity {


    String url;
    String overview;
    String title;
    String releaseDate;
    String rating;
    ImageView favorite;
    ListView trailersList;
    String movieId;
    String backgroundImg;
    ArrayList<TrailerItem> trailerUrl;
   public DetailFragment detailFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if(this.getIntent()!=null)
        {

            detailFragment = new DetailFragment();
            detailFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, detailFragment)
                    .commit();
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


}

