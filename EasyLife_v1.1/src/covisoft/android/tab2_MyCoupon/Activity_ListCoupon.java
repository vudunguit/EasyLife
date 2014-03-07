package covisoft.android.tab2_MyCoupon;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
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
import covisoft.android.adapter.Adapter_Coupon_Added;
import covisoft.android.adapter.Adapter_QRCoupon;
import covisoft.android.item.item_Banner;
import covisoft.android.item.item_Coupon;
import covisoft.android.item.item_QRCoupon_list;
import covisoft.android.services.service_Banner;
import covisoft.android.services.service_Coupon_List;
import covisoft.android.services.service_QRCoupon_List;
import covisoft.android.services.service_User_Info;
import covisoft.android.tabhost.NavigationActivity;

/*
 * Activity List Coupon in tab 2
 * 
 * last Updated: 13/06/2013
 * last Updater: Huan
 * 
 * Last Updated: 01.07.2013
 * Last Updater: Huan
 * Update Info:
 *        - Use EZUtil progress
 */
public class Activity_ListCoupon extends NavigationActivity {

	private Activity activity;
	private LinearLayout layout_Back;
	private ArrayList<item_Banner> arItemBanner = new ArrayList<item_Banner>();
	
	/* List normal coupon */
	private Button btn_NormalCoupon;
	private ListView listCoupon;
	private Adapter_Coupon_Added adapter;
	private ArrayList<item_Coupon> arCoupon = new ArrayList<item_Coupon>();
	
	/* List QR Coupon */
	private Button btn_QRCoupon;
	private ListView listQRCoupon;
	private Adapter_QRCoupon adapter_QRCoupon;
	private ArrayList<item_QRCoupon_list> arQRCoupon = new ArrayList<item_QRCoupon_list>();

	private Timer timer;
	
	private String cur_Kind = "Coupon";

	
// ======================================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_14_tab2);

		activity = this;

		init();
	}
	@Override
	protected void onResume() {
		super.onResume();
		if ( EasyLifeActivity.user != null ) {
			
			String text = "";
			SharedPreferences settings = getSharedPreferences("Current_Coupon", 0);
			text = settings.getString("Current_Coupon", text);
			
			if(text.equals("QRCoupon")) {
				if(adapter_QRCoupon != null) {
					arQRCoupon.clear();
					adapter_QRCoupon.notifyDataSetChanged();
				}
				btn_QRCoupon.performClick();
			} else {
				if(adapter != null) {
					arCoupon.clear();
					adapter.notifyDataSetChanged();
				}
				btn_NormalCoupon.performClick();
			}
			
		} else {
			Intent intent = new Intent(Activity_ListCoupon.this, Activity_Login_Tab2.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			goNextHistory_2("Activity_Login_Tab2", intent);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		SharedPreferences settings = getSharedPreferences("Current_Coupon", MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("Current_Coupon", "Coupon");
		editor.commit();
		
		super.onBackPressed();
	}
	public void init() {
		
		listQRCoupon = (ListView) findViewById(R.id.listQRCoupon);
		listCoupon = (ListView) findViewById(R.id.listCoupon);
		
		initBtnBack();
		init_Toogle();

	}
	
	public void initBtnBack() {
		layout_Back = (LinearLayout) findViewById(R.id.layout_Back);
		layout_Back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	
	void init_Banner() {
		RelativeLayout banner = (RelativeLayout) findViewById(R.id.banner);
		viewFlipper_Banner.init(TabpageActivity.activity, banner, arItemBanner);
	}
	
	public void init_Toogle() {
		
		btn_NormalCoupon = (Button)findViewById(R.id.btn_NormalCoupon);
		btn_NormalCoupon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(!EZUtil.isLoading) {
					
					SharedPreferences settings = getSharedPreferences("Current_Coupon", MODE_WORLD_WRITEABLE);
					SharedPreferences.Editor editor = settings.edit();
					editor.putString("Current_Coupon", "Coupon");
					editor.commit();
					
					cur_Kind = "Coupon";
					
					btn_NormalCoupon.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_green_border_clicked));
					btn_QRCoupon.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_green_border));
					
					if(EZUtil.isNetworkConnected(activity)) {
						AsyncTaskRequestData task = new AsyncTaskRequestData();
						task.execute();
						timer = new Timer();
						timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);
					} else {
						showPopupOneOption(activity.getResources().getString(R.string.No_Internet_now), 2);
					}
				}
				
			}
		});
		
		btn_QRCoupon = (Button)findViewById(R.id.btn_QRCoupon);
		btn_QRCoupon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!EZUtil.isLoading){
					
					SharedPreferences settings = getSharedPreferences("Current_Coupon", MODE_WORLD_WRITEABLE);
					SharedPreferences.Editor editor = settings.edit();
					editor.putString("Current_Coupon", "QRCoupon");
					editor.commit();
					
					cur_Kind = "QRCoupon";
					
					btn_QRCoupon.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_green_border_clicked));
					btn_NormalCoupon.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_green_border));
					
					if(EZUtil.isNetworkConnected(activity)) {
						AsyncTaskRequestData task = new AsyncTaskRequestData();
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

	private class AsyncTaskRequestData extends AsyncTask<Void, Void, Void> {
		
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
			
			if(cur_Kind.equals("Coupon")){
				service_Coupon_List xml = new service_Coupon_List(EasyLifeActivity.user.getUsername(), activity);
				arCoupon.clear();
				arCoupon = xml.start();
				
			} else if(cur_Kind.equals("QRCoupon")){
				
				service_QRCoupon_List xml = new service_QRCoupon_List(activity, EasyLifeActivity.user.getUsername());
				arQRCoupon.clear();
				arQRCoupon = xml.start();

			}
			
			service_Banner xml = new service_Banner(Activity_ListCoupon.this, "2");
			arItemBanner = xml.start();
			
			if(EasyLifeActivity.user.getEmail() == null) {
				service_User_Info xml_UserInfo = new service_User_Info(activity, EasyLifeActivity.user.getUsername());
				xml_UserInfo.start();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			timer.cancel();

			EZUtil.cancelProgress();
			
			init_Banner();
			
			if(cur_Kind.equals("QRCoupon")){
				initListQRCoupon();
				
				listCoupon.setVisibility(View.GONE);
				listQRCoupon.setVisibility(View.VISIBLE);
				listQRCoupon.invalidate();
				listQRCoupon.requestLayout();
				
				if (arQRCoupon.size() == 0) {
					Timer t = new Timer();
					t.schedule(new TimerTask() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							activity.runOnUiThread(new Runnable() {
								  public void run() {
									  showPopupOneOption(activity.getResources().getString(R.string.popup_Noanycoupon), 1);
								  }
							});
						}
					}, 300);
					
				}
			} else if(cur_Kind.equals("Coupon")){
				initListView_NormalCoupon();
				listQRCoupon.setVisibility(View.GONE);
				listCoupon.setVisibility(View.VISIBLE);
				listCoupon.invalidate();
				listCoupon.requestLayout();
				
				if (arCoupon.size() == 0) {
					Timer t = new Timer();
					t.schedule(new TimerTask() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							activity.runOnUiThread(new Runnable() {
								  public void run() {
									  showPopupOneOption(activity.getResources().getString(R.string.popup_Noanycoupon), 1);
								  }
							});
						}
					}, 300);
					
				}
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


	public void initListView_NormalCoupon() {

		adapter = new Adapter_Coupon_Added(this, arCoupon);
		listCoupon.setAdapter(adapter);

	}

	public void initListQRCoupon() {
		
		adapter_QRCoupon = new Adapter_QRCoupon(this, arQRCoupon);
		listQRCoupon.setAdapter(adapter_QRCoupon);
		
	}
	
	public void showPopupOneOption(String content, int type) {
		
		final int fiType = type;
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
					if(fiType == 2) {
						onBackPressed();
					}
				}
			});
		}
	}
	
}
