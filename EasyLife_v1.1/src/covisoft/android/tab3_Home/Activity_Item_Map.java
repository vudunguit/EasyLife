package covisoft.android.tab3_Home;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.maps.OnSingleTapListener;
import com.readystatesoftware.maps.TapControlledMapView;

import covisoft.android.AR.MixView;
import covisoft.android.EasyLife.CheckTimeAsyncTask;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.R;
import covisoft.android.adapter.Adapter_Direction;
import covisoft.android.item.item_MyGeoPoint;
import covisoft.android.item.item_store_list;
import covisoft.android.mapview.MyItemizedOverlay;
import covisoft.android.services.service_Google_Direction;
import covisoft.android.tabhost.NavigationMapActivity;

/*
 * Activity Show map of one shop
 * 
 * Updated: 14/06/2013
 * Updater: Huan
 * 
 */
public class Activity_Item_Map extends NavigationMapActivity {

	public static Activity activity = null;
	private LinearLayout layout_Back;
	
	double lat = 0;
	double lng = 0;
	public static item_store_list item;

	private Button btnAR;
	private Button btnDirection;
	private LinearLayout layoutDirection;
	private Button btnCloseDirection;
	private ListView listDirection;
	private Adapter_Direction adapterDirection;

	private ArrayList<item_MyGeoPoint> arrayGeo;
	public static String address;

	private Timer timer;

// ===========================================================================================
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_10_map);

		activity = this;
		
		Intent intent = getIntent();
		item = (item_store_list) intent.getSerializableExtra("item");
		
		try {
			lat = Double.valueOf(item.lat).doubleValue();
			lng = Double.valueOf(item.lng).doubleValue();
		} catch (Exception e) {
			lat = 0;
			lng = 0;
		}

		init();
	}

	void init() {

		init_Btn_AR();
		init_BtnBack();

		if (EZUtil.isNetworkConnected(activity)) {
			AsyncTask_Direction task = new AsyncTask_Direction();
			task.execute();
			timer = new Timer();
			timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);
		} else {
			showPopupOneOption(activity.getResources().getString(R.string.No_Internet_now), 2);
		}
	}

	private class AsyncTask_Direction extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			if (getParent() != null) {
				EZUtil.init_progressDialog(getParent());
			} else {
				EZUtil.init_progressDialog(activity);
			}
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params) {

			if (isCancelled()) {
				return null;
			}
			String lat = Double.toString(Home_Activity.latitude);
			String lng = Double.toString(Home_Activity.longitude);
			
			service_Google_Direction xml = new service_Google_Direction(lat, lng, item.lat, item.lng, Activity_Item_Map.this);

			arrayGeo = new ArrayList<item_MyGeoPoint>();
			arrayGeo = xml.getDirections();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			timer.cancel();
			EZUtil.cancelProgress();
			init_mapview();
			init_Direction();

			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {

			timer.cancel();
			EZUtil.cancelProgress();
			Timer t = new Timer();
			t.schedule(new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					activity.runOnUiThread(new Runnable() {
						public void run() {
							showPopupOneOption(activity.getResources().getString(R.string.No_Response), 1);
						}
					});
				}
			}, 500);
		}
	}

	public void init_Direction() {
		btnDirection = (Button) findViewById(R.id.btn_Direction);
		layoutDirection = (LinearLayout) findViewById(R.id.layout_Direction);
		btnCloseDirection = (Button) findViewById(R.id.btn_CloseDirection);

		if (Home_Activity.latitude == 0) {
			btnDirection.setVisibility(View.GONE);
		}
		
		ArrayList<String> arrDescription = new ArrayList<String>();
		for (int i = 0; i < arrayGeo.size(); i++) {
			if (!arrayGeo.get(i).getDescription().equals("")) {
				arrDescription.add(arrayGeo.get(i).getDescription());
			}
		}
		listDirection = (ListView) findViewById(R.id.list_Direction);
		adapterDirection = new Adapter_Direction(activity, arrDescription);
		listDirection.setAdapter(adapterDirection);
		listDirection.setCacheColorHint(Color.TRANSPARENT); // not sure if this
															// is required for
															// you.
		listDirection.setFastScrollEnabled(true);
		listDirection.setScrollingCacheEnabled(false);

		btnDirection.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnDirection.setVisibility(View.GONE);
				layoutDirection.setVisibility(View.VISIBLE);

			}
		});
		btnCloseDirection.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				layoutDirection.setVisibility(View.GONE);
				btnDirection.setVisibility(View.VISIBLE);
			}
		});
	}

	void init_Btn_AR() {
		btnAR = (Button) findViewById(R.id.btn_AR);
		btnAR.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(Activity_Item_Map.this, MixView.class);
				intent.putExtra("type", "Single");
				startActivity(intent);

			}
		});
	}

	void init_BtnBack() {
		layout_Back = (LinearLayout) findViewById(R.id.layout_Back);
		layout_Back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		item = null;
		super.onBackPressed();
	}

	void init_mapview() {
		TapControlledMapView mapView = (TapControlledMapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(false);

		List<Overlay> mapOverlays = mapView.getOverlays();

		init_itemizeoverlay_me(mapOverlays, mapView);
		init_itemizeoverlay(mapOverlays, mapView);

	}

	void init_itemizeoverlay_me(List<Overlay> mapOverlays, TapControlledMapView mapView) {
		String test;
		Drawable drawable = getResources().getDrawable(R.drawable.lib_my_mapview_marker_me);
		MyItemizedOverlay itemizedOverlay = new MyItemizedOverlay(drawable, mapView, activity);

		f_add_overlayItem(itemizedOverlay, Home_Activity.latitude, Home_Activity.longitude, getString(R.string.Map_My_Location), null);

		test = readData("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + String.valueOf(Home_Activity.latitude) + "," + String.valueOf(Home_Activity.longitude) + "&sensor=false");
		
		if (test != null && !test.contains("ZERO_RESULTS")) {
			int index = test.indexOf("formatted_address") + 22;
			int end = test.indexOf('"' + ",", index);
			address = test.substring(index, end);

		} else {
			address = "Can not dectect your location";
		}

		LinearLayout LinearLayout_address = (LinearLayout) findViewById(R.id.LinearLayout_address);
		LinearLayout_address.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				v.setVisibility(View.INVISIBLE);

			}
		});

		mapOverlays.add(itemizedOverlay);

		final MapController mc = mapView.getController();
		GeoPoint point = getPoint(Home_Activity.latitude, Home_Activity.longitude);
		mc.animateTo(point);
		mc.setZoom(16);

	}

	void f_add_overlayItem(MyItemizedOverlay itemizedOverlay, double lat, double lng, String name, String addr) {
		GeoPoint point = getPoint(lat, lng);
		OverlayItem overlayItem = new OverlayItem(point, name, addr);
		itemizedOverlay.addOverlay(overlayItem);
	}

	public String readData(String Url) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(Url);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
			} else {
				// Log.e(ParseJSON.class.toString(), "Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}

	void init_itemizeoverlay(List<Overlay> mapOverlays, TapControlledMapView mapView) {

		Drawable drawable = getResources().getDrawable(R.drawable.item_overlay);
		final MyItemizedOverlay itemizedOverlay = new MyItemizedOverlay(drawable, mapView, arrayGeo, this);

		f_add_overlayItem(itemizedOverlay, lat, lng);

		mapOverlays.add(itemizedOverlay);

		final MapController mc = mapView.getController();
		GeoPoint point = getPoint(lat, lng);
		mapView.setOnSingleTapListener(new OnSingleTapListener() {

			public boolean onSingleTap(MotionEvent e) {
				itemizedOverlay.hideAllBalloons();
				return false;
			}
		});
		itemizedOverlay.onTap(0);
		mc.animateTo(point);
		mc.setZoom(16);
		LinearLayout LinearLayout_address = (LinearLayout) findViewById(R.id.LinearLayout_address);
		LinearLayout_address.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				v.setVisibility(View.INVISIBLE);

			}
		});
	}

	void f_add_overlayItem(MyItemizedOverlay itemizedOverlay, double lat, double lng) {

		Activity_Map_All_Shop.arItem = null;
		Activity_Map_All_Franchise.arItemFranchise = null;

		GeoPoint point = getPoint(lat, lng);
		OverlayItem overlayItem = new OverlayItem(point, item.name, "");

		TextView address = (TextView) findViewById(R.id.textview_address);
		address.setText(item.addr);

		itemizedOverlay.addOverlay(overlayItem);

	}

	private GeoPoint getPoint(double lat, double lon) {
		return (new GeoPoint((int) (lat * 1000000.0), (int) (lon * 1000000.0)));
	}

	@Override
	protected void onStart() {

		super.onStart();
	}

	public void showPopupOneOption(String content, int type) {

		final int fiType = type;
		if (!EZUtil.isLoading) {
			EZUtil.isLoading = true;

			final Dialog dialog = new Dialog(activity.getParent(), R.style.myBackgroundStyle);
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			lp.copyFrom(dialog.getWindow().getAttributes());
			lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
			lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.popup_one_option);

			TextView txt = (TextView) dialog.findViewById(R.id.txtContent);
			txt.setText(content);

			Button btn_OK = (Button) dialog.findViewById(R.id.btn_OK);
			btn_OK.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialog.getWindow().setLayout(500, 400);
			dialog.show();

			dialog.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					EZUtil.isLoading = false;
					if (fiType == 2) {
						onBackPressed();
					}
				}
			});
		}
	}

}
