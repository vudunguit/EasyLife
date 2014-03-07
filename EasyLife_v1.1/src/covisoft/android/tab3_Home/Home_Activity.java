package covisoft.android.tab3_Home;

import java.util.ArrayList;
import java.util.Timer;

import lib.etc.Gps2;
import lib.imageLoader.ScrollTextView;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.Facebook;
import com.facebook.android.SessionEvents;
import com.facebook.android.SessionEvents.AuthListener;
import com.facebook.android.SessionEvents.LogoutListener;
import com.facebook.android.SessionStore;
import com.facebook.android.Utility;
import com.google.android.gcm.GCMRegistrar;
import com.google.zxing.client.android.CaptureActivity;

import covisoft.android.EasyLife.CheckTimeAsyncTask;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.EasyLifeActivity;
import covisoft.android.EasyLife.R;
import covisoft.android.EasyLife.ServerUtilities;
import covisoft.android.EasyLife.TabpageActivity;
import covisoft.android.EasyLife.viewFlipper_Banner;
import covisoft.android.item.item_Banner;
import covisoft.android.item.item_Notification;
import covisoft.android.item.item_total_store_category;
import covisoft.android.services.service_Banner;
import covisoft.android.services.service_Notification;
import covisoft.android.services.service_PromotionText;
import covisoft.android.tab5.Activity_NotifyList;
import covisoft.android.tabhost.NavigationActivity;

/*
 * Main Activity
 * 
 * Last Updated: 14.06.2013
 * Last Updater: Huan
 * Update Info:
 *         - Remove set value for EZUtil.curSubCategoryNo when click in button
 * 
 */

public class Home_Activity extends NavigationActivity {

//	private final String TAG = "Home_Activity";

	private Activity activity;
	public static NavigationActivity navi;
	
	private RelativeLayout btn01;
	private TextView txtQuantity_01;
	private RelativeLayout btn02;
	private TextView txtQuantity_02;
	private RelativeLayout btn03;
	private TextView txtQuantity_03;
	private RelativeLayout btn04;
	private TextView txtQuantity_04;
	private RelativeLayout btn05;
	private TextView txtQuantity_05;
	private RelativeLayout btn06;
	private TextView txtQuantity_06;
	private RelativeLayout btn07;
	private TextView txtQuantity_07;
	private RelativeLayout btn08;
	private TextView txtQuantity_08;
	private RelativeLayout btn09;
	private TextView txtQuantity_09;
	private Button btn10;
	private Button btn11;

	private ArrayList<String> arBoardTitle = new ArrayList<String>();
	private ArrayList<item_Banner> arItemBanner = new ArrayList<item_Banner>();
	private ArrayList<item_total_store_category> arCategory = new ArrayList<item_total_store_category>();

	private ArrayList<item_Notification> arNotify = null;
	
	private Timer timer;

	public static double latitude = 0;
	public static double longitude = 0;
	
	public static boolean GpsOnOff= false;
	
	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
	
	AsyncTask<Void, Void, Void> mRegisterTask;
// =================================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_03);
		
		activity = this;
		navi = this;
		
		settings = getSharedPreferences("Notification", MODE_WORLD_WRITEABLE);
		editor = settings.edit();
		editor.putString("notification", "");
		editor.commit();
			
		if(EasyLifeActivity.regId.equals("")) {
			EasyLifeActivity.regId = GCMRegistrar.getRegistrationId(this);
			
			// Device is already registered on GCM, check server.
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.
				// mDisplay.append(getString(R.string.already_registered) +
				// "\n");
				ServerUtilities.register(this, EasyLifeActivity.regId, "null");
				// mRegisterTask.execute(null, null, null);
			} else {
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {

						boolean registered = ServerUtilities.register(context, EasyLifeActivity.regId, "null");
						// At this point all attempts to register with the app
						// server failed, so we need to unregister the device
						// from GCM - the app will try to register again when
						// it is restarted. Note that GCM will send an
						// unregistered callback upon completion, but
						// GCMIntentService.onUnregistered() will ignore it.
						if (!registered) {
							GCMRegistrar.unregister(context);
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}  

				};
				mRegisterTask.execute(null, null, null);
			}
		}
		initButton();
		if (!EZUtil.isNetworkConnected(activity)) {
			final Dialog dialog = new Dialog(activity.getParent(), R.style.myBackgroundStyle);
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			lp.copyFrom(dialog.getWindow().getAttributes());
			lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
			lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.popup_one_option);

			TextView txt = (TextView) dialog.findViewById(R.id.txtContent);
			txt.setText(activity.getResources().getString(R.string.No_Internet));

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
					activity.finish();
				}
			});
		} else {
			
			location();
			AsyncTaskRequestData task = new AsyncTaskRequestData();
			task.execute();
			timer = new Timer();
			timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);
		}
		
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.e("close app", "closed");
		super.onDestroy();
	}
	@Override
	protected void onResume() {
		super.onResume();
		Utility.mFacebook = new Facebook(Utility.mAPP_ID);
		// Instantiate the asynrunner object for asynchronous api calls.
		Utility.mAsyncRunner = new AsyncFacebookRunner(Utility.mFacebook);

		// restore session if one exists
		SessionStore.restore(Utility.mFacebook, this);
		SessionEvents.addAuthListener(new FbAPIsAuthListener());
		SessionEvents.addLogoutListener(new FbAPIsLogoutListener());
		if (GpsOnOff)
		{
			GpsOnOff = false;
			location();
		}
	}

	void location() {

		 Gps2 gps2 = new Gps2(this);
		 gps2.getLocation();

	}
// ===============================================================================================================

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

			service_Banner xml = new service_Banner(Home_Activity.this, "1");
			arItemBanner = xml.start();

//			getxml_total_store_category xmlCategory = new getxml_total_store_category(Home_Activity.this);
//			arCategory = xmlCategory.start();

			service_PromotionText xmlBoard1 = new service_PromotionText(Home_Activity.this);
			arBoardTitle = xmlBoard1.start();

			service_Notification xmlNotify;
			if(EasyLifeActivity.user != null) {
				xmlNotify = new service_Notification(activity, EasyLifeActivity.user.getUsername());
			} else {
				xmlNotify = new service_Notification(activity, "");
			}
			
			arNotify = xmlNotify.start();
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			timer.cancel();

			EZUtil.cancelProgress();
			banner();
//			init_QuantityText();
			init_Notic();
			
			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());    
			String ret = settings.getString("notification", "");
			
			if(ret.equals("new")) {
		        SharedPreferences.Editor editor = settings.edit();
		        editor.putString("notification", "");
		        editor.commit();
				
				Intent intent = new Intent(activity, Activity_NotifyList.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				Home_Activity.navi.goNextHistory("Activity_NotifyList", intent);
			}
			
			if(arNotify.size() > 0 && arNotify.size() < 100) {
				TabpageActivity.rel_Notification.setVisibility(View.VISIBLE);
				TabpageActivity.txt_Number.setText(arNotify.size() + "");
			} else if(arNotify.size() >= 100) {
				TabpageActivity.rel_Notification.setVisibility(View.VISIBLE);
				TabpageActivity.txt_Number.setText("...");
			} else {
				TabpageActivity.rel_Notification.setVisibility(View.GONE);
			}
			
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {

			timer.cancel();
			EZUtil.cancelProgress();
		}
	}

// ====================================================================================================================
	void init_QuantityText() {
		txtQuantity_01 = (TextView) findViewById(R.id.txtQuantity_01);
		txtQuantity_01.setVisibility(View.GONE);
		txtQuantity_02 = (TextView) findViewById(R.id.txtQuantity_02);
		txtQuantity_03 = (TextView) findViewById(R.id.txtQuantity_03);
		txtQuantity_04 = (TextView) findViewById(R.id.txtQuantity_04);
		txtQuantity_05 = (TextView) findViewById(R.id.txtQuantity_05);
		txtQuantity_06 = (TextView) findViewById(R.id.txtQuantity_06);
		txtQuantity_07 = (TextView) findViewById(R.id.txtQuantity_07);
		txtQuantity_08 = (TextView) findViewById(R.id.txtQuantity_08);
		txtQuantity_09 = (TextView) findViewById(R.id.txtQuantity_09);

		for (int i = 0; i < arCategory.size(); i++) {
			if (arCategory.get(i).getNo() == 1) {

				txtQuantity_02.setText(arCategory.get(i).getTotal() + ""); // food

			} else if (arCategory.get(i).getNo() == 4) {

				txtQuantity_03.setText(arCategory.get(i).getTotal() + ""); // delivery

			} else if (arCategory.get(i).getNo() == 5) {

				txtQuantity_08.setText(arCategory.get(i).getTotal() + ""); // health

			} else if (arCategory.get(i).getNo() == 8) {

				txtQuantity_04.setText(arCategory.get(i).getTotal() + ""); // spa

			} else if (arCategory.get(i).getNo() == 9) {

				txtQuantity_05.setText(arCategory.get(i).getTotal() + ""); // beer

			} else if (arCategory.get(i).getNo() == 10) {

				txtQuantity_07.setText(arCategory.get(i).getTotal() + ""); // entertainment

			} else if (arCategory.get(i).getNo() == 11) {

				txtQuantity_06.setText(arCategory.get(i).getTotal() + ""); // service

			} else if (arCategory.get(i).getNo() == 150) {

				txtQuantity_09.setText(arCategory.get(i).getTotal() + ""); // franchise
			} else if (arCategory.get(i).getNo() == 24) {

				txtQuantity_01.setText(arCategory.get(i).getTotal() + ""); // recommend

			}
		}

	}

	public void init_Notic() {

		String text = "";

		for (int i = 0; i < arBoardTitle.size(); i++) {

			String content = arBoardTitle.get(i) + "          ";
			
			text += content;

		}

		ScrollTextView txtNotic = (ScrollTextView) findViewById(R.id.txtNotic);
		txtNotic.setText(text);
		txtNotic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				TabpageActivity.btnThree.setVisibility(View.VISIBLE);
				TabpageActivity.btnThree.setBackgroundResource(R.drawable.footer_home_click);
				TabpageActivity.btnSearch.setVisibility(View.GONE);

				Intent intent = new Intent(activity, Activity_Notic_List.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				goNextHistory("Activity_Notic_List", intent);

			}
		});

	}

	void banner() {
		RelativeLayout banner = (RelativeLayout) findViewById(R.id.banner);
		viewFlipper_Banner.init(TabpageActivity.activity, banner, arItemBanner);

	}

	public void initButton() {

		btn01 = (RelativeLayout) findViewById(R.id.btn01);
		btn02 = (RelativeLayout) findViewById(R.id.btn02);
		btn03 = (RelativeLayout) findViewById(R.id.btn03);
		btn04 = (RelativeLayout) findViewById(R.id.btn04);
		btn05 = (RelativeLayout) findViewById(R.id.btn05);
		btn06 = (RelativeLayout) findViewById(R.id.btn06);
		btn07 = (RelativeLayout) findViewById(R.id.btn07);
		btn08 = (RelativeLayout) findViewById(R.id.btn08);
		btn09 = (RelativeLayout) findViewById(R.id.btn09);
		btn10 = (Button) findViewById(R.id.btn10);
		btn11 = (Button) findViewById(R.id.btn11);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		int width = metrics.widthPixels;

		ViewGroup.LayoutParams lp1 = btn01.getLayoutParams();
		lp1.width = width / 4;
		lp1.height = width / 4;

		ViewGroup.LayoutParams lp2 = btn02.getLayoutParams();
		lp2.width = width / 5;
		lp2.height = width / 5;

		ViewGroup.LayoutParams lp3 = btn03.getLayoutParams();
		lp3.width = width / 5;
		lp3.height = width / 5;
		ViewGroup.LayoutParams lp4 = btn04.getLayoutParams();
		lp4.width = width / 5;
		lp4.height = width / 5;
		ViewGroup.LayoutParams lp5 = btn05.getLayoutParams();
		lp5.width = width / 5;
		lp5.height = width / 5;
		ViewGroup.LayoutParams lp6 = btn06.getLayoutParams();
		lp6.width = width / 5;
		lp6.height = width / 5;
		ViewGroup.LayoutParams lp7 = btn07.getLayoutParams();
		lp7.width = width / 5;
		lp7.height = width / 5;
		ViewGroup.LayoutParams lp8 = btn08.getLayoutParams();
		lp8.width = width / 5;
		lp8.height = width / 5;
		ViewGroup.LayoutParams lp9 = btn09.getLayoutParams();
		lp9.width = width / 5;
		lp9.height = width / 5;
		ViewGroup.LayoutParams lp10 = btn10.getLayoutParams();
		lp10.width = width / 5;
		lp10.height = width / 5;
		ViewGroup.LayoutParams lp11 = btn11.getLayoutParams();
		lp11.width = width / 5;
		lp11.height = width / 5;

		btn01.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					Intent intent = new Intent(Home_Activity.this, Activity_RecommendationList.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					goNextHistory("Activity_RecommendationList", intent);

					TabpageActivity.btnThree.setVisibility(View.VISIBLE);
					TabpageActivity.btnSearch.setVisibility(View.GONE);
					break;
				case MotionEvent.ACTION_UP:
					break;
				}
				return false;
			}
		});
		btn02.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					Intent intent = new Intent(Home_Activity.this, Activity_SubCategory.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("category", EZUtil.category_code_2);
					goNextHistory("Activity_SubCategory", intent);

					TabpageActivity.btnThree.setVisibility(View.VISIBLE);
					TabpageActivity.btnThree.setBackgroundResource(R.drawable.footer_home_click);
					TabpageActivity.btnSearch.setVisibility(View.GONE);
					break;
				case MotionEvent.ACTION_UP:
					break;
				}
				return false;
			}
		});
		btn03.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					Intent intent = new Intent(Home_Activity.this, Activity_SubCategory.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("category", EZUtil.category_code_1);
					goNextHistory("Activity_SubCategory", intent);

					TabpageActivity.btnThree.setVisibility(View.VISIBLE);
					TabpageActivity.btnThree.setBackgroundResource(R.drawable.footer_home_click);
					TabpageActivity.btnSearch.setVisibility(View.GONE);
					break;
				case MotionEvent.ACTION_UP:
					break;
				}
				return false;
			}
		});
		btn04.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					Intent intent = new Intent(Home_Activity.this, Activity_SubCategory.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("category", EZUtil.category_code_3);
					goNextHistory("Activity_SubCategory", intent);

					TabpageActivity.btnThree.setVisibility(View.VISIBLE);
					TabpageActivity.btnThree.setBackgroundResource(R.drawable.footer_home_click);
					TabpageActivity.btnSearch.setVisibility(View.GONE);
					break;
				case MotionEvent.ACTION_UP:
					break;
				}
				return false;
			}
		});
		btn05.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					Intent intent = new Intent(Home_Activity.this, Activity_SubCategory.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("category", EZUtil.category_code_4);
					goNextHistory("Activity_SubCategory", intent);

					TabpageActivity.btnThree.setVisibility(View.VISIBLE);
					TabpageActivity.btnThree.setBackgroundResource(R.drawable.footer_home_click);
					TabpageActivity.btnSearch.setVisibility(View.GONE);
					break;
				case MotionEvent.ACTION_UP:
					break;
				}
				return false;
			}
		});
		btn06.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					Intent intent = new Intent(Home_Activity.this, Activity_SubCategory.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("category", EZUtil.category_code_7);
					goNextHistory("Activity_SubCategory", intent);

					TabpageActivity.btnThree.setVisibility(View.VISIBLE);
					TabpageActivity.btnThree.setBackgroundResource(R.drawable.footer_home_click);
					TabpageActivity.btnSearch.setVisibility(View.GONE);
					break;
				case MotionEvent.ACTION_UP:
					break;
				}
				return false;
			}
		});
		btn07.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					Intent intent = new Intent(Home_Activity.this, Activity_SubCategory.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("category", EZUtil.category_code_6);
					goNextHistory("Activity_SubCategory", intent);

					TabpageActivity.btnThree.setVisibility(View.VISIBLE);
					TabpageActivity.btnThree.setBackgroundResource(R.drawable.footer_home_click);
					TabpageActivity.btnSearch.setVisibility(View.GONE);
					break;
				case MotionEvent.ACTION_UP:
					break;
				}
				return false;
			}
		});
		btn08.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					Intent intent = new Intent(Home_Activity.this, Activity_SubCategory.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("category", EZUtil.category_code_5);
					goNextHistory("Activity_SubCategory", intent);

					TabpageActivity.btnThree.setVisibility(View.VISIBLE);
					TabpageActivity.btnThree.setBackgroundResource(R.drawable.footer_home_click);
					TabpageActivity.btnSearch.setVisibility(View.GONE);
					break;
				case MotionEvent.ACTION_UP:
					break;
				}
				return false;
			}
		});
		btn09.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					Intent intent = new Intent(Home_Activity.this, Activity_Franchise_Category.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					goNextHistory("Activity_Franchise_Group", intent);

					TabpageActivity.btnThree.setVisibility(View.VISIBLE);
					TabpageActivity.btnThree.setBackgroundResource(R.drawable.footer_home_click);
					TabpageActivity.btnSearch.setVisibility(View.GONE);
					break;
				case MotionEvent.ACTION_UP:
					break;
				}
				return false;
			}
		});
		btn10.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
	
						if (EasyLifeActivity.user != null) {
							Intent intent = new Intent(Home_Activity.this, CaptureActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							goNextHistory("CaptureActivity", intent);
						} else {
							Intent intent = new Intent(Home_Activity.this, Activity_Login.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
							intent.putExtra("no", "Home");
							goNextHistory("Activity_Login", intent);
						}
	
						TabpageActivity.btnThree.setVisibility(View.VISIBLE);
						TabpageActivity.btnThree.setBackgroundResource(R.drawable.footer_home_click);
						TabpageActivity.btnSearch.setVisibility(View.GONE);
						break;
					case MotionEvent.ACTION_UP:
						break;
				}
				return false;
			}
		});
		btn11.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

//					Intent intent = new Intent(Home_Activity.this, Activity_Setting.class);
//					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					goNextHistory("Activity_Setting", intent);
//
//					TabpageActivity.btnThree.setVisibility(View.VISIBLE);
//					TabpageActivity.btnThree.setBackgroundResource(R.drawable.footer_home_click);
//					TabpageActivity.btnSearch.setVisibility(View.GONE);
					TabpageActivity.btnThree.setVisibility(View.VISIBLE);
					TabpageActivity.btnThree.setBackgroundResource(R.drawable.footer_home_click);
					TabpageActivity.btnSearch.setVisibility(View.GONE);

					Intent intent = new Intent(activity, Activity_Notic_List.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					goNextHistory("Activity_Notic_List", intent);
					
					
					break;
				case MotionEvent.ACTION_UP:
					break;
				}
				
				return false;
			}
		});

	}
	
	/*
	 * The Callback for notifying the application when authorization succeeds or
	 * fails.
	 */

	public class FbAPIsAuthListener implements AuthListener {

		@Override
		public void onAuthSucceed() {
			requestUserData();
		}

		@Override
		public void onAuthFail(String error) {
		}
	}

	/*
	 * The Callback for notifying the application when log out starts and
	 * finishes.
	 */
	public class FbAPIsLogoutListener implements LogoutListener {
		@Override
		public void onLogoutBegin() {
		}

		@Override
		public void onLogoutFinish() {
		}
	}
	public void requestUserData() {
		Bundle params = new Bundle();
		params.putString("fields", "name, picture, gender, username, email");
		Utility.mAsyncRunner.request("me", params, new UserRequestListener());
	}
	public class UserRequestListener extends BaseRequestListener {

		@Override
		public void onComplete(final String response, final Object state) {
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(response);

				final String picURL = jsonObject.getJSONObject("picture").getJSONObject("data").getString("url");
				final String name = jsonObject.getString("name");
				Utility.userUID = jsonObject.getString("id");
				final String gender = jsonObject.getString("gender");
				String username = jsonObject.getString("gender");
				String email = jsonObject.getString("email");

				Log.e("getUserInfo", name + " - " + Utility.userUID + " - " + gender + " - " + picURL + " - " + username + " - " + email);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
	
}
