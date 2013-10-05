package com.fifty.wdrsfe;

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
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WandersafeService extends Service implements LocationListener {

    private final static String TAG = "WandersafeService";

    private final static String BASE_URL = "";

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

    /**
     * Creates the notification.
     */
    private void createNotification() {
        Intent intent = new Intent(this, Main.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("Dangerous Area")
                .setContentIntent(pendingIntent)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
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
            JSONObject jsonObject = getJSONFromURL(BASE_URL + "?latitude="+latitude+"&longitude="+longitude+"&radius="+radius);
            level = jsonObject.getInt("level");
        } catch (IOException e) {
            Log.e(TAG, "Unable to retrieve data", e);
        } catch (JSONException e) {
            Log.e(TAG, "Unable to retrieve data", e);
        }

        return level;
    }

    public static JSONObject getJSONFromURL(String url) throws ClientProtocolException, IOException, JSONException {
        JSONObject responseJSON = null;
        InputStream in = null;

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            in = httpResponse.getEntity().getContent();

            //If invalid response type, throw exception
            if(httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new IOException("Unable to get JSON from URL: "+url);
            }

            StringBuilder response = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while((line = reader.readLine()) != null) {
                response.append(line);
            }

            //Parse the response
            responseJSON = (JSONObject) new JSONTokener(response.toString()).nextValue();
        }
        finally {
            if(in != null) { in.close(); }
        }

        return responseJSON;
    }
}
