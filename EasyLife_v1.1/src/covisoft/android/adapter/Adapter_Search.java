package covisoft.android.adapter;

import java.util.ArrayList;

import lib.imageLoader.ImageLoader_Rounded;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.R;
import covisoft.android.item.item_store_list_search;
import covisoft.android.tab3_Home.Activity_Home_Item;
import covisoft.android.tabhost.NavigationActivity;

/*
 * Adapter show list shop from function Search
 * 
 * Last Updater: Huan
 * Last Updated: 14/06/2013
 * Update Info:
 *        - Delete set value for variable curPos of Activity_Home_List  
 *        - Navigation navi + Activity activity -> NavigationActivity activity
 * 
 */

public class Adapter_Search extends BaseAdapter {

	private final ArrayList<item_store_list_search> values;

	private NavigationActivity activity;

	public ImageLoader_Rounded imageLoader;
	public String categoryName = "";
	public String parentCategoryNo = "1";
	private LayoutInflater inflater;

	private Dialog dialog;
	private Boolean isLoading = false;
	private int type;
	
// =========================================================================================
	public Adapter_Search(NavigationActivity activity, ArrayList<item_store_list_search> arItem, int type) {
		this.values = arItem;
		this.activity = activity;
		this.type = type;
		
		imageLoader = new ImageLoader_Rounded(activity.getApplicationContext());
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}

	public int getCount() {
		return values.size();
	}

	public item_store_list_search getItem(int position) {
		return values.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		return position;
	}

	public int getViewTypeCount() {
		return 1;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		final int pos = position;

		ViewHolder holder;

		View rowView = convertView;
		if (rowView == null) {
			// inflating the row
			rowView = inflater.inflate(R.layout.listview_item_screen_05, parent, false);
			holder = new ViewHolder();

			holder.layout_LevelGroup = (LinearLayout) rowView.findViewById(R.id.layout_LevelGroup);
			holder.txtLevelGroup = (TextView) rowView.findViewById(R.id.txtLevelGroup);
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
		if (values.get(pos).level == 0) {
			holder.txtLevelGroup.setText(activity.getResources().getString(R.string.shopRank_Normal));
		} else if (values.get(pos).level == 1) {
			holder.txtLevelGroup.setText(activity.getResources().getString(R.string.shopRank_Member));
		} else if (values.get(pos).level == 2) {
			holder.txtLevelGroup.setText(activity.getResources().getString(R.string.shopRank_VIP));
		}

		imageLoader.DisplayImage(values.get(pos).s_logo, holder.image, R.drawable.s_05_noimage, 40);

		holder.txtName.setText(values.get(pos).name);
		holder.txtAddress.setText(values.get(pos).addr);
		if (values.get(pos).distance == 0) {
			holder.txtDistance.setVisibility(View.GONE);
		} else {
			holder.txtDistance.setVisibility(View.VISIBLE);
			holder.txtDistance.setText(String.format("%.2f", values.get(pos).distance) + " km");
		}

		if (values.get(pos).coupon == 0) {
			holder.img_Coupon.setVisibility(View.GONE);
		} else if (values.get(pos).coupon == 1) {
			holder.img_Coupon.setVisibility(View.VISIBLE);
			holder.img_Coupon.setImageResource(R.drawable.sale);
		}

		if(values.get(pos).tel.trim().equals("")) {
			holder.btnPhone.setBackgroundResource(R.drawable.s_05phone_none);
		} else {
			holder.btnPhone.setBackgroundResource(R.drawable.s_05phone);
		}
		holder.btnPhone.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				if (!values.get(pos).tel.equals("")) {
					String s_tel = values.get(pos).tel;
					Uri number = Uri.parse("tel:" + s_tel);
					Intent intent_tel = new Intent(Intent.ACTION_DIAL, number);
					activity.startActivity(intent_tel);
				} else {
					showPopupOneOption(activity.getResources().getString(R.string.popup_NoPhoneNumber));
				}
			}
		});

		holder.layout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(activity, Activity_Home_Item.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("act", EZUtil.db_act_general);
				intent.putExtra("no", values.get(pos).no);
				intent.putExtra("CategoryName", categoryName);
				intent.putExtra("parentCategoryNo", values.get(pos).categoryNoBasic);
				activity.goNextHistory("Activity_Home_Item", intent);

			}
		});

		if(type == 1) {
			if (pos == 0) {
				holder.layout_LevelGroup.setVisibility(View.VISIBLE);
			} else {
				if (values.get(pos).level != values.get(pos - 1).level) {
					holder.layout_LevelGroup.setVisibility(View.VISIBLE);
				} else {
					holder.layout_LevelGroup.setVisibility(View.GONE);
				}
			}
		} else if(type == 2){
			holder.layout_LevelGroup.setVisibility(View.GONE);
		}

		return rowView;
	}

	static class ViewHolder {
		public LinearLayout layout_LevelGroup;
		public TextView txtLevelGroup;
		public ImageView image;
		public TextView txtName;
		public TextView txtAddress;
		public TextView txtDistance;
		public ImageView img_Coupon;
		public Button btnPhone;
		public LinearLayout layout;
	}

	public void showPopupOneOption(String content) {

		if (!isLoading) {
			isLoading = true;

			dialog = new Dialog(activity.getParent(), R.style.myBackgroundStyle);
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			lp.copyFrom(dialog.getWindow().getAttributes());
			lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
			lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.popup_one_option);

			TextView txt = (TextView) dialog.findViewById(R.id.txtContent);
			txt.setText(content);

			Button btn_OK = (Button) dialog.findViewById(R.id.btn_OK);
			btn_OK.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialog.getWindow().setLayout(500, 400);
			dialog.show();

			dialog.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					isLoading = false;
				}
			});
		}
	}

}
