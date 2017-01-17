package com.unishop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Daniel on 1/17/17.
 */

public class CreateListingInformationActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_listing_information_activity);

    }

    public void onBackClick(View v){
        finish();
    }

    public void onCancelClick(View v){
        super.finish();
        finish();
    }

    public void onSubmitClick(View v){
        Intent intent = new Intent(this, HomeActivity.class );
        startActivity(intent);
    }
}
