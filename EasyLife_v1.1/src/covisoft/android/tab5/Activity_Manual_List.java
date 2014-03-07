package covisoft.android.tab5;

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
import android.widget.TextView;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.R;
import covisoft.android.adapter.Adapter_Manual;
import covisoft.android.item.item_Manual;
import covisoft.android.services.service_Manual;
import covisoft.android.tabhost.NavigationActivity;

public class Activity_Manual_List extends NavigationActivity {
	
	private Activity activity;
	private LinearLayout layout_Back;

	private ListView listManual;
	private ArrayList<item_Manual> arrManual;
	private Adapter_Manual adapter;

	private Timer timer;

// ======================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_manual_list);

		activity = this;
		
		init();
	}
	
	public void init() {
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
			service_Manual xml = new service_Manual(activity);
			
			arrManual = new ArrayList<item_Manual>();
			arrManual = xml.start();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			timer.cancel();
			EZUtil.cancelProgress();
			
			init_ListManual();
			
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
	
	
	public void init_ListManual() {
		
		listManual = (ListView)findViewById(R.id.listManual);
		adapter = new Adapter_Manual(activity, arrManual);
		listManual.setAdapter(adapter);
		listManual.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
	}

	void init_BtnBack() {
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
