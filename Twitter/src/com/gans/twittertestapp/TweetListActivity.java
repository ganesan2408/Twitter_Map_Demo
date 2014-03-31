package com.gans.twittertestapp;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class TweetListActivity extends Activity {

	Context _context;

	ListView tweetListView;

	List<TweetsData> tweetsDataList = new ArrayList<TweetsData>();

	CustomProgressDialog customProgressDialog;
	
	boolean loaded =  true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tweets_list);
		_context = getApplicationContext();
		customProgressDialog = new CustomProgressDialog(this);
		tweetListView = (ListView) findViewById(R.id.tweet_list);
		initControl();
	}

	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) _context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}
	
	private void initControl() {
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(TwitterUtil.TWITTER_CALLBACK_URL)) {
            String verifier = uri.getQueryParameter(TwitterUtil.URL_PARAMETER_TWITTER_OAUTH_VERIFIER);
            new TwitterGetAccessTokenTask().execute(verifier);
        } else
            new TwitterGetAccessTokenTask().execute("");
    }

	class TwitterGetAccessTokenTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			customProgressDialog.showDialog();
		}

		@Override
		protected void onPostExecute(String userName) {
			customProgressDialog.cancelDialog();
			// textViewUserName.setText(Html.fromHtml("<b> Welcome " + userName
			// + "</b>"));
			
			
			Log.i("userName", ""+userName);
			loaded = true;
			new TwitterStatusList().execute();
		}

		@Override
		protected String doInBackground(String... params) {

			Twitter twitter = TwitterUtil.getInstance().getTwitter();
			RequestToken requestToken = TwitterUtil.getInstance()
					.getRequestToken();
			if (!isNullOrWhitespace(params[0])) {
				try {

					AccessToken accessToken = twitter.getOAuthAccessToken(
							requestToken, params[0]);
					SharedPreferences sharedPreferences = PreferenceManager
							.getDefaultSharedPreferences(getApplicationContext());
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString(
							TwitterUtil.PREFERENCE_TWITTER_OAUTH_TOKEN,
							accessToken.getToken());
					editor.putString(
							TwitterUtil.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET,
							accessToken.getTokenSecret());
					editor.putBoolean(
							TwitterUtil.PREFERENCE_TWITTER_IS_LOGGED_IN, true);
					editor.commit();
					return twitter.showUser(accessToken.getUserId()).getName();
				} catch (TwitterException e) {
					e.printStackTrace(); // To change body of catch statement
											// use File | Settings | File
											// Templates.
				}
			} else {
				SharedPreferences sharedPreferences = PreferenceManager
						.getDefaultSharedPreferences(getApplicationContext());
				String accessTokenString = sharedPreferences.getString(
						TwitterUtil.PREFERENCE_TWITTER_OAUTH_TOKEN, "");
				String accessTokenSecret = sharedPreferences.getString(
						TwitterUtil.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, "");
				AccessToken accessToken = new AccessToken(accessTokenString,
						accessTokenSecret);
				try {
					TwitterUtil.getInstance().setTwitterFactory(accessToken);
					return TwitterUtil.getInstance().getTwitter()
							.showUser(accessToken.getUserId()).getName();
				} catch (TwitterException e) {
					e.printStackTrace(); 
				}
			}

			return null; 
		}
	}

	class TwitterStatusList extends AsyncTask<String, String, Boolean> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			customProgressDialog.showDialog();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			customProgressDialog.cancelDialog();
			if (result) {
				tweetListView.setAdapter(new MYAdapter(TweetListActivity.this,
						tweetsDataList));
			} else
				Toast.makeText(getApplicationContext(),
						"Error Occurred failed", Toast.LENGTH_SHORT).show();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				SharedPreferences sharedPreferences = PreferenceManager
						.getDefaultSharedPreferences(getApplicationContext());
				String accessTokenString = sharedPreferences.getString(
						TwitterUtil.PREFERENCE_TWITTER_OAUTH_TOKEN, "");
				String accessTokenSecret = sharedPreferences.getString(
						TwitterUtil.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, "");
				
				if (!isNullOrWhitespace(accessTokenString)
						&& !isNullOrWhitespace(accessTokenSecret)) {
					AccessToken accessToken = new AccessToken(
							accessTokenString, accessTokenSecret);
					List<twitter4j.Status> statuses = TwitterUtil.getInstance()
							.getTwitterFactory().getInstance(accessToken)
							.getUserTimeline(TwitterUtil.getInstance().getTwitter()
									.showUser(accessToken.getUserId()).getScreenName());
					for (twitter4j.Status status : statuses) {
						System.out.println();
						// status.getId();
						tweetsDataList.add(new TweetsData("@ "
								+ status.getUser().getScreenName(), status
								.getText(), status.getUser().getProfileImageURL()));
						Log.i("user image", ""+status.getUser().getMiniProfileImageURL());
					}

					return true;
				}

			} catch (TwitterException e) {
				e.printStackTrace();
			}
			return false;
		}
	}

	private boolean isNullOrWhitespace(String val) {

		if (val != null && val.trim().length() != 0)
			return false;
		else
			return true;

	}
}
