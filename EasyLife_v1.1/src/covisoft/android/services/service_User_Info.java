package covisoft.android.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.util.Log;
import covisoft.android.EasyLife.EasyLifeActivity;
import covisoft.android.EasyLife.R;


/* http://easylife.com.vn/Web_Mobile_New/api/userInfo/? */
public class service_User_Info {
	StringBuffer sb;

	String username = "";
	String name = "";
	String birthday = "";
	String gender = "";
	String address = "";
	String email = "";
	String phone = "";
	String imageUrl = "";
	
	public Activity act = null;

	public service_User_Info(Activity act, String username) {
		this.act = act;
		
		try {
			sb = new StringBuffer();
			sb.append(act.getResources().getString(R.string.service_getUserInfo));
			sb.append("username").append("=").append(URLEncoder.encode(username, "UTF-8"));
			
			Log.e("get User's Info", sb.toString());
		
		} catch (Exception e) {
			// TODO: handle exception
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

					if (tag_start.compareTo("username") == 0) {
						username = parser.nextText();
					} else if (tag_start.compareTo("name") == 0) {
						name = parser.nextText();
					} else if (tag_start.compareTo("birthday") == 0) {
						birthday = parser.nextText();
					} else if (tag_start.compareTo("gender") == 0) {
						gender = parser.nextText();
					} else if (tag_start.compareTo("address") == 0) {
						address = parser.nextText();
					} else if (tag_start.compareTo("email") == 0) {
						email = parser.nextText();
					} else if (tag_start.compareTo("phone") == 0) {
						phone = parser.nextText();
					} else if (tag_start.compareTo("imageUrl") == 0) {
						imageUrl = parser.nextText();
					}
					break;
				case XmlPullParser.END_TAG:
					
					String tag_end = parser.getName();
					if (tag_end.compareTo("root") == 0 && EasyLifeActivity.user != null) {
						
						EasyLifeActivity.user.setAddress(address);
						EasyLifeActivity.user.setBirthday(birthday);
						EasyLifeActivity.user.setEmail(email);
						EasyLifeActivity.user.setGender(gender);
						EasyLifeActivity.user.setImageUrl(imageUrl);
						EasyLifeActivity.user.setName(name);
						EasyLifeActivity.user.setTel(phone);
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

	}

}