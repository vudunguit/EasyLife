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

public class service_Favorite_Del {

	StringBuffer sb;
	String result = "";
	public Activity act = null;

	public service_Favorite_Del() {
	}

	public void init(String favoriteNo, Activity act) {
		this.act = act;
		try {
			sb = new StringBuffer();
			sb.append(act.getResources().getString(R.string.service_Favorite_Del));
			sb.append("favoriteNo").append("=").append(URLEncoder.encode(favoriteNo, "UTF-8"));

			Log.e("Del Favorite", sb.toString());
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
					if (tag_end.compareTo("result") == 0) {

						init_string();
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
