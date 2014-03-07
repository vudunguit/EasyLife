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
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.R;
import covisoft.android.item.item_QRCoupon_Shop;
import covisoft.android.item.item_QRCoupon_detail;

public class ImageLoader {
	
	Bitmap bitmapOrg;
	Bitmap bitmapOrg2;

	Bitmap resizedBitmap;

	Canvas canvas_main;

	BitmapDrawable bmd;
	MemoryCache memoryCache = new MemoryCache();
	FileCache fileCache;
	private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());

	LinearLayout ln_image;
	ExecutorService executorService;

	public ImageLoader(Context context) {
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
	}

	int noImg_ID = 0;
	int REQUIRED_SIZE = 100;

	public void DisplayImage(String url, ImageView imageView, int noImg_ID, int REQUIRED_SIZE) {

		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null && this.REQUIRED_SIZE == REQUIRED_SIZE) {
			try {
				imageView.setImageBitmap(bitmap);
			} catch (OutOfMemoryError e) {
				imageView.setImageResource(R.drawable.s_05_noimage);
			}

		} else {
			this.REQUIRED_SIZE = REQUIRED_SIZE;
			queuePhoto(url, imageView);
		}

		this.noImg_ID = noImg_ID;
		this.REQUIRED_SIZE = REQUIRED_SIZE;

	}

	public void DisplayImage2(String url, LinearLayout ln, int noImg_ID, int REQUIRED_SIZE, item_QRCoupon_detail coupon) {

		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {

			bmd = new BitmapDrawable(QRInput(bitmap, 50, ln, coupon));
			ln.setBackgroundDrawable(bmd);

		} else {
			queuePhoto2(url, ln, coupon);
		}

		this.noImg_ID = noImg_ID;
		this.REQUIRED_SIZE = REQUIRED_SIZE;
	}
	
	public void DisplayImage3(String url, LinearLayout ln, int noImg_ID, int REQUIRED_SIZE, item_QRCoupon_Shop coupon) {

		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) {

			bmd = new BitmapDrawable(QRInput_2(bitmap, 50, ln, coupon));
			ln.setBackgroundDrawable(bmd);

		} else {
			queuePhoto3(url, ln, coupon);
		}

		this.noImg_ID = noImg_ID;
		this.REQUIRED_SIZE = REQUIRED_SIZE;
	}

	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	private void queuePhoto2(String url, LinearLayout ln, item_QRCoupon_detail coupon) {
		PhotoToLoadQR p = new PhotoToLoadQR(url, ln, coupon);
		executorService.submit(new PhotosLoaderQR(p));
	}
	
	private void queuePhoto3(String url, LinearLayout ln, item_QRCoupon_Shop coupon) {
		PhotoToLoadQR_2 p = new PhotoToLoadQR_2(url, ln, coupon);
		executorService.submit(new PhotosLoaderQR_2(p));
	}

	public Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);

		// from SD cache
		Bitmap b = decodeFile(f);

		if (b != null) {

			return b;
		}
		// from web
		try {
			Bitmap bitmap = null;
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
			return bitmap;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
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
				if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
					break;
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

	private class PhotoToLoadQR {
		public String url;
		public LinearLayout ln;
		item_QRCoupon_detail coupon;

		public PhotoToLoadQR(String u, LinearLayout i, item_QRCoupon_detail coupon) {
			url = u;
			ln = i;
			this.coupon = coupon;
		}
	}
	
	private class PhotoToLoadQR_2 {
		public String url;
		public LinearLayout ln;
		item_QRCoupon_Shop qrcoupon;

		public PhotoToLoadQR_2(String u, LinearLayout i, item_QRCoupon_Shop coupon) {
			url = u;
			ln = i;
			this.qrcoupon = coupon;
		}
	}

	class PhotosLoaderQR implements Runnable {
		PhotoToLoadQR photoToLoad;

		PhotosLoaderQR(PhotoToLoadQR photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		public void run() {
			// if(imageViewReused(photoToLoad))
			// return;
			Bitmap bmp = getBitmap(photoToLoad.url);
			memoryCache.put(photoToLoad.url, bmp);
			// if(imageViewReused(photoToLoad))
			// return;
			BitmapDisplayerQR bd = new BitmapDisplayerQR(bmp, photoToLoad);
			Activity a = (Activity) photoToLoad.ln.getContext();
			a.runOnUiThread(bd);
		}
	}

	class PhotosLoaderQR_2 implements Runnable {
		PhotoToLoadQR_2 photoToLoad;

		PhotosLoaderQR_2(PhotoToLoadQR_2 photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		public void run() {
			// if(imageViewReused(photoToLoad))
			// return;
			Bitmap bmp = getBitmap(photoToLoad.url);
			memoryCache.put(photoToLoad.url, bmp);
			// if(imageViewReused(photoToLoad))
			// return;
			BitmapDisplayerQR_2 bd = new BitmapDisplayerQR_2(bmp, photoToLoad);
			Activity a = (Activity) photoToLoad.ln.getContext();
			a.runOnUiThread(bd);
		}
	}
	
	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	boolean imageViewReused(PhotoToLoadQR photoToLoad) {
		String tag = imageViews.get(photoToLoad.ln);
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
				try {
					photoToLoad.imageView.setImageBitmap(bitmap);
				} catch (OutOfMemoryError e) {
					System.gc();
				}
			} else { 
				try {
					photoToLoad.imageView.setImageResource(noImg_ID);
				} catch (OutOfMemoryError e) {
					System.gc();
				}
			}
		}
	}

	class BitmapDisplayerQR implements Runnable {
		Bitmap bitmap;
		PhotoToLoadQR photoToLoad;

		public BitmapDisplayerQR(Bitmap b, PhotoToLoadQR p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			// if(imageViewReused(photoToLoad))
			// return;
			if (bitmap != null) {
				try {
					bmd = new BitmapDrawable(QRInput(bitmap, 24, photoToLoad.ln, photoToLoad.coupon));
					photoToLoad.ln.setBackgroundDrawable(bmd);
					Log.e("DisplayQR", "Bitmap != null");
				} catch (OutOfMemoryError e) {
					System.gc();
					e.printStackTrace();
				}
			}

			else {
				try {
					// photoToLoad.imageView.setImageResource(noImg_ID);
					bmd = new BitmapDrawable(QRInput(bitmap, 24, photoToLoad.ln, photoToLoad.coupon));
					photoToLoad.ln.setBackgroundDrawable(bmd);
					Log.e("DisplayQR", "Bitmap == null");
				} catch (OutOfMemoryError e) {
					System.gc();
					e.printStackTrace();
				}
			}

		}
	}

	class BitmapDisplayerQR_2 implements Runnable {
		Bitmap bitmap;
		PhotoToLoadQR_2 photoToLoad;

		public BitmapDisplayerQR_2(Bitmap b, PhotoToLoadQR_2 p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			// if(imageViewReused(photoToLoad))
			// return;
			if (bitmap != null) {
				try {
					bmd = new BitmapDrawable(QRInput_2(bitmap, 24, photoToLoad.ln, photoToLoad.qrcoupon));
					photoToLoad.ln.setBackgroundDrawable(bmd);
					Log.e("DisplayQR", "Bitmap != null");
				} catch (OutOfMemoryError e) {
					System.gc();
					e.printStackTrace();
				}
			}

			else {
				try {
					// photoToLoad.imageView.setImageResource(noImg_ID);
					bmd = new BitmapDrawable(QRInput_2(bitmap, 24, photoToLoad.ln, photoToLoad.qrcoupon));
					photoToLoad.ln.setBackgroundDrawable(bmd);
					Log.e("DisplayQR", "Bitmap == null");
				} catch (OutOfMemoryError e) {
					System.gc();
					e.printStackTrace();
				}
			}

		}
	}

	
	// public void clearCache() {
	// memoryCache.clear();
	// fileCache.clear();
	// }

	Bitmap QRInput(Bitmap bitmapOrg, int total, LinearLayout ln, item_QRCoupon_detail coupon) {

		total = coupon.getNoKey();
		bitmapOrg2 = bitmapOrg;

		if (bitmapOrg == null) {
			bitmapOrg = BitmapFactory.decodeResource(ln.getResources(), R.drawable.s_24_noimage);
		}

		Bitmap bitmap_lock = BitmapFactory.decodeResource(ln.getResources(), R.drawable.s_24_key);

		int width = bitmapOrg.getWidth();
		int height = bitmapOrg.getHeight();
		int newWidth = 1200;
		int newHeight = 1200;
		int row_tree = 0;

		// calculate the scale - in this case = 0.4f
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		// createa matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);
		// rotate the Bitmap
		matrix.postRotate(0);

		int posX = 0;
		int posY = 0;
		boolean check = false;

		bitmapOrg = Bitmap.createBitmap(bitmapOrg, posX, posY, width, height, matrix, true);

		width = bitmapOrg.getWidth();
		height = bitmapOrg.getHeight();

		bitmapOrg2 = bitmapOrg;
		canvas_main = new Canvas(bitmapOrg2);

		matrix = new Matrix();
		matrix.postRotate(0);

		int numrow = total / 3;
		if(total == 1) {
			numrow = 1;
		} else if(total == 2) {
			numrow = 2;
		}
		int du = -1;
		du = total % 3;
		int calc = 0;
		int resutl = 0;
		int mod;

		for (int i = 0; i < total; i++) {
			
			mod = width % numrow;
			calc = (width / numrow);
			resutl = calc + mod;

			int rowHeight = 0;
			if(total == 0 || total == 1 || total == 2) {
				rowHeight = height;
			} else {
				rowHeight = height / 3;
			}
			resizedBitmap = Bitmap.createBitmap(bitmapOrg, posX, posY, width / numrow, rowHeight, matrix, true);
			if (i == total - 1)
				resizedBitmap = Bitmap.createBitmap(bitmapOrg, posX, posY, resutl, rowHeight, matrix, true);
			if (coupon.getArOpenTime().size() != 0 && i < coupon.getArOpenTime().size()) {
				// if (coupon.getArOpenTime().get(i)!=null)
				// {
				if(total != 1) {
					resizedBitmap = toborder(resizedBitmap, width / 4, rowHeight);
					
				}
				resizedBitmap = draw_text(resizedBitmap, coupon.getArOpenTime().get(i));
				mixall(resizedBitmap, posX, posY, posX + width / numrow, posY + rowHeight);
				// }

			} else {
				resizedBitmap = draw_lock(resizedBitmap, width / numrow, rowHeight, bitmap_lock, bitmap_lock.getWidth(), bitmap_lock.getHeight());
				resizedBitmap = toGrayscale(resizedBitmap);
				if(total != 1) {
					resizedBitmap = toborder(resizedBitmap, width / numrow, rowHeight);
				}
				
				mixall(resizedBitmap, posX, posY, posX + width / numrow, posY + rowHeight);
			}

			posX += width / numrow;
			if (posX + 10 >= width) {
				row_tree += 1;
				posX = 0;
				posY += rowHeight;
				if (row_tree == 2) {
					numrow = numrow + du;
					check = true;
				}
			}

			if (row_tree == 3) {
				if (check == false)
					posX += width / numrow + du;
				else
					check = false;

				if (posX + 10 >= width) {
					posX = 0;
					posY += rowHeight;
				}
			}

		}

		return bitmapOrg2;

	}


	Bitmap QRInput_2(Bitmap bitmapOrg, int total, LinearLayout ln, item_QRCoupon_Shop qrcoupon) {

		total = qrcoupon.getNoofkey();
		bitmapOrg2 = bitmapOrg;

		if (bitmapOrg == null) {
			bitmapOrg = BitmapFactory.decodeResource(ln.getResources(), R.drawable.s_24_noimage);
		}

		int width = bitmapOrg.getWidth();
		int height = bitmapOrg.getHeight();
		int newWidth = 1200;
		int newHeight = 1200;
		int row_tree = 0;

		// calculate the scale - in this case = 0.4f
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		// createa matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);
		// rotate the Bitmap
		matrix.postRotate(0);

		int posX = 0;
		int posY = 0;
		boolean check = false;

		bitmapOrg = Bitmap.createBitmap(bitmapOrg, posX, posY, width, height, matrix, true);

		width = bitmapOrg.getWidth();
		height = bitmapOrg.getHeight();

		bitmapOrg2 = bitmapOrg;
		canvas_main = new Canvas(bitmapOrg2);

		matrix = new Matrix();
		matrix.postRotate(0);

		int numrow = total / 3;
		
		if(total == 1) {
			numrow = 1;
		} else if(total == 2) {
			numrow = 2;
		}
		int du = -1;
		du = total % 3;
		int calc = 0;
		int resutl = 0;
		int mod;

		for (int i = 0; i < total; i++) {
			mod = width % numrow;
			calc = (width / numrow);
			resutl = calc + mod;

			int rowHeight = 0;
			if(total == 0 || total == 1 || total == 2) {
				rowHeight = height;
			} else {
				rowHeight = height / 3;
			}
			
			resizedBitmap = Bitmap.createBitmap(bitmapOrg, posX, posY, width / numrow, rowHeight, matrix, true);
			if (i == total - 1) {
				resizedBitmap = Bitmap.createBitmap(bitmapOrg, posX, posY, resutl, rowHeight, matrix, true);
			}
			if(total != 1) {
				resizedBitmap = toborder(resizedBitmap, width / numrow, rowHeight);
			}
			
			mixall(resizedBitmap, posX, posY, posX + width / numrow, posY + rowHeight);

			posX += width / numrow;
			if (posX + 10 >= width) {
				row_tree += 1;
				posX = 0;
				posY += rowHeight;
				if (row_tree == 2) {
					numrow = numrow + du;
					check = true;
				}
			}

			if (row_tree == 3) {
				if (check == false)
					posX += width / numrow + du;
				else
					check = false;

				if (posX + 10 >= width) {
					posX = 0;
					posY += rowHeight;
				}
			}

		}

		return bitmapOrg2;

	}
	
	public Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);

		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	public Bitmap toborder(Bitmap bmpOriginal, int width, int height) {

		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		Canvas canvas = new Canvas(bmpOriginal);
		Paint p = new Paint();

		p.setARGB(255, 0, 0, 0);
		p.setStyle(Style.STROKE);
		p.setPathEffect(new DashPathEffect(new float[] { 10, 20 }, 0));

		p.setStrokeWidth(5f);
		p.setColor(Color.WHITE);
		canvas.drawRoundRect(new RectF(0, 0, width + 10, height + 10), 0f, 0f, p);
		return bmpOriginal;

	}

	public Bitmap draw_lock(Bitmap bmpOriginal, int width, int height, Bitmap bmplock, int width_lock, int height_lock) {
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();
		width_lock = 88;
		height_lock = 113;
		Canvas canvas = new Canvas(bmpOriginal);

		canvas.drawBitmap(bmplock, null, new RectF(width / 2 - width_lock / 2, height / 2 - height_lock / 2, width / 2 + width_lock / 2, height / 2 + height_lock / 2), null);
		return bmpOriginal;

	}

	public Bitmap draw_text(Bitmap bmpOriginal, String date) {
		
		String str[] = date.split(" ");
		int height = bmpOriginal.getHeight();
		
		Canvas canvas = new Canvas(bmpOriginal);

		Paint paint = new Paint();
		paint.setColor(Color.BLACK);

		paint.setStyle(Style.FILL);
		paint.setTextSize(50);

		canvas.drawText(str[0], 8, height / 2, paint);

		canvas.drawText(str[1], 8, height / 2 + 50, paint);
		return bmpOriginal;

	}

	public Bitmap mixall(Bitmap bit, float left, float top, float right, float bottom) {

		// int width = bit.getWidth();
		canvas_main.drawBitmap(bit, null, new RectF(left, top, left + bit.getWidth(), bottom), null);
		return bitmapOrg2;
	}

}
