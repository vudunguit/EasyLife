package covisoft.android.item;

public class item_Franchise {
	
	public String categoryNoBasic = "";  // Parent category ID
	public String categoryNo = "";       // Category ID
	public String categoryName = "";     // Category Name
	public String franchiseNo = "";      // Franchise ID
	public String franchiseName = "";    // Franchise Name
	public String s_logo = "";           // Logo - url
	
	public item_Franchise(String categoryNoBasic, String categoryNo,
			String categoryName, String franchiseNo, String franchiseName, String s_logo) {

		this.categoryNoBasic = categoryNoBasic;
		this.categoryNo = categoryNo;
		this.categoryName = categoryName;
		this.franchiseNo = franchiseNo;
		this.franchiseName = franchiseName;
		this.s_logo = s_logo;
	}

}
