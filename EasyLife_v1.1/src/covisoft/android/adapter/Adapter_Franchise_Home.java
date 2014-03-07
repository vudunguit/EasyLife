package covisoft.android.adapter;

import java.util.ArrayList;

import lib.imageLoader.ImageLoader_Rounded;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.R;
import covisoft.android.item.item_Franchise;
import covisoft.android.tab3_Home.Activity_Franchise_Shop;
import covisoft.android.tabhost.NavigationActivity;

/*
 * Adapter show list Franchise
 * Last Updated: 17.06.2013
 * Last Updater: Huan
 * Updated Info:
 *          - Use class ViewHolder to keep information
 */

public class Adapter_Franchise_Home extends BaseAdapter {
	
	private ArrayList<item_Franchise> values;
	private NavigationActivity activity;

	private ImageLoader_Rounded imageLoader;

	public Adapter_Franchise_Home(NavigationActivity activity, ArrayList<item_Franchise> arItem) {

		this.values = arItem;
		this.activity = activity;
		imageLoader = new ImageLoader_Rounded(activity.getApplicationContext());

	}

	public int getCount() {
		return values.size();
	}

	public item_Franchise getItem(int position) {
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

		final ViewHolder holder;

		View rowView = convertView;
		if (rowView == null) {
			// inflating the row
			rowView = inflater.inflate(R.layout.listview_item_franchise, parent, false);
			holder = new ViewHolder();
			
			holder.imageview = (ImageView) rowView.findViewById(R.id.img_franchise);
			holder.relative_franchise = (RelativeLayout) rowView.findViewById(R.id.relative_franchise);
			holder.txtFranchiseName = (TextView) rowView.findViewById(R.id.txtFranchiseName);
			
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}

		holder.txtFranchiseName.setText(values.get(position).franchiseName);
		imageLoader.DisplayImage(values.get(position).s_logo, holder.imageview, R.drawable.s_19_noimage, EZUtil.REQUIRED_SIZE_MIDDLE);

		holder.relative_franchise.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(activity, Activity_Franchise_Shop.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("franchiseNo", values.get(pos).franchiseNo);
				intent.putExtra("franchiseName", values.get(pos).franchiseName);
				intent.putExtra("s_logo", values.get(pos).s_logo);
				intent.putExtra("type", values.get(pos).categoryNoBasic);
				activity.goNextHistory("Activity_FranchiseShopList", intent);

			}
		});

		return rowView;
	}
	
	static class ViewHolder {
		public ImageView imageview;
		public RelativeLayout relative_franchise;
		public TextView txtFranchiseName;
	}

//	void f_icon(ImageView imageview, String category) {
//		if (category.equals(EZUtil.category_code_1)) {
//			imageview.setBackgroundResource(R.drawable.s_03_button02);
//		} else if (category.equals(EZUtil.category_code_2)) {
//			imageview.setBackgroundResource(R.drawable.s_03_button01);
//		} else if (category.equals(EZUtil.category_code_3)) {
//			imageview.setBackgroundResource(R.drawable.s_03_button03);
//		} else if (category.equals(EZUtil.category_code_4)) {
//			imageview.setBackgroundResource(R.drawable.s_03_button04);
//		} else if (category.equals(EZUtil.category_code_5)) {
//			imageview.setBackgroundResource(R.drawable.s_03_button08);
//		} else if (category.equals(EZUtil.category_code_6)) {
//			imageview.setBackgroundResource(R.drawable.s_03_button07);
//		} else if (category.equals(EZUtil.category_code_7)) {
//			imageview.setBackgroundResource(R.drawable.s_03_button06);
//		}
//	}
}