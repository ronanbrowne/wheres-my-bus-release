package com.example.ronan.wheremybus;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<BusData>>, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String DUBLIN_BUS_URL = "https://data.dublinked.ie/cgi-bin/rtpi/realtimebusinformation";


    private TextView bus;

    private static final int BUS_LOADER = 1;
    private final Handler handler = new Handler();

    private BusAdapter mAdapter;
    private ListView busListView;
    private TextView heading;
    private TextView empty;
    private ProgressBar loading;
    private RadioGroup chooseStopGrou;

    private String busStopNumber = "4495";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        busListView = (ListView) findViewById(R.id.list);
        heading = (TextView) findViewById(R.id.heading);
        empty = (TextView) findViewById(R.id.empty);
        loading = (ProgressBar) findViewById(R.id.loading);
        chooseStopGrou = (RadioGroup) findViewById(R.id.radioStop);

        empty.setVisibility(View.GONE);
        chooseStopGrou.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = (RadioButton) findViewById(i);

                if (rb.getText().equals("Tesco")) {
                    busStopNumber = "1279";
                    reload();
                } else if (rb.getText().equals("Spar")) {
                    busStopNumber = "1934";
                    reload();
                } else {
                    busStopNumber = "4495";
                    reload();
                }
            }
        });


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(BUS_LOADER, null, this);
        } else {
            // Hide loading indicator because the data has been loaded
            //View loadingIndicator = findViewById(R.id.loading_indicator);
            //  loadingIndicator.setVisibility(View.GONE);
            bus.setText("No internet Connectivity");
        }

        mAdapter = new BusAdapter(this, new ArrayList<BusData>());


        if (mAdapter != null) {
            busListView.setAdapter(mAdapter);
        }


        // Obtain a reference to the SharedPreferences file for this app
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // And register to be notified of preference changes
        // So we know when the user has adjusted the query settings
        prefs.registerOnSharedPreferenceChangeListener(this);

        doTheAutoRefresh();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<BusData>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_direction_key),
                getString(R.string.settings_direction_default)
        );

        Uri baseUri = Uri.parse(DUBLIN_BUS_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        if (orderBy.equals(getString(R.string.settings_into_town_value))) {
            heading.setText("Buses into Town");
            chooseStopGrou.setVisibility(View.GONE);
            uriBuilder.appendQueryParameter("stopid", orderBy);
        } else {
            heading.setText("Buses back to gaff");
            chooseStopGrou.setVisibility(View.VISIBLE);
            uriBuilder.appendQueryParameter("stopid", busStopNumber);

        }
        loading.setVisibility(View.VISIBLE);


        //   uriBuilder.appendQueryParameter("stopid", orderBy);
        uriBuilder.appendQueryParameter("format", "json");

        Log.v("*path", uriBuilder.toString());
        return new BusDataLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<BusData>> loader, List<BusData> busDatas) {

        // Clear the adapter of previous bus data
        mAdapter.clear();

        loading.setVisibility(View.GONE);

        // If there is a valid list of {@link bus}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (busDatas != null && !busDatas.isEmpty()) {

            ArrayList<BusData> myBuses = new ArrayList<>();

            for (BusData b : busDatas) {
                Log.v("*route", b.getRoute());
                if (b.getRoute().equals("150")) {
                    myBuses.add(b);
                }
            }
            mAdapter.addAll(myBuses);
        } else {
            empty.setVisibility(View.VISIBLE);


        }
    }

    @Override
    public void onLoaderReset(Loader<List<BusData>> loader) {
        mAdapter.clear();

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        mAdapter.clear();

        getLoaderManager().restartLoader(BUS_LOADER, null, this);

        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);

        Toast.makeText(getApplicationContext(), "Route Changed", Toast.LENGTH_LONG).show();

    }

    //RECURSIVE METHOD TO REFRESH TIMES
    private void doTheAutoRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                reload();
                doTheAutoRefresh();
            }
        }, 25000);
    }

    //RELOAD LOADER
    void reload() {
        getLoaderManager().restartLoader(BUS_LOADER, null, this);
    }
}
