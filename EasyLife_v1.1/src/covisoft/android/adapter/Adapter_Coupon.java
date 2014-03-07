package covisoft.android.adapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import lib.imageLoader.ImageDialog;
import lib.imageLoader.ImageLoader_Rounded;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import covisoft.android.EasyLife.CheckTimeAsyncTask;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.EasyLifeActivity;
import covisoft.android.EasyLife.R;
import covisoft.android.item.item_Coupon;
import covisoft.android.services.service_Coupon_Add;
import covisoft.android.tab3_Home.Activity_Login;
import covisoft.android.tabhost.NavigationActivity;

/*
 * adapter for list coupon (Shop -> list coupon)
 * 
 * Last Updated: 14.06.2013
 * Last Updater: Huan
 * Update Info:
 *         - NavigationActivity navi + Activity activity = NavigationActivity activity
 *         - Use Holder to control rowview smoothly
 *         - Clear all check condition about Activity_Home_Item (used another adapter for list in shop's detail)
 */
public class Adapter_Coupon extends BaseAdapter {

	private final ArrayList<item_Coupon> values;
	private NavigationActivity activity;

	private Dialog dialog;
	private Boolean isLoading = false;
	private Timer timer;

	private String addCouponResult = "";

	private Boolean isClicked = false;

	private ImageLoader_Rounded imageLoader;

	private String openPosition = "";
	
	public Adapter_Coupon(NavigationActivity activity, ArrayList<item_Coupon> values, String position) {
		this.activity = activity;
		this.values = values;
		this.openPosition = position;

		imageLoader = new ImageLoader_Rounded(activity.getApplicationContext());
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

		final int pos = position;

		final item_Coupon item = values.get(position);

		final ViewHolder holder;

		View rowView = convertView;
		if (rowView == null) {
			// inflating the row
			rowView = inflater.inflate(R.layout.listview_item_screen_13, parent, false);
			holder = new ViewHolder();

			// Layout Small
			holder.layout_small = (RelativeLayout) rowView.findViewById(R.id.layout_small);
			holder.txtName = (TextView) rowView.findViewById(R.id.txtCouponName_small);
			holder.btnExpand = (Button) rowView.findViewById(R.id.btnExpand_small);

			// Layout Large
			holder.layout_large = (RelativeLayout) rowView.findViewById(R.id.layout_large);
			holder.btnOff = (Button) rowView.findViewById(R.id.btnOff_large);
			holder.txtCouponName_large = (TextView) rowView.findViewById(R.id.txtCouponName_large);
			holder.txtDate_large = (TextView) rowView.findViewById(R.id.txtDate_large);
			holder.txtContent_Large = (TextView) rowView.findViewById(R.id.txtContent_Large);
			holder.layout_Caution = (LinearLayout) rowView.findViewById(R.id.layout_Caution);
			holder.txtText2Large = (TextView) rowView.findViewById(R.id.txtText2Large);
			holder.btnUseCoupon = (Button) rowView.findViewById(R.id.btnUseCoupon);
			holder.img_Coupon = (ImageView) rowView.findViewById(R.id.img_Coupon);

			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}

		if (values.get(position).isFlag()) {
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

		holder.txtName.setText(values.get(position).getCouponName());

		holder.btnExpand.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				holder.layout_small.setVisibility(View.GONE);
				holder.layout_large.setVisibility(View.VISIBLE);
				values.get(pos).setFlag(true);
			}
		});

		holder.layout_large.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				holder.layout_small.setVisibility(View.VISIBLE);
				holder.layout_large.setVisibility(View.GONE);
				values.get(pos).setFlag(false);
			}
		});

		holder.btnOff.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				holder.layout_small.setVisibility(View.VISIBLE);
				holder.layout_large.setVisibility(View.GONE);
				values.get(pos).setFlag(false);
			}
		});

		holder.txtCouponName_large.setText(values.get(position).getCouponName());

		holder.txtDate_large.setText(values.get(position).getEndDate());

		holder.txtContent_Large.setText(values.get(position).getCont());

		holder.txtText2Large.setText(values.get(position).getAttentionCont());
		if (values.get(position).getAttentionCont().trim().equals("")) {
			holder.layout_Caution.setVisibility(View.GONE);
		} else {
			holder.layout_Caution.setVisibility(View.VISIBLE);
		}

		imageLoader.DisplayImage(values.get(position).getLinkimage(), holder.img_Coupon, 0, 300);

		holder.img_Coupon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(activity, ImageDialog.class);
				intent.putExtra("image_link", values.get(pos).getLinkimage());
				activity.startActivity(intent);
			}
		});

		holder.btnUseCoupon.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				if (EasyLifeActivity.user != null) {
					if (!isClicked) {
						isClicked = true;
						AsyncTask_AddCoupon task = new AsyncTask_AddCoupon();
						task.execute(item, null, null);
						timer = new Timer();
						timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);
					}

				} else {

					Intent intent = new Intent(activity, Activity_Login.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					intent.putExtra("no", "Activity_Coupon");
					activity.goNextHistory("Activity_Login", intent);

				}

			}
		});
		
		if(openPosition != null && !openPosition.equals("") && Integer.parseInt(openPosition) == pos) {
			openPosition = "";
			holder.layout_small.setVisibility(View.GONE);
			holder.layout_large.setVisibility(View.VISIBLE);
			values.get(pos).setFlag(true);
		}

		return rowView;
	}

	ProgressDialog progressDialog;

	void init_progressDialog() {
		if (!isLoading) {
			isLoading = true;
			if (activity.getParent() != null) {
				progressDialog = ProgressDialog.show(activity.getParent(), "", "Loading...", true, true);
			} else {
				progressDialog = ProgressDialog.show(activity, "", "Loading...", true, true);
			}
			progressDialog.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					if (isLoading) {
						progressDialog.show();
					}
				}
			});
		}

	}

	private class AsyncTask_AddCoupon extends AsyncTask<item_Coupon, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			init_progressDialog();
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(item_Coupon... params) {

			if (isCancelled()) {
				return null;
			}

			service_Coupon_Add xml = new service_Coupon_Add();
			xml.init(params[0].getCouponNo() + "", EasyLifeActivity.user.getUsername(), activity);

			addCouponResult = xml.start();

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			timer.cancel();

			isLoading = false;
			progressDialog.dismiss();

			if (addCouponResult.equals("Y")) {
				Timer t = new Timer();
				t.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						activity.runOnUiThread(new Runnable() {
							public void run() {
								showPopupOneOption(activity.getResources().getString(R.string.popup_Coupon_Added));
							}
						});
					}
				}, 300);

			} else if (addCouponResult.equals("N")) {
				Timer t = new Timer();
				t.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						activity.runOnUiThread(new Runnable() {
							public void run() {
								showPopupOneOption(activity.getResources().getString(R.string.popup_CouponExisted));
							}
						});
					}
				}, 300);

			}

			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {

			timer.cancel();
			isLoading = false;
			progressDialog.dismiss();
			Timer t = new Timer();
			t.schedule(new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					activity.runOnUiThread(new Runnable() {
						public void run() {
							showPopupOneOption(activity.getResources().getString(R.string.No_Response));
						}
					});
				}
			}, 300);
		}
	}

	static class ViewHolder {
		public RelativeLayout layout_small;
		public RelativeLayout layout_large;
		public TextView txtName;
		public Button btnExpand;
		public Button btnOff;
		public TextView txtCouponName_large;
		public TextView txtDate_large;
		public TextView txtContent_Large;
		public LinearLayout layout_Caution;
		public TextView txtText2Large;
		public Button btnUseCoupon;
		public ImageView img_Coupon;

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
					isClicked = false;
					dialog.dismiss();
				}
			});
			dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
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
