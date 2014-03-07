package lib.etc;

import android.location.Location;

public class lib_calculate_distance {

	// double store_lat = Double.parseDouble(s_latitude);
	// double store_log = Double.parseDouble(s_longitude);

	public static String calculate_distance(double latA, double lngA,
			String latB, String lngB) {
		String distance = "999";
		try {
			double store_lat = Double.parseDouble(latB);
			double store_log = Double.parseDouble(lngB);

			double d_distance = distance(latA, lngA, store_lat, store_log);

			distance = String.format("%.2f", d_distance);
		} catch (Exception ex) {

		}

		return distance;
	}

	public static String calculate_distance(double latA, double lngA,
			double latB, double lngB) {
		String distance = "999";
		try {
			double d_distance = distance(latA, lngA, latB, lngB);

			distance = String.format("%.2f", d_distance);
		} catch (Exception ex) {

		}

		return distance;
	}

	public static double distance(double latA, double lngA, double latB,
			double lngB) {
		double distance = 999;

		try {
			Location locationA = new Location("point A");

			locationA.setLatitude(latA);
			locationA.setLongitude(lngA);

			Location locationB = new Location("point B");

			locationB.setLatitude(latB);
			locationB.setLongitude(lngB);

			distance = locationA.distanceTo(locationB) / 1000;
		} catch (Exception ex) {

		}

		return distance;
	}

	public static double distance(double latA, double lngA, String latB,
			String lngB) {
		double distance = 999;

		try {
			Location locationA = new Location("point A");

			locationA.setLatitude(latA);
			locationA.setLongitude(lngA);

			Location locationB = new Location("point B");

			double store_lat = Double.parseDouble(latB);
			double store_log = Double.parseDouble(lngB);

			locationB.setLatitude(store_lat);
			locationB.setLongitude(store_log);

			distance = locationA.distanceTo(locationB) / 1000;
		} catch (Exception ex) {

		}

		return distance;
	}

}
