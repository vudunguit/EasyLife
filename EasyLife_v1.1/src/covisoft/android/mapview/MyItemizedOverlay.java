/***
 * Copyright (c) 2010 readyState Software Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package covisoft.android.mapview;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

import covisoft.android.EasyLife.R;
import covisoft.android.item.item_MyGeoPoint;
import covisoft.android.item.item_Franchise_Shop;
import covisoft.android.item.item_store_list;
import covisoft.android.tab3_Home.Activity_Item_Map;
import covisoft.android.tab3_Home.Activity_Map_All_Shop;
import covisoft.android.tab3_Home.Activity_Map_All_Franchise;
import covisoft.android.tab4.Activity_HireEmployee;

public class MyItemizedOverlay extends BalloonItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> m_overlays = new ArrayList<OverlayItem>();
	private Context c;
	final MapController mc;
	Activity root;
	private ArrayList<item_MyGeoPoint> all_geo_points;
	
	public MyItemizedOverlay(Drawable defaultMarker, MapView mapView,ArrayList allGeoPoints,
			Activity root) {
		super(boundCenter(defaultMarker), mapView);
		c = mapView.getContext();
		mc = mapView.getController();
		this.all_geo_points = allGeoPoints;
		this.root = root;
	}
	public MyItemizedOverlay(Drawable defaultMarker, MapView mapView, Activity root) {
		super(boundCenter(defaultMarker), mapView);
		c = mapView.getContext();
		mc = mapView.getController();
		this.root = root;
	}
	
    
	public MyItemizedOverlay(Drawable defaultMarker, MapView mapView,ArrayList allGeoPoints) {
		super(boundCenter(defaultMarker), mapView);
		c = mapView.getContext();
		mc = mapView.getController();
		this.all_geo_points = allGeoPoints;
	}
	@Override
    public boolean draw(Canvas canvas, MapView mv, boolean shadow, long when) {
        super.draw(canvas, mv, shadow);
        if(all_geo_points != null) {
        	drawPath(mv, canvas);
        }
        
        return true;
    }
    public void drawPath(MapView mv, Canvas canvas) {
        int xPrev = -1, yPrev = -1, xNow = -1, yNow = -1;
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(4);
        paint.setAlpha(100);
        for (int i = 0; i < all_geo_points.size() - 4; i++) {
            item_MyGeoPoint gp = all_geo_points.get(i);
            Point point = new Point();
            mv.getProjection().toPixels(gp, point);
            xNow = point.x;
            yNow = point.y;
            if (xPrev != -1) {
                canvas.drawLine(xPrev, yPrev, xNow, yNow, paint);
            }
            xPrev = xNow;
            yPrev = yNow;
        }
    }
	public void addOverlay(OverlayItem overlay) {
		m_overlays.add(overlay);
		populate();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return m_overlays.get(i);
	}

	@Override
	public int size() {
		return m_overlays.size();
	}

	@Override
	protected boolean onBalloonTap(int index, OverlayItem item) {
		LinearLayout LinearLayout_address = null;
		TextView address = null;
		if (root != null) {
			LinearLayout_address = (LinearLayout) root.findViewById(R.id.LinearLayout_address);

			address = (TextView) root.findViewById(R.id.textview_address);
		}

		if (LinearLayout_address != null) {
			if (Activity_Map_All_Shop.arItem != null) {

				if (item.getTitle().compareTo(root.getString(R.string.Map_My_Location)) == 0) {
					address.setText(Activity_Map_All_Shop.address);
				} else {
					item_store_list ite;

					for (int i = 0; i < Activity_Map_All_Shop.arItem.size(); i++) {
						ite = Activity_Map_All_Shop.arItem.get(i);
						if (ite.name.compareTo(item.getTitle()) == 0) {
							address.setText(ite.addr);
							i = Activity_Map_All_Shop.arItem.size();

						}

					}
				}
			} else if(Activity_Item_Map.item != null) {
				if (item.getTitle().compareTo(root.getString(R.string.Map_My_Location)) == 0) {
					
					address.setText(Activity_Item_Map.address);
					
				}  else {

					address.setText(Activity_Item_Map.item.addr);

				}
			} else if (Activity_Map_All_Franchise.arItemFranchise != null) {
				if (item.getTitle().compareTo(root.getString(R.string.Map_My_Location)) == 0) {
					address.setText(Activity_Map_All_Franchise.address);
				} else {
					item_Franchise_Shop ite;

					for (int i = 0; i < Activity_Map_All_Franchise.arItemFranchise.size(); i++) {
						ite = Activity_Map_All_Franchise.arItemFranchise.get(i);
						if (ite.getName().compareTo(item.getTitle()) == 0) {
							address.setText(ite.getAddr());
							i = Activity_Map_All_Franchise.arItemFranchise.size();

						}

					}
				}
			}

			LinearLayout_address.setVisibility(View.VISIBLE);
		} else {
			if (Activity_HireEmployee.xml != null) {
				LinearLayout_address = (LinearLayout) Activity_HireEmployee.activity.findViewById(R.id.LinearLayout_address);
				address = (TextView) Activity_HireEmployee.activity.findViewById(R.id.textview_address);
				address.setText("");
			}

			LinearLayout_address.setVisibility(View.VISIBLE);
		}

		return true;
	}

}
