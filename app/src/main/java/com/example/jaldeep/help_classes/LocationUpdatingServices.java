package com.example.jaldeep.help_classes;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class LocationUpdatingServices extends Service {
    private String mUserID;

    private LocationService mLocationService;
    private ServerCommunication mServerCommunication;

    private Thread mUpdatingThread;
    private boolean mServiceRunning = false;

    public LocationUpdatingServices()   {

    }


    public LocationUpdatingServices(String userID) {
        mServiceRunning = false;
        mUserID = userID;
    }


    @Override
    public void onCreate() {
        mLocationService = new LocationService(this, mUserID);
        mServerCommunication = new ServerCommunication();

        mUpdatingThread = new Thread() {

            @Override
            public void run() {

                while (mServiceRunning) {
                    String lat = mLocationService.getLatitude() + "";
                    String lng = mLocationService.getLongitude() + "";

                    mServerCommunication.publishLocation(lat, lng, mUserID);

                    try {
                        if(Double.parseDouble(lat) == 0 && Double.parseDouble(lng) == 0) {
                            Thread.sleep(5 * 1000);
                        } else  {
                            Thread.sleep(60 * 1000);
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }
        };
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mUserID = intent.getStringExtra("UserID");
        mServiceRunning = true;
        mUpdatingThread.start();
        return 0;
    }

    @Override
    public void onDestroy() {
        mServiceRunning = false;
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void updateUserID(String newUserID) {
        mUserID = newUserID;
    }
}
