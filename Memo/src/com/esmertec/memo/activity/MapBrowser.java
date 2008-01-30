package com.esmertec.memo.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.ContentURI;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.SubMenu;
import android.view.Menu.Item;

import com.esmertec.memo.R;
import com.esmertec.memo.provider.Memo;
import com.esmertec.memo.provider.MemoProvider;
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

public class MapBrowser extends MapActivity {

	private int mState;

	private static final int STATE_BROWSE = 0;
	private static final int STATE_SET_LOCATION = 1;

	private ContentURI mURI;

	private MapView mMapView;

	private DestnationOverlay mDestOverlay;

	private static final String TAG = MapBrowser.class.getName();

	@Override
	public void onCreate(Bundle icicle) {

		super.onCreate(icicle);

		mMapView = new MapView(this);

		mState = STATE_BROWSE;

		Point p = null;

		Intent intent = getIntent();
		if (intent != null) {
			if (intent.getAction().equals(MemoProvider.ACTION_SET_LOCATION)) {
				mURI = intent.getData();
				if (mURI != null) {
					Cursor cursor = managedQuery(mURI, new String[] {
							Memo.Memos.LOCATION, Memo.Memos._ID }, null, null);
					mState = STATE_SET_LOCATION;
					cursor.first();
					String str = cursor.getString(0);
					p = locationStringToPoint(str);
					mDestOverlay = new DestnationOverlay(p, BitmapFactory
							.decodeResource(getResources(), R.drawable.dest),
							this);
					mMapView.createOverlayController().add(mDestOverlay, true);

				}
			}
		}

		if (p == null) {
			p = currentLocationToPoint();
		}

		MapController mc = mMapView.getController();
		mc.animateTo(p);
		mc.zoomTo(9);

		setContentView(mMapView);

	}

	private Point locationStringToPoint(String loca_txt) {
		if (StringUtils.isEmpty(loca_txt)) {
			return null;
		}
		int i = loca_txt.indexOf('@');
		if (i < 0) {
			return null;
		}

		String[] lat_lon = loca_txt.substring(i + 1, loca_txt.length()).split(
				",");

		if (lat_lon.length < 2) {
			return null;
		}

		try {
			int lat = Integer.parseInt(lat_lon[0]);
			int lon = Integer.parseInt(lat_lon[1]);
			return new Point((int) (lat), (int) (lon));
		} catch (Exception e) {
			return null;
		}

	}

	private Point currentLocationToPoint() {

		LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		Location l = lm.getCurrentLocation("gps");

		return new Point((int) (l.getLatitude() * 1000000), (int) (l
				.getLongitude() * 1000000));
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

		if (mState == STATE_SET_LOCATION) {
			menu.add(Menu.FIRST, MENU_SAVE_LOCATION, "Save Location")
					.setShortcut(KeyEvent.KEYCODE_5, 0, KeyEvent.KEYCODE_L);
		}

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
		if (mState == STATE_SET_LOCATION) {
			int lat = mMapView.getMapCenter().getLatitudeE6() ;
			int lon = mMapView.getMapCenter().getLongitudeE6();

			Cursor cursor = managedQuery(mURI, new String[] {
					Memo.Memos.LOCATION, Memo.Memos._ID }, null, null);

			cursor.first();

			String label = cursor.getString(0);
			if (StringUtils.isEmpty(label)) {
				label = "";
			} else {
				label = label.substring(0, label.indexOf('@'));
			}
			String label_text = "@" + lat + "," + lon;
			Log.v(TAG, "Add GEO:" + mMapView.getMapCenter().getLatitudeE6());
			Log.v(TAG, "Add GEO:" + label_text);

			cursor.updateString(0, label_text);
			managedCommitUpdates(cursor);
			cursor.deactivate();

			Intent intent = new Intent(this, SaveLocation.class);

			startSubActivity(intent, 0);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			String data, Bundle extras) {
		if (resultCode == RESULT_OK) {

			Cursor cursor = managedQuery(mURI, new String[] {
					Memo.Memos.LOCATION, Memo.Memos._ID }, null, null);

			cursor.first();
			
			String label = cursor.getString(0);
			if (StringUtils.isEmpty(label)) {
				label = "";
			} else {
				label = label.substring(label.indexOf('@'), label.length());
			}

			String label_text = data + label;
			
			Log.v(TAG, "Add label:" + label_text);
			
			cursor.updateString(0, label_text);
			managedCommitUpdates(cursor);
			cursor.deactivate();

			Point p = locationStringToPoint(label_text);

			mDestOverlay.setDestPoint(p);

			MapController mc = mMapView.getController();
			mc.animateTo(p);

			// setResult(RESULT_OK, label_text);
			// finish();
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
				Log.i(TAG, ".");
			}

			// Print the details.
			Log.i(TAG, "Done - " + search.numPlacemarks());
			MapPoint point = null;
			for (int i = 0; i < search.numPlacemarks(); i++) {
				Placemark placemark = search.getPlacemark(i);
				point = placemark.getLocation();
				Log.i(TAG, " - i : " + Integer.toString(i));
				Log.i(TAG, "- Bubble : " + placemark.getBubbleDescriptor());
				Log.i(TAG, "- Detail : " + placemark.getDetailsDescriptor());
				Log.i(TAG, "- Title : " + placemark.getTitle());
				Log
						.i(TAG, "- Location : "
								+ placemark.getLocation().toString());
				Log.i(TAG, "- routable : " + placemark.routableString());
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

		Cursor cursor = managedQuery(mURI, new String[] { Memo.Memos.LOCATION,
				Memo.Memos._ID }, null, null);

		cursor.first();

		String loca_txt = cursor.getString(0);
		cursor.deactivate();

		String str = null;
		if (!StringUtils.isEmpty(loca_txt)) {
			str = loca_txt.split("@")[0];
		}
		if (StringUtils.isEmpty(str)) {
			str = "Destination";
		}
		return str;
	}
}
