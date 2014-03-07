package covisoft.android.tab3_Home;

import java.util.ArrayList;

import lib.imageLoader.ScrollTextView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import covisoft.android.EasyLife.R;
import covisoft.android.adapter.Adapter_Coupon;
import covisoft.android.item.item_Coupon;
import covisoft.android.item.item_store_list;
import covisoft.android.tabhost.NavigationActivity;

/*
 * Activity show list coupon of shop. Tab 3 (Home Item -> list coupon)
 * 
 * last Updated: 13/06/2013
 * last Updater: Huan
 * 
 */
public class Activity_Coupon extends NavigationActivity {

	private LinearLayout layout_Back;
	
	private String position;
	private ListView listCoupon;
	private Adapter_Coupon adapter;
	private ArrayList<item_Coupon> arCoupon = new ArrayList<item_Coupon>();
	private Boolean expand = false;
	
	private Button btnAllCoupon;
	private ScrollTextView txtName;

	public item_store_list item;

//==============================================================================================================
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_13_coupon);

		Intent intent = getIntent();
		item = (item_store_list) intent.getSerializableExtra("item");
		arCoupon = (ArrayList<item_Coupon>) intent.getSerializableExtra("couponList");
		position = intent.getStringExtra("position");
		
		init();

	}

	public void init() {
		initBtnBack();
		initListView();
		initBtnAll();

		txtName = (ScrollTextView) findViewById(R.id.txtName);
		txtName.setText(item.name);
	}

	public void initListView() {

		listCoupon = (ListView) findViewById(R.id.listCoupon);
		adapter = new Adapter_Coupon(this, arCoupon, position);
		listCoupon.setAdapter(adapter);
	}

	public void initBtnBack() {
		layout_Back = (LinearLayout) findViewById(R.id.layout_Back);
		layout_Back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				for (int i = 0; i < arCoupon.size(); i++) {
					arCoupon.get(i).setFlag(false);
				}
				onBackPressed();
			}
		});
	}

	public void initBtnAll() {
		btnAllCoupon = (Button) findViewById(R.id.btnAllCoupon);
		btnAllCoupon.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				int size = arCoupon.size();
				int checkExpand = 0;
				int checkNotExpand = 0;

				for (int i = 0; i < arCoupon.size(); i++) {
					if (arCoupon.get(i).isFlag()) {
						checkExpand++;
					}
				}
				for (int i = 0; i < arCoupon.size(); i++) {
					if (!arCoupon.get(i).isFlag()) {
						checkNotExpand++;
					}
				}

				if (size == checkExpand) {
					for (int i = 0; i < arCoupon.size(); i++) {
						arCoupon.get(i).setFlag(false);
					}
					expand = false;
				} else if (size == checkNotExpand) {
					for (int i = 0; i < arCoupon.size(); i++) {
						arCoupon.get(i).setFlag(true);
					}

					expand = true;
				} else {
					if (!expand) {
						for (int i = 0; i < arCoupon.size(); i++) {
							arCoupon.get(i).setFlag(true);
						}

						expand = true;
					} else {
						for (int i = 0; i < arCoupon.size(); i++) {
							arCoupon.get(i).setFlag(false);
						}
						expand = false;
					}
				}
				adapter.notifyDataSetChanged();
			}
		});
	}
}
