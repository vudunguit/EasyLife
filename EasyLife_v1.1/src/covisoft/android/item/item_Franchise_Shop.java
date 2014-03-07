package covisoft.android.item;

public class item_Franchise_Shop {
	
	private String no;       // Shop ID
	private int coupon;      // Coupon: 0 - Doesn't have coupon; 1 - Have coupon
	private String name;     // Shop Name
	private String addr;     // Address
	private String tel;      // Telephone
	private String s_logo;   // Logo - url
	private Double lat;      // latitude
	private Double lng;      // Longitude
	private Boolean flag;    // Flag to show small/large - false/true

	public item_Franchise_Shop(String no, int coupon, String name,
			String addr, String tel, String s_logo, Double lat, Double lng,
			Boolean flag) {

		this.no = no;
		this.coupon = coupon;
		this.name = name;
		this.addr = addr;
		this.tel = tel;
		this.s_logo = s_logo;
		this.lat = lat;
		this.lng = lng;
		this.flag = flag;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
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

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public int getCoupon() {
		return coupon;
	}

	public void setCoupon(int coupon) {
		this.coupon = coupon;
	}

}
