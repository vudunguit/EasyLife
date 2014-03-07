package covisoft.android.item;

import java.io.Serializable;
import java.util.Comparator;

@SuppressWarnings("serial")
public class item_store_list_fav implements Serializable {

	public item_store_list_fav(String favoriteNo, String no, int coupon,
			String name, String categoryNoBasic, String categoryName,
			String type, String addr, String tel, String lat, String lng,
			String s_logo, double distance) {
		super();
		this.favoriteNo = favoriteNo;
		this.no = no;
		this.coupon = coupon;
		this.name = name;
		this.categoryNoBasic = categoryNoBasic;
		this.categoryName = categoryName;
		this.type = type;
		this.addr = addr;
		this.tel = tel;
		this.lat = lat;
		this.lng = lng;
		this.s_logo = s_logo;
		this.distance = distance;
	}

	private String favoriteNo = "";
	private String no = "";
	private int coupon = 0;
	private String name = "";
	private String categoryNoBasic = "";
	private String categoryName = "";
	private String type = "";
	private String addr = "";
	private String tel = "";
	private String lat = "";
	private String lng = "";
	private String s_logo = "";
	private double distance = 0;

	public static class NameComparator implements Comparator<Object> {
		public int compare(Object o1, Object o2) {
			String en1 = ((item_store_list_fav) o1).name;
			String en2 = ((item_store_list_fav) o2).name;
			return en1.compareTo(en2); // desc Á¤·Ä
		}
	}

	public static class DistanceComparator implements Comparator<Object> {
		public int compare(Object o1, Object o2) {

			double en1 = ((item_store_list_fav) o1).distance;
			double en2 = ((item_store_list_fav) o2).distance;

			if (en1 < en2) {
				return -1;
			} else if (en1 == en2) {
				return 0;
			} else {
				return 1;
			}
		}
	}

	public String getFavoriteNo() {
		return favoriteNo;
	}

	public void setFavoriteNo(String favoriteNo) {
		this.favoriteNo = favoriteNo;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public int getCoupon() {
		return coupon;
	}

	public void setCoupon(int coupon) {
		this.coupon = coupon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getS_logo() {
		return s_logo;
	}

	public void setS_logo(String s_logo) {
		this.s_logo = s_logo;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

}
