package covisoft.android.tab3_Home;

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
import covisoft.android.EasyLife.R;
import covisoft.android.adapter.Adapter_Franchise_Home;
import covisoft.android.item.item_Franchise;
import covisoft.android.services.service_Franchise;
import covisoft.android.tabhost.NavigationActivity;

/*
 * Activity show list of Franchise: Home_Activity -> Franchise_Category -> Franchise
 * 
 * Last Updated: 14/06/2013
 * Last Updater: Huan
 * 
 * Last Updated: 14/06/2013
 * Last Updater: Huan
 * Update Info:
 *        - Use EZUtil Progress
 * 
 */
public class Activity_Franchise extends NavigationActivity {
	
	private Activity activity;
	private LinearLayout layout_Back;
	private RelativeLayout layoutTop;
	
	private String category_Code = "";
	private String category_Name = "";
	private TextView txtTitle;
	
	private ListView listFranchise;
	
	private Timer timer;

// ===================================================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_18_franchise);

		activity = this;
		
		Intent intent = getIntent();
		category_Code = intent.getStringExtra("category_Code");
		category_Name = intent.getStringExtra("category_Name");

		init();
	}

	public void init() {

		initTop();
		initBtnBack();
		
		if (EZUtil.isNetworkConnected(activity)) {
			AsyncTaskRequestData task = new AsyncTaskRequestData();
			task.execute();
			timer = new Timer();
			timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);
		} else {
			showPopupOneOption(activity.getResources().getString(R.string.No_Internet_now), 2);
		}
	}

	public void initBtnBack() {
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
		super.onBackPressed();
	}
	
	private class AsyncTaskRequestData extends AsyncTask<Void, Void, ArrayList<item_Franchise>> {

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
		protected ArrayList<item_Franchise> doInBackground(Void... params) {

			if (isCancelled()) {
				return null;
			}
			
			service_Franchise xml = new service_Franchise(Activity_Franchise.this, category_Code);
			ArrayList<item_Franchise> arItem = xml.start();

			return arItem;
		}

		@Override
		protected void onPostExecute(ArrayList<item_Franchise> arItem) {

			timer.cancel();
			EZUtil.cancelProgress();
			init_listview(arItem);

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

	void init_listview(ArrayList<item_Franchise> arItem) {

		listFranchise = (ListView) findViewById(R.id.listFranchise);
		Adapter_Franchise_Home MyAdapter = new Adapter_Franchise_Home(Activity_Franchise.this, arItem);
		listFranchise.setAdapter(MyAdapter);
		listFranchise.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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
	
	public void initTop() {

		layoutTop = (RelativeLayout) findViewById(R.id.layoutTop);
		layoutTop.setBackgroundDrawable(getResources().getDrawable(R.drawable.top_franchise_notext));

		txtTitle = (TextView) findViewById(R.id.txt_Title);
		txtTitle.setText(category_Name);
		
		layoutTop = (RelativeLayout) findViewById(R.id.layoutTop);
		
		// Temporary - Don't have service 
		if(category_Name.equals(getString(R.string.text_top_Food))) {
			
			layoutTop.setBackgroundResource(R.drawable.top_food_notext);
			
		} else if(category_Name.equals(getString(R.string.text_top_Education))) {
			
			layoutTop.setBackgroundResource(R.drawable.top_education_notext);
			
		} else if(category_Name.equals(getString(R.string.text_top_Beauty))) {
			
			layoutTop.setBackgroundResource(R.drawable.top_beauty_notext);
			
		} else if(category_Name.equals(getString(R.string.text_top_Shopping))) {
			
			layoutTop.setBackgroundResource(R.drawable.top_shopping_notext);
			
		} else if(category_Name.equals(getString(R.string.text_top_Tourism))) {
			
			layoutTop.setBackgroundResource(R.drawable.top_tourism_notext);
			
		} else if(category_Name.equals(getString(R.string.text_top_Entertainment))) {
			
			layoutTop.setBackgroundResource(R.drawable.top_entertainment_notext);
			
		} else if(category_Name.equals(getString(R.string.text_top_Health))) {
			
			layoutTop.setBackgroundResource(R.drawable.top_health_notext);
			
		}
		
	}
}
