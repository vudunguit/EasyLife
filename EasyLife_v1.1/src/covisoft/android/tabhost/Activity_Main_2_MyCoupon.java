package covisoft.android.tabhost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import covisoft.android.tab2_MyCoupon.Activity_ListCoupon;

public class Activity_Main_2_MyCoupon extends NavigationGroupActivity{
	
	public static NavigationGroupActivity activity;
//	public static String currentView = "";
	public View view;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
        
        activity = this;
        
    	Intent intent = new Intent(this, Activity_ListCoupon.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_SINGLE_TOP);
        view = getLocalActivityManager().startActivity("Activity_ListCoupon",intent).getDecorView();
        
        replaceView(view , "Activity_ListCoupon");
        
    }
	
	@Override
	public void onBackPressed() {
	// TODO Auto-generated method stub
		super.onBackPressed();
	}
}
