package com.unishop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.audiofx.AudioEffect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.unishop.models.ApiEndpointInterface;
import com.unishop.models.ErrorResponse;
import com.unishop.models.Item;
import com.unishop.models.Offer;
import com.unishop.models.SendOffer;
import com.unishop.utils.NetworkUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        String source = getIntent().getStringExtra("source");
        if(source.equals("personal")) {
            Button bidButton = (Button) findViewById(R.id.submit_bid_button);
            bidButton.setVisibility(View.INVISIBLE);
            bidButton.setClickable(false);
        }
        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra("item");
        item = gson.fromJson(strObj, Item.class);

        title = (TextView) findViewById(R.id.listing_title);
        description = (TextView) findViewById(R.id.listing_description);
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
            Picasso.with(mContext).load(url_list.get(position)).resize(1200,900).centerCrop().into(imageView);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
