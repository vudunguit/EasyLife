package covisoft.android.tab5;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import covisoft.android.EasyLife.CheckTimeAsyncTask;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.EasyLifeActivity;
import covisoft.android.EasyLife.R;
import covisoft.android.services.service_Password_Change;
import covisoft.android.tabhost.NavigationActivity;

/*
 * Activity Change Password
 * 
 * Service: http://easylife.com.vn/Web_Mobile_New/api/changePass/?
 * 			param: (username : string , oldpass : string , newpass : string)
 *  
 */
public class Activity_ChangePass extends NavigationActivity {

	private Activity activity;
	private LinearLayout layoutBack;

	private EditText edtOld;
	private EditText edtNew;
	private EditText edtConfirm;

	private Button btnConfirm;

	private Timer timer;
	
	// ===================================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_changepass);

		activity = this;

		init();
	}

	public void init() {
		initBtnBack();
		initWidget();
	}

	public void initBtnBack() {
		layoutBack = (LinearLayout) findViewById(R.id.layout_Back);
		layoutBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hideKeyboard();
				onBackPressed();
			}
		});
	}

	public void initWidget() {

		edtOld = (EditText) findViewById(R.id.edtOld);
		edtNew = (EditText) findViewById(R.id.edtNew);
		edtConfirm = (EditText) findViewById(R.id.edtConfirm);
		edtConfirm.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER
							|| event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

						btnConfirm.performClick();

					}
				}

				return false;
			}
		});
		
		btnConfirm = (Button) findViewById(R.id.btnConfirm);
		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				hideKeyboard();
				
				final String sOld = edtOld.getText().toString().trim();
				final String sNew = edtNew.getText().toString().trim();
				final String sConfirm = edtConfirm.getText().toString().trim();
				
				if(!EZUtil.isNetworkConnected(activity)) {
					showPopupOneOption(activity.getResources().getString(R.string.No_Internet_now) ,2);
				} else if (sOld.equals("") || sNew.equals("") || sConfirm.equals("")) {
					showPopupOneOption(getResources().getString(R.string.popup_ChangePass_Require), 1);
				} else if (!edtNew.getText().toString().trim().equals(edtConfirm.getText().toString().trim())) {
					showPopupOneOption(getResources().getString(R.string.popup_ChangePass_notMatch), 1);
				} else if(edtNew.getText().toString().trim().length() < 4 || edtNew.getText().toString().trim().length() > 16) {
					showPopupOneOption(getResources().getString(R.string.popup_ChangePass_Length), 1);
				} else {
					if (!EZUtil.isLoading) {
						EZUtil.isLoading = true;
						final Dialog dialog = new Dialog(activity.getParent(), R.style.myBackgroundStyle);
						WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
						lp.copyFrom(dialog.getWindow().getAttributes());
						lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
						lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
						dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.setContentView(R.layout.popup_two_option);

						TextView txt = (TextView) dialog.findViewById(R.id.txtContent);
						txt.setText(getResources().getString(R.string.popup_ChangePass_Confirm));

						Button btn_OK = (Button) dialog.findViewById(R.id.btn_OK);
						btn_OK.setOnClickListener(new OnClickListener() {

							public void onClick(View v) {
								dialog.dismiss();
								
								AsyncTask_ChangePass task = new AsyncTask_ChangePass();
								task.execute();
								timer = new Timer();
								timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);
								
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
		});

	}
	
	private class AsyncTask_ChangePass extends AsyncTask<Void, Void, String> {

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

			String sOld = edtOld.getText().toString().trim();
			String sNew = edtNew.getText().toString().trim();
			
			service_Password_Change xml_Changepass = new service_Password_Change(activity, EasyLifeActivity.user.getUsername(), sOld, sNew);
			String updateResult = xml_Changepass.start();

			return updateResult;
		}

		@Override
		protected void onPostExecute(String result) {

			timer.cancel();

			EZUtil.cancelProgress();
			Log.e("Change password result", result);
			if(result.equals("1")) {
				showPopupOneOption(activity.getResources().getString(R.string.popup_ChangePass_Success), 2);
			} else if(result.equals("2")){
				showPopupOneOption(activity.getResources().getString(R.string.popup_ChangePass_OldWrong), 1);
			} else if(result.equals("3")){
				showPopupOneOption(activity.getResources().getString(R.string.popup_ChangePass_Same), 1);
			} else if(result.equals("0")){
				showPopupOneOption(activity.getResources().getString(R.string.popup_ChangePass_Fail), 1);
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
			final Dialog dialog = new Dialog(Activity_ChangePass.this.getParent(), R.style.myBackgroundStyle);
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
	public void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edtOld.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(edtNew.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(edtConfirm.getWindowToken(), 0);
	}
}
