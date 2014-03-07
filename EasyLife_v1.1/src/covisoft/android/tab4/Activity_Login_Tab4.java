package covisoft.android.tab4;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.BaseRequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionStore;
import com.facebook.android.Utility;

import covisoft.android.EasyLife.CheckTimeAsyncTask;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.EasyLifeActivity;
import covisoft.android.EasyLife.R;
import covisoft.android.EasyLife.ServerUtilities;
import covisoft.android.EasyLife.TabpageActivity;
import covisoft.android.item.item_FBLogin;
import covisoft.android.item.item_Notification;
import covisoft.android.item.item_User;
import covisoft.android.services.service_Login;
import covisoft.android.services.service_Login_FB;
import covisoft.android.services.service_Notification;
import covisoft.android.tab3_Home.Activity_Register;
import covisoft.android.tab5.Activity_ForgotPass_Tab5;
import covisoft.android.tabhost.NavigationActivity;

public class Activity_Login_Tab4 extends NavigationActivity {

	private LinearLayout layoutBack;
	private Activity activity;

	private EditText edtEmail;
	private EditText edtPassword;
	private Button btnLogin;
	private Button btnRegister;
	private Button btnForgot;
	private Button btnFBLogin;

	private SharedPreferences settings;
	private SharedPreferences.Editor editor;

	private Timer timer;
	
	//---FB Info-----
	private String fbEmail;
	private String fbUsername;
	private String fbFname;
	private String fbGender;
	private String fbImg;

	private String[] permission = {"offline_access", "publish_stream", "user_photos", "publish_checkins", "photo_upload", "email"};
	
	private ArrayList<item_Notification> arNotify = null;
	
// ===================================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_login);

		activity = this;
		
		init();
	}

	void init() {
		
		SessionStore.restore(Utility.mFacebook, this);
		
		initBtnRegister();
		init_btn_Login();
		initBtnForgotPass();
		initBtnBack();
		init_EditText();

	}
	
	public void initBtnBack() {
		layoutBack = (LinearLayout) findViewById(R.id.layout_Back);
		layoutBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}

	@Override
	public void onBackPressed() {

		hideKeyboard();
		
		super.onBackPressed();

	}
	
	@Override
	protected void onPause() {
		hideKeyboard();
		super.onPause();
	}
	
	@Override
	protected void onResume() {

		init_BtnFB();
		
		if(EasyLifeActivity.user != null || (Utility.mFacebook != null && Utility.mFacebook.isSessionValid())) {
			onBackPressed();
		}
		super.onResume();
	}
	
	public void init_EditText() {

		String text = "";
		
		settings = getSharedPreferences("UserName", 0);
		text = settings.getString("username", text);
		edtEmail = (EditText) findViewById(R.id.edtEmail);
		edtEmail.setText(text);

		settings = getSharedPreferences("Pass", 0);
		text = settings.getString("pass", text);
		edtPassword = (EditText) findViewById(R.id.edtPassword);
		edtPassword.setText(text);

		if (!edtEmail.getText().toString().equals("") && !edtPassword.getText().toString().equals("")) {
			if (EasyLifeActivity.user == null) {
				btnLogin.performClick();
			}

		}

	}
	
	void init_btn_Login() {
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edtEmail.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(edtPassword.getWindowToken(), 0);

				String text = "";

				if (edtEmail.getText().toString().trim().compareTo("") == 0) {
					text = getString(R.string.popup_LoginFail_FillID);
				} else if (edtPassword.getText().toString().trim().compareTo("") == 0) {
					text = getString(R.string.popup_LoginFail_FillPass);
				}

				if (text.compareTo("") == 0) {

					if (EZUtil.isNetworkConnected(activity)) {
						AsyncTask_Login task = new AsyncTask_Login();
						task.execute();
						timer = new Timer();
						timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);
					} else {
						showPopupOneOption(activity.getResources().getString(R.string.No_Internet_now), 1);
					}

				} else {

					showPopupOneOption(text, 1);

				}
			}
		});

	}
	
	//********************
	//  AsyncTask Login  *
	//********************
	private class AsyncTask_Login extends AsyncTask<Void, Void, String> {

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

			String username = edtEmail.getText().toString();
			String password = edtPassword.getText().toString();

			service_Login xml = new service_Login(Activity_Login_Tab4.this, username, password);
			String loginResult = xml.start();
			
			return loginResult;
		}

		@Override
		protected void onPostExecute(String result) {

			timer.cancel();

			EZUtil.cancelProgress();

			if (result.equals("2")) {

				settings = getSharedPreferences("UserName", MODE_WORLD_WRITEABLE);
				editor = settings.edit();
				editor.putString("username", edtEmail.getText().toString());
				editor.commit();

				settings = getSharedPreferences("Pass", MODE_WORLD_WRITEABLE);
				editor = settings.edit();
				editor.putString("pass", edtPassword.getText().toString());
				editor.commit();

				settings = getSharedPreferences("Save", MODE_WORLD_WRITEABLE);
				editor = settings.edit();
				editor.putBoolean("save", true);

				editor.commit();

				EasyLifeActivity.user = new item_User();
				EasyLifeActivity.user.setUsername(edtEmail.getText().toString());

				AsyncTask_RegisID register = new AsyncTask_RegisID();
				register.execute();
				
				AsyncTask_Notify task = new AsyncTask_Notify();
				task.execute();
				
				Timer t = new Timer();
				t.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						activity.runOnUiThread(new Runnable() {
							public void run() {
								showPopupOneOption(activity.getResources().getString(R.string.popup_LoginSuccessful) + " " + EasyLifeActivity.user.getUsername(), 2);
							}
						});
					}
				}, 300);

			} else if (result.equals("0")) {
				Timer t = new Timer();
				t.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						activity.runOnUiThread(new Runnable() {
							public void run() {
								showPopupOneOption(Activity_Login_Tab4.this.getResources().getString(R.string.popup_LoginFail_cannotConnect), 1);
							}
						});
					}
				}, 300);

			} else if (result.equals("1")) {
				Timer t = new Timer();
				t.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						activity.runOnUiThread(new Runnable() {
							public void run() {
								showPopupOneOption(Activity_Login_Tab4.this.getResources().getString(R.string.popup_LoginFail), 1);
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
								showPopupOneOption(Activity_Login_Tab4.this.getResources().getString(R.string.popup_cannot_conect), 1);
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
	
	private class AsyncTask_RegisID extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			ServerUtilities.register(activity, EasyLifeActivity.regId, edtEmail.getText().toString());
			return null;
		}

	}
	
	private class AsyncTask_Notify extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			if (isCancelled()) {
				return null;
			}
			
			service_Notification xmlNotify;
			if(EasyLifeActivity.user != null) {
				xmlNotify = new service_Notification(activity, EasyLifeActivity.user.getUsername());
			} else {
				xmlNotify = new service_Notification(activity, "");
			}
			
			arNotify = xmlNotify.start();
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			if (arNotify != null) {
				
				//==================================================================
		        if(arNotify.size() > 0 && arNotify.size() < 100) {
					TabpageActivity.rel_Notification.setVisibility(View.VISIBLE);
					TabpageActivity.txt_Number.setText(arNotify.size() + "");
				} else if(arNotify.size() >= 100) {
					TabpageActivity.rel_Notification.setVisibility(View.VISIBLE);
					TabpageActivity.txt_Number.setText("...");
				} else {
					TabpageActivity.rel_Notification.setVisibility(View.GONE);
					TabpageActivity.txt_Number.setText("0");
				}
				
				//==================================================================
			} else {
				TabpageActivity.rel_Notification.setVisibility(View.GONE);
				TabpageActivity.txt_Number.setText("0");
			}
			
			super.onPostExecute(result);
		}

	}
	
	void initBtnRegister() {
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edtEmail.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(edtPassword.getWindowToken(), 0);

				Intent intent = new Intent(Activity_Login_Tab4.this, Activity_Register.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				goNextHistory("Activity_Register", intent);

			}
		});
	}
	public void initBtnForgotPass() {
		btnForgot = (Button) findViewById(R.id.btnForgot);
		btnForgot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edtEmail.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(edtPassword.getWindowToken(), 0);

				Intent intent = new Intent(Activity_Login_Tab4.this, Activity_ForgotPass_Tab5.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				goNextHistory("Activity_ForgotPass_Tab5", intent);
			}
		});
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
						onBackPressed();
					}
				}
			});
		}
	}
	
	public void init_BtnFB() {
		btnFBLogin = (Button)findViewById(R.id.btnFBLogin);
		
		if(EasyLifeActivity.user != null) {
			com.facebook.android.SessionStore.restore(Utility.mFacebook, activity.getParent());
			btnFBLogin.setVisibility(View.GONE);
		} else {
			btnFBLogin.setVisibility(View.VISIBLE);
			btnFBLogin.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!Utility.mFacebook.isSessionValid()) {
						Toast.makeText(Activity_Login_Tab4.this, "Authorizing", Toast.LENGTH_SHORT).show();
						Utility.mFacebook.authorize(Activity_Login_Tab4.this.getParent(), permission, new LoginDialogListener());
					} else {
						activity.runOnUiThread(new Runnable() {
							public void run() {
								if (getParent() != null) {
									EZUtil.init_progressDialog(getParent());
								} else {
									EZUtil.init_progressDialog(activity);
								}
							}
						});
						try {
							//The user has logged in, so now you can query and use their Facebook info
							SessionStore.save(Utility.mFacebook, Activity_Login_Tab4.this);
							
							if (Utility.mFacebook.isSessionValid()) {
								Utility.mFacebook.setAccessToken(Utility.mFacebook.getAccessToken());
					            requestUserData();
					        } else {
					        	EZUtil.cancelProgress();
					        }
							
						} catch (FacebookError error) {
							EZUtil.cancelProgress();
							error.printStackTrace();
						} catch (Exception error) {
							EZUtil.cancelProgress();
							error.printStackTrace();
						}
					}
				}
			});
		}
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Utility.mFacebook.authorizeCallback(requestCode, resultCode, data);
	}
	// ***********************************************************************
	// ***********************************************************************
	// LoginDialogListener
	// ***********************************************************************
	// ***********************************************************************
	public final class LoginDialogListener implements DialogListener {
		public void onComplete(Bundle values) {
			btnFBLogin.setVisibility(View.GONE);
			activity.runOnUiThread(new Runnable() {
				public void run() {
					if (getParent() != null) {
						EZUtil.init_progressDialog(getParent());
					} else {
						EZUtil.init_progressDialog(activity);
					}
				}
			});
			try {
				//The user has logged in, so now you can query and use their Facebook info
				SessionStore.save(Utility.mFacebook, Activity_Login_Tab4.this);
				if (Utility.mFacebook.isSessionValid()) {
					Utility.mFacebook.setAccessToken(Utility.mFacebook.getAccessToken());
		            requestUserData();
		        } else {
		        	EZUtil.cancelProgress();
		        }
				
			} catch (FacebookError error) {
				EZUtil.cancelProgress();
				error.printStackTrace();
			} catch (Exception error) {
				EZUtil.cancelProgress();
				error.printStackTrace();
			}
		}

		public void onFacebookError(FacebookError error) {
			btnFBLogin.setVisibility(View.VISIBLE);
			Toast.makeText(Activity_Login_Tab4.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
		}

		public void onError(DialogError error) {
			btnFBLogin.setVisibility(View.VISIBLE);
			Toast.makeText(Activity_Login_Tab4.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
		}

		public void onCancel() {
			btnFBLogin.setVisibility(View.VISIBLE);
			Toast.makeText(Activity_Login_Tab4.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
		}
	}
	
	
	public void requestUserData() {
        Bundle params = new Bundle();  
        params.putString("fields", "name, picture, gender, username, email");
        Utility.mAsyncRunner.request("me", params, new UserRequestListener());
    }
	 /*
     * Callback for fetching current user's name, picture, uid.
     */
    public class UserRequestListener extends BaseRequestListener {

        @Override
        public void onComplete(final String response, final Object state) {
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(response);
                
                Utility.userUID = jsonObject.getString("id");
                fbEmail = jsonObject.getString("email");
                fbUsername = jsonObject.getString("username");
                fbFname = jsonObject.getString("name");
                fbGender = jsonObject.getString("gender");
                fbImg = jsonObject.getJSONObject("picture").getJSONObject("data").getString("url");
                
                Log.e("getUserInfo","email: " + fbEmail + " - username: " + fbUsername + " - name: " + fbFname + " - gender: " + fbGender + 
                		" - Picture: " + fbImg);
                
                AsyncTask_LoginFB task = new AsyncTask_LoginFB();
				task.execute();
				timer = new Timer();
				timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);
				
            } catch (JSONException e) {
            	EZUtil.cancelProgress();
                e.printStackTrace();
            }
        }

    }
    
	
    private class AsyncTask_LoginFB extends AsyncTask<Void, Void, item_FBLogin> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
			super.onPreExecute();

		}

		@Override
		protected item_FBLogin doInBackground(Void... params) {

			if (isCancelled()) {
				return null;
			}

			service_Login_FB xml = new service_Login_FB(Activity_Login_Tab4.this, fbEmail, fbUsername, fbFname, fbGender, fbImg);
			item_FBLogin result = xml.start();

			return result;
		}

		@Override
		protected void onPostExecute(item_FBLogin result) {

			timer.cancel();

			EZUtil.cancelProgress();
			
			if (result.getResult().equals("1")) {
				btnFBLogin.setVisibility(View.GONE);

				settings = getSharedPreferences("UserName", MODE_WORLD_WRITEABLE);
				editor = settings.edit();
				editor.putString("username", result.getUsername());
				editor.commit();

				settings = getSharedPreferences("Save", MODE_WORLD_WRITEABLE);
				editor = settings.edit();
				editor.putBoolean("save", true);

				editor.commit();

				EasyLifeActivity.user = new item_User();
				EasyLifeActivity.user.setUsername(result.getUsername());

				AsyncTask_RegisID register = new AsyncTask_RegisID();
				register.execute();
				
				AsyncTask_Notify task = new AsyncTask_Notify();
				task.execute();
				
				Timer t = new Timer();
				t.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						activity.runOnUiThread(new Runnable() {
							public void run() {
								showPopupOneOption(activity.getResources().getString(R.string.popup_LoginSuccessful) + " " + EasyLifeActivity.user.getUsername(), 2);
							}
						});
					}
				}, 300);

			} else if(result.getResult().equals("2")) {
				btnFBLogin.setVisibility(View.VISIBLE);
				Timer t = new Timer();
				t.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						activity.runOnUiThread(new Runnable() {
							public void run() {
								showPopupOneOption(Activity_Login_Tab4.this.getResources().getString(R.string.popup_LoginFB_ProblemConnect), 1);
							}
						});
					}
				}, 300);
			} else if(result.getResult().equals("3")){
				btnFBLogin.setVisibility(View.GONE);
				settings = getSharedPreferences("UserName", MODE_WORLD_WRITEABLE);
				editor = settings.edit();
				editor.putString("username", result.getUsername());
				editor.commit();

				settings = getSharedPreferences("Save", MODE_WORLD_WRITEABLE);
				editor = settings.edit();
				editor.putBoolean("save", true);

				editor.commit();

				EasyLifeActivity.user = new item_User();
				EasyLifeActivity.user.setUsername(result.getUsername());
				
				AsyncTask_RegisID register = new AsyncTask_RegisID();
				register.execute();
				
				AsyncTask_Notify task = new AsyncTask_Notify();
				task.execute();
				
				Timer t = new Timer();
				t.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						activity.runOnUiThread(new Runnable() {
							public void run() {
								showPopupOneOption(Activity_Login_Tab4.this.getResources().getString(R.string.popup_LoginFB_New), 2);
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
    
    public void hideKeyboard() {
    	InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edtEmail.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(edtPassword.getWindowToken(), 0);
    }

}
