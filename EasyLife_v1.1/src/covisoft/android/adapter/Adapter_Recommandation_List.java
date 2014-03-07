package covisoft.android.adapter;

import java.util.ArrayList;

import lib.etc.lib_calculate_distance;
import lib.imageLoader.ImageLoader_Rounded;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.R;
import covisoft.android.item.item_Recommandation;
import covisoft.android.tab3_Home.Activity_Home_Item;
import covisoft.android.tab3_Home.Home_Activity;
import covisoft.android.tabhost.NavigationActivity;

public class Adapter_Recommandation_List extends BaseAdapter {
	
	ArrayList<item_Recommandation> arSrc;
	NavigationActivity activity;

	public Adapter_Recommandation_List(NavigationActivity activity,ArrayList<item_Recommandation> arItem) {
		
		arSrc = arItem;
		this.activity = activity;
		
	}

	public int getCount() {
		if(arSrc.size() > 100) {
			return 100;
		} else {
			return arSrc.size();
		}
	}

	public item_Recommandation getItem(int position) {
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
		
		ImageLoader_Rounded imageLoader = new ImageLoader_Rounded(activity.getApplicationContext());
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		final int pos = position;
		
		View rowView = convertView;
		
		ViewHolder holder;
		
		if (convertView == null) {
	        // inflating the row
			rowView = inflater.inflate(R.layout.listview_item_recommandation,parent, false);
			
			holder = new ViewHolder();
			
			holder.layout_LevelGroup = (LinearLayout)rowView.findViewById(R.id.layout_LevelGroup);
			holder.txtLevelGroup = (TextView)rowView.findViewById(R.id.txtLevelGroup);
			holder.layout_Recommandation = (LinearLayout) rowView.findViewById(R.id.layout_Recommandation);
			holder.imageview = (ImageView) rowView.findViewById(R.id.img_Logo);
			holder.txtName = (TextView) rowView.findViewById(R.id.txtName);
			holder.txtAddress = (TextView) rowView.findViewById(R.id.txtAddress);
			holder.txtDistance = (TextView) rowView.findViewById(R.id.txtDistance);
			holder.img_icon = (ImageView) rowView.findViewById(R.id.img_icon);
			holder.img_Coupon = (ImageView)rowView.findViewById(R.id.img_coupon);
			
			rowView.setTag(holder);
			
	    } else {
	    	holder = (ViewHolder) rowView.getTag();
	    }
		
		holder.txtLevelGroup.setText("VIP Level " + arSrc.get(pos).getLevel());
		holder.layout_LevelGroup.setVisibility(View.GONE);
		holder.layout_Recommandation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(activity, Activity_Home_Item.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("act", EZUtil.db_act_general);
				intent.putExtra("no", arSrc.get(pos).getNo());
				intent.putExtra("CategoryName", arSrc.get(pos).getCategoryName());
				intent.putExtra("parentCategoryNo", arSrc.get(pos).getCategoryNoBasic());
				activity.goNextHistory("Activity_Home_Item", intent);

			}
		});
		imageLoader.DisplayImage(arSrc.get(position).getS_logo(), holder.imageview,R.drawable.s_19_noimage, 40);
		holder.txtName.setText(arSrc.get(position).getName());
		holder.txtAddress.setText(arSrc.get(position).getAddr());
		
		if(Home_Activity.latitude != 0 || Home_Activity.longitude != 0) {
			holder.txtDistance.setText(String.format("%.2f", lib_calculate_distance.distance(Home_Activity.latitude, Home_Activity.longitude, arSrc
					.get(position).getLat(), arSrc.get(position).getLng())) + " km");
		} else {
			holder.txtDistance.setVisibility(View.GONE);
		}
		
		show_icon(holder.img_icon, arSrc.get(pos).getCategoryNoBasic());
		if (Integer.parseInt(arSrc.get(position).getCoupon()) == 0) {
			holder.img_Coupon.setVisibility(View.GONE);
		} else if (Integer.parseInt(arSrc.get(position).getCoupon()) == 1) {
			holder.img_Coupon.setVisibility(View.VISIBLE);
			holder.img_Coupon.setImageResource(R.drawable.sale);
		}
		return rowView;
	}

	void show_icon(ImageView imageview, String category) {
		if (category.equals(EZUtil.category_code_1)) {
			imageview.setBackgroundResource(R.drawable.s_03_button02);
		} else if (category.equals(EZUtil.category_code_2)) {
			imageview.setBackgroundResource(R.drawable.s_03_button01);
		} else if (category.equals(EZUtil.category_code_3)) {
			imageview.setBackgroundResource(R.drawable.s_03_button03);
		} else if (category.equals(EZUtil.category_code_4)) {
			imageview.setBackgroundResource(R.drawable.s_03_button04);
		} else if (category.equals(EZUtil.category_code_5)) {
			imageview.setBackgroundResource(R.drawable.s_03_button08);
		} else if (category.equals(EZUtil.category_code_6)) {
			imageview.setBackgroundResource(R.drawable.s_03_button07);
		} else if (category.equals(EZUtil.category_code_7)) {
			imageview.setBackgroundResource(R.drawable.s_03_button06);
		}
	}
	
	static class ViewHolder 
    {
		public LinearLayout layout_LevelGroup;
		public TextView txtLevelGroup;
		public LinearLayout layout_Recommandation;
        public ImageView imageview;
        public TextView txtName;
        public TextView txtAddress;
        public TextView txtDistance;
        public ImageView img_icon;
        public ImageView img_Coupon;
        
    }
	
}