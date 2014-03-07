package covisoft.android.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.util.Log;
import covisoft.android.EasyLife.R;

public class service_User_UpdateInfo {

	StringBuffer sb;
	
	String result = "";
	
	public Activity act = null;

	public service_User_UpdateInfo(Activity act, String username, String name, String birthday, String gender, String address, String phone) {
		this.act = act;
		
		try {
			sb = new StringBuffer();
			sb.append(act.getResources().getString(R.string.service_UpdateUserInfo));
			sb.append("Username").append("=").append(URLEncoder.encode(username, "UTF-8")).append("&");
			sb.append("name").append("=").append(URLEncoder.encode(name, "UTF-8")).append("&");
			sb.append("birthday").append("=").append(URLEncoder.encode(birthday, "UTF-8")).append("&");
			sb.append("gender").append("=").append(URLEncoder.encode(gender, "UTF-8")).append("&");
			sb.append("address").append("=").append(URLEncoder.encode(address, "UTF-8")).append("&");
			sb.append("phone").append("=").append(URLEncoder.encode(phone, "UTF-8"));
			
			Log.e("Update User's Info", sb.toString());
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public String start() {
		init_string();
		try {
			URL text = new URL(sb.toString());

			XmlPullParserFactory parserCreator = XmlPullParserFactory
					.newInstance();
			XmlPullParser parser = parserCreator.newPullParser();

			parser.setInput(text.openStream(), null);

			int parserEvent = parser.getEventType();
			while (parserEvent != XmlPullParser.END_DOCUMENT) {

				switch (parserEvent) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.END_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					String tag_start = parser.getName();

					if (tag_start.compareTo("result") == 0) {
						result = parser.nextText();
					}
					break;
				case XmlPullParser.END_TAG:
					
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
	private void init_string() {
		result = "";
	}
}