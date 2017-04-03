package com.unishop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.audiofx.AudioEffect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unishop.models.ApiEndpointInterface;
import com.unishop.models.ErrorResponse;
import com.unishop.models.Item;
import com.unishop.models.Offer;
import com.unishop.models.SendOffer;
import com.unishop.utils.NetworkUtils;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kaosp on 1/31/17.
 */

public class ListingActivity extends Activity {

    Item item;
    String titleString, descriptionString, imageURL;
    TextView title, description;
    ImageView thumbnail, preview1, preview2, preview3, preview4, preview5;
    EditText bid;

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
        thumbnail = (ImageView)findViewById(R.id.listing_image);
        preview1 = (ImageView)findViewById(R.id.listing_image_preview1);
        preview2 = (ImageView)findViewById(R.id.listing_image_preview2);
        preview3 = (ImageView)findViewById(R.id.listing_image_preview3);
        preview4 = (ImageView)findViewById(R.id.listing_image_preview4);
        preview5 = (ImageView)findViewById(R.id.listing_image_preview5);
        bid = (EditText)findViewById(R.id.listing_bid_editText);
        titleString = item.getTitle().toString();
        descriptionString = item.getDescription().toString();
        //imageURL = item.getItem().getImageURL().toString();

        title.setText(titleString);
        description.setText(descriptionString);

    }

    public void submitBid(View v) {

        final ProgressDialog dialog = ProgressDialog.show(ListingActivity.this, "",
                "Dubmitting Bid...", true);
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

}
