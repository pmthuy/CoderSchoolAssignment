package com.codepath.apps.tweetsclient.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweetsclient.R;
import com.codepath.apps.tweetsclient.activities.ITwitterListener;
import com.codepath.apps.tweetsclient.models.Tweet;
import com.codepath.apps.tweetsclient.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 27/03/2016.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {
    private ITwitterListener mListener;



    public TweetsArrayAdapter(Context context,ITwitterListener listener, List<Tweet> tweets) {
        super(context, 0, tweets);
        mListener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Tweet tweet = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }
        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        TextView tvRetweet = (TextView) convertView.findViewById(R.id.tvRetweetCount);
        TextView tvLike = (TextView) convertView.findViewById(R.id.tvLikeCount);
        TextView tvRetweeted = (TextView) convertView.findViewById(R.id.tvRetweet);
        ImageView ivReply = (ImageView) convertView.findViewById(R.id.ivReply);
        ImageView ivMedia = (ImageView) convertView.findViewById(R.id.ivMedia);


        String retweet = "";
        if (tweet.retweet) {
            retweet = tweet.retweetUser.getName() + " Retweeted";
            tvRetweeted.setVisibility(View.GONE);
        }else{
            tvRetweeted.setVisibility(View.VISIBLE);
        }
        tvRetweeted.setText(retweet);
        tvUserName.setText(tweet.user.getName());
        tvBody.setText(tweet.body);
        tvName.setText("@" + tweet.user.getScreenName());
        tvRetweet.setText(String.valueOf(tweet.retweetCount));
        tvLike.setText(String.valueOf(tweet.favoriteCount));
        tvTime.setText(Utils.getTimeString(tweet.createAt));
        ivProfileImage.setImageResource(0);
        Picasso.with(getContext()).load(tweet.user.getProfileImageUrl()).into(ivProfileImage);
        String imageURL = "";
        try {
            imageURL = tweet.entities.getMedia().get(0).getUrl();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(imageURL != null && !imageURL.isEmpty()){
            Picasso.with(getContext()).load(imageURL).noPlaceholder().into(ivMedia);
        }else{
            ivMedia.setVisibility(View.GONE);
        }
        ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onReply(position);
            }
        });

        return convertView;
    }
}
