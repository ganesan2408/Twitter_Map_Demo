package com.gans.twittertestapp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapActivity extends FragmentActivity  {

	GoogleMap mGoogleMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.map_view);
		
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getBaseContext());

		if (status != ConnectionResult.SUCCESS) { 

			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status,
					this, requestCode);
			dialog.show();

		} else { 

			SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map_on_map_view);

			mGoogleMap = mapFragment.getMap();

			mGoogleMap.setMyLocationEnabled(true);

				GPSTracker gps = new GPSTracker(getApplicationContext());
				if (gps != null)
					gps.getLocation();

				if (gps != null && gps.canGetLocation()) {
					
					double cLatitude = gps.getLatitude();
					double cLongitude = gps.getLongitude();
					
					LatLng latLng = new LatLng(cLatitude, cLongitude);
			
					mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
					mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
					
				}
				else
					gps.showSettingsAlert();
			}
	}
	
}
