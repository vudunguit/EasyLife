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
import covisoft.android.item.item_Coupon;
import covisoft.android.item.item_QRCoupon_Shop;
import covisoft.android.item.item_Shop_Comment;
import covisoft.android.item.item_Shop_Event;
import covisoft.android.item.item_Shop_Menu;
import covisoft.android.tab3_Home.Home_Activity;

public class service_Shop_Detail {
	public ArrayList<String> arItem_pic = new ArrayList<String>();
	public ArrayList<item_Shop_Event> arItem_event = new ArrayList<item_Shop_Event>();
	public ArrayList<item_Coupon> arItem_coupon = new ArrayList<item_Coupon>();
	public ArrayList<item_QRCoupon_Shop> arItem_QRCoupon = new ArrayList<item_QRCoupon_Shop>();
	public ArrayList<item_Shop_Comment> arItem_comment = new ArrayList<item_Shop_Comment>();
	public ArrayList<item_Shop_Menu> arItem_menu = new ArrayList<item_Shop_Menu>();
	StringBuffer sb;

	public String no = "";
	public String name = "";
	public String addr = "";
	public String tel = "";
	public String s_info = "";
	public String s_short_info = "";
	public String s_etc = "";
	public String lat = "";
	public String lng = "";
	public String s_logo = "";

	
	public String basicMenu = "";
	public String basicItem = "";
	public String workDay = "";
	public String freeDay = "";
	public String companyLocate = "";
	public String parking = "";
	public String seat = "";
	public String deliveryYn = "";
	public String homepage = "";
	public String visited = "";
	public String facebook = "";
	public String wifi = "";
	public String navigation = "";
	public String suitable = "";
	public String near = "";
	public String style = "";
	public String subProduct = "";
	

	public String distance = "";

	
	String event_name = "";
	String event_cont = "";
	String start_date = "";
	String end_date = "";

	String couponNo = "";
	String coupon_name = "";
	String linkimage = "";
	String coupon_cont = "";
	String coupon_startDate = "";
	String coupon_endDate = "";
	String idcouponkind = "";
	String coupon_attentionCont = "";
	
	String qrcouponNo = "";
	String qrcouponName = "";
	String qrlinkimage = "";
	String qrnoofkey = "";
	String qrcont = "";
	String qrstartDate = "";
	String qrendDate = "";
	String qrattentionCont = "";

	String comment_title = "";
	String comment_cont = "";
	String comment_regDate = "";
	String comment_user = "";
	String comment_goodNum = "";

	String menu_title = "";
	String menu_price = "";
	public Activity act = null;

	public service_Shop_Detail() {
	}

	// http://mobile.subnara.info/api/store_detail.php?no=44&
	public void init_detail(String no, String deviceID, Activity act) {
		this.act = act;
		try {
			sb = new StringBuffer();
			sb.append(act.getResources().getString(R.string.service_Store_detail));
			sb.append("&no").append("=").append(URLEncoder.encode(no, "UTF-8")).append("&");
			sb.append("deviceID").append("=").append(URLEncoder.encode(deviceID, "UTF-8"));

			Log.e("get shop detail", sb + "");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void init_franchise_detail(String no, Activity act) {
		this.act = act;
		try {
			sb = new StringBuffer();
			sb.append(act.getResources().getString(R.string.service_Franchise_detail));
			sb.append("&no").append("=").append(URLEncoder.encode(no, "UTF-8"));
			
			Log.e("get franchise detail", sb.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	boolean b_item = false;
	boolean b_event = false;
	boolean b_coupon = false;
	boolean b_qrcoupon = false;
	boolean b_comment = false;
	boolean b_menu = false;

	public void start() {
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
					if (tag_start.compareTo("item") == 0) {
						b_item = true;
					} else if (tag_start.equals("event_item")) {
						b_event = true;
					} else if (tag_start.equals("coupon_item")) {
						b_coupon = true;
					} else if (tag_start.equals("qrcoupon_item")) {
						b_qrcoupon = true;
					} else if (tag_start.equals("comment_item")) {
						b_comment = true;
					} else if (tag_start.equals("menu_item")) {
						b_menu = true;
					}

					if (b_item == true) {
						if (tag_start.equals("no")) {
							no = parser.nextText();
						} else if (tag_start.equals("name")) {
							name = parser.nextText();
						} else if (tag_start.equals("addr")) {
							addr = parser.nextText();
						} else if (tag_start.equals("tel")) {
							tel = parser.nextText();
						} else if (tag_start.equals("s_info")) {
							s_info = parser.nextText();
						} else if (tag_start.equals("s_short_info")) {
							s_short_info = parser.nextText();
						} else if (tag_start.equals("s_etc")) {
							s_etc = parser.nextText();
						} else if (tag_start.equals("lat")) {
							lat = parser.nextText();
							if (lat.equals("")) {
								lat = "0.0";
							}
						} else if (tag_start.equals("lng")) {
							lng = parser.nextText();
							if (lng.equals("")) {
								lng = "0.0";
							}
						} else if (tag_start.equals("facebook")) {
							facebook = parser.nextText();
						} else if (tag_start.equals("wifi")) {
							wifi = parser.nextText();
						} else if (tag_start.equals("navigation")) {
							navigation = parser.nextText();
						} else if (tag_start.equals("suitable")) {
							suitable = parser.nextText();
						} else if (tag_start.equals("near")) {
							near = parser.nextText();
						} else if (tag_start.equals("style")) {
							style = parser.nextText();
						} else if (tag_start.equals("subProduct")) {
							subProduct = parser.nextText();
						} else if (tag_start.equals("s_logo")) {
							s_logo = parser.nextText();
						} else if (tag_start.equals("s_img")) {
							arItem_pic.add(parser.nextText());
						} else if (tag_start.equals("basicMenu")) {
							basicMenu = parser.nextText();
						} else if (tag_start.equals("basicItem")) {
							basicItem = parser.nextText();
						} else if (tag_start.equals("workDay")) {
							workDay = parser.nextText();
						} else if (tag_start.equals("freeDay")) {
							freeDay = parser.nextText();
						} else if (tag_start.equals("companyLocate")) {
							companyLocate = parser.nextText();
						} else if (tag_start.equals("parking")) {
							parking = parser.nextText();
						} else if (tag_start.equals("seat")) {
							seat = parser.nextText();
						} else if (tag_start.equals("deliveryYn")) {
							deliveryYn = parser.nextText();
						} else if (tag_start.equals("homepage")) {
							homepage = parser.nextText();
						} else if (tag_start.equals("visited")) {
							visited = parser.nextText();
						}

					} 
					// Get Event
					else if (b_event == true) {
						if (tag_start.equals("eventName")) {
							event_name = parser.nextText();
						} else if (tag_start.equals("cont")) {
							event_cont = parser.nextText();
						} else if (tag_start.equals("startDate")) {
							start_date = parser.nextText();
						} else if (tag_start.equals("endDate")) {
							end_date = parser.nextText();
						}
					}
					// Get Coupon Detail 
					else if (b_coupon == true) {
						if (tag_start.equals("couponName")) {
							coupon_name = parser.nextText();
						} else if (tag_start.equals("couponNo")) {
							couponNo = parser.nextText();
						} else if (tag_start.equals("linkimage")) {
							linkimage = parser.nextText();
						} else if (tag_start.equals("cont")) {
							coupon_cont = parser.nextText();
						} else if (tag_start.equals("startDate")) {
							coupon_startDate = parser.nextText();
						} else if (tag_start.equals("endDate")) {
							coupon_endDate = parser.nextText();
						} else if (tag_start.equals("idcouponkind")) {
							idcouponkind = parser.nextText();
							if(idcouponkind.equals("")) {
								idcouponkind = "0";
							}
						} else if (tag_start.equals("attentionCont")) {
							coupon_attentionCont = parser.nextText();
						}
					}
					
					// Get QRCoupon Detail
					else if(b_qrcoupon == true) {
						if(tag_start.equals("qrcouponNo")) {
							qrcouponNo = parser.nextText();
						} else if(tag_start.equals("qrcouponName")) {
							qrcouponName = parser.nextText();
						} else if(tag_start.equals("qrlinkimage")) {
							qrlinkimage = parser.nextText();
						} else if(tag_start.equals("qrnoofkey")) {
							qrnoofkey = parser.nextText();
							if(qrnoofkey.equals("")) {
								qrnoofkey = "1";
							}
						} else if(tag_start.equals("qrcont")) {
							qrcont = parser.nextText();
						} else if(tag_start.equals("qrstartDate")) {
							qrstartDate = parser.nextText();
						} else if(tag_start.equals("qrendDate")) {
							qrendDate = parser.nextText();
						} else if(tag_start.equals("qrattentionCont")) {
							qrattentionCont = parser.nextText();
						}
					}
					// Get Comment
					else if (b_comment == true) {
						if (tag_start.equals("title")) {
							comment_title = parser.nextText();
						} else if (tag_start.equals("cont")) {
							comment_cont = parser.nextText();
						} else if (tag_start.equals("regDate")) {
							comment_regDate = parser.nextText();
						} else if (tag_start.equals("user")) {
							comment_user = parser.nextText();
						} else if (tag_start.equals("goodNum")) {
							comment_goodNum = parser.nextText();
						}
					} 
					// Get Menu
					else if (b_menu == true) {
						if (tag_start.equals("title")) {
							menu_title = parser.nextText();
						} else if (tag_start.equals("menuPrice")) {
							menu_price = parser.nextText();
						}
					}

					break;
				case XmlPullParser.END_TAG:
					String tag_end = parser.getName();
					if (tag_end.equals("item")) {
						b_item = false;
					} else if (tag_end.equals("event_item")) {
						b_event = false;
					} else if (tag_end.equals("coupon_item")) {
						b_coupon = false;
					} else if (tag_end.equals("comment_item")) {
						b_comment = false;
					} else if (tag_end.equals("menu_item")) {
						b_menu = false;
					}

					if (tag_end.equals("item")) {
						distance = lib_calculate_distance.calculate_distance(Home_Activity.latitude, Home_Activity.longitude, lat, lng);
					} else if (tag_end.equals("event_item")) {
						arItem_event.add(new item_Shop_Event(event_name, event_cont, start_date, end_date));
					} else if (tag_end.equals("coupon_item")) {
						if(!idcouponkind.equals("")) {
							arItem_coupon.add(new item_Coupon(couponNo, coupon_name, linkimage, coupon_cont, coupon_startDate, coupon_endDate, Integer.parseInt(idcouponkind), coupon_attentionCont));
						}
						
					} else if (tag_end.equals("qrcoupon_item")) {
						if(!qrnoofkey.equals("")) {
							arItem_QRCoupon.add(new item_QRCoupon_Shop(qrcouponNo, qrcouponName, qrlinkimage, Integer.parseInt(qrnoofkey), qrcont, qrstartDate, qrendDate, qrattentionCont));
						}
						
					} else if (tag_end.equals("comment_item")) {
						arItem_comment.add(new item_Shop_Comment(comment_title, comment_cont, comment_regDate, comment_user, comment_goodNum));
					} else if (tag_end.equals("menu_item")) {
						arItem_menu.add(new item_Shop_Menu(menu_title, menu_price));
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