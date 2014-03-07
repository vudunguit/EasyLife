package covisoft.android.tab3_Home;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
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
import covisoft.android.EasyLife.TabpageActivity;
import covisoft.android.services.service_Register;
import covisoft.android.tabhost.NavigationActivity;


/*
 * Activity use to register account EZ
 */
public class Activity_Register extends NavigationActivity {

	private Activity activity;
	private LinearLayout layout_Back;

	private Button btn_Join;
	private EditText edit_Username;
	private EditText edit_Password;
	private EditText edit_Repass;
	private EditText edit_Email;
	private EditText edit_Name;
	private EditText edit_date;
	private EditText edit_year;
	private EditText edit_Phone;
	private EditText edit_Address;
	private Spinner spinner_Month;
	private CheckBox cbFemale;
	private CheckBox cbMale;

	String Login_Type = "";

	private Timer timer;

// ======================================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View viewToLoad = LayoutInflater.from(this.getParent()).inflate(R.layout.layout_06_2_join, null);
		this.setContentView(viewToLoad);

		activity = this;

		Intent intent = getIntent();

		Login_Type = intent.getStringExtra("Login_Type");
		init();
	}

	void init() {
		init_Input();
		init_Btn_Join();
		init_BtnBack();
	}

	void init_BtnBack() {
		layout_Back = (LinearLayout) findViewById(R.id.layout_Back);
		layout_Back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				hidKeyboard();
				onBackPressed();
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(EasyLifeActivity.user != null) {
			onBackPressed();
		}
	}
	void init_Input() {
		edit_Username = (EditText) findViewById(R.id.edit_Username);
		edit_Password = (EditText) findViewById(R.id.edit_Password);
		edit_Repass = (EditText) findViewById(R.id.edit_Repass);
		edit_Email = (EditText) findViewById(R.id.edit_Email);
		edit_Name = (EditText) findViewById(R.id.edit_Name);
		edit_date = (EditText) findViewById(R.id.edit_date);
		edit_year = (EditText) findViewById(R.id.edit_year);
		edit_Phone = (EditText) findViewById(R.id.edit_Phone);
		edit_Phone.setOnFocusChangeListener(new OnFocusChangeListener() {          

	        public void onFocusChange(View v, boolean hasFocus) {
	            if(!hasFocus) {
	            	if(edit_Phone.getText().toString().trim().length() > 11) {
	            		showPopupOneOption(activity.getResources().getString(R.string.popup_RegisFail_WrongPhoneNo));	
	            	}
	            }
	        }
	    });
		edit_Address = (EditText) findViewById(R.id.edit_Address);

		spinner_Month = (Spinner) findViewById(R.id.spinner_Month);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.month_arrays, R.layout.my_spinner_textview);
		adapter.setDropDownViewResource(R.layout.my_spinner_textview);
		spinner_Month.setAdapter(adapter);

		cbFemale = (CheckBox) findViewById(R.id.cbFemale);
		cbMale = (CheckBox) findViewById(R.id.cbMale);

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

	void init_Btn_Join() {

		btn_Join = (Button) findViewById(R.id.btn_Join);
		btn_Join.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				hidKeyboard();

				String sUsername = edit_Username.getText().toString().trim();
				String sPass = edit_Password.getText().toString().trim();
				String sRepass = edit_Repass.getText().toString().trim();
				String sEmail = edit_Email.getText().toString().trim();

				if (!EZUtil.isLoading) {
					if (sUsername.equals("") || sPass.equals("") || sRepass.equals("") || sEmail.equals("")) { 

						showPopupOneOption(activity.getString(R.string.registerNoticeFullInfo));

						if (sUsername.equals("")) {
							edit_Username.setText("");
						}
						if (sPass.equals("")) {
							edit_Password.setText("");
						}
						if (sRepass.equals("")) {
							edit_Repass.setText("");
						}
						if (sEmail.equals("")) {
							edit_Email.setText("");
						}

					} else {
						if (EZUtil.getSpecialCharacter(edit_Username.getText().toString()) > 0) { 
							showPopupOneOption(activity.getResources().getString(R.string.registerNoticeWrongUsername2));
							edit_Username.setText("");

						} else if(EZUtil.checkUppercase(edit_Username.getText().toString().trim())) {
							showPopupOneOption(activity.getResources().getString(R.string.popup_Register_UserName_Uppercase));
							edit_Username.setText("");
						} else {
							if (sUsername.length() < 3) { 

								showPopupOneOption(activity.getResources().getString(R.string.registerNoticeWrongUsername));
								edit_Username.setText("");

							} else {

								if (sPass.length() < 4) {

									showPopupOneOption(activity.getResources().getString(R.string.registerNoticeWrongPassword));
									edit_Password.setText("");

								} else {
									if (!sPass.equals(sRepass)) {

										showPopupOneOption(activity.getResources().getString(R.string.registerNoticePasswordNotMatch));

										edit_Password.setText("");
										edit_Repass.setText("");

									} else {

										if (!EZUtil.isValidEmail(sEmail)) {

											showPopupOneOption(activity.getResources().getString(R.string.registerNoticeWrongEmail));
											edit_Email.setText("");

										} else {
											if (EZUtil.isNetworkConnected(activity)) {
												AsyncTask_Register task = new AsyncTask_Register();
												task.execute();
												timer = new Timer();
												timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);
												
											} else {
												showPopupOneOption(activity.getResources().getString(R.string.No_Internet_now));

											}
										}
									}
								}
							}
						}
					}
				}

			}
		});
	}

	private class AsyncTask_Register extends AsyncTask<Void, Void, String> {

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
			
			String sUsername = edit_Username.getText().toString().trim();
			String sPass     = edit_Password.getText().toString().trim();
			String sEmail    = edit_Email.getText().toString().trim();
			String sName     = edit_Name.getText().toString().trim();
			String sDate     = edit_date.getText().toString().trim();
			String sYear     = edit_year.getText().toString().trim();
			String sPhone    = edit_Phone.getText().toString().trim();
			String sAddress  = edit_Address.getText().toString().trim();
			String sMonth    = spinner_Month.getSelectedItem().toString().trim();

			String sBirthday = "";
			if(EZUtil.isThisDateValid(sDate + "/" + sMonth + "/" + sYear, "dd/MM/yyyy")) {
				sBirthday = sYear + "-" + sMonth + "-" + sDate;
			}
			
			String gender = "";
			if (cbMale.isChecked()) {
				gender = "M";
			} else {
				gender = "F";
			}

			
			service_Register xml = new service_Register();
			xml.init(sUsername, sPass, sEmail, sName, sBirthday, gender, sAddress, sPhone, activity);

			String result = xml.start();
			return result;
		}

		@Override
		protected void onPostExecute(String result) {

			timer.cancel();
			EZUtil.cancelProgress();
			if (result.equals("0")) {

				Timer t = new Timer();
				t.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO
						// Auto-generated
						// method stub
						activity.runOnUiThread(new Runnable() {
							public void run() {
								showPopupOneOption(activity.getResources().getString(R.string.popup_cannot_conect));
							}
						});
					}
				}, 300);

			} else if (result.equals("1")) {

				Timer t = new Timer();
				t.schedule(new TimerTask() {

					@Override
					public void run() {
						activity.runOnUiThread(new Runnable() {
							public void run() {
								showPopupOneOption(activity.getResources().getString(R.string.popup_RegisFail_UserExisted));
							}
						});
					}
				}, 300);

			} else if (result.equals("3")) {

				Timer t = new Timer();
				t.schedule(new TimerTask() {

					@Override
					public void run() {
						activity.runOnUiThread(new Runnable() {
							public void run() {
								showPopupOneOption(activity.getResources().getString(R.string.popup_RegisFail_EmailExisted));
							}
						});
					}
				}, 300);

			} else if (result.equals("2")) {

				Timer t = new Timer();
				t.schedule(new TimerTask() {

					@Override
					public void run() {
						activity.runOnUiThread(new Runnable() {
							public void run() {
								EZUtil.isLoading = true;
								final Dialog dialog = new Dialog(activity.getParent(), R.style.myBackgroundStyle);
								WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
								lp.copyFrom(dialog.getWindow().getAttributes());
								lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
								lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialog.setContentView(R.layout.popup_two_option);

								TextView txt = (TextView) dialog.findViewById(R.id.txtContent);
								txt.setText(activity.getString(R.string.registerSuccess));

								Button btn_OK = (Button) dialog.findViewById(R.id.btn_OK);
								btn_OK.setText(activity.getString(R.string.registerGoHome));
								btn_OK.setOnClickListener(new OnClickListener() {

									public void onClick(View v) {
										dialog.dismiss();
										onBackPressed();
										TabpageActivity.ChangePage(3, false, true);
									}
								});
								Button btn_Cancel = (Button) dialog.findViewById(R.id.btn_Cancel);
								btn_Cancel.setText(activity.getString(R.string.registerLogin));
								btn_Cancel.setOnClickListener(new OnClickListener() {

									public void onClick(View v) {
										dialog.dismiss();
										onBackPressed();
									}
								});

								dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
								dialog.show();

								dialog.setOnDismissListener(new OnDismissListener() {

									@Override
									public void onDismiss(DialogInterface dialog) {
										EZUtil.isLoading = false;
									}
								});
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

	public void hidKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit_Username.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(edit_Password.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(edit_Repass.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(edit_Email.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(edit_Name.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(edit_date.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(edit_year.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(edit_Phone.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(edit_Address.getWindowToken(), 0);
	}

}
