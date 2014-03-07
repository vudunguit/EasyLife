package covisoft.android.item;

public class item_User {

	private String username;
	private String email;
	private String name;
	private String birthday;
	private String gender;
	private String tel;
	private String address;
	private String imageUrl;

	public item_User() {
		super();
	}

	public item_User(String username, String email, String name, String birthday, String gender, String tel, String address, String imageUrl) {
		super();
		this.username = username;
		this.email = email;
		this.name = name;
		this.birthday = birthday;
		this.gender = gender;
		this.tel = tel;
		this.address = address;
		this.imageUrl = imageUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return "item_User [username=" + username + ", email=" + email + ", name=" + name + ", birthday=" + birthday + ", gender=" + gender + ", tel=" + tel + ", address=" + address + ", imageUrl=" + imageUrl + "]";
	}

}
