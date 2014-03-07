package covisoft.android.item;

import java.util.Comparator;

import lib.etc.lib_calculate_distance;
import android.os.Parcel;
import covisoft.android.tab3_Home.Home_Activity;

public class item_Recommandation {

	private String no;
	private String coupon;
	private String categoryNo;
	private String categoryNoBasic;
	private String categoryName;
	private String name;
	private String addr;
	private String tel;
	private String s_logo;
	private Double lat;
	private Double lng;
	private int level;

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getCoupon() {
		return coupon;
	}

	public void setCoupon(String coupon) {
		this.coupon = coupon;
	}

	public String getCategoryNo() {
		return categoryNo;
	}

	public void setCategoryNo(String categoryNo) {
		this.categoryNo = categoryNo;
	}

	public String getCategoryNoBasic() {
		return categoryNoBasic;
	}

	public void setCategoryNoBasic(String categoryNoBasic) {
		this.categoryNoBasic = categoryNoBasic;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getS_logo() {
		return s_logo;
	}

	public void setS_logo(String s_logo) {
		this.s_logo = s_logo;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public item_Recommandation(String no, String coupon,
			String categoryNo, String categoryNoBasic, String categoryName,
			String name, String addr, String tel, String s_logo, Double lat,
			Double lng) {
		super();
		this.no = no;
		this.coupon = coupon;
		this.categoryNo = categoryNo;
		this.categoryNoBasic = categoryNoBasic;
		this.categoryName = categoryName;
		this.name = name;
		this.addr = addr;
		this.tel = tel;
		this.s_logo = s_logo;
		this.lat = lat;
		this.lng = lng;
		this.level = 0;
	}

	
	
	public item_Recommandation(String no, String coupon,
			String categoryNo, String categoryNoBasic, String categoryName,
			String name, String addr, String tel, String s_logo, Double lat,
			Double lng, int level) {
		super();
		this.no = no;
		this.coupon = coupon;
		this.categoryNo = categoryNo;
		this.categoryNoBasic = categoryNoBasic;
		this.categoryName = categoryName;
		this.name = name;
		this.addr = addr;
		this.tel = tel;
		this.s_logo = s_logo;
		this.lat = lat;
		this.lng = lng;
		this.level = level;
	}

	public item_Recommandation() {
		super();
	}

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

	}

	public static class NameComparator implements Comparator<Object> {
		public int compare(Object o1, Object o2) {
			String en1 = ((item_Recommandation) o1).getName();
			String en2 = ((item_Recommandation) o2).getName();
			return en1.compareTo(en2); // desc Á¤·Ä
		}
	}

	public static class DistanceComparator implements Comparator<Object> {
		public int compare(Object o1, Object o2) {

			item_Recommandation item_1 = (item_Recommandation) o1;
			item_Recommandation item_2 = (item_Recommandation) o2;
			double en1 = lib_calculate_distance.distance(Home_Activity.latitude, Home_Activity.longitude, item_1.getLat(), item_1.getLng());
			double en2 = lib_calculate_distance.distance(Home_Activity.latitude, Home_Activity.longitude, item_2.getLat(), item_2.getLng());

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

			item_Recommandation item_1 = (item_Recommandation) o1;
			item_Recommandation item_2 = (item_Recommandation) o2;
			

			if (item_1.level > item_2.level) {
				return -1;
			} else if (item_1.level == item_2.level) {
				item_Recommandation item_3 = (item_Recommandation) o1;
				item_Recommandation item_4 = (item_Recommandation) o2;
				double en1 = lib_calculate_distance.distance(Home_Activity.latitude, Home_Activity.longitude, item_3.getLat(), item_3.getLng());
				double en2 = lib_calculate_distance.distance(Home_Activity.latitude, Home_Activity.longitude, item_4.getLat(), item_4.getLng());

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
