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

import android.app.Activity;
import android.util.Log;

public class service_User_Feedback {

	StringBuffer sb;
	String result = "";
	public Activity act = null;

	public service_User_Feedback(Activity act, String name, String email, String phone, String title, String content) {
		this.act = act;
		try {
			sb = new StringBuffer();
			sb.append(act.getResources().getString(R.string.service_UserReport));
			sb.append("title").append("=").append(URLEncoder.encode(title, "UTF-8")).append("&");
			sb.append("name").append("=").append(URLEncoder.encode(name, "UTF-8")).append("&");
			sb.append("phone").append("=").append(URLEncoder.encode(phone, "UTF-8")).append("&");
			sb.append("content").append("=").append(URLEncoder.encode(content, "UTF-8")).append("&");
			sb.append("email").append("=").append(URLEncoder.encode(email, "UTF-8"));
			
			Log.e("User Comment EZ", sb.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
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
					String tag_end = parser.getName();
					if (tag_end.compareTo("root") == 0) {

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