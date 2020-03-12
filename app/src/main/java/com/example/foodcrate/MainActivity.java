package com.example.foodcrate;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.foodcrate.data.Status;
import com.example.foodcrate.data.YelpItem;
import com.example.foodcrate.utils.NetworkUtils;
import com.example.foodcrate.utils.YelpUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText mSearchTV;
    private ProgressBar mLoadingIndicatorPB;
    private Button mButton;

    private YelpQueryViewModel mViewModel;

    private String lat;
    private String lon;
    private String mBusinessId;
    private static final int REQUEST_LOCATION = 123;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private List<YelpItem> mYelpItems;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        mButton = findViewById(R.id.button_query);
        mLoadingIndicatorPB = findViewById(R.id.pb_loading_indicator);

        mViewModel = new ViewModelProvider(this).get(YelpQueryViewModel.class);
        mViewModel.getQueryResults().observe(this, new Observer<List<YelpItem>>() {
            @Override
            public void onChanged(List<YelpItem> yelpItems) {
                if (yelpItems != null) {
                    startCrateActivity(yelpItems);
                }
            }
        });

        mViewModel.getLoadingStatus().observe(this, new Observer<Status>() {
            @Override
            public void onChanged(Status status) {
                if (status == Status.LOADING) {
                    mLoadingIndicatorPB.setVisibility(View.VISIBLE);
                    mButton.setVisibility(View.INVISIBLE);
                } else if (status == Status.SUCCESS) {
                    mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
                } else {
                    mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
                    mButton.setVisibility(View.VISIBLE);
                }
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request the user to give me permissions to stalk their location...
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButton.setVisibility(View.INVISIBLE);
                mSearchTV = findViewById(R.id.et_keyword_search);
                String sample = String.valueOf(mSearchTV.getText());
                executeYelpQuery(sample, lon, lat);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = Double.toString(location.getLatitude());
        lon = Double.toString(location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("StatusChanged", Integer.toString(status));
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","status");
    }



    private void executeYelpQuery(String query, String lon, String lat) {
        String url = YelpUtils.buildYelpQuery(query, lon, lat);
        Log.d(TAG, "querying search URL: " + url);
        mViewModel.loadQueryResults(query, lon, lat);
        //new YelpQueryAsyncTask().execute(url);
    }

    private void startCrateActivity(List<YelpItem> yelpItems) {
        // Get a random item from our list
        int max = yelpItems.size() - 1;
        int rand = (int) ((Math.random() * ((max - 0) + 1)) + 0);

        Intent crateActivityIntent = new Intent(MainActivity.this, CrateActivity.class);
        crateActivityIntent.putExtra(CrateActivity.EXTRA_YELP_ITEM, yelpItems.get(rand));
        startActivity(crateActivityIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public class YelpQueryAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicatorPB.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            String searchResults = null;
            try {
                searchResults = NetworkUtils.doHttpGet(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
            List<YelpItem> yelpItems = null;
            if (s != null) {
                yelpItems = YelpUtils.parseYelpQueryResults(s);
            }

            // Get a random item from our list
            int max = yelpItems.size() - 1;
            int rand = (int) ((Math.random() * ((max - 0) + 1)) + 0);

            //mBusinessId = yelpItems.get(rand).id;
            Intent crateActivityIntent = new Intent(MainActivity.this, CrateActivity.class);
            crateActivityIntent.putExtra(CrateActivity.EXTRA_YELP_ITEM, yelpItems.get(rand));
            startActivity(crateActivityIntent);
        }
    }
}
