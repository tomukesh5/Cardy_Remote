package com.cardyapp.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.cardyapp.App.Cardy;
import com.cardyapp.Models.Userdata;
import com.cardyapp.Utils.AppConstants;
import com.cardyapp.Utils.CardySingleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationService extends Service {

    private Cardy app;
    private Userdata userdata;
    private Handler handler;
    private LocationManager locationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler(Looper.getMainLooper());
        app = (Cardy) getApplication();
        userdata = app.getPreferences().getLoggedInUser(app);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        initLocationManager();

        return START_NOT_STICKY;
    }

    void initLocationManager() {

        handler.post(new Runnable() {
            @Override
            public void run() {
                locationManager = (LocationManager) app.getSystemService(Context.LOCATION_SERVICE);

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (ActivityCompat.checkSelfPermission(app, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(app, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            AppConstants.LOCATION_UPDATE_INTERVAL_MILLIS,
                            AppConstants.LOCATION_UPDATE_DISTANCE_METERS,
                            locationListener);
                }
            }
        });
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (null != location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                updateLocation(latitude,longitude);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    public void updateLocation(final double lat, final double lng) {
        // TODO: 12/30/2017 update location api integration
        CardySingleton.getInstance().callToUpdateUserLocationAPI(userdata.getUserid(), lat + "", lng + "", new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    @Override
    public void onDestroy() {
        locationManager.removeUpdates(locationListener);
        stopSelf();
        super.onDestroy();
    }
}
