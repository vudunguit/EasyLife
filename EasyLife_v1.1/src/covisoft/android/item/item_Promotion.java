package covisoft.android.item;

import java.util.Date;

/*
 * This model use to control promotion item
 * Promotion show in list of event & coupon from home page 
 */
		
public class item_Promotion {

	private int no;                 // board id
	private int parentCategoryNo;        
	private String categoryName;
	private int shopId;
	private String shopName;
	private String shopLogo;
	private String subject;
	private Date startDate;
	private Date endDate;
	private int type;               // 1 - thường ; 2 - fanchise
	private int kind;               // 1/3 - Voucher ; 2 - Event
	public item_Promotion(int no, int parentCategoryNo, String categoryName, int shopId, String shopName, String shopLogo, String subject, Date startDate, Date endDate, int type, int kind) {
		super();
		this.no = no;
		this.parentCategoryNo = parentCategoryNo;
		this.categoryName = categoryName;
		this.shopId = shopId;
		this.shopName = shopName;
		this.shopLogo = shopLogo;
		this.subject = subject;
		this.startDate = startDate;
		this.endDate = endDate;
		this.type = type;
		this.kind = kind;
	}
	public item_Promotion() {
		super();
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public int getParentCategoryNo() {
		return parentCategoryNo;
	}
	public void setParentCategoryNo(int parentCategoryNo) {
		this.parentCategoryNo = parentCategoryNo;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public int getShopId() {
		return shopId;
	}
	public void setShopId(int shopId) {
		this.shopId = shopId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getShopLogo() {
		return shopLogo;
	}
	public void setShopLogo(String shopLogo) {
		this.shopLogo = shopLogo;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getKind() {
		return kind;
	}
	public void setKind(int kind) {
		this.kind = kind;
	}
	
}
