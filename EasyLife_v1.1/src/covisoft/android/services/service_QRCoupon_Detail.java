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

public class service_QRCoupon_Detail {

	item_QRCoupon_detail coupon;
	StringBuffer sb;

	public String error = "";
	
	String no = "0";
	String categoryNo = "0";
	String categoryName = "";
	String gubun = "0"; // ---------
	String couponName = "";
	String franchiseNo = "0";
	String companyNo = "0";
	String companyName = "";
	String addr = "";
	String cont = "";
	String regDate = "";
	String useYn = "";
	String startDate = "";
	String endDate = "";
	String attentionCont = "";
	String linkImage = "";
	String status = "";
	String linkImageQR = "";
	String NoKey = "0";
	String openTime;
	ArrayList<String> arOpenDate;
	public Activity act = null;

	public service_QRCoupon_Detail() {

	}

	public void init(String couponID, String username, Activity act) {

		this.act = act;
		try {
			sb = new StringBuffer();
			sb.append(act.getResources().getString(R.string.service_QRCoupon_Detail));
			sb.append("CouponId").append("=").append(URLEncoder.encode(couponID, "UTF-8")).append("&");
			sb.append("Username").append("=").append(URLEncoder.encode(username, "UTF-8"));
			
			Log.e("get QRCoupon detail", sb.toString());

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
					} else if (tag_start.compareTo("couponName") == 0) {
						couponName = parser.nextText();
					} else if (tag_start.compareTo("franchiseNo") == 0) {
						franchiseNo = parser.nextText();
						if(franchiseNo.equals("")) {
							franchiseNo = "0";
						}
					} else if (tag_start.compareTo("companyNo") == 0) {
						companyNo = parser.nextText();
						if(companyNo.equals("")) {
							companyNo = "0";
						}
					} else if (tag_start.compareTo("companyName") == 0) {
						companyName = parser.nextText();
					} else if (tag_start.compareTo("addr") == 0) {
						addr = parser.nextText();
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
						if(status.equals("")) {
							status = "0";
						}
					} else if (tag_start.compareTo("linkImageQR") == 0) {
						linkImageQR = parser.nextText();
					} else if (tag_start.compareTo("NoKey") == 0) {
						NoKey = parser.nextText();
						if(NoKey.equals("")) {
							NoKey = "0";
						}
					} else if (tag_start.compareTo("DateOpen") == 0) {
						openTime = parser.nextText();
					} else if (tag_start.compareTo("error") == 0) {
						error = parser.nextText();
					}
					

					break;

				case XmlPullParser.END_TAG:
					String tag_end = parser.getName();
					if (tag_end.compareTo("CouponDate") == 0) {
						coupon.setNo(Integer.parseInt(no));
						coupon.setCategoryNo(Integer.parseInt(categoryNo));
						coupon.setCategoryName(categoryName);
						coupon.setGubun(Integer.parseInt(gubun));
						coupon.setCouponName(couponName);
						coupon.setFranchiseNo(Integer.parseInt(franchiseNo));
						coupon.setCompanyNo(Integer.parseInt(companyNo));
						coupon.setCompanyName(companyName);
						coupon.setAddress(addr);
						coupon.setCont(cont);
						coupon.setRegDate(regDate);
						coupon.setUseYn(useYn);
						coupon.setStartDate(startDate);
						coupon.setEndDate(endDate);
						coupon.setAttentionCont(attentionCont);
						coupon.setLinkImage(linkImage);
						coupon.setStatus(Integer.parseInt(status));
						coupon.setNoKey(Integer.parseInt(NoKey));
						coupon.setArOpenTime(arOpenDate);
						
						coupon.setNo(Integer.parseInt(no));
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
