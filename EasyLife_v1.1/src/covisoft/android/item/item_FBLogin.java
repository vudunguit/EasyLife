package covisoft.android.item;

public class item_FBLogin {

	private String result;
	private String username;

	public item_FBLogin() {
		super();
	}

	public item_FBLogin(String result, String username) {
		super();
		this.result = result;
		this.username = username;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public String toString() {
		return "item_FBLogin [result=" + result + ", username=" + username + "]";
	}

}
