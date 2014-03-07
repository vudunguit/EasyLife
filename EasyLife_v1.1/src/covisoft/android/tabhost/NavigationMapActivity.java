package covisoft.android.tabhost;

import android.content.Intent;
import android.view.View;

import com.google.android.maps.MapActivity;

public class NavigationMapActivity extends MapActivity {
    
	public void goNextHistory(String id,Intent intent)  { //앞으로 가기 처리
        NavigationGroupActivity parent = ((NavigationGroupActivity)getParent());
        View view = parent.group.getLocalActivityManager().startActivity(id,intent).getDecorView();   
        parent.group.replaceView(view, id);
	}

	@Override
	public void onBackPressed() { //뒤로가기 처리
          NavigationGroupActivity parent = ((NavigationGroupActivity)getParent());
          parent.back();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
