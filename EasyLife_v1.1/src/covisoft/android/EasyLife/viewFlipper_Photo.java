package covisoft.android.EasyLife;

import java.util.ArrayList;

import lib.imageLoader.ImageLoader;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import covisoft.android.EasyLife.R;

public class viewFlipper_Photo {

	float xAtDown;
	float xAtUp;
	public ImageLoader imageLoader;

	int size = 0;
	int current_pos = 0;

	public ArrayList<String> arItem = new ArrayList<String>();

	public void init_ViewFlipper(final Activity activity) {

		imageLoader = new ImageLoader(activity.getApplicationContext());

		final ViewFlipper flipper = (ViewFlipper) activity.findViewById(R.id.viewFlipper);
		size = arItem.size();

		init_Text(activity, size, current_pos);

		for (int i = 0; i < size; i++) {

			ImageView imageview = new ImageView(activity);
			imageLoader.DisplayImage(arItem.get(i), imageview, R.drawable.s_05_noimage, EZUtil.REQUIRED_SIZE_MIDDLE);

			flipper.addView(imageview);

		}

		flipper.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					xAtDown = event.getX();
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					xAtUp = event.getX();

					if (xAtUp < xAtDown) {
						flipper.setInAnimation(AnimationUtils.loadAnimation(activity, R.anim.push_left_in));
						flipper.setOutAnimation(AnimationUtils.loadAnimation(activity, R.anim.push_left_out));

						flipper.showNext();

						if (current_pos == size - 1) {
							current_pos = 0;
						} else {
							current_pos++;
						}

						init_Text(activity, size, current_pos);

					} else if (xAtUp > xAtDown) {

						flipper.setInAnimation(AnimationUtils.loadAnimation(activity, R.anim.push_right_in));
						flipper.setOutAnimation(AnimationUtils.loadAnimation(activity, R.anim.push_right_out));

						flipper.showPrevious();

						if (current_pos == 0) {
							current_pos = size - 1;
						} else {
							current_pos--;
						}

						init_Text(activity, size, current_pos);
					}
				}
				return true;
			}

		});

	}

	void init_Text(Activity activity, int size, int position) {
		
		TextView txtNoOfImage = (TextView)activity.findViewById(R.id.txtNoOfImage);
		txtNoOfImage.setText((position+1) + "/" + size);
		
	}
}
