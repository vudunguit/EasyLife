package covisoft.android.item;

import com.google.android.maps.GeoPoint;

public class item_MyGeoPoint extends GeoPoint {

	private String description;
	public item_MyGeoPoint(int lat, int lng, String description) {
		super(lat,lng);
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
