package com.esmertec.memo;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Point;
import com.google.googlenav.Placemark;
import com.google.googlenav.Search;
import com.google.googlenav.map.Map;
import com.google.googlenav.map.MapPoint;
import com.google.googlenav.map.Zoom;

public class BrowseMap extends MapActivity {
    private MapView mMapView;

    private String LOG_TAG = "BrowseMap";

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        mMapView = new MapView(this);
        
		LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		Location l = lm.getCurrentLocation("gps");

		Point p = new Point((int) (l.getLatitude() * 1000000), (int)
                (l.getLongitude() * 1000000));
//        // Use Yahoo Geo code to find the lat/long.
//        // Click on the Sample Request URL here for example
//        // http://developer.yahoo.com/maps/rest/V1/geocode.html
//        Point p = new Point((int) (37.416402 * 1000000), (int)
//                (-122.025078 * 1000000));
        MapController mc = mMapView.getController();
        mc.animateTo(p);
        mc.zoomTo(9);
        setContentView(mMapView);

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_I) {
            // Zoom In
            int level = mMapView.getZoomLevel();
            mMapView.getController().zoomTo(level + 1);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_O) {
            // Zoom Out
            int level = mMapView.getZoomLevel();
            mMapView.getController().zoomTo(level - 1);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_S) {
            // Switch on the satellite images
            mMapView.toggleSatellite();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_T) {
            // Switch on traffic overlays
            mMapView.toggleTraffic();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_P) {
            // W00t! Search for Pizza.
            // Create a MapPoint from the map's coordinates
            MapPoint mapPoint = new MapPoint(mMapView.getMapCenter().getLatitudeE6(),
                    mMapView.getMapCenter().getLongitudeE6());
            // Create a dummy Map for use in Search
            Map map = new Map(getDispatcher(), null, 0, 0, 0, mapPoint,
                    Zoom.getZoom(mMapView.getZoomLevel()), 0);
            // Search for Pizza near the specified coordinates
            Search search = new Search("London", map, 0);
            // add the request the dispatcher
            getDispatcher().addDataRequest(search);
            // Wait for the search to complete, Should do this
            // in another thread ideally, this is just for illustration here.
            while (!search.isComplete()) {
                Log.i(LOG_TAG, ".");
            }

            // Print the details.
            Log.i(LOG_TAG, "Done - " + search.numPlacemarks());
            MapPoint point = null;
            for (int i = 0; i < search.numPlacemarks(); i++) {
                Placemark placemark = search.getPlacemark(i);
                point = placemark.getLocation();
                Log.i(LOG_TAG, " - i : " + Integer.toString(i));
                Log.i(LOG_TAG, "- Bubble : " + placemark.getBubbleDescriptor());
                Log.i(LOG_TAG, "- Detail : " + placemark.getDetailsDescriptor());
                Log.i(LOG_TAG, "- Title : " + placemark.getTitle());
                Log.i(LOG_TAG, "- Location : " + placemark.getLocation().toString());
                Log.i(LOG_TAG, "- routable : " + placemark.routableString());
            }

            // Animate to the last location.
            if (point != null) {
                MapController mc = mMapView.getController();
                Point point1 = new Point(point.getLatitude(), point.getLongitude());
                mc.animateTo(point1);
                mc.zoomTo(12);
                Log.i("animateTo", point1.toString());
            }
            return true;
        }else{
        	finish();
        	
        }
        return false;
    }
}
