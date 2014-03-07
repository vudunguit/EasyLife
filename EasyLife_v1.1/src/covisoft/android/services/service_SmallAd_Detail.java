package covisoft.android.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import covisoft.android.EasyLife.R;

import android.app.Activity;
import android.util.Log;

public class service_SmallAd_Detail {
	StringBuffer sb;

	public String no = "";
	public String title = "";
	public String cont = "";
	public String companyName = "";
	public String tel = "";
	public String s_logo = "";
	public String linkimage = "";
	public String email = "";
	public String regDate = "";
	public String add = "";
	public String lat = "";
	public String lng = "";
	public Activity act = null;

	public service_SmallAd_Detail(String no, Activity act) {
		this.act = act;
		try {
			sb = new StringBuffer();
			sb.append(act.getResources().getString(R.string.service_alba_detail));
			sb.append("no").append("=").append(URLEncoder.encode(no, "UTF-8"));
			
			Log.e("getxml_alba_detail", sb.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void start() {
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
					} else if (tag_start.compareTo("title") == 0) {
						title = parser.nextText();
					} else if (tag_start.compareTo("cont") == 0) {
						cont = parser.nextText();
					} else if (tag_start.compareTo("companyName") == 0) {
						companyName = parser.nextText();
					} else if (tag_start.compareTo("tel") == 0) {
						tel = parser.nextText();
					} else if (tag_start.compareTo("s_logo") == 0) {
						s_logo = parser.nextText();
					} else if (tag_start.compareTo("linkimage") == 0) {
						linkimage = parser.nextText();
					} else if (tag_start.compareTo("email") == 0) {
						email = parser.nextText();
					} else if (tag_start.compareTo("regDate") == 0) {
						regDate = parser.nextText();
					} else if (tag_start.compareTo("add") == 0) {
						add = parser.nextText();
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
					}

					break;
				case XmlPullParser.END_TAG:
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
	}
}