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
import covisoft.android.item.item_store_list_alba;
import covisoft.android.tab3_Home.Home_Activity;

public class service_RecruitShop_List {
	ArrayList<item_store_list_alba> arItem;
	StringBuffer sb;

	String favoriteNo = "";
	String no = "";
	String title = "";
	String categoryNoBasic = "";
	String categoryName = "";
	String type = "";
	String cont = "";
	String companyName = "";
	String tel = "";
	String s_logo = "";
	String email = "";
	String regDate = "";
	public String addr = "";
	public String lat = "";
	public String lng = "";
	public Activity act = null;

	public service_RecruitShop_List(String category, String latitude, String longitude, Activity act) {
		this.act = act;
		try {
			sb = new StringBuffer();
			sb.append(act.getResources().getString(R.string.service_Store_list_alba));
			sb.append("category").append("=").append(URLEncoder.encode(category, "UTF-8")).append("&");
			sb.append("latitude").append("=").append(URLEncoder.encode(latitude, "UTF-8")).append("&");
			sb.append("longitude").append("=").append(URLEncoder.encode(longitude, "UTF-8"));
			Log.e("get list shop", sb + "");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<item_store_list_alba> start() {
		init_string();
		arItem = new ArrayList<item_store_list_alba>();

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

					if (tag_start.compareTo("favoriteNo") == 0) {
						favoriteNo = parser.nextText();
					} else if (tag_start.compareTo("no") == 0) {
						no = parser.nextText();
					} else if (tag_start.compareTo("title") == 0) {
						title = parser.nextText();
					} else if (tag_start.compareTo("companyName") == 0) {
						companyName = parser.nextText();
					} else if (tag_start.compareTo("email") == 0) {
						email = parser.nextText();
					} else if (tag_start.compareTo("categoryNoBasic") == 0) {
						categoryNoBasic = parser.nextText();
					} else if (tag_start.compareTo("categoryName") == 0) {
						categoryName = parser.nextText();
					} else if (tag_start.compareTo("regDate") == 0) {
						regDate = parser.nextText();
					} else if (tag_start.compareTo("type") == 0) {
						type = parser.nextText();
					} else if (tag_start.compareTo("addr") == 0) {
						addr = parser.nextText();
					} else if (tag_start.compareTo("tel") == 0) {
						tel = parser.nextText();
					}

					// ------- Danh riêng cho Rao Vat
					else if (tag_start.compareTo("cont") == 0) {
						cont = parser.nextText();
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
					} else if (tag_start.compareTo("s_logo") == 0) {
						s_logo = parser.nextText();
					}

					break;
				case XmlPullParser.END_TAG:
					String tag_end = parser.getName();
					if (tag_end.compareTo("item") == 0) {

						arItem.add(new item_store_list_alba(favoriteNo, no, title, companyName, email, categoryNoBasic, categoryName, regDate, type, addr, tel, cont, lat, lng, s_logo, lib_calculate_distance.distance(Home_Activity.latitude, Home_Activity.longitude, lat, lng)));
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
		favoriteNo = "";
		no = "";
		categoryNoBasic = "";
		categoryName = "";
		type = "";
		addr = "";
		tel = "";
		lat = "";
		lng = "";
		s_logo = "";

		title = "";
		companyName = "";
		email = "";
		regDate = "";
		cont = "";

	}
}