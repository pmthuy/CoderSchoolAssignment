package com.codepath.apps.tweetsclient.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.tweetsclient.R;
import com.codepath.apps.tweetsclient.dialogs.ComposeDialog;
import com.codepath.apps.tweetsclient.models.Tweet;
import com.codepath.apps.tweetsclient.models.User;
import com.codepath.apps.tweetsclient.services.EndlessScrollListener;
import com.codepath.apps.tweetsclient.services.TweetsApplication;
import com.codepath.apps.tweetsclient.services.TweetsArrayAdapter;
import com.codepath.apps.tweetsclient.services.TweetsClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimelineActivity extends AppCompatActivity implements ITwitterListener {
    private TweetsClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;
    private User myInfo;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnCompose:
                FragmentManager fm = getSupportFragmentManager();
                ComposeDialog composeDialog = ComposeDialog.newInstance(myInfo, "",0);
                composeDialog.show(fm, "fragment_compose");
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        client = TweetsApplication.getRestClient();
        getOwnerInfo();

        lvTweets = (ListView) findViewById(R.id.lvTweets);
        tweets = new ArrayList<>();
        aTweets = new TweetsArrayAdapter(getApplicationContext(), this, tweets);
        lvTweets.setAdapter(aTweets);


        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromApi();
                return true;
            }
        });

        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        populateTimeline(1);

    }

    @Override
    public void onReply(int position) {
        Tweet tweet = tweets.get(position);
        FragmentManager fm = getSupportFragmentManager();
        ComposeDialog composeDialog = ComposeDialog.newInstance(myInfo, tweet.user.getScreenName(), tweet.id);
        composeDialog.show(fm, "fragment_compose");
    }

    // Append more data into the adapter
    public void customLoadMoreDataFromApi() {
        if (tweets.size() > 0) {
            Tweet tweet = tweets.get(tweets.size() - 1);
            long lastId = tweet.id;
            populateTimeline(lastId);
        } else {
            populateTimeline(1);
        }
    }

    private void populateTimeline(long lastId) {
        client.getHomeTimeline(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {
//                Toast.makeText(getApplicationContext(), json.toString(), Toast.LENGTH_LONG);
                Log.d("DEBUG", "onSuccess" + json.toString());
                ArrayList<Tweet> array = Tweet.fromJSONArray(json);
                aTweets.addAll(array);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getApplicationContext(), "getHomeTimeLine fail" + errorResponse.toString(), Toast.LENGTH_LONG).show();
                Log.d("DEBUG", "onFailure" + errorResponse.toString());
            }
        }, lastId);
    }

    private void getOwnerInfo() {
        client.getOwnerInfo(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                Log.d("DEBUG", "getOwnerInfo onSuccess" + json.toString());
                myInfo = User.fromJSON(json);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", "getOwnerInfo onFailure" + errorResponse.toString());
            }
        });
    }

}
