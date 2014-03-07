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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import covisoft.android.EasyLife.CheckTimeAsyncTask;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.R;
import covisoft.android.EasyLife.TabpageActivity;
import covisoft.android.EasyLife.viewFlipper_Banner;
import covisoft.android.adapter.Adapter_ShopTab4;
import covisoft.android.item.item_Banner;
import covisoft.android.item.item_Category;
import covisoft.android.item.item_store_list_alba;
import covisoft.android.services.service_Banner;
import covisoft.android.services.service_Category;
import covisoft.android.services.service_RecruitShop_List;
import covisoft.android.tab3_Home.Activity_Search;
import covisoft.android.tab3_Home.Home_Activity;
import covisoft.android.tabhost.NavigationActivity;

public class Activity_Store_List extends NavigationActivity {

	private Activity activity = null;
	private RelativeLayout layoutTop;
	private Button btn_AllStore;
	private TextView txt_top;

	private LinearLayout layout_Back;
	private LinearLayout layout_Search;

	private ArrayList<item_store_list_alba> arItem_store = new ArrayList<item_store_list_alba>();
	private ArrayList<item_store_list_alba> arItem_store_tmp = new ArrayList<item_store_list_alba>();
	private ArrayList<item_Category> aritem_Category = new ArrayList<item_Category>();
	
	private ArrayList<item_Banner> arItemBanner = new ArrayList<item_Banner>();
	
	private String category = "";

	private Timer timer;
	
// ======================================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_05_list_result_tab4);

		Intent intent = getIntent();
		category = intent.getStringExtra("category");

		activity = this;

		init();
	}

	void init() {
		initBtnSearch();
		initBtnBack();
		initTop();
		
		btn_AllStore = (Button) findViewById(R.id.btn_AllStore);
		btn_AllStore.setVisibility(View.GONE);
		
		if(EZUtil.isNetworkConnected(activity)) {
			AsyncTaskRequestData task = new AsyncTaskRequestData();
			task.execute();
			timer = new Timer();
			timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);
		} else {
			showPopupOneOption(activity.getResources().getString(R.string.No_Internet_now), 2);
		}
	}

	void initBanner() {
		RelativeLayout banner = (RelativeLayout) findViewById(R.id.banner);
		viewFlipper_Banner.init(TabpageActivity.activity, banner, arItemBanner);
		
	}

	void initBtnBack() {
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
	public void initBtnSearch() {
		layout_Search = (LinearLayout) findViewById(R.id.layout_Search);
		layout_Search.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(Activity_Store_List.this, Activity_Search.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("category", category);
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
			String lat = Double.toString(Home_Activity.latitude);
			String lng = Double.toString(Home_Activity.longitude);

			service_Category xml_Category = new service_Category(category, Activity_Store_List.this);
			aritem_Category = xml_Category.start();

			for (int i = 0; i < aritem_Category.size(); i++) {
				service_RecruitShop_List xml = new service_RecruitShop_List(aritem_Category.get(i).no, lat, lng, Activity_Store_List.this);
				arItem_store_tmp = xml.start();

				for (int j = 0; j < arItem_store_tmp.size(); j++) {
					arItem_store.add(arItem_store_tmp.get(j));
				}
			}
			
			service_Banner xmlBanner = new service_Banner(Activity_Store_List.this, "2");
			arItemBanner = xmlBanner.start();
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			timer.cancel();
			
			EZUtil.cancelProgress();
			
			init_listview();
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
			}, 500);
		}
	}

	void init_listview() {
		final Adapter_ShopTab4 MyAdapter = new Adapter_ShopTab4(Activity_Store_List.this, arItem_store);
		MyAdapter.categoryName = category;
		ListView listview;

		listview = (ListView) findViewById(R.id.list_home_1);
		listview.setAdapter(MyAdapter);
		listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listview.setDividerHeight(1);
	}

	public void initTop() {

		layoutTop = (RelativeLayout) findViewById(R.id.layoutTop);
		txt_top = (TextView) findViewById(R.id.txt_top);
		switch (Integer.parseInt(category)) {
		case 4:
			layoutTop.setBackgroundResource(R.drawable.top_education_notext);
			txt_top.setText(getString(R.string.text_top_Education));
			break;
		case 1:
			layoutTop.setBackgroundResource(R.drawable.top_food_notext);
			txt_top.setText(getString(R.string.text_top_Food));
			break;

		case 8:
			layoutTop.setBackgroundResource(R.drawable.top_beauty_notext);
			txt_top.setText(getString(R.string.text_top_Beauty));
			break;
		case 9:
			layoutTop.setBackgroundResource(R.drawable.top_shopping_notext);
			txt_top.setText(getString(R.string.text_top_Shopping));
			break;
		case 5:
			layoutTop.setBackgroundResource(R.drawable.top_health_notext);
			txt_top.setText(getString(R.string.text_top_Health));
			break;
		case 10:
			layoutTop.setBackgroundResource(R.drawable.top_entertainment_notext);
			txt_top.setText(getString(R.string.text_top_Entertainment));
			break;
		case 11:
			layoutTop.setBackgroundResource(R.drawable.top_tourism_notext);
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
