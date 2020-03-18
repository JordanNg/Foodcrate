package com.example.foodcrate;

import android.app.ActionBar;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodcrate.data.YelpItem;
import com.example.foodcrate.data.YelpQueryAsyncTask;
import com.example.foodcrate.data.YelpQueryRepository;
import com.example.foodcrate.utils.YelpUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.example.foodcrate.R.color.colorAccent;
import static com.example.foodcrate.R.color.colorClosed;
import static com.example.foodcrate.R.color.colorPrimary;

public class CrateActivity extends AppCompatActivity {
    public static final String EXTRA_YELP_ITEM = "YelpItem";

    private YelpItem business;

    private TextView mNameTV;
    private TextView mPriceTV;
    private TextView mRatingTV;
    private TextView mPhoneTV;
    private TextView mLocationTV;
    private TextView mTransactionsTV;
    private TextView mTransLabelTV;
    private TextView mDistance;

    private Button mAddToDatabaseButton;
    private boolean mIsSaved = false;

    private SavedYelpItemsViewModel mViewModel;

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

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewModel = new ViewModelProvider(
                this,
                new ViewModelProvider.AndroidViewModelFactory(
                        getApplication()
                )
        ).get(SavedYelpItemsViewModel.class);

        mNameTV = findViewById(R.id.tv_crate_name);
        mPriceTV = findViewById(R.id.tv_crate_price);
        mRatingTV = findViewById(R.id.tv_crate_rating);
        mPhoneTV = findViewById(R.id.tv_crate_phone);
        mPhotoIV = findViewById(R.id.iv_image_business);
        mLocationTV = findViewById(R.id.tv_crate_location);
        mDistance = findViewById(R.id.tv_distance);

        mTransLabelTV = findViewById(R.id.tv_crate_transactions);
        mTransactionsTV = findViewById(R.id.tv_crate_transElements);

        mAddToDatabaseButton = findViewById(R.id.button_add_to_database);

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
            business = (YelpItem) intent.getSerializableExtra(EXTRA_YELP_ITEM);

            mNameTV.setText(business.name);
            mPriceTV.setText(business.price);
            mRatingTV.setText(Float.toString(business.rating));

            mDistance.setText(String.format("%.2f", business.distance/1609).concat(" mi"));

            mPhoneTV.setText(business.displayPhone);
            mLocationTV.setText(business.address1 + "\n"
                    + business.city + " " + business.state + ", " + business.zipCode);

            // Must do it this way for database...
            if (business.transactions != null) {
                mTransactionsTV.setText(business.transactions);
            } else {
                mTransactionsTV.setText("No transactions listed");
            }

            /* Switch statement for all of the possible ratings */
            int rating2x = (int) (business.rating * 2);
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
                    break;
            }

            /* Insert the business Yelp image into the Image View */
            Glide.with(mPhotoIV)
                    .load(business.imageUrl)
                    .apply(new RequestOptions().override(500,500).centerCrop())
                    .into(mPhotoIV);

            mAddToDatabaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIsSaved = !mIsSaved;
                    if (mIsSaved) {
                        mAddToDatabaseButton.setBackgroundColor(getResources().getColor(colorClosed));
                        mAddToDatabaseButton.setText("Remove from visited");
                        mViewModel.insertYelpItem(business);
                    } else {
                        mAddToDatabaseButton.setBackgroundColor(getResources().getColor(colorAccent));
                        mAddToDatabaseButton.setText("Add to visited");
                        mViewModel.deleteYelpItem(business);
                    }
                }
            });
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Share this place with a friend!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                Intent intent = getIntent();
                YelpItem business = (YelpItem) intent.getSerializableExtra(EXTRA_YELP_ITEM);
                String message = "Food Crate dropped us " + business.name + "\n"
                            + "It has a rating of " + business.rating + " stars!\n"
                            + "Here is a link if you want to check it out:\n\n" + business.url;


                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, message);
                shareIntent.setType("text/plain");

                Intent chooserIntent = Intent.createChooser(shareIntent, null);
                startActivity(chooserIntent);
            }
        });
    }
}
