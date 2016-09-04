package com.example.jaldeep.help_classes;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

public class LocationService {
    private String userID;

    //Booleans to see if the service is online
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;

    //Booleans that keep track of if the location is received via GPS or Network
    private boolean isGPSTrackingEnable = false;
    private boolean isNetworkTrackingEnabled = false;

    private double longitude, latitude;

    // The minimum distance to change updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Store LocationManager.GPS_PROVIDER or LocationManager.NETWORK_PROVIDER information
    private String provider_info;

    private Context mContext;

    private LocationManager locationManager;

    public LocationService(Context context, String uID) {
        mContext = context;
        userID = uID;
        getLocation();
    }


    public void getLocation() {
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        //Check if the GPS is enabled, and save the value in the isGPSTrackingEnabled variable
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGPSEnabled) {
            isGPSTrackingEnable = true;
            isNetworkTrackingEnabled = false;

            provider_info = LocationManager.GPS_PROVIDER;
        } else if(isNetworkEnabled) {
            isGPSTrackingEnable = false;
            isNetworkTrackingEnabled = false;

            provider_info = LocationManager.NETWORK_PROVIDER;
        }

        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
        }

        if (!provider_info.isEmpty()) {
            locationManager.requestLocationUpdates(provider_info,
                    MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
        }


    }

    /**
     * Check GPS is enabled
     */
    public boolean getIsGPSTrackingEnabled() {
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return this.isGPSEnabled;
    }

    /**
     * Check if Network Provider is enabled
     */
    public boolean getIsNetworkTrackingEnabled() {
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return this.isNetworkEnabled;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude()    {
        return longitude;
    }
}
