package com.unishop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by kaosp on 1/31/17.
 */

public class ListingActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        Intent intent = getIntent();
        Listing listing = (Listing) intent.getParcelableExtra("listing");

        String title = listing.getTitle().toString();
        String description = listing.getDescription().toString();

    }
}
