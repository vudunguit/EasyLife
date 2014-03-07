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
import covisoft.android.item.item_Coupon;

public class service_Coupon_List {
	ArrayList<item_Coupon> arItem;
	StringBuffer sb;

	String no = "";
	String categoryNo = "";
	String categoryName = "";
	String gubun = "";
	String couponNo = "";
	String couponName = "";
	String linkimage = "";
	String cont = "";
	String companyNo = "";
	String companyName = "";
	String hp = "";
	String useDate = "";
	Boolean useYn = false;
	String couponNum = "";
	String startDate;
	String endDate;
	String attentionCont = "";
	int idcouponkind = 0;
	String addr = "";
	String lat = "";
	String lng = "";
	String downUpfile1 = "";
	String residual_day = "";

	public Activity act = null;

	public service_Coupon_List(String username, Activity act) {
		this.act = act;
		try {

			sb = new StringBuffer();
			sb.append(act.getResources().getString(R.string.service_couponList));

			sb.append("username").append("=").append(URLEncoder.encode(username, "UTF-8"));

			Log.e("get NormalCoupon List of " + username, sb.toString());

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ArrayList<item_Coupon> start() {
		arItem = new ArrayList<item_Coupon>();

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
					} else if (tag_start.compareTo("categoryNo") == 0) {
						categoryNo = parser.nextText();
						if(categoryNo.equals("")) {
							categoryNo = "0";
						}
					} else if (tag_start.compareTo("categoryName") == 0) {
						categoryName = parser.nextText();
					} else if (tag_start.compareTo("gubun") == 0) {
						gubun = parser.nextText();
						if(gubun.equals("")) {
							gubun = "0";
						}
					} else if (tag_start.compareTo("couponNo") == 0) {
						couponNo = parser.nextText();
					} else if (tag_start.compareTo("couponName") == 0) {
						couponName = parser.nextText();
					} else if (tag_start.compareTo("linkimage") == 0) {
						linkimage = parser.nextText();
					} else if (tag_start.compareTo("cont") == 0) {
						cont = parser.nextText();
					} else if (tag_start.compareTo("companyNo") == 0) {
						companyNo = parser.nextText();
						if(companyNo.equals("")) {
							companyNo = "0";
						}
					} else if (tag_start.compareTo("companyName") == 0) {
						companyName = parser.nextText();
					} else if (tag_start.compareTo("hp") == 0) {
						hp = parser.nextText();
					} else if (tag_start.compareTo("useDate") == 0) {
						useDate = parser.nextText();

					} else if (tag_start.compareTo("useYn") == 0) {
						String useYnstr = parser.nextText();

						if (useYnstr.equals("Y")) {
							useYn = true;
						} else {
							useYn = false;
						}
					} else if (tag_start.compareTo("couponNum") == 0) {
						couponNum = parser.nextText();
						if(couponNum.equals("")) {
							couponNum = "0";
						}
					} else if (tag_start.compareTo("startDate") == 0) {
						startDate = parser.nextText();
					} else if (tag_start.compareTo("endDate") == 0) {
						endDate = parser.nextText();
					} else if (tag_start.compareTo("attentionCont") == 0) {
						attentionCont = parser.nextText();
					} else if (tag_start.equals("idcouponkind")) {
						try {
							idcouponkind = Integer.parseInt(parser.nextText());
						} catch (Exception e) {
							idcouponkind = 0;
						}
					} else if (tag_start.compareTo("addr") == 0) {
						addr = parser.nextText();
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
					} else if (tag_start.compareTo("downUpfile1") == 0) {
						downUpfile1 = parser.nextText();
					} else if (tag_start.compareTo("residual_day") == 0) {
						residual_day = parser.nextText();
						if(residual_day.equals("")) {
							residual_day = "0";
						}
					}
					break;
				case XmlPullParser.END_TAG:
					String tag_end = parser.getName();
					if (tag_end.compareTo("item") == 0) {
						arItem.add(new item_Coupon(no, Integer.parseInt(categoryNo), categoryName, Integer.parseInt(gubun), couponNo, couponName, linkimage, cont, Integer.parseInt(companyNo), companyName, hp, useDate, useYn, Integer.parseInt(couponNum), startDate, endDate, attentionCont, idcouponkind, addr, Double.parseDouble(lat), Double.parseDouble(lng),
								downUpfile1, Integer.parseInt(residual_day), true));
						
						initString();
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
	
	public void initString() {
		no = "";
		categoryNo = "";
		categoryName = "";
		gubun = "";
		couponNo = "";
		couponName = "";
		linkimage = "";
		cont = "";
		companyNo = "";
		companyName = "";
		hp = "";
		useDate = "";
		useYn = false;
		couponNum = "";
		startDate = "";
		endDate = "";
		attentionCont = "";
		idcouponkind = 0;
		addr = "";
		lat = "";
		lng = "";
		downUpfile1 = "";
		residual_day = "";
	}

}