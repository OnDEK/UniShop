package com.unishop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.unishop.models.ApiEndpointInterface;
import com.unishop.models.ErrorResponse;
import com.unishop.models.Item;
import com.unishop.models.SendOffer;
import com.unishop.utils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kaosp on 1/31/17.
 */

public class ListingActivity extends Activity {

    Item item;
    String titleString, descriptionString;
    TextView title, description;
    EditText bid;
    CustomPagerAdapter customPagerAdapter;
    ViewPager pager;
    List<String> url_list;
    Button followButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra("item");
        item = gson.fromJson(strObj, Item.class);

        followButton = (Button) findViewById(R.id.follow_button);
        title = (TextView) findViewById(R.id.edit_listing_title);
        description = (TextView) findViewById(R.id.edit_listing_description);
        bid = (EditText)findViewById(R.id.listing_bid_editText);
        titleString = item.getTitle().toString();
        descriptionString = item.getDescription().toString();
        //imageURL = item.getItem().getImageURL().toString();

        title.setText(titleString);
        description.setText(descriptionString);

        String imagePaths = item.getImagePaths();
        List<String> pathList = Arrays.asList(imagePaths.split(";"));


        url_list = new ArrayList<>();

        if(imagePaths != null) {
            int i = 0;
            for (String path : pathList) {
                String thumbnailPath = new String("https://unishop.shop").concat(path).concat(".png");
                url_list.add(thumbnailPath);
            }
        }

        customPagerAdapter = new CustomPagerAdapter(this);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(customPagerAdapter);

        ApiEndpointInterface apiService = NetworkUtils.getApiService();
        String sessionToken = NetworkUtils.getSessionToken(getApplicationContext());
        Call<List<Item>> call = apiService.getFollows(sessionToken);
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                int statusCode = response.code();
                boolean notContained = true;
                if(statusCode == 200) {
                    for(Item itemFromList : response.body()) {
                        if(item.getId() == itemFromList.getId()) {
                            notContained = false;
                            followButton.setText("Unfollow");
                            followButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    onUnfollowClick(followButton);
                                }
                            });
                        }
                        if(notContained) {
                            followButton.setText("Follow");
                            followButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    onFollowClick(followButton);
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {

            }
        });
    }

    public void submitBid(View v) {

        final ProgressDialog dialog = ProgressDialog.show(ListingActivity.this, "",
                "Submitting Bid...", true);
        ApiEndpointInterface apiService = NetworkUtils.getApiService();
        String sessionToken = NetworkUtils.getSessionToken(getApplicationContext());
        String bidString = bid.getText().toString();
        Integer bidInt = Integer.valueOf(bidString);
        SendOffer offer = new SendOffer(bidInt);
        Call<ResponseBody> call = apiService.offer(offer, item.getId().toString(), sessionToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int statusCode = response.code();
                if(statusCode == 200) {

                    dialog.cancel();
                    Toast toast = Toast.makeText(getApplicationContext(), "Bid Submitted!", Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    Gson gson = new GsonBuilder().create();
                    ErrorResponse error = new ErrorResponse();
                    try {
                        error = gson.fromJson(response.errorBody().string(), ErrorResponse.class);

                    }catch (IOException e) {}
                    dialog.cancel();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ListingActivity.this);
                    builder.setMessage("error " + error.getCode() + ": " + error.getMessage()).setNegativeButton("Okay", null).create().show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void onFollowClick(View v) {

        ApiEndpointInterface apiService = NetworkUtils.getApiService();
        String sessionToken = NetworkUtils.getSessionToken(getApplicationContext());
        Call<ResponseBody> call = apiService.followItem(String.valueOf(item.getId()), sessionToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int statusCode = response.code();

                if(statusCode == 200) {
                    followButton.setText("Unfollow");
                    followButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onUnfollowClick(followButton);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    public void onUnfollowClick(View v) {

        ApiEndpointInterface apiService = NetworkUtils.getApiService();
        String sessionToken = NetworkUtils.getSessionToken(getApplicationContext());
        Call<ResponseBody> call = apiService.unfollowItem(String.valueOf(item.getId()), sessionToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int statusCode = response.code();

                if(statusCode == 200) {
                    followButton.setText("Follow");
                    followButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onFollowClick(followButton);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return url_list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.pagerImage);
            Picasso.with(mContext).load(url_list.get(position)).placeholder(R.drawable.progress_animation).resize(1200,900).centerCrop().into(imageView);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
