package covisoft.android.adapter;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import covisoft.android.EasyLife.R;
import covisoft.android.item.item_Coupon;
import covisoft.android.services.service_Coupon_Delete;
import covisoft.android.tab2_MyCoupon.Activity_UseCoupon;
import covisoft.android.tabhost.NavigationActivity;

/*
 * Adapter for list Normal Coupon show in Tab 1
 * 
 * Last Updated: 13.06.2013
 * Last Updater: Huan
 * Update Info:
 *          - NavigationActivity navi + Activity activity = NavigationActivity activity
 * 
 */
public class Adapter_Coupon_Added extends BaseAdapter {

	private ArrayList<item_Coupon> values;

	private NavigationActivity activity;
	private Dialog dialog;
	private LayoutInflater inflater;
	
	public Adapter_Coupon_Added(NavigationActivity activity, ArrayList<item_Coupon> values) {
		this.activity = activity;
		this.values = values;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		
		final int pos = position;
		
		View rowView;
		if (convertView == null) {
	        // inflating the row
			rowView = inflater.inflate(R.layout.listview_item_screen_14_normal_coupon, parent, false);
	    } else {  
	    	rowView = convertView;
	    }

		TextView txtShopName = (TextView) rowView.findViewById(R.id.txtShopName);
		TextView txtSecond = (TextView) rowView.findViewById(R.id.txtSecond);
		
		TextView txtCouponTitle = (TextView) rowView.findViewById(R.id.txtCouponTitle);
		Button btnDate = (Button) rowView.findViewById(R.id.btnDate);
		Button btnDelete = (Button) rowView.findViewById(R.id.btnDelete);

		LinearLayout layout_useCoupon = (LinearLayout)rowView.findViewById(R.id.layout_useCoupon);

		txtShopName.setText(values.get(position).getCompanyName());
		txtSecond.setText(values.get(position).getAddr());
		txtCouponTitle.setText(values.get(position).getCouponName());

		btnDate.setText(activity.getString(R.string.s_14_ListCoupon_Remain) + values.get(position).getResidual_day() + " " + activity.getString(R.string.s_14_ListCoupon_Date));
		layout_useCoupon.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(activity, Activity_UseCoupon.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("coupon", values.get(pos));
				activity.goNextHistory("Activity_UseCoupon", intent);
			}

		});
			
		btnDate.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(activity, Activity_UseCoupon.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("coupon", values.get(pos));
				activity.goNextHistory("Activity_UseCoupon", intent);

			}
		});
		btnDelete.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				dialog = new Dialog(activity.getParent(), R.style.myBackgroundStyle);
				WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
				lp.copyFrom(dialog.getWindow().getAttributes());
				lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
				lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.popup_two_option);

				TextView txt = (TextView) dialog.findViewById(R.id.txtContent);
				txt.setText(v.getContext().getString(R.string.popup_Coupon_Del));

				Button btn_OK = (Button) dialog.findViewById(R.id.btn_OK);
				btn_OK.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						dialog.dismiss();

						service_Coupon_Delete xml = new service_Coupon_Delete();
						xml.init(values.get(pos).getNo() + "", activity);

						String result = xml.start();
						if(result.equals("Y")) {
							values.remove(pos);
							notifyDataSetChanged();
							
							dialog = new Dialog(activity.getParent(), R.style.myBackgroundStyle);
							WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
							lp.copyFrom(dialog.getWindow().getAttributes());
							lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
							lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
							dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialog.setContentView(R.layout.popup_one_option);

							TextView txt = (TextView) dialog.findViewById(R.id.txtContent);
							txt.setText(activity.getString(R.string.popup_Coupon_Del_Success));

							Button btn_OK = (Button) dialog.findViewById(R.id.btn_OK);
							btn_OK.setOnClickListener(new OnClickListener() {

								public void onClick(View v) {
									dialog.dismiss();
								}
							});
							dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
							dialog.show();
							
						} else {
							dialog = new Dialog(activity.getParent(), R.style.myBackgroundStyle);
							WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
							lp.copyFrom(dialog.getWindow().getAttributes());
							lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
							lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
							dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialog.setContentView(R.layout.popup_one_option);

							TextView txt = (TextView) dialog.findViewById(R.id.txtContent);
							txt.setText(activity.getString(R.string.popup_Coupon_Del_Fail));

							Button btn_OK = (Button) dialog.findViewById(R.id.btn_OK);
							btn_OK.setOnClickListener(new OnClickListener() {

								public void onClick(View v) {
									dialog.dismiss();
								}
							});
							dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
							dialog.show();
						}
						
					}
				});
				Button btn_Cancel = (Button) dialog.findViewById(R.id.btn_Cancel);
				btn_Cancel.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						dialog.dismiss();
					}
				});

				dialog.getWindow().setLayout(
						WindowManager.LayoutParams.WRAP_CONTENT,
						WindowManager.LayoutParams.WRAP_CONTENT);
				dialog.show();
			}
		});

		return rowView;
	}

}
