package lib.etc;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import covisoft.android.EasyLife.R;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.tab3_Home.Home_Activity;

public class GPS_location {

	Boolean loading = false;

	Timer timer1;
	LocationManager lm;
	boolean gps_enabled = false;
	boolean network_enabled = false;

	public Activity activity;

	public double latitude = 0;
	public double longitude = 0;

	ProgressDialog progressDialog;

	AlertDialog alertDialog;
	Dialog dialog;
	
	String provider;

	public boolean getLocation(Context context) {
		// I use LocationResult callback class to pass location value from
		// MyLocation to user code.
		if (lm == null) {
			lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		}
		
		//locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
       
		// exceptions will be thrown if provider is not permitted.
		try {
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		// don't start listeners if no provider is enabled
		if (!gps_enabled) {

			dialog = new Dialog(activity.getParent(), R.style.myBackgroundStyle);
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			lp.copyFrom(dialog.getWindow().getAttributes());
			lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
			lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.popup_two_option);

			TextView txt = (TextView) dialog.findViewById(R.id.txtContent);
			txt.setText(activity.getString(R.string.popup_OpenGPS));

			Button btnOk = (Button) dialog.findViewById(R.id.btn_OK);
			btnOk.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
  
					dialog.dismiss();
					activity.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
				}
			});
			Button btnNo = (Button) dialog.findViewById(R.id.btn_Cancel);
			btnNo.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
			dialog.show();
			return false;
		}
		
		 Criteria criteria = new Criteria();
	        provider = lm.getBestProvider(criteria, false);
	        Location location =lm.getLastKnownLocation(provider);
	        
	        if (location != null) {
				Home_Activity.latitude = location.getLatitude();
				Home_Activity.longitude = location.getLongitude();
		     } else {
					if (gps_enabled) {
						lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
					}
					if (network_enabled) {
						lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
					}
					timer1 = new Timer();
					timer1.schedule(new GetLastLocation(), EZUtil.DelayTime);
					init_progressDialog();
		     }

		return true;
	}

	LocationListener locationListenerGps = new LocationListener() {
		public void onLocationChanged(Location location) {
			timer1.cancel();
			latitude = location.getLatitude();
			longitude = location.getLongitude();

			lm.removeUpdates(this);
			lm.removeUpdates(locationListenerNetwork);

			Log.e("location 1", latitude + " - " + longitude);
			Home_Activity.latitude = latitude;
			Home_Activity.longitude = longitude;
			loading = false;
			progressDialog.dismiss();
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	LocationListener locationListenerNetwork = new LocationListener() {
		public void onLocationChanged(Location location) {
			timer1.cancel();
			lm.removeUpdates(this);
			lm.removeUpdates(locationListenerGps);
			latitude = location.getLatitude();
			longitude = location.getLongitude();

			Log.e("location 2", latitude + " - " + longitude);
			Home_Activity.latitude = latitude;
			Home_Activity.longitude = longitude;
			loading = false;
			progressDialog.dismiss();
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	class GetLastLocation extends TimerTask {
		@Override
		public void run() {
			lm.removeUpdates(locationListenerGps);
			lm.removeUpdates(locationListenerNetwork);

			latitude = 0;
			longitude = 0;

			loading = false;
			progressDialog.dismiss();

			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					dialog = new Dialog(activity.getParent(), R.style.myBackgroundStyle);
					WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
					lp.copyFrom(dialog.getWindow().getAttributes());
					lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
					lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.popup_one_option);

					TextView txt = (TextView) dialog.findViewById(R.id.txtContent);
					txt.setText(activity.getResources().getString(R.string.No_Location));

					Button btn_OK = (Button) dialog.findViewById(R.id.btn_OK);
					btn_OK.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							loading = false;
							dialog.dismiss();
						}
					});

					dialog.getWindow().setLayout(500, 400);
					dialog.show();
				}
			});

		}
	}

	public static abstract class LocationResult {
		public abstract void gotLocation(Location location);
	}

	void init_progressDialog() {
		loading = true;
		progressDialog = ProgressDialog.show(activity.getParent(), "", "Location...", true, true);

		progressDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				if (loading) {
					progressDialog.show();
				}
			}
		});
	}
	
}
