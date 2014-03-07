package covisoft.android.tab4;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import covisoft.android.EasyLife.CheckTimeAsyncTask;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.R;
import covisoft.android.EasyLife.TabpageActivity;
import covisoft.android.adapter.Adapter_MainCategory_4;
import covisoft.android.item.item_MainCategory;
import covisoft.android.services.service_SmallAd_Quantity;
import covisoft.android.tab3_Home.Activity_Search;
import covisoft.android.tabhost.NavigationActivity;

public class Activity_Category extends NavigationActivity {

	private Activity activity;
	
	private LinearLayout layout_Back;
	private LinearLayout layout_Search;

	private ListView listCategory;
	private Adapter_MainCategory_4 adapter;

	private ArrayList<item_MainCategory> arrCategory = new ArrayList<item_MainCategory>();
	
	private Timer timer;
	
//----------------------------------------------------------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main_category_tab4);

		activity = this;

		InitButton();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(EZUtil.isNetworkConnected(activity)) {
			
			AsyncTaskRequestData task = new AsyncTaskRequestData();
			task.execute();
			timer = new Timer();
			timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);
			
		} else {
			showPopupOneOption(activity.getResources().getString(R.string.No_Internet_now), 2);
		}
		
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		TabpageActivity.ChangePage(3, false, false);
	}
	
	public void InitButton() {
		layout_Back = (LinearLayout) findViewById(R.id.layout_Back);
		layout_Back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				onBackPressed();
			}
		});

		layout_Search = (LinearLayout) findViewById(R.id.layout_Search);
		layout_Search.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(Activity_Category.this, Activity_Search.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				goNextHistory("Activity_Search", intent);
			}
		});
		layout_Search.setVisibility(View.GONE);
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
			service_SmallAd_Quantity xml_Category = new service_SmallAd_Quantity(activity);
			arrCategory = xml_Category.start();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			timer.cancel();
			
			EZUtil.cancelProgress();
			
			init_ListCategory();
			
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
	
	public void init_ListCategory() {

		listCategory = (ListView) findViewById(R.id.listCategory);
		listCategory.setVisibility(View.VISIBLE);
		adapter = new Adapter_MainCategory_4(Activity_Category.this, arrCategory);

		listCategory.setAdapter(adapter);
		listCategory.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
				if(arrCategory.get(arg2).getQuantity() > 0) {
					Intent intent = new Intent(Activity_Category.this, Activity_Store_List.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("category", arrCategory.get(arg2).getCategory_code());
					goNextHistory("Activity_Store_List", intent);
				} else {
					showPopupOneOption(activity.getResources().getString(R.string.popup_No_Recruitment), 1);
				}
				
			}

		});
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
