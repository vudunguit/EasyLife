package covisoft.android.EasyLife;

import java.util.ArrayList;

import lib.imageLoader.ImageLoader;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import covisoft.android.EasyLife.R;
import covisoft.android.item.item_Banner;

public class viewFlipper_Banner {
	
	public static void init(final Activity activity, RelativeLayout banner, ArrayList<item_Banner> arBanner) {

		ImageLoader imageLoader = new ImageLoader(activity.getApplicationContext());

		LinearLayout.LayoutParams lparam_v = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lparam_v.gravity= Gravity.TOP; 
		
		ViewFlipper flipper = new ViewFlipper(activity);
		flipper.setLayoutParams(lparam_v);
		
		if (arBanner != null) {
			final ArrayList<item_Banner> arItem = arBanner;

			int size = arItem.size();

			for (int i = 0; i < size; i++) {
				
				final int pos = i;
				
				LinearLayout.LayoutParams lparam = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				
				lparam.gravity= Gravity.TOP; 
				
				LinearLayout layout_Background = new LinearLayout(activity);
				layout_Background.setLayoutParams(lparam);
				layout_Background.setBackgroundColor(Color.WHITE);
				layout_Background.setGravity(Gravity.CENTER);
				
				
				ImageView imageview = new ImageView(activity);
				imageview.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						
						String url = arItem.get(pos).getLinkUrl();
						if (!url.equals("")) {
							Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
							try {
								activity.startActivity(intent);
							} catch(ActivityNotFoundException e) {
								
								final Dialog dialog;
								dialog = new Dialog(activity,R.style.myBackgroundStyle);
								WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
								lp.copyFrom(dialog.getWindow().getAttributes());
								lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
								lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialog.setContentView(R.layout.popup_one_option);

								TextView txt = (TextView) dialog.findViewById(R.id.txtContent);
								txt.setText(activity.getString(R.string.popup_WrongLink));

								Button btn_OK = (Button) dialog.findViewById(R.id.btn_OK);
								btn_OK.setOnClickListener(new OnClickListener() {

									public void onClick(View v) {
										dialog.dismiss();
									}
								});
								dialog.getWindow().setLayout(
												WindowManager.LayoutParams.WRAP_CONTENT,
												WindowManager.LayoutParams.WRAP_CONTENT);
								dialog.show();
							}
							
						}
					}
				});
				
				imageLoader.DisplayImage(arItem.get(i).getLinkImg(), imageview, Color.TRANSPARENT, EZUtil.REQUIRED_SIZE_MIDDLE);
				
				layout_Background.addView(imageview);
				
				flipper.addView(layout_Background);
			}

			flipper.setFlipInterval(4000);
			flipper.startFlipping();
			
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
			
			
			banner.addView(flipper, params);
		}
	}
}
