package covisoft.android.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.util.Log;
import covisoft.android.EasyLife.R;
import covisoft.android.item.item_Promotion;

public class service_PromotionList {

	ArrayList<item_Promotion> arItem;
	StringBuffer sb;

	String no = "";
	String parentCategoryNo = "";
	String categoryName = "";
	String companyNo = "";
	String companyName = "";
	String logo = "";
	String subject = "";
	String startDate = "";
	String endDate = "";
	String type = "";
	String kind = "";
	
	Date sDate;
	Date eDate;
	
	public Activity act = null;

	public service_PromotionList() {

	}

	public void init(Activity act) {

		this.act = act;
		sb = new StringBuffer();
		sb.append(act.getResources().getString(R.string.service_board_2));
		Log.e("get board 2", sb.toString());
	}

	public ArrayList<item_Promotion> start() {
		arItem = new ArrayList<item_Promotion>();

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
					} else if (tag_start.compareTo("categoryNo") == 0) {
						parentCategoryNo = parser.nextText();
						if(parentCategoryNo.equals("")) {
							parentCategoryNo = "0";
						}
					} else if (tag_start.compareTo("categoryName") == 0) {
						categoryName = parser.nextText();
					} else if (tag_start.compareTo("companyNo") == 0) {
						companyNo = parser.nextText();
						if(companyNo.equals("")) {
							companyNo = "0";
						}
					} else if (tag_start.compareTo("companyName") == 0) {
						companyName = parser.nextText();
					} else if (tag_start.compareTo("logo") == 0) {
						logo = parser.nextText();
					}  else if (tag_start.compareTo("text") == 0) {
						subject = parser.nextText();
					} else if (tag_start.compareTo("startDate") == 0) {
						startDate = parser.nextText();
					} else if (tag_start.compareTo("endDate") == 0) {
						endDate = parser.nextText();
					} else if (tag_start.compareTo("type") == 0) {
						type = parser.nextText();
						if(type.equals("")) {
							type = "0";
						}
					} else if (tag_start.compareTo("kind") == 0) {
						kind = parser.nextText();
						if(kind.equals("")) {
							kind = "0";
						}
					}

					break;
				case XmlPullParser.END_TAG:
					String tag_end = parser.getName();
					if (tag_end.compareTo("boardInfo") == 0) {

						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						try {
							sDate = formatter.parse(startDate);
							eDate = formatter.parse(endDate);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						arItem.add(new item_Promotion(Integer.parseInt(no)
													, Integer.parseInt(parentCategoryNo)
													, categoryName
													, Integer.parseInt(companyNo)
													, companyName
													, logo
													, subject
													, sDate
													, eDate
													, Integer.parseInt(type)
													, Integer.parseInt(kind)));
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
