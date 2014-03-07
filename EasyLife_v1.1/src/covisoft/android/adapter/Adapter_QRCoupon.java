package covisoft.android.adapter;

import java.util.ArrayList;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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
import covisoft.android.EasyLife.EasyLifeActivity;
import covisoft.android.EasyLife.R;
import covisoft.android.item.item_QRCoupon_list;
import covisoft.android.services.service_QRCoupon_Delete;
import covisoft.android.tab2_MyCoupon.QRCodeActivity;
import covisoft.android.tabhost.NavigationActivity;

public class Adapter_QRCoupon extends BaseAdapter {
	
	private final ArrayList<item_QRCoupon_list> values;

	NavigationActivity activity;
	String result = "";
	int del = -1;

	Dialog dialog;
	
	public Adapter_QRCoupon(NavigationActivity activity, ArrayList<item_QRCoupon_list> values) {
		this.activity = activity;
		this.values = values;
	}

	public int getCount() {
		return values.size();
	}

	public item_QRCoupon_list getItem(int position) {
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
		View rowView = inflater.inflate(R.layout.listview_item_screen_14_qrcoupon, parent, false);

		TextView txtShopName = (TextView) rowView.findViewById(R.id.txtShopName);
		TextView txtSecond = (TextView) rowView.findViewById(R.id.txtSecond);
		TextView txtCouponTitle = (TextView) rowView.findViewById(R.id.txtCouponTitle);

		LinearLayout layout_useCoupon = (LinearLayout) rowView.findViewById(R.id.layout_useCoupon);

		txtShopName.setText(values.get(position).getNameStore());
		txtSecond.setText(values.get(position).getAddress4());

		txtCouponTitle.setText(values.get(position).getStartDate() + " -- " + values.get(position).getEndDate());

		layout_useCoupon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, QRCodeActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("couponID", values.get(pos).getNo() + "");
				activity.goNextHistory("QRCodeActivity", intent);
			}
		});
		Button btnDelete = (Button) rowView.findViewById(R.id.btnDelete);
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
						del = pos;
						init_thread_Delete();
					}
				});
				Button btn_Cancel = (Button) dialog.findViewById(R.id.btn_Cancel);
				btn_Cancel.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						dialog.dismiss();
					}
				});

				dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
				dialog.show();

			}
		});

		return rowView;
	}

	ProgressDialog progressDialog;

	void init_progressDialog() {
		progressDialog = ProgressDialog.show(activity.getParent(), "", "Loading...", true, true);
	}

	void init_thread_Delete() {
		init_progressDialog();
		Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {

				if (result.equals("2")) {
					values.remove(del);
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
					txt.setText(activity.getString(R.string.popup_QRCoupon_wrong));

					Button btn_OK = (Button) dialog.findViewById(R.id.btn_OK);
					btn_OK.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
					dialog.show();
				}
				progressDialog.dismiss();
			}
		};

		InitThread_Delete thread = new InitThread_Delete();

		thread.mHandler = mHandler;
		thread.setDaemon(true);
		thread.start();
	}

	class InitThread_Delete extends Thread {
		Handler mHandler;

		public void run() {

			service_QRCoupon_Delete xml = new service_QRCoupon_Delete();
			xml.init(activity, values.get(del).getNo() + "", EasyLifeActivity.user.getUsername());

			result = xml.start();

			mHandler.sendEmptyMessage(0);
		}
	}
}
