package covisoft.android.EasyLife;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import covisoft.android.tab3_Home.Activity_SearchFull;
import covisoft.android.tab3_Home.Home_Activity;
import covisoft.android.tabhost.Activity_Main_1_Favorite;
import covisoft.android.tabhost.Activity_Main_2_MyCoupon;
import covisoft.android.tabhost.Activity_Main_3_Home;
import covisoft.android.tabhost.Activity_Main_4;
import covisoft.android.tabhost.Activity_Main_5_More;
import covisoft.android.tabhost.NavigationGroupActivity;

public class TabpageActivity extends NavigationGroupActivity {

	public static int currentTab = 0;
	public static TabHost mTabHost;
	private static Button btnOne;
	private static Button btnTwo;
	public static Button btnThree;
	public static Button btnSearch;
	private static Button btnFour;
	private static Button btnFive;
	
	public static RelativeLayout rel_Notification;
	public static TextView txt_Number;

	public static LinearLayout LinearLayout_tab;

	public static NavigationGroupActivity activity;
	
	public static Boolean isSearching = false;
	
// ===============================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_tabpage);

		activity = this;

		initTab();
		initButton();
		ChangePage(3, false, false);
	}

	public void initTab() {

		LinearLayout_tab = (LinearLayout) findViewById(R.id.LinearLayout_tab);
		mTabHost = (TabHost) findViewById(R.id.tabHost);
		LocalActivityManager mLocalActivityManager = getLocalActivityManager();
		mTabHost.setup(mLocalActivityManager);

		mTabHost.addTab(mTabHost.newTabSpec("Tab_0").setIndicator("0")
				.setContent(new Intent(this, Activity_Main_3_Home.class)));
		mTabHost.addTab(mTabHost.newTabSpec("Tab_1").setIndicator("1")
				.setContent(new Intent(this, Activity_Main_1_Favorite.class)));
		mTabHost.addTab(mTabHost.newTabSpec("Tab_2").setIndicator("2")
				.setContent(new Intent(this, Activity_Main_2_MyCoupon.class)));
		mTabHost.addTab(mTabHost.newTabSpec("Tab_3").setIndicator("3")
				.setContent(new Intent(this, Activity_Main_4.class)));
		mTabHost.addTab(mTabHost.newTabSpec("Tab_4").setIndicator("4")
				.setContent(new Intent(this, Activity_Main_5_More.class)));

	}

	void initButton() {
		btnOne = (Button) findViewById(R.id.btn_tab_01);
		btnTwo = (Button) findViewById(R.id.btn_tab_02);
		btnThree = (Button) findViewById(R.id.btn_tab_03);
		btnSearch = (Button) findViewById(R.id.btn_tab_03_search);
		btnFour = (Button) findViewById(R.id.btn_tab_04);
		btnFive = (Button) findViewById(R.id.btn_tab_05);

		btnOne.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					currentTab = 1;
					ChangePage(1, false, false);
					break;
				case MotionEvent.ACTION_UP:
					break;
				}
				return false;
			}
		});
		btnTwo.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					currentTab = 2;
					ChangePage(2, false, false);
					break;
				case MotionEvent.ACTION_UP:
					break;
				}
				return false;
			}
		});
		btnThree.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					currentTab = 3;
					ChangePage(3, false, false);

					break;
				case MotionEvent.ACTION_UP:
					break;
				}
				return false;
			}
		});

		btnSearch.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					ChangePage(6, false, false);
					break;
				case MotionEvent.ACTION_UP:
					break;
				}
				return false;
			}
		});

		btnFour.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					currentTab = 4;
					ChangePage(4, false, false);
					break;
				case MotionEvent.ACTION_UP:
					break;
				}
				return false;
			}
		});
		btnFive.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					currentTab = 5;
					ChangePage(5, false, false);
					break;
				case MotionEvent.ACTION_UP:
					break;
				}
				return false;
			}
		});
		
		rel_Notification = (RelativeLayout)findViewById(R.id.rel_Notification);
		rel_Notification.setVisibility(View.GONE);
		txt_Number = (TextView)findViewById(R.id.txt_Number);
	}

	public void clear_History(int tab) {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	public static void ChangePage(int page, Boolean next, Boolean goHome) {
		switch (page) {
		
		case 1:

			System.gc();
			mTabHost.setCurrentTab(1);

			btnOne.setBackgroundResource(R.drawable.footer_post_click);
			btnTwo.setBackgroundResource(R.drawable.footer_my_xml);

			btnThree.setVisibility(View.VISIBLE);
			btnThree.setBackgroundResource(R.drawable.footer_home_xml);
			btnSearch.setVisibility(View.GONE);

			btnFour.setBackgroundResource(R.drawable.footer_job_xml);
			btnFive.setBackgroundResource(R.drawable.footer_association_xml);
			break;
		case 2:
			System.gc();
			mTabHost.setCurrentTab(2);  

			btnOne.setBackgroundResource(R.drawable.footer_post_xml);
			btnTwo.setBackgroundResource(R.drawable.footer_my_click);
			btnThree.setVisibility(View.VISIBLE);
			btnThree.setBackgroundResource(R.drawable.footer_home_xml);
			btnSearch.setVisibility(View.GONE);
			btnFour.setBackgroundResource(R.drawable.footer_job_xml);
			btnFive.setBackgroundResource(R.drawable.footer_association_xml);
			break;

		case 3:
			System.gc();
			if (mTabHost.getCurrentTab() == 0) {
				if (Activity_Main_3_Home.activity.getHistory_aID().size() > 1) {

					Activity_Main_3_Home.activity.goHome();
					btnThree.setVisibility(View.GONE);
					btnThree.setBackgroundResource(R.drawable.footer_home_xml);
					btnSearch.setVisibility(View.VISIBLE);

				} else {

					if (next) {
						Intent intent = new Intent(Home_Activity.navi,Activity_SearchFull.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra("category", 1);
						Home_Activity.navi.goNextHistory("Activity_SearchFull",intent);

						btnThree.setVisibility(View.VISIBLE);
						btnSearch.setVisibility(View.GONE);
					} else {
						Activity_Main_3_Home.activity.goHome();
						btnThree.setVisibility(View.GONE);
						btnThree.setBackgroundResource(R.drawable.footer_home_xml);
						btnSearch.setVisibility(View.VISIBLE);
					}

				}

			} else {
				if(goHome || isSearching) {
					isSearching = false;
					Activity_Main_3_Home.activity.goHome();
					btnThree.setVisibility(View.GONE);
					btnThree.setBackgroundResource(R.drawable.footer_home_xml);
					btnSearch.setVisibility(View.VISIBLE);

				} else {
					if (Activity_Main_3_Home.activity.getHistory_aID().size() > 1) {

						btnThree.setVisibility(View.VISIBLE);
						btnThree.setBackgroundResource(R.drawable.footer_home_click);
						btnSearch.setVisibility(View.GONE);

					} else {

						btnThree.setVisibility(View.GONE);
						btnThree.setBackgroundResource(R.drawable.footer_home);
						btnSearch.setVisibility(View.VISIBLE);

					}
				}
				
			}

			mTabHost.setCurrentTab(0);
			btnOne.setBackgroundResource(R.drawable.footer_post_xml);
			btnTwo.setBackgroundResource(R.drawable.footer_my_xml);

			btnFour.setBackgroundResource(R.drawable.footer_job_xml);
			btnFive.setBackgroundResource(R.drawable.footer_association_xml);
			break;

		case 4:
			System.gc();
			mTabHost.setCurrentTab(3);
			btnOne.setBackgroundResource(R.drawable.footer_post_xml);
			btnTwo.setBackgroundResource(R.drawable.footer_my_xml);

			btnThree.setVisibility(View.VISIBLE);
			btnThree.setBackgroundResource(R.drawable.footer_home_xml);
			btnSearch.setVisibility(View.GONE);

			btnFour.setBackgroundResource(R.drawable.footer_job_click);
			btnFive.setBackgroundResource(R.drawable.footer_association_xml);
			break;
		case 5:
			System.gc();
			mTabHost.setCurrentTab(4);

			btnOne.setBackgroundResource(R.drawable.footer_post_xml);
			btnTwo.setBackgroundResource(R.drawable.footer_my_xml);

			btnThree.setVisibility(View.VISIBLE);
			btnThree.setBackgroundResource(R.drawable.footer_home_xml);
			btnSearch.setVisibility(View.GONE);

			btnFour.setBackgroundResource(R.drawable.footer_job_xml);
			btnFive.setBackgroundResource(R.drawable.footer_association_click);

			break;
		case 6:
			System.gc();
			isSearching = true;
			mTabHost.setCurrentTab(0);
			btnOne.setBackgroundResource(R.drawable.footer_post_xml);
			btnTwo.setBackgroundResource(R.drawable.footer_my_xml);

			Intent intent = new Intent(Home_Activity.navi,Activity_SearchFull.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("category", 1);
			Home_Activity.navi.goNextHistory("Activity_SearchFull",intent);

			btnThree.setVisibility(View.VISIBLE);
			btnThree.setBackgroundResource(R.drawable.footer_home_click);
			btnSearch.setVisibility(View.GONE);
			
			btnFour.setBackgroundResource(R.drawable.footer_job_xml);
			btnFive.setBackgroundResource(R.drawable.footer_association_xml);
			break;
		}
	}

}
