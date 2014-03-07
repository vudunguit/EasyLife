package covisoft.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import covisoft.android.EasyLife.R;
import covisoft.android.item.item_MainCategory;
import covisoft.android.tabhost.NavigationActivity;

public class Adapter_MainCategory_4 extends BaseAdapter {

	private ArrayList<item_MainCategory> values;
	private NavigationActivity activity;

	public Adapter_MainCategory_4(NavigationActivity activity, ArrayList<item_MainCategory> arItem) {
		values = arItem;
		this.activity = activity;

	}

	public int getCount() {
		return values.size();
	}

	public item_MainCategory getItem(int position) {
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

		View rowView = inflater.inflate(R.layout.listview_item_category_tab4, parent, false);

		final int pos = position;

		ImageView imgIcon = (ImageView) rowView.findViewById(R.id.imgIcon);
		TextView txtName = (TextView) rowView.findViewById(R.id.txtName);
		TextView txtQuantity = (TextView) rowView.findViewById(R.id.txt_Quantity);

		if (values.get(pos).getCategoryName().equals(activity.getString(R.string.text_top_Food))) {
			imgIcon.setBackgroundResource(R.drawable.button01_notext);
		} else if (values.get(pos).getCategoryName().equals(activity.getString(R.string.text_top_Education))) {
			imgIcon.setBackgroundResource(R.drawable.button02_notext);
		} else if (values.get(pos).getCategoryName().equals(activity.getString(R.string.text_top_Beauty))) {
			imgIcon.setBackgroundResource(R.drawable.button03_notext);
		} else if (values.get(pos).getCategoryName().equals(activity.getString(R.string.text_top_Shopping))) {
			imgIcon.setBackgroundResource(R.drawable.button04_notext);
		} else if (values.get(pos).getCategoryName().equals(activity.getString(R.string.text_top_Tourism))) {
			imgIcon.setBackgroundResource(R.drawable.button06_notext);
		} else if (values.get(pos).getCategoryName().equals(activity.getString(R.string.text_top_Entertainment))) {
			imgIcon.setBackgroundResource(R.drawable.button07_notext);
		} else if (values.get(pos).getCategoryName().equals(activity.getString(R.string.text_top_Health))) {
			imgIcon.setBackgroundResource(R.drawable.button08_notext);
		}

		txtName.setText(values.get(position).getCategoryName());
		txtQuantity.setText(values.get(position).getQuantity() + "");

		return rowView;
	}

}
