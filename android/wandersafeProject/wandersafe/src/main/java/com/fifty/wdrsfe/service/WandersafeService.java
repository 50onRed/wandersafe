package com.fifty.wdrsfe.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.fifty.wdrsfe.R;
import com.fifty.wdrsfe.Utils;
import com.fifty.wdrsfe.activity.Main;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class WandersafeService extends Service implements LocationListener {

    private final static String TAG = "WandersafeService";

    private final static String BASE_URL = "";

    //TODO - Configure the radius / alert threshold
    private final static int RADIUS = 100;
    private final static int THRESHOLD = 3;

    private LocationManager locationManager;
    private NotificationManager notificationManager;

    private int lastLevel = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        this.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //Register for GPS updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
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
        /**
         * TODO:
         *
         * 1. Check to see if there are any current alerts based on the location
         * 2. Load the radius threshold from the shared preferences.
         * 3. Pass that & the location to the api.
         * 4. Check the level against the user's preferences. If it is >=, create an alert.
         */

        int level = getAlertLevel(location.getLatitude(), location.getLongitude(), RADIUS);
        if(level > lastLevel && level > THRESHOLD) {
            lastLevel = level;
            createNotification();
        }
        else if(level > 0) {
            lastLevel = level;
        }
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
            JSONObject jsonObject = Utils.getJSONFromURL(BASE_URL + "?latitude=" + latitude + "&longitude=" + longitude + "&radius=" + radius);
            level = jsonObject.getInt("level");
        } catch (IOException e) {
            Log.e(TAG, "Unable to retrieve data", e);
        } catch (JSONException e) {
            Log.e(TAG, "Unable to retrieve data", e);
        }

        return level;
    }
}
