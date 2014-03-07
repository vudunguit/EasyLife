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
import covisoft.android.item.item_QRCoupon_detail;

public class service_QRCoupon_Capture {

	item_QRCoupon_detail coupon;
	StringBuffer sb;

	String no = "";
	String categoryNo = "";
	String categoryName = "";
	String gubun = "";
	String couponName = "";
	String franchiseNo = "";
	String companyNo = "";
	String companyName = "";
	String cont = "";
	String regDate = "";
	String useYn = "";
	String startDate = "";
	String endDate = "";
	String attentionCont = "";
	String linkImage = "";
	String status = "";
	String linkImageQR = "";
	String NoKey = "";
	String linkkeyimage = "";
	String openTime = "";
	ArrayList<String> arOpenDate;
	
	public String wrongQRCoupon = "";
	public Activity act = null;

	public service_QRCoupon_Capture(String Username, String shopID, String couponID, String passCode, Activity act) {
		this.act = act;
		try {

			sb = new StringBuffer();
			sb.append(act.getResources().getString(R.string.service_QRCoupon_Capture));
			sb.append("Username").append("=").append(URLEncoder.encode(Username, "UTF-8")).append("&");
			sb.append("ShopID").append("=").append(URLEncoder.encode(shopID, "UTF-8")).append("&");
			sb.append("CouponID").append("=").append(URLEncoder.encode(couponID, "UTF-8")).append("&");
			sb.append("System").append("=").append(URLEncoder.encode("Android", "UTF-8")).append("&");
			sb.append("passCode").append("=").append(URLEncoder.encode(passCode, "UTF-8"));

			Log.e("Send QR", sb.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public item_QRCoupon_detail start() {
		coupon = new item_QRCoupon_detail();
		arOpenDate = new ArrayList<String>();
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
						if(no.equals("")) {
							no = "0";
						}
					} else if (tag_start.compareTo("categoryNo") == 0) {
						categoryNo = parser.nextText();
					} else if (tag_start.compareTo("categoryName") == 0) {
						categoryName = parser.nextText();
					} else if (tag_start.compareTo("gubun") == 0) {
						gubun = parser.nextText();
					} else if (tag_start.compareTo("couponName") == 0) {
						couponName = parser.nextText();
					} else if (tag_start.compareTo("franchiseNo") == 0) {
						franchiseNo = parser.nextText();
					} else if (tag_start.compareTo("companyNo") == 0) {
						companyNo = parser.nextText();
					} else if (tag_start.compareTo("companyName") == 0) {
						companyName = parser.nextText();
					} else if (tag_start.compareTo("cont") == 0) {
						cont = parser.nextText();
					} else if (tag_start.compareTo("regDate") == 0) {
						regDate = parser.nextText();
					} else if (tag_start.compareTo("useYn") == 0) {
						useYn = parser.nextText();
					} else if (tag_start.compareTo("startDate") == 0) {
						startDate = parser.nextText();
					} else if (tag_start.compareTo("endDate") == 0) {
						endDate = parser.nextText();
					} else if (tag_start.compareTo("attentionCont") == 0) {
						attentionCont = parser.nextText();
					} else if (tag_start.compareTo("linkImage") == 0) {
						linkImage = parser.nextText();
					} else if (tag_start.compareTo("status") == 0) {
						status = parser.nextText();
					} else if (tag_start.compareTo("linkImageQR") == 0) {
						linkImageQR = parser.nextText();
					} else if (tag_start.compareTo("NoKey") == 0) {
						NoKey = parser.nextText();
					} else if (tag_start.compareTo("linkkeyimage") == 0) {
						linkkeyimage = parser.nextText();
					} else if (tag_start.compareTo("DateOpen") == 0) {
						openTime = parser.nextText();
					} else if (tag_start.compareTo("WrongQRCoupon") == 0) {
						wrongQRCoupon = parser.nextText();
					}

					break;

				case XmlPullParser.END_TAG:
					String tag_end = parser.getName();
					if (tag_end.compareTo("CouponDate") == 0) {
						coupon.setAttentionCont(attentionCont);
						coupon.setLinkImage(linkImage);
						coupon.setNo(Integer.parseInt(no));
						coupon.setNoKey(Integer.parseInt(NoKey));
						coupon.setArOpenTime(arOpenDate);
						coupon.setCouponName(couponName);
					} else if (tag_end.compareTo("Date") == 0) {
						arOpenDate.add(openTime);
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

		return coupon;
	}
	

}
