package com.codepath.apps.tweetsclient.models;

import android.util.Log;

import com.codepath.apps.tweetsclient.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by thuypm on 27/03/2016.
 */
public class Tweet {

    public long id = 1;
    public String body = "";
    public long uid = 0;
    public User user = new User();
    public String createAt = "";
    public int retweetCount = 0;
    public int favoriteCount = 0;
    public boolean retweet = false;
    public User retweetUser = new User();
    public Entities entities = new Entities();

    public Tweet(){

    }
    public static Tweet fromJSON(JSONObject jsonObject){
        boolean retweeted = false;
        JSONObject jo = null;
        Tweet tweet = new Tweet();
        try{
            tweet.id = jsonObject.getLong("id");
            retweeted = jsonObject.getBoolean("retweeted");
            tweet.retweet = retweeted;
            if(retweeted){
                tweet.retweetUser = User.fromJSON(jsonObject.getJSONObject("user"));
                jo = jsonObject.getJSONObject("retweeted_status");
            }else{
                jo = jsonObject;
            }

            tweet.body = Utils.URLDecode(jo.getString("text"));
            tweet.uid = jo.getLong("id");
            tweet.createAt = jo.getString("created_at");
            tweet.user = User.fromJSON(jo.getJSONObject("user"));
            tweet.retweetCount = jo.getInt("retweet_count");
            tweet.favoriteCount = jo.getInt("favorite_count");
            tweet.entities = new Gson().fromJson(jo.getJSONObject("entities").toString(), Entities.class);
        }catch (JSONException e){
            e.printStackTrace();
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray json){
        ArrayList<Tweet> tweets = new ArrayList<>();
        Tweet tweet;
        for(int i = 0; i < json.length(); i++){
            try {
                JSONObject tweetJson = json.getJSONObject(i);
                Log.d("DEBUG", "tweetJson " + i + "____" + tweetJson.toString());
                tweet = Tweet.fromJSON(tweetJson);
                tweets.add(tweet);
            }catch (JSONException e){
                e.printStackTrace();
                continue;
            }

        }
        return tweets;
    }

}

