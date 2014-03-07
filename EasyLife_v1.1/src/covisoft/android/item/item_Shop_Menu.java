package covisoft.android.item;

import android.os.Parcel;
import android.os.Parcelable;

public class item_Shop_Menu implements Parcelable {
	public item_Shop_Menu(String name, String price) {

		this.name = name;
		this.price = price;
	}

	public String name = "";
	public String price = "";

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub

	}
}
