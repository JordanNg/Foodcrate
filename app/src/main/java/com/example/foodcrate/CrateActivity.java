package com.example.foodcrate;

import android.content.Intent;
import android.os.Bundle;

import com.example.foodcrate.data.YelpItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

public class CrateActivity extends AppCompatActivity {
    public static final String EXTRA_YELP_ITEM = "YelpItem";
    private TextView mNameTV;
    private TextView mPriceTV;
    private TextView mRatingTV;
    private TextView mOpenNowTV;
    private TextView mPhoneTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNameTV = findViewById(R.id.tv_crate_name);
        mPriceTV = findViewById(R.id.tv_crate_price);
        mRatingTV = findViewById(R.id.tv_crate_rating);
        mOpenNowTV = findViewById(R.id.tv_crate_isOpen);
        mPhoneTV = findViewById(R.id.tv_crate_phone);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_YELP_ITEM)) {
            YelpItem business = (YelpItem) intent.getSerializableExtra(EXTRA_YELP_ITEM);
            mNameTV.setText(business.name);
            mPriceTV.setText(business.price);
            mRatingTV.setText(Float.toString(business.rating));
            mOpenNowTV.setText(Boolean.toString(business.isClosed));
            mPhoneTV.setText(business.phone);
        }

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
