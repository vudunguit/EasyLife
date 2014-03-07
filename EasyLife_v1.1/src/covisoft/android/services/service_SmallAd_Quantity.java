package covisoft.android.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.util.Log;
import covisoft.android.EasyLife.R;
import covisoft.android.EasyLife.EZUtil;
import covisoft.android.item.item_MainCategory;

public class service_SmallAd_Quantity {
	ArrayList<item_MainCategory> arItem_tmp;
	ArrayList<item_MainCategory> arItem;

	StringBuffer sb;

	private String categoryName = "";
	private String categoryCode = "";
	private String quantity = "0";
	public Activity act = null;

	public service_SmallAd_Quantity(Activity act) {
		try {
			this.act = act;
			sb = new StringBuffer();
			sb.append(act.getResources().getString(R.string.service_hiring_list));
			Log.e("get Recruitment list", sb.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<item_MainCategory> start() {
		init_string();
		arItem = new ArrayList<item_MainCategory>();
		arItem_tmp = new ArrayList<item_MainCategory>();

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

					if (tag_start.equals("eating")) {

						quantity = parser.nextText();
						if(quantity.equals("")) {
							quantity = "0";
						}
						categoryName = act.getString(R.string.text_top_Food);
						categoryCode = EZUtil.category_code_2;
						arItem_tmp.add(new item_MainCategory(categoryName, categoryCode, Integer.parseInt(quantity)));
					} else if (tag_start.equals("delivery")) {

						quantity = parser.nextText();
						if(quantity.equals("")) {
							quantity = "0";
						}
						categoryName = act.getString(R.string.text_top_Education);
						categoryCode = EZUtil.category_code_1;
						arItem_tmp.add(new item_MainCategory(categoryName, categoryCode, Integer.parseInt(quantity)));
					} else if (tag_start.equals("beauty")) {

						quantity = parser.nextText();
						if(quantity.equals("")) {
							quantity = "0";
						}
						categoryName = act.getString(R.string.text_top_Beauty);
						categoryCode = EZUtil.category_code_3;
						arItem_tmp.add(new item_MainCategory(categoryName, categoryCode, Integer.parseInt(quantity)));
					} else if (tag_start.equals("beer")) {

						quantity = parser.nextText();
						if(quantity.equals("")) {
							quantity = "0";
						}
						categoryName = act.getString(R.string.text_top_Shopping);
						categoryCode = EZUtil.category_code_4;
						arItem_tmp.add(new item_MainCategory(categoryName, categoryCode, Integer.parseInt(quantity)));
					} else if (tag_start.equals("service")) {

						quantity = parser.nextText();
						if(quantity.equals("")) {
							quantity = "0";
						}
						categoryName = act.getString(R.string.text_top_Tourism);
						categoryCode = EZUtil.category_code_7;
						arItem_tmp.add(new item_MainCategory(categoryName, categoryCode, Integer.parseInt(quantity)));
					} else if (tag_start.equals("entertainment")) {

						quantity = parser.nextText();
						if(quantity.equals("")) {
							quantity = "0";
						}
						categoryName = act.getString(R.string.text_top_Entertainment);
						categoryCode = EZUtil.category_code_6;
						arItem_tmp.add(new item_MainCategory(categoryName, categoryCode, Integer.parseInt(quantity)));
					} else if (tag_start.equals("medical")) {

						quantity = parser.nextText();
						if(quantity.equals("")) {
							quantity = "0";
						}
						categoryName = act.getString(R.string.text_top_Health);
						categoryCode = EZUtil.category_code_5;
						arItem_tmp.add(new item_MainCategory(categoryName, categoryCode, Integer.parseInt(quantity)));
					}
					break;
				case XmlPullParser.END_TAG:
//					String tag_end = parser.getName();
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
		for (int i = 0; i < arItem_tmp.size(); i++) {
			arItem.add(new item_MainCategory());
		}
		for (int i = 0; i < arItem_tmp.size(); i++) {
			if (arItem_tmp.get(i).getCategoryName().equals(act.getString(R.string.text_top_Food))) {
				arItem.get(0).setCategory_code(arItem_tmp.get(i).getCategory_code());
				arItem.get(0).setCategoryName(arItem_tmp.get(i).getCategoryName());
				arItem.get(0).setQuantity(arItem_tmp.get(i).getQuantity());
			} else if (arItem_tmp.get(i).getCategoryName().equals(act.getString(R.string.text_top_Education))) {
				arItem.get(1).setCategory_code(arItem_tmp.get(i).getCategory_code());
				arItem.get(1).setCategoryName(arItem_tmp.get(i).getCategoryName());
				arItem.get(1).setQuantity(arItem_tmp.get(i).getQuantity());
			} else if (arItem_tmp.get(i).getCategoryName().equals(act.getString(R.string.text_top_Beauty))) {
				arItem.get(2).setCategory_code(arItem_tmp.get(i).getCategory_code());
				arItem.get(2).setCategoryName(arItem_tmp.get(i).getCategoryName());
				arItem.get(2).setQuantity(arItem_tmp.get(i).getQuantity());
			} else if (arItem_tmp.get(i).getCategoryName().equals(act.getString(R.string.text_top_Shopping))) {
				arItem.get(3).setCategory_code(arItem_tmp.get(i).getCategory_code());
				arItem.get(3).setCategoryName(arItem_tmp.get(i).getCategoryName());
				arItem.get(3).setQuantity(arItem_tmp.get(i).getQuantity());
			} else if (arItem_tmp.get(i).getCategoryName().equals(act.getString(R.string.text_top_Tourism))) {
				arItem.get(4).setCategory_code(arItem_tmp.get(i).getCategory_code());
				arItem.get(4).setCategoryName(arItem_tmp.get(i).getCategoryName());
				arItem.get(4).setQuantity(arItem_tmp.get(i).getQuantity());
			} else if (arItem_tmp.get(i).getCategoryName().equals(act.getString(R.string.text_top_Entertainment))) {
				arItem.get(5).setCategory_code(arItem_tmp.get(i).getCategory_code());
				arItem.get(5).setCategoryName(arItem_tmp.get(i).getCategoryName());
				arItem.get(5).setQuantity(arItem_tmp.get(i).getQuantity());
			} else if (arItem_tmp.get(i).getCategoryName().equals(act.getString(R.string.text_top_Health))) {
				arItem.get(6).setCategory_code(arItem_tmp.get(i).getCategory_code());
				arItem.get(6).setCategoryName(arItem_tmp.get(i).getCategoryName());
				arItem.get(6).setQuantity(arItem_tmp.get(i).getQuantity());
			}
		}
		return arItem;
	}

	private void init_string() {
		categoryName = "";
		categoryCode = "";
		quantity = "0";
	}
}