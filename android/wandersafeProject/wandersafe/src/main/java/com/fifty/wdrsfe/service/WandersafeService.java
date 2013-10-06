package com.fifty.wdrsfe.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.fifty.wdrsfe.R;
import com.fifty.wdrsfe.Utils;
import com.fifty.wdrsfe.activity.Main;

public class WandersafeService extends Service implements LocationListener {

    private final static String TAG = "WandersafeService";

    private final static String BASE_URL = "http://www.wandersafe.com/";

    private int radius = 100;
    private int threshold = 3;
    private boolean notificationsEnabled = true;


    private LocationManager locationManager;
    private NotificationManager notificationManager;

    private int lastLevel = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        this.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        loadPreferences();
        registerLocationListener();
    }

    @Override
    public void onDestroy() {
        if(locationManager != null) {
            locationManager.removeUpdates(this);
        }
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        int level = getAlertLevel(location.getLatitude(), location.getLongitude(), radius);
        if(level == -1)
            return;

        if(level <= threshold && level < lastLevel) {
            //Better hide that wallet!
            createNotification();
        }
        else if(level > threshold) {
            //Clear the notification. Safer neighborhood now.
            clearNotification();
        }
        lastLevel = level;
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

    /**
     * Creates the notification.
     */
    private void createNotification() {
        if(!notificationsEnabled)
            return;

        Intent intent = new Intent(this, Main.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification notification = new Notification.Builder(this)
                .setContentTitle(getString(R.string.alert_msg))
                .setContentIntent(pendingIntent)
                .setSound(soundUri)
                .build();

        notificationManager.notify(TAG, 0, notification);
    }

    private void clearNotification(){
        notificationManager.cancel(TAG, 0);
    }

    /**
     * Gets the alert level based on the geo location and the radius
     * @param latitude
     * @param longitude
     * @param radius
     * @return
     */
    private int getAlertLevel(double latitude, double longitude, int radius) {
        int level = -1;
        try {
            String response = Utils.getResponseFromURL(BASE_URL + latitude + "/" + longitude + "/" + radius);
            if(response != null)
                level = Integer.parseInt(response.trim());
        }
        catch (Exception e) {
            Log.e(TAG, "Could not parse level", e);
        }
        return level;
    }

    private void registerLocationListener() {
        //Register for GPS updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, this);
    }

    private void loadPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.threshold = prefs.getInt("threshold", 3);
        this.radius = prefs.getInt("radius", 600);
        this.notificationsEnabled = prefs.getBoolean("notificationsEnabled", true);
    }
}
