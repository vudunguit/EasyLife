package covisoft.android.tab3_Home;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import lib.imageLoader.FileCache;
import lib.imageLoader.ScrollTextView;
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

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;

import covisoft.android.EasyLife.CheckTimeAsyncTask;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.R;
import covisoft.android.EasyLife.TabpageActivity;
import covisoft.android.EasyLife.viewFlipper_Banner;
import covisoft.android.adapter.Adapter_Shop;
import covisoft.android.item.item_Banner;
import covisoft.android.item.item_store_list;
import covisoft.android.services.service_Banner;
import covisoft.android.services.service_Shop_List;
import covisoft.android.tabhost.NavigationActivity;

/*
 * Activity show list of Shop 
 * 
 * Updated: 14/06/2013
 * Updater: Huan
 * Update Info:
 *         - Change layout_Back, layout_Top, txtName -> local variable
 *         - Delete curPos (don't use) : Adapter_Shop, Adapter_Search set value, but not use
 *         
 * Last Updated: 1.07.2013
 * Last Updater: Huan
 * Update Info:
 *        - Use EZUtil Progress
 * 
 */
public class Activity_Home_List extends NavigationActivity {

	private Activity activity;
	
	private LoadMoreListView listStore;
	private Adapter_Shop myAdapter;
	public static ArrayList<item_store_list> arItem = new ArrayList<item_store_list>();
	private ArrayList<item_store_list> tmp = new ArrayList<item_store_list>();
	
	private String category = "";
	private String categoryName = "";
	private String parentCategoryNo = "";

	public static String url = "";
	public static FileCache fileCache;
	
	private ArrayList<item_Banner> arItemBanner = new ArrayList<item_Banner>();
	
	private Timer timer;
	
	
// ======================================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_05_list_result);

		Intent intent = getIntent();
		category = intent.getStringExtra("category");
		categoryName = intent.getStringExtra("CategoryName");
		parentCategoryNo = intent.getStringExtra("parentCategoryNo");

		activity = this;
		fileCache = new FileCache(this.getApplicationContext());
		init();

	}

	void init() {
		initTop();
		init_BtnBack();
		initBtnSearch();
		
		if(EZUtil.isNetworkConnected(activity)) {
			
			AsyncTask_ListShop task = new AsyncTask_ListShop();
			task.execute();
			timer = new Timer();
			timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);
			
		} else {
			showPopupOneOption(activity.getResources().getString(R.string.No_Internet_now), 2);
		}

	}

	private void init_MapAllShop() {
		Button btn_MapAllShop = (Button) findViewById(R.id.btn_AllStore);
		btn_MapAllShop.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (arItem != null && arItem.size() > 0) {
					Intent intent = new Intent(Activity_Home_List.this,Activity_Map_All_Shop.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("category", category);
					intent.putExtra("categoryName", categoryName);
					intent.putExtra("parentCategoryNo", parentCategoryNo);
					intent.putExtra("ItemStore", arItem);
					goNextHistory("Activity_Map_All_Shop", intent);
				}
			}
		});

	}

	void init_BtnBack() {
		LinearLayout layout_Back = (LinearLayout) findViewById(R.id.layout_Back);
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
	
	/*
	 * Init and Implement Button Search
	 */ 
	public void initBtnSearch() {
		LinearLayout layout_Search = (LinearLayout) findViewById(R.id.layout_Search);
		layout_Search.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(Activity_Home_List.this,Activity_Search.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("category", category);
				goNextHistory("Activity_Search", intent);
			}
		});
	}

	/*
	 * Request list shop & Init Listview
	 */
	private class AsyncTask_ListShop extends AsyncTask<Void, Void, Void> {
		
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
			
			arItem.clear();
			
			String lat = Double.toString(Home_Activity.latitude);
			String lng = Double.toString(Home_Activity.longitude);

			service_Shop_List xml = new service_Shop_List(category, lat, lng, Activity_Home_List.this);
			arItem = xml.start();

			Collections.sort(arItem, new item_store_list.LevelComparator());

			service_Banner xmlBanner = new service_Banner(Activity_Home_List.this, "3");
			arItemBanner = xmlBanner.start();
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			timer.cancel();
			EZUtil.cancelProgress();
			init_listview();
			init_MapAllShop();
			
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


	void initBanner() {
		RelativeLayout banner = (RelativeLayout) findViewById(R.id.banner);
		viewFlipper_Banner.init(TabpageActivity.activity, banner, arItemBanner);
		
	}
	void init_listview() {

		for(int i = 0 ; i < EZUtil.LISTVIEW_NUMBER_INIT; i++) {
			if(i < arItem.size() ) {
				tmp.add(arItem.get(i));
			}
		}
		Collections.sort(tmp, new item_store_list.LevelComparator());
		
		listStore = (LoadMoreListView) findViewById(R.id.list_home_1);
		
		myAdapter = new Adapter_Shop(Activity_Home_List.this, tmp, 1, categoryName, parentCategoryNo);
		listStore.setAdapter(myAdapter);
		listStore.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		if(tmp.size() < arItem.size()) {
			listStore.getmFooterView().setVisibility(View.VISIBLE);
		} else {
			listStore.getmFooterView().setVisibility(View.GONE);
		}
		
		// set a listener to be invoked when the list reaches the end
		listStore.setOnLoadMoreListener(new OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				new LoadMoreDataTask().execute();
			}
		});
		
	}
//===============================================================================================================
	private class LoadMoreDataTask extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected Void doInBackground(Void... params) {

			if (isCancelled()) {
				return null;
			}

			// Simulates a background task
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			int nextitem = tmp.size() + EZUtil.LISTVIEW_NUMBER_MORE;
			for(int i = tmp.size(); i < nextitem; i++) {
				if(i < arItem.size()) {
	   			    tmp.add(arItem.get(i));
	   			}
	   		}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			if(tmp.size() == arItem.size()) {
				listStore.getmFooterView().setVisibility(View.GONE);
			}
			myAdapter.notifyDataSetChanged();
			listStore.onLoadMoreComplete();

			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// Notify the loading more operation has finished
			
			listStore.onLoadMoreComplete();
		}
	}
		
	public void initTop() {

		RelativeLayout layoutTop = (RelativeLayout) findViewById(R.id.layoutTop);
		ScrollTextView txt_top = (ScrollTextView) findViewById(R.id.txt_top);
		switch (Integer.parseInt(parentCategoryNo)) {
		case 4:
			layoutTop.setBackgroundResource(R.drawable.top_education_notext);
			txt_top.setText(categoryName);
			break;
		case 1:
			layoutTop.setBackgroundResource(R.drawable.top_food_notext);
			txt_top.setText(categoryName);
			break;

		case 8:
			layoutTop.setBackgroundResource(R.drawable.top_beauty_notext);
			txt_top.setText(categoryName);
			break;
		case 9:
			layoutTop.setBackgroundResource(R.drawable.top_shopping_notext);
			txt_top.setText(categoryName);
			break;
		case 5:
			layoutTop.setBackgroundResource(R.drawable.top_health_notext);
			txt_top.setText(categoryName);
			break;
		case 10:
			layoutTop.setBackgroundResource(R.drawable.top_entertainment_notext);
			txt_top.setText(categoryName);
			break;
		case 11:
			layoutTop.setBackgroundResource(R.drawable.top_tourism_notext);
			txt_top.setText(categoryName);
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
