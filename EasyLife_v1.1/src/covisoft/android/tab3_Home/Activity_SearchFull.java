package covisoft.android.tab3_Home;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;

import covisoft.android.EasyLife.CheckTimeAsyncTask;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.R;
import covisoft.android.EasyLife.TabpageActivity;
import covisoft.android.adapter.Adapter_Search;
import covisoft.android.item.item_store_list_search;
import covisoft.android.services.service_Search;
import covisoft.android.tabhost.NavigationActivity;

/*
 * Activity search All shop from home page
 * last Updated: 12/06/2013
 * last Updater: Huan
 * 
 * Last Updated: 14.06.2013
 * Last Updater: Huan
 * Last Update Info:
 *           - Update Adapter_Search: navi -> NavigationActivity
 *           
 * Last Updated: 1.07.2013
 * Last Updater: Huan
 * Last Update Info:
 *           - Use EZUtil Progress
 */
public class Activity_SearchFull extends NavigationActivity {

	private Activity activity;

	private LinearLayout layout_Back;

	private EditText editSearch;
	private ToggleButton btnSearchName;
	private ToggleButton btnSearchLocation;
	private ToggleButton btnSearchProduct;
	private Button btnSearch;
	private Button btnClear;

	private String keyword = "";

	private LoadMoreListView listResult_Name;
	private LoadMoreListView listResult_Location;
	private LoadMoreListView listResult_Product;

	private Adapter_Search adapter_Name;
	private Adapter_Search adapter_Location;
	private Adapter_Search adapter_Product;
	private String type = "Name";

	// List all shop from result
	ArrayList<item_store_list_search> arItem = new ArrayList<item_store_list_search>();
	
	// List shop show in list
	ArrayList<item_store_list_search> arValue = new ArrayList<item_store_list_search>();

	Timer timer;

// ==========================================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_search);

		activity = this;

		init();

	}

	public void init() {

		listResult_Name = (LoadMoreListView) findViewById(R.id.listResult_Name);
		listResult_Location = (LoadMoreListView) findViewById(R.id.listResult_Location);
		listResult_Product = (LoadMoreListView) findViewById(R.id.listResult_Product);

		init_btnBack();
		init_listview_Name();
		init_listview_Location();
		init_listview_product();
		init_Toogle();
		init_SearchTool();

	}

	public void init_btnBack() {
		layout_Back = (LinearLayout) findViewById(R.id.layout_Back);
		layout_Back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				onBackPressed();
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		TabpageActivity.btnThree.setVisibility(View.GONE);
		TabpageActivity.btnThree.setBackgroundResource(R.drawable.footer_home);
		TabpageActivity.btnSearch.setVisibility(View.VISIBLE);
		
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);

		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
		super.onPause();
		
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if(editSearch != null) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
		}
		super.onResume();
	}
	
	public void init_Toogle() {
		btnSearchName = (ToggleButton) findViewById(R.id.btnSearchName);
		btnSearchName.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (btnSearchName.isChecked()) {
					arItem.clear();
					arValue.clear();
					btnSearchName.setChecked(true);
					btnSearchLocation.setChecked(false);
					btnSearchProduct.setChecked(false);

					btnSearchName.setEnabled(false);
					btnSearchLocation.setEnabled(true);
					btnSearchProduct.setEnabled(true);
					type = "Name";
					listResult_Name.setVisibility(View.VISIBLE);
					listResult_Location.setVisibility(View.GONE);
					listResult_Product.setVisibility(View.GONE);

					btnClear.performClick();
					init_listview_Name();
				}
			}
		});

		btnSearchLocation = (ToggleButton) findViewById(R.id.btnSearchLocation);
		btnSearchLocation.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (btnSearchLocation.isChecked()) {
					arItem.clear();
					arValue.clear();
					btnSearchLocation.setChecked(true);
					btnSearchName.setChecked(false);
					btnSearchProduct.setChecked(false);

					btnSearchName.setEnabled(true);
					btnSearchProduct.setEnabled(true);
					btnSearchLocation.setEnabled(false);
					type = "Location";
					listResult_Name.setVisibility(View.GONE);
					listResult_Product.setVisibility(View.GONE);
					listResult_Location.setVisibility(View.VISIBLE);

					btnClear.performClick();
					init_listview_Location();
				}
			}
		});

		btnSearchProduct = (ToggleButton) findViewById(R.id.btnSearchProduct);
		btnSearchProduct.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (btnSearchProduct.isChecked()) {
					arItem.clear();
					arValue.clear();
					btnSearchProduct.setChecked(true);
					btnSearchName.setChecked(false);
					btnSearchLocation.setChecked(false);

					btnSearchName.setEnabled(true);
					btnSearchLocation.setEnabled(true);
					btnSearchProduct.setEnabled(false);

					type = "Product";

					listResult_Name.setVisibility(View.GONE);
					listResult_Location.setVisibility(View.GONE);
					listResult_Product.setVisibility(View.VISIBLE);

					btnClear.performClick();
					init_listview_product();
				}
			}
		});
	}

	public void init_SearchTool() {
		btnClear = (Button) findViewById(R.id.btnClear);
		btnClear.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				editSearch.setText("");
				arItem.clear();
				arValue.clear();
				if (adapter_Name != null) {
					adapter_Name.notifyDataSetChanged();
				}
				if (adapter_Location != null) {
					adapter_Location.notifyDataSetChanged();
				}
				if (adapter_Product != null) {
					adapter_Product.notifyDataSetChanged();
				}

			}
		});

		editSearch = (EditText) findViewById(R.id.editSearchContent);
		editSearch.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER) {

						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

						btnSearch.performClick();

					}
				}

				return false;
			}
		});
		editSearch.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (editSearch.getText().toString().length() > 0 && btnClear.getVisibility() == View.GONE) {
					btnClear.setVisibility(View.VISIBLE);
				} else if (editSearch.getText().toString().length() == 0) {
					btnClear.setVisibility(View.GONE);
				}
			}
		});

//		editSearch.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//			public void onFocusChange(View v, boolean hasFocus) {
//				if (hasFocus) {
//					((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(editSearch, InputMethodManager.SHOW_FORCED);
//				} else {
//					init();
//
//				}
//
//			}
//		});

		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);

				if (!EZUtil.isLoading) {

					if (type.equals("Name")) {
						listResult_Name.smoothScrollBy(0, 0);
					} else if (type.equals("Location")) {
						listResult_Location.smoothScrollBy(0, 0);
					} else if (type.equals("Product")) {
						listResult_Product.smoothScrollBy(0, 0);
					}

					keyword = editSearch.getText().toString().trim();
					if (keyword.equals("")) {
						if (btnSearchName.isChecked()) {

							showPopupOneOption(activity.getResources().getString(R.string.popup_CannotSearchName), 1);

						} else if (btnSearchLocation.isChecked()) {

							showPopupOneOption(activity.getResources().getString(R.string.popup_CannotSearchLocation), 1);

						} else if (btnSearchProduct.isChecked()) {

							showPopupOneOption(activity.getResources().getString(R.string.popup_CannotSearchProduct), 1);

						}

					} else {

						if (EZUtil.isNetworkConnected(activity)) {
							arItem.clear();

							AsyncTaskSearch task = new AsyncTaskSearch();
							task.execute();
							timer = new Timer();
							timer.schedule(new CheckTimeAsyncTask(activity, task), EZUtil.DelayTime);

						} else {
							showPopupOneOption(activity.getResources().getString(R.string.No_Internet_now), 1);
						}
					}
				}
			}
		});
	}

	private class AsyncTaskSearch extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			if (getParent() != null) {
				EZUtil.init_progressDialog(getParent());
			} else {
				EZUtil.init_progressDialog(activity);
			}
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params) {

			if (isCancelled()) {
				return null;
			}
			if (type.equals("Name")) {
				service_Search xml = new service_Search(activity, keyword, "name", null);
				arItem = xml.start();
			} else if (type.equals("Location")) {
				service_Search xml = new service_Search(activity, keyword, "location", null);
				arItem = xml.start();
			} else if (type.equals("Product")) {
				service_Search xml = new service_Search(activity, keyword, "product", null);
				arItem = xml.start();
			}

			Collections.sort(arItem, new item_store_list_search.DistanceComparator());

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			timer.cancel();

			EZUtil.cancelProgress();

			if (type.equals("Name")) {
				init_listview_Name();
			} else if (type.equals("Location")) {
				init_listview_Location();
			} else if (type.equals("Product")) {
				init_listview_product();
			}

			if (arItem.size() == 0) {
				Timer t = new Timer();
				t.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						activity.runOnUiThread(new Runnable() {
							public void run() {
								showPopupOneOption(Activity_SearchFull.this.getResources().getString(R.string.popup_Search_EmptyList), 1);
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
			EZUtil.cancelProgress();
			Timer t = new Timer();
			t.schedule(new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					activity.runOnUiThread(new Runnable() {
						public void run() {
							showPopupOneOption(activity.getResources().getString(R.string.No_Response), 1);
						}
					});

				}
			}, 300);
		}
	}

// ===============================================================================================================
	private class LoadMoreDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			if (isCancelled()) {
				return null;
			}

			// Simulates a background task
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			int nextitem = arValue.size() + EZUtil.LISTVIEW_NUMBER_MORE;
			for (int i = arValue.size(); i < nextitem; i++) {
				if (i < arItem.size()) {
					arValue.add(arItem.get(i));
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			if (type.equals("Name")) {
				if (arValue.size() == arItem.size()) {
					listResult_Name.getmFooterView().setVisibility(View.GONE);
				}
				adapter_Name.notifyDataSetChanged();
				listResult_Name.onLoadMoreComplete();

			} else if (type.equals("Location")) {
				if (arValue.size() == arItem.size()) {
					listResult_Location.getmFooterView().setVisibility(View.GONE);
				}
				adapter_Location.notifyDataSetChanged();
				listResult_Location.onLoadMoreComplete();

			} else if (type.equals("Product")) {
				if (arValue.size() == arItem.size()) {
					listResult_Product.getmFooterView().setVisibility(View.GONE);
				}
				adapter_Product.notifyDataSetChanged();
				listResult_Product.onLoadMoreComplete();

			}

			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// Notify the loading more operation has finished

			if (type.equals("Name")) {
				listResult_Name.onLoadMoreComplete();
			} else if (type.equals("Location")) {
				listResult_Location.onLoadMoreComplete();
			} else if (type.equals("Product")) {
				listResult_Product.onLoadMoreComplete();
			}
		}
	}

// =============================================================================================

	private void init_listview_Name() {

		arValue.clear();
		for (int i = 0; i < EZUtil.LISTVIEW_NUMBER_INIT; i++) {
			if (i < arItem.size()) {
				arValue.add(arItem.get(i));
			}
		}
		if (listResult_Name.getAdapter() != null) {

			adapter_Name.notifyDataSetChanged();

		} else {
			adapter_Name = new Adapter_Search(Activity_SearchFull.this, arValue, 2);
			listResult_Name.setAdapter(adapter_Name);
			listResult_Name.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			listResult_Name.setDividerHeight(1);
		}
		if (arValue.size() < arItem.size()) {
			listResult_Name.getmFooterView().setVisibility(View.VISIBLE);
		} else {
			listResult_Name.getmFooterView().setVisibility(View.GONE);
		}

		// set a listener to be invoked when the list reaches the end
		listResult_Name.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				new LoadMoreDataTask().execute();
			}
		});

	}

	private void init_listview_Location() {
		arValue.clear();
		for (int i = 0; i < EZUtil.LISTVIEW_NUMBER_INIT; i++) {
			if (i < arItem.size()) {
				arValue.add(arItem.get(i));
			}
		}
		if (adapter_Location != null) {

			adapter_Location.notifyDataSetChanged();
		} else {
			adapter_Location = new Adapter_Search(Activity_SearchFull.this, arValue, 2);
			listResult_Location.setAdapter(adapter_Location);
			listResult_Location.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			listResult_Location.setDividerHeight(1);
		}
		if (arValue.size() < arItem.size()) {
			listResult_Location.getmFooterView().setVisibility(View.VISIBLE);
		} else {
			listResult_Location.getmFooterView().setVisibility(View.GONE);
		}
		// set a listener to be invoked when the list reaches the end
		listResult_Location.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				new LoadMoreDataTask().execute();
			}
		});
	}

	private void init_listview_product() {
		arValue.clear();
		for (int i = 0; i < EZUtil.LISTVIEW_NUMBER_INIT; i++) {
			if (i < arItem.size()) {
				arValue.add(arItem.get(i));
			}
		}

		if (adapter_Product != null) {

			adapter_Product.notifyDataSetChanged();

		} else {
			adapter_Product = new Adapter_Search(Activity_SearchFull.this, arValue, 2);
			listResult_Product.setAdapter(adapter_Product);
			listResult_Product.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			listResult_Product.setDividerHeight(1);
		}
		if (arValue.size() < arItem.size()) {
			listResult_Product.getmFooterView().setVisibility(View.VISIBLE);
		} else {
			listResult_Product.getmFooterView().setVisibility(View.GONE);
		}
		// set a listener to be invoked when the list reaches the end
		listResult_Product.setOnLoadMoreListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				new LoadMoreDataTask().execute();
			}
		});
	}

// ==========================================================================================================
	public void showPopupOneOption(String content, int type) {

		final int fiType = type;
		if (!EZUtil.isLoading) {
			EZUtil.isLoading = true;

			final Dialog dialog = new Dialog(activity.getParent(), R.style.myBackgroundStyle);
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
					EZUtil.isLoading = false;
					if (fiType == 2) {
						onBackPressed();
					}
				}
			});
		}
	}

}
