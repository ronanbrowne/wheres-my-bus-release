package com.example.ronan.wheremybus;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<BusData>> {

    private static final String DUBLIN_BUS_URL = "https://data.dublinked.ie/cgi-bin/rtpi/realtimebusinformation?stopid=307&format=json";


    private TextView bus;

    private static final int EARTHQUAKE_LOADER_ID = 1;
    private BusAdapter mAdapter;
    ListView busListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         busListView = (ListView) findViewById(R.id.list);


      //  bus = (TextView) findViewById(R.id.busTime);


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {
            // Hide loading indicator because the data has been loaded
            //View loadingIndicator = findViewById(R.id.loading_indicator);
          //  loadingIndicator.setVisibility(View.GONE);
            bus.setText("No internet Connectivity");
        }

        mAdapter = new BusAdapter(this, new ArrayList<BusData>());


        if(mAdapter!=null) {
            busListView.setAdapter(mAdapter);

        }




    }

    @Override
    public Loader<List<BusData>> onCreateLoader(int i, Bundle bundle) {

        Uri uri = Uri.parse(DUBLIN_BUS_URL);
        return new BusDataLoader(this, uri.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<BusData>> loader, List<BusData> busDatas) {

//        Toast.makeText(this, "Route: "+busDatas.get(0).getRoute()+" Arrivat Time:"+busDatas.get(0).getDuetime() ,Toast.LENGTH_SHORT).show();
//        bus.setText("Bus will be at the stop in: "+busDatas.get(0).getDuetime()+" Min " +
//                "\nRoute: "+busDatas.get(0).getRoute()+
//                "\nDestination: "+busDatas.get(0).getDestination());
//


        // Clear the adapter of previous earthquake data
        mAdapter.clear();


        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (busDatas != null && !busDatas.isEmpty()) {
            mAdapter.addAll(busDatas);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<BusData>> loader) {
        mAdapter.clear();

    }
}
