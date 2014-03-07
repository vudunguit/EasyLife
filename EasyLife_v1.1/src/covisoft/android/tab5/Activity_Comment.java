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
import android.view.View;
import android.view.View.OnClickListener;
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
import covisoft.android.services.service_User_Feedback;
import covisoft.android.tabhost.NavigationActivity;

/*
 * Activity Comment 
 * 
 * Service: http://easylife.com.vn/Web_Mobile_New/api/report/?
 * 			param: (title : string, name : String , email : string , phone : string , content : String)
 * 
 * Last Updated: 1.07.2013
 * Last Updater: Huan
 * Update Info: 
 *        - Use EZUtil Progress
 *        - Global variable -> local variable
 */
public class Activity_Comment extends NavigationActivity {

	private Activity activity;
	
	private EditText edtName;
	private EditText edtEmail;
	private EditText edtPhone;
	private EditText edtTitle;
	private EditText edtContent;
	
	private Timer timer;
	
// ===================================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_comment_ezlife);

		activity = this;
		
		init();
	}

	public void init() {
		initBtnBack();
		initBtnConfirm();
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

	public void initBtnConfirm() {
		
		edtName = (EditText)findViewById(R.id.edtName);
		edtEmail = (EditText)findViewById(R.id.edtEmail);
		edtPhone = (EditText)findViewById(R.id.edtPhone);
		edtTitle = (EditText)findViewById(R.id.edtTitle);
		edtContent = (EditText)findViewById(R.id.edtComment);
		
		Button btnConfirm = (Button)findViewById(R.id.btnConfirm);
		btnConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(EZUtil.isNetworkConnected(activity)) {
					AsyncTask_Comment task = new AsyncTask_Comment();
					task.execute();
					timer = new Timer();
					timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);
				} else {
					showPopupOneOption(getResources().getString(R.string.No_Internet_now), 1);
				}
			}
		});
	}
	
	private class AsyncTask_Comment extends AsyncTask<Void, Void, String> {

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

			String sName = edtName.getText().toString().trim();
			String sEmail = edtEmail.getText().toString().trim();
			String sPhone = edtPhone.getText().toString().trim();
			String sTitle = edtTitle.getText().toString().trim();
			String sContent = edtContent.getText().toString().trim();
			
			service_User_Feedback xml_Comment = new service_User_Feedback(activity, sName, sEmail, sPhone, sTitle, sContent);
			String cmResult = xml_Comment.start();
			
			return cmResult;
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
								showPopupOneOption(activity.getResources().getString(R.string.popup_CommentEZ_success), 2);
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
								showPopupOneOption(activity.getResources().getString(R.string.popup_CommentEZ_fail), 1);
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
		final int isBack = type;
		if (!EZUtil.isLoading) {
			EZUtil.isLoading = true;
			final Dialog dialog = new Dialog(Activity_Comment.this.getParent(), R.style.myBackgroundStyle);
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
					if(isBack == 2) {
						onBackPressed();
					}
				}
			});
		}

	}

	public void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edtName.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(edtEmail.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(edtPhone.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(edtTitle.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(edtContent.getWindowToken(), 0);
	}
	
}
