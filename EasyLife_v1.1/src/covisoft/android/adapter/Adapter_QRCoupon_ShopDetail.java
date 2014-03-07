package covisoft.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import covisoft.android.EasyLife.R;
import covisoft.android.item.item_QRCoupon_Shop;
import covisoft.android.tab3_Home.Activity_QRCoupon_Shop;
import covisoft.android.tabhost.NavigationActivity;

/*
 * Adapter show list QRCoupon in tab Event(Khuyen Mai) of Shop Detail page: Just show
 * 
 * Creater: Huan
 * Created: 18.07.2013
 * 
 */
public class Adapter_QRCoupon_ShopDetail extends BaseAdapter {

	private final ArrayList<item_QRCoupon_Shop> values;
	private NavigationActivity activity;

	public Adapter_QRCoupon_ShopDetail(NavigationActivity activity, ArrayList<item_QRCoupon_Shop> values) {
		this.activity = activity;
		this.values = values;
	}

	public int getCount() {
		return values.size();
	}

	public item_QRCoupon_Shop getItem(int position) {
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
		
		final int pos = position;
		
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.listview_item_screen_05_coupon, parent, false);

		TextView txtName = (TextView) rowView.findViewById(R.id.txtCouponName_small);
		txtName.setText(values.get(position).getCouponName());

		rowView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(activity, Activity_QRCoupon_Shop.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intent.putExtra("qrCoupon", values.get(pos));
				activity.goNextHistory("Activity_QRCoupon_Shop", intent);
			}
		});
		
		return rowView;
	}
}