package covisoft.android.tabhost;

import android.content.Intent;
import android.util.Log;
import android.view.View;

public class NavigationActivity extends NavigationGroupActivity {
    
	
	public void goNextHistory(String id,Intent intent)  { //앞으로 가기 처리
        NavigationGroupActivity parent = ((NavigationGroupActivity)getParent());
        View view = parent.group.getLocalActivityManager().startActivity(id,intent).getDecorView();   
        parent.group.replaceView(view, id);
	}
	public void goNextHistory_2(String id,Intent intent)  { //앞으로 가기 처리
        NavigationGroupActivity parent = ((NavigationGroupActivity)getParent());
        View view = parent.group.getLocalActivityManager().startActivity(id,intent).getDecorView();   
        parent.group.changeView(view, id);
	}
	public void goNextHistory_3(String id,Intent intent)  { //앞으로 가기 처리
        NavigationGroupActivity parent = ((NavigationGroupActivity)getParent());
        View view = parent.group.getLocalActivityManager().startActivity(id,intent).getDecorView();   
        parent.group.clearView(view, id);  
	}
	
	@Override
	public void onBackPressed() { //뒤로가기 처리
		Log.e("Navigation Onbackpress", "Navigation");
		covisoft.android.tab3_Home.Activity_QRCode.data = null;
		
		NavigationGroupActivity parent = ((NavigationGroupActivity)getParent());
		if (parent != null) {
			parent.back(); 
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
}
