package covisoft.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import covisoft.android.EasyLife.R;
import covisoft.android.item.item_Category;
import covisoft.android.tab3_Home.Activity_Home_List;
import covisoft.android.tabhost.NavigationActivity;

/*
 * Adapter of list of SubCategory: Category -> SubCategory
 * 
 * Last Updated: 14.06.2013
 * Last Updater: Huan
 * Update Info:
 *         - Remove set value for EZUtil.curSubCategoryNo
 *         - Remove set value for EZUtil.curSubCategoryName
 * 
 */
public class Adapter_SubCategory extends BaseAdapter {
	
	private ArrayList<item_Category> arSrc;

	private NavigationActivity activity;
	private String category = "";

	public Adapter_SubCategory(NavigationActivity activity, ArrayList<item_Category> arItem, String category) {
		
		this.arSrc = arItem;
		this.activity = activity;
		this.category = category;
	}

	public int getCount() {
		return arSrc.size();
	}

	public item_Category getItem(int position) {
		return arSrc.get(position);
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
		
		if (convertView == null) {
			int res = 0;
			res = R.layout.listview_item_category;
			convertView = mInflater.inflate(res, parent, false);
		}

		final int pos = position;

		final RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout_1);
		final TextView textview = (TextView) convertView.findViewById(R.id.txtCategoryName);

		layout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(activity, Activity_Home_List.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("category", arSrc.get(pos).no);
				intent.putExtra("CategoryName", arSrc.get(pos).categoryName);
				intent.putExtra("parentCategoryNo", category);
				activity.goNextHistory("Activity_Home_List", intent);

			}
		});
		textview.setText(arSrc.get(position).categoryName);

		return convertView;
	}
}
