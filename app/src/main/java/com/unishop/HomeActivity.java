package com.unishop;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.unishop.menu.BidsFragment;
import com.unishop.menu.HomeFragment;
import com.unishop.menu.ListingsFragment;
import com.unishop.menu.MoreFragment;
import com.unishop.menu.SettingsFragment;
import com.unishop.models.ApiEndpointInterface;
import com.unishop.utils.NetworkUtils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Daniel on 10/30/16.
 */

public class HomeActivity extends Activity {

    public static final String BASE_URL = "http://168.61.54.234/api/v1/";

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

    public void onEditListingClick(View v) {
        Listing listing = (Listing)v.getTag();
        Intent intent = new Intent(this, CreateListingInformationActivity.class);
        intent.putExtra("listing", listing);
        startActivity(intent);
    }
    public void handleListingClick(View v){
        Listing listing = (Listing)v.getTag();
        Intent intent = new Intent(this, ListingActivity.class);
        intent.putExtra("listing", listing);
        startActivity(intent);

    }
    public void handleLogout(View v){


        String sessionToken = NetworkUtils.getSessionToken(getApplicationContext());

        ApiEndpointInterface apiService = NetworkUtils.getApiService();

        Call<ResponseBody> call = apiService.logout(sessionToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int statusCode = response.code();
                if(statusCode == 200) {
                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("com.unishop.preference_file", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("session_token", "");
                    editor.commit();
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
    public String[] populateString() {

        return new String[0];
    }

    @Override
    public void onBackPressed() {

        String sessionToken = NetworkUtils.getSessionToken(getApplicationContext());


        ApiEndpointInterface apiService = NetworkUtils.getApiService();

        Call<ResponseBody> call = apiService.testauth(sessionToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                int statusCode = response.code();

                if(statusCode == 200){
                    Intent i = new Intent(Intent.ACTION_MAIN);
                    i.addCategory(Intent.CATEGORY_HOME);
                    startActivity(i);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
