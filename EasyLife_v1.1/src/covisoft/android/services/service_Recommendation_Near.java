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
import covisoft.android.item.item_Recommandation;

public class service_Recommendation_Near {
	ArrayList<item_Recommandation> arItem;
	StringBuffer sb;

	String no = "";
	String coupon = "";
	String categoryNo = "";
	String categoryNoBasic = "";
	String categoryName = "";
	String name = "";
	String addr = "";
	String tel = "";
	String s_logo = "";
	String regvip = "";
	String lat = "";
	String lng = "";
	String level = "";

	public Activity act = null;

	public service_Recommendation_Near(Activity act, String lat, String lng) {
		this.act = act;

		try {
			sb = new StringBuffer();
			sb.append(act.getResources().getString(R.string.service_recommandation_list_Near));
			sb.append("lat").append("=").append(URLEncoder.encode(lat, "UTF-8")).append("&");
			sb.append("lng").append("=").append(URLEncoder.encode(lng, "UTF-8"));

			Log.e("Get recommandation list NEAR", sb.toString());

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<item_Recommandation> start() {
		arItem = new ArrayList<item_Recommandation>();

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

					if (tag_start.compareTo("no") == 0) {
						no = parser.nextText();
					} else if (tag_start.compareTo("coupon") == 0) {
						coupon = parser.nextText();
					} else if (tag_start.compareTo("categoryNo") == 0) {
						categoryNo = parser.nextText();
					} else if (tag_start.compareTo("categoryNoBasic") == 0) {
						categoryNoBasic = parser.nextText();
					} else if (tag_start.compareTo("categoryName") == 0) {
						categoryName = parser.nextText();
					} else if (tag_start.compareTo("name") == 0) {
						name = parser.nextText();
					} else if (tag_start.compareTo("categoryName") == 0) {
						categoryName = parser.nextText();
					} else if (tag_start.compareTo("addr") == 0) {
						addr = parser.nextText();
					} else if (tag_start.compareTo("tel") == 0) {
						tel = parser.nextText();
					} else if (tag_start.compareTo("s_logo") == 0) {
						s_logo = parser.nextText();
					} else if (tag_start.compareTo("regvip") == 0) {
						regvip = parser.nextText();
					} else if (tag_start.compareTo("lat") == 0) {
						lat = parser.nextText();
						if (lat.equals("")) {
							lat = "0.0";
						}
					} else if (tag_start.compareTo("lng") == 0) {
						lng = parser.nextText();
						if (lng.equals("")) {
							lng = "0.0";
						}
					} else if (tag_start.compareTo("level") == 0) {
						level = parser.nextText();
						if(level.equals("")) {
							level = "0";
						}
					}
					break;
				case XmlPullParser.END_TAG:
					String tag_end = parser.getName();
					if (tag_end.compareTo("item") == 0) {
						arItem.add(new item_Recommandation(no, coupon, categoryNo, categoryNoBasic, categoryName, name, addr, tel, s_logo, Double.parseDouble(lat), Double.parseDouble(lng), Integer.parseInt(level)));
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