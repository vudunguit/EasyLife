package covisoft.android.tab3_Home;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ecs.android.sample.twitter.PrepareRequestTokenActivity;
import com.ecs.android.sample.twitter.TwitterUtils;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.LoginButton;
import com.facebook.android.SessionEvents.AuthListener;
import com.facebook.android.SessionEvents.LogoutListener;
import com.facebook.android.Utility;

import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.R;
import covisoft.kakaotalk.KakaoLink;

public class Activity_Recommend extends Activity {

	private Activity activity;
	
	private Button btnKakaotalk;
//	private Button btnTwitter;
	private LoginButton btnFacebook;
	private Button btnWebsite;
	
	private final Handler mTwitterHandler = new Handler();
	private SharedPreferences prefs;
	String[] permissions = { "offline_access", "publish_stream", "user_photos", "publish_checkins", "photo_upload" };

	public static final String category = "Activity_Home_Item";
	public static final String PARAM_FORWARD_CLASS = "forwardClass";
	public static final String LOGOUT_ID = "com.nhn.android.me2day.Logout";

	private Handler mHandler = new Handler();

// ============================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
		setContentView(R.layout.layout_23_share);

		
		activity = this;

		initButton();

		if (Utility.mFacebook.isSessionValid()) {
			requestUserData();
		}
	}

	@Override
	protected void onResume() {
		LoginButton.fromAct = "Activity_Recommend";
		if (Utility.mFacebook != null && Utility.mFacebook.isSessionValid()) {
			Utility.mFacebook.extendAccessTokenIfNeeded(this, null);
		}
		
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	private String getTweetMsg() {
		return "WingCoupons";

	}

	final Runnable mUpdateTwitterNotification = new Runnable() {
		public void run() {

			final Dialog dialog = new Dialog(Activity_Recommend.this.getParent(), R.style.myBackgroundStyle);
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			lp.copyFrom(dialog.getWindow().getAttributes());
			lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
			lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.popup_one_option);

			TextView txt = (TextView) dialog.findViewById(R.id.txtContent);
			txt.setText(getString(R.string.text_twitter));

			Button btn_OK = (Button) dialog.findViewById(R.id.btn_OK);
			btn_OK.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			dialog.getWindow().setLayout(
					WindowManager.LayoutParams.WRAP_CONTENT,
					WindowManager.LayoutParams.WRAP_CONTENT);
			dialog.show();
		}
	};

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

//	private void clearCredentials() {
//		SharedPreferences prefs = PreferenceManager
//				.getDefaultSharedPreferences(this);
//		final Editor edit = prefs.edit();
//		edit.remove(OAuth.OAUTH_TOKEN);
//		edit.remove(OAuth.OAUTH_TOKEN_SECRET);
//		edit.commit();
//	}

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
			// mText.setText("Login Failed: " + error);
		}
	}

	/*
	 * Request user name, and picture to show on the main screen.
	 */
	public void requestUserData() {
		// mText.setText("Fetching user name, profile pic...");
		Bundle params = new Bundle();
		params.putString("fields", "name, picture");
		Utility.mAsyncRunner.request("me", params, new UserRequestListener());
	}

	public class UserRequestListener extends BaseRequestListener {

		@Override
		public void onComplete(final String response, final Object state) {
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(response);

//				final String picURL = jsonObject.getString("picture");
//				final String name = jsonObject.getString("name");
				Utility.userUID = jsonObject.getString("id");

				mHandler.post(new Runnable() {
					@Override
					public void run() {
					}
				});

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void initButton() {
		
		
		btnFacebook = (LoginButton) findViewById(R.id.btnFacebook);
		LoginButton.fromAct = "Activity_Recommend";
		btnFacebook.init(activity,Activity_Home_Item.AUTHORIZE_ACTIVITY_RESULT_CODE, Utility.mFacebook, permissions);
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		
//		btnTwitter = (Button) findViewById(R.id.btnTwitter);
//		btnTwitter.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//
//				init_thread_Twitter();
//			}
//		});
		
		
		btnKakaotalk = (Button) findViewById(R.id.btnKakaotalk);
		btnKakaotalk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Recommended: Use application context for parameter.
				KakaoLink kakaoLink = KakaoLink.getLink(getApplicationContext());

				// check, intent is available.
				if (!kakaoLink.isAvailableIntent()) {
					showPopupOneOption(getString(R.string.popup_Share_NoKakaotalk));
				} else {

					String strInstallUrl = "https://play.google.com/store/apps/details?id=covisoft.android.EasyLife"; 
					
					ArrayList<Map<String, String>> metaInfoArray = new ArrayList<Map<String,String>>();
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
						kakaoLink.openKakaoAppLink(activity, 
								"http://easylife.com.vn", 
								"Ăn uống, Giao hàng, Làm đẹp, Beer/Rượu, Chuỗi cửa hàng, Dịch vụ, Giải trí, Y tế, Khuyến mãi và các sự kiện khác. \n Ứng dung tốt nhất cung cấp thông tin chi tiết của các cửa hàng xung quanh bạn.", 
								activity.getPackageName(), 
								activity.getPackageManager().getPackageInfo(getPackageName(), 0).versionName, 
								"EasyLife", 
								EZUtil.encoding,
								metaInfoArray);
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});
		
		btnWebsite = (Button) findViewById(R.id.btnWebsite);
		btnWebsite.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.easylife.com.vn"));
				startActivity(intent);

			}
		});
	}

	ProgressDialog progressDialog;

	void init_progressDialog() {
		progressDialog = ProgressDialog.show(Activity_Recommend.this, "",
				"Loading...", true, true);
	}

	void init_thread_Twitter() {
		init_progressDialog();
		Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {
				progressDialog.dismiss();
			}
		};

		InitThread_Twitter thread = new InitThread_Twitter();

		thread.mHandler = mHandler;
		thread.setDaemon(true);
		thread.start();
	}

	class InitThread_Twitter extends Thread {
		Handler mHandler;

		public void run() {
			if (TwitterUtils.isAuthenticated(prefs)) {
				sendTweet();
			} else {
				Intent i = new Intent(getApplicationContext(),
						PrepareRequestTokenActivity.class);
				i.putExtra("tweet_msg", getTweetMsg());
				startActivity(i);
			}
			mHandler.sendEmptyMessage(0);
		}
	}
	
	public void showPopupOneOption(String content) {

		final Dialog dialog = new Dialog(activity, R.style.myBackgroundStyle);
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

	}
}
