package com.codepath.apps.tweetsclient.services;

import android.content.Context;

import com.codepath.apps.tweetsclient.utils.Utils;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TweetsClient extends OAuthBaseClient {
	public static final Class<? extends Api> TWITTER_API_CLASS = TwitterApi.class; // Change this
	public static final String TWITTER_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String TWITTER_CONSUMER_KEY = "eRMikpAXBCmSkz5IWuYX3ogME";       // Change this
	public static final String TWITTER_CONSUMER_SECRET = "pwtIse6DspUu5BGu5xAUvRGIScoFEP2r9vcL9k62tpKvpZklZ6"; // Change this
	public static final String TWITTER_CALLBACK_URL = "oauth://cptweetsclient"; // Change this (here and in manifest)

	public TweetsClient(Context context) {
		super(context, TWITTER_API_CLASS, TWITTER_URL, TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET, TWITTER_CALLBACK_URL);
	}

	public void getHomeTimeline(AsyncHttpResponseHandler handler, long lastId){
		String apiURL = getApiUrl("statuses/home_timeline.json");
//		String apiURL = getApiUrl("statuses/user_timeline.json");

		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("since_id", lastId);
		getClient().get(apiURL, params, handler);
	}

	public void tweetting(String status, AsyncHttpResponseHandler handler){
		String apiURL = getApiUrl("statuses/update.json");
		String statusEncode = Utils.URLEncode(status);
		RequestParams params = new RequestParams();
		params.put("status", statusEncode);
		getClient().post(apiURL, params, handler);
	}

	public void reTweet(String status, long statusId,  AsyncHttpResponseHandler handler){
		String apiURL = getApiUrl("statuses/update.json");
		String statusEncode = Utils.URLEncode(status);
		RequestParams params = new RequestParams();
		params.put("status", statusEncode);
		params.put("in_reply_to_status_id", statusId);
		getClient().post(apiURL, params, handler);
	}

	public void getOwnerInfo(AsyncHttpResponseHandler handler){
		String apiURL = getApiUrl("account/verify_credentials.json");
		RequestParams params = new RequestParams();
		getClient().get(apiURL, params, handler);
	}
	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}