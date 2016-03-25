package com.example.soujanya.moiveapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.soujanya.moiveapp.DetailActivity;
import com.example.soujanya.moiveapp.MainActivity;
import com.example.soujanya.moiveapp.R;
import com.example.soujanya.moiveapp.modal.TrailerItem;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by souji on 12/12/15.
 */
public class TrailerAdapter extends ArrayAdapter<TrailerItem>{

    private ArrayList<TrailerItem> items;
    private LayoutInflater inflater;
  // private DetailActivity context;
    private YouTubePlayerView youtubeplayerview;
    ImageView play_btn;
    private TextView trailer_title;
    TrailerItem item;
   public MainActivity mContext;
    public DetailActivity dContext;
    Context context;
    public String tab;


    public TrailerAdapter(Context context, ArrayList<TrailerItem> items,String from) {
        super(context, 0, items);
        tab = from;
        if(from=="tablet") {
            mContext =(MainActivity) context;
        }
        else {
            dContext = (DetailActivity) context;
        }
        this.items = items;
        Log.d("Items in TrailerAdapter", " "+ items.get(0));

        inflater = LayoutInflater.from(context);
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;




          item = items.get(position);
        Log.d("Trailer adapter", "Pos" + items.get(position));

           if (convertView == null) {

              holder = new ViewHolder();

            convertView = inflater.inflate(R.layout.trailer_item, parent, false);
               holder.play_btn = (ImageView) convertView.findViewById(R.id.video_view);
               holder.trailer_title = (TextView) convertView.findViewById(R.id.trailer_title);
               convertView.setTag(holder);

        } else {
               holder = (ViewHolder) convertView.getTag();
        }

        String img_url="http://img.youtube.com/vi/"+item.getKey()+"/0.jpg";



        Uri thumbnail = Uri.parse(img_url);

        Log.d("thumbnailll"," "+thumbnail);

     //   holder.play_btn.setImageResource(R.drawable.play);

       //holder.play_btn.setImageURI(thumbnail);

        Picasso.with(mContext).load(thumbnail).placeholder(R.drawable.play).into(holder.play_btn, new Callback() {


            public void onSuccess() {
                // TODO Auto-generated method stub

            }


            public void onError() {
                // TODO Auto-generated method stub

            }
        });


        holder.trailer_title.setText("Trailer"+(position+1));
       // youtubeplayerview.initialize("AIzaSyAHx8rEiZAuUqZ7KTBJDx9ArmJ90QP2Dls", this);

        /*videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent videoIntent = YouTubeStandalonePlayer.createVideoIntent(this, "AIzaSyAHx8rEiZAuUqZ7KTBJDx9ArmJ90QP2Dls", item.getKey(), 0, true, false);

                StartActivity(videoIntent, 1);
            }
        });*/

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tab == "tablet"){
                    Log.d("tablet","true"+tab);
                    Intent intent = YouTubeStandalonePlayer.createVideoIntent(mContext, "AIzaSyAHx8rEiZAuUqZ7KTBJDx9ArmJ90QP2Dls", item.getKey(), 0, true, true);
                mContext.startActivity(intent);
            }else{
                    Log.d("mobile","true"+tab);
                    Intent intent = YouTubeStandalonePlayer.createVideoIntent(dContext, "AIzaSyAHx8rEiZAuUqZ7KTBJDx9ArmJ90QP2Dls", item.getKey(), 0, true, true);
                    dContext.startActivity(intent);
                }




              //  getContext().startActivity(YouTubeIntents.createPlayVideoIntent(this,"AIzaSyAHx8rEiZAuUqZ7KTBJDx9ArmJ90QP2Dls", item.getKey()));
            }
        });





     //   Uri video = Uri.parse("http://www.youtube.com/watchv="+item.getKey());

      //  Log.d("vidoeeeeeeeee"," "+video);
     //   videoView.setVideoURI(video);

        return  convertView;
    }

    private class ViewHolder {
        ImageView play_btn;
        TextView trailer_title;

    }


}
