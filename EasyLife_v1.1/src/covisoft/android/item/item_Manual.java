package covisoft.android.item;

public class item_Manual {

	private String id;      // ID
	private String title;   // Title
	private String link;    // Link to video - url

	public item_Manual(String id, String title, String link) {
		super();
		this.id = id;
		this.title = title;
		this.link = link;
	}

	public item_Manual() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
