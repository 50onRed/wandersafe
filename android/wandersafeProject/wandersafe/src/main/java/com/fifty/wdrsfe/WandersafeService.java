package com.fifty.wdrsfe;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class WandersafeService extends Service implements LocationListener {

    private LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onDestroy() {
        locationManager.removeUpdates(this);
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        /**
         * TODO:
         *
         * 1. Check to see if there are any current alerts based on the location
         * 2. Load the radius threshold from the shared preferences.
         * 3. Pass that & the location to the api.
         * 4. Check the level against the user's preferences. If it is >=, create an alert.
         */
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //Not Implemented
    }

    @Override
    public void onProviderEnabled(String provider) {
        //Not Implemented
    }

    @Override
    public void onProviderDisabled(String provider) {
        //Not Implemented
    }

    private void createNotification(){

    }
}
