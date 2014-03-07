package covisoft.android.tab5;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.R;
import covisoft.android.services.service_Membership;
import covisoft.android.tabhost.NavigationActivity;


/*
 * Activity sent Membership Info
 * 
 * Service: http://www.easylife.com.vn/Web_Mobile_New/Webservice//SendMail/sendmail.php?
 * 			
 */
public class Activity_Membership extends NavigationActivity{

	private Button btn_Cancel;
	private Button btn_SendEmail;
	
	private LinearLayout ln_left;
	private LinearLayout ln_right;
	private CheckBox cb_1;
	private CheckBox cb_2;
	
	private EditText edit_Company;
	private EditText edit_Contact;
	private EditText edit_Address;
	private EditText edit_Content;
	private EditText edit_Email;
	
//===================================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_membership);

		init();
		
	}
	
	void init() {
		init_Edit();
		initButtonBack();
		init_Checkbox();
		init_Btn();
	}
	public void initButtonBack() {
		LinearLayout layout_Back = (LinearLayout)findViewById(R.id.layout_Back);
		layout_Back.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				hideKeyboard();
				onBackPressed();
			}
		});
	}

	public void init_Edit() {
		edit_Company = (EditText)findViewById(R.id.edit_Company);
		edit_Contact = (EditText)findViewById(R.id.edit_Contact);
		edit_Address = (EditText)findViewById(R.id.edit_Address);
		edit_Content = (EditText)findViewById(R.id.edit_Content);
		edit_Email = (EditText)findViewById(R.id.edit_Email);
		
	}
	
	public void init_Btn() {
		btn_Cancel = (Button)findViewById(R.id.btn_Cancel);
		btn_Cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
				final Dialog dialog = new Dialog(Activity_Membership.this.getParent(), R.style.myBackgroundStyle);
	        	WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    	    lp.copyFrom(dialog.getWindow().getAttributes());
	    	    lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
	    	    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
	    	    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    	    dialog.setContentView(R.layout.popup_two_option);
	    	  
	    	    TextView txt = (TextView)dialog.findViewById(R.id.txtContent);
	    	    txt.setText(v.getContext().getString(R.string.popup_Membership_SureToCancel));
	    	    
			    Button btn_OK = (Button)dialog.findViewById(R.id.btn_OK);
			    btn_OK.setText(v.getContext().getString(R.string.Yes));
			    btn_OK.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						dialog.dismiss();
						cb_1.setChecked(false);
						cb_2.setChecked(false);
						edit_Company.setText("");
						edit_Contact.setText("");
						edit_Address.setText("");
						edit_Content.setText("");
						edit_Email.setText("");
					}
				});
			    Button btn_Cancel = (Button)dialog.findViewById(R.id.btn_Cancel);
			    btn_Cancel.setText(v.getContext().getString(R.string.No));
			    btn_Cancel.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
	    	    
	    	    dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
	    	    dialog.show();
	    	    
			}
		});
		btn_SendEmail = (Button)findViewById(R.id.btn_SendEmail);
		btn_SendEmail.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
				
				if (!cb_1.isChecked() && !cb_2.isChecked()) {
		    	    
					showPopupOneOption(Activity_Membership.this.getResources().getString(R.string.popup_Membership_NotSelectType));
					
				} else {
					if (edit_Company.getText().toString().trim().equals("") 
							|| edit_Contact.getText().toString().trim().equals("") 
							|| edit_Address.getText().toString().trim().equals("") 
							|| edit_Content.getText().toString().trim().equals("")
							|| edit_Email.getText().toString().trim().equals("")) {
						
						showPopupOneOption(Activity_Membership.this.getResources().getString(R.string.popup_Membership_NotEnoughInfo));
						
					} else if(!EZUtil.isValidEmail(edit_Email.getText().toString().trim())) {
						
						showPopupOneOption(Activity_Membership.this.getResources().getString(R.string.popup_Membership_InvalidEmail));
						
					}
					else {
						
						final Dialog dialog = new Dialog(Activity_Membership.this.getParent(), R.style.myBackgroundStyle);
			        	WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			    	    lp.copyFrom(dialog.getWindow().getAttributes());
			    	    lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
			    	    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
			    	    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			    	    dialog.setContentView(R.layout.popup_two_option);
			    	  
			    	    TextView txt = (TextView)dialog.findViewById(R.id.txtContent);
			    	    txt.setText(v.getContext().getString(R.string.popup_Membership_SureToSendMail));
			    	    
					    Button btn_OK = (Button)dialog.findViewById(R.id.btn_OK);
					    btn_OK.setText(v.getContext().getString(R.string.Yes));
					    btn_OK.setOnClickListener(new OnClickListener() {
							
							public void onClick(View v) {
								dialog.dismiss();
								service_Membership xml = new service_Membership();
								if (cb_1.isChecked()) {
									xml.init(getResources().getString(R.string.membership_check1), edit_Company.getText().toString(), edit_Contact.getText().toString(), edit_Address.getText().toString(), edit_Content.getText().toString(), edit_Email.getText().toString(),Activity_Membership.this);
								} else {
									xml.init(getResources().getString(R.string.membership_check2), edit_Company.getText().toString(), edit_Contact.getText().toString(), edit_Address.getText().toString(), edit_Content.getText().toString(), edit_Email.getText().toString(),Activity_Membership.this);
								}
								
								String result = xml.start();
								
								if (result.equals("1")) {
									edit_Company.setText("");
									edit_Contact.setText("");
									edit_Address.setText("");
									edit_Content.setText("");
									edit_Email.setText("");
									
									showPopupOneOption(Activity_Membership.this.getResources().getString(R.string.popup_Membership_Send_mail_success));
									
								} else if (result.equals("The following From address failed: " + edit_Email.getText().toString() +  "\n0")) {
									
									showPopupOneOption(Activity_Membership.this.getResources().getString(R.string.popup_Membership_wrong_mail));
									
								} else {
									
									showPopupOneOption(Activity_Membership.this.getResources().getString(R.string.popup_Membership_Send_mail_fail));
									
								}
							}
						});
					    Button btn_Cancel = (Button)dialog.findViewById(R.id.btn_Cancel);
					    btn_Cancel.setText(v.getContext().getString(R.string.No));
					    btn_Cancel.setOnClickListener(new OnClickListener() {
							
							public void onClick(View v) {
								dialog.dismiss();
							}
						});
			    	    
			    	    dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
			    	    dialog.show();
						
						
					}
				}
				
			}
		});
	}
	
	public void init_Checkbox() {
		
		cb_1 = (CheckBox)findViewById(R.id.cb_1);
		cb_2 = (CheckBox)findViewById(R.id.cb_2);
		
		cb_1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(cb_1.isChecked() && cb_2.isChecked()) {
					cb_2.setChecked(false);
				}
			}
		});
		cb_2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(cb_2.isChecked() && cb_1.isChecked()) {
					cb_1.setChecked(false);
				}
			}
		});
		
		ln_left = (LinearLayout)findViewById(R.id.ln_left);
		ln_right = (LinearLayout)findViewById(R.id.ln_right);
		
		ln_left.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (cb_1.isChecked()) {
					cb_1.setChecked(false);
				} else {
					cb_1.setChecked(true);
					if(cb_2.isChecked()) {
						cb_2.setChecked(false);
					}
				}
			}
		});
		ln_right.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if (cb_2.isChecked()) {
					cb_2.setChecked(false);
				} else {
					cb_2.setChecked(true);
					if(cb_1.isChecked()) {
						cb_1.setChecked(false);
					}
				}
			}
		});
	}
	
	public void showPopupOneOption(String content) {
		
		final Dialog dialog = new Dialog(Activity_Membership.this.getParent(), R.style.myBackgroundStyle);
    	WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	    lp.copyFrom(dialog.getWindow().getAttributes());
	    lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
	    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
	    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    dialog.setContentView(R.layout.popup_one_option);
	  
	    TextView txt = (TextView)dialog.findViewById(R.id.txtContent);
	    txt.setText(content);
	    
	    Button btn_OK = (Button)dialog.findViewById(R.id.btn_OK);
	    btn_OK.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	    dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
	    dialog.show();
	}
	
	public void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit_Company.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(edit_Contact.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(edit_Address.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(edit_Content.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(edit_Email.getWindowToken(), 0);
	}

}
