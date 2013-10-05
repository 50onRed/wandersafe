package com.fifty.wdrsfe;


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

public class Utils {

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
