package com.example.soujanya.moiveapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

/**
 * Created by souji on 27/1/16.
 */
public class ReviewFragment extends Fragment {

    String movieId;
    TextView review_title;
    public TextView review_synopsis;
    String tvReview;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.reviews, null);


        review_title = (TextView) rootView.findViewById(R.id.title_review);
        review_synopsis = (TextView) rootView.findViewById(R.id.tv_review);

        if (getArguments().getString("movieId") != null)
            movieId = getArguments().getString("movieId");


        try {
            tvReview = new FetchReviews().execute(movieId).get();
        }catch(ExecutionException e){
            Log.e("",""+e);

        }catch (InterruptedException e){
            Log.e("",""+e);
        }


        review_synopsis.setText(tvReview);

        return rootView;
    }


}
