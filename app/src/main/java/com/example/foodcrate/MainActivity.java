package com.example.foodcrate;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodcrate.data.Status;
import com.example.foodcrate.data.YelpItem;
import com.example.foodcrate.utils.NetworkUtils;
import com.example.foodcrate.utils.YelpUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
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
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener, NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText mSearchTV;
    private ProgressBar mLoadingIndicatorPB;
    private Button mButton;
    private TextView mUnpackCrate;

    private YelpQueryViewModel mViewModel;

    private String lat;
    private String lon;
    private String mBusinessId;
    private static final int REQUEST_LOCATION = 123;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private DrawerLayout mDrawer;

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
        mDrawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(mDrawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //NavigationUI.setupWithNavController(navigationView, navController);

        mButton = findViewById(R.id.button_query);
        mUnpackCrate = findViewById(R.id.tv_unpack_crate);
        mLoadingIndicatorPB = findViewById(R.id.pb_loading_indicator);

        mViewModel = new ViewModelProvider(this).get(YelpQueryViewModel.class);
        mViewModel.getQueryResults().observe(this, new Observer<List<YelpItem>>() {
            @Override
            public void onChanged(List<YelpItem> yelpItems) {
                if (yelpItems != null) {
                    // Get a random item from our list
                    if (yelpItems.size() > 0) {
                        int max = yelpItems.size() - 1;
                        int rand = (int) ((Math.random() * ((max - 0) + 1)) + 0);

                        // Launch Activity
                        startCrateActivity(yelpItems.get(rand));
                    } else {
                        Snackbar.make(findViewById(R.id.nav_host_fragment), "Your Food Crate seems a little light... \nTry expanding your preferences",
                                Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        mButton.setVisibility(View.VISIBLE);
                    }
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
                mUnpackCrate.setVisibility(View.INVISIBLE);
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
        mUnpackCrate.setVisibility(View.VISIBLE);
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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String price_pref = sharedPreferences.getString(
                getString(R.string.pref_price_filter_key),
                getString(R.string.pref_price_default)
        );
        boolean open_now = sharedPreferences.getBoolean(
                getString(R.string.pref_open_now_key),
                true
        );
        boolean deals = sharedPreferences.getBoolean(
                getString(R.string.pref_deals_key),
                false
        );

        /*String url = YelpUtils.buildYelpQuery(query, lon, lat, price_pref);
        Log.d(TAG, "querying search URL: " + url);*/
        mViewModel.loadQueryResults(query, lon, lat, price_pref, open_now, deals);
    }

    private void startCrateActivity(YelpItem yelpItem ) {
        Intent crateActivityIntent = new Intent(MainActivity.this, CrateActivity.class);
        crateActivityIntent.putExtra(CrateActivity.EXTRA_YELP_ITEM, yelpItem);
        startActivity(crateActivityIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mDrawer.closeDrawers();
        switch (item.getItemId()) {
            case R.id.nav_home:
                return true;
            case R.id.nav_gallery:
                Intent visitedIntent = new Intent(this, VisitedActivity.class);
                startActivity(visitedIntent);
                return true;
            case R.id.nav_slideshow:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }
}
