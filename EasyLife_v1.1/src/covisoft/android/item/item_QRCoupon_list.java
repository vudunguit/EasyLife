package covisoft.android.item;


public class item_QRCoupon_list {

	private int no;
	private String nameStore;
	private String address1;
	private String address2;
	private String address3;
	private String address4;
	private String startDate;
	private String endDate;
	private String linkImageQR;

	public item_QRCoupon_list(int no, String nameStore, String address1, String address2, String address3, String address4, String startDate, String endDate, String linkImageQR) {
		super();
		this.no = no;
		this.nameStore = nameStore;
		this.address1 = address1;
		this.address2 = address2;
		this.address3 = address3;
		this.address4 = address4;
		this.startDate = startDate;
		this.endDate = endDate;
		this.linkImageQR = linkImageQR;
	}

	public item_QRCoupon_list() {
		super();
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getNameStore() {
		return nameStore;
	}

	public void setNameStore(String nameStore) {
		this.nameStore = nameStore;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getAddress4() {
		return address4;
	}

	public void setAddress4(String address4) {
		this.address4 = address4;
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

	public String getLinkImageQR() {
		return linkImageQR;
	}

	public void setLinkImageQR(String linkImageQR) {
		this.linkImageQR = linkImageQR;
	}

}
