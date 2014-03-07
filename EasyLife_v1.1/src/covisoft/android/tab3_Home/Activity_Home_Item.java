package covisoft.android.tab3_Home;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import lib.imageLoader.ImageLoader_Rounded;
import lib.imageLoader.ScrollTextView;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecs.android.sample.twitter.TwitterUtils;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.LoginButton;
import com.facebook.android.SessionEvents;
import com.facebook.android.SessionEvents.LogoutListener;
import com.facebook.android.Utility;

import covisoft.android.AR.MixView;
import covisoft.android.EasyLife.CheckTimeAsyncTask;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.EasyLifeActivity;
import covisoft.android.EasyLife.R;
import covisoft.android.adapter.Adapter_Coupon_ShopDetail;
import covisoft.android.adapter.Adapter_Event;
import covisoft.android.adapter.Adapter_QRCoupon_ShopDetail;
import covisoft.android.item.item_store_list;
import covisoft.android.services.service_Favorite_Add;
import covisoft.android.services.service_Shop_Comment;
import covisoft.android.services.service_Shop_Detail;
import covisoft.android.tabhost.NavigationActivity;
import covisoft.kakaotalk.KakaoLink;

public class Activity_Home_Item extends NavigationActivity {

	public static Activity activity;
	
	private Button btn_Pro_out;
	private Button btn_Info_out;
	private Button btn_Event_out;
	private Button btn_Other_out;

	private ImageView img_New_Coupon;
	private ImageView img_New_Event;

	private LinearLayout layout_info;
	private ListView layout_event_list;

	private Button btnShare;
	private Button btnLike;
	private Button btnPhone;

	private Button btnPhoto;
	private Button btnMap;
	private Button btnMenu;
	private TextView txtTopName;
	private TextView txtName;
	private TextView txtAddress;
	private TextView txtIntroduction;

	
	/*  Layout Comment */
	private Button btnComment;
	
	private ImageView imgCmt;
	private Button btn_star_1;
	private Button btn_star_2;
	private Button btn_star_3;
	private Button btn_star_4;
	private Button btn_star_5;
	private String grade = "0";
	private EditText edittext;

	/* Detail Information Layout */
	private ScrollView linearBasicInformation;

	private LinearLayout linearCoupon;
	private ListView listCoupon;
	private Adapter_Coupon_ShopDetail adapter;
	private Button btnAllCoupon;
	private LinearLayout linearQRCoupon;
	private ListView listQRCoupon;
	private Adapter_QRCoupon_ShopDetail adapterQR;
	private Button btnAllQRCoupon;
	
	private TextView txt_NoCoupon;

	private TextView txt_no_event;

	private LoginButton btnFacebook;
	private Button btnKakaotalk;
	// private Button btnTwitter;

	final static int AUTHORIZE_ACTIVITY_RESULT_CODE = 0;
	public static item_store_list item;

	

	private String no = "";
	private String act = "";
	private String shopType = "";
	private String parentCategoryNo = "";
	private String type = "";
	private String notic_kind = ""; // 1/3 - Show tab voucher, 2 - show tab event

	
	public ImageLoader_Rounded imageLoader;

	public static service_Shop_Detail xml;

	private SharedPreferences prefs;
	private final Handler mTwitterHandler = new Handler();
	private String[] permissions = { "offline_access", "publish_stream", "user_photos", "publish_checkins", "photo_upload" };

	private Handler mHandler;

	private Dialog dialog;
	private Timer timer;

// =================================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);

		setContentView(R.layout.layout_07_item);

		activity = this;

		Intent intent = getIntent();
		no = intent.getStringExtra("no");
		act = intent.getStringExtra("act");
		shopType = intent.getStringExtra("shopType");
		parentCategoryNo = intent.getStringExtra("parentCategoryNo");
		type = intent.getStringExtra("type");
		notic_kind = intent.getStringExtra("notic_kind");

		imageLoader = new ImageLoader_Rounded(activity.getApplicationContext());

		init();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
		super.onPause();
	}
	void init() {

		init_BtnBack();
		init_comment();
		initTop();

		if (EZUtil.isNetworkConnected(activity)) {

			AsyncTask_ShopDetail task = new AsyncTask_ShopDetail();
			task.execute();
			timer = new Timer();
			timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);

		} else {
			showPopupOneOption(activity.getResources().getString(R.string.No_Internet_now), 2);
		}

	}

	private class AsyncTask_ShopDetail extends AsyncTask<Void, Void, Void> {

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
			String deviceId = Settings.System.getString(activity.getContentResolver(), Settings.System.ANDROID_ID);

			xml = new service_Shop_Detail();
			if (act != null && act.equals(EZUtil.db_act_franchise)) {
				xml.init_franchise_detail(no, activity);
			} else if (act != null && act.equals(EZUtil.db_act_favorite)) {
				if (shopType.equals("0")) {
					xml.init_detail(no, deviceId, activity);
				} else if (shopType.equals("1")) {
					xml.init_franchise_detail(no, activity);
				}
			} else {
				xml.init_detail(no, deviceId, activity);
			}

			xml.start();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			timer.cancel();
			EZUtil.cancelProgress();

			init_Item();
			init_CouponList();
			init_QRCouponList();
			init_ListEvent();
			initButtonTab();

			initBtnFunction();

			init_Text();
			init_comment_list();
			
			initBtnAll();

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
							showPopupOneOption(activity.getResources().getString(R.string.No_Response), 2);
						}
					});
				}
			}, 300);
		}
	}

	public void init_Item() {
		item = new item_store_list();
		item.addr = xml.addr;
		item.lat = xml.lat;
		item.lng = xml.lng;
		item.name = xml.name;
		item.no = xml.no;
		item.s_logo = xml.s_logo;
		item.tel = xml.tel;
	}

	public void init_ListEvent() {
		txt_no_event = (TextView) findViewById(R.id.Event_infor_text);

		layout_event_list = (ListView) findViewById(R.id.Event_infor_listview);
		Adapter_Event MyAdapter = new Adapter_Event(Activity_Home_Item.this, xml.arItem_event);
		layout_event_list.setAdapter(MyAdapter);
		layout_event_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}

	public void initButtonTab() {

		btn_Pro_out = (Button) findViewById(R.id.btn_Pro_out);
		btn_Info_out = (Button) findViewById(R.id.btn_Info_out);
		btn_Event_out = (Button) findViewById(R.id.btn_Event_out);
		btn_Other_out = (Button) findViewById(R.id.btn_Other_out);

		img_New_Coupon = (ImageView) findViewById(R.id.img_New_Coupon);
		img_New_Event = (ImageView) findViewById(R.id.img_New_Event);

		layout_info = (LinearLayout) findViewById(R.id.layout_info);
		linearCoupon = (LinearLayout) findViewById(R.id.linearCoupon);
		linearQRCoupon = (LinearLayout) findViewById(R.id.linearQRCoupon);
		txt_NoCoupon = (TextView) findViewById(R.id.txt_NoCoupon);

		if (xml.arItem_coupon.size() > 0 || xml.arItem_QRCoupon.size() > 0) {
			img_New_Coupon.setVisibility(View.VISIBLE);
		} else {
			img_New_Coupon.setVisibility(View.GONE);
		}
		if (xml.arItem_event.size() > 0) {
			img_New_Event.setVisibility(View.VISIBLE);
		} else {
			img_New_Event.setVisibility(View.GONE);
		}

		txtIntroduction = (TextView) findViewById(R.id.txtIntroduction);
		try {
			txtIntroduction.setText(xml.s_info);
		} catch (Exception e) {
			e.printStackTrace();
			txtIntroduction.setText("Null");
		}

		linearBasicInformation = (ScrollView) findViewById(R.id.BasicInformation);

		// Working Time
		LinearLayout Layout_WorkTime = (LinearLayout) findViewById(R.id.Layout_WorkTime);
		TextView textview_WorkTime = (TextView) findViewById(R.id.textview_WorkTime);
		if (!xml.workDay.equals("")) {
			Layout_WorkTime.setVisibility(View.VISIBLE);
			textview_WorkTime.setText(xml.workDay);
		} else {
			Layout_WorkTime.setVisibility(View.GONE);
		}

		// Day Off
		LinearLayout Layout_DayOff = (LinearLayout) findViewById(R.id.Layout_DayOff);
		TextView textview_DayOff = (TextView) findViewById(R.id.textview_DayOff);
		if (!xml.freeDay.equals("")) {
			Layout_DayOff.setVisibility(View.VISIBLE);
			textview_DayOff.setText(xml.freeDay);
		} else {
			Layout_DayOff.setVisibility(View.GONE);
		}

		// Location
		LinearLayout Layout_Location = (LinearLayout) findViewById(R.id.Layout_Location);
		TextView textview_Location = (TextView) findViewById(R.id.textview_Location);
		if (!xml.companyLocate.equals("")) {
			Layout_Location.setVisibility(View.VISIBLE);
			textview_Location.setText(xml.companyLocate);
		} else {
			Layout_Location.setVisibility(View.GONE);
		}

		// Parking
		LinearLayout Layout_Parking = (LinearLayout) findViewById(R.id.Layout_Parking);
		TextView textview_Parking = (TextView) findViewById(R.id.textview_Parking);
		if (!xml.parking.equals("")) {
			Layout_Parking.setVisibility(View.VISIBLE);
			if(xml.parking.equals("Y")) {
				textview_Parking.setText(activity.getString(R.string.s_07_Parking_Yes));
			} else if(xml.parking.equals("N")){
				Layout_Parking.setVisibility(View.GONE);
			}
			
		} else {
			Layout_Parking.setVisibility(View.GONE);
		}

		// Delivery
		LinearLayout Layout_Delivery = (LinearLayout) findViewById(R.id.Layout_Delivery);
		TextView textview_Delivery = (TextView) findViewById(R.id.textview_Delivery);
		if (!xml.deliveryYn.equals("")) {
			Layout_Delivery.setVisibility(View.VISIBLE);
			if(xml.deliveryYn.equals("Y") && xml.tel.length() > 0) {
				textview_Delivery.setText(xml.tel);
			} else if(xml.deliveryYn.equals("N")){
				Layout_Delivery.setVisibility(View.GONE);
			}
		} else {
			Layout_Delivery.setVisibility(View.GONE);
		}

		// Wifi
		LinearLayout Layout_Wifi = (LinearLayout) findViewById(R.id.Layout_Wifi);
		TextView textview_Wifi = (TextView) findViewById(R.id.textview_Wifi);
		if (!xml.wifi.equals("")) {
			Layout_Wifi.setVisibility(View.VISIBLE);
			textview_Wifi.setText(xml.wifi);
		} else {
			Layout_Wifi.setVisibility(View.GONE);
		}

		// HomePage
		LinearLayout Layout_Homepage = (LinearLayout) findViewById(R.id.Layout_Homepage);
		TextView textview_Homepage = (TextView) findViewById(R.id.textview_Homepage);
		if (!xml.homepage.equals("")) {
			Layout_Homepage.setVisibility(View.VISIBLE);
			textview_Homepage.setText(xml.homepage);
		} else {
			Layout_Homepage.setVisibility(View.GONE);
		}

		// Facebook
		LinearLayout Layout_Facebook = (LinearLayout) findViewById(R.id.Layout_Facebook);
		TextView textview_Facebook = (TextView) findViewById(R.id.textview_Facebook);
		if (!xml.facebook.equals("")) {
			Layout_Facebook.setVisibility(View.VISIBLE);
			textview_Facebook.setText(xml.facebook);
		} else {
			Layout_Facebook.setVisibility(View.GONE);
		}

		btn_Pro_out.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				layout_info.setBackgroundResource(R.drawable.s_7_pro_out);

				txtIntroduction.setVisibility(View.VISIBLE);
				linearBasicInformation.setVisibility(View.GONE);
				linearCoupon.setVisibility(View.GONE);
				linearQRCoupon.setVisibility(View.GONE);
				txt_NoCoupon.setVisibility(View.GONE);
				layout_event_list.setVisibility(View.GONE);
				txt_no_event.setVisibility(View.GONE);

				if (xml.arItem_coupon.size() > 0 || xml.arItem_QRCoupon.size() > 0) {
					img_New_Coupon.setVisibility(View.VISIBLE);
				} else {
					img_New_Coupon.setVisibility(View.GONE);
				}
				if (xml.arItem_event.size() > 0) {
					img_New_Event.setVisibility(View.VISIBLE);
				} else {
					img_New_Event.setVisibility(View.GONE);
				}

			}
		});
		btn_Info_out.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				layout_info.setBackgroundResource(R.drawable.s_7_info_out);

				txtIntroduction.setVisibility(View.GONE);
				linearBasicInformation.setVisibility(View.VISIBLE);
				linearCoupon.setVisibility(View.GONE);
				linearQRCoupon.setVisibility(View.GONE);
				txt_NoCoupon.setVisibility(View.GONE);
				layout_event_list.setVisibility(View.GONE);
				txt_no_event.setVisibility(View.GONE);

				if (xml.arItem_coupon.size() > 0 || xml.arItem_QRCoupon.size() > 0) {
					img_New_Coupon.setVisibility(View.VISIBLE);
				} else {
					img_New_Coupon.setVisibility(View.GONE);
				}
				if (xml.arItem_event.size() > 0) {
					img_New_Event.setVisibility(View.VISIBLE);
				} else {
					img_New_Event.setVisibility(View.GONE);
				}
			}
		});

		btn_Event_out.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				layout_info.setBackgroundResource(R.drawable.s_7_event_out);
				txtIntroduction.setVisibility(View.GONE);
				linearBasicInformation.setVisibility(View.GONE);
				layout_event_list.setVisibility(View.GONE);
				txt_no_event.setVisibility(View.GONE);

				if(xml.arItem_coupon.size() > 0 || xml.arItem_QRCoupon.size() > 0) {
					txt_NoCoupon.setVisibility(View.GONE);
				} else {
					txt_NoCoupon.setVisibility(View.VISIBLE);
				}
				if (xml.arItem_coupon.size() > 0) {
					linearCoupon.setVisibility(View.VISIBLE);
				} else {
					linearCoupon.setVisibility(View.GONE);
				}
				
				if(xml.arItem_QRCoupon.size() > 0) {
					linearQRCoupon.setVisibility(View.VISIBLE);
				} else {
					linearQRCoupon.setVisibility(View.GONE);
				}

				img_New_Coupon.setVisibility(View.GONE);
				if (xml.arItem_event.size() > 0) {
					img_New_Event.setVisibility(View.VISIBLE);
				} else {
					img_New_Event.setVisibility(View.GONE);
				}

			}
		});
		btn_Other_out.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				layout_info.setBackgroundResource(R.drawable.s_7_other_out);

				txtIntroduction.setVisibility(View.GONE);
				linearBasicInformation.setVisibility(View.GONE);
				linearCoupon.setVisibility(View.GONE);
				linearQRCoupon.setVisibility(View.GONE);
				txt_NoCoupon.setVisibility(View.GONE);

				if (xml.arItem_event.size() != 0 || xml.arItem_QRCoupon.size() > 0) {
					layout_event_list.setVisibility(View.VISIBLE);
					txt_no_event.setVisibility(View.GONE);
				} else {
					layout_event_list.setVisibility(View.GONE);
					txt_no_event.setVisibility(View.VISIBLE);
				}

				if (xml.arItem_coupon.size() > 0 || xml.arItem_QRCoupon.size() > 0) {
					img_New_Coupon.setVisibility(View.VISIBLE);
				} else {
					img_New_Coupon.setVisibility(View.GONE);
				}
				img_New_Event.setVisibility(View.GONE);

			}
		});

		linearCoupon.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
				if (xml.arItem_coupon.size() > 0) {
					Intent intent = new Intent(Activity_Home_Item.this, Activity_Coupon.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					intent.putExtra("item", item);
					intent.putExtra("couponList", xml.arItem_coupon);
					intent.putExtra("position", "");
					goNextHistory("Activity_Coupon", intent);
				}  

			}
		});
		linearQRCoupon.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);

				Intent intent = new Intent(Activity_Home_Item.this, Activity_QRCoupon_Shop.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra("qrCoupon", xml.arItem_QRCoupon.get(0));
				goNextHistory("Activity_QRCoupon_Shop", intent);
			}
		});
		
		if(notic_kind != null && notic_kind.equals("Voucher")) {
			btn_Event_out.performClick();
		} else if(notic_kind != null && notic_kind.equals("Event")) {
			btn_Other_out.performClick();
		}
	}

	public void init_CouponList() {

		for(int i = 0; i < xml.arItem_coupon.size(); i++) {
			xml.arItem_coupon.get(i).setCompanyName(item.name);
			xml.arItem_coupon.get(i).setAddr(item.addr);
			xml.arItem_coupon.get(i).setFlag(false);
		}
		
		listCoupon = (ListView) findViewById(R.id.listCoupon);
		adapter = new Adapter_Coupon_ShopDetail(Activity_Home_Item.this, xml.arItem_coupon);
		listCoupon.setAdapter(adapter);
		
		listCoupon.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				Intent intent = new Intent(Activity_Home_Item.this, Activity_Coupon.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
				intent.putExtra("item", item);
				intent.putExtra("couponList", xml.arItem_coupon);
				intent.putExtra("position", arg2 + "");
				goNextHistory("Activity_Coupon", intent);

			}
		});
	}
	
	public void init_QRCouponList() {

		for(int i = 0; i < xml.arItem_QRCoupon.size(); i++) {
			xml.arItem_QRCoupon.get(i).setCompanyName(item.name);
			xml.arItem_QRCoupon.get(i).setCompanyAddr(item.addr);
		}
		
		listQRCoupon = (ListView) findViewById(R.id.listQRCoupon);
		adapterQR = new Adapter_QRCoupon_ShopDetail(Activity_Home_Item.this, xml.arItem_QRCoupon);
		listQRCoupon.setAdapter(adapterQR);
	}

	void init_BtnBack() {
		LinearLayout layout_Back = (LinearLayout) findViewById(R.id.layout_Back);
		layout_Back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				onBackPressed();

			}
		});
	}

	@Override
	public void onBackPressed() {

		item = null;
		if (act != null && act.equals(EZUtil.db_act_favorite)) {
			super.onBackPressed();
		} else if (act != null && act.equals("AR")) {
			if (type.equals("Single")) {
				Intent intent = new Intent(Activity_Home_Item.this, MixView.class);
				intent.putExtra("type", "Single");
				startActivity(intent);
				super.onBackPressed();
			} else if (type.equals("All")) {
				Intent intent = new Intent(Activity_Home_Item.this, MixView.class);
				intent.putExtra("type", "All");
				startActivity(intent);
				super.onBackPressed();
			}
		} else {
			super.onBackPressed();
		}

	}

	public void initBtnFunction() {
		btnPhoto = (Button) findViewById(R.id.btnPhoto);
		btnPhoto.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
				if (xml.arItem_pic.size() > 0) {
					Intent intent = new Intent(Activity_Home_Item.this, Activity_Item_Photo.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putStringArrayListExtra("pic", xml.arItem_pic);
					goNextHistory("Activity_Item_Photo", intent);
				} else {

					showPopupOneOption(activity.getResources().getString(R.string.popup_s_07_NoImage), 1);

				}

			}
		});
		btnMap = (Button) findViewById(R.id.btnMap);
		btnMap.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);

				Intent intent = new Intent(Activity_Home_Item.this, Activity_Item_Map.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("item", item);
				goNextHistory("Activity_Item_Map", intent);
			}
		});
		btnMenu = (Button) findViewById(R.id.btnMenu);
		btnMenu.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);

				if (xml.arItem_menu.size() > 0) {
					Intent intent = new Intent(Activity_Home_Item.this, Activity_Item_Menu.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putParcelableArrayListExtra("menu", xml.arItem_menu);
					goNextHistory("Activity_Item_Menu", intent);
				} else {

					showPopupOneOption(activity.getResources().getString(R.string.popup_s_07_NoMenu), 1);

				}
			}
		});
		btnShare = (Button) findViewById(R.id.btnShare);
		btnShare.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				if (!EZUtil.isLoading) {

					EZUtil.isLoading = true;
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);

					final View view = activity.getLayoutInflater().inflate(R.layout.popup_share, null);

					// btnTwitter = (Button) view.findViewById(R.id.btnTwitter);

					btnFacebook = (LoginButton) view.findViewById(R.id.btnFacebook);
					btnFacebook.fromAct = "Activity_Home_Item";
					btnFacebook.init(activity.getParent(), AUTHORIZE_ACTIVITY_RESULT_CODE, Utility.mFacebook, permissions);

					LinearLayout layout_fb = (LinearLayout) view.findViewById(R.id.layout_fb);
					layout_fb.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
							btnFacebook.performClick();
						}
					});
					// btnTwitter.setOnClickListener(new OnClickListener() {
					//
					// public void onClick(View v) {
					//
					// dialog.dismiss();
					// init_thread_Twitter();
					//
					// }
					// });

					btnKakaotalk = (Button) view.findViewById(R.id.btnKakaotalk);
					btnKakaotalk.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							dialog.dismiss();
							// Recommended: Use application context for
							// parameter.
							KakaoLink kakaoLink = KakaoLink.getLink(getApplicationContext());

							// check, intent is available.
							if (!kakaoLink.isAvailableIntent())
								return;

							String strInstallUrl = "https://play.google.com/store/apps/details?id=covisoft.android.EasyLife";

							ArrayList<Map<String, String>> metaInfoArray = new ArrayList<Map<String, String>>();
							Map<String, String> metaInfoAndroid = new Hashtable<String, String>(1);
							metaInfoAndroid.put("os", "android");
							metaInfoAndroid.put("devicetype", "phone");
							metaInfoAndroid.put("installurl", strInstallUrl);
							metaInfoAndroid.put("executeurl", "EasyLife://");

							// If application is support ios platform.
							Map<String, String> metaInfoIOS = new Hashtable<String, String>(1);
							metaInfoIOS.put("os", "ios");
							metaInfoIOS.put("devicetype", "phone");
							metaInfoIOS.put("installurl", "https://itunes.apple.com/us/app/ezlife/id600776352?ls=1&mt=8");
							metaInfoIOS.put("executeurl", "EasyLife://");

							// add to array
							metaInfoArray.add(metaInfoAndroid);
							metaInfoArray.add(metaInfoIOS);

							/**
							 * @param activity
							 * @param url
							 * @param message
							 * @param appId
							 * @param appVer
							 * @param appName
							 * @param encoding
							 */
							try {
								kakaoLink.openKakaoAppLink(activity, "http://easylife.com.vn", xml.name + "\n" + "Address: " + xml.addr + "\n " + "Tel : " + xml.tel, activity.getPackageName(), activity.getPackageManager().getPackageInfo(getPackageName(), 0).versionName, "EasyLife", EZUtil.encoding, metaInfoArray);
							} catch (NameNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

					});

					dialog = new Dialog(activity.getParent());
					WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
					lp.copyFrom(dialog.getWindow().getAttributes());
					lp.width = WindowManager.LayoutParams.MATCH_PARENT;
					lp.height = WindowManager.LayoutParams.MATCH_PARENT;
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(view, lp);

					dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
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
		});

		btnLike = (Button) findViewById(R.id.btnLike);
		btnLike.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);

				if (!EZUtil.isLoading) {
					if (EasyLifeActivity.user != null) {

						AsyncTask_AddFavourite task = new AsyncTask_AddFavourite();
						task.execute();
						timer = new Timer();
						timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);

					} else {
						Intent intent = new Intent(Activity_Home_Item.this, Activity_Login.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra("no", "itemDetail");
						goNextHistory("Activity_Login", intent);
					}
				}
			}
		});

		btnPhone = (Button) findViewById(R.id.btnPhone);
		if (xml.tel.trim().equals("")) {
			btnPhone.setBackgroundResource(R.drawable.s_05phone_none);
		} else {
			btnPhone.setBackgroundResource(R.drawable.s_05phone);
		}
		btnPhone.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);

				if (xml != null && !xml.tel.equals("")) {
					String s_tel = item.tel;
					Uri number = Uri.parse("tel:" + s_tel);
					Intent intent_tel = new Intent(Intent.ACTION_DIAL, number);
					startActivity(intent_tel);
				} else {
					showPopupOneOption(activity.getResources().getString(R.string.popup_NoPhoneNumber), 1);
				}
			}
		});
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

	public void init_Text() {
		ImageView image = (ImageView) findViewById(R.id.img);

		imageLoader.DisplayImage(xml.s_logo, image, R.drawable.s_05_noimage, EZUtil.REQUIRED_SIZE_MIDDLE);

		txtTopName = (TextView) findViewById(R.id.txtTopName);
		txtName = (ScrollTextView) findViewById(R.id.txtName);
		txtAddress = (ScrollTextView) findViewById(R.id.txtAddress);

		txtTopName.setText(xml.name);
		txtName.setText(xml.name);
		txtAddress.setText(xml.addr);
	}

	// void init_thread_Twitter() {
	// init_progressDialog();
	// Handler mHandler = new Handler() {
	// public void handleMessage(Message msg) {
	// progressDialog.dismiss();
	//
	// }
	// };
	//
	// InitThread_Twitter thread = new InitThread_Twitter();
	//
	// thread.mHandler = mHandler;
	// thread.setDaemon(true);
	// thread.start();
	// }
	//
	// class InitThread_Twitter extends Thread {
	// Handler mHandler;
	//
	// public void run() {
	// if (TwitterUtils.isAuthenticated(prefs)) {
	// sendTweet();
	// } else {
	// Intent i = new Intent(getApplicationContext(),
	// PrepareRequestTokenActivity.class);
	// i.putExtra("tweet_msg", getTweetMsg());
	// startActivity(i);
	// }
	// mHandler.sendEmptyMessage(0);
	// }
	// }

	// ----------- Add favorite --------------------------------------
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

			service_Favorite_Add xml = new service_Favorite_Add();
			if (act.equals(EZUtil.db_act_general) || act.equals(EZUtil.db_act_favorite)) {
				xml = new service_Favorite_Add(EasyLifeActivity.user.getUsername(), item.no + "", "0", Activity_Home_Item.this);
			} else if (act.equals(EZUtil.db_act_franchise)) {
				xml = new service_Favorite_Add(EasyLifeActivity.user.getUsername(), item.no + "", "1", Activity_Home_Item.this);
			}

			String addResult = xml.start();

			return addResult;
		}

		@Override
		protected void onPostExecute(String addResult) {

			timer.cancel();
			EZUtil.cancelProgress();

			final String result = addResult;
			Timer t = new Timer();
			t.schedule(new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					activity.runOnUiThread(new Runnable() {
						public void run() {
							if (result.equals("1")) {

								showPopupOneOption(activity.getResources().getString(R.string.popup_Favourite_AddSuccessful), 1);

							} else if (result.equals("0")) {

								showPopupOneOption(activity.getResources().getString(R.string.popup_Favourite_Existed), 1);

							}
						}
					});
				}
			}, 300);

			// super.onPostExecute(addResult);
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

	public class LogoutRequestListener extends BaseRequestListener {
		@Override
		public void onComplete(String response, final Object state) {
			/*
			 * callback should be run in the original thread, not the background
			 * thread
			 */
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					SessionEvents.onLogoutFinish();

					Toast toast = Toast.makeText(activity.getParent(), "Update Status executed", Toast.LENGTH_SHORT);
					toast.show();
				}
			});
		}
	}

	@Override
	protected void onResume() {

		if (Utility.mFacebook != null ) {
			if (Utility.mFacebook.isSessionValid()) {
				Utility.mFacebook.extendAccessTokenIfNeeded(this, null);
			}
		}
		super.onResume();
		if(listCoupon != null && listCoupon.getVisibility() == View.VISIBLE) {
			listCoupon.setVisibility(View.GONE);
			listCoupon.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		/*
		 * if this is the activity result from authorization flow, do a call
		 * back to authorizeCallback Source Tag: login_tag
		 */
		case AUTHORIZE_ACTIVITY_RESULT_CODE: {
			Utility.mFacebook.authorizeCallback(requestCode, resultCode, data);
			break;
		}
		}
	}

	void init_comment_star() {

		edittext = (EditText) findViewById(R.id.edittext);
		edittext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(EasyLifeActivity.user == null) {
					Intent intent = new Intent(Activity_Home_Item.this, Activity_Login.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("no", "itemDetail");
					goNextHistory("Activity_Login", intent);
				}
			}
		});
		
		edittext.clearFocus();

		imgCmt = (ImageView) findViewById(R.id.imgCmt);
		btn_star_1 = (Button) findViewById(R.id.btn_star_1);
		btn_star_2 = (Button) findViewById(R.id.btn_star_2);
		btn_star_3 = (Button) findViewById(R.id.btn_star_3);
		btn_star_4 = (Button) findViewById(R.id.btn_star_4);
		btn_star_5 = (Button) findViewById(R.id.btn_star_5);

		btn_star_1.setOnClickListener(mClickListener_Tab);
		btn_star_2.setOnClickListener(mClickListener_Tab);
		btn_star_3.setOnClickListener(mClickListener_Tab);
		btn_star_4.setOnClickListener(mClickListener_Tab);
		btn_star_5.setOnClickListener(mClickListener_Tab);
	}

	Button.OnClickListener mClickListener_Tab = new Button.OnClickListener() {
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.btn_star_1:
				imgCmt.setBackgroundResource(R.drawable.star01);
				grade = "1";
				break;
			case R.id.btn_star_2:
				imgCmt.setBackgroundResource(R.drawable.star02);
				grade = "2";
				break;
			case R.id.btn_star_3:
				imgCmt.setBackgroundResource(R.drawable.star03);
				grade = "3";
				break;
			case R.id.btn_star_4:
				imgCmt.setBackgroundResource(R.drawable.star04);
				grade = "4";
				break;
			case R.id.btn_star_5:
				imgCmt.setBackgroundResource(R.drawable.star05);
				grade = "5";
				break;
			}
		}
	};

	void init_comment() {
		init_btn_comment();
		init_comment_star();
	}

	void init_btn_comment() {
		btnComment = (Button) findViewById(R.id.btnComment);
		btnComment.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				final String cont = edittext.getText().toString().trim();

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);

				if (cont.equals("")) {

					showPopupOneOption(activity.getResources().getString(R.string.popup_s_07_CommentNull), 1);

				} else if(grade.equals("0")) {
					showPopupOneOption(activity.getResources().getString(R.string.popup_s_07_CommentZero), 1);
				} else {
					if (EZUtil.isNetworkConnected(activity)) {

						AsyncTask_Comment task = new AsyncTask_Comment();
						task.execute();
						timer = new Timer();
						timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);

					} else {
						showPopupOneOption(activity.getResources().getString(R.string.No_Internet_now), 2);
					}
				}
			}
		});
	}

	// ----------- Comment --------------------------------------
	private class AsyncTask_Comment extends AsyncTask<Void, Void, String> {

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

			String boardName = "";
			if (act.equals(EZUtil.db_act_franchise)) {
				boardName = "B01";
			} else {
				boardName = "B02";
			}

			final String cont = edittext.getText().toString().trim();

			service_Shop_Comment service_Cmt = new service_Shop_Comment(Activity_Home_Item.this, xml.no, cont, grade, boardName, EasyLifeActivity.user.getUsername());
			String result = service_Cmt.start();

			return result;
		}

		@Override
		protected void onPostExecute(String result) {

			timer.cancel();

			EZUtil.cancelProgress();
			if (result.endsWith("Y")) {
				activity.onBackPressed();

				Intent intent = new Intent(activity, Activity_Home_Item.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("no", no);
				intent.putExtra("act", act);
				if (shopType != null) {
					intent.putExtra("shopType", shopType);
				}
				intent.putExtra("parentCategoryNo", parentCategoryNo);
				goNextHistory("Activity_Home_Item", intent);
			} else {
				Timer t = new Timer();
				t.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						activity.runOnUiThread(new Runnable() {
							public void run() {

								showPopupOneOption(activity.getResources().getString(R.string.popup_HomeItem_CannotComment), 1);

							}
						});
					}
				}, 300);
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

	private String getTweetMsg() {
		String text;
		if (xml.name.compareTo("") == 0 && xml.addr.equals("")) {

			showPopupOneOption(activity.getResources().getString(R.string.errortwiteer), 1);

		}
		text = xml.name + " - " + xml.addr;
		if (text.length() > 140) {
			text.substring(0, 135);
		}
		return text;
	}

	public void sendTweet() {
		Thread t = new Thread() {
			public void run() {

				try {
					TwitterUtils.sendTweet(prefs, getTweetMsg());
					mTwitterHandler.post(mUpdateTwitterNotification);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		};
		t.start();
	}

	/*
	 * The Callback for notifying the application when log out starts and
	 * finishes.
	 */
	public class FbAPIsLogoutListener implements LogoutListener {
		@Override
		public void onLogoutBegin() {
			// mText.setText("Logging out...");
		}

		@Override
		public void onLogoutFinish() {
		}
	}

	final Runnable mUpdateTwitterNotification = new Runnable() {
		public void run() {

			showPopupOneOption(activity.getResources().getString(R.string.text_twitter), 1);

		}
	};

	void init_comment_list() {
		int size = xml.arItem_comment.size();
		TextView txt_NoComment = (TextView) findViewById(R.id.txt_NoComment);
		if (size > 0) {
			txt_NoComment.setVisibility(View.GONE);
		} else {
			txt_NoComment.setVisibility(View.VISIBLE);
		}
		for (int i = 0; i < size; i++) {
			String cont = xml.arItem_comment.get(i).cont;
			String goodNum = xml.arItem_comment.get(i).goodNum;
			String regDate = xml.arItem_comment.get(i).regDate;
			String user = xml.arItem_comment.get(i).user;
			init_comment_add(cont, goodNum, regDate, user);
		}
	}

	void init_comment_add(String cont, String goodNum, String regDate, String user) {
		LinearLayout LinearLayout_Event = (LinearLayout) findViewById(R.id.LinearLayout_Comment);

		View view = View.inflate(activity, R.layout.listview_item_comment, null);

		TextView textview = (TextView) view.findViewById(R.id.textview);
		textview.setText(cont);

		TextView textview_date = (TextView) view.findViewById(R.id.textview_date);
		textview_date.setText(regDate);

		TextView txt_User = (TextView) view.findViewById(R.id.txt_User);
		txt_User.setText(user);

		Button btn_star_1 = (Button) view.findViewById(R.id.btn_star_1);
		Button btn_star_2 = (Button) view.findViewById(R.id.btn_star_2);
		Button btn_star_3 = (Button) view.findViewById(R.id.btn_star_3);
		Button btn_star_4 = (Button) view.findViewById(R.id.btn_star_4);
		Button btn_star_5 = (Button) view.findViewById(R.id.btn_star_5);

		btn_star_1.setBackgroundResource(R.drawable.star_off);
		btn_star_2.setBackgroundResource(R.drawable.star_off);
		btn_star_3.setBackgroundResource(R.drawable.star_off);
		btn_star_4.setBackgroundResource(R.drawable.star_off);
		btn_star_5.setBackgroundResource(R.drawable.star_off);

		if (goodNum.equals("1")) {
			btn_star_1.setBackgroundResource(R.drawable.star_on);
		} else if (goodNum.equals("2")) {
			btn_star_1.setBackgroundResource(R.drawable.star_on);
			btn_star_2.setBackgroundResource(R.drawable.star_on);
		} else if (goodNum.equals("3")) {
			btn_star_1.setBackgroundResource(R.drawable.star_on);
			btn_star_2.setBackgroundResource(R.drawable.star_on);
			btn_star_3.setBackgroundResource(R.drawable.star_on);
		} else if (goodNum.equals("4")) {
			btn_star_1.setBackgroundResource(R.drawable.star_on);
			btn_star_2.setBackgroundResource(R.drawable.star_on);
			btn_star_3.setBackgroundResource(R.drawable.star_on);
			btn_star_4.setBackgroundResource(R.drawable.star_on);
		} else if (goodNum.equals("5")) {
			btn_star_1.setBackgroundResource(R.drawable.star_on);
			btn_star_2.setBackgroundResource(R.drawable.star_on);
			btn_star_3.setBackgroundResource(R.drawable.star_on);
			btn_star_4.setBackgroundResource(R.drawable.star_on);
			btn_star_5.setBackgroundResource(R.drawable.star_on);
		}

		LinearLayout_Event.addView(view);
	}

	public void initBtnAll() {
		btnAllCoupon = (Button) findViewById(R.id.btnAllCoupon);
		btnAllCoupon.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);

				if (xml.arItem_coupon.size() > 0) {
					Intent intent = new Intent(Activity_Home_Item.this, Activity_Coupon.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					intent.putExtra("item", item);
					intent.putExtra("couponList", xml.arItem_coupon);
					intent.putExtra("position", "");
					goNextHistory("Activity_Coupon", intent);
				}
			}
		});
		
		btnAllQRCoupon = (Button)findViewById(R.id.btnAllQRCoupon);
		btnAllQRCoupon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
				
				Intent intent = new Intent(Activity_Home_Item.this, Activity_QRCoupon_Shop.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("qrCoupon", xml.arItem_QRCoupon.get(0));
				goNextHistory("Activity_QRCoupon_Shop", intent);
			}
		});
	}

	public void initTop() {

		RelativeLayout layoutTop = (RelativeLayout) findViewById(R.id.layoutTop);
		switch (Integer.parseInt(parentCategoryNo)) {
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
		case 100:
			layoutTop.setBackgroundResource(R.drawable.top_name);
			break;
		case 200:
			layoutTop.setBackgroundResource(R.drawable.top_recommand_notext);
			break;
		default:
			break;
		}
	}

	public void showPopupOneOption(String content, int type) {

		final int fiType = type;
		if (!EZUtil.isLoading) {
			EZUtil.isLoading = true;

			dialog = new Dialog(activity.getParent(), R.style.myBackgroundStyle);
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
