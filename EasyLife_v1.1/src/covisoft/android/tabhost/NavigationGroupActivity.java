package covisoft.android.tabhost;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.LocalActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import covisoft.android.EasyLife.R;
import covisoft.android.EasyLife.TabpageActivity;
import covisoft.android.tab5.Activity_MyProfile_Tab5;

public class NavigationGroupActivity extends ActivityGroup {
	ArrayList<String> history_aID;

	public static Activity activity;
	
	Dialog dialog;

	private SharedPreferences settings;
	private SharedPreferences.Editor editor;
	
	public NavigationGroupActivity group; // Activity���� �����ϱ� ���� Group

	public ArrayList<String> getHistory_aID() {
		return history_aID;
	}

	public void setHistory_aID(ArrayList<String> history_aID) {
		this.history_aID = history_aID;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		history_aID = new ArrayList<String>();
		group = this;

		changeLaguage();
		
		activity = this;
		
	}
	
	public void changeLaguage() {
		
		settings = getSharedPreferences("language", MODE_WORLD_WRITEABLE);
		String text = "";
		text = settings.getString("language", text);
		
		if(text.equals("") || text == null) {
			text = "vn";
			
			settings = getSharedPreferences("language", MODE_WORLD_WRITEABLE);
			editor = settings.edit();
			editor.putString("language", text);
			editor.commit();
		}
		
		Locale locale = new Locale(text);
		Resources res = getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		Configuration config = new Configuration();
		config.locale = locale;
		res.updateConfiguration(config, dm);
		getApplicationContext().getResources().updateConfiguration(config, null);
	}

	public void changeView(View v, String activityId) { // ������ Level��
														// Activity�� �ٸ�
														// Activity�� �����ϴ�
														// ���

		if (history_aID.size() > 0) {
			history_aID.remove(history_aID.size() - 1);
		}
		history_aID.add(activityId);
		setContentView(v);
	}

	public void replaceView(View v, String activityId) {
		history_aID.add(activityId);
		setContentView(v);
	}

	public void clearView(View v, String activityId) {
		history_aID.clear();
		history_aID.add(activityId);
		setContentView(v);
	}

	public void goHome() {
		if (history_aID.size() > 0) {
			if (this.equals(Activity_Main_3_Home.activity)) {
				for (int i = history_aID.size() - 1; i > -1; i--) {
					history_aID.remove(i);
				}
			}

		}
		if (this.equals(Activity_Main_3_Home.activity)) {
			Activity_Main_3_Home.activity.replaceView(Activity_Main_3_Home.view, "Home_Activity");
		}
	}

	public void back() {
		Log.e("Navigation Onbackpress", "NavigationGroup");
		System.gc();
		TabpageActivity.LinearLayout_tab.setVisibility(View.VISIBLE);
		if (history_aID.size() > 1) {
			history_aID.remove(history_aID.size() - 1);

			LocalActivityManager manager = getLocalActivityManager();
			String lastId = history_aID.get(history_aID.size() - 1);
			Log.e("lastID", lastId);
			Intent lastIntent = manager.getActivity(lastId).getIntent();

			Window newWindow;
			if (lastId.equals("Activity_Home_Item") || lastId.equals("Activity_SubCategory") || lastId.equals("Activity_Home_List") || lastId.equals("Activity_Favorite") || lastId.equals("Activity_Search") || lastId.equals("Activity_SearchFull") || lastId.equals("Activity_RecommendationList") || lastId.equals("Activity_Franchise_Category") || lastId.equals("Activity_Franchise") || lastId.equals("Activity_Franchise_Shop")) {
				newWindow = manager.startActivity(lastId, lastIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
			} else {
				newWindow = manager.startActivity(lastId, lastIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			}

			setContentView(newWindow.getDecorView());

			if (TabpageActivity.mTabHost.getCurrentTab() == 0 && history_aID.size() == 1) {
				TabpageActivity.btnThree.setVisibility(View.GONE);
				TabpageActivity.btnThree.setBackgroundResource(R.drawable.footer_home_xml);
				TabpageActivity.btnSearch.setVisibility(View.VISIBLE);
			}
		} else {
			if (this.equals(Activity_Main_4.activity)) {
				TabpageActivity.ChangePage(3, false, false);
			} else if (this.equals(Activity_Main_1_Favorite.activity)) {
				TabpageActivity.ChangePage(3, false, false);
			} else if (this.equals(Activity_Main_2_MyCoupon.activity)) {
				TabpageActivity.ChangePage(3, false, false);
			} else if (this.equals(Activity_Main_5_More.activity)) {
				TabpageActivity.ChangePage(3, false, false);
			} else {

				dialog = new Dialog(this, R.style.myBackgroundStyle);
				WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
				lp.copyFrom(dialog.getWindow().getAttributes());
				lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
				lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.popup_two_option);

				TextView txt = (TextView) dialog.findViewById(R.id.txtContent);
				txt.setText(getString(R.string.popup_Quit));

				Button btn_OK = (Button) dialog.findViewById(R.id.btn_OK);
				btn_OK.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						dialog.dismiss();
						System.gc();
						clearApplicationCache(null);
						finish();

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
			System.gc();
			clearApplicationCache(null);
		}

	}

	@Override
	public void onBackPressed() { // Back Key�� ���� Event Handler
		group.back();
		return;
	}

	DialogInterface.OnClickListener Exit_Alert = new DialogInterface.OnClickListener() {

		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			if (which == DialogInterface.BUTTON1) {
				System.gc();
				clearApplicationCache(null);
				finish();
			}
		}
	};

	// ��������� ĳ�� �����
	private void clearApplicationCache(java.io.File dir) {
		if (dir == null)
			dir = getCacheDir();
		else
			;
		if (dir == null)
			return;
		else
			;
		java.io.File[] children = dir.listFiles();
		try {
			for (int i = 0; i < children.length; i++)
				if (children[i].isDirectory())
					clearApplicationCache(children[i]);
				else
					children[i].delete();
		} catch (Exception e) {
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		am.restartPackage(getPackageName());
		clearApplicationCache(null);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
	    if (resultCode == Activity.RESULT_OK) 
	    {
	        switch(requestCode)
	        {
	        case 1 : // global variable to indicate camera result
	        	Activity_MyProfile_Tab5 activity = (Activity_MyProfile_Tab5) getLocalActivityManager().getCurrentActivity();
	            activity.onActivityResult(requestCode, resultCode, data);
	        	break;
	        case 2 : // global variable to indicate camera result
	        	Activity_MyProfile_Tab5 activity1 = (Activity_MyProfile_Tab5) getLocalActivityManager().getCurrentActivity();
	            activity1.onActivityResult(requestCode, resultCode, data);
	        	break;
	        case 3 : // global variable to indicate camera result
	        	Activity_MyProfile_Tab5 activity2 = (Activity_MyProfile_Tab5) getLocalActivityManager().getCurrentActivity();
	            activity2.onActivityResult(requestCode, resultCode, data);
	        	break;
	        }

	    }
	}
	
}
