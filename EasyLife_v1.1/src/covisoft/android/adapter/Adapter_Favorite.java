package covisoft.android.adapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import lib.imageLoader.ImageLoader_Rounded;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.R;
import covisoft.android.item.item_store_list_fav;
import covisoft.android.services.service_Favorite_Del;
import covisoft.android.tab3_Home.Activity_Home_Item;
import covisoft.android.tabhost.NavigationActivity;

/*
 * Last Updated: 17.06.2013
 * Last Updater: Huan
 * Update Info:
 *        - Navigation navi + Activity activity = NavigationActivity activity
 *        - Use ViewHolder to keep information
 *        - Change imageLoder and inflater to Local Variable
 */

public class Adapter_Favorite extends BaseAdapter {

	private final ArrayList<item_store_list_fav> values;
	private NavigationActivity activity;
	private String delResult = "";
	private Dialog dialog;
	private Boolean isLoading = false;

// ===============================================================================================
	public Adapter_Favorite(NavigationActivity activity, ArrayList<item_store_list_fav> values) {
		this.activity = activity;
		this.values = values;
	}

	public int getCount() {
		return values.size();
	}

	public item_store_list_fav getItem(int position) {
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
		
		ImageLoader_Rounded imageLoader = new ImageLoader_Rounded(activity.getApplicationContext());
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		final int pos = position;
		final ViewHolder holder;

		View rowView = convertView;
		if (rowView == null) {
			// inflating the row
			rowView = inflater.inflate(R.layout.listview_item_screen_11, parent, false);
			holder = new ViewHolder();
			
			holder.image = (ImageView) rowView.findViewById(R.id.img);
			holder.txtName = (TextView) rowView.findViewById(R.id.txtName);
			holder.txtAddress = (TextView) rowView.findViewById(R.id.txtAddress);
			holder.txtDistance = (TextView) rowView.findViewById(R.id.txtDistance);
			holder.btnPhone = (Button) rowView.findViewById(R.id.btnPhone);
			holder.btnDelete = (Button) rowView.findViewById(R.id.btnDelete);
			holder.layout = (LinearLayout) rowView.findViewById(R.id.layout_1);
			
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}
		
		imageLoader.DisplayImage(values.get(position).getS_logo(), holder.image, R.drawable.s_05_noimage, 40);

		holder.txtName.setText(values.get(position).getName());

		holder.txtAddress.setText(values.get(position).getAddr());


		if (values.get(position).getDistance() == 0) {
			holder.txtDistance.setVisibility(View.GONE);
		} else {
			holder.txtDistance.setText(String.format("%.2fkm", values.get(position).getDistance()));
		}

		if(values.get(pos).getTel().trim().equals("")) {
			holder.btnPhone.setBackgroundResource(R.drawable.s_05phone_none);
		} else {
			holder.btnPhone.setBackgroundResource(R.drawable.s_05phone);
		}
		holder.btnPhone.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String s_tel = values.get(pos).getTel();
				if (!s_tel.equals("")) {
					Uri number = Uri.parse("tel:" + s_tel);
					Intent intent_tel = new Intent(Intent.ACTION_DIAL, number);
					intent_tel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					activity.startActivity(intent_tel);
				} else {
					showPopupOneOption(activity.getResources().getString(R.string.popup_NoPhoneNumber));
				}
			}
		});
		holder.btnDelete.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {

				dialog = new Dialog(activity.getParent(), R.style.myBackgroundStyle);
				WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
				lp.copyFrom(dialog.getWindow().getAttributes());
				lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
				lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.popup_two_option);

				TextView txt = (TextView) dialog.findViewById(R.id.txtContent);
				txt.setText(v.getContext().getString(R.string.popup_Favourite_Delete));

				Button btn_OK = (Button) dialog.findViewById(R.id.btn_OK);
				btn_OK.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						dialog.dismiss();
						init_thread_Del_Favorite(values.get(pos).getFavoriteNo(), pos);
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

		holder.layout.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(activity, Activity_Home_Item.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("act", EZUtil.db_act_favorite);
				intent.putExtra("shopType", values.get(pos).getType());
				intent.putExtra("no", values.get(pos).getNo());
				intent.putExtra("CategoryName", values.get(pos).getCategoryName());
				intent.putExtra("parentCategoryNo", values.get(pos).getCategoryNoBasic());
				activity.goNextHistory("Activity_Home_Item", intent);

			}
		});
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

	// ----------- Del favorite
	void init_thread_Del_Favorite(String no, int pos) {
		final int posi = pos;
		init_progressDialog();
		Handler mHandler = new Handler() {
			public void handleMessage(Message msg) {
				isLoading = false;
				progressDialog.dismiss();
				if (delResult.equals("1")) {

					values.remove(posi);
					notifyDataSetChanged();

					Timer t = new Timer();
					t.schedule(new TimerTask() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							activity.runOnUiThread(new Runnable() {
								public void run() {
									showPopupOneOption(activity.getString(R.string.popup_Favourite_Del_Success));
								}
							});
						}
					}, 300);

				} else if (delResult.equals("0")) {

					Timer t = new Timer();
					t.schedule(new TimerTask() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							activity.runOnUiThread(new Runnable() {
								public void run() {
									showPopupOneOption(activity.getString(R.string.popup_Favourite_Del_Fail));
								}
							});
						}
					}, 300);
				}
			}
		};

		InitThread_Del_Favorite thread = new InitThread_Del_Favorite();
		thread.no = no;
		thread.mHandler = mHandler;
		thread.setDaemon(true);
		thread.start();
	}

	class InitThread_Del_Favorite extends Thread {
		Handler mHandler;
		String no;

		public void run() {
			delResult = "";

			service_Favorite_Del xml = new service_Favorite_Del();
			xml.init(no, activity);
			delResult = xml.start();

			mHandler.sendEmptyMessage(0);
		}
	}

	static class ViewHolder {
		public ImageView image;
		public TextView txtName;
		public TextView txtAddress;
		public TextView txtDistance;
		public Button btnPhone;
		public Button btnDelete;
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
