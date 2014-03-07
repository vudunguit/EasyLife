package covisoft.android.tabhost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import covisoft.android.tab1_Favorite.Activity_Favorite;

public class Activity_Main_1_Favorite extends NavigationGroupActivity {

	public static NavigationGroupActivity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		activity = this;

		Intent intent = new Intent(this, Activity_Favorite.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		View view = getLocalActivityManager().startActivity("Activity_Favorite", intent).getDecorView();
		
		replaceView(view,"Activity_Favorite");
		
	}

	@Override  
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		super.onBackPressed();
		
	}
}
