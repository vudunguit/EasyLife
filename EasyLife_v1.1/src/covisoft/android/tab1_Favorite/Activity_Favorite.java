package covisoft.android.tab1_Favorite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import covisoft.android.EasyLife.EasyLifeActivity;
import covisoft.android.EasyLife.R;
import covisoft.android.EasyLife.TabpageActivity;
import covisoft.android.EasyLife.viewFlipper_Banner;
import covisoft.android.adapter.Adapter_Favorite;
import covisoft.android.adapter.Adapter_Favorite_Job;
import covisoft.android.item.item_Banner;
import covisoft.android.item.item_store_list_alba;
import covisoft.android.item.item_store_list_fav;
import covisoft.android.services.service_Banner;
import covisoft.android.services.service_Favorite_List;
import covisoft.android.services.service_User_Info;
import covisoft.android.tab3_Home.Activity_Search;
import covisoft.android.tabhost.NavigationActivity;


/*
 * Activity List Favourite, first page in Tab 1
 * last Updated: 12/06/2013
 * last Updater: Huan
 * Update Info: 
 *         - Follow Adapter_Favourite_Job
 */

public class Activity_Favorite extends NavigationActivity {

	private Activity activity;
	
	private ArrayList<item_Banner> arItemBanner = new ArrayList<item_Banner>();
	
	private String cur_Kind = "Shop";
	
	// Apply for list shop
	private Button btn_Shop;
	private Adapter_Favorite adapter;
	private LoadMoreListView listFavorite_Shop;
	private ArrayList<item_store_list_fav> arItem_Normal = new ArrayList<item_store_list_fav>();
	private ArrayList<item_store_list_fav> arValue_Normal = new ArrayList<item_store_list_fav>();

	// Apply for list job
	private Button btn_Job;
	private ListView listFavorite_Job;
	private Adapter_Favorite_Job job_adapter;
	private ArrayList<item_store_list_alba> arItem_Job = new ArrayList<item_store_list_alba>();
	
	private Timer timer;

// ======================================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_11_like);

		activity = this;

		init();

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (EasyLifeActivity.user != null) {
			
			if(cur_Kind.equals("Job")) {
				if (job_adapter != null) {
					arItem_Job.clear();
					job_adapter.notifyDataSetChanged();
				}
				btn_Job.performClick();
			} else {
				if (adapter != null) {
					arItem_Normal.clear();
					adapter.notifyDataSetChanged();
				}
				btn_Shop.performClick();
			}
		} else {
			Intent intent = new Intent(Activity_Favorite.this, Activity_Login_Tab1.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			goNextHistory_2("Activity_Login_Tab1", intent);
		}
	}

	public void init() {
		initBtnTop();

		listFavorite_Shop = (LoadMoreListView) findViewById(R.id.listFavorite);
		listFavorite_Job = (ListView) findViewById(R.id.listFavorite_Job);

		init_Toogle();

	}

	public void initBtnTop() {
		LinearLayout layout_Back = (LinearLayout) findViewById(R.id.layout_Back);
		layout_Back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				onBackPressed();
			}
		});

		// Gone this because doesn't use
		LinearLayout layout_Search = (LinearLayout) findViewById(R.id.layout_Search);
		layout_Search.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(Activity_Favorite.this, Activity_Search.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				goNextHistory("Activity_Search", intent);
			}
		});
		layout_Search.setVisibility(View.GONE);
	}

	public void init_Toogle() {

		btn_Shop = (Button) findViewById(R.id.btn_Shop);
		btn_Shop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!EZUtil.isLoading) {

					cur_Kind = "Shop";

					if (adapter != null) {
						arItem_Normal.clear();
						arValue_Normal.clear();
						adapter.notifyDataSetChanged();
					}

					btn_Shop.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_green_border_clicked));
					btn_Job.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_green_border));

					if (EasyLifeActivity.user != null) {

						if (EZUtil.isNetworkConnected(activity)) {
							AsyncTaskRequestData task = new AsyncTaskRequestData();
							task.execute();
							timer = new Timer();
							timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);
						} else {
							showPopupOneOption(activity.getResources().getString(R.string.No_Internet_now), 2);
						}

					} else {
						Intent intent = new Intent(Activity_Favorite.this, Activity_Login_Tab1.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
						goNextHistory_2("Activity_Login_Tab1", intent);
					}
				}
			}
		});
		btn_Job = (Button) findViewById(R.id.btn_Job);
		btn_Job.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!EZUtil.isLoading) {
					cur_Kind = "Job";

					if (job_adapter != null) {
						arItem_Job.clear();
						job_adapter.notifyDataSetChanged();
					}

					btn_Job.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_green_border_clicked));
					btn_Shop.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_green_border));

					if (EasyLifeActivity.user != null) {

						if (EZUtil.isNetworkConnected(activity)) {
							AsyncTaskRequestData task = new AsyncTaskRequestData();
							task.execute();
							timer = new Timer();
							timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);
						} else {
							showPopupOneOption(activity.getResources().getString(R.string.No_Internet_now), 2);
						}

					} else {
						Intent intent = new Intent(Activity_Favorite.this, Activity_Login_Tab1.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
						intent.putExtra("no", "Tab1");
						goNextHistory_2("Activity_Login_Tab1", intent);
					}

				}

			}
		});

	}

	
//	====================================================================================================================

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

			service_Banner xmlbanner = new service_Banner(Activity_Favorite.this, "3");
			arItemBanner = xmlbanner.start();
			
			if(EasyLifeActivity.user.getEmail() == null) {
				service_User_Info xml_UserInfo = new service_User_Info(activity, EasyLifeActivity.user.getUsername());
				xml_UserInfo.start();
			}
			
			if (cur_Kind.equals("Shop")) {
				
				service_Favorite_List xml = new service_Favorite_List(EasyLifeActivity.user.getUsername(), Activity_Favorite.this);
				arItem_Normal = xml.start_Normal();
				
				Collections.sort(arItem_Normal, new item_store_list_fav.DistanceComparator());
				
			} else if (cur_Kind.equals("Job")) {
				
				service_Favorite_List xml = new service_Favorite_List(EasyLifeActivity.user.getUsername(), Activity_Favorite.this);
				arItem_Job = xml.start_Job();

				Collections.sort(arItem_Job, new item_store_list_alba.DistanceComparator());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			timer.cancel();

			EZUtil.cancelProgress();
			RelativeLayout banner = (RelativeLayout) findViewById(R.id.banner);
			viewFlipper_Banner.init(TabpageActivity.activity, banner, arItemBanner);
			
			if (cur_Kind.equals("Shop")) {
				initListFavorite_Normal();
				listFavorite_Shop.setVisibility(View.VISIBLE);
				listFavorite_Job.setVisibility(View.GONE);

				listFavorite_Shop.invalidate();
				listFavorite_Shop.requestLayout();
				
				if (arItem_Normal.size() == 0) {
					Timer t = new Timer();
					t.schedule(new TimerTask() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							activity.runOnUiThread(new Runnable() {
								  public void run() {
									  showPopupOneOption(activity.getResources().getString(R.string.popup_Favourite_NoAnyFav), 1);
								  }
							});
						}
					}, 300);
				}
			} else if (cur_Kind.equals("Job")) {
				initListFavorite_Job();
				listFavorite_Job.setVisibility(View.VISIBLE);
				listFavorite_Shop.setVisibility(View.GONE);

				listFavorite_Job.invalidate();
				listFavorite_Job.requestLayout();
				
				if (arItem_Job.size() == 0) {
					Timer t = new Timer();
					t.schedule(new TimerTask() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							activity.runOnUiThread(new Runnable() {
								  public void run() {
									  showPopupOneOption(activity.getResources().getString(R.string.popup_Favourite_NoAnyFav), 1);
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
	
//	==============================================================================================
	public void initListFavorite_Normal() {

		for (int i = 0; i < EZUtil.LISTVIEW_NUMBER_INIT; i++) {
			if (i < arItem_Normal.size()) {
				arValue_Normal.add(arItem_Normal.get(i));
			}
		}

		adapter = new Adapter_Favorite(Activity_Favorite.this, arValue_Normal);
		listFavorite_Shop.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		if (arValue_Normal.size() < arItem_Normal.size()) {
			listFavorite_Shop.getmFooterView().setVisibility(View.VISIBLE);
		} else {
			listFavorite_Shop.getmFooterView().setVisibility(View.GONE);
		}

		// set a listener to be invoked when the list reaches the end
		listFavorite_Shop.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				new LoadMoreDataTask().execute();
			}
		});

	}

	public void initListFavorite_Job() {

		job_adapter = new Adapter_Favorite_Job(Activity_Favorite.this, arItem_Job);
		listFavorite_Job.setAdapter(job_adapter);
		job_adapter.notifyDataSetChanged();

	}
	
// ===============================================================================================================
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
			int nextitem = arValue_Normal.size() + EZUtil.LISTVIEW_NUMBER_MORE;
			for (int i = arValue_Normal.size(); i < nextitem; i++) {
				if (i < arItem_Normal.size()) {
					arValue_Normal.add(arItem_Normal.get(i));
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			if (arValue_Normal.size() >= arItem_Normal.size()) {
				Log.e("Hide progress", "");
				listFavorite_Shop.getmFooterView().setVisibility(View.GONE);
			}
			adapter.notifyDataSetChanged();
			listFavorite_Shop.onLoadMoreComplete();

			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// Notify the loading more operation has finished

			listFavorite_Shop.onLoadMoreComplete();
		}
	}

// ===============================================================================================================
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

}
