package com.codepath.apps.tweetsclient.services;

import android.content.Context;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     TweetsClient client = TweetsApplication.getRestClient();
 *     // use client to send requests to API
 *
 */
public class TweetsApplication extends com.activeandroid.app.Application {
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		TweetsApplication.context = this;
	}

	public static TweetsClient getRestClient() {
		return (TweetsClient) TweetsClient.getInstance(TweetsClient.class, TweetsApplication.context);
	}
}