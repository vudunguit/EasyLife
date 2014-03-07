package covisoft.android.tab3_Home;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import covisoft.android.EasyLife.R;
import covisoft.android.EasyLife.viewFlipper_Photo;
import covisoft.android.tabhost.NavigationActivity;

/*
 * Activity show shop's Image
 * 
 * Updated: 14/06/2013
 * Updater: Huan
 * 
 * Update Info: 
 *      - Delete variable "name" (receive from Home_Item)
 *      - Change layout_Back to local variable
 *      - Delete variable activity, don't use
 *      
 */

public class Activity_Item_Photo extends NavigationActivity {

//	=========================================================================================================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_09_photo);

		Intent i = getIntent();
		ArrayList<String> arItem_pic = i.getStringArrayListExtra("pic");
		
		init_BtnBack();
		init_viewflipper(arItem_pic);
		
	}

	void init_BtnBack() {
		LinearLayout layout_Back = (LinearLayout) findViewById(R.id.layout_Back);
		layout_Back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	void init_viewflipper(ArrayList<String> arItem_pic) {
		viewFlipper_Photo lib = new viewFlipper_Photo();
		lib.arItem = arItem_pic;
		lib.init_ViewFlipper(Activity_Item_Photo.this);
	}

}
