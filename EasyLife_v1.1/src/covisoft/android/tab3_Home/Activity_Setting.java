package covisoft.android.tab3_Home;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import covisoft.android.EasyLife.R;
import covisoft.android.EasyLife.TabpageActivity;
import covisoft.android.tabhost.NavigationActivity;

public class Activity_Setting extends NavigationActivity {

	private LinearLayout layout_Back;
	
	private RelativeLayout layout_CustomerService;    // Customer Services
	private Button btn_CustomerService;
	
	private RelativeLayout layout_TermService;        // Term Of Services
	private Button btn_TermService;
	
	private RelativeLayout layout_Privacy;            // Privacy
	private Button btn_Privacy;
	
	private RelativeLayout layout_Recomment;          // Share application
	private Button btnRecomment;
	
	private RelativeLayout layout_SocialAccount;      // Control Social Network account
	private Button btn_SocialAccount;
	
	private RelativeLayout layout_Language;           // Change Language
	private Button btn_Language;

// ===================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_22_setting);

		initLayout();
		init_BtnBack();
	}

	public void initLayout() {

		layout_CustomerService = (RelativeLayout) findViewById(R.id.layout_CustomerService);
		layout_CustomerService.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(Activity_Setting.this,Activity_Privacy.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("type", "customerService");
				goNextHistory("Activity_Privacy", intent);

			}
		});

		btn_CustomerService = (Button) findViewById(R.id.btn_CustomerService);
		btn_CustomerService.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(Activity_Setting.this,Activity_Privacy.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("type", "customerService");
				goNextHistory("Activity_Privacy", intent);

			}
		});
		layout_TermService = (RelativeLayout) findViewById(R.id.layout_TermService);
		layout_TermService.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(Activity_Setting.this,Activity_Privacy.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("type", "term");
				goNextHistory("Activity_Privacy", intent);
			}
		});

		btn_TermService = (Button) findViewById(R.id.btn_TermService);
		btn_TermService.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(Activity_Setting.this,Activity_Privacy.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("type", "term");
				goNextHistory("Activity_Privacy", intent);

			}
		});
		layout_Privacy = (RelativeLayout) findViewById(R.id.layout_Privacy);
		layout_Privacy.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(Activity_Setting.this,Activity_Privacy.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("type", "privacy");
				goNextHistory("Activity_Privacy", intent);

			}
		});
		btn_Privacy = (Button) findViewById(R.id.btn_Privacy);
		btn_Privacy.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(Activity_Setting.this,Activity_Privacy.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("type", "privacy");
				goNextHistory("Activity_Privacy", intent);

			}
		});
		layout_SocialAccount = (RelativeLayout) findViewById(R.id.layout_SocialAccount);
		layout_SocialAccount.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(Activity_Setting.this,Activity_SocialNetwork.class);
				goNextHistory("Activity_SocialNetwork", intent);

			}
		});
		btn_SocialAccount = (Button) findViewById(R.id.btn_SocialAccount);
		btn_SocialAccount.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(Activity_Setting.this,Activity_SocialNetwork.class);
				goNextHistory("Activity_SocialNetwork", intent);
			}
		});
		
		
		layout_Recomment = (RelativeLayout) findViewById(R.id.layout_Recomment);
		layout_Recomment.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(Activity_Setting.this,Activity_Recommend.class);
				startActivity(intent);

			}
		});
		btnRecomment = (Button) findViewById(R.id.btn_Recomment);
		btnRecomment.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(Activity_Setting.this,Activity_Recommend.class);
				startActivity(intent);

			}
		});
		
		
		layout_Language = (RelativeLayout)findViewById(R.id.layout_Language);
		layout_Language.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(Activity_Setting.this, Activity_ChangeLanguage.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				goNextHistory("Activity_ChangeLanguage", intent);
				
			}
		});
		
		btn_Language = (Button)findViewById(R.id.btn_Language);
		btn_Language.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(Activity_Setting.this, Activity_ChangeLanguage.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				goNextHistory("Activity_ChangeLanguage", intent);
				
			}
		});
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		super.onResume();
	}
	void init_BtnBack() {
		layout_Back = (LinearLayout) findViewById(R.id.layout_Back);
		layout_Back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				onBackPressed();
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		TabpageActivity.btnThree.setVisibility(View.GONE);
		TabpageActivity.btnSearch.setVisibility(View.VISIBLE);
		super.onBackPressed();
	}
}
