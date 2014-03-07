package covisoft.android.tab5;

import java.util.ArrayList;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import covisoft.android.EasyLife.CheckTimeAsyncTask;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.EasyLifeActivity;
import covisoft.android.EasyLife.R;
import covisoft.android.EasyLife.TabpageActivity;
import covisoft.android.EasyLife.viewFlipper_Banner;
import covisoft.android.adapter.Adapter_Notification;
import covisoft.android.item.item_Banner;
import covisoft.android.item.item_Notification;
import covisoft.android.services.service_Banner;
import covisoft.android.services.service_Notification;
import covisoft.android.tabhost.NavigationActivity;


/*
 * Activity show List of Notify
 * 
 * Service: http://easylife.com.vn/Web_Mobile_New/api/myNotification/?
 * 			param: (username : string)
 */
public class Activity_NotifyList extends NavigationActivity {

	private Activity activity;
	
	private ListView listNotify;
	private Adapter_Notification adapter;
	private ArrayList<item_Notification> arNotify = null;
	
	private ArrayList<item_Banner> arItemBanner = new ArrayList<item_Banner>();
	
	private Timer timer;
	
// ===================================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_notifylist);

		activity = this;
		
		init();
	}

	public void init() {
		initBtnBack();
		
		if (EZUtil.isNetworkConnected(activity)) {
			AsyncTask_Notify task = new AsyncTask_Notify();
			task.execute();
			timer = new Timer();
			timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);
		} else {
			showPopupOneOption(activity.getResources().getString(R.string.No_Internet_now), 1);
		}
		
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

	void initBanner() {
		RelativeLayout banner = (RelativeLayout) findViewById(R.id.banner);
		viewFlipper_Banner.init(TabpageActivity.activity, banner, arItemBanner);
	}
	
	private class AsyncTask_Notify extends AsyncTask<Void, Void, Void> {

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
			
			service_Notification xmlNotify;
			if(EasyLifeActivity.user != null) {
				xmlNotify = new service_Notification(activity, EasyLifeActivity.user.getUsername());
			} else {
				xmlNotify = new service_Notification(activity, "");
			}
			
			arNotify = xmlNotify.start();
			
			service_Banner xmlBanner = new service_Banner(activity, "2");
			arItemBanner = xmlBanner.start();
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			timer.cancel();

			EZUtil.cancelProgress();

			if (arNotify != null) {
				listNotify = (ListView)findViewById(R.id.listNotify);
				adapter = new Adapter_Notification(Activity_NotifyList.this, arNotify);
				listNotify.setAdapter(adapter);
				
				//==================================================================
		        if(arNotify.size() > 0 && arNotify.size() < 100) {
					TabpageActivity.rel_Notification.setVisibility(View.VISIBLE);
					TabpageActivity.txt_Number.setText(arNotify.size() + "");
				} else if(arNotify.size() >= 100) {
					TabpageActivity.rel_Notification.setVisibility(View.VISIBLE);
					TabpageActivity.txt_Number.setText("...");
				} else {
					TabpageActivity.rel_Notification.setVisibility(View.GONE);
				}
				
				//==================================================================
			} else {
				Timer t = new Timer();
				t.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						activity.runOnUiThread(new Runnable() {
							public void run() {
								showPopupOneOption(Activity_NotifyList.this.getResources().getString(R.string.popup_cannot_conect), 1);
							}
						});
					}
				}, 300);

			}
			
			initBanner();
			
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
						Intent intent = new Intent(Activity_NotifyList.this, Activity_MyProfile_Tab5.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						goNextHistory_2("Activity_MyProfile_Tab5", intent);
					}
				}
			});
		}
	}
	
}
