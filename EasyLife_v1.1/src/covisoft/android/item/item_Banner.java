package covisoft.android.item;

/*
 * This model is used for banner image
 */
public class item_Banner {

	private String title;            // Banner title
	private String bannerKind;       // Kind: 1 - Banner home page; 2 - Banner info page; 3 - Banner normal page
	private String linkUrl;          // Link to show browser when user click on banner
	private String description;      // Banner's content
	private String idCompany;        // ID of company own this banner
	private String linkImg;          // Link of image

	public item_Banner(String title, String bannerKind, String linkUrl, String description, String idCompany, String linkImg) {
		super();
		this.title = title;
		this.bannerKind = bannerKind;
		this.linkUrl = linkUrl;
		this.description = description;
		this.idCompany = idCompany;
		this.linkImg = linkImg;
	}

	public item_Banner() {
		super();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBannerKind() {
		return bannerKind;
	}

	public void setBannerKind(String bannerKind) {
		this.bannerKind = bannerKind;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIdCompany() {
		return idCompany;
	}

	public void setIdCompany(String idCompany) {
		this.idCompany = idCompany;
	}

	public String getLinkImg() {
		return linkImg;
	}

	public void setLinkImg(String linkImg) {
		this.linkImg = linkImg;
	}

}
