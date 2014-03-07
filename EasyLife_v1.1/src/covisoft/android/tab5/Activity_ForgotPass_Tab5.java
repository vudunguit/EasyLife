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
import covisoft.android.EasyLife.R;
import covisoft.android.services.service_Password_Forgot;
import covisoft.android.tabhost.NavigationActivity;

/*
 * Activity use to sent request when user forgot their password
 * 
 * Service: http://www.easylife.com.vn/Web_Mobile_New/Webservice/SendMail/forgotPass.php?
 * 			param: (email : string)
 * 
 * Last Updated: 2.07.2013
 * Last Updater: Huan
 * Update Info: 
 *        - Use EZUtil Progress
 *        - Global variable -> local variable
 */
public class Activity_ForgotPass_Tab5 extends NavigationActivity {

	private Activity activity;

	private EditText edtEmail;
	private Button btnRequest;
	
	private Timer timer;
	
// ===================================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_forgotpass);

		activity = this;
		
		init();
	}

	public void init() {
		initBtnBack();
		initFunction();
	}

	public void initBtnBack() {
		LinearLayout layoutBack = (LinearLayout) findViewById(R.id.layout_Back);
		layoutBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hideKeyboard();
				onBackPressed();
			}
		});
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		hideKeyboard();
		super.onStop();
	}
	public void initFunction() {
		edtEmail = (EditText)findViewById(R.id.edtEmail);
		edtEmail.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER
							|| event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

						btnRequest.performClick();

					}
				}

				return false;
			}
		});
		btnRequest = (Button)findViewById(R.id.btnRequest);
		btnRequest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				hideKeyboard();
				if(!EZUtil.isValidEmail( edtEmail.getText().toString())) {
					showPopupOneOption(activity.getResources().getString(R.string.registerNoticeWrongEmail), 2);
					edtEmail.setText("");
				} else {
					if(EZUtil.isNetworkConnected(activity)) {
						AsyncTask_ForgotPass task = new AsyncTask_ForgotPass();
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
	
	private class AsyncTask_ForgotPass extends AsyncTask<Void, Void, String> {
		
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
			
			service_Password_Forgot xml_ForgotPass = new service_Password_Forgot(activity, edtEmail.getText().toString());
			String requestResult = xml_ForgotPass.start();

			return requestResult;
		}

		@Override
		protected void onPostExecute(String result) {
			
			timer.cancel();
			
			EZUtil.cancelProgress();
			
			if(result.equals("1")) {
				Timer t = new Timer();
				t.schedule(new TimerTask() {
					
					@Override  
					public void run() {
						// TODO Auto-generated method stub
						activity.runOnUiThread(new Runnable() {
							  public void run() {
								  showPopupOneOption(activity.getResources().getString(R.string.popup_ForgotPassOK), 2);
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
								  showPopupOneOption(activity.getResources().getString(R.string.popup_ForgotPass_Fail), 1);
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
			}, 300);
		}
	}
	public void showPopupOneOption(String content, int type) {
		
		final int finalType = type;
		if(!EZUtil.isLoading) {
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
					if(finalType == 2) {
						onBackPressed();
					}
				}
			});
		}
	}
	public void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edtEmail.getWindowToken(), 0);
	}
	
}
