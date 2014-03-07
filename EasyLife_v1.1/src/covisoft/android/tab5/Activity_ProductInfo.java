package covisoft.android.tab5;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import covisoft.android.EasyLife.R;
import covisoft.android.tabhost.NavigationActivity;

public class Activity_ProductInfo extends NavigationActivity {

	private Activity activity;

	// ===================================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_info);

		activity = this;

		init();
	}

	public void init() {
		initBtnBack();

		initBtnManual();
		initBtnComment();
		initBtnVote();
		initBtnFacebook();
		initBtnZingMe();

	}

	public void initBtnBack() {
		LinearLayout layoutBack = (LinearLayout) findViewById(R.id.layout_Back);
		layoutBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}

	public void initBtnManual() {
		Button btnManual = (Button) findViewById(R.id.btnManual);
		btnManual.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(activity, Activity_Manual_List.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				goNextHistory("Activity_Manual_List", intent);
			}
		});
	}

	public void initBtnComment() {
		Button btnComment = (Button) findViewById(R.id.btnComment);
		btnComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(activity, Activity_Comment.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				goNextHistory("Activity_Comment", intent);
			}
		});
	}

	public void initBtnVote() {
		Button btnVote = (Button) findViewById(R.id.btnVote);
		btnVote.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				try {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "covisoft.android.EasyLife")));
				} catch (android.content.ActivityNotFoundException anfe) {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + "covisoft.android.EasyLife")));
				}

			}
		});
	}

	public void initBtnFacebook() {
		Button btnFacebook = (Button) findViewById(R.id.btnFacebook);
		btnFacebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent fbIntent;
				try {
					activity.getPackageManager().getPackageInfo("covisoft.android.EasyLife", 0);
					fbIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/492557340803199"));
				} catch (Exception e) {
					fbIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/easylifevn"));
				}
				startActivity(fbIntent);
			}
		});
	}

	public void initBtnZingMe() {
		Button btnZingMe = (Button) findViewById(R.id.btnZingMe);
		btnZingMe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String url = "http://me.zing.vn/u/ezlife";
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					activity.startActivity(intent);
				} catch(ActivityNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
