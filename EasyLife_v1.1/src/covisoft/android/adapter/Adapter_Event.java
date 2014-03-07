package covisoft.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import covisoft.android.EasyLife.R;
import covisoft.android.item.item_Shop_Event;
import covisoft.android.tabhost.NavigationActivity;

public class Adapter_Event extends BaseAdapter {
	
	private ArrayList<item_Shop_Event> arItem;
	private NavigationActivity activity;

	public Adapter_Event(NavigationActivity activity, ArrayList<item_Shop_Event> arItem) {
		
		this.arItem = arItem;
		this.activity = activity;
		
	}

	public int getCount() {

		return this.arItem.size();
	}

	public item_Shop_Event getItem(int position) {
		return this.arItem.get(position);
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
		
		ViewHolder holder;

		View rowView = convertView;
		if (rowView == null) {
			// inflating the row
			rowView = mInflater.inflate(R.layout.listview_item_event, parent, false);
			holder = new ViewHolder();

			holder.event_name = (TextView) rowView.findViewById(R.id.textview_eventName);
			holder.event_date = (TextView) rowView.findViewById(R.id.textview_eventStartDate);
			holder.event_cont = (TextView) rowView.findViewById(R.id.textview_eventCont);

			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}
		
		holder.event_name.setText(arItem.get(position).name);
		
		holder.event_date.setText(arItem.get(position).start_date + "~" + arItem.get(position).end_date);
		
		holder.event_cont.setText(arItem.get(position).cont);

		return rowView;
	}
	
	static class ViewHolder {
		public TextView event_name;
		public TextView event_date;
		public TextView event_cont;
	}

}