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
import covisoft.android.item.item_total_store_category;

public class service_Category_Quantity {
	ArrayList<item_total_store_category> arItem;
	StringBuffer sb;

	String no = "";
	String total = "";
	public Activity act = null;

	// http://mobile.subnara.info/api/category.php?category=1
	public service_Category_Quantity(Activity act) {
		this.act = act;
		try {
			sb = new StringBuffer();
			sb.append(act.getResources().getString(R.string.service_Total_store_category));
			
			Log.e("get total store", sb.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<item_total_store_category> start() {
		init_string();
		arItem = new ArrayList<item_total_store_category>();

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

					if (tag_start.compareTo("categoryNo") == 0) {
						no = parser.nextText();
					} else if (tag_start.compareTo("totalStore") == 0) {
						total = parser.nextText();
					} else if (tag_start.compareTo("categoryNoRecommand") == 0) {
						no = parser.nextText();
						if(no.equals("")) {
							no = "0";
						}
					} else if (tag_start.compareTo("totalStoreRecommand") == 0) {
						total = parser.nextText();
						if(total.equals("")) {
							total = "0";
						}
					}
					break;
				case XmlPullParser.END_TAG:
					String tag_end = parser.getName();
					if (tag_end.compareTo("totalCategory") == 0) {
						arItem.add(new item_total_store_category(Integer.parseInt(no), Integer.parseInt(total)));
						init_string();
					} else if (tag_end.compareTo("totalCategoryRecommand") == 0) {
						arItem.add(new item_total_store_category(Integer.parseInt(no), Integer.parseInt(total)));
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
		no = "";
		total = "";
	}
}
