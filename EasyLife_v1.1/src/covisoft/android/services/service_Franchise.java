package covisoft.android.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import covisoft.android.item.item_Franchise;

public class service_Franchise {
	ArrayList<item_Franchise> arItem;
	StringBuffer sb;

	String categoryNoBasic = "";
	String categoryNo = "";
	String categoryName = "";
	String franchiseNo = "";
	String franchiseName = "";
	String s_logo = "";
	public Activity act = null;

	public service_Franchise(Activity act, String category) {
		this.act = act;
		try {
			sb = new StringBuffer();
			sb.append(act.getResources().getString(R.string.service_franchise_List));
			sb.append("category").append("=").append(URLEncoder.encode(category, "UTF-8"));
			Log.e("get franchise home", sb.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<item_Franchise> start() {
		arItem = new ArrayList<item_Franchise>();

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

					if (tag_start.compareTo("categoryNoBasic") == 0) {
						categoryNoBasic = parser.nextText();
					} else if (tag_start.compareTo("categoryNo") == 0) {
						categoryNo = parser.nextText();
					} else if (tag_start.compareTo("categoryName") == 0) {
						categoryName = parser.nextText();
					} else if (tag_start.compareTo("franchiseNo") == 0) {
						franchiseNo = parser.nextText();
					} else if (tag_start.compareTo("franchisename") == 0) {
						franchiseName = parser.nextText();
					} else if (tag_start.compareTo("s_logo") == 0) {
						s_logo = parser.nextText();
					}
					break;
				case XmlPullParser.END_TAG:
					String tag_end = parser.getName();
					if (tag_end.compareTo("item") == 0) {
						arItem.add(new item_Franchise(categoryNoBasic, categoryNo, categoryName, franchiseNo, franchiseName, s_logo));
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