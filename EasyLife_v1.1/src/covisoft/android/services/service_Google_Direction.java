package covisoft.android.services;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.util.Log;
import covisoft.android.item.item_MyGeoPoint;

public class service_Google_Direction {

	public Activity act = null;

	String url = "";

	public service_Google_Direction(String lat1, String lng1, String lat2, String lng2, Activity act) {
		this.act = act;
		url = "http://maps.googleapis.com/maps/api/directions/xml?origin=" + lat1 + "," + lng1 + "&destination=" + lat2 + "," + lng2 + "&sensor=false&units=metric" + "&language=vi";

		Log.e("get Direction", url);
	}

	public ArrayList<item_MyGeoPoint> getDirections() {
  
		String tag[] = { "lat", "lng", "html_instructions" };
		ArrayList<item_MyGeoPoint> list_of_geopoints = new ArrayList<item_MyGeoPoint>();
		HttpResponse response = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(url);
			response = httpClient.execute(httpPost, localContext);
			InputStream in = response.getEntity().getContent();
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(in);
			if (doc != null) {
				NodeList nl1, nl2, nl3;
				nl1 = doc.getElementsByTagName(tag[0]);
				nl2 = doc.getElementsByTagName(tag[1]);
				nl3 = doc.getElementsByTagName(tag[2]);
				if (nl1.getLength() > 0) {

					// list_of_geopoints = new ArrayList();
					for (int i = 0; i < nl1.getLength(); i++) {
						Node node1 = nl1.item(i);
						Node node2 = nl2.item(i);
						String desc = "";
						if (nl3.getLength() > i) {
							Node node3 = nl3.item(i);
							desc = node3.getTextContent();
							while (desc.contains("<")) {
								int first = desc.indexOf('<');
								int last = desc.indexOf('>');
								String tmp = desc.substring(first, last + 1);
								desc = desc.replace(tmp, "");
								desc = desc.replace("&nbsp;", "");
							}
						}

						double lat = Double.parseDouble(node1.getTextContent());
						double lng = Double.parseDouble(node2.getTextContent());

						if (!desc.equals("")) {
							list_of_geopoints.add(new item_MyGeoPoint((int) (lat * 1E6), (int) (lng * 1E6), desc));
						} else {
							list_of_geopoints.add(new item_MyGeoPoint((int) (lat * 1E6), (int) (lng * 1E6), ""));
						}

					}
				} else {
					// No points found
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list_of_geopoints;
	}

	public String getDistance() {
		Double distance = 0.0;
		String tag = "text";
		ArrayList<String> arr = new ArrayList<String>();
		HttpResponse response = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(url);
			response = httpClient.execute(httpPost, localContext);
			InputStream in = response.getEntity().getContent();
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(in);
			if (doc != null) {
				NodeList nl1;
				nl1 = doc.getElementsByTagName(tag);
				if (nl1.getLength() > 0) {

					for (int i = 0; i < nl1.getLength(); i++) {
						Node node1 = nl1.item(i);

						arr.add(node1.getTextContent());
					}
				} else {
					// No points found
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < arr.size(); i++) {
			if (arr.get(i).contains("km")) {
				String tmp = arr.get(i).replace(" km", "");
				tmp = tmp.replace(",", ".");
				Double doub = Double.parseDouble(tmp);
				if (doub > distance) {
					distance = doub;
				}
			}
		}

		return distance + "";
	}

}
