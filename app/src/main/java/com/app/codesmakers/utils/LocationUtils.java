package com.app.codesmakers.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Looper;

import com.app.codesmakers.utils.session.Keys;
import com.app.codesmakers.utils.session.SessionManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import static androidx.core.app.ActivityCompat.requestPermissions;

public class LocationUtils {
    private static FusedLocationProviderClient mFusedLocationClient;
    private static LocationRequest locationRequest;
    private static LocationCallback locationCallback;
    private static boolean isTesting = false;

    public static void fetchCurrentLocation(Context context, OnLocationFetchListener onLocationFetchListener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                onLocationFetchListener.permissionDenied();
                return;
            }
        }

        if (mFusedLocationClient == null) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(1000); // 1 seconds
            locationRequest.setFastestInterval(1000); // 1 seconds

            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {
                            if(location.getLatitude() > 0) {
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                if (isTesting)
                                    latLng = new LatLng(34.764307, 76.194857);
                                SessionManager.getInstance().saveLastLocation(latLng);
                                onLocationFetchListener.locationReceived(latLng);
                                mFusedLocationClient.removeLocationUpdates(locationCallback);
                            }
                            break;
                        }
                    }
                }
            };
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                if(location.getLatitude() > 0) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    if (isTesting)
                        latLng = new LatLng(24.004307, 38.194857);
                    SessionManager.getInstance().saveLastLocation(latLng);
                    onLocationFetchListener.locationReceived(latLng);
                }
            } else {
                mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            }
        }).addOnFailureListener(e -> {
            LatLng latLng = SessionManager.getInstance().getLastLocation();
            if(isTesting)
                latLng = new LatLng(24.004307,38.194857);
            onLocationFetchListener.errorInFetchLocation(e,latLng);
        });

    }
}
