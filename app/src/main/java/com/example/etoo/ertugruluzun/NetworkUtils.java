package com.example.etoo.ertugruluzun;

import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class NetworkUtils {

    private static final String BASE_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=3&limit=100";
    private static ArrayList<Earthquake> earhtquakeList;
    private static final String QUERY_MIN_MAG = "minmagnitude";
    public static String getEarthquakes(String minmag) {
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        String rawEarthquakeJSON = null;

        URL requestURL = null;

        Uri queryUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_MIN_MAG, minmag).build();

        try {
            requestURL = new URL(queryUri.toString());
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            Log.d("Network", requestURL.toString());
            urlConnection.setRequestMethod("GET");
            InputStream inputStream = urlConnection.getInputStream();

            //Create a buffered bufferedReader from that input stream.
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            //Use a StringBuilder to hold the incoming response.
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
                // Since it's JSON, adding a newline isn't necessary (it won't
                // affect parsing) but it does make debugging a *lot* easier
                // if you print out the completed buffer for debugging.
                builder.append("\n");
            }
            rawEarthquakeJSON = builder.toString();

            Log.e("URL response", "Response from url: " + rawEarthquakeJSON);

            } catch (IOException e) {
            e.printStackTrace();
            }
        return rawEarthquakeJSON;
    }


}
