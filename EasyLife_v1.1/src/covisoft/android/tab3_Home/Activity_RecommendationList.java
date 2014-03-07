package covisoft.android.tab3_Home;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;

import covisoft.android.EasyLife.CheckTimeAsyncTask;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.R;
import covisoft.android.EasyLife.TabpageActivity;
import covisoft.android.adapter.Adapter_Recommandation_List;
import covisoft.android.item.item_Recommandation;
import covisoft.android.services.service_Recommendation_Hot;
import covisoft.android.services.service_Recommendation_Near;
import covisoft.android.services.service_Recommendation_New;
import covisoft.android.tabhost.NavigationActivity;

public class Activity_RecommendationList extends NavigationActivity {

	private LinearLayout layout_Back;
	private NavigationActivity activity;

	private ToggleButton btnListHot;
	private ToggleButton btnListNew;
	private ToggleButton btnListNear;

	private LoadMoreListView listHot;
	private LoadMoreListView listNew;
	private LoadMoreListView listNear;

	private Adapter_Recommandation_List adapter_Hot;
	private Adapter_Recommandation_List adapter_New;
	private Adapter_Recommandation_List adapter_Near;

	private ArrayList<item_Recommandation> arItem_Hot = new ArrayList<item_Recommandation>();
	private ArrayList<item_Recommandation> arValue_Hot = new ArrayList<item_Recommandation>();
	
	private ArrayList<item_Recommandation> arItem_New = new ArrayList<item_Recommandation>();
	private ArrayList<item_Recommandation> arValue_New = new ArrayList<item_Recommandation>();
	
	private ArrayList<item_Recommandation> arItem_Near = new ArrayList<item_Recommandation>();
	private ArrayList<item_Recommandation> arValue_Near = new ArrayList<item_Recommandation>();

	private Timer timer;
	
	private String type = "HOT";
	
// ======================================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_recommandation_list);

		activity = this;

		init();
	}

	public void init() {

		initBtnBack();
		init_Toogle();
	}


	public void init_Toogle() {
		listHot = (LoadMoreListView) findViewById(R.id.listHot);
		listNew = (LoadMoreListView) findViewById(R.id.listNew);
		listNear = (LoadMoreListView) findViewById(R.id.listNear);

		btnListNew = (ToggleButton) findViewById(R.id.btnListNew);
		btnListNew.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if (btnListNew.isChecked()) {
					
					type = "NEW";
					
					if(adapter_New != null) {
						arItem_New.clear();
						arValue_New.clear();
						adapter_New.notifyDataSetChanged();
					}
					
					btnListNew.setChecked(true);
					btnListHot.setChecked(false);
					btnListNear.setChecked(false);

					btnListNew.setEnabled(false);
					btnListHot.setEnabled(true);
					btnListNear.setEnabled(true);

					listNew.setVisibility(View.VISIBLE);
					listHot.setVisibility(View.GONE);
					listNear.setVisibility(View.GONE);
					
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

		btnListHot = (ToggleButton) findViewById(R.id.btnListHot);
		btnListHot.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (btnListHot.isChecked()) {

					type = "HOT";
					
					if(adapter_Hot != null) {
						arItem_Hot.clear();
						arValue_Hot.clear();
						adapter_Hot.notifyDataSetChanged();
					}
					
					btnListHot.setChecked(true);
					btnListNew.setChecked(false);
					btnListNear.setChecked(false);

					btnListHot.setEnabled(false);
					btnListNew.setEnabled(true);
					btnListNear.setEnabled(true);

					listHot.setVisibility(View.VISIBLE);
					listNew.setVisibility(View.GONE);
					listNear.setVisibility(View.GONE);
					
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
		
		btnListNear = (ToggleButton) findViewById(R.id.btnListNear);
		btnListNear.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (btnListNear.isChecked()) {

					type = "NEAR";
					
					if(adapter_Near != null) {
						arItem_Near.clear();
						arValue_Near.clear();
						adapter_Near.notifyDataSetChanged();
					}
					
					btnListNear.setChecked(true);
					btnListNew.setChecked(false);
					btnListHot.setChecked(false);

					btnListNear.setEnabled(false);
					btnListNew.setEnabled(true);
					btnListHot.setEnabled(true);

					listNear.setVisibility(View.VISIBLE);
					listNew.setVisibility(View.GONE);
					listHot.setVisibility(View.GONE);
					
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
		
		btnListHot.performClick();
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
			
			if(type.equals("HOT")) {
				service_Recommendation_Hot xml_listVip = new service_Recommendation_Hot(activity);
				arItem_Hot = xml_listVip.start();
			} else if(type.equals("NEW")) {
				service_Recommendation_New xml_listNew = new service_Recommendation_New(activity);
				arItem_New = xml_listNew.start();
			} else if(type.equals("NEAR")) {
				String lat = Double.toString(Home_Activity.latitude);
				String lng = Double.toString(Home_Activity.longitude);
				if (Double.compare(new Double(0),Home_Activity.latitude)!=0
						&&Double.compare(new Double(0),Home_Activity.longitude)!=0)
				{
					service_Recommendation_Near xml_listHot = new service_Recommendation_Near(activity, lat, lng);
					arItem_Near = xml_listHot.start();
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			timer.cancel();
			
			EZUtil.cancelProgress();
			
			if(type.equals("HOT")) {
				init_ListHOT();
			} else if(type.equals("NEW")) {
				init_ListNEW();
			} else if(type.equals("NEAR")) {
				init_ListNEAR();
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
	
	
	public void init_ListHOT() {
		Log.e("arItem_HOT", arItem_Hot.size() + "");
		for(int i = 0 ; i < EZUtil.LISTVIEW_NUMBER_INIT; i++) {
			if(i < arItem_Hot.size() ) {
				arValue_Hot.add(arItem_Hot.get(i));
			}
		}
		
		adapter_Hot = new Adapter_Recommandation_List(activity, arValue_Hot);
		listHot.setAdapter(adapter_Hot);
		
		if(arValue_Hot.size() < arValue_Hot.size()) {
			listHot.getmFooterView().setVisibility(View.VISIBLE);
		} else {
			listHot.getmFooterView().setVisibility(View.GONE);
		}
		
		// set a listener to be invoked when the list reaches the end
		listHot.setOnLoadMoreListener(new OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				new LoadMoreDataTask().execute();
			}
		});
		
	}
	

	public void init_ListNEW() {
		
		Log.e("arItem_NEW", arItem_New.size() + "");
		for(int i = 0 ; i < EZUtil.LISTVIEW_NUMBER_INIT; i++) {
			if(i < arItem_New.size() ) {
				arValue_New.add(arItem_New.get(i));
			}
		}
		
		adapter_New = new Adapter_Recommandation_List(activity, arValue_New);
		listNew.setAdapter(adapter_New);
		
		if(arValue_New.size() < arItem_New.size()) {
			listNew.getmFooterView().setVisibility(View.VISIBLE);
		} else {
			listNew.getmFooterView().setVisibility(View.GONE);
		}
		
		// set a listener to be invoked when the list reaches the end
		listNew.setOnLoadMoreListener(new OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				new LoadMoreDataTask().execute();
			}
		});
		
	}
	public void init_ListNEAR() {
		
		Log.e("arItem_NEAR", arItem_Near.size() + "");
		for(int i = 0 ; i < EZUtil.LISTVIEW_NUMBER_INIT; i++) {
			if(i < arItem_Near.size() ) {
				arValue_Near.add(arItem_Near.get(i));
			}
		}
		
		adapter_Near = new Adapter_Recommandation_List(activity, arValue_Near);
		listNear.setAdapter(adapter_Near);
		
		if(arValue_Near.size() < arItem_Near.size()) {
			listNear.getmFooterView().setVisibility(View.VISIBLE);
		} else {
			listNear.getmFooterView().setVisibility(View.GONE);
		}
		
		// set a listener to be invoked when the list reaches the end
		listNear.setOnLoadMoreListener(new OnLoadMoreListener() {
			
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
			
			if(type.equals("HOT")) {
				int nextitem = arValue_Hot.size() + EZUtil.LISTVIEW_NUMBER_MORE;
				for(int i = arValue_Hot.size(); i < nextitem; i++) {
					if(i < arItem_Hot.size()) {
						arValue_Hot.add(arItem_Hot.get(i));
		   			}
		   		}
			} else if(type.equals("NEW")) {
				int nextitem = arValue_New.size() + EZUtil.LISTVIEW_NUMBER_MORE;
				for(int i = arValue_New.size(); i < nextitem; i++) {
					if(i < arItem_New.size()) {
						arValue_New.add(arItem_New.get(i));
		   			}
		   		}
			} else if(type.equals("NEAR")) {
				int nextitem = arValue_Near.size() + EZUtil.LISTVIEW_NUMBER_MORE;
				for(int i = arValue_Near.size(); i < nextitem; i++) {
					if(i < arItem_Near.size()) {
						arValue_Near.add(arItem_Near.get(i));
		   			}
		   		}
			}
			
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			

			if(type.equals("HOT")) {
				if(arValue_Hot.size() == arItem_Hot.size()) {
					listHot.getmFooterView().setVisibility(View.GONE);
				}
				adapter_Hot.notifyDataSetChanged();
				listHot.onLoadMoreComplete();
			} else if(type.equals("NEW")) {
				if(arValue_New.size() == arItem_New.size()) {
					listNew.getmFooterView().setVisibility(View.GONE);
				}
				adapter_New.notifyDataSetChanged();
				listNew.onLoadMoreComplete();
			} else if(type.equals("NEAR")) {
				if(arValue_Near.size() == arItem_Near.size()) {
					listNear.getmFooterView().setVisibility(View.GONE);
				}
				adapter_Near.notifyDataSetChanged();
				listNear.onLoadMoreComplete();
			}
			

			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// Notify the loading more operation has finished
			
			if(type.equals("HOT")) {
				listHot.onLoadMoreComplete();
			} else if(type.equals("NEW")) {
				listNew.onLoadMoreComplete();
			} else if(type.equals("NEAR")) {
				listNear.onLoadMoreComplete();
			}
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
		
		TabpageActivity.btnThree.setVisibility(View.GONE);
		TabpageActivity.btnSearch.setVisibility(View.VISIBLE);
		super.onBackPressed();
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
