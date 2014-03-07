package covisoft.android.tab3_Home;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.BaseRequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.LoginButton;
import com.facebook.android.SessionEvents;
import com.facebook.android.SessionEvents.AuthListener;
import com.facebook.android.SessionEvents.LogoutListener;
import com.facebook.android.SessionStore;
import com.facebook.android.Utility;

import covisoft.android.EasyLife.R;
import covisoft.android.tabhost.NavigationActivity;
import covisoft.kakaotalk.KakaoLink;

public class Activity_SocialNetwork extends NavigationActivity {

	private LinearLayout layout_Back;
	public static CheckBox cb_fb;
	private LoginButton btnFacebook;
	private RelativeLayout layout_fb;

//	private RelativeLayout layout_twitter;
//	private CheckBox cb_twitter;
	private RelativeLayout layout_kakaotalk;
	private CheckBox cb_kakaotalk;

	private Handler mHandler;

//	private final Handler mTwitterHandler = new Handler();

	public Activity activity = null;
	private String[] permissions = { "offline_access", "publish_stream", "user_photos", "publish_checkins", "photo_upload" };
	final static int AUTHORIZE_ACTIVITY_RESULT_CODE = 0;

	private Dialog dialog;
	
	// ------------------------------------------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_social_network);

		activity = this;
		mHandler = new Handler();

		if (Utility.mFacebook.isSessionValid()) {
			requestUserData();
		}
				
	}

	public void init() {
		initBtnBack();
		init_Fb();
		init_Kakaotalk();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		
		init();
		super.onResume();

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case AUTHORIZE_ACTIVITY_RESULT_CODE: {
				Utility.mFacebook.authorizeCallback(requestCode, resultCode, data);
				if (btnFacebook != null && cb_fb != null) {
					if (Utility.mFacebook.isSessionValid()) {
						cb_fb.setSelected(true);
						cb_fb.setChecked(true);
					} else {
						cb_fb.setSelected(false);
						cb_fb.setChecked(false);
					}
				}
				break;
			}
		}
	}


	public void initBtnBack() {
		layout_Back = (LinearLayout) findViewById(R.id.layout_Back);
		layout_Back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				onBackPressed();
			}
		});
	}

	public void init_Fb() {

		btnFacebook = (LoginButton) findViewById(R.id.btnFacebook);
		btnFacebook.fromAct = "Activity_Social_Network";
		btnFacebook.init(activity.getParent(), AUTHORIZE_ACTIVITY_RESULT_CODE, Utility.mFacebook, permissions);

		layout_fb = (RelativeLayout) findViewById(R.id.layout_fb);
		layout_fb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				btnFacebook.fromAct = "Activity_Social_Network";
				if(Utility.mFacebook.isSessionValid()) {
					dialog = new Dialog(activity.getParent(), R.style.myBackgroundStyle);
					WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
					lp.copyFrom(dialog.getWindow().getAttributes());
					lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
					lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.popup_two_option);

					TextView txt = (TextView) dialog.findViewById(R.id.txtContent);
					txt.setText(v.getContext().getString(R.string.popup_Facebook_Logout));

					Button btn_OK = (Button) dialog.findViewById(R.id.btn_OK);
					btn_OK.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							dialog.dismiss();
							
							if(Utility.mFacebook.isSessionValid()) {
								cb_fb.setSelected(false);
								cb_fb.setChecked(false);
							}
							btnFacebook.performClick();
						}
					});
					Button btn_Cancel = (Button) dialog.findViewById(R.id.btn_Cancel);
					btn_Cancel.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							dialog.dismiss();
						}
					});

					dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
					dialog.show();
				} else {
					btnFacebook.performClick();
				}
				
			}
		});

		cb_fb = (CheckBox) findViewById(R.id.cb_fb);
		cb_fb.setEnabled(false);
		cb_fb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				btnFacebook.fromAct = "Activity_Social_Network";
				if(Utility.mFacebook.isSessionValid()) {
					dialog = new Dialog(activity.getParent(), R.style.myBackgroundStyle);
					WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
					lp.copyFrom(dialog.getWindow().getAttributes());
					lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
					lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.popup_two_option);

					TextView txt = (TextView) dialog.findViewById(R.id.txtContent);
					txt.setText(v.getContext().getString(R.string.popup_Facebook_Logout));

					Button btn_OK = (Button) dialog.findViewById(R.id.btn_OK);
					btn_OK.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							dialog.dismiss();
							
							if(Utility.mFacebook.isSessionValid()) {
								cb_fb.setSelected(false);
								cb_fb.setChecked(false);
							}
							btnFacebook.performClick();
						}
					});
					Button btn_Cancel = (Button) dialog.findViewById(R.id.btn_Cancel);
					btn_Cancel.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							dialog.dismiss();
						}
					});

					dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
					dialog.show();
				} else {
					btnFacebook.performClick();
				}
			}
		});
		if (Utility.mFacebook.isSessionValid()) {
			cb_fb.setSelected(true);
			cb_fb.setChecked(true);
		} else {
			cb_fb.setSelected(false);
			cb_fb.setChecked(false);
		}

	}

	public void init_Kakaotalk() {
		
		final KakaoLink kakaoLink = KakaoLink.getLink(getApplicationContext());
		
		layout_kakaotalk = (RelativeLayout)findViewById(R.id.layout_Kakaotalk);
		layout_kakaotalk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!kakaoLink.isAvailableIntent()) {
					showPopupOneOption(getString(R.string.popup_SococialNetWork_NoKakaotalk));
				}
			}
		});
		
		cb_kakaotalk = (CheckBox)findViewById(R.id.cb_Kakaotalk);
		cb_kakaotalk.setEnabled(false);
		cb_kakaotalk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!kakaoLink.isAvailableIntent()) {
					showPopupOneOption(getString(R.string.popup_SococialNetWork_NoKakaotalk));
				}
			}
		});
		
		// check, intent is available.
		if (!kakaoLink.isAvailableIntent()) {
			cb_kakaotalk.setChecked(false);
		} else {
			cb_kakaotalk.setChecked(true);
		}
		
	}
	
	public void showPopupOneOption(String content) {

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

	}
	
//	public void init_twitter() {
//		layout_twitter = (RelativeLayout) findViewById(R.id.layout_twitter);
//		layout_twitter.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				init_thread_Twitter();
//			}
//		});
//
//		cb_twitter = (CheckBox) findViewById(R.id.cb_twitter);
//		cb_twitter.setEnabled(false);
//		cb_twitter.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				init_thread_Twitter();
//			}
//		});
//		if (TwitterUtils.isAuthenticated(prefs)) {
//			cb_twitter.setSelected(true);
//			cb_twitter.setChecked(true);
//		} else {
//			cb_twitter.setSelected(false);
//			cb_twitter.setChecked(false);
//		}
//	}

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

	public class FbAPIsLogoutListener implements LogoutListener {
		@Override
		public void onLogoutBegin() {
			// mText.setText("Logging out...");
		}

		@Override
		public void onLogoutFinish() {
			// mText.setText("You have logged out! ");
			// mUserPic.setImageBitmap(null);
		}
	}

	final Runnable mUpdateTwitterNotification = new Runnable() {
		public void run() {

			dialog = new Dialog(activity, R.style.myBackgroundStyle);
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

//	private void clearCredentials() {
//		SharedPreferences prefs = PreferenceManager
//				.getDefaultSharedPreferences(this);
//		final Editor edit = prefs.edit();
//		edit.remove(OAuth.OAUTH_TOKEN);
//		edit.remove(OAuth.OAUTH_TOKEN_SECRET);
//		edit.commit();
//	}


//	void init_thread_Twitter() {
//		init_progressDialog();
//		Handler mHandler = new Handler() {
//			public void handleMessage(Message msg) {
//				cb_twitter.setChecked(false);
//				progressDialog.dismiss();
//			}
//		};
//
//		InitThread_Twitter thread = new InitThread_Twitter();
//
//		thread.mHandler = mHandler;
//		thread.setDaemon(true);
//		thread.start();
//	}
//
//	class InitThread_Twitter extends Thread {
//		Handler mHandler;
//
//		public void run() {
//			if (TwitterUtils.isAuthenticated(prefs)) {
//				// sendTweet();
//				clearCredentials();
//
//			} else {
//				Intent i = new Intent(getApplicationContext(),PrepareRequestTokenActivity.class);
//				i.putExtra("tweet_msg", getTweetMsg());
//				startActivity(i);
//			}
//			mHandler.sendEmptyMessage(0);
//		}
//	}

//	public void sendTweet() {
//		Thread t = new Thread() {
//			public void run() {
//
//				try {
//					TwitterUtils.sendTweet(prefs, getTweetMsg());
//					mTwitterHandler.post(mUpdateTwitterNotification);
//				} catch (Exception ex) {
//					ex.printStackTrace();
//				}
//			}
//
//		};
//		t.start();
//	}
//
//	private String getTweetMsg() {
//		return "twitter";
//	}

	public final class LoginDialogListener implements DialogListener {
		@Override
		public void onComplete(Bundle values) {
			SessionEvents.onLoginSuccess();
		}

		@Override
		public void onFacebookError(FacebookError error) {
			Toast.makeText(activity, "onFacebookError", Toast.LENGTH_LONG)
					.show();
			SessionEvents.onLoginError(error.getMessage());
		}

		@Override
		public void onError(DialogError error) {
			Toast.makeText(activity, "onError", Toast.LENGTH_LONG).show();
			SessionEvents.onLoginError(error.getMessage());
		}

		@Override
		public void onCancel() {
			Toast.makeText(activity, "onCancel", Toast.LENGTH_LONG).show();
			SessionEvents.onLoginError("Action Canceled");
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
				}
			});
		}
	}

	public class SessionListener implements AuthListener, LogoutListener {

		@Override
		public void onAuthSucceed() {
			// setImageResource(R.drawable.logout_button);
			SessionStore.save(Utility.mFacebook, activity);
		}

		@Override
		public void onAuthFail(String error) {
		}

		@Override
		public void onLogoutBegin() {
		}

		@Override
		public void onLogoutFinish() {
			SessionStore.clear(activity);
			// setImageResource(R.drawable.login_button);
		}
	}
}
