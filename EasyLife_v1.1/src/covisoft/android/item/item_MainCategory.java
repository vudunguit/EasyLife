package covisoft.android.item;

public class item_MainCategory {

	private String categoryName;       // Name
	private String category_code;      // ID  
	private int quantity;              // Quantity of shop in this category

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategory_code() {
		return category_code;
	}

	public void setCategory_code(String category_code) {
		this.category_code = category_code;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public item_MainCategory(String categoryName, String category_code,
			int quantity) {
		super();
		this.categoryName = categoryName;
		this.category_code = category_code;
		this.quantity = quantity;
	}

	public item_MainCategory() {
		super();
	}

}
