/*
 * Copyright (C) 2010- Peer internet solutions
 * 
 * This file is part of mixare.
 * 
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details. 
 * 
 * You should have received a copy of the GNU General Public License along with 
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
package covisoft.android.AR.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.mixare.lib.gui.PaintScreen;
import org.mixare.lib.gui.ScreenObj;
import org.mixare.lib.render.MixVector;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import covisoft.android.AR.DataView;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.tab3_Home.Activity_Home_List;

/** Takes care of the small radar in the top left corner and of its points
 * @author daniele
 *
 */
public class ImagePoints implements ScreenObj {
	
	
	int REQUIRED_SIZE = 100;
	
	/** The screen */
	public DataView view;
	/** The radar's range */
	/** Radius in pixel on screen */
	public static float WIDTH = 40;
	public static float HEIGHT = 40;
	/** Position on screen */
	static float originX = 0 , originY = 0;
	
	public String url = "";
	
	
	public ImagePoints(String url) {
		super();
		this.url = url;
	}
	/** Color */
	public MixVector cMarker = new MixVector();
	public void paint(PaintScreen dw) {
		/** radius is in KM. */
		/** Draw the radar */
		dw.setFill(true);
		Bitmap bitmapOrg = getBitmap(url);
		dw.paintBitmap(bitmapOrg, cMarker.x, cMarker.y);
		
	}

	public void rePaint(PaintScreen dw, String url) {
		/** radius is in KM. */
		/** Draw the radar */
		dw.setFill(true);
		this.url = url;
		Bitmap bitmapOrg = getBitmap(url);
		dw.paintBitmap(bitmapOrg, cMarker.x, cMarker.y);
		
	}

	/** Width on screen */
	public float getWidth() {
		return WIDTH;
	}

	/** Height on screen */
	public float getHeight() {
		return HEIGHT;
	}
	
	public Bitmap getBitmap(String url) 
    {
    	Bitmap bitmap=null;
        File f=Activity_Home_List.fileCache.getFile(url);
        
        //from SD cache
        Bitmap b = decodeFile(f);
        
        if(b!=null) {
        	 Bitmap output = Bitmap.createBitmap(b.getWidth(), b
                     .getHeight(), Config.ARGB_8888);
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
        	//from web
            try {
                
                URL imageUrl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(30000);
                conn.setInstanceFollowRedirects(true);
                InputStream is=conn.getInputStream();
                OutputStream os = new FileOutputStream(f);
                EZUtil.CopyStream(is, os);
                os.close();
                bitmap = decodeFile(f);
            } catch (Exception ex){
               ex.printStackTrace();
            }
            if (bitmap != null) {
            	return bitmap;
            } else {
            	return null;
            }
        }
        
    }
	 private Bitmap decodeFile(File f){
	        try {
	            //decode image size
	            BitmapFactory.Options o = new BitmapFactory.Options();
	            o.inJustDecodeBounds = true;
	            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
	            
	            //Find the correct scale value. It should be the power of 2.
	            
	            int width_tmp=o.outWidth, height_tmp=o.outHeight;
	            int scale=1;
	            while(true){
	                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
	                    break;
	                width_tmp/=2;
	                height_tmp/=2;
	                scale*=2;
	            }
	            
	            //decode with inSampleSize
	            BitmapFactory.Options o2 = new BitmapFactory.Options();
	            o2.inSampleSize=scale;
	            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
	        } catch (FileNotFoundException e) {}
	        return null;
    }
}

