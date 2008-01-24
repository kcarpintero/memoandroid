package com.esmertec.memo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;

import com.google.android.maps.Overlay;
import com.google.android.maps.Point;

public class DestnationOverlay extends Overlay {

	private Point mPointDest;
	private Bitmap mBitmap;

	private BrowseMap mMap;

	// public DestnationOverlay(Point dest, Bitmap icon) {
	// mPointDest = dest;
	// mBitmap = icon;
	// }

	private Rect mRectDest;

	public DestnationOverlay(Point dest, Bitmap icon, BrowseMap map) {
		mPointDest = dest;
		mBitmap = icon;
		mMap = map;
	}

	@Override
	public void draw(Canvas canvas, PixelCalculator calculator, boolean shadow) {
		super.draw(canvas, calculator, shadow);
		int[] oldcoords = new int[2];
		calculator.getPointXY(mPointDest, oldcoords);
		oldcoords[0] -= mBitmap.width() / 2;
		oldcoords[1] -= mBitmap.height();
		canvas.drawBitmap(mBitmap, oldcoords[0], oldcoords[1], new Paint());

		mRectDest = new Rect(oldcoords[0], oldcoords[1], oldcoords[0]
				+ mBitmap.width(), oldcoords[1] + mBitmap.height());
		int[] coords = new int[2];
		calculator.getPointXY(mMap.getCenterPoint(), coords);
		if (mRectDest.contains(coords[0], coords[1])) {
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(0xff000000);
			paint.setTextAlign(Align.CENTER);
			canvas.drawText(mMap.getDestLabel(), oldcoords[0] + mBitmap.width()
					/ 2, oldcoords[1], paint);

		}

	}

}
