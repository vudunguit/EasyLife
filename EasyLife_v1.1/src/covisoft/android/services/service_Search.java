package covisoft.android.services;

import java.io.IOException;
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
import covisoft.android.item.item_store_list_search;
import covisoft.android.tab3_Home.Home_Activity;

/*
 * Service use for search shop
 * last Updated: - 12/06/2013
 *               - Combine 3 services search (search_all, search, search_product) -> 1 service: search
 * last Updater: HuanHuynh              
 */
public class service_Search {
	ArrayList<item_store_list_search> arItem;
	StringBuffer sb;

	String no = "";
	String coupon = "";
	String categoryNoBasic = "";
	String name = "";
	String addr = "";
	String tel = "";
	String lat = "";
	String lng = "";
	String s_logo = "";
	
	public Activity act = null;
	public service_Search(Activity activity, String keyword, String searchType, String category) {
		
		this.act = activity;
		
		try {
			/*
			 * Check if category is null: It use for search in Home (FULL)
			 *          category # null : It use for search in Category
			 */
			if(category == null) {   
				if (searchType.equals("product")) {
					sb = new StringBuffer();
					sb.append(act.getResources().getString(R.string.service_Store_list_search_product));
					sb.append("keyword").append("=").append(URLEncoder.encode(keyword, "UTF-8"));

				} else if (searchType.equals("name") || searchType.equals("location")) {
					sb = new StringBuffer();
					sb.append(act.getResources().getString(R.string.service_Store_list_search_all));
					sb.append("&column").append("=").append(URLEncoder.encode(searchType, "UTF-8")).append("&");
					sb.append("keyword").append("=").append(URLEncoder.encode(keyword, "UTF-8"));
					
				}
				
				if(searchType.equals("product")) {
					Log.e("search Product FULL", sb.toString());
				} else if(searchType.equals("name")) {
					Log.e("Search Name FULL", sb.toString());
				} else if(searchType.equals("location")) {
					Log.e("Search Location FULL", sb.toString());
				}
					
			} else {
				if (searchType.equals("product")) {
					sb = new StringBuffer();
					sb.append(act.getResources().getString(R.string.service_Store_list_search_product));
					sb.append("keyword").append("=").append(URLEncoder.encode(keyword, "UTF-8")).append("&");
					sb.append("categoryNo").append("=").append(URLEncoder.encode(category, "UTF-8"));

				} else if (searchType.equals("name") || searchType.equals("location")) {
					sb = new StringBuffer();
					sb.append(act.getResources().getString(R.string.service_Store_list_search));
					sb.append("&column").append("=").append(URLEncoder.encode(searchType, "UTF-8")).append("&");
					sb.append("category").append("=").append(URLEncoder.encode(category, "UTF-8")).append("&");
					sb.append("keyword").append("=").append(URLEncoder.encode(keyword, "UTF-8"));

				}
				
				if(searchType.equals("product")) {
					Log.e("search Product", sb.toString());
				} else if(searchType.equals("name")) {
					Log.e("Search Name", sb.toString());
				} else if(searchType.equals("location")) {
					Log.e("Search Location", sb.toString());
				}
			}
		} catch (Exception e) {
		}
		
	}

	public ArrayList<item_store_list_search> start() {
		init_string();
		arItem = new ArrayList<item_store_list_search>();

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
						if(coupon.equals("")) {
							coupon = "0";
						}
					} else if (tag_start.compareTo("categoryNoBasic") == 0) {
						categoryNoBasic = parser.nextText();
					} else if (tag_start.compareTo("name") == 0) {
						name = parser.nextText();
					} else if (tag_start.compareTo("addr") == 0) {
						addr = parser.nextText();
					} else if (tag_start.compareTo("tel") == 0) {
						tel = parser.nextText();
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

						if (Home_Activity.latitude != 0 || Home_Activity.longitude != 0) {
							arItem.add(new item_store_list_search(no, Integer.parseInt(coupon), categoryNoBasic, name, addr, tel, lat, lng, s_logo, lib_calculate_distance.distance(Home_Activity.latitude, Home_Activity.longitude, lat, lng)));
						} else {
							arItem.add(new item_store_list_search(no, Integer.parseInt(coupon), categoryNoBasic, name, addr, tel, lat, lng, s_logo, 0));
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