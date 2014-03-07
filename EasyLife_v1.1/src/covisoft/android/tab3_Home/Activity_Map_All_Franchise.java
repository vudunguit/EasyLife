package covisoft.android.tab3_Home;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.mixare.lib.marker.Marker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.maps.OnSingleTapListener;
import com.readystatesoftware.maps.TapControlledMapView;

import covisoft.android.AR.MixView;
import covisoft.android.EasyLife.R;
import covisoft.android.item.item_Franchise_Shop;
import covisoft.android.mapview.MyItemizedOverlay;
import covisoft.android.tabhost.NavigationMapActivity;

public class Activity_Map_All_Franchise extends NavigationMapActivity {

	public Activity activity = null;

	public static MyItemizedOverlay itemizedOverlay;
	public static ArrayList<item_Franchise_Shop> arItemFranchise;
	public static String address;

	private Button btnAR;
	public static ArrayList<Marker> bearListtmp = new ArrayList<Marker>();

// =============================================================================================
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_fullmap);

		Intent intent = getIntent();
		Serializable intent_franchise = intent.getSerializableExtra("ItemFranchise");

		arItemFranchise = (ArrayList<item_Franchise_Shop>) intent_franchise;

		activity = this;
		init();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	void init() {
		init_Btn_AR();
		init_button_left();
		init_mapview();
	}

	void init_Btn_AR() {
		btnAR = (Button) findViewById(R.id.btn_AR);
		btnAR.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(Activity_Map_All_Franchise.this, MixView.class);
				intent.putExtra("type", "All");
				startActivity(intent);

			}
		});
	}

	void init_button_left() {
		LinearLayout layout_Back = (LinearLayout) findViewById(R.id.layout_Back);
		layout_Back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				onBackPressed();

			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		arItemFranchise = null;
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

	void init_itemizeoverlay(List<Overlay> mapOverlays, TapControlledMapView mapView) {

		Drawable drawable = getResources().getDrawable(R.drawable.item_overlay);
		final MyItemizedOverlay itemizedOverlay = new MyItemizedOverlay(drawable, mapView, this);

		int len = arItemFranchise.size();

		for (int i = 0; i < len; i++) {
			double latitude = arItemFranchise.get(i).getLat();
			double longitude = arItemFranchise.get(i).getLng();
			f_add_overlayItem(itemizedOverlay, latitude, longitude, arItemFranchise.get(i).getName(), null);
		}

		mapOverlays.add(itemizedOverlay);
		mapView.setOnSingleTapListener(new OnSingleTapListener() {

			public boolean onSingleTap(MotionEvent e) {

				itemizedOverlay.hideAllBalloons();
				return false;
			}
		});

	}

	double f_convert(String s_value) {
		double d_value = 0;

		try {
			d_value = Double.valueOf(s_value).doubleValue();
		} catch (Exception e) {
			// TODO: handle exception
			d_value = 0;
		}

		return d_value;
	}

	void f_add_overlayItem(MyItemizedOverlay itemizedOverlay, double lat, double lng, String name, String addr) {
		GeoPoint point = getPoint(lat, lng);
		OverlayItem overlayItem = new OverlayItem(point, name, addr);
		itemizedOverlay.addOverlay(overlayItem);
	}

	private GeoPoint getPoint(double lat, double lon) {
		return (new GeoPoint((int) (lat * 1000000.0), (int) (lon * 1000000.0)));
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

}
