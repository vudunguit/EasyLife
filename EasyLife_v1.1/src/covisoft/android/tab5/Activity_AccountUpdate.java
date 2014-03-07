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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import covisoft.android.EasyLife.CheckTimeAsyncTask;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.EasyLifeActivity;
import covisoft.android.EasyLife.R;
import covisoft.android.services.service_User_UpdateInfo;
import covisoft.android.tabhost.NavigationActivity;

/*
 * Activity use to update user's info
 * 
 * Service: http://easylife.com.vn/Web_Mobile_New/api/updateUserInfo/?
 * 			param: (username : string, name : String , birthday : string , gender : string , address)
 * 
 * Last Updated: 2.07.2013
 * Last Updater: Huan
 * Update Info:
 *        - Use EZUtil Progress
 *        - Global variable -> local variable
 * 
 */
public class Activity_AccountUpdate extends NavigationActivity {

	private Activity activity;

	private EditText edtName;
	private EditText edtDate;
	private Spinner spin_Month;
	private EditText edtYear;

	private CheckBox cbFemale;
	private CheckBox cbMale;

	private EditText edtPhone;
	private EditText edtAddress;

	private Timer timer;

// ===================================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(R.layout.layout_account_update, null);
		this.setContentView(viewToLoad);
		activity = this;

		init();
	}

	public void init() {
		initBtnBack();
		initWidget();
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

	public void initWidget() {
		edtName = (EditText) findViewById(R.id.edtName);
		edtName.setText(EasyLifeActivity.user.getName());
		edtDate = (EditText) findViewById(R.id.edtdate);
		edtYear = (EditText) findViewById(R.id.edtyear);

		edtPhone = (EditText) findViewById(R.id.edtPhone);
		edtPhone.setText(EasyLifeActivity.user.getTel());
		edtPhone.setOnFocusChangeListener(new OnFocusChangeListener() {          

	        public void onFocusChange(View v, boolean hasFocus) {
	            if(!hasFocus) {
	            	if(edtPhone.getText().toString().trim().length() > 11) {
	            		showPopupOneOption(activity.getResources().getString(R.string.popup_RegisFail_WrongPhoneNo));	
	            	}
	            }
	        }
	    });
		
		edtAddress = (EditText) findViewById(R.id.edtAddress);
		edtAddress.setText(EasyLifeActivity.user.getAddress());

		spin_Month = (Spinner) findViewById(R.id.spinner_Month);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.month_arrays, R.layout.my_spinner_textview);
		adapter.setDropDownViewResource(R.layout.my_spinner_textview);
		spin_Month.setAdapter(adapter);
		if (!EasyLifeActivity.user.getBirthday().equals("")) {

			String listString[] = EasyLifeActivity.user.getBirthday().split("-");
			
			edtDate.setText( listString[0]);

			edtYear.setText( listString[2]);
			
			if (Integer.parseInt( listString[1]) > 0 && Integer.parseInt( listString[1]) < 12) {
				spin_Month.setSelection(Integer.parseInt( listString[1]) - 1);
			}
			
		}

		cbFemale = (CheckBox) findViewById(R.id.cbFemale);
		cbMale = (CheckBox) findViewById(R.id.cbMale);

		if (EasyLifeActivity.user.getGender().equals("M")) {
			cbMale.setChecked(true);
			cbFemale.setChecked(false);
		} else {
			cbMale.setChecked(false);
			cbFemale.setChecked(true);
		}
		cbFemale.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (cbFemale.isChecked()) {
					cbMale.setChecked(false);
				} else {
					cbMale.setChecked(true);
				}
			}
		});
		cbMale.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (cbMale.isChecked()) {
					cbFemale.setChecked(false);
				} else {
					cbFemale.setChecked(true);
				}
			}
		});

	}

	public void initBtnConfirm() {
		Button btnConfirm = (Button) findViewById(R.id.btnConfirm);
		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				hideKeyboard();
				if (EZUtil.isNetworkConnected(activity)) {
					AsyncTask_UpdateInfo task = new AsyncTask_UpdateInfo();
					task.execute();
					timer = new Timer();
					timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);
				} else {
					showPopupOneOption(getResources().getString(R.string.No_Internet_now));
				}

			}
		});
	}

	private class AsyncTask_UpdateInfo extends AsyncTask<Void, Void, String> {

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
			String sDate = edtDate.getText().toString().trim();
			String sMonth = spin_Month.getSelectedItem().toString().trim();
			String sYear = edtYear.getText().toString().trim();

			String sGender = "";
			if (cbMale.isChecked()) {
				sGender = "M";
			} else {
				sGender = "F";
			}

			if (!sDate.equals("") && !sYear.equals("")) {
				if (Integer.parseInt(sMonth) < 10) {
					sMonth = "0" + sMonth;
				}
				if (Integer.parseInt(sDate) < 10) {
					sDate = "0" + sDate;
				}
				if (Integer.parseInt(sYear) < 1000) {
					sYear = "0" + sYear;
				}

			}

			String sBirthday = sYear + "-" + sMonth + "-" + sDate;

			if (!EZUtil.isThisDateValid(sBirthday, "yyyy-MM-dd")) {
				sBirthday = "1990-01-01";
			}

			String sAddress = edtAddress.getText().toString().trim();
			String sPhone = edtPhone.getText().toString().trim();

			service_User_UpdateInfo xml_UpdateInfo = new service_User_UpdateInfo(activity, EasyLifeActivity.user.getUsername(), sName, sBirthday, sGender, sAddress, sPhone);
			String updateResult = xml_UpdateInfo.start();

			return updateResult;
		}

		@Override
		protected void onPostExecute(String result) {

			timer.cancel();

			EZUtil.cancelProgress();

			if (result.equals("1")) {
				showPopupOneOption(activity.getResources().getString(R.string.popup_UpdateInfo_Success));
				onBackPressed();
			} else {
				showPopupOneOption(activity.getResources().getString(R.string.popup_UpdateInfo_Fail));
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
							showPopupOneOption(activity.getResources().getString(R.string.No_Response));
						}
					});
				}
			}, 300);
		}
	}

	public void showPopupOneOption(String content) {

		if (!EZUtil.isLoading) {
			EZUtil.isLoading = true;
			final Dialog dialog = new Dialog(Activity_AccountUpdate.this.getParent(), R.style.myBackgroundStyle);
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
				}
			});
		}

	}

	public void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edtName.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(edtDate.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(edtYear.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(edtAddress.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(edtPhone.getWindowToken(), 0);
	}
}
