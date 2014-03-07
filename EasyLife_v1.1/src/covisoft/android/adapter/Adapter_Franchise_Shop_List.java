package covisoft.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.R;
import covisoft.android.item.item_Franchise_Shop;
import covisoft.android.tab3_Home.Activity_Home_Item;
import covisoft.android.tabhost.NavigationActivity;

/*
 * Adapter show list shop of 1 franchise
 * Last Updated: 17.06.2013
 * Last Updater: Huan
 * Update Info:
 *        - Change mInflater to Local variable
 * 
 */
public class Adapter_Franchise_Shop_List extends BaseAdapter {
	
	private ArrayList<item_Franchise_Shop> values;
	private String categoryName = "";
	private String type = "";
	
	private NavigationActivity activity;

	public Adapter_Franchise_Shop_List(NavigationActivity activity,ArrayList<item_Franchise_Shop> arItem, String categoryName, String type) {
		
		this.activity = activity;
		this.values = arItem;
		this.categoryName = categoryName;
		this.type = type;
		
	}

	public int getCount() {
		return values.size();
	}

	public item_Franchise_Shop getItem(int position) {
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

		LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		final int pos = position;
		final ViewHolder holder;
		

		View rowView = convertView;
		if (rowView == null) {
			// inflating the row
			rowView = mInflater.inflate(R.layout.listview_item_franchise_shop_list, parent, false);
			holder = new ViewHolder();

			holder.layout_small = (RelativeLayout) rowView.findViewById(R.id.layout_small);
			holder.layout_large = (RelativeLayout) rowView.findViewById(R.id.layout_large);
			holder.txtName = (TextView) rowView.findViewById(R.id.txtShopName_small);
			holder.btnExpand = (Button) rowView.findViewById(R.id.btnExpand_small);
			holder.btnOFf = (Button) rowView.findViewById(R.id.btnOff_large);
			holder.txtShopName_large = (TextView) rowView.findViewById(R.id.txtShopName_large);
			holder.txtAddress_large = (TextView) rowView.findViewById(R.id.txtAddress_large);
			holder.txtTel_large = (TextView) rowView.findViewById(R.id.txtTel_Large);
			holder.img_coupon_small = (ImageView) rowView.findViewById(R.id.img_Coupon);

			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}
		
		if (values.get(position).getFlag()) {
			holder.layout_large.setVisibility(View.VISIBLE);
			holder.layout_small.setVisibility(View.GONE);
		} else {
			holder.layout_large.setVisibility(View.GONE);
			holder.layout_small.setVisibility(View.VISIBLE);
		}

		holder.layout_small.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				holder.layout_small.setVisibility(View.GONE);
				holder.layout_large.setVisibility(View.VISIBLE);
				values.get(pos).setFlag(true);
			}
		});

		holder.txtName.setText(values.get(position).getName() + " - " + values.get(position).getAddr());

		holder.layout_large.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				values.get(pos).setFlag(false);

				Intent intent = new Intent(activity, Activity_Home_Item.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("no", values.get(pos).getNo());
				intent.putExtra("act", EZUtil.db_act_franchise);
				intent.putExtra("CategoryName", categoryName);
				intent.putExtra("parentCategoryNo", type);
				activity.goNextHistory("Activity_Home_Item", intent);

			}
		});

		holder.btnExpand.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				holder.layout_small.setVisibility(View.GONE);
				holder.layout_large.setVisibility(View.VISIBLE);
				values.get(pos).setFlag(true);
			}
		});

		holder.btnOFf.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				holder.layout_small.setVisibility(View.VISIBLE);
				holder.layout_large.setVisibility(View.GONE);
				values.get(pos).setFlag(false);
			}
		});

		holder.txtShopName_large.setText(values.get(position).getName());

		holder.txtAddress_large.setText(values.get(position).getAddr());

		holder.txtTel_large.setText(values.get(position).getTel());

		if (values.get(position).getCoupon() == 1) {
			holder.img_coupon_small.setVisibility(View.VISIBLE);
			holder.img_coupon_small.setImageResource(R.drawable.img_sale);
		} else {
			holder.img_coupon_small.setVisibility(View.GONE);
		}
		
		return rowView;
	}
	
	static class ViewHolder {
		public RelativeLayout layout_small;
		public RelativeLayout layout_large;
		public TextView txtName;
		public Button btnExpand;
		public Button btnOFf;
		public TextView txtShopName_large;
		public TextView txtAddress_large;
		public TextView txtTel_large;
		public ImageView img_coupon_small;
	}

}