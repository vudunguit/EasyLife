package covisoft.android.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import covisoft.android.EasyLife.R;
import covisoft.android.tabhost.NavigationActivity;

import android.app.Activity;
import android.util.Log;

public class service_QRCoupon_Delete {

	
	StringBuffer sb;
	String result = "";
	public Activity act = null;
	
	public service_QRCoupon_Delete() {
	}

	//http://mobile.subnara.info/api/category.php?category=1
	public void init(NavigationActivity act, String couponID, String username)
	{
		this.act = act;
		try 
		{
			sb = new StringBuffer();
			sb.append(act.getResources().getString(R.string.service_QRCoupon_Delete));
			sb.append("username").append("=").append(URLEncoder.encode(username, "UTF-8")).append("&");
			sb.append("couponId").append("=").append(URLEncoder.encode(couponID, "UTF-8"));
			
			
			Log.e("Delete QRCoupon: username = " + username + " - couponID = " + couponID, sb.toString());
		} 
		catch (UnsupportedEncodingException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String start()
	{
		init_string();
        
		try {
			URL text = new URL(sb.toString());
			
			XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
			XmlPullParser parser = parserCreator.newPullParser();
			
			parser.setInput(text.openStream(), null);
			
			int parserEvent = parser.getEventType();
			while(parserEvent != XmlPullParser.END_DOCUMENT) {
				
				switch(parserEvent) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					String tag_start = parser.getName();
					
					if(tag_start.compareTo("result") == 0) {
						result = parser.nextText();
					}
					
					break;
				case XmlPullParser.END_TAG:
					String tag_end = parser.getName();
					if(tag_end.compareTo("result") == 0) {
						
					}
					break;
				case XmlPullParser.TEXT:
					break;
				}
				parserEvent = parser.next();
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	private void init_string()
	{
		result = "";
	}
}
