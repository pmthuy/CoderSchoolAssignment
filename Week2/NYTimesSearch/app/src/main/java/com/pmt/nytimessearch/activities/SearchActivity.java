package com.pmt.nytimessearch.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pmt.nytimessearch.R;
import com.pmt.nytimessearch.adapters.ArticleArrayAdapter;
import com.pmt.nytimessearch.listeners.EndlessRecyclerViewScrollListener;
import com.pmt.nytimessearch.models.Article;
import com.pmt.nytimessearch.models.SettingData;
import com.pmt.nytimessearch.utils.Constants;
import com.pmt.nytimessearch.utils.ItemClickSupport;
import com.pmt.nytimessearch.utils.NYTimeHTTPClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    public static SettingData settingData = new SettingData();
    private String query = "";
    RecyclerView rvResults;

    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        Toast.makeText(this, toolbar.getMenu().getItem(0).getTitle(), Toast.LENGTH_SHORT);
        setupView();
        if(!isOnline()){
            Toast.makeText(this.getApplicationContext(), "Internet connecttion error, please reconnect to use app", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        shareItem.setVisible(false);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(true);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryStr) {
                query = queryStr;
                articles.clear();
                adapter.notifyDataSetChanged();
                customLoadMoreDataFromApi(0);
                hideSoftKeyboard();
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private void setupView() {
        rvResults = (RecyclerView) findViewById(R.id.rvResults);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(articles);
        rvResults.setAdapter(adapter);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        rvResults.setLayoutManager(gridLayoutManager);

        rvResults.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromApi(page);
            }
        });
        ItemClickSupport.addTo(rvResults).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
                        Article article = articles.get(position);
                        i.putExtra("article", article);
                        startActivity(i);
                    }
                }
        );
    }

    public void customLoadMoreDataFromApi(int page) {
        if(query.isEmpty()){
            Toast.makeText(this,"Please enter search query",Toast.LENGTH_SHORT);
            return;
        }
        RequestParams parram = new RequestParams();
        parram.put("api-key","22a31c8026412655d506bcc55a419888:9:74744585");
        parram.put("page", page);
        parram.put("q", query);
        if(settingData.beginDate != null){
            String stringDate = Constants.DATE_FORMAT_SEND_API.format(settingData.beginDate.getTime());
            parram.put("begin_date", stringDate);
        }
        if(settingData.endDate != null){
            String stringDate = Constants.DATE_FORMAT_SEND_API.format(settingData.endDate.getTime());
            parram.put("end_date", stringDate);
        }
        if(settingData.sortType.equalsIgnoreCase(Constants.SORT_TYPE_OLD)){
            parram.put("sort", "oldest");
        }
        if(settingData.newsDeskValues != null){
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i< settingData.newsDeskValues.size(); i++){
                if(sb.length() > 0){
                    sb.append(" ");
                }
                sb.append("\"").append(settingData.newsDeskValues.get(i)).append("\"");
            }
            if(sb.length()>0){
                String fillter = "news_desk:(" + sb.toString() + ")";
                parram.put("fq", fillter);
            }
        }
        try {
            NYTimeHTTPClient.get(parram, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("DEBUG", response.toString());
                    JSONArray acticleJsonResults = null;
                    try {
                        acticleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                        articles.addAll(Article.fromJsonArray(acticleJsonResults));
                        int curSize = adapter.getItemCount();
                        adapter.notifyItemRangeInserted(curSize, articles.size() - 1);
                        Log.d("DEBUG", acticleJsonResults.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            Log.e("ERROR", "Error when get article search", e);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.d("DEBUG", "onSetting");
            Intent i = new Intent(this, SettingActivity.class);
            i.putExtra("SettingData", settingData);
            startActivityForResult(i, 200);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200) {
            if(resultCode == Activity.RESULT_OK){
                this.settingData = (SettingData)data.getSerializableExtra("SettingData");
            }
        }
    }//onActivityResult
}
