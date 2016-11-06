package com.unishop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Daniel on 10/30/16.
 */

public class HomeActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        String firstname = intent.getStringExtra("firstname");
        String lastname = intent.getStringExtra("lastname");

        TextView homeTextView = (TextView) findViewById(R.id.homeScreenTextView);
        homeTextView.setText("Welcome back "+ firstname + "!");
        homeTextView.setTextColor(0xFF000000);
    }
}
