package covisoft.android.tab2_MyCoupon;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import lib.imageLoader.ImageDialog;
import lib.imageLoader.ImageLoader_Rounded;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.android.BaseRequestListener;
import com.facebook.android.LoginButton;
import com.facebook.android.SessionEvents.AuthListener;
import com.facebook.android.SessionEvents.LogoutListener;
import com.facebook.android.Utility;

import covisoft.android.EasyLife.CheckTimeAsyncTask;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.EasyLifeActivity;
import covisoft.android.EasyLife.R;
import covisoft.android.item.item_Coupon;
import covisoft.android.services.service_Coupon_Use;
import covisoft.android.tabhost.NavigationActivity;
import covisoft.kakaotalk.KakaoLink;

/*
 * Activity Detail Coupon, tab 2
 * 
 * Last Updated: 13/06/2013
 * Last Updater: Huan
 */
public class Activity_UseCoupon extends NavigationActivity {

	public Activity activity;

	private LinearLayout layout_Back;

	private LinearLayout layout_Caution;
	private Button btnUseCoupon;
	private Button btnTransfer;
	
	private Button btnShare;
	private LoginButton btnFacebook;
	private Button btnKakaotalk;
	
	private TextView txtShopName;
	private TextView txtCouponTitle;
	private TextView txtCaution;
	private TextView txtDate;
	private TextView txtContent;
	private TextView txtID;
	
	private ImageView img_Coupon;
	private ImageLoader_Rounded imageLoader;
	
	public static item_Coupon coupon;

	private LinearLayout layoutInfo;

	private Dialog dialog;
	
	private Timer timer;

	final static int AUTHORIZE_ACTIVITY_RESULT_CODE = 0;
	private String[] permissions = { "offline_access", "publish_stream", "user_photos", "publish_checkins", "photo_upload" };
	private Handler mHandler;
	
// ==============================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_15_use_coupon);

		activity = this;

		Intent intent = getIntent();
		coupon = (item_Coupon) intent.getSerializableExtra("coupon");
		
		imageLoader = new ImageLoader_Rounded(activity.getApplicationContext());
		
		init();
		
		mHandler = new Handler();

		if (Utility.mFacebook.isSessionValid()) {
			requestUserData();
		}
	}

	public void init() {

		initBtnBack();
		init_Image();
		initText();
		initBtnUse();
	}

	public void initBtnBack() {
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

	public void initText() {

		txtShopName = (TextView) findViewById(R.id.txtTopName);
		txtShopName.setText(coupon.getCompanyName());

		txtCouponTitle = (TextView) findViewById(R.id.txtCouponTitle);
		txtCouponTitle.setText(coupon.getCouponName());

		txtDate = (TextView) findViewById(R.id.txtExpireDate);
		txtDate.setText(coupon.getEndDate());

		txtContent = (TextView) findViewById(R.id.txtCouponContent);
		txtContent.setText(coupon.getCont());

		layout_Caution = (LinearLayout) findViewById(R.id.layout_Caution);
		txtCaution = (TextView) findViewById(R.id.txtCaution);
		txtCaution.setText(coupon.getAttentionCont());

		if (coupon.getAttentionCont().trim().equals("")) {
			layout_Caution.setVisibility(View.GONE);
		} else {
			layout_Caution.setVisibility(View.VISIBLE);
		}

		txtID = (TextView) findViewById(R.id.txtCouponID);
		txtID.setText(coupon.getNo() + "");
	}
	
	public void init_Image() {
		img_Coupon = (ImageView)findViewById(R.id.img_Coupon);
		imageLoader = new ImageLoader_Rounded(activity.getApplicationContext());
		imageLoader.DisplayImage(coupon.getLinkimage(), img_Coupon, 0, 300);
		
		img_Coupon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(activity, ImageDialog.class);
				intent.putExtra("image_link",coupon.getLinkimage());
				activity.startActivity(intent);
			}
		});
	}
	public void initBtnUse() {
		btnUseCoupon = (Button) findViewById(R.id.btnUseCoupon);
		btnTransfer = (Button)findViewById(R.id.btnTransfer);
		layoutInfo = (LinearLayout) findViewById(R.id.layoutInfo);
		if (coupon.getIdCouponKind() == 2) {
			btnUseCoupon.setVisibility(View.VISIBLE);
			btnTransfer.setVisibility(View.VISIBLE);
			layoutInfo.setVisibility(View.VISIBLE);
		} else {
			btnUseCoupon.setVisibility(View.GONE);
			btnTransfer.setVisibility(View.GONE);
			layoutInfo.setVisibility(View.GONE);
		}

		btnUseCoupon.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				dialog = new Dialog(Activity_UseCoupon.this.getParent(), R.style.myBackgroundStyle);
				WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
				lp.copyFrom(dialog.getWindow().getAttributes());
				lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
				lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.popup_two_option);

				TextView txt = (TextView) dialog.findViewById(R.id.txtContent);
				txt.setText(v.getContext().getString(R.string.popup_Coupon_Use));

				Button btn_OK = (Button) dialog.findViewById(R.id.btn_OK);
				btn_OK.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						dialog.dismiss();

						if(EZUtil.isNetworkConnected(activity)) {
							AsyncTaskUseCoupon task = new AsyncTaskUseCoupon();
							task.execute();
							timer = new Timer();
							timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);
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

			}
		});
		
		btnTransfer = (Button)findViewById(R.id.btnTransfer);
		btnTransfer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Activity_UseCoupon.this, Activity_TransferNormalCoupon.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra("coupon", coupon);
				goNextHistory_2("Activity_TransferNormalCoupon", intent);
			}
		});
		
		btnShare = (Button) findViewById(R.id.btnShare);
		btnShare.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				if (!EZUtil.isLoading) {

					EZUtil.isLoading = true;

					final View view = activity.getLayoutInflater().inflate(R.layout.popup_share, null);

					btnFacebook = (LoginButton) view.findViewById(R.id.btnFacebook);
					LoginButton.fromAct = "Activity_UseCoupon";
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
										"Address: " + coupon.getAddr() + "\n " + 
										"Time: " + coupon.getStartDate() + " - " + coupon.getEndDate(), 
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
		

	}

	private class AsyncTaskUseCoupon extends AsyncTask<Void, Void, String> {

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

			service_Coupon_Use xml = new service_Coupon_Use(activity, EasyLifeActivity.user.getUsername(), coupon.getCouponNo() + "");
			String result = xml.start();
			return result;
		}

		@Override
		protected void onPostExecute(String result) {

			timer.cancel();

			EZUtil.cancelProgress();
			if (result.equals("6")) {
				Timer t = new Timer();
				t.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						activity.runOnUiThread(new Runnable() {
							public void run() {
								showPopupOneOption(activity.getResources().getString(R.string.popup_Coupon_Use_Success), 2);
							}
						});
					}
				}, 300);
			} else {
				Timer t = new Timer();
				t.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						activity.runOnUiThread(new Runnable() {
							public void run() {
								showPopupOneOption(activity.getResources().getString(R.string.popup_Coupon_Use_Fail), 2);
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
							showPopupOneOption(activity.getResources().getString(R.string.No_Response), 2);
						}
					});
				}
			}, 300);
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
		final int fiType = type;
		
		if (!EZUtil.isLoading) {
			EZUtil.isLoading = true;
			dialog = new Dialog(Activity_UseCoupon.this.getParent(), R.style.myBackgroundStyle);
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
			dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
			dialog.show();

			dialog.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					EZUtil.isLoading = false;
					if(fiType == 2) {
						onBackPressed();
					}
				}
			});
		}

	}

}
