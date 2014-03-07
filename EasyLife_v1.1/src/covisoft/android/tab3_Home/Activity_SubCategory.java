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
import covisoft.android.adapter.Adapter_SubCategory;
import covisoft.android.item.item_Category;
import covisoft.android.services.service_Category;
import covisoft.android.tabhost.NavigationActivity;

/*
 * Activity Sub Category - List sub category follow main category
 * 
 * Last Updated: 1.07.2013
 * Last Updater: Huan
 * Update Info:
 *        - Use EZUtil Progress 
 */
public class Activity_SubCategory extends NavigationActivity {

	private Activity activity;
	private String category = "";

	private LinearLayout layout_Back;
	private RelativeLayout layoutTop;
	private RelativeLayout layout_top;
	private TextView txt_top;
	
	private ArrayList<item_Category> arItem = new ArrayList<item_Category>();

	private Timer timer;
	
// ==========================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_16_subcategory);

		activity = this;

		Intent intent = getIntent();
		category = intent.getStringExtra("category");

		init();

	}

	void init() {
		initBtnBack();
		initTop();
		
		if(EZUtil.isNetworkConnected(activity)) {
			
			AsyncTaskRequestData task = new AsyncTaskRequestData();
			task.execute();
			timer = new Timer();
			timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);
			
		} else {
			showPopupOneOption(activity.getResources().getString(R.string.No_Internet_now), 2);
		}
		
	}

	void initBtnBack() {
		layout_Back = (LinearLayout) findViewById(R.id.layout_Back);
		layout_Back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				onBackPressed();
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
			service_Category xml = new service_Category(category, Activity_SubCategory.this);
			arItem = xml.start();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			timer.cancel();
			
			EZUtil.cancelProgress();
			
			init_listview();
			
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


	void init_listview() {
		Adapter_SubCategory MyAdapter = new Adapter_SubCategory(Activity_SubCategory.this, arItem, category);
		ListView listview;
		listview = (ListView) findViewById(R.id.listCategory);
		listview.setAdapter(MyAdapter);
		listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listview.setDividerHeight(1);
	}

	public void initTop() {

		layoutTop = (RelativeLayout) findViewById(R.id.layoutTop);
		layout_top = (RelativeLayout) findViewById(R.id.layout_top);
		txt_top = (TextView) findViewById(R.id.txt_top);
		switch (Integer.parseInt(category)) {
		case 4:
			layoutTop.setBackgroundResource(R.drawable.top_education_notext);
			layout_top.setBackgroundResource(R.drawable.top_education_notext);
			txt_top.setText(getString(R.string.text_top_Education));
			break;
		case 1:
			layoutTop.setBackgroundResource(R.drawable.top_food_notext);
			layout_top.setBackgroundResource(R.drawable.top_food_notext);
			txt_top.setText(getString(R.string.text_top_Food));
			break;

		case 8:
			layoutTop.setBackgroundResource(R.drawable.top_beauty_notext);
			layout_top.setBackgroundResource(R.drawable.top_beauty_notext);
			txt_top.setText(getString(R.string.text_top_Beauty));
			break;
		case 9:
			layoutTop.setBackgroundResource(R.drawable.top_shopping_notext);
			layout_top.setBackgroundResource(R.drawable.top_shopping_notext);
			txt_top.setText(getString(R.string.text_top_Shopping));
			break;
		case 5:
			layoutTop.setBackgroundResource(R.drawable.top_health_notext);
			layout_top.setBackgroundResource(R.drawable.top_health_notext);
			txt_top.setText(getString(R.string.text_top_Health));

			break;
		case 10:
			layoutTop.setBackgroundResource(R.drawable.top_entertainment_notext);
			layout_top.setBackgroundResource(R.drawable.top_entertainment_notext);
			txt_top.setText(getString(R.string.text_top_Entertainment));
			break;
		case 11:
			layoutTop.setBackgroundResource(R.drawable.top_tourism_notext);
			layout_top.setBackgroundResource(R.drawable.top_tourism_notext);
			txt_top.setText(getString(R.string.text_top_Tourism));
			break;

		default:
			break;
		}
	}

	public void showPopupOneOption(String content, int type) {
		
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
			if(type == 1) {
				btn_OK.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						dialog.dismiss();
					}
				});
			} else if(type == 2) {
				btn_OK.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						dialog.dismiss();
						onBackPressed();
					}
				});
			}
			

			dialog.getWindow().setLayout(500, 400);
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
	
}
