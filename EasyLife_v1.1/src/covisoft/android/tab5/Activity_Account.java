package covisoft.android.tab5;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import covisoft.android.EasyLife.CheckTimeAsyncTask;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.EasyLifeActivity;
import covisoft.android.EasyLife.R;
import covisoft.android.services.service_User_Info;
import covisoft.android.tabhost.NavigationActivity;


/*
 * Activity Account Show User's Info
 * Resume to get user's info every time this activity is called
 * 
 * Service: http://easylife.com.vn/Web_Mobile_New/api/userInfo/?
 * 
 * Last Updated: 2.07.2013
 * Last Updater: Huan
 * Update Info:
 *        - Global variable -> local variable
 *  
 */
public class Activity_Account extends NavigationActivity {

	private Activity activity;

	private Timer timer;

// ===================================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_account);

		activity = this;
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (EZUtil.isNetworkConnected(activity)) {
			AsyncTask_UserInfo task = new AsyncTask_UserInfo();
			task.execute();
			timer = new Timer();
			timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);
		} else {
			showPopupOneOption(activity.getResources().getString(R.string.No_Internet_now), 2);
		}
		super.onResume();
	}
	
	private class AsyncTask_UserInfo extends AsyncTask<Void, Void, Void> {

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

			service_User_Info xml_UserInfo = new service_User_Info(activity, EasyLifeActivity.user.getUsername());
			xml_UserInfo.start();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			timer.cancel();

			EZUtil.cancelProgress();

			init();
			
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
	
	public void init() {
		initBtnBack();
		initBtnUpdate();
		initBtnChangePass();
		initText();
	}

	public void initBtnBack() {
		LinearLayout layoutBack = (LinearLayout) findViewById(R.id.layout_Back);
		layoutBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}
	
	public void initBtnUpdate() {
		Button btnUpdate = (Button)findViewById(R.id.btnUpdate);
		btnUpdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Activity_Account.this, Activity_AccountUpdate.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				goNextHistory("Activity_AccountUpdate", intent);
			}
		});
	}
	
	public void initBtnChangePass() {
		Button btnChangePass = (Button)findViewById(R.id.btnChangePass);
		btnChangePass.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Activity_Account.this, Activity_ChangePass.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				goNextHistory("Activity_ChangePass", intent);
			}
		});
	}
	
	public void initText() {
		
		TextView txtAccount = (TextView)findViewById(R.id.txtAccount);
		txtAccount.setText(EasyLifeActivity.user.getUsername());
		
		TextView txtFullname = (TextView)findViewById(R.id.txtFullname);
		txtFullname.setText(EasyLifeActivity.user.getName());
		
		TextView txtEmail = (TextView)findViewById(R.id.txtEmail);
		txtEmail.setText(EasyLifeActivity.user.getEmail());
		
		TextView txtBirthday = (TextView)findViewById(R.id.txtBirthday);
		txtBirthday.setText(EasyLifeActivity.user.getBirthday());
		
		TextView txtGender = (TextView)findViewById(R.id.txtGender);
		if (EasyLifeActivity.user.getGender().equals("M")) {
			txtGender.setText(getResources().getString(R.string.Male));
		} else if (EasyLifeActivity.user.getGender().equals("F")) {
			txtGender.setText(getResources().getString(R.string.FeMale));
		} else {
			txtGender.setText("");
		}
		
		TextView txtPhone = (TextView)findViewById(R.id.txtPhone);
		txtPhone.setText(EasyLifeActivity.user.getTel());
		
		TextView txtAddress = (TextView)findViewById(R.id.txtAddress);
		txtAddress.setText(EasyLifeActivity.user.getAddress());
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
						dialog.dismiss();
					}
				});
			} else if (type == 2) {
				btn_OK.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
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
	
}
