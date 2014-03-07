package covisoft.android.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import lib.etc.lib_calculate_distance;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.util.Log;
import covisoft.android.EasyLife.R;
import covisoft.android.item.item_store_list;
import covisoft.android.tab3_Home.Activity_Home_List;
import covisoft.android.tab3_Home.Home_Activity;

public class service_Shop_List {
	ArrayList<covisoft.android.item.item_store_list> arItem;
	StringBuffer sb;

	String no = "";
	String name = "";
	String addr = "";
	String tel = "";
	String lat = "";
	String lng = "";
	String s_logo = "";
	String coupon = "";
	String level = "";
	
	public Activity act = null;

	public service_Shop_List(String category, String latitude,
			String longitude, Activity act) {
		this.act = act;
		try {
			sb = new StringBuffer();
			sb.append(act.getResources().getString(R.string.service_Store_list));
			sb.append("&category").append("=").append(URLEncoder.encode(category, "UTF-8")).append("&");
			sb.append("latitude").append("=").append(URLEncoder.encode(latitude, "UTF-8")).append("&");
			sb.append("longitude").append("=").append(URLEncoder.encode(longitude, "UTF-8"));
			
			Activity_Home_List.url = sb.toString();
			
			Log.e("get list shop", sb + "");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<item_store_list> start() {
		init_string();
		arItem = new ArrayList<item_store_list>();

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
					} else if (tag_start.compareTo("lat") == 0) {
						lat = parser.nextText();
						if(lat.equals("")) {
							lat = "0.0";
						}
					} else if (tag_start.compareTo("lng") == 0) {
						lng = parser.nextText();
						if(lng.equals("")) {
							lng = "0.0";
						}
					} else if (tag_start.compareTo("s_logo") == 0) {
						s_logo = parser.nextText();
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
						if(Home_Activity.latitude != 0 || Home_Activity.longitude != 0) {
							arItem.add(new item_store_list(no, Integer.parseInt(coupon)
									, name
									, addr
									, tel
									, lat
									, lng
									, s_logo
									, lib_calculate_distance.distance(Home_Activity.latitude, Home_Activity.longitude, lat, lng)
									, Integer.parseInt(level)));
						} else {
							arItem.add(new item_store_list(no, Integer.parseInt(coupon)
									, name
									, addr
									, tel
									, lat
									, lng
									, s_logo
									, 0
									, Integer.parseInt(level)));
						}
						
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
		name = "";
		addr = "";
		tel = "";
		lat = "";
		lng = "";
		s_logo = "";
	}
}