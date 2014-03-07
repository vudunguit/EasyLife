package covisoft.android.item;

public class Item_Notification_Read {

	private int id;
	private int status;

	public Item_Notification_Read(int id, int status) {
		super();
		this.id = id;
		this.status = status;
	}

	public Item_Notification_Read() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
