package covisoft.android.adapter;

import java.util.ArrayList;

import lib.imageLoader.ImageLoader_Rounded;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.R;
import covisoft.android.item.item_store_list_alba;
import covisoft.android.tab4.Activity_HireEmployee;
import covisoft.android.tabhost.NavigationActivity;

/*
 * Adapter show list shop in tab 4
 * Last Updated: 17.06.2013
 * Last Updater: Huan
 * Update Info:
 *        - NavigationActivity navi + Activity activity = NavigationActivity activity
 *        - Change inflater + imageLoader to Local Variable
 *        - Use ViewHolder to keep Information
 */
public class Adapter_ShopTab4 extends BaseAdapter {

	private final ArrayList<item_store_list_alba> values;
	public NavigationActivity activity;
	public String categoryName = "";

	
	public Adapter_ShopTab4(NavigationActivity activity,ArrayList<item_store_list_alba> arItem) {
		
		this.values = arItem;
		this.activity = activity;
		
	}

	public int getCount() {
		return values.size();
	}

	public item_store_list_alba getItem(int position) {
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
		ImageLoader_Rounded imageLoader = new ImageLoader_Rounded(activity.getApplicationContext());
		
		final int pos = position;
		final ViewHolder holder;

		View rowView = convertView;
		if (rowView == null) {
			// inflating the row
			rowView = inflater.inflate(R.layout.listview_item_screen_05, parent, false);
			holder = new ViewHolder();
			
			holder.layout_LevelGroup = (LinearLayout)rowView.findViewById(R.id.layout_LevelGroup);
			holder.image = (ImageView) rowView.findViewById(R.id.img);
			holder.txtName = (TextView) rowView.findViewById(R.id.txtName);
			holder.txtAddress = (TextView) rowView.findViewById(R.id.txtAddress);
			holder.txtDistance = (TextView) rowView.findViewById(R.id.txtDistance);
			holder.img_Coupon = (ImageView) rowView.findViewById(R.id.img_coupon);
			holder.btnPhone = (Button) rowView.findViewById(R.id.btnPhone);
			holder.layout = (LinearLayout) rowView.findViewById(R.id.layout_1);
			
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}
		
		holder.layout_LevelGroup.setVisibility(View.GONE);
		
		imageLoader.DisplayImage(values.get(position).s_logo, holder.image,R.drawable.s_05_noimage, 35);

		holder.txtName.setText(values.get(position).title);

		holder.txtAddress.setText(values.get(position).cont);

		holder.txtDistance.setText(values.get(position).companyName);

		holder.img_Coupon.setVisibility(View.GONE);

		if(values.get(pos).tel.trim().equals("")) {
			holder.btnPhone.setBackgroundResource(R.drawable.s_05phone_none);
		} else {
			holder.btnPhone.setBackgroundResource(R.drawable.s_05phone);
		}
		holder.btnPhone.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if(!values.get(pos).tel.equals("")) {
					String s_tel = values.get(pos).tel;
					Uri number = Uri.parse("tel:" + s_tel);
					Intent intent_tel = new Intent(Intent.ACTION_DIAL, number);
					activity.startActivity(intent_tel);
				} else {
					Toast.makeText(activity, activity.getResources().getString(R.string.popup_NoPhoneNumber), Toast.LENGTH_LONG);
				}
			}
		});

		holder.layout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(activity, Activity_HireEmployee.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("no", values.get(pos).no);
				intent.putExtra("category", categoryName);
				intent.putExtra("act", EZUtil.db_act_general);
				activity.goNextHistory("Activity_HireEmployee", intent);

			}
		});

		return rowView;
	}
	
	static class ViewHolder {
		private LinearLayout layout_LevelGroup;
		private ImageView image;
		private TextView txtName;
		private TextView txtAddress;
		private TextView txtDistance;
		private ImageView img_Coupon;
		private Button btnPhone;
		private LinearLayout layout;
		
	}
	
}