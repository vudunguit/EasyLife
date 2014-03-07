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
import covisoft.android.EasyLife.EasyLifeActivity;
import covisoft.android.EasyLife.R;
import covisoft.android.item.item_Notification;

public class service_Notification {

	StringBuffer sb;
	
	String id;
	String title;
	String description;
	String radius;
	String kind;
	String categoryNoBasic;
	String idcompany;
	String idfranchise;
	String status;

	
	public Activity act = null;

	public service_Notification(Activity act, String userName) {
		this.act = act;
		try {
			sb = new StringBuffer();
			sb.append(act.getResources().getString(R.string.service_GetNotification));
			if(!userName.equals("")) {
				sb.append("username").append("=").append(URLEncoder.encode(userName, "UTF-8"));
			}
			sb.append("&regid").append("=").append(URLEncoder.encode(EasyLifeActivity.regId, "UTF-8"));
			Log.e("Get Notification", sb.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<item_Notification> start() {
		init_string();
		ArrayList<item_Notification> arNotification = new ArrayList<item_Notification>();
		

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

					if (tag_start.compareTo("id") == 0) {
						id = parser.nextText();
					} else if (tag_start.compareTo("title") == 0) {
						title = parser.nextText();
					} else if(tag_start.compareTo("description") == 0) {
						description = parser.nextText();
					} else if(tag_start.compareTo("radius") == 0) {
						radius = parser.nextText();
					} else if(tag_start.compareTo("kind") == 0) {
						kind = parser.nextText();
					} else if(tag_start.compareTo("categoryNoBasic") == 0) {
						categoryNoBasic = parser.nextText();
					} else if(tag_start.compareTo("idcompany") == 0) {
						idcompany = parser.nextText();
					} else if(tag_start.compareTo("idfranchise") == 0) {
						idfranchise = parser.nextText();
					} else if(tag_start.compareTo("status") == 0) {
						status = parser.nextText();
					}

					break;
				case XmlPullParser.END_TAG:
					String tag_end = parser.getName();
					if (tag_end.compareTo("item") == 0) {
						item_Notification item = new item_Notification();
						item.setId(Integer.parseInt(id));
						item.setTitle(title);
						item.setDescription(description);
						try {
							item.setRadius(Integer.parseInt(radius));
						} catch (Exception e) {
							// TODO: handle exception
							item.setRadius(0);
						}
						try {
							item.setKind(Integer.parseInt(kind));
						} catch (Exception e) {
							// TODO: handle exception
							item.setKind(1);
						}
						try {
							item.setCategoryNoBasic(Integer.parseInt(categoryNoBasic));
						} catch (Exception e) {
							// TODO: handle exception
							item.setCategoryNoBasic(1);
						}
						item.setIdcompany(idcompany);
						item.setIdfranchise(idfranchise);
						try {
							item.setStatus(Integer.parseInt(status));
						} catch (Exception e) {
							// TODO: handle exception
							item.setStatus(0);
						}
						
						arNotification.add(item);
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
		return arNotification;
	}

	private void init_string() {
		id = "";
		title = "";
		description= "";
		radius= "";
		kind= "";
		categoryNoBasic= "";
		idcompany= "";
		idfranchise= "";
		status= "";
	}
}