package covisoft.android.tab2_MyCoupon;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import covisoft.android.EasyLife.CheckTimeAsyncTask;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.EasyLifeActivity;
import covisoft.android.EasyLife.R;
import covisoft.android.EasyLife.TabpageActivity;
import covisoft.android.EasyLife.viewFlipper_Banner;
import covisoft.android.item.item_Coupon;
import covisoft.android.item.item_Banner;
import covisoft.android.services.service_Banner;
import covisoft.android.services.service_Coupon_Transfer;
import covisoft.android.tabhost.NavigationActivity;

/*
 * Activity Transfer Normal Coupon to another user in tab 2
 * 
 * Last Updated: 13/06/2013
 * Last Updater: Huan
 * 
 * Last Updated: 01.07.2013
 * Last Updater: Huan
 * Update Info:
 *        - Use EZUtil progress
 */
public class Activity_TransferNormalCoupon extends NavigationActivity {

	private Activity activity;
	private LinearLayout layout_Back;
	
	private EditText edtYourID;
	private EditText edtYourPassword;  
	private EditText edtYourEmail;
	private EditText edtFriendID;
	private EditText edtFriendEmail;
	private Button btnTransfer;
	
	private item_Coupon coupon;
	
	private Timer timer;
	
// ====================================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_transfercoupon);
		
		activity = this;

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Intent intent = getIntent();

		coupon = (item_Coupon)intent.getSerializableExtra("coupon");
		
		init();
	}
	void init() {
		initBtnBack();
		initWidget();
		
		if(EZUtil.isNetworkConnected(activity)) {
			AsyncTaskRequestBanner task = new AsyncTaskRequestBanner();
			task.execute();
			timer = new Timer();
			timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);
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
	public void onBackPressed() {
		super.onBackPressed();
		
	}

	public void initWidget() {
		edtYourID = (EditText)findViewById(R.id.edtYourID);
		edtYourID.setText(EasyLifeActivity.user.getUsername());
		edtYourPassword = (EditText)findViewById(R.id.edtYourPassword);
		edtYourEmail = (EditText)findViewById(R.id.edtYourEmail);
		edtYourEmail.setText(EasyLifeActivity.user.getEmail());
		edtFriendID = (EditText)findViewById(R.id.edtFriendID);
		edtFriendEmail = (EditText)findViewById(R.id.edtFriendEmail);
		
		btnTransfer = (Button)findViewById(R.id.btnTransfer);
		btnTransfer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String sYourID = edtYourID.getText().toString().trim();
				String sYourPassword = edtYourPassword.getText().toString().trim();
				String sYourEmail = edtYourEmail.getText().toString().trim();
				String sFriendID = edtFriendID.getText().toString().trim();
				String sFriendEmail = edtFriendEmail.getText().toString().trim();
				
				if(sYourID.equals("") || sYourPassword.equals("") || sYourEmail.equals("") || sFriendID.equals("") || sFriendEmail.equals("")) {
					showPopupOneOption(getResources().getString(R.string.popup_TransferQRCoupon_NotEnought), 1);
				} else if(sYourID.equals(sFriendEmail)) {
					showPopupOneOption(getResources().getString(R.string.popup_TransferQRCoupon_SameUser), 1);
				} else {
					final Dialog dialog = new Dialog(activity.getParent(), R.style.myBackgroundStyle);
					WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
					lp.copyFrom(dialog.getWindow().getAttributes());
					lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
					lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.popup_two_option);

					TextView txt = (TextView) dialog.findViewById(R.id.txtContent);
					txt.setText(v.getContext().getString(R.string.popup_QRCoupon_Full_Transfer));

					Button btn_OK = (Button) dialog.findViewById(R.id.btn_OK);
					btn_OK.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							dialog.dismiss();
							
							if (EZUtil.isNetworkConnected(activity)) {
								AsyncTask_TransferCoupon task = new AsyncTask_TransferCoupon();
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
				}
				
			}
		});
	
	}
	
	public void initBanner(ArrayList<item_Banner> arItemBanner) {
		RelativeLayout banner = (RelativeLayout) findViewById(R.id.banner);
		viewFlipper_Banner.init(TabpageActivity.activity, banner, arItemBanner);
	}
	
	private class AsyncTaskRequestBanner extends AsyncTask<Void, Void, ArrayList<item_Banner>> {

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
		protected ArrayList<item_Banner> doInBackground(Void... params) {

			if (isCancelled()) {
				return null;
			}

			service_Banner xml = new service_Banner(Activity_TransferNormalCoupon.this, "3");
			ArrayList<item_Banner> arItemBanner = xml.start();

			return arItemBanner;
		}

		@Override
		protected void onPostExecute(ArrayList<item_Banner> arItemBanner) {

			timer.cancel();
			EZUtil.cancelProgress();
			initBanner(arItemBanner);

		}

		@Override
		protected void onCancelled() {

			timer.cancel();
			EZUtil.cancelProgress();
		}
	}

	private class AsyncTask_TransferCoupon extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			EZUtil.init_progressDialog(getParent());
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(Void... params) {

			if (isCancelled()) {
				return null;
			}
			
			String sYourUsername = edtYourID.getText().toString().trim();
			String sYourPassword = edtYourPassword.getText().toString().trim();
			String sYourEmail = edtYourEmail.getText().toString().trim();
			String sFriendID = edtFriendID.getText().toString().trim();
			String sFriendEmail = edtFriendEmail.getText().toString().trim();
			
			service_Coupon_Transfer xmlTransfer = new service_Coupon_Transfer(activity, sYourUsername, sYourPassword, coupon.getCouponNo() + "", sFriendID, sYourEmail, sFriendEmail);
			String transferResult = xmlTransfer.start();

			return transferResult;
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
								showPopupOneOption(Activity_TransferNormalCoupon.this.getResources().getString(R.string.popup_Coupon_Transfer_WrongInfo), 1);
							}
						});
					}
				}, 300);
				
			} else if(result.equals("2")) {
				Timer t = new Timer();
				t.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						activity.runOnUiThread(new Runnable() {
							public void run() {
								showPopupOneOption(Activity_TransferNormalCoupon.this.getResources().getString(R.string.popup_Coupon_Transfer_WrongRecipients), 1);
							}
						});
					}
				}, 300);
				
			} else if(result.equals("6")) {
				Timer t = new Timer();
				t.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						activity.runOnUiThread(new Runnable() {
							public void run() {
								showPopupOneOption(Activity_TransferNormalCoupon.this.getResources().getString(R.string.popup_Coupon_Transfer_Existed), 1);
							}
						});
					}
				}, 300);
				
			} else if(result.equals("7")) {
				Timer t = new Timer();
				t.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						activity.runOnUiThread(new Runnable() {
							public void run() {
								showPopupOneOption(Activity_TransferNormalCoupon.this.getResources().getString(R.string.popup_Coupon_Transfer_Success), 3);
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
								showPopupOneOption(Activity_TransferNormalCoupon.this.getResources().getString(R.string.popup_cannot_conect), 1);
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
						Intent intent = new Intent(Activity_TransferNormalCoupon.this, Activity_UseCoupon.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
						intent.putExtra("coupon", coupon);
						goNextHistory_2("Activity_UseCoupon", intent);
					} else if(fiType == 3) {

						onBackPressed();
					}
				}
			});
		}
	}
	
}
