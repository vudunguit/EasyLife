package covisoft.android.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.util.Log;
import covisoft.android.EasyLife.R;
import covisoft.android.item.item_Banner;

public class service_Banner {
	ArrayList<item_Banner> arItem;
	StringBuffer sb;

	String title;
	String bannerkind;
	String linkurl;
	String description;
	String idCompany;
	String linkimage;

	public Activity act = null;

	public service_Banner(Activity act, String token) {
		this.act = act;

		try {
			sb = new StringBuffer();
			sb.append(act.getResources().getString(R.string.service_banner));
			sb.append("token").append("=").append(URLEncoder.encode(token, "UTF-8"));

			Log.e("get banner", sb.toString());

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public ArrayList<item_Banner> start() {
		arItem = new ArrayList<item_Banner>();

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

					if (tag_start.compareTo("title") == 0) {
						title = parser.nextText();
					} else if (tag_start.compareTo("bannerkind") == 0) {
						bannerkind = parser.nextText();
					} else if (tag_start.compareTo("linkurl") == 0) {
						linkurl = parser.nextText();
					} else if (tag_start.compareTo("description") == 0) {
						description = parser.nextText();
					} else if (tag_start.compareTo("idCompany") == 0) {
						idCompany = parser.nextText();
					} else if (tag_start.compareTo("linkimage") == 0) {
						linkimage = parser.nextText();
					}
					break;
				case XmlPullParser.END_TAG:
					String tag_end = parser.getName();
					if (tag_end.compareTo("item") == 0) {
						arItem.add(new item_Banner(title, bannerkind, linkurl, description, idCompany, linkimage));
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

		return arItem;
	}

}