package covisoft.android.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.util.Log;
import covisoft.android.EasyLife.R;
import covisoft.android.item.item_Manual;

public class service_Manual {
	ArrayList<item_Manual> arItem;
	StringBuffer sb;

	String id = "";
	String title = "";
	String link = "";
	public Activity act = null;

	public service_Manual(Activity act) {
		this.act = act;
		sb = new StringBuffer();
		sb.append(act.getResources().getString(R.string.service_manual));
		Log.e("get Manual", sb.toString());
	}

	public ArrayList<item_Manual> start() {
		init_string();
		arItem = new ArrayList<item_Manual>();

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

					if (tag_start.compareTo("id") == 0) {
						id = parser.nextText();
					} else if (tag_start.compareTo("title") == 0) {
						title = parser.nextText();
					} else if (tag_start.compareTo("link") == 0) {
						link = parser.nextText();
					}
					break;
				case XmlPullParser.END_TAG:
					String tag_end = parser.getName();
					if (tag_end.compareTo("item") == 0) {
						arItem.add(new item_Manual(id, title, link));
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
		return arItem;
	}

	private void init_string() {
		id = "";
		title = "";
		link = "";
	}
}