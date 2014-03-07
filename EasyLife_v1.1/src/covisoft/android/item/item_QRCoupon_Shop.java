package covisoft.android.item;

import java.io.Serializable;

@SuppressWarnings("serial")
public class item_QRCoupon_Shop implements Serializable{

	private String companyName;
	private String companyAddr;
//	private String categoryName;
	private String couponNo;
	private String couponName;
	private String linkImage;
	private int noofkey;
	private String cont;
	private String startDate;
	private String endDate;
	private String attentionCont;
	
//	public item_QRCoupon_Shop(String companyName, String companyAddr, String categoryName, String couponNo, String couponName, String linkImage, int noofkey, String cont, String startDate, String endDate, String attentionCont) {
//		super();
//		this.companyName = companyName;
//		this.companyAddr = companyAddr;
//		this.categoryName = categoryName;
//		this.couponNo = couponNo;
//		this.couponName = couponName;
//		this.linkImage = linkImage;
//		this.noofkey = noofkey;
//		this.cont = cont;
//		this.startDate = startDate;
//		this.endDate = endDate;
//		this.attentionCont = attentionCont;
//	}
	public item_QRCoupon_Shop(String companyName, String companyAddr, String couponNo, String couponName, String linkImage, int noofkey, String cont, String startDate, String endDate, String attentionCont) {
		super();
		this.companyName = companyName;
		this.companyAddr = companyAddr;
		this.couponNo = couponNo;
		this.couponName = couponName;
		this.linkImage = linkImage;
		this.noofkey = noofkey;
		this.cont = cont;
		this.startDate = startDate;
		this.endDate = endDate;
		this.attentionCont = attentionCont;
	}
	public item_QRCoupon_Shop(String couponNo, String couponName, String linkImage, int noofkey, String cont, String startDate, String endDate, String attentionCont) {
		super();
		this.couponNo = couponNo;
		this.couponName = couponName;
		this.linkImage = linkImage;
		this.noofkey = noofkey;
		this.cont = cont;
		this.startDate = startDate;
		this.endDate = endDate;
		this.attentionCont = attentionCont;
	}
	public item_QRCoupon_Shop() {
		super();
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyAddr() {
		return companyAddr;
	}
	public void setCompanyAddr(String companyAddr) {
		this.companyAddr = companyAddr;
	}
//	public String getCategoryName() {
//		return categoryName;
//	}
//	public void setCategoryName(String categoryName) {
//		this.categoryName = categoryName;
//	}
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
	public String getLinkImage() {
		return linkImage;
	}
	public void setLinkImage(String linkImage) {
		this.linkImage = linkImage;
	}
	public int getNoofkey() {
		return noofkey;
	}
	public void setNoofkey(int noofkey) {
		this.noofkey = noofkey;
	}
	public String getCont() {
		return cont;
	}
	public void setCont(String cont) {
		this.cont = cont;
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
	
}
