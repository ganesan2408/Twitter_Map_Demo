package com.gans.twittertestapp;

import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	Context _context;

	Button loginBt;

	CustomProgressDialog customProgressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		_context = getApplicationContext();
		customProgressDialog = new CustomProgressDialog(this);

		loginBt = (Button) findViewById(R.id.buttonLogin);
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

	public void clickToLogin(View v) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		if (!sharedPreferences.getBoolean(
				TwitterUtil.PREFERENCE_TWITTER_IS_LOGGED_IN, false)) {
			new TwitterAuthenticateTask().execute();
		} else {
			
			 Intent intent = new Intent(MainActivity.this, TweetListActivity.class); 
			 startActivity(intent);
			
		}
	}
	public void clickToViewCurrentLocation(View v) {
		 Intent intent = new Intent(MainActivity.this, MapActivity.class); 
		 startActivity(intent);
		
	}
	

	class TwitterAuthenticateTask extends
			AsyncTask<String, String, RequestToken> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			customProgressDialog.showDialog();
		}

		@Override
		protected void onPostExecute(RequestToken requestToken) {
			customProgressDialog.cancelDialog();
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(requestToken.getAuthenticationURL()));
			startActivity(intent);
		}
		
		@Override
		protected RequestToken doInBackground(String... params) {
			return TwitterUtil.getInstance().getRequestToken();
		}
	}
}
