package covisoft.android.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.util.Log;
import covisoft.android.EasyLife.R;
import covisoft.android.item.item_QRCoupon_list;

public class service_QRCoupon_List {

	ArrayList<item_QRCoupon_list> arItem;
	StringBuffer sb;

	String no = "";
	String nameStore = "";
	String address1 = "";
	String address2 = "";
	String address3 = "";
	String address4 = "";
	String startDate = "";
	String endDate = "";
	Date sDate;
	Date eDate;
	String linkImageQR = "";
	public Activity act = null;

	public service_QRCoupon_List(Activity act, String username) {
		this.act = act;
		try {
			sb = new StringBuffer();
			sb.append(act.getResources().getString(R.string.service_QRCoupon_List));
			sb.append("Username").append("=").append(URLEncoder.encode(username, "UTF-8"));
			
			Log.e("get QRCoupon list", sb.toString());

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ArrayList<item_QRCoupon_list> start() {
		arItem = new ArrayList<item_QRCoupon_list>();

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
						if(no.equals("")) {
							no = "0";
						}
					} else if (tag_start.compareTo("nameStore") == 0) {
						nameStore = parser.nextText();
					} else if (tag_start.compareTo("addr1") == 0) {
						address1 = parser.nextText();
					} else if (tag_start.compareTo("addr2") == 0) {
						address2 = parser.nextText();
					} else if (tag_start.compareTo("addr3") == 0) {
						address3 = parser.nextText();
					} else if (tag_start.compareTo("addr4") == 0) {
						address4 = parser.nextText();
					} else if (tag_start.compareTo("startDate") == 0) {
						startDate = parser.nextText();
					} else if (tag_start.compareTo("endDate") == 0) {
						endDate = parser.nextText();
					} else if (tag_start.compareTo("linkImageQR") == 0) {
						linkImageQR = parser.nextText();
					}

					break;
				case XmlPullParser.END_TAG:
					String tag_end = parser.getName();
					if (tag_end.compareTo("couponList") == 0) {

						arItem.add(new item_QRCoupon_list(Integer.parseInt(no),
								nameStore, address1, address2, address3,
								address4, startDate, endDate, linkImageQR

						));
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
