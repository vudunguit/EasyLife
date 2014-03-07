package lib.etc;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import covisoft.android.EasyLife.R;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.tab3_Home.Home_Activity;

public class Gps2 implements  ConnectionCallbacks, OnConnectionFailedListener, LocationListener  {
	  private LocationClient mLocationClient;
	  private LocationManager lm;
	  private boolean gps_enabled;
	  private Dialog dialog;
	  private ProgressDialog Prdialog;
		public double latitude = 0;
		public double longitude = 0;
	  Timer timer1;
	  Activity main;
	  Boolean loading = false;
	  
	  
	  public Gps2(Activity main) {
		  this.main = main;
		// TODO Auto-generated constructor stub
	}
	  
	 public void run ()
	 {
		    setUpLocationClientIfNeeded();
		    mLocationClient.connect();

	 }
	 
	 public boolean getLocation() {
			if (lm == null) {
				lm = (LocationManager)main.getSystemService(Context.LOCATION_SERVICE);
			}
			
			try {
				gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
			} catch (Exception ex) {
			}
			// don't start listeners if no provider is enabled
			if (!gps_enabled) {

				dialog = new Dialog(main.getParent(), R.style.myBackgroundStyle);
				WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
				lp.copyFrom(dialog.getWindow().getAttributes());
				lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
				lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.popup_two_option);

				TextView txt = (TextView) dialog.findViewById(R.id.txtContent);
				txt.setText(main.getString(R.string.popup_OpenGPS));

				Button btnOk = (Button) dialog.findViewById(R.id.btn_OK);
				btnOk.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
	  
						dialog.dismiss();
						Home_Activity.GpsOnOff = true;
						main.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
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
			else {
				run();
			}
			timer1 = new Timer();
			timer1.schedule(new GetLastLocation(), EZUtil.DelayTime);
			init_progressDialog();
			
			return true;
		}

	@Override
	public void onLocationChanged(Location arg0) {
		Home_Activity.latitude = arg0.getLatitude();
		Home_Activity.longitude = arg0.getLongitude();
		
		 if (mLocationClient != null) {
		      mLocationClient.disconnect();
		    }
		 loading = false;
		 timer1.cancel();
		 Prdialog.dismiss();
	}
	
	private static final LocationRequest REQUEST = LocationRequest.create()
		      .setInterval(5000)         // 5 seconds
		      .setFastestInterval(16)    // 16ms = 60fps
		      .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	
	
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		 mLocationClient.requestLocationUpdates(
			        REQUEST,
			        this);
		
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}
	
	private void setUpLocationClientIfNeeded() {
		    if (mLocationClient == null) {
		      mLocationClient = new LocationClient(
		          main,
		          this,  // ConnectionCallbacks
		          this); // OnConnectionFailedListener
		    }
		    
	 }
	
	void init_progressDialog() {
		loading = true;
		Prdialog = ProgressDialog.show(main.getParent(), "", "Locating...", true, true);

		Prdialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				if (loading) {
					Prdialog.show();
				}
			}
		});
	}
	
	class GetLastLocation extends TimerTask {
		@Override
		public void run() {
		
			
			 Criteria criteria = new Criteria();
		        String provider = lm.getBestProvider(criteria, false);
		        Location location =lm.getLastKnownLocation(provider);
		        
		        if (location!=null)
		        {
					Home_Activity.latitude = location.getLatitude();
					Home_Activity.longitude = location.getLongitude();
		        }

		        else 
		        {
		        	latitude = 0;
					longitude = 0;

		        
			
			loading = false;
			Prdialog.dismiss();

			main.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					dialog = new Dialog(main.getParent(), R.style.myBackgroundStyle);
					WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
					lp.copyFrom(dialog.getWindow().getAttributes());
					lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
					lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.popup_one_option);

					TextView txt = (TextView) dialog.findViewById(R.id.txtContent);
					txt.setText(main.getResources().getString(R.string.No_Location));

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

	}
}
