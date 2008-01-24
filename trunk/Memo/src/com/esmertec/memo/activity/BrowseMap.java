package com.esmertec.memo.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.SubMenu;
import android.view.Menu.Item;

import com.esmertec.memo.Constants;
import com.esmertec.memo.R;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Point;
import com.google.googlenav.Placemark;
import com.google.googlenav.Search;
import com.google.googlenav.map.Map;
import com.google.googlenav.map.MapPoint;
import com.google.googlenav.map.Zoom;
import com.google.wireless.gdata.data.StringUtils;

public class BrowseMap extends MapActivity {
	private MapView mMapView;

	private String LOG_TAG = "BrowseMap";

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		mMapView = new MapView(this);

		Intent intent = getIntent();

		Point p = null;

		boolean b = true;
		if (intent != null) {
			String loca_txt = (String) intent
					.getExtra(Constants.PREF_LOCATION_TEXT);
			if (loca_txt != null) {
				int i = loca_txt.indexOf('@');
				if (i >= 0) {

					String[] lat_lon = loca_txt.substring(i + 1,
							loca_txt.length()).split(",");
					for (String str : lat_lon) {
						Log.v("qinyu", str);
					}

					if (lat_lon.length >= 2) {
						try {
							double lat = Double.parseDouble(lat_lon[0]);
							double lon = Double.parseDouble(lat_lon[1]);
							p = new Point((int) (lat * 1000000),
									(int) (lon * 1000000));
							b = false;
						} catch (Exception e) {

						}
					}
				}
			}
		}

		if (b) {
			LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
			Location l = lm.getCurrentLocation("gps");

			p = new Point((int) (l.getLatitude() * 1000000), (int) (l
					.getLongitude() * 1000000));
		}
		// // Use Yahoo Geo code to find the lat/long.
		// // Click on the Sample Request URL here for example
		// // http://developer.yahoo.com/maps/rest/V1/geocode.html
		// Point p = new Point((int) (37.416402 * 1000000), (int)
		// (-122.025078 * 1000000));
		MapController mc = mMapView.getController();
		mc.animateTo(p);
		mc.zoomTo(9);

		mMapView.createOverlayController().add(
				new DestnationOverlay(p, BitmapFactory.decodeResource(
						getResources(), R.drawable.dest), this), true);

		setContentView(mMapView);

	}

	private static final int MENU_FIND = 1;
	private static final int MENU_ZOOM_IN = 2;
	private static final int MENU_ZOOM_OUT = 3;
	private static final int MENU_VIEW = 4;
	private static final int MENU_SATELLITE = 5;
	private static final int MENU_TRAFFIC = 6;
	private static final int MENU_WEATHER = 7;
	private static final int MENU_CONTACT = 8;
	private static final int MENU_SAVE_LOCATION = 9;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.FIRST, MENU_FIND, "Find...").setShortcut(
				KeyEvent.KEYCODE_0, 0, KeyEvent.KEYCODE_F);
		menu.add(Menu.FIRST, MENU_ZOOM_IN, "Zoom In").setShortcut(
				KeyEvent.KEYCODE_1, 0, KeyEvent.KEYCODE_I);
		menu.add(Menu.FIRST, MENU_ZOOM_OUT, "Zoom Out").setShortcut(
				KeyEvent.KEYCODE_3, 0, KeyEvent.KEYCODE_O);
		SubMenu subMenuView = menu.addSubMenu(Menu.FIRST, MENU_VIEW, "View");

		subMenuView.add(SubMenu.FIRST, MENU_SATELLITE, "Satellite")
				.setShortcut(KeyEvent.KEYCODE_7, 0, KeyEvent.KEYCODE_S);
		subMenuView.add(SubMenu.FIRST, MENU_TRAFFIC, "Trafic").setShortcut(
				KeyEvent.KEYCODE_9, 0, KeyEvent.KEYCODE_T);
		subMenuView.add(SubMenu.FIRST, MENU_WEATHER, "Weather").setShortcut(
				KeyEvent.KEYCODE_STAR, 0, KeyEvent.KEYCODE_W);
		subMenuView.add(SubMenu.FIRST, MENU_CONTACT, "Contacts").setShortcut(
				KeyEvent.KEYCODE_POUND, 0, KeyEvent.KEYCODE_C);

		menu.add(Menu.FIRST, MENU_SAVE_LOCATION, "Save Location").setShortcut(
				KeyEvent.KEYCODE_5, 0, KeyEvent.KEYCODE_L);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(Item item) {
		switch (item.getId()) {
		case MENU_ZOOM_IN:
			zoomIn();
			return true;
		case MENU_ZOOM_OUT:
			zoomOut();
			return true;
		case MENU_SATELLITE:
			mMapView.toggleSatellite();
			return true;
		case MENU_TRAFFIC:
			mMapView.toggleTraffic();
		case MENU_FIND:
			// TODO Add find places here
			return true;
		case MENU_WEATHER:
			// TODO Get weather info
			return true;
		case MENU_CONTACT:
			// TODO Get Contacts info
			return true;
		case MENU_SAVE_LOCATION:
			saveLocation();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void saveLocation() {
		Intent intent = new Intent(this, SaveLocation.class);
		startSubActivity(intent, 0);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			String data, Bundle extras) {
		if (resultCode == RESULT_OK) {

			double lat = mMapView.getMapCenter().getLatitudeE6() / 1000000;
			double lon = mMapView.getMapCenter().getLongitudeE6() / 1000000;
			String label_text = data + "@" + lat + "," + lon;
			setResult(RESULT_OK, label_text);
			finish();
		}
	}

	private void zoomOut() {
		mMapView.getController().zoomTo(mMapView.getZoomLevel() - 1);
	}

	private void zoomIn() {
		mMapView.getController().zoomTo(mMapView.getZoomLevel() + 1);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_I) {
			zoomIn();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_O) {
			zoomOut();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_S) {
			// Switch on the satellite images
			mMapView.toggleSatellite();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_T) {
			// Switch on traffic overlays
			mMapView.toggleTraffic();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			int step = mMapView.getLatitudeSpan() / 10;
			mMapView.getController().animateTo(
					new Point(mMapView.getMapCenter().getLatitudeE6() + step,
							mMapView.getMapCenter().getLongitudeE6()));
			return true;

		} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			int step = mMapView.getLatitudeSpan() / 10;
			mMapView.getController().animateTo(
					new Point(mMapView.getMapCenter().getLatitudeE6() - step,
							mMapView.getMapCenter().getLongitudeE6()));
			return true;

		} else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			int step = mMapView.getLongitudeSpan() / 10;
			mMapView.getController().animateTo(
					new Point(mMapView.getMapCenter().getLatitudeE6(), mMapView
							.getMapCenter().getLongitudeE6()
							- step));
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			int step = mMapView.getLongitudeSpan() / 10;
			mMapView.getController().animateTo(
					new Point(mMapView.getMapCenter().getLatitudeE6(), mMapView
							.getMapCenter().getLongitudeE6()
							+ step));
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			saveLocation();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_P) {
			// W00t! Search for Pizza.
			// Create a MapPoint from the map's coordinates
			MapPoint mapPoint = new MapPoint(mMapView.getMapCenter()
					.getLatitudeE6(), mMapView.getMapCenter().getLongitudeE6());
			// Create a dummy Map for use in Search
			Map map = new Map(getDispatcher(), null, 0, 0, 0, mapPoint, Zoom
					.getZoom(mMapView.getZoomLevel()), 0);
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
				Log
						.i(LOG_TAG, "- Detail : "
								+ placemark.getDetailsDescriptor());
				Log.i(LOG_TAG, "- Title : " + placemark.getTitle());
				Log.i(LOG_TAG, "- Location : "
						+ placemark.getLocation().toString());
				Log.i(LOG_TAG, "- routable : " + placemark.routableString());
			}

			// Animate to the last location.
			if (point != null) {
				MapController mc = mMapView.getController();
				Point point1 = new Point(point.getLatitude(), point
						.getLongitude());
				mc.animateTo(point1);
				mc.zoomTo(12);
				Log.i("animateTo", point1.toString());
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public Point getCenterPoint() {

		return mMapView.getMapCenter();
	}

	public String getDestLabel() {
		Intent intent = getIntent();

		String str = null;
		if (intent != null) {
			String loca_txt = (String) intent
					.getExtra(Constants.PREF_LOCATION_TEXT);
			str = loca_txt.split("@")[0];
		}
		if (StringUtils.isEmpty(str)) {
			str = "Destnation";
		}
		return str;
	}
}
