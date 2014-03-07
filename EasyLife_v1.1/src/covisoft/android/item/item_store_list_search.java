package covisoft.android.item;

import java.io.Serializable;
import java.util.Comparator;

import android.os.Parcel;

@SuppressWarnings("serial")
public class item_store_list_search implements Serializable {
	public item_store_list_search(String no, int coupon, String categoryNoBasic, String name, String addr,
			String tel, String lat, String lng, String s_logo, double distance) {

		this.no = no;
		this.coupon = coupon;
		this.categoryNoBasic = categoryNoBasic;
		this.name = name;
		this.addr = addr;
		this.tel = tel;
		this.lat = lat;
		this.lng = lng;
		this.s_logo = s_logo;
		this.distance = distance;
		
	}
	public item_store_list_search(String no, int coupon, String categoryNoBasic, String name, String addr,
			String tel, String lat, String lng, String s_logo, double distance, int level) {

		this.no = no;
		this.coupon = coupon;
		this.categoryNoBasic = categoryNoBasic;
		this.name = name;
		this.addr = addr;
		this.tel = tel;
		this.lat = lat;
		this.lng = lng;
		this.s_logo = s_logo;
		this.distance = distance;
		this.level = level;
		
	}
	public item_store_list_search() {
		super();
	}

	public String no = "";
	public int coupon = 0;
	public String categoryNoBasic;
	public String name = "";
	public String addr = "";
	public String tel = "";
	public String lat = "";
	public String lng = "";
	public String s_logo = "";
	public double distance = 0;
	public int level = 0;

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

	}

	public static class NameComparator implements Comparator<Object> {
		public int compare(Object o1, Object o2) {
			String en1 = ((item_store_list_search) o1).name;
			String en2 = ((item_store_list_search) o2).name;
			return en1.compareTo(en2); // desc Á¤·Ä
		}
	}

	public static class DistanceComparator implements Comparator<Object> {
		public int compare(Object o1, Object o2) {

			double en1 = ((item_store_list_search) o1).distance;
			double en2 = ((item_store_list_search) o2).distance;

			if (en1 < en2) {
				return -1;
			} else if (en1 == en2) {
				return 0;
			} else {
				return 1;
			}
		}
	}
	public static class LevelComparator implements Comparator<Object> {
		public int compare(Object o1, Object o2) {

			int lev1 = ((item_store_list_search) o1).level;
			int lev2 = ((item_store_list_search) o2).level;

			if (lev1 > lev2) {
				return -1;
			} else if (lev1 == lev2) {
				double en1 = ((item_store_list_search) o1).distance;
				double en2 = ((item_store_list_search) o2).distance;

				if (en1 < en2) {
					return -1;
				} else if (en1 == en2) {
					return 0;
				} else {
					return 1;
				}
			} else {
				return 1;
			}
		}
	}
}
