package covisoft.android.AR;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import lib.imageLoader.FileCache;
import lib.imageLoader.ImageLoader_Rounded;
import lib.imageLoader.MemoryCache;

import org.mixare.lib.MixUtils;
import org.mixare.lib.gui.PaintScreen;
import org.mixare.lib.gui.TextObj;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.location.Location;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.EasyLife.R;
import covisoft.android.tab3_Home.Activity_Home_List;
import covisoft.android.tab3_Home.Activity_Map_All_Shop;

public class EzLifeMarker extends LocalMarker {

	public static final int MAX_OBJECTS = 20;
	public static final int OSM_URL_MAX_OBJECTS = 5;

	FileCache fileCache;
	int REQUIRED_SIZE = 100;
	public ImageLoader_Rounded imageLoader;
	MemoryCache memoryCache;

	public EzLifeMarker(String id, String title, double latitude, double longitude, double altitude, String URL, int type, int color, String s_logo) {
		super(id, title, latitude, longitude, altitude, URL, type, color);

		imageLoader = new ImageLoader_Rounded(MixView.activity.getApplicationContext());
		memoryCache = new MemoryCache();

	}

	@Override
	public void update(Location curGPSFix) {
		super.update(curGPSFix);
	}

	@Override
	public int getMaxObjects() {
		return MAX_OBJECTS;
	}

	@Override
	public void drawCircle(PaintScreen dw) {
		if (isVisible) {

			Bitmap bm = null;
			if (Activity_Map_All_Shop.parentCategoryNo.equals("1")) {
				bm = BitmapFactory.decodeResource(MixView.activity.getResources(), R.drawable.button01_notext);
			} else if (Activity_Map_All_Shop.parentCategoryNo.equals("4")) {
				bm = BitmapFactory.decodeResource(MixView.activity.getResources(), R.drawable.button06_notext);
			} else if (Activity_Map_All_Shop.parentCategoryNo.equals("8")) {
				bm = BitmapFactory.decodeResource(MixView.activity.getResources(), R.drawable.button03_notext);
			} else if (Activity_Map_All_Shop.parentCategoryNo.equals("9")) {
				bm = BitmapFactory.decodeResource(MixView.activity.getResources(), R.drawable.button04_notext);
			} else if (Activity_Map_All_Shop.parentCategoryNo.equals("5")) {
				bm = BitmapFactory.decodeResource(MixView.activity.getResources(), R.drawable.button08_notext);
			} else if (Activity_Map_All_Shop.parentCategoryNo.equals("10")) {
				bm = BitmapFactory.decodeResource(MixView.activity.getResources(), R.drawable.button07_notext);
			} else if (Activity_Map_All_Shop.parentCategoryNo.equals("11")) {
				bm = BitmapFactory.decodeResource(MixView.activity.getResources(), R.drawable.button06_notext);
			}

			dw.paintBitmap(bm, cMarker.x - 20, cMarker.y);

		}
	}

	public void otherShape(PaintScreen dw) {
		// This is to draw new shape, triangle
		float currentAngle = MixUtils.getAngle(cMarker.x, cMarker.y, signMarker.x, signMarker.y);
		float maxHeight = Math.round(dw.getHeight() / 10f) + 1;

		dw.setColor(getColour());
		float radius = maxHeight / 1.5f;
		dw.setStrokeWidth(dw.getHeight() / 100f);
		dw.setFill(false);

		Path tri = new Path();
		float x = 0;
		float y = 0;
		tri.moveTo(x, y);
		tri.lineTo(x - radius, y - radius);
		tri.lineTo(x + radius, y - radius);

		tri.close();
		dw.paintPath(tri, cMarker.x, cMarker.y, radius * 2, radius * 2, currentAngle + 90, 1);
	}

	@Override
	public void drawTextBlock(PaintScreen dw) {
		float maxHeight = Math.round(dw.getHeight() / 10f) + 1;
		// TODO: change textblock only when distance changes

		String textStr = "";

		double d = distance;
		DecimalFormat df = new DecimalFormat("@#");
		if (d < 1000.0) {
			textStr = title + " (" + df.format(d) + "m)";
		} else {
			d = d / 1000.0;
			textStr = title + " (" + df.format(d) + "km)";
		}

		textBlock = new TextObj(textStr, Math.round(maxHeight / 2f) + 1, 250, dw, underline);

		if (isVisible) {
			// based on the distance set the colour
			if (distance < 100.0) {
				textBlock.setBgColor(Color.argb(128, 52, 52, 52));
				textBlock.setBorderColor(Color.rgb(255, 104, 91));
			} else {
				textBlock.setBgColor(Color.argb(128, 0, 0, 0));
				textBlock.setBorderColor(Color.rgb(255, 255, 255));
			}
			// dw.setColor(DataSource.getColor(type));

			float currentAngle = MixUtils.getAngle(cMarker.x, cMarker.y, signMarker.x, signMarker.y);
			txtLab.prepare(textBlock);
			dw.setStrokeWidth(1f);
			dw.setFill(true);
			dw.paintObj(txtLab, signMarker.x - txtLab.getWidth() / 2, signMarker.y + maxHeight, currentAngle + 90, 1);

		}
	}

	// @Override
	// public boolean fClick(float x, float y, MixContextInterface ctx,
	// MixStateInterface state) {
	// // TODO Auto-generated method stub
	// return false;
	// }
	public Bitmap getBitmap(String url) {
		Bitmap bitmap = null;
		File f = Activity_Home_List.fileCache.getFile(url);

		// from SD cache
		Bitmap b = decodeFile(f);

		if (b != null) {
			Bitmap output = Bitmap.createBitmap(b.getWidth(), b.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, b.getWidth(), b.getHeight());
			final RectF rectF = new RectF(rect);
			final float roundPx = 25;

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(b, rect, rect, paint);

			return output;
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
}
