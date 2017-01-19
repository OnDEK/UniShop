package com.unishop;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

/**
 * Created by Daniel on 10/30/16.
 */

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        String firstname = intent.getStringExtra("firstname");
        String lastname = intent.getStringExtra("lastname");

        // TextView homeTextView = (TextView) findViewById(R.id.homeScreenTextView);
        // homeTextView.setText("Welcome back "+ firstname + "!");
        // homeTextView.setTextColor(0xFF000000);

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                switch (tabId) {
                    case R.id.bottombaritemone:
                        BidsFragment bidFragment = new BidsFragment();
                        fragmentTransaction.replace(R.id.contentContainer, bidFragment);
                        fragmentTransaction.commit();
                        break;
                    case R.id.bottombaritemtwo:
                        ListingsFragment listingsFragment = new ListingsFragment();
                        fragmentTransaction.replace(R.id.contentContainer, listingsFragment);
                        fragmentTransaction.commit();

                        break;
                    case R.id.bottombaritemthree:
                        HomeFragment homeFragment = new HomeFragment();
                        fragmentTransaction.replace(R.id.contentContainer, homeFragment);
                        fragmentTransaction.commit();
                        break;
                    case R.id.bottombaritemfour:
                        SettingsFragment settingsFragment = new SettingsFragment();
                        fragmentTransaction.replace(R.id.contentContainer, settingsFragment);
                        fragmentTransaction.commit();
                        break;
                    case R.id.bottombaritemfive:
                        MoreFragment moreFragment = new MoreFragment();
                        fragmentTransaction.replace(R.id.contentContainer, moreFragment);
                        fragmentTransaction.commit();
                        break;
                    default:
                        break;
                }
            }
        });

    }

    public void onCreateListingClick(View v) {
        Intent intent = new Intent(this, CreateListingPhotoActivity.class);
        startActivity(intent);
    }

    public String[] populateString() {

        return new String[0];
    }

}
