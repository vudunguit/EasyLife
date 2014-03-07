package covisoft.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import covisoft.android.EasyLife.R;
import covisoft.android.item.item_Shop_Menu;
import covisoft.android.tabhost.NavigationActivity;

/*
 * Adapter show Shop's Menu: just show
 * 
 * Last Updated: 14.06.2013
 * Last Updater: Huan
 * Update Info:
 *        - NavigationActivity navi + Activity activity = NavigationActivity activity
 * 
 */
public class Adapter_Menu extends BaseAdapter {

	private final ArrayList<item_Shop_Menu> values;
	private NavigationActivity activity;

	public Adapter_Menu(NavigationActivity activity, ArrayList<item_Shop_Menu> values) {
		
		this.activity = activity;
		this.values = values;
		
	}

	public int getCount() {
		return values.size();
	}

	public item_Shop_Menu getItem(int position) {
		return values.get(position);
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
		
		final int pos = position;
		
		View rowView = convertView;
		
		ViewHolder holder;
		
		if (convertView == null) {
	        // inflating the row
			rowView = inflater.inflate(R.layout.listview_item_screen_12,parent, false);
			holder = new ViewHolder();
			
			holder.txtName = (TextView) rowView.findViewById(R.id.txtFoodName);
			holder.txtPrice = (TextView) rowView.findViewById(R.id.txtFoodPrice);
			
			rowView.setTag(holder);
	    } else {
	    	holder = (ViewHolder) rowView.getTag();
	    }
		
		holder.txtName.setText(values.get(pos).name);

		holder.txtPrice.setText(values.get(pos).price);

		return rowView;
	}
	
	static class ViewHolder {
		public TextView txtName;
		public TextView txtPrice;
	}

}
