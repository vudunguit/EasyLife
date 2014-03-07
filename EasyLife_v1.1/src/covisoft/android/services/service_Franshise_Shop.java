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
import covisoft.android.item.item_Franchise_Shop;

public class service_Franshise_Shop {
	ArrayList<item_Franchise_Shop> arItem;
	StringBuffer sb;

	String no = "";
	String coupon = "";
	String name = "";
	String addr = "";
	String tel = "";
	String s_logo = "";
	String lat = "";
	String lng = "";
	public Activity act = null;

	public service_Franshise_Shop(String franchiseNo, Activity act) {
		this.act = act;
		try {
			sb = new StringBuffer();

			sb.append(act.getResources().getString(R.string.service_franchise_shop_list));
			sb.append("franchiseNo").append("=").append(URLEncoder.encode(franchiseNo, "UTF-8"));
			
			Log.e("get franchise shop List", sb.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<item_Franchise_Shop> start() {
		arItem = new ArrayList<item_Franchise_Shop>();

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

					if (tag_start.compareTo("no") == 0) {
						no = parser.nextText();
					} else if (tag_start.compareTo("coupon") == 0) {
						coupon = parser.nextText();
						if(coupon.equals("")) {
							coupon = "0";
						}
					} else if (tag_start.compareTo("name") == 0) {
						name = parser.nextText();
					} else if (tag_start.compareTo("addr") == 0) {
						addr = parser.nextText();
					} else if (tag_start.compareTo("tel") == 0) {
						tel = parser.nextText();
					} else if (tag_start.compareTo("s_logo") == 0) {
						s_logo = parser.nextText();
					} else if (tag_start.compareTo("lat") == 0) {
						lat = parser.nextText();
						if (lat.equals("")) {
							lat = "0";
						}
					} else if (tag_start.compareTo("lng") == 0) {
						lng = parser.nextText();
						if (lng.equals("")) {
							lng = "0";
						}
					}
					break;
				case XmlPullParser.END_TAG:
					String tag_end = parser.getName();
					if (tag_end.compareTo("item") == 0) {
						arItem.add(new item_Franchise_Shop(no, Integer.parseInt(coupon), name, addr, tel, s_logo,
								Double.parseDouble(lat), Double.parseDouble(lng), false));
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