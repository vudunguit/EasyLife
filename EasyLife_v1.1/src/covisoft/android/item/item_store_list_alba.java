package covisoft.android.item;

import java.io.Serializable;
import java.util.Comparator;

/*
 * This model use for list Favourite_Job & list Recruitment
 */
@SuppressWarnings("serial")
public class item_store_list_alba implements Serializable {

	public String favoriteNo = "";
	public String no = "";
	public String title = "";
	public String companyName = "";
	public String email = "";
	public String categoryNoBasic = "";
	public String categoryName = "";
	public String regDate = "";

	public String type = "";
	public String addr = "";
	public String tel = "";
	public String cont = "";
	public String lat = "";
	public String lng = "";
	public String s_logo = "";
	private double distance = 0;

	public item_store_list_alba(String favoriteNo, String no, String title,
			String companyName, String email, String categoryNoBasic,
			String categoryName, String regDate, String type, String addr,
			String tel, String cont, String lat, String lng, String s_logo,
			Double distance) {
		super();
		this.favoriteNo = favoriteNo;
		this.no = no;
		this.title = title;
		this.companyName = companyName;
		this.email = email;
		this.categoryNoBasic = categoryNoBasic;
		this.categoryName = categoryName;
		this.regDate = regDate;
		this.type = type;
		this.addr = addr;
		this.tel = tel;
		this.cont = cont;
		this.lat = lat;
		this.lng = lng;
		this.s_logo = s_logo;
		this.distance = distance;
	}

	public static class NameComparator implements Comparator<Object> {
		public int compare(Object o1, Object o2) {
			String en1 = ((item_store_list_alba) o1).companyName;
			String en2 = ((item_store_list_alba) o2).companyName;
			return en1.compareTo(en2); // desc Á¤·Ä
		}
	}

	public static class DistanceComparator implements Comparator<Object> {
		public int compare(Object o1, Object o2) {

			double en1 = ((item_store_list_alba) o1).distance;
			double en2 = ((item_store_list_alba) o2).distance;

			if (en1 < en2) {
				return -1;
			} else if (en1 == en2) {
				return 0;
			} else {
				return 1;
			}
		}
	}

	public item_store_list_alba() {
		super();
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
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

	public String getCont() {
		return cont;
	}

	public void setCont(String cont) {
		this.cont = cont;
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
