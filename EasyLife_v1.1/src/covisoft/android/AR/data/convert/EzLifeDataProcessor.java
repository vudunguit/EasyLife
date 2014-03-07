/*
 * Copyright (C) 2012- Peer internet solutions & Finalist IT Group
 * 
 * This file is part of mixare.
 * 
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details. 
 * 
 * You should have received a copy of the GNU General Public License along with 
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
package covisoft.android.AR.data.convert;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.mixare.lib.HtmlUnescape;
import org.mixare.lib.marker.Marker;

import covisoft.android.AR.EzLifeMarker;
import covisoft.android.AR.data.DataHandler;
import covisoft.android.tab3_Home.Activity_Home_List;

/**
 * A data processor for custom urls or data, Responsible for converting raw data (to json and then) to marker data.
 * @author A. Egal
 */
public class EzLifeDataProcessor extends DataHandler implements DataProcessor{

	public static final int MAX_JSON_OBJECTS = 1000;
	
	@Override
	public String[] getUrlMatch() {
		String[] str = new String[0]; //only use this data source if all the others don't match
		return str;
	}

	@Override
	public String[] getDataMatch() {
		String[] str = new String[0]; //only use this data source if all the others don't match
		return str;
	}
	
	@Override
	public boolean matchesRequiredType(String type) {
		return true; //this datasources has no required type, it will always match.
	}

	@Override
	public List<Marker> load(String rawData, int taskId, int colour) throws JSONException {
		
		List<Marker> markers = new ArrayList<Marker>();
		for (int i = 0 ; i < Activity_Home_List.arItem.size(); i++) {
			Marker ma = new EzLifeMarker(
					Activity_Home_List.arItem.get(i).no+"",
					HtmlUnescape.unescapeHTML(Activity_Home_List.arItem.get(i).name, 0), 
					Double.parseDouble(Activity_Home_List.arItem.get(i).lat), 
					Double.parseDouble(Activity_Home_List.arItem.get(i).lng), 
					7.0, 
					Activity_Home_List.arItem.get(i).s_logo, 
					taskId,
					colour,
					Activity_Home_List.arItem.get(i).s_logo
					);
		
			markers.add(ma);
		}
		
		return markers;
	}
}
