package com.example.ronan.wheremybus;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.net.URL;
import java.util.List;

/**
 * Created by Ronan on 21/12/2016.
 */


/**
 * Loads a list of bud data by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class BusDataLoader extends AsyncTaskLoader<List<BusData>> {


    private String url;

    public BusDataLoader(Context context, String url) {
        super(context);
        this.url =url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */    @Override
    public List<BusData> loadInBackground() {
        if (url == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<BusData> earthquakes = HttpHandler.fetchBusData(url);
        return earthquakes;
    }
}
