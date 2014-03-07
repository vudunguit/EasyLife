package lib.imageLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.widget.ImageView;
import covisoft.android.EasyLife.EZUtil;

public class ImageLoader_Rounded {

	MemoryCache memoryCache = new MemoryCache();
	FileCache fileCache;
	private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
	ExecutorService executorService;

	public ImageLoader_Rounded(Context context) {
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
	}

	int noImg_ID = 0;
	int REQUIRED_SIZE = 100;

	public void DisplayImage(String url, ImageView imageView, int noImg_ID,	int REQUIRED_SIZE) {
		
		this.noImg_ID = noImg_ID;
		this.REQUIRED_SIZE = REQUIRED_SIZE;
		
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		
		if (bitmap != null) {
			imageView.setImageBitmap(roundBitmap(bitmap));
		} else {
			queuePhoto(url, imageView);
		}

	}

	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	public Bitmap getBitmap(String url) {
		Bitmap bitmap = null;
		File f = fileCache.getFile(url);

		// from SD cache
		Bitmap b = decodeFile(f);

		//
		if (b != null) {

			return roundBitmap(b);
			
		} else {
			// from web
			try {

				URL imageUrl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
				conn.setConnectTimeout(30000);
				conn.setReadTimeout(30000);
				conn.setInstanceFollowRedirects(true);
				InputStream is = conn.getInputStream();
				OutputStream os = new FileOutputStream(f);
				EZUtil.CopyStream(is, os);
				os.close();
				bitmap = decodeFile(f);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			if (bitmap != null) {
				return bitmap;
			} else {
				return null;
			}
		}

	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.

			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
					break;
				}
					
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			Bitmap bmp = getBitmap(photoToLoad.url);
			memoryCache.put(photoToLoad.url, bmp);
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null) {

				photoToLoad.imageView.setImageBitmap(roundBitmap(bitmap));

			} else if(noImg_ID != 0){
				photoToLoad.imageView.setImageResource(noImg_ID);
			} else {
				photoToLoad.imageView.setVisibility(View.GONE);
			}
				
		}
	}
	
	public Bitmap roundBitmap(Bitmap bitmap) {
	    Paint paintForRound = new Paint();
	    paintForRound.setAntiAlias(true);
	    paintForRound.setColor(0xff424242);
	    paintForRound.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

	    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);

	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	    final RectF rectF = new RectF(rect);

	    canvas.drawARGB(0, 0, 0, 0);
	    paintForRound.setXfermode(null);

	    float roundPx = bitmap.getWidth() > bitmap.getHeight() ? ((float) bitmap.getHeight())/6 : ((float) bitmap.getWidth())/6;
        roundPx = roundPx < 10 ? 10 : roundPx;
        
	    canvas.drawRoundRect(rectF, roundPx, roundPx, paintForRound);

	    paintForRound.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paintForRound);

	    return output;
	}
}
