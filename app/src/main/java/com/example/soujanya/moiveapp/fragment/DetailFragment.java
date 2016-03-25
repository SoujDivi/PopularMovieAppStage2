package com.example.soujanya.moiveapp.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soujanya.moiveapp.DetailActivity;
import com.example.soujanya.moiveapp.MainActivity;
import com.example.soujanya.moiveapp.R;
import com.example.soujanya.moiveapp.ReviewFragment;
import com.example.soujanya.moiveapp.adapter.TrailerAdapter;
import com.example.soujanya.moiveapp.asyncTask.FetchTrailersTask;
import com.example.soujanya.moiveapp.modal.TrailerItem;
import com.example.soujanya.moiveapp.provider.FavoritesProvider;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by souji on 9/1/16.
 */
public class DetailFragment extends Fragment {


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
    TrailerAdapter adapter = null;

    private final String LOG_TAG = DetailFragment.class.getSimpleName();
    public DetailActivity detailActivity;
    public MainActivity mainActivity;
    String from = null;



 public void onAttach(Activity activity) {
     super.onAttach(activity);
     if (getArguments() != null) {
          from = getArguments().getString("from");

         if (from=="tablet")
             mainActivity = (MainActivity) activity;
         else
             detailActivity = (DetailActivity) activity;

     }
 }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_detail, null);


        setHasOptionsMenu(true);



        final ImageView img = (ImageView) rootView.findViewById(R.id.img);
        TextView tv_title = (TextView) rootView.findViewById(R.id.title);
        TextView tv_overView = (TextView) rootView.findViewById(R.id.tv_synopsis);
        TextView tv_releaseDate = (TextView) rootView.findViewById(R.id.releaseDate);
        TextView tv_rating = (TextView) rootView.findViewById(R.id.rating);
        favorite = (ImageView) rootView.findViewById(R.id.favorite);
        trailersList = (ListView) rootView.findViewById(R.id.lv_trailers);


        if(getArguments()!=null) {
            movieId = getArguments().getString("movieId");
            backgroundImg = getArguments().getString("backgroundImg");
            url = getArguments().getString("url");
            overview = getArguments().getString("overview");
            title = getArguments().getString("title");
            releaseDate = getArguments().getString("releaseDate");
            rating = getArguments().getString("rating");

            FetchTrailersTask trailersTask = new FetchTrailersTask(this);
            trailersTask.execute(movieId);

        }


        Picasso
                .with(getActivity())
                .load(backgroundImg)
                .into(img, new Callback() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onError() {
                        // TODO Auto-generated method stub

                    }
                });

        tv_overView.setText(overview);
        tv_rating.setText(rating);
        tv_releaseDate.setText(releaseDate);
        tv_title.setText(title);



        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                favorite.setImageResource(R.drawable.favorite_color);

                ContentValues values = new ContentValues();
                values.put(FavoritesProvider.name, movieId);

                Uri uri = getActivity().getContentResolver().insert(FavoritesProvider.CONTENT_URI, values);


                Toast.makeText(getActivity(), title + " is added to favorites", Toast.LENGTH_LONG).show();
            }
        });


        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {


        inflater.inflate(R.menu.menu_detail, menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==android.R.id.home){
            NavUtils.navigateUpFromSameTask(getActivity());
        }

        switch (id){
            case R.id.share :
                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");

                if(title != null && trailerUrl!=null ) {
                    share.putExtra(Intent.EXTRA_SUBJECT, title + " " + "Trailer");
                    share.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=" + trailerUrl.get(0).getKey());

                    startActivity(Intent.createChooser(share, "Share link!"));
                }
                break;

            case R.id.review:
                FragmentManager fragmentManager;

                if(from!=null) {
                    if (from == "tablet")
                        fragmentManager = mainActivity.getSupportFragmentManager();
                    else
                        fragmentManager = detailActivity.getSupportFragmentManager();

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null);
                    ReviewFragment reviewFragment = new ReviewFragment();
                    Bundle args = new Bundle();
                    args.putString("movieId", movieId);
                    reviewFragment.setArguments(args);
                    fragmentTransaction.replace(R.id.container, reviewFragment).commit();
                }

        }

        return super.onOptionsItemSelected(item);
    }


    public void setDataToAdapter(ArrayList<TrailerItem> result){

        trailerUrl = result;

        if(result!= null) {
            if(from == "tablet")
                adapter = new TrailerAdapter(mainActivity, result, "tablet");
            else
                adapter = new TrailerAdapter(detailActivity, result, "mobile");

                trailersList.setAdapter(adapter);



        }

    }

    }
