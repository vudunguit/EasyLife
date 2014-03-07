package covisoft.android.EasyLife;

import static covisoft.android.EasyLife.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static covisoft.android.EasyLife.CommonUtilities.EXTRA_MESSAGE;
import static covisoft.android.EasyLife.CommonUtilities.SENDER_ID;
import static covisoft.android.EasyLife.CommonUtilities.SERVER_URL;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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

import covisoft.android.item.item_User;
import covisoft.android.tabhost.NavigationActivity;

public class EasyLifeActivity extends NavigationActivity {

	public static Activity activity;
	private final String TAG = "EasyLifeActivity";
	public static String regId = "";
	Timer timer;
	AsyncTask<Void, Void, Void> mRegisterTask;

	public static item_User user;  
	DeviceUuidFactory uuid;
	
	private boolean isRegisted = false;

	
	// ====================================================================================================================
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		
		activity = this;
		
		onNewIntent(getIntent());
		
		uuid = new DeviceUuidFactory(this);
		Log.e(TAG, "call oncreated");
		// Create the Facebook Object using the app id.
		Utility.mFacebook = new Facebook(Utility.mAPP_ID);
		// Instantiate the asynrunner object for asynchronous api calls.
		Utility.mAsyncRunner = new AsyncFacebookRunner(Utility.mFacebook);

		// restore session if one exists
		SessionStore.restore(Utility.mFacebook, this);
		SessionEvents.addAuthListener(new FbAPIsAuthListener());
		SessionEvents.addLogoutListener(new FbAPIsLogoutListener());
		if (Utility.mFacebook.isSessionValid()) {
			requestUserData();
		}

		if (EZUtil.isNetworkConnected(EasyLifeActivity.this)) {
			/* Start Activity Home_Activity in 3 seconds */
			timer = new Timer();
			MyTask myTask = new MyTask();
			timer.schedule(myTask, 3000);

			checkNotNull(SERVER_URL, "SERVER_URL");
			checkNotNull(SENDER_ID, "SENDER_ID");
			// Make sure the device has the proper dependencies.
			GCMRegistrar.checkDevice(this);
			// Make sure the manifest was properly set - comment out this line
			// while developing the app, then uncomment it when it's ready.
			GCMRegistrar.checkManifest(this);

			isRegisted = true;
			registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));

			regId = GCMRegistrar.getRegistrationId(this);
			
			if (regId.equals("")) {
				// Automatically registers application on startup.
				GCMRegistrar.register(this, SENDER_ID);
			} else {
				// Device is already registered on GCM, check server.
				if (GCMRegistrar.isRegisteredOnServer(this)) {
					// Skips registration.
					// mDisplay.append(getString(R.string.already_registered) +
					// "\n");
					ServerUtilities.register(this, regId, "null");
					// mRegisterTask.execute(null, null, null);
				} else {
					// Try to register again, but not in the UI thread.
					// It's also necessary to cancel the thread onDestroy(),
					// hence the use of AsyncTask instead of a raw thread.
					final Context context = this;
					mRegisterTask = new AsyncTask<Void, Void, Void>() {

						@Override
						protected Void doInBackground(Void... params) {

							boolean registered = ServerUtilities.register(context, regId, "null");
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
			
		} else {
			final Dialog dialog = new Dialog(EasyLifeActivity.this, R.style.myBackgroundStyle);
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			lp.copyFrom(dialog.getWindow().getAttributes());
			lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
			lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.popup_one_option);

			TextView txt = (TextView) dialog.findViewById(R.id.txtContent);
			txt.setText(EasyLifeActivity.this.getResources().getString(R.string.No_Internet));

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
					finish();
					
				}
			});
		}
	}
	
	
	class MyTask extends TimerTask {
		public void run() {
			try {
				Intent intent = new Intent(EasyLifeActivity.this, TabpageActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
				finish();
			} catch (Exception ex) {
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
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

	@Override
	public void onDestroy() {
		
		Log.e("EasyLifeActivity", "Destroy");
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		if(isRegisted) {
			unregisterReceiver(mHandleMessageReceiver);
		}
		
		super.onDestroy();
	}

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
		}
	};
	
	/*
	 * Callback for fetching current user's name, picture, uid.
	 */
	private void checkNotNull(Object reference, String name) {
		if (reference == null) {
			throw new NullPointerException(getString(R.string.error_config, name));
		}
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