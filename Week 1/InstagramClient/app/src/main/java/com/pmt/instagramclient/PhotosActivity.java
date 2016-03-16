package com.pmt.instagramclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class PhotosActivity extends AppCompatActivity {
    public static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";
    private ArrayList<Photo> photos;
    private PhotosAdapter aPhotos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Tag", "====================test loge");
        setContentView(R.layout.activity_photos);
        //send out request popular photos
        photos = new ArrayList<>();
        aPhotos = new PhotosAdapter(this, photos);
        ListView lvPhotos = (ListView)findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(aPhotos);
        fetchPopularPhotos();
    }

    private void fetchPopularPhotos() {
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        AsyncHttpClient client  = new AsyncHttpClient();
        Log.i("tag", "url: " + url);
        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray photosJSON = null;

                try {
                    photosJSON = response.getJSONArray("data");
                    JSONObject temple = null;
                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        String type = photoJSON.getString("type");
                        if (type.equals("image")) {
                            Photo photo = new Photo();
                            temple = photoJSON.getJSONObject("user");
                            if(temple != null){
                                photo.userName = temple.getString("username");
                                photo.avatar = temple.getString("profile_picture");
                            }
                            temple = photoJSON.getJSONObject("caption");
                            if(temple != null){
                                photo.caption = temple.getString("text");
                            }
                            temple = photoJSON.getJSONObject("images");
                            if(temple != null){
                                photo.imageUrl = temple.getJSONObject("standard_resolution").getString("url");
                                photo.imageHeight = temple.getJSONObject("standard_resolution").getInt("height");
                            }temple = photoJSON.getJSONObject("likes");
                            if(temple != null){
                                photo.likesCount = temple.getInt("count");
                            }
                            photo.createTime = photoJSON.getLong("created_time");
                            photos.add(photo);
                        }
                    }

                } catch (Exception e) {
                    Log.e("ERROR", "error when fetch data:", e);
                }
                aPhotos.notifyDataSetChanged();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //DO SOMETHING
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
