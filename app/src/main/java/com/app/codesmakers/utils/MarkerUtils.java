package com.app.codesmakers.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLngBounds;

public class MarkerUtils {
    public static Bitmap getBitmapOfDesiredWidth(Resources resources, int resourceId, int sizeInPx) {
        Bitmap b = BitmapFactory.decodeResource(resources, resourceId);
        float bitmapWidth = b.getWidth();
        float bitmapHeight = b.getHeight();
        float aspectRatio = bitmapHeight / bitmapWidth;
        int desiredHeight = (int) (sizeInPx * aspectRatio);
        return Bitmap.createScaledBitmap(b, sizeInPx, desiredHeight, false);
    }

    public static boolean areBoundsTooSmall(LatLngBounds bounds, int minDistanceInMeter) {
        float[] result = new float[1];
        try {
            Location.distanceBetween(bounds.southwest.latitude, bounds.southwest.longitude, bounds.northeast.latitude, bounds.northeast.longitude, result);
            return result[0] < minDistanceInMeter;
        } catch (Exception e) {
            return true;
        }

    }
}
