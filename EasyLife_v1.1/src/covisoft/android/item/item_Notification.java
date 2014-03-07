package covisoft.android.item;

/*
 * This model use to show list notification in tab 5
 */
public class item_Notification {

	private int id;
	private String title;          // Title
	private String description;    // Content
	private int radius;            // Distance to show
	private int kind;              // Notification kype: 1,2,3 - Shop's notify; 4 - System notification 
	private int categoryNoBasic;   // Category ID
	private String idcompany;      // Shop ID
	private String idfranchise;    // Franchise ID
	private int status;            // Status: 0 - not view; 1: view

	
	public item_Notification(int id, String title, String description, int radius, int kind, int categoryNoBasic, String idcompany, String idfranchise, int status) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.radius = radius;
		this.kind = kind;
		this.categoryNoBasic = categoryNoBasic;
		this.idcompany = idcompany;
		this.idfranchise = idfranchise;
		this.status = status;
	}

	public item_Notification() {
		super();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getKind() {
		return kind;
	}

	public void setKind(int kind) {
		this.kind = kind;
	}

	public int getCategoryNoBasic() {
		return categoryNoBasic;
	}

	public void setCategoryNoBasic(int categoryNoBasic) {
		this.categoryNoBasic = categoryNoBasic;
	}

	public String getIdcompany() {
		return idcompany;
	}

	public void setIdcompany(String idcompany) {
		this.idcompany = idcompany;
	}

	public String getIdfranchise() {
		return idfranchise;
	}

	public void setIdfranchise(String idfranchise) {
		this.idfranchise = idfranchise;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
