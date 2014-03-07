package com.ecs.android.sample.twitter;

import oauth.signpost.OAuth;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import android.content.SharedPreferences;

public class TwitterUtils {

	public static boolean isAuthenticated(SharedPreferences prefs) {

//		String token = prefs.getString(OAuth.OAUTH_TOKEN, "408606331-8NjeCjKaWDQvGOhMGQx1zEBd5FZtKksrjjJMPh5D");
//		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "GYtxxSy55Cu6Jdvck3WsmbQToDiLN7JvojmH9LbMwA");
		String token = prefs.getString(OAuth.OAUTH_TOKEN, "408606331-FYmgjBX3XwbgVetamsGNNR8Y049xYfWuF2RKED8V");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "dgWFQ34tZ43TUfJZcUzSGdOtFLL0eRA0JgFFkAAQJc");
		
		AccessToken a = new AccessToken(token,secret);
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		twitter.setOAuthAccessToken(a);
		
		try {
			twitter.getAccountSettings();
			return true;
		} catch (TwitterException e) {
			return false;
		}
	}
	
	public static void sendTweet(SharedPreferences prefs,String msg) throws Exception {
		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		
		AccessToken a = new AccessToken(token,secret);
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		twitter.setOAuthAccessToken(a);
        twitter.updateStatus(msg);
	}
	
	public static void Logout(SharedPreferences prefs) {
		
		Twitter twitter = new TwitterFactory().getInstance();
		
	}
}
