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
import android.util.Log;

import covisoft.android.EasyLife.R;

public class service_Shop_Comment {

	private String flag = "";
	private StringBuffer sb;

	public service_Shop_Comment(Activity activity, String boardNo, String cont, String goodNum, String boardName, String user) {
		super();
		try {
			sb = new StringBuffer();
			sb.append(activity.getString(R.string.service_comment));
			sb.append("boardName").append("=").append(URLEncoder.encode(boardName, "UTF-8")).append("&");
			sb.append("cont").append("=").append(URLEncoder.encode(cont, "UTF-8")).append("&");
			sb.append("goodNum").append("=").append(URLEncoder.encode(goodNum, "UTF-8")).append("&");
			sb.append("boardNo").append("=").append(URLEncoder.encode(boardNo, "UTF-8")).append("&");
			sb.append("user").append("=").append(URLEncoder.encode(user, "UTF-8"));
			
			Log.e("comment", sb.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String start() {
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

					if (tag_start.compareTo("flag") == 0) {
						flag = parser.nextText();
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
		
		return flag;
	}
}
