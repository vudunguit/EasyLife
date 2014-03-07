package covisoft.android.tab4;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lib.imageLoader.ImageLoader_Rounded;
import lib.imageLoader.RoundedMapView;
import lib.imageLoader.ScrollTextView;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.maps.OnSingleTapListener;

import covisoft.android.EasyLife.CheckTimeAsyncTask;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.EasyLifeActivity;
import covisoft.android.EasyLife.R;
import covisoft.android.mapview.MyItemizedOverlay;
import covisoft.android.services.service_Favorite_Add;
import covisoft.android.services.service_SmallAd_Detail;
import covisoft.android.tab3_Home.Activity_Map_All_Franchise;
import covisoft.android.tab3_Home.Activity_Map_All_Shop;
import covisoft.android.tabhost.NavigationMapActivity;

public class Activity_HireEmployee extends NavigationMapActivity {

	public static service_SmallAd_Detail xml = null;
	private RelativeLayout layoutTop;
	private LinearLayout layout_Back;

	public static Activity activity = null;

	private String no = "";
	static String category = "";

	private ImageLoader_Rounded imageLoader;
	private ImageView img_Coupon;

	private TextView txtTopname;
	private ScrollTextView txtName;
	private ScrollTextView txtAddress;
	private TextView txtPhone;

	private Button btnLike;
	private Button btnPhone;

	private Timer timer;

// =======================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_17_hire_employer);

		activity = this;

		Intent intent = getIntent();
		no = intent.getStringExtra("no");
		category = intent.getStringExtra("category");
		imageLoader = new ImageLoader_Rounded(activity.getApplicationContext());

		init();
	}

	public void init() {
		ScrollView parentScroll=(ScrollView)findViewById(R.id.parentScroll);
		ScrollView childScroll=(ScrollView)findViewById(R.id.childScroll);
		
		parentScroll.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                findViewById(R.id.childScroll).getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        childScroll.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event)
            {
                                    // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
	     
		initTop();
		initBtnBack();

		if (EZUtil.isNetworkConnected(activity)) {
			AsyncTaskRequestData task = new AsyncTaskRequestData();
			task.execute();
			timer = new Timer();
			timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);
		} else {
			showPopupOneOption(activity.getResources().getString(R.string.No_Internet_now), 2);
		}

	}

	void initBtnBack() {
		layout_Back = (LinearLayout) findViewById(R.id.layout_Back);
		layout_Back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();

	}

	void init_mapview() {

		RoundedMapView mapView = (RoundedMapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(false);

		List<Overlay> mapOverlays = mapView.getOverlays();

		init_itemizeoverlay(mapOverlays, mapView);

	}

	void init_itemizeoverlay(List<Overlay> mapOverlays, RoundedMapView mapView) {
		Drawable drawable = getResources().getDrawable(R.drawable.item_overlay);
		final MyItemizedOverlay itemizedOverlay = new MyItemizedOverlay(drawable, mapView, this);

		f_add_overlayItem(itemizedOverlay, Double.parseDouble(xml.lat), Double.parseDouble(xml.lng));

		mapOverlays.add(itemizedOverlay);

		final MapController mc = mapView.getController();
		GeoPoint point = getPoint(Double.parseDouble(xml.lat), Double.parseDouble(xml.lng));
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
		OverlayItem overlayItem = new OverlayItem(point, xml.companyName, "");

		TextView address = (TextView) findViewById(R.id.textview_address);
		address.setText(xml.add);

		itemizedOverlay.addOverlay(overlayItem);

	}

	private GeoPoint getPoint(double lat, double lon) {
		return (new GeoPoint((int) (lat * 1000000.0), (int) (lon * 1000000.0)));
	}

	public void initText() {

		ImageView image = (ImageView) findViewById(R.id.img);
		imageLoader.DisplayImage(xml.s_logo, image, R.drawable.s_05_noimage, EZUtil.REQUIRED_SIZE_SMALL);

		txtTopname = (TextView) findViewById(R.id.txtTopName);
		txtName = (ScrollTextView) findViewById(R.id.txtName);
		txtAddress = (ScrollTextView) findViewById(R.id.txtAddress);
		txtPhone = (TextView) findViewById(R.id.txtPhone);

		txtTopname.setText(xml.companyName);
		txtName.setText(xml.title);
		txtAddress.setText(xml.add);
		txtPhone.setText(xml.tel);

		TextView textview_cont = (TextView) findViewById(R.id.textview_cont);
		textview_cont.setText(xml.cont);

		TextView textview_time = (TextView) findViewById(R.id.textview_time);
		textview_time.setText(xml.regDate);

		TextView textview_day = (TextView) findViewById(R.id.textview_day);
		textview_day.setText("");

		TextView textview_pay = (TextView) findViewById(R.id.textview_pay);
		textview_pay.setText("");

		TextView textview_welfare = (TextView) findViewById(R.id.textview_welfare);
		textview_welfare.setText("");

		TextView textview_etc = (TextView) findViewById(R.id.textview_etc);
		textview_etc.setText("");

		TextView txt_Address = (TextView) findViewById(R.id.txt_Address);
		txt_Address.setText(xml.add);

	}
	
	public void init_Image() {
		img_Coupon = (ImageView)findViewById(R.id.img_Coupon);
		imageLoader = new ImageLoader_Rounded(activity.getApplicationContext());
		imageLoader.DisplayImage(xml.linkimage, img_Coupon, R.drawable.s_05_noimage, 120);
	}

	private class AsyncTaskRequestData extends AsyncTask<Void, Void, Void> {

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
			xml = new service_SmallAd_Detail(no, activity);
			xml.start();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			timer.cancel();

			EZUtil.cancelProgress();

			if (xml != null) {  

				init_Btn();
				initText();  
				init_mapview();
				init_Image();

			} else {
				onBackPressed();
			}

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
			}, 300);
		}
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public void init_Btn() {
		btnLike = (Button) findViewById(R.id.btnLike);
		btnLike.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				if (xml != null) {

					if (EasyLifeActivity.user != null) {
						if (EZUtil.isNetworkConnected(activity)) {
							AsyncTask_AddFavourite task = new AsyncTask_AddFavourite();
							task.execute();
							timer = new Timer();
							timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);
						} else {
							showPopupOneOption(activity.getResources().getString(R.string.No_Internet_now), 1);
						}

					} else {
						Intent intent = new Intent(Activity_HireEmployee.this, Activity_Login_Tab4.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						goNextHistory("Activity_Login_Tab4", intent);
					}
				}

			}
		});
		btnPhone = (Button) findViewById(R.id.btnPhone);
		if(xml.tel.trim().equals("")) {
			btnPhone.setBackgroundResource(R.drawable.s_05phone_none);
		} else {
			btnPhone.setBackgroundResource(R.drawable.s_05phone);
		}
		btnPhone.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (xml != null && !xml.tel.trim().equals("")) {
					String s_tel = xml.tel;
					Uri number = Uri.parse("tel:" + s_tel);
					Intent intent_tel = new Intent(Intent.ACTION_DIAL, number);
					startActivity(intent_tel);
				} else {
					showPopupOneOption(activity.getResources().getString(R.string.popup_NoPhoneNumber), 1);
				}

			}
		});
	}

	//********************************
	//   AsynsTask Add Favourite     *
	//********************************
	private class AsyncTask_AddFavourite extends AsyncTask<Void, Void, String> {

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
		protected String doInBackground(Void... params) {

			if (isCancelled()) {
				return null;
			}
			
			service_Favorite_Add xml_add = new service_Favorite_Add(EasyLifeActivity.user.getUsername(), xml.no + "", "2", Activity_HireEmployee.this);
			String addResult = xml_add.start();
			
			return addResult;
		}

		@Override
		protected void onPostExecute(String result) {

			timer.cancel();

			EZUtil.cancelProgress();

			if (result.equals("1")) {
				Timer t = new Timer();
				t.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						activity.runOnUiThread(new Runnable() {
							public void run() {
								showPopupOneOption(Activity_HireEmployee.this.getResources().getString(R.string.popup_Favourite_AddSuccessful), 1);
							}
						});
					}
				}, 300);
				

			} else if (result.equals("0")) {
				Timer t = new Timer();
				t.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						activity.runOnUiThread(new Runnable() {
							public void run() {
								showPopupOneOption(Activity_HireEmployee.this.getResources().getString(R.string.popup_Favourite_Existed), 1);
							}
						});
					}
				}, 300);

			}

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
			}, 300);
		}
	}
	

	public void showPopupOneOption(String content, int type) {

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
			if (type == 1) {
				btn_OK.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						EZUtil.isLoading = false;
						dialog.dismiss();
					}
				});
			} else if (type == 2) {
				btn_OK.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						EZUtil.isLoading = false;
						dialog.dismiss();
						onBackPressed();
					}
				});
			}

			dialog.getWindow().setLayout(500, 400);
			dialog.show();

			dialog.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					EZUtil.isLoading = false;
				}
			});
		}
	}

	public void initTop() {

		layoutTop = (RelativeLayout) findViewById(R.id.layoutTop);
		switch (Integer.parseInt(category)) {
			case 4:
				layoutTop.setBackgroundResource(R.drawable.top_education_notext);
				break;
			case 1:
				layoutTop.setBackgroundResource(R.drawable.top_food_notext);
				break;
			case 8:
				layoutTop.setBackgroundResource(R.drawable.top_beauty_notext);
				break;
			case 9:
				layoutTop.setBackgroundResource(R.drawable.top_shopping_notext);
				break;
			case 5:
				layoutTop.setBackgroundResource(R.drawable.top_health_notext);
				break;
			case 10:
				layoutTop.setBackgroundResource(R.drawable.top_entertainment_notext);
				break;
			case 11:
				layoutTop.setBackgroundResource(R.drawable.top_tourism_notext);
				break;
	
			default:
				break;
		}
	}

}
