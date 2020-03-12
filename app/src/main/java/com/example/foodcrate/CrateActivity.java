package com.example.foodcrate;

import android.app.ActionBar;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodcrate.data.YelpItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CrateActivity extends AppCompatActivity {
    public static final String EXTRA_YELP_ITEM = "YelpItem";
    private TextView mNameTV;
    private TextView mPriceTV;
    private TextView mRatingTV;
    private TextView isClosed;
    private TextView mPhoneTV;
    private ImageView mPhotoIV;

    private ImageView mOneStar;
    private ImageView mTwoStar;
    private ImageView mThreeStar;
    private ImageView mFourStar;
    private ImageView mFiveStar;
    private ImageView mOnehStar;
    private ImageView mTwohStar;
    private ImageView mThreehStar;
    private ImageView mFourhStar;
    private ImageView mFivehStar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNameTV = findViewById(R.id.tv_crate_name);
        mPriceTV = findViewById(R.id.tv_crate_price);
        mRatingTV = findViewById(R.id.tv_crate_rating);
        isClosed = findViewById(R.id.tv_crate_isOpen);
        mPhoneTV = findViewById(R.id.tv_crate_phone);
        mPhotoIV = findViewById(R.id.iv_image_business);

        /* Grab references to each of the stars */
        mOneStar = findViewById(R.id.iv_one_star);
        mTwoStar = findViewById(R.id.iv_two_star);
        mThreeStar = findViewById(R.id.iv_three_star);
        mFourStar = findViewById(R.id.iv_four_star);
        mFiveStar = findViewById(R.id.iv_five_star);
        mOnehStar = findViewById(R.id.iv_oneh_star);
        mTwohStar = findViewById(R.id.iv_twoh_star);
        mThreehStar = findViewById(R.id.iv_threeh_star);
        mFourhStar = findViewById(R.id.iv_fourh_star);
        mFivehStar = findViewById(R.id.iv_fiveh_star);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_YELP_ITEM)) {
            YelpItem business = (YelpItem) intent.getSerializableExtra(EXTRA_YELP_ITEM);
            mNameTV.setText(business.name);
            mPriceTV.setText(business.price);
            mRatingTV.setText(Float.toString(business.rating));
            isClosed.setText("Open Now: ".concat(Boolean.toString(!business.isClosed)));
            mPhoneTV.setText("Phone Number: ".concat(business.displayPhone));

            /* Switch statement for all of the possible ratings */
            int rating2x = (int) (business.rating * 2);
            Log.d("Tag", Integer.toString(rating2x));
            switch (rating2x) {
                case 1:
                    mOnehStar.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    mOneStar.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    mOneStar.setVisibility(View.VISIBLE);
                    mTwohStar.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    mOneStar.setVisibility(View.VISIBLE);
                    mTwoStar.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    mOneStar.setVisibility(View.VISIBLE);
                    mTwoStar.setVisibility(View.VISIBLE);
                    mThreehStar.setVisibility(View.VISIBLE);
                    break;
                case 6:
                    mOneStar.setVisibility(View.VISIBLE);
                    mTwoStar.setVisibility(View.VISIBLE);
                    mThreeStar.setVisibility(View.VISIBLE);
                    break;
                case 7:
                    mOneStar.setVisibility(View.VISIBLE);
                    mTwoStar.setVisibility(View.VISIBLE);
                    mThreeStar.setVisibility(View.VISIBLE);
                    mFourhStar.setVisibility(View.VISIBLE);
                    break;
                case 8:
                    mOneStar.setVisibility(View.VISIBLE);
                    mTwoStar.setVisibility(View.VISIBLE);
                    mThreeStar.setVisibility(View.VISIBLE);
                    mFourStar.setVisibility(View.VISIBLE);
                    break;
                case 9:
                    mOneStar.setVisibility(View.VISIBLE);
                    mTwoStar.setVisibility(View.VISIBLE);
                    mThreeStar.setVisibility(View.VISIBLE);
                    mFourStar.setVisibility(View.VISIBLE);
                    mFivehStar.setVisibility(View.VISIBLE);
                    break;
                case 10:
                    mOneStar.setVisibility(View.VISIBLE);
                    mTwoStar.setVisibility(View.VISIBLE);
                    mThreeStar.setVisibility(View.VISIBLE);
                    mFourStar.setVisibility(View.VISIBLE);
                    mFiveStar.setVisibility(View.VISIBLE);
                    break;
                default:
            }

            /* Insert the business Yelp image into the Image View */
            Glide.with(mPhotoIV)
                    .load(business.imageUrl)
                    .apply(new RequestOptions().override(500,500).centerCrop())
                    .into(mPhotoIV);
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
