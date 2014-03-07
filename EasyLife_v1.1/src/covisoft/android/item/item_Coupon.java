package covisoft.android.item;

import java.io.Serializable;

@SuppressWarnings("serial")
public class item_Coupon implements Serializable {

	private String no;                 // ID - coupon follow user        
	private int categoryNo;            // Category ID
	private String categoryName;       // Category Name
	private int gubun;                 // shop normal / Franchise 
	private String couponNo;           // Coupon ID
	private String couponName;         // Coupon Name
	private String linkimage;          // Coupon Image
	private String cont;               // Coupon content
	private int companyNo;             // Company ID
	private String companyName;        // Company Name
	private String hp;                 // Username    
	private String useDate;            // Date when user use this coupon - 0000-00-00 00:00:00 when unuse
	private Boolean useYn;             // Used or Not
	private int couponNum;             // ????  
	private String startDate;          
	private String endDate;
	private String attentionCont;      // ????
	private int idCouponKind;          // Coupon kind: 1 - Time; 2 - Quantity
	private String addr;               // Shop Address
	private Double lat;                // Shop Latitude
	private Double lng;                // Shop longtitude
	private String downUpfile1;        // Coupon's image
	private int residual_day;          // Left day
	private boolean flag;              // Flag to show small/large

	public item_Coupon(String no, int categoryNo, String categoryName, int gubun, String couponNo, String couponName, String linkimage, String cont, int companyNo, String companyName, String hp, String useDate, Boolean useYn, int couponNum, String startDate, String endDate, String attentionCont, int idCouponKind, String addr, Double lat, Double lng, String downUpfile1, int residual_day, boolean flag) {
		super();
		this.no = no;
		this.categoryNo = categoryNo;
		this.categoryName = categoryName;
		this.gubun = gubun;
		this.couponNo = couponNo;
		this.couponName = couponName;
		this.linkimage = linkimage;
		this.cont = cont;
		this.companyNo = companyNo;
		this.companyName = companyName;
		this.hp = hp;
		this.useDate = useDate;
		this.useYn = useYn;
		this.couponNum = couponNum;
		this.startDate = startDate;
		this.endDate = endDate;
		this.attentionCont = attentionCont;
		this.idCouponKind = idCouponKind;
		this.addr = addr;
		this.lat = lat;
		this.lng = lng;
		this.downUpfile1 = downUpfile1;
		this.residual_day = residual_day;
		this.flag = flag;
	}
	
	public item_Coupon(String couponNo, String couponName, String linkimage, String cont, String startDate, String endDate, int idcouponkind, String attentionCont) {
		this.couponNo = couponNo;
		this.couponName = couponName;
		this.linkimage = linkimage;
		this.cont = cont;
		this.startDate = startDate;
		this.endDate = endDate;
		this.idCouponKind = idcouponkind;
		this.attentionCont = attentionCont;
	}

	public item_Coupon() {
		super();
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public int getCategoryNo() {
		return categoryNo;
	}

	public void setCategoryNo(int categoryNo) {
		this.categoryNo = categoryNo;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getGubun() {
		return gubun;
	}

	public void setGubun(int gubun) {
		this.gubun = gubun;
	}

	public String getCouponNo() {
		return couponNo;
	}

	public void setCouponNo(String couponNo) {
		this.couponNo = couponNo;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getLinkimage() {
		return linkimage;
	}

	public void setLinkimage(String linkimage) {
		this.linkimage = linkimage;
	}

	public String getCont() {
		return cont;
	}

	public void setCont(String cont) {
		this.cont = cont;
	}

	public int getCompanyNo() {
		return companyNo;
	}

	public void setCompanyNo(int companyNo) {
		this.companyNo = companyNo;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getHp() {
		return hp;
	}

	public void setHp(String hp) {
		this.hp = hp;
	}

	public String getUseDate() {
		return useDate;
	}

	public void setUseDate(String useDate) {
		this.useDate = useDate;
	}

	public Boolean getUseYn() {
		return useYn;
	}

	public void setUseYn(Boolean useYn) {
		this.useYn = useYn;
	}

	public int getCouponNum() {
		return couponNum;
	}

	public void setCouponNum(int couponNum) {
		this.couponNum = couponNum;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getAttentionCont() {
		return attentionCont;
	}

	public void setAttentionCont(String attentionCont) {
		this.attentionCont = attentionCont;
	}

	public int getIdCouponKind() {
		return idCouponKind;
	}

	public void setIdCouponKind(int idCouponKind) {
		this.idCouponKind = idCouponKind;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
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

	public String getDownUpfile1() {
		return downUpfile1;
	}

	public void setDownUpfile1(String downUpfile1) {
		this.downUpfile1 = downUpfile1;
	}

	public int getResidual_day() {
		return residual_day;
	}

	public void setResidual_day(int residual_day) {
		this.residual_day = residual_day;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	
}
