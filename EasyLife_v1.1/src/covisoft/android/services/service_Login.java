package covisoft.android.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.provider.Settings;
import android.util.Log;
import covisoft.android.EasyLife.R;

public class service_Login {

	StringBuffer sb;
	String result = "";
	public Activity act = null;

	public service_Login(Activity act, String userName, String password) {
		this.act = act;
		String deviceId = Settings.System.getString(act.getContentResolver(), Settings.System.ANDROID_ID);

		try {
			sb = new StringBuffer();
			sb.append(act.getResources().getString(R.string.service_login));
			sb.append("username").append("=").append(URLEncoder.encode(userName, "UTF-8")).append("&");
			sb.append("password").append("=").append(URLEncoder.encode(password, "UTF-8")).append("&");
			sb.append("deviceId").append("=").append(URLEncoder.encode(deviceId, "UTF-8"));

			Log.e("Login", sb.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String start() {
		init_string();

		try {
			URL text = new URL(sb.toString());

			XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
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
					String tag_end = parser.getName();
					if (tag_end.compareTo("item") == 0) {

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

	private void init_string() {
		result = "";
	}
}