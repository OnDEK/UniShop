package com.unishop;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.gson.Gson;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.unishop.Closing.MessagingActivity;
import com.unishop.menu.BidsFragment;
import com.unishop.menu.HomeFragment;
import com.unishop.menu.ListingsFragment;
import com.unishop.menu.MoreFragment;
import com.unishop.menu.ClosingFragment;
import com.unishop.models.ApiEndpointInterface;
import com.unishop.models.Item;
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

    ListingsFragment listingsFragment;
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
        bottomBar.selectTabAtPosition(2);
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
                        listingsFragment = new ListingsFragment();
                        fragmentTransaction.replace(R.id.contentContainer, listingsFragment);
                        fragmentTransaction.commit();

                        break;
                    case R.id.bottombaritemthree:
                        HomeFragment homeFragment = new HomeFragment();
                        fragmentTransaction.replace(R.id.contentContainer, homeFragment);
                        fragmentTransaction.commit();
                        break;
                    case R.id.bottombaritemfour:
                        ClosingFragment closingFragment = new ClosingFragment();
                        fragmentTransaction.replace(R.id.contentContainer, closingFragment);
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
        Item item =(Item) v.getTag();
        Gson gson = new Gson();
        Intent intent = new Intent(this, EditListingActivity.class);
        intent.putExtra("item", gson.toJson(item));
        startActivity(intent);
    }
    public void handleListingClick(View v){
        Item item =(Item) v.getTag();
        Gson gson = new Gson();
        Intent intent = new Intent(this, ListingActivity.class);
        intent.putExtra("item", gson.toJson(item));
        String source = (String) v.getTag(R.id.source);
        intent.putExtra("source", source);
        startActivity(intent);

    }


    public void setListEditable(View v) {
        ListView listView = (ListView)findViewById(R.id.listingList);

        for (int i = 0; i < listView.getCount(); i++) {
            View view = listView.getChildAt(i);
            Button deleteButton = (Button)view.findViewById(R.id.listing_personal_button_delete);
            Button editButton = (Button)view.findViewById(R.id.listing_personal_button);
            if(deleteButton.getVisibility() == View.VISIBLE) {
                deleteButton.setVisibility(View.INVISIBLE);
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        handleListingClick(v);
                    }
                });
            }else {
                deleteButton.setVisibility(View.VISIBLE);
                deleteButton.bringToFront();
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onEditListingClick(v);
                    }
                });
            }
        }
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
    public void handleChat(View v) {
        String id = (String)v.getTag();
        Intent intent = new Intent(HomeActivity.this, MessagingActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
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

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private PopupWindow pw;
    public void initiatePopupWindow(View v) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) HomeActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.sorting_popup,
                    (ViewGroup) findViewById(R.id.popup_element));
            layout.setAnimation(AnimationUtils.loadAnimation(this, R.anim.sort_anim));
            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout, 800, 1300, true);
            pw.setAnimationStyle(R.anim.sort_anim);
            // display the popup in the center
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

            Button cancelPopuop = (Button) findViewById(R.id.cancelpopup);
            cancelPopuop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pw.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
