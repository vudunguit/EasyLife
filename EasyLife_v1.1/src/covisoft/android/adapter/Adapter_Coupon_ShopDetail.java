package covisoft.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import covisoft.android.EasyLife.R;
import covisoft.android.item.item_Coupon;
import covisoft.android.tabhost.NavigationActivity;

/*
 * Adapter show list coupon in tab Event(Khuyen Mai) of Shop Detail page: Just show
 * 
 * Last Updated: 14.06.2013
 * Last Updater: Huan
 * Update Info:
 *        - NavigationActivity navi + Activity activity = NavigationActivity activity
 *        - inflater -> local variable
 * 
 */
public class Adapter_Coupon_ShopDetail extends BaseAdapter {

	private final ArrayList<item_Coupon> values;
	private NavigationActivity activity;

	public Adapter_Coupon_ShopDetail(NavigationActivity activity, ArrayList<item_Coupon> values) {
		this.activity = activity;
		this.values = values;
	}

	public int getCount() {
		return values.size();
	}

	public item_Coupon getItem(int position) {
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

		View rowView = inflater.inflate(R.layout.listview_item_screen_05_coupon, parent, false);

		TextView txtName = (TextView) rowView.findViewById(R.id.txtCouponName_small);
		txtName.setText(values.get(position).getCouponName());

		return rowView;
	}
}