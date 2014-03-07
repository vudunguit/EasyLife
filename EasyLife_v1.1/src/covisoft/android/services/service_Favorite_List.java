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
import covisoft.android.item.item_store_list_fav;
import covisoft.android.tab3_Home.Activity_Home_List;
import covisoft.android.tab3_Home.Home_Activity;

public class service_Favorite_List {

	ArrayList<covisoft.android.item.item_store_list_fav> arItem_Normal;
	ArrayList<covisoft.android.item.item_store_list_fav> arItem_Franchise;
	ArrayList<covisoft.android.item.item_store_list_alba> arItem_Job;

	StringBuffer sb;

	String favoriteNo = "";
	String no = "";
	String coupon = "";
	String name = "";
	String categoryNoBasic = "";
	String categoryName = "";
	String type = "";
	String addr = "";
	String tel = "";
	String lat = "";
	String lng = "";
	String s_logo = "";

	String title = "";
	String companyName = "";
	String email = "";
	String regDate = "";
	String cont = "";

	public Activity act = null;

	public service_Favorite_List(String userName, Activity act) {
		this.act = act;
		try {
			sb = new StringBuffer();
			sb.append(act.getResources().getString(R.string.service_Favorite_List));
			sb.append("username").append("=").append(URLEncoder.encode(userName, "UTF-8"));
			Activity_Home_List.url = sb.toString();
			
			Log.e("get Favorite List", sb + "");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<item_store_list_fav> start_Normal() {
		init_string();
		arItem_Normal = new ArrayList<item_store_list_fav>();

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

					if (tag_start.compareTo("favoriteNo") == 0) {
						favoriteNo = parser.nextText();
					} else if (tag_start.compareTo("no") == 0) {
						no = parser.nextText();
					} else if (tag_start.compareTo("coupon") == 0) {
						coupon = parser.nextText();
						if(coupon.equals("")) {
							coupon = "0";
						}
					} else if (tag_start.compareTo("name") == 0) {
						name = parser.nextText();
					} else if (tag_start.compareTo("categoryNoBasic") == 0) {
						categoryNoBasic = parser.nextText();
					} else if (tag_start.compareTo("categoryName") == 0) {
						categoryName = parser.nextText();
					} else if (tag_start.compareTo("type") == 0) {
						type = parser.nextText();
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
					}

					break;
				case XmlPullParser.END_TAG:
					String tag_end = parser.getName();
					if (tag_end.compareTo("item") == 0) {

						if (type.equals("0") || type.equals("1")) {
							if(Home_Activity.latitude != 0 || Home_Activity.longitude != 0) {
								arItem_Normal.add(new item_store_list_fav(favoriteNo
										, no
										, Integer.parseInt(coupon)
										, name
										, categoryNoBasic
										, categoryName
										, type
										, addr
										, tel
										, lat
										, lng
										, s_logo
										, lib_calculate_distance.distance(Home_Activity.latitude,Home_Activity.longitude, lat, lng)));
							} else {
								arItem_Normal.add(new item_store_list_fav(favoriteNo
										, no
										, Integer.parseInt(coupon)
										, name
										, categoryNoBasic
										, categoryName
										, type
										, addr
										, tel
										, lat
										, lng
										, s_logo
										, 0));
							}
							
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

		return arItem_Normal;

	}

	public ArrayList<item_store_list_fav> start_Franchise() {
		init_string();
		arItem_Franchise = new ArrayList<item_store_list_fav>();

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

					if (tag_start.compareTo("favoriteNo") == 0) {
						favoriteNo = parser.nextText();
					} else if (tag_start.compareTo("no") == 0) {
						no = parser.nextText();
					} else if (tag_start.compareTo("coupon") == 0) {
						coupon = parser.nextText();
						if(coupon.equals("")) {
							coupon = "0";
						}
					} else if (tag_start.compareTo("name") == 0) {
						name = parser.nextText();
					} else if (tag_start.compareTo("categoryNoBasic") == 0) {
						categoryNoBasic = parser.nextText();
					} else if (tag_start.compareTo("categoryName") == 0) {
						categoryName = parser.nextText();
					} else if (tag_start.compareTo("type") == 0) {
						type = parser.nextText();
					} else if (tag_start.compareTo("addr") == 0) {
						addr = parser.nextText();
					} else if (tag_start.compareTo("tel") == 0) {
						tel = parser.nextText();
					} else if (tag_start.compareTo("lat") == 0) {
						lat = parser.nextText();
					} else if (tag_start.compareTo("lng") == 0) {
						lng = parser.nextText();
					} else if (tag_start.compareTo("s_logo") == 0) {
						s_logo = parser.nextText();
					}

					break;
				case XmlPullParser.END_TAG:
					String tag_end = parser.getName();
					if (tag_end.compareTo("item") == 0) {

						if (type.equals("1")) {
							if(Home_Activity.latitude != 0 || Home_Activity.longitude != 0) {
								arItem_Franchise.add(new item_store_list_fav(favoriteNo
										, no
										, Integer.parseInt(coupon)
										, name
										, categoryNoBasic
										, categoryName
										, type
										, addr
										, tel
										, lat
										, lng
										, s_logo
										, lib_calculate_distance.distance( Home_Activity.latitude, Home_Activity.longitude, lat, lng)));
							} else {
								arItem_Franchise.add(new item_store_list_fav(favoriteNo
										, no
										, Integer.parseInt(coupon)
										, name
										, categoryNoBasic
										, categoryName
										, type
										, addr
										, tel
										, lat
										, lng
										, s_logo
										, 0));
							}
							
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

		return arItem_Franchise;
	}

	public ArrayList<item_store_list_alba> start_Job() {
		init_string();
		arItem_Job = new ArrayList<item_store_list_alba>();

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
					} else if (tag_start.compareTo("lng") == 0) {
						lng = parser.nextText();
					} else if (tag_start.compareTo("s_logo") == 0) {
						s_logo = parser.nextText();
					}

					break;
				case XmlPullParser.END_TAG:
					String tag_end = parser.getName();
					if (tag_end.compareTo("item") == 0) {

						if (type.equals("2")) {
							if(Home_Activity.latitude != 0 || Home_Activity.longitude != 0 ) {
								arItem_Job.add(new item_store_list_alba(favoriteNo
										, no
										, title
										, companyName
										, email
										, categoryNoBasic
										, categoryName
										, regDate
										, type
										, addr
										, tel
										, cont
										, lat
										, lng
										, s_logo
										, lib_calculate_distance.distance( Home_Activity.latitude, Home_Activity.longitude, lat, lng)));
							} else {
								arItem_Job.add(new item_store_list_alba(favoriteNo
										, no
										, title
										, companyName
										, email
										, categoryNoBasic
										, categoryName
										, regDate
										, type
										, addr
										, tel
										, cont
										, lat
										, lng
										, s_logo
										, 0.0));
							}
							

							
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

		return arItem_Job;
	}

	private void init_string() {
		favoriteNo = "";
		no = "";
		coupon = "";
		name = "";
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