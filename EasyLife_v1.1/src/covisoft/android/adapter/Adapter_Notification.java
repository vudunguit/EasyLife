package covisoft.android.adapter;

import java.util.ArrayList;

import lib.imageLoader.ScrollTextView;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.R;
import covisoft.android.EasyLife.TabpageActivity;
import covisoft.android.item.item_Notification;
import covisoft.android.services.service_Notification_Delete;
import covisoft.android.tab3_Home.Activity_Home_Item;
import covisoft.android.tabhost.NavigationActivity;

/*
 * Adapter show list Notification from notification system
 * 
 * Last Updater: Huan
 * Last Updated: 14/06/2013
 * Update Info:
 *        - Delete set value for variable curPos of Activity_Home_List  
 *        - Navigation navi + Activity activity -> NavigationActivity activity
 * 
 */

public class Adapter_Notification extends BaseAdapter {

	private ArrayList<item_Notification> arNotification;
	private NavigationActivity activity;
	
	public Adapter_Notification(NavigationActivity activity, ArrayList<item_Notification> arNotification) {

		this.arNotification = arNotification;
		this.activity = activity;
	}

	public int getCount() {
		return arNotification.size();
	}

	public item_Notification getItem(int position) {
		return arNotification.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		return 1;
	}

	public int getViewTypeCount() {
		return 1;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		final ViewHolder holder;

		View rowView = convertView;
		if (rowView == null) {
			// inflating the row
			rowView = mInflater.inflate(R.layout.listview_notification, parent, false);
			holder = new ViewHolder();

			holder.layout_Main = (LinearLayout) rowView.findViewById(R.id.layout_Main);
			holder.layout_Large = (LinearLayout) rowView.findViewById(R.id.layout_Large);
			holder.txtTitle = (ScrollTextView) rowView.findViewById(R.id.txtTitle);
			holder.txtDesc = (ScrollTextView) rowView.findViewById(R.id.txtDesc);
			holder.txtTitle_large = (ScrollTextView) rowView.findViewById(R.id.txtTitle_large);
			holder.txtDesc_large = (TextView) rowView.findViewById(R.id.txtDesc_large);
			holder.img_Arrow = (ImageView)rowView.findViewById(R.id.img_Arrow);
			
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}
		
		final int pos = position;

		holder.txtTitle.setText(arNotification.get(pos).getTitle());

		holder.txtDesc.setText(arNotification.get(pos).getDescription());
		
		holder.txtTitle_large.setText(arNotification.get(pos).getTitle());

		holder.txtDesc_large.setText(arNotification.get(pos).getDescription());

		if(arNotification.get(pos).getKind() == 4) {
			holder.img_Arrow.setVisibility(View.VISIBLE);
		} else {
			holder.img_Arrow.setVisibility(View.GONE);
		}
		
		holder.layout_Main.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				AsyncTask_DelNotification task = new AsyncTask_DelNotification(pos);
				task.execute();
				
//				service_Notification_Delete service = new service_Notification_Delete(activity, arNotification.get(pos).getId());
//				service.start();
				//==================================================================
				
				
				
				//==================================================================
				
				if(arNotification.get(pos).getKind() != 4) {

					Intent intent = new Intent(activity, Activity_Home_Item.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					if (arNotification.get(pos).getIdfranchise().equals("")) {
						intent.putExtra("act", EZUtil.db_act_general);
					} else {
						intent.putExtra("act", EZUtil.db_act_franchise);
					}
					if(arNotification.get(pos).getKind() == 1 || arNotification.get(pos).getKind() == 3) {
						intent.putExtra("notic_kind", "Voucher");
					} else if(arNotification.get(pos).getKind() == 2){
						intent.putExtra("notic_kind", "Event");
					}
					intent.putExtra("no", arNotification.get(pos).getIdcompany() + "");
					intent.putExtra("CategoryName", "");
					intent.putExtra("parentCategoryNo", arNotification.get(pos).getCategoryNoBasic() + "");
					activity.goNextHistory("Activity_Home_Item", intent);
				} else {
					holder.layout_Main.setVisibility(View.GONE);
					holder.layout_Large.setVisibility(View.VISIBLE);
					
				}
				
			}
		});
		
		holder.layout_Large.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				holder.layout_Main.setVisibility(View.VISIBLE);
				holder.layout_Large.setVisibility(View.GONE);
				
			}
		});

		return rowView;
	}
	
	private class AsyncTask_DelNotification extends AsyncTask<Void, Void, String> {

		int pos;
		
		public AsyncTask_DelNotification(int pos) {
			
			super();
			this.pos = pos;
	        // do stuff
	    }
		
		@Override
		protected String doInBackground(Void... params) {

			service_Notification_Delete service = new service_Notification_Delete(activity, arNotification.get(pos).getId());
			return service.start();
			
		}

		@Override
		protected void onPostExecute(String result) {

			if(result.equals("1")) {
				int no_Notification = Integer.parseInt(TabpageActivity.txt_Number.getText().toString()) - 1;
		        if(no_Notification > 0 && no_Notification < 100) {
					TabpageActivity.rel_Notification.setVisibility(View.VISIBLE);
					TabpageActivity.txt_Number.setText(no_Notification + "");
				} else if(no_Notification >= 100) {
					TabpageActivity.rel_Notification.setVisibility(View.VISIBLE);
					TabpageActivity.txt_Number.setText("...");
				} else {
					TabpageActivity.rel_Notification.setVisibility(View.GONE);
				}
			}
			
			super.onPostExecute(result);
		}

	}
	static class ViewHolder {
		public LinearLayout layout_Main;
		public LinearLayout layout_Large;
		public ScrollTextView txtTitle;
		public ScrollTextView txtDesc;
		public TextView txtDesc1;
		public ScrollTextView txtTitle_large;
		public TextView txtDesc_large;
		public ImageView img_Arrow;
	}
}
