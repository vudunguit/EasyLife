package covisoft.android.adapter;

import java.util.ArrayList;

import lib.imageLoader.ImageLoader_Rounded;
import lib.imageLoader.ScrollTextView;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.R;
import covisoft.android.item.item_Promotion;
import covisoft.android.tab3_Home.Activity_Home_Item;
import covisoft.android.tabhost.NavigationActivity;

/*
 * Adapter show list ALL Notification
 * 
 * Last Updater: Huan
 * Last Updated: 17/06/2013
 * Update Info:
 *        - Delete set value for variable curPos of Activity_Home_List  
 *        - Navigation navi + Activity activity -> NavigationActivity activity
 *        - Use ViewHolder to keep Information
 *        - Change ImageLoader_Rounded to Local Variable
 * 
 */
public class Adapter_Notic extends BaseAdapter {

	ArrayList<item_Promotion> arBoard;
	NavigationActivity activity;

	public Adapter_Notic(NavigationActivity activity, ArrayList<item_Promotion> arBoard) {

		this.arBoard = arBoard;
		this.activity = activity;
		
	}

	public int getCount() {
		return arBoard.size();
	}

	public item_Promotion getItem(int position) {
		return arBoard.get(position);
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

		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		ImageLoader_Rounded imageLoader = new ImageLoader_Rounded(activity.getApplicationContext());
		
		final int pos = position;

		View rowView = convertView;
		
		ViewHolder holder;
		
		if (convertView == null) {
	        // inflating the row
			rowView = inflater.inflate(R.layout.listview_item_notic,parent, false);
			holder = new ViewHolder();
			
			holder.image = (ImageView) rowView.findViewById(R.id.imgShopLogo);
			holder.txtShopName = (ScrollTextView) rowView.findViewById(R.id.txtShopName);
			holder.txtShopEvent = (ScrollTextView) rowView.findViewById(R.id.txtShopEvent);
			holder.layout_Main = (LinearLayout) rowView.findViewById(R.id.layout_Main);
			
			rowView.setTag(holder);
	    } else {
	    	holder = (ViewHolder) rowView.getTag();
	    }
		
		imageLoader.DisplayImage(arBoard.get(position).getShopLogo(), holder.image, R.drawable.s_05_noimage, 40);

		holder.txtShopName.setText(arBoard.get(pos).getShopName());

		holder.txtShopEvent.setText(arBoard.get(pos).getSubject());

		holder.layout_Main.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(activity, Activity_Home_Item.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				if (arBoard.get(pos).getType() == 1) {
					intent.putExtra("act", EZUtil.db_act_general);
				} else if (arBoard.get(pos).getType() == 2) {
					intent.putExtra("act", EZUtil.db_act_franchise);
				}

				intent.putExtra("no", arBoard.get(pos).getShopId() + "");
				intent.putExtra("CategoryName", arBoard.get(pos).getCategoryName());
				intent.putExtra("parentCategoryNo", arBoard.get(pos).getParentCategoryNo() + "");
				if(arBoard.get(pos).getKind() == 1 || arBoard.get(pos).getKind() == 3) {
					intent.putExtra("notic_kind", "Voucher");
				} else if(arBoard.get(pos).getKind() == 2){
					intent.putExtra("notic_kind", "Event");
				}
				
				activity.goNextHistory("Activity_Home_Item", intent);
			}
		});

		return rowView;
	}
	
	static class ViewHolder {
		private ImageView image;
		private ScrollTextView txtShopName;
		private ScrollTextView txtShopEvent;
		private LinearLayout layout_Main;
	}
	
}
