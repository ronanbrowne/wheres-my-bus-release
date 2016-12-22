package com.example.ronan.wheremybus;

import android.renderscript.ScriptGroup;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

/**
 * Created by Ronan on 21/12/2016.
 */

public class HttpHandler {

    public static final String LOG_TAG = HttpHandler.class.getSimpleName();



    private HttpHandler() {
    }


    public static List<BusData> fetchBusData(String requestURL) {

        //turn string into URL object
        URL url = getUrlObject(requestURL);


        String json = null;
        // Perform HTTP request to the URL
        //return back a string containing all json data

        try {
            json = makeHTTP_Request(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an List<Busdata>
        List<BusData> buses = extractBusDataObjects(json);

        // Return the list of buses
        return buses;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL getUrlObject(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }


    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */

    public static String makeHTTP_Request(URL url) throws IOException {

        String jsonRespons_String = "";

        if (url == null) {
            return jsonRespons_String;
        }

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        //set up connection
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            //if succesfull connection achievd get inout stream data store in string
            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonRespons_String = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Response code " + httpURLConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //close connection and input stream
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonRespons_String;
    }

    //convert input stream from the url Into a string this contains the JSOM
    public static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }


    public static List<BusData> extractBusDataObjects(String jsonRespons_String){
        // For Buses Returned
        List<BusData> buses = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        //  Parse the response given by the SAMPLE_JSON_RESPONSE string and
        try {
            JSONObject rootObj = new JSONObject(jsonRespons_String);
            //getting "results" array
            JSONArray jsonArray = rootObj.optJSONArray("results");

            //loop through array
            for (int i = 0; i < jsonArray.length(); i++) {

                //get i posotion of array
                JSONObject objBus= jsonArray.getJSONObject(i);

                //extract data i want passing to thg busData constructor
                buses.add(new BusData(objBus.getString("arrivaldatetime"), objBus.getString("destination"), objBus.getString("duetime"), objBus.getString("operator"), objBus.getString("origin"),objBus.getString("route")));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return buses;
    }

}
