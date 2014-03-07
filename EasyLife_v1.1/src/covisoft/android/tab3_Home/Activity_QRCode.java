package covisoft.android.tab3_Home;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import lib.imageLoader.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.BaseRequestListener;
import com.facebook.android.LoginButton;
import com.facebook.android.SessionEvents.AuthListener;
import com.facebook.android.SessionEvents.LogoutListener;
import com.facebook.android.Utility;

import covisoft.android.EasyLife.CheckTimeAsyncTask;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.EasyLifeActivity;
import covisoft.android.EasyLife.R;
import covisoft.android.EasyLife.TabpageActivity;
import covisoft.android.item.item_QRCoupon_detail;
import covisoft.android.services.service_QRCoupon_Capture;
import covisoft.android.services.service_QRCoupon_Use;
import covisoft.android.tab2_MyCoupon.Activity_TransferQRCoupon;
import covisoft.android.tabhost.NavigationActivity;
import covisoft.kakaotalk.KakaoLink;

public class Activity_QRCode extends NavigationActivity {

	private Activity activity;
	
	private LinearLayout layout_Back;
	
	final static int REQ_CODE = 1;
	private TextView txt_CounponName;
	public static item_QRCoupon_detail coupon;
	private String couponID;
	public ImageLoader imageLoader;

	public static String data;
	
	public static MediaPlayer mPlayer2;
	
	private Dialog dialog;
	
	private Timer timer;
	
	private String errorCode = "";
	
	private Button btnShare;
	private LoginButton btnFacebook;
	private Button btnKakaotalk;
	
	final static int AUTHORIZE_ACTIVITY_RESULT_CODE = 0;
	private String[] permissions = { "offline_access", "publish_stream", "user_photos", "publish_checkins", "photo_upload" };
	private Handler mHandler;
	
	
//===========================================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    View viewToLoad = LayoutInflater.from(this.getParent()).inflate(R.layout.layout_24, null);
	    this.setContentView(viewToLoad);

		data = null;

		Intent intent = getIntent();
		data = intent.getStringExtra("data");
		
		activity = this;
		init();

		TabpageActivity.LinearLayout_tab.setVisibility(View.INVISIBLE);
		
		mHandler = new Handler();

		if (Utility.mFacebook.isSessionValid()) {
			requestUserData();
		}
		
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		if (Activity_QRCode.mPlayer2 != null) {
			Activity_QRCode.mPlayer2.stop();
			Activity_QRCode.mPlayer2 = null;
		}
		super.onStop();
	}
	@Override
	public void onBackPressed() {

		if (mPlayer2 != null) {
			mPlayer2.stop();
			mPlayer2 = null;
		}

		data = null;

		TabpageActivity.btnThree.setVisibility(View.GONE);
		TabpageActivity.btnSearch.setVisibility(View.VISIBLE);
		TabpageActivity.ChangePage(3, false, false);
		TabpageActivity.LinearLayout_tab.setVisibility(View.VISIBLE);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (data != null) {
			if (EZUtil.isNetworkConnected(activity)) {
				AsyncTaskSendQR task = new AsyncTaskSendQR();
				task.execute();
				timer = new Timer();
				timer.schedule(new CheckTimeTaskSendQR(task), EZUtil.DelayTime);
				
			} else {
				showPopupOneOption(activity.getResources().getString(R.string.No_Internet_now), 1);
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	void init() {
		initBtnBack();
	}

	void initBtnBack() {

		layout_Back = (LinearLayout) findViewById(R.id.layout_Back);
		layout_Back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				onBackPressed();
			}
		});

	}
	
	private class AsyncTaskSendQR extends AsyncTask<Void, Void, Void> {

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

			try {
				String[] s = data.split("\\|");
				service_QRCoupon_Capture xml = new service_QRCoupon_Capture(EasyLifeActivity.user.getUsername(), s[0], s[1], s[2], Activity_QRCode.this);

				coupon = xml.start();
				couponID = coupon.getNo() + "";
				errorCode = xml.wrongQRCoupon;
				Log.e("QRCoupon", coupon.toString());

			} catch (Exception e) {
				// TODO: handle exception
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			timer.cancel();

			EZUtil.cancelProgress();

			if(errorCode.equals("0")) {
				
				showPopupOneOption(Activity_QRCode.this.getResources().getString(R.string.popup_QRCoupon_CannotConnect), 2);
			
			} else if(errorCode.equals("1")) {
				
				showPopupOneOption(Activity_QRCode.this.getResources().getString(R.string.popup_QRCoupon_wrong), 2);
			
			} else if(errorCode.equals("5")) {
				
				showPopupOneOption(Activity_QRCode.this.getResources().getString(R.string.popup_QRCoupon_Full), 1);
				if(coupon.getArOpenTime().size() == 0) {
					onBackPressed();
				} else {
					init_Image();
				}
			} else if(errorCode.equals("6")) {
				
				showPopupOneOption(Activity_QRCode.this.getResources().getString(R.string.popup_QRCoupon_WrongPassword), 2);
			
			} else if (coupon != null && coupon.getArOpenTime() != null) {
				if(coupon.getArOpenTime().size() == 0) {
					onBackPressed();
				} else {
					init_Image();
				}
			} else {
				showPopupOneOption(Activity_QRCode.this.getResources().getString(R.string.popup_QRCoupon_wrong), 2);
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
			}, 500);
		}
	}
	
	class CheckTimeTaskSendQR extends TimerTask {

		AsyncTaskSendQR async;

		public CheckTimeTaskSendQR(AsyncTaskSendQR async) {
			super();
			this.async = async;
		}

		@Override
		public void run() {

			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {

					async.cancel(true);
				}
			});
		}
	}

	
	public void init_Image() {

		txt_CounponName = (TextView) findViewById(R.id.txt_CouponName);
		txt_CounponName.setText(coupon.getCouponName());

		imageLoader = new ImageLoader(getApplicationContext());

		LinearLayout ln_img = (LinearLayout) findViewById(R.id.ln_img);

		imageLoader.DisplayImage2(coupon.getLinkImage(), ln_img, R.drawable.s_24_noimage, EZUtil.REQUIRED_SIZE_BIG, coupon);

		if (coupon != null) {
			if (coupon.getArOpenTime().size() >= coupon.getNoKey()) {

				mPlayer2 = MediaPlayer.create(this, R.raw.town);
				mPlayer2.start();

				LinearLayout ln_NotYet = (LinearLayout) findViewById(R.id.ln_NotYet);
				LinearLayout ln_Finish = (LinearLayout) findViewById(R.id.ln_Finish);

				ln_NotYet.setVisibility(View.GONE);
				ln_Finish.setVisibility(View.VISIBLE);
				
			} else {
				LinearLayout ln_NotYet = (LinearLayout) findViewById(R.id.ln_NotYet);
				LinearLayout ln_Finish = (LinearLayout) findViewById(R.id.ln_Finish);

				ln_NotYet.setVisibility(View.VISIBLE);
				ln_Finish.setVisibility(View.GONE);
			}
			
			Button btnUse = (Button) findViewById(R.id.btnUse);
			btnUse.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if(coupon.getArOpenTime().size() >= coupon.getNoKey()) {
						dialog = new Dialog(Activity_QRCode.this.getParent(), R.style.myBackgroundStyle);
						WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
						lp.copyFrom(dialog.getWindow().getAttributes());
						lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
						lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.setContentView(R.layout.popup_two_option);

						TextView txt = (TextView) dialog.findViewById(R.id.txtContent);
						txt.setText(v.getContext().getString(R.string.popup_QRCoupon_Full_Use));

						Button btn_OK = (Button) dialog.findViewById(R.id.btn_OK);
						btn_OK.setOnClickListener(new OnClickListener() {

							public void onClick(View v) {
								dialog.dismiss();
								if (EZUtil.isNetworkConnected(activity)) {
									AsyncTask_UseQR task = new AsyncTask_UseQR();
									task.execute();
									timer = new Timer();
									timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);
								} else {
									showPopupOneOption(activity.getResources().getString(R.string.No_Internet_now), 1);
								}
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
						showPopupOneOption(getResources().getString(R.string.popup_QRCoupon_NotFull_Use), 1);
					}
					
				}
			});
			
			Button btnTransfer = (Button) findViewById(R.id.btnTransfer);
			btnTransfer.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if(coupon.getArOpenTime().size() >= coupon.getNoKey()) {
						Intent intent = new Intent(Activity_QRCode.this, Activity_TransferQRCoupon.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra("couponID", couponID + "");
						goNextHistory("Activity_TransferQRCoupon", intent);
					} else {
						showPopupOneOption(getResources().getString(R.string.popup_QRCoupon_NotFull_Transfer), 1);
					}
					
				}
			});
			
			btnShare = (Button) findViewById(R.id.btnShare);
			btnShare.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {

					if (!EZUtil.isLoading) {

						EZUtil.isLoading = true;

						try {
							final View view = activity.getLayoutInflater().inflate(R.layout.popup_share, null);

							btnFacebook = (LoginButton) view.findViewById(R.id.btnFacebook);
							LoginButton.fromAct = "Activity_QRCode";
							btnFacebook.init(activity.getParent(), AUTHORIZE_ACTIVITY_RESULT_CODE, Utility.mFacebook, permissions);
							
							LinearLayout layout_fb = (LinearLayout) view.findViewById(R.id.layout_fb);
							layout_fb.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									dialog.dismiss();
									btnFacebook.performClick();
								}
							});
							btnKakaotalk = (Button) view.findViewById(R.id.btnKakaotalk);
							btnKakaotalk.setOnClickListener(new OnClickListener() {
								
								public void onClick(View v) {
									dialog.dismiss();
									// Recommended: Use application context for parameter.
									KakaoLink kakaoLink = KakaoLink.getLink(getApplicationContext());

									// check, intent is available.
									if (!kakaoLink.isAvailableIntent())
										return;

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
												coupon.getCouponName() + "\n" + 
												"Shop: " + coupon.getCompanyName() + "\n" + 
												"Time: " + coupon.getStartDate() + " - " + coupon.getEndDate(), 
												activity.getPackageName(), 
												activity.getPackageManager().getPackageInfo(getPackageName(), 0).versionName, 
												"EZLife", 
												EZUtil.encoding,
												metaInfoArray);
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
						} catch (Exception e) {
							// TODO: handle exception
							Log.e("outofMemory", "Bug");
						}

					}

				}
			});
		}
	}

	
	private class AsyncTask_UseQR extends AsyncTask<Void, Void, String> {

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

			service_QRCoupon_Use xml = new service_QRCoupon_Use(EasyLifeActivity.user.getUsername(), couponID + "", Activity_QRCode.this);
			String useCouponResult = xml.start();

			return useCouponResult;
		}

		@Override
		protected void onPostExecute(String result) {

			timer.cancel();
			EZUtil.cancelProgress();

			if (result.equals("4")) {
				showPopupOneOption(getResources().getString(R.string.popup_QRCoupon_Success), 2);
			} else if(result.equals("0") || result.equals("1") || result.equals("2") || result.equals("3")){
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.popup_QRCoupon_CannotUse), Toast.LENGTH_LONG).show();
			} else {
				Timer t = new Timer();
				t.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						activity.runOnUiThread(new Runnable() {
							public void run() {
								showPopupOneOption(Activity_QRCode.this.getResources().getString(R.string.popup_cannot_conect), 1);
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
			}, 500);
		}
	}
	
	public void requestUserData() {
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
		}
	}

	public void showPopupOneOption(String content, int type) {
		final int cType = type;
		dialog = new Dialog(Activity_QRCode.this.getParent(), R.style.myBackgroundStyle);
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
				if(cType == 2) {
					onBackPressed();
				}
			}
		});
		dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
		dialog.show();
	}
	
}
