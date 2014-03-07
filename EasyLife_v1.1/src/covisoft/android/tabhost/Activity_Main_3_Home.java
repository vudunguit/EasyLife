package covisoft.android.tabhost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import covisoft.android.tab3_Home.Home_Activity;

public class Activity_Main_3_Home extends NavigationGroupActivity {

	public static NavigationGroupActivity activity;
	public static View view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		activity = this;

		Intent intent = new Intent(this, Home_Activity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP	| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		view = getLocalActivityManager().startActivity("Home_Activity", intent).getDecorView();
		replaceView(view, "Home_Activity");

	}

	@Override
	public void onBackPressed() {
		
		super.onBackPressed();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
}
