package com.gans.twittertestapp;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public final class TwitterUtil {

	public static String TWITTER_CONSUMER_KEY = "R2XwAvgabWEL2hUne6vhTQ";
	public static String TWITTER_CONSUMER_SECRET = "7vpFE5vmivLahIZm0rMZFYvYuhsg8tG4xFdIWhGbE";
	public static String TWITTER_CALLBACK_URL = "oauth://com.gans.twittertestapp";
	public static String URL_PARAMETER_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	public static String PREFERENCE_TWITTER_OAUTH_TOKEN = "TWITTER_OAUTH_TOKEN";
	public static String PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET = "TWITTER_OAUTH_TOKEN_SECRET";
	public static String PREFERENCE_TWITTER_IS_LOGGED_IN = "TWITTER_IS_LOGGED_IN";

	private RequestToken requestToken = null;
	private TwitterFactory twitterFactory = null;
	private Twitter twitter;

	public TwitterUtil() {
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder
				.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
		configurationBuilder
				.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
		Configuration configuration = configurationBuilder.build();
		twitterFactory = new TwitterFactory(configuration);
		twitter = twitterFactory.getInstance();
	}

	public TwitterFactory getTwitterFactory() {
		return twitterFactory;
	}

	public void setTwitterFactory(AccessToken accessToken) {
		twitter = twitterFactory.getInstance(accessToken);
	}

	public Twitter getTwitter() {
		return twitter;
	}

	public RequestToken getRequestToken() {
		if (requestToken == null) {
			try {
				requestToken = twitterFactory.getInstance()
						.getOAuthRequestToken(
								TWITTER_CALLBACK_URL);
			} catch (TwitterException e) {
				e.printStackTrace(); // To change body of catch statement use
										// File | Settings | File Templates.
			}
		}
		return requestToken;
	}

	static TwitterUtil instance = new TwitterUtil();

	public static TwitterUtil getInstance() {
		return instance;
	}

	public void reset() {
		instance = new TwitterUtil();
	}
}
