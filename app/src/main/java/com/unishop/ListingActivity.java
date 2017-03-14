package com.unishop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.audiofx.AudioEffect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.unishop.models.Item;

import java.io.InputStream;

/**
 * Created by kaosp on 1/31/17.
 */

public class ListingActivity extends Activity {

    String titleString, descriptionString, imageURL;
    TextView title, description;
    ImageView thumbnail, preview1, preview2, preview3, preview4, preview5;
    ImageView[] images = {thumbnail, preview1, preview2, preview3, preview4, preview5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra("item");
        Item item = gson.fromJson(strObj, Item.class);

        title = (TextView) findViewById(R.id.listing_title);
        description = (TextView) findViewById(R.id.listing_description);
        thumbnail = (ImageView)findViewById(R.id.listing_image);
        preview1 = (ImageView)findViewById(R.id.listing_image_preview1);
        preview2 = (ImageView)findViewById(R.id.listing_image_preview2);
        preview3 = (ImageView)findViewById(R.id.listing_image_preview3);
        preview4 = (ImageView)findViewById(R.id.listing_image_preview4);
        preview5 = (ImageView)findViewById(R.id.listing_image_preview5);

        titleString = item.getTitle().toString();
        descriptionString = item.getDescription().toString();
        //imageURL = item.getItem().getImageURL().toString();

        title.setText(titleString);
        description.setText(descriptionString);
        new DownloadImageTask(thumbnail)
                .execute(imageURL);

    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
