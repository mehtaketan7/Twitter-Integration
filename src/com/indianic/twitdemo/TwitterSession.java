package com.indianic.twitdemo;

import twitter4j.http.AccessToken;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class TwitterSession {
	private SharedPreferences sharedPref;
	private Editor editor;
	private static final String SHARED = "Twitter_Preferences";
	private static final String TWEET_AUTH_KEY = "250099150-DFVEJXanDkAbUoX4TgZ4Gk5tE3MO99R2WiH6Eb2x";
	private static final String TWEET_AUTH_SECRET_KEY = "BYSSlnujS0Feh00NSzz5ZaFIDjnmflEIrumCB7vI";
	private static final String TWEET_USER_NAME = "indianic.android.dev";
	public TwitterSession(Context context) {
		// TODO Auto-generated constructor stub
		sharedPref = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);

		editor = sharedPref.edit();
	}

	public AccessToken getAccessToken() {
		// TODO Auto-generated method stub
		String token 		= sharedPref.getString(TWEET_AUTH_KEY, null);
		String tokenSecret 	= sharedPref.getString(TWEET_AUTH_SECRET_KEY, null);
		
		if (token != null && tokenSecret != null) 
			return new AccessToken(token, tokenSecret);
		else
			return null;
	
	}

	public void resetAccessToken() {
		// TODO Auto-generated method stub
//		editor.putString(TWEET_AUTH_KEY, null);
//		editor.putString(TWEET_AUTH_SECRET_KEY, null);
//		editor.putString(TWEET_USER_NAME, null);
//		
		editor.remove(TWEET_AUTH_KEY);
		editor.remove(TWEET_AUTH_SECRET_KEY);
		editor.remove(TWEET_USER_NAME);
		
		
		editor.commit();
	}

	public String getUsername() {
		// TODO Auto-generated method stub
		return sharedPref.getString(TWEET_USER_NAME, "");
	}

	public void storeAccessToken(AccessToken mAccessToken, String name) {
		// TODO Auto-generated method stub
		editor.putString(TWEET_AUTH_KEY, mAccessToken.getToken());
		editor.putString(TWEET_AUTH_SECRET_KEY, mAccessToken.getTokenSecret());
		editor.putString(TWEET_USER_NAME, name);
		
		editor.commit();
	}

}
