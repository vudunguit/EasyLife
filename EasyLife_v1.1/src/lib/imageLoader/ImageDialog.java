package lib.imageLoader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import covisoft.android.EasyLife.R;

public class ImageDialog extends Activity {

	private ImageView image;

	private ImageLoader imageLoader;

	private Activity activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_image);

		activity = this;

		imageLoader = new ImageLoader(activity.getApplicationContext());

		String image_link = getIntent().getStringExtra("image_link");

		image = (ImageView) findViewById(R.id.image);
		image.setClickable(true);
		image.setAdjustViewBounds(true);

		Bitmap tmp = imageLoader.getBitmap(image_link);
		
		image.setImageBitmap(scaleToFitWidth(tmp, 350));
		// finish the activity (dismiss the image dialog) if the user clicks
		// anywhere on the image
		image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}
	
	static public Bitmap scaleToFitWidth(Bitmap b, int width) {
        float factor = width / (float) b.getWidth();
//        return Bitmap.createScaledBitmap(b, width, (int) (b.getHeight() * factor), false);  
        
        Bitmap scaledBitmap = Bitmap.createBitmap(width, (int)(b.getHeight()*factor), Config.ARGB_8888);

        float ratioX = width / (float) b.getWidth();
        float ratioY = b.getHeight()*factor / (float) b.getHeight();
        float middleX = width / 2.0f;
        float middleY = b.getHeight()*factor / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(b, middleX - b.getWidth() / 2, middleY - b.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        
        return scaledBitmap;
    }
}