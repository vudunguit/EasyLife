package covisoft.android.tab3_Home;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
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
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.R;
import covisoft.android.EasyLife.TabpageActivity;
import covisoft.android.EasyLife.viewFlipper_Banner;
import covisoft.android.adapter.Adapter_Notic;
import covisoft.android.item.item_Promotion;
import covisoft.android.item.item_Banner;
import covisoft.android.services.service_Banner;
import covisoft.android.services.service_PromotionList;
import covisoft.android.tabhost.NavigationActivity;

public class Activity_Notic_List extends NavigationActivity{

	private Activity activity;
	
	private LinearLayout layout_Back;
	
	private ArrayList<item_Promotion> arBoard;
	
	private ArrayList<item_Banner> arItemBanner = new ArrayList<item_Banner>();
	
	private Button btnVoucher;
	private Button btnEvent;
	
	private ListView listVoucher;  
	private Adapter_Notic adapVoucher;
	
	private ListView listEvent;  
	private Adapter_Notic adapEvent;
	
//	private ListView list_Notic;  
//	private Adapter_Notic adapter;
	
	private String cur_Kind = "Voucher";
	
	private Timer timer;
	
// ====================================================================================================
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_notic_list);
		
		activity = this;
		
		init_BtnBack();
		
		if(EZUtil.isNetworkConnected(activity)) {
			AsyncTaskRequestData task = new AsyncTaskRequestData();
			task.execute();
			timer = new Timer();
			timer.schedule(new CheckTimeTask(task), EZUtil.DelayTime);
		} else {
			showPopupOneOption(activity.getResources().getString(R.string.No_Internet_now), 2);
		}
		
		
	}
	
	void initBanner() {
		RelativeLayout banner = (RelativeLayout) findViewById(R.id.banner);
		viewFlipper_Banner.init(TabpageActivity.activity, banner, arItemBanner);
		
	}
	
	void init_View() {
		btnVoucher = (Button)findViewById(R.id.btnVoucher);
		btnEvent = (Button)findViewById(R.id.btnEvent);
		listVoucher = (ListView)findViewById(R.id.listVoucher);
		listEvent = (ListView)findViewById(R.id.listEvent);
		
		btnVoucher.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cur_Kind = "Voucher";
				// TODO Auto-generated method stub
				btnVoucher.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_green_border_clicked));
				btnEvent.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_green_border));
				
				listVoucher.setVisibility(View.VISIBLE);
				listEvent.setVisibility(View.GONE);
			}
		});
		
		btnEvent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cur_Kind = "Event";
				btnEvent.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_green_border_clicked));
				btnVoucher.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_green_border));
				
				listEvent.setVisibility(View.VISIBLE);
				listVoucher.setVisibility(View.GONE);
			}
		});
		
		if(cur_Kind.equals("Event")) {
			btnEvent.performClick();
		} else {
			btnVoucher.performClick();
		}
	}
	
	
	public void init_ListView() {
//		list_Notic = (ListView)findViewById(R.id.list_Notic);
//		adapter = new Adapter_Notic(Activity_Notic_List.this, arBoard);
//		list_Notic.setAdapter(adapter);
		
		ArrayList<item_Promotion> arVoucher = new ArrayList<item_Promotion>();
		ArrayList<item_Promotion> arEvent = new ArrayList<item_Promotion>();
		for(int i = 0 ; i < arBoard.size(); i++) {
			if(arBoard.get(i).getKind() == 1 || arBoard.get(i).getKind() == 3) {
				arVoucher.add(arBoard.get(i));
			} else {
				arEvent.add(arBoard.get(i));
			}
		}
		
		adapVoucher = new Adapter_Notic(Activity_Notic_List.this, arVoucher);
		listVoucher.setAdapter(adapVoucher);
		
		adapEvent = new Adapter_Notic(Activity_Notic_List.this, arEvent);
		listEvent.setAdapter(adapEvent);
	}
	public void init_BtnBack() {
		layout_Back = (LinearLayout)findViewById(R.id.layout_Back);
		layout_Back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
			service_PromotionList xmlBoard = new service_PromotionList();
			xmlBoard.act = Activity_Notic_List.this;
			xmlBoard.init(Activity_Notic_List.this);
			
			arBoard = new ArrayList<item_Promotion>();
			arBoard = xmlBoard.start();

			service_Banner xmlBanner = new service_Banner(Activity_Notic_List.this, "3");
			arItemBanner = xmlBanner.start();
			
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			timer.cancel();
			EZUtil.cancelProgress();
			
			init_View();
			init_ListView();
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

	class CheckTimeTask extends TimerTask {
		
		AsyncTaskRequestData async;
		
	    public CheckTimeTask(AsyncTaskRequestData async) {
			super();
			this.async = async;
		}
	
		@Override
	    public void run() {
	         
			 
	         activity.runOnUiThread(new Runnable() {
	             @Override
	             public void run() {
	            	 
	            	 async.cancel(true);
	             }
	         });
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
