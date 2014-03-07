package covisoft.android.item;

import java.util.ArrayList;

public class item_QRCoupon_detail {

	private int no;
	private int categoryNo;
	private String categoryName;
	private int gubun; 
	private String couponName;
	private int franchiseNo;
	private int companyNo;
	private String companyName;
	private String address;
	private String cont;
	private String regDate;
	private String useYn;
	private String startDate;
	private String endDate;
	private String attentionCont;
	private String linkImage;
	private int status;
	private String linkImageQR;
	private int NoKey;
	private String linkkeyimage;
	private ArrayList<String> arOpenTime;

	public String toString() {
		return no + " - " + categoryName + " - " + couponName;
	}

	
	public item_QRCoupon_detail() {
		super();
	}


	public item_QRCoupon_detail(int no, int categoryNo, String categoryName, int gubun, String couponName, int franchiseNo, int companyNo, String companyName, String address, String cont, String regDate, String useYn, String startDate, String endDate, String attentionCont, String linkImage, int status, String linkImageQR, int noKey, String linkkeyimage, ArrayList<String> arOpenTime) {
		super();
		this.no = no;
		this.categoryNo = categoryNo;
		this.categoryName = categoryName;
		this.gubun = gubun;
		this.couponName = couponName;
		this.franchiseNo = franchiseNo;
		this.companyNo = companyNo;
		this.companyName = companyName;
		this.address = address;
		this.cont = cont;
		this.regDate = regDate;
		this.useYn = useYn;
		this.startDate = startDate;
		this.endDate = endDate;
		this.attentionCont = attentionCont;
		this.linkImage = linkImage;
		this.status = status;
		this.linkImageQR = linkImageQR;
		NoKey = noKey;
		this.linkkeyimage = linkkeyimage;
		this.arOpenTime = arOpenTime;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
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

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public int getFranchiseNo() {
		return franchiseNo;
	}

	public void setFranchiseNo(int franchiseNo) {
		this.franchiseNo = franchiseNo;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCont() {
		return cont;
	}

	public void setCont(String cont) {
		this.cont = cont;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
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

	public String getLinkImage() {
		return linkImage;
	}

	public void setLinkImage(String linkImage) {
		this.linkImage = linkImage;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getLinkImageQR() {
		return linkImageQR;
	}

	public void setLinkImageQR(String linkImageQR) {
		this.linkImageQR = linkImageQR;
	}

	public int getNoKey() {
		return NoKey;
	}

	public void setNoKey(int noKey) {
		NoKey = noKey;
	}

	public String getLinkkeyimage() {
		return linkkeyimage;
	}

	public void setLinkkeyimage(String linkkeyimage) {
		this.linkkeyimage = linkkeyimage;
	}

	public ArrayList<String> getArOpenTime() {
		return arOpenTime;
	}

	public void setArOpenTime(ArrayList<String> arOpenTime) {
		this.arOpenTime = arOpenTime;
	}

	
}
