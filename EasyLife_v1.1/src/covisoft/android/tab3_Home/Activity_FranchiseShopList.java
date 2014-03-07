package covisoft.android.tab3_Home;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import lib.imageLoader.ImageLoader_Rounded;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import covisoft.android.EasyLife.CheckTimeAsyncTask;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.R;
import covisoft.android.adapter.Adapter_Franchise_Shop_List;
import covisoft.android.item.item_Franchise_Shop;
import covisoft.android.services.service_Franshise_Shop;
import covisoft.android.tabhost.NavigationActivity;

/*
 * Activity show Franshise's shop
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
public class Activity_FranchiseShopList extends NavigationActivity {

	private NavigationActivity activity;
	private LinearLayout layout_Back;
	
	private RelativeLayout layoutTop;
	private Button btn_MapAllShop;
	
	private ListView listView;
	private Adapter_Franchise_Shop_List myAdapter;
	private ArrayList<item_Franchise_Shop> arItem  = new ArrayList<item_Franchise_Shop>();
	
	private TextView txtName;
	
	private String franchiseName = "";
	private String type = "";
	private String franchiseNo = "";
	private String s_logo = "";
	private Boolean expand = false;
	
	private Timer timer;
	
//================================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_19_franchise_1);

		activity = this;

		Intent intent = getIntent();
		franchiseName = intent.getStringExtra("franchiseName");
		franchiseNo = intent.getStringExtra("franchiseNo");
		s_logo = intent.getStringExtra("s_logo");
		type = intent.getStringExtra("type");

		init();

	}

	public void init() {

		initTop();
		initBtnBack();
		
		if(EZUtil.isNetworkConnected(activity)) {
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
			
			service_Franshise_Shop xml = new service_Franshise_Shop(franchiseNo, Activity_FranchiseShopList.this);
			arItem = xml.start();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			timer.cancel();
			EZUtil.cancelProgress();
			init_listview();
			init_BtnMap();
			
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

	public void init_listview() {

		listView = (ListView) findViewById(R.id.listFranchiseShop);
		myAdapter = new Adapter_Franchise_Shop_List(Activity_FranchiseShopList.this, arItem, franchiseName, type);
		
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View header = inflater.inflate(R.layout.listview_item_franchise_header, listView, false);
		
		ImageView imageview = (ImageView) header.findViewById(R.id.img_franchise);

		ImageLoader_Rounded imageLoader = new ImageLoader_Rounded(activity.getApplicationContext());
		imageLoader.DisplayImage(s_logo, imageview, R.drawable.s_19_noimage, EZUtil.REQUIRED_SIZE_MIDDLE);
		
		Button btnAll = (Button)header.findViewById(R.id.btnAllCoupon);
		btnAll.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Log.e("expand", expand + "");

				int size = arItem.size();
				int checkExpand = 0;
				int checkNotExpand = 0;

				for (int i = 0; i < arItem.size(); i++) {
					if (arItem.get(i).getFlag()) {
						checkExpand++;
					}
				}
				for (int i = 0; i < arItem.size(); i++) {
					if (!arItem.get(i).getFlag()) {
						checkNotExpand++;
					}
				}

				if (size == checkExpand) {
					for (int i = 0; i < arItem.size(); i++) {
						arItem.get(i).setFlag(false);
					}
					expand = false;
				} else if (size == checkNotExpand) {
					for (int i = 0; i < arItem.size(); i++) {
						arItem.get(i).setFlag(true);
					}

					expand = true;
				} else {
					if (expand == false) {
						for (int i = 0; i < arItem.size(); i++) {
							arItem.get(i).setFlag(true);
						}

						expand = true;
					} else {
						for (int i = 0; i < arItem.size(); i++) {
							arItem.get(i).setFlag(false);
						}
						expand = false;
					}
				}

				myAdapter.notifyDataSetChanged();
			}
		});
		
		listView.addHeaderView(header);
		listView.setAdapter(myAdapter);
		
	}

	public void init_BtnMap() {
		btn_MapAllShop = (Button) findViewById(R.id.btn_MapAllShop);
		btn_MapAllShop.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (arItem != null && arItem.size() > 0) {
					Intent intent = new Intent(Activity_FranchiseShopList.this,	Activity_Map_All_Franchise.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("ItemFranchise", arItem);
					goNextHistory("Activity_Map_All_Franchise", intent);
				}
			}
		});
	}

	public void initTop() {

		txtName = (TextView) findViewById(R.id.txtName);
		txtName.setText(franchiseName);
		
		layoutTop = (RelativeLayout) findViewById(R.id.layoutTop);
		switch (Integer.parseInt(type)) {
		case 4:
			layoutTop.setBackgroundResource(R.drawable.top_education_notext);
			break;
		case 1:
			layoutTop.setBackgroundResource(R.drawable.top_food_notext);
			break;
		case 8:
			layoutTop.setBackgroundResource(R.drawable.top_beauty_notext);
			break;
		case 9:
			layoutTop.setBackgroundResource(R.drawable.top_shopping_notext);
			break;
		case 5:
			layoutTop.setBackgroundResource(R.drawable.top_health_notext);
			break;
		case 10:
			layoutTop.setBackgroundResource(R.drawable.top_entertainment_notext);
			break;
		case 11:
			layoutTop.setBackgroundResource(R.drawable.top_tourism_notext);
			break;

		default:
			break;
		}
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
