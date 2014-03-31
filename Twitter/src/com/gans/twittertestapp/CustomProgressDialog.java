package com.gans.twittertestapp;

import android.app.Activity;
import android.app.ProgressDialog;

public class CustomProgressDialog {
	
	ProgressDialog pDialog;

	public CustomProgressDialog(Activity activity) {
		// TODO Auto-generated constructor stub
		pDialog = new ProgressDialog(activity);
		pDialog.setMessage("Loading...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
	}
	
	public void showDialog(){
		if(pDialog != null)
			pDialog.show();
	}
	
	public void cancelDialog(){
		if(pDialog != null && pDialog.isShowing())
			pDialog.cancel();
	}
}
