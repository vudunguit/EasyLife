package covisoft.android.tab5;

import java.util.ArrayList;
import java.util.Timer;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import covisoft.android.EasyLife.CheckTimeAsyncTask;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.EasyLifeActivity;
import covisoft.android.EasyLife.R;
import covisoft.android.EasyLife.TabpageActivity;
import covisoft.android.EasyLife.viewFlipper_Banner;
import covisoft.android.item.item_Banner;
import covisoft.android.services.service_Banner;
import covisoft.android.tab3_Home.Activity_Setting;
import covisoft.android.tabhost.NavigationActivity;

public class Activity_More  extends NavigationActivity{

	private Activity activity;
	
	private ArrayList<item_Banner> arItemBanner = new ArrayList<item_Banner>();
	
	private Timer timer;
	
	public static RelativeLayout rel_Notification;
	public static TextView txt_Number;
	
//===================================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_more);

		activity = this;
		
		init();
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		if(!TabpageActivity.txt_Number.getText().equals("") && !TabpageActivity.txt_Number.getText().equals("0")) {
			TabpageActivity.rel_Notification.setVisibility(View.VISIBLE);
			rel_Notification.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(!TabpageActivity.txt_Number.getText().equals("") && !TabpageActivity.txt_Number.getText().equals("0")) {
			TabpageActivity.rel_Notification.setVisibility(View.VISIBLE);
			rel_Notification.setVisibility(View.GONE);
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		rel_Notification = (RelativeLayout)findViewById(R.id.rel_Notification);
		rel_Notification.setVisibility(View.GONE);
		txt_Number = (TextView)findViewById(R.id.txt_Number);
		
		if(TabpageActivity.rel_Notification.getVisibility() == View.VISIBLE) {
			rel_Notification.setVisibility(View.VISIBLE);
			txt_Number.setText(TabpageActivity.txt_Number.getText());
			
			TabpageActivity.rel_Notification.setVisibility(View.GONE);
		}
	}
	public void init() {
		initButtonBack();
		initLayoutContact();
		initLayoutProfile();
		initLayoutNotify();
		initLayoutInfo();
		initLayoutSetting();
		
		if(EZUtil.isNetworkConnected(activity)) {
			AsyncTask_Banner task = new AsyncTask_Banner();
			task.execute();
			timer = new Timer();
			timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);
		}
	}
	public void initBanner() {
		RelativeLayout banner = (RelativeLayout) findViewById(R.id.banner);
		viewFlipper_Banner.init(TabpageActivity.activity, banner, arItemBanner);
		
	}
	public void initButtonBack() {
		LinearLayout layoutBack = (LinearLayout)findViewById(R.id.layout_Back);
		layoutBack.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				onBackPressed();
			}
		});
	}
	
	//************************************
	//************************************
	//     Go to Activity Membership    **
	//************************************
	//************************************
	public void initLayoutContact() {
		LinearLayout layoutContact = (LinearLayout)findViewById(R.id.layoutContact);
		layoutContact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Activity_More.this, Activity_Membership.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				goNextHistory("Activity_Membership", intent);
			}
		});
	}
	
	//************************************
	//************************************
	//       Go to Activity Profile     **
	//************************************
	//************************************
	public void initLayoutProfile() {
		LinearLayout layoutProfile = (LinearLayout)findViewById(R.id.layoutProfile);
		layoutProfile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(EasyLifeActivity.user != null) {
					Intent intent = new Intent(Activity_More.this, Activity_MyProfile_Tab5.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
					goNextHistory("Activity_MyProfile", intent);
				} else {
					Intent intent = new Intent(Activity_More.this, Activity_Login_Tab5.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					goNextHistory("Activity_MyProfile", intent);
				}
			}
		});
	}
	
	//************************************
	//************************************
	//       Go to Activity Notify      **
	//************************************
	//************************************
	public void initLayoutNotify() {
		
		LinearLayout layoutNotify = (LinearLayout)findViewById(R.id.layoutNotify);
		layoutNotify.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				if(EasyLifeActivity.user != null) {
					Intent intent = new Intent(Activity_More.this, Activity_NotifyList.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					goNextHistory("Activity_NotifyList", intent);
//				} else {
//					Intent intent = new Intent(Activity_More.this, Activity_Login_Tab5.class);
//					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					intent.putExtra("from", "Activity_More");
//					goNextHistory("Activity_MyProfile", intent);
//				}
				
			}
		});
	}
	
	//************************************
	//************************************
	//   Go to Activity EZlife Info     **
	//************************************
	//************************************
	public void initLayoutInfo() {
		LinearLayout layoutInfo = (LinearLayout)findViewById(R.id.layoutInfo);
		layoutInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Activity_More.this, Activity_ProductInfo.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				goNextHistory("Activity_ProductInfo", intent);
			}
		});
	}
	
	//************************************
	//************************************
	//   Go to Activity EZlife Info     **
	//************************************
	//************************************
	public void initLayoutSetting() {
		LinearLayout layoutSetting = (LinearLayout)findViewById(R.id.layoutSetting);
		layoutSetting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Activity_More.this, Activity_Setting.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				goNextHistory("Activity_Setting", intent);

			}
		});
	}
	
	private class AsyncTask_Banner extends AsyncTask<Void, Void, Void> {

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

			service_Banner xml = new service_Banner(Activity_More.this, "3");
			arItemBanner = xml.start();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			timer.cancel();

			EZUtil.cancelProgress();
			
			initBanner();
			
			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {

			timer.cancel();
			EZUtil.cancelProgress();
		}
	}
}
