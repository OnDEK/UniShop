package com.unishop.menu;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.unishop.R;
import com.unishop.models.ApiEndpointInterface;
import com.unishop.models.ErrorResponse;
import com.unishop.models.Item;
import com.unishop.utils.NetworkUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Daniel on 1/13/17.
 */

public class HomeFragment extends android.app.Fragment {

    ArrayList<Item> itemArray = new ArrayList<>();
    SearchView searchView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        searchView = (SearchView)view.findViewById(R.id.searchView);
        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setTextColor(Color.BLACK);

        return view;
    }

    @Override
    public void onResume() {
        itemArray.clear();
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
                "Loading listings", true);
        String sessionToken = NetworkUtils.getSessionToken(getActivity().getApplicationContext());
        ApiEndpointInterface apiService = NetworkUtils.getApiService();
        Call<List<Item>> call = apiService.unownedItems(sessionToken);
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                int statuscode = response.code();
                List<Item> itemsList = response.body();
                if(statuscode == 200) {
                    for(Item item: itemsList) {
                        itemArray.add(item);
                    }

                    ListView ll = (ListView) getActivity().findViewById(R.id.homelist);
                    HomeFragment.CustomAdapter cus = new HomeFragment.CustomAdapter();
                    ll.setAdapter(cus);
                    dialog.cancel();
                }
                else {
                    Gson gson = new GsonBuilder().create();
                    ErrorResponse error = new ErrorResponse();
                    try {
                        error = gson.fromJson(response.errorBody().string(), ErrorResponse.class);

                    }catch (IOException e) {}
                    dialog.cancel();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("error " + error.getCode() + ": " + error.getMessage()).setNegativeButton("Okay", null).create().show();
                }
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                dialog.cancel();
            }
        });
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    public class CustomAdapter extends BaseAdapter {
        LayoutInflater mInflater;


        public CustomAdapter() {
            mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            if(itemArray.size()%2 == 0)
                return itemArray.size()/2;
            else
                return itemArray.size()/2+1;//listview item count.
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final ViewHolder vh;
            vh = new ViewHolder();



            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_listing_home, parent, false);
                vh.title1= (TextView)convertView.findViewById(R.id.listing_home_title_left);
                vh.title2 = (TextView)convertView.findViewById(R.id.listing_home_title_right);
                vh.button1 = (Button)convertView.findViewById(R.id.button_home_left);
                vh.button2 = (Button)convertView.findViewById(R.id.button_home_right);
                vh.thumbnail1 = (ImageView) convertView.findViewById(R.id.listing_home_thumbnail_left);
                vh.thumbnail2 = (ImageView) convertView.findViewById(R.id.listing_home_thumbnail_right);
                vh.price1 = (TextView) convertView.findViewById(R.id.listing_home_price_left);
                vh.price2 = (TextView) convertView.findViewById(R.id.listing_home_price_right);

                //inflate custom layour

            } else {
                convertView.setTag(vh);
                vh.title1= (TextView)convertView.findViewById(R.id.listing_home_title_left);
                vh.title2 = (TextView)convertView.findViewById(R.id.listing_home_title_right);
                vh.button1 = (Button)convertView.findViewById(R.id.button_home_left);
                vh.button2 = (Button)convertView.findViewById(R.id.button_home_right);
                vh.thumbnail1 = (ImageView) convertView.findViewById(R.id.listing_home_thumbnail_left);
                vh.thumbnail2 = (ImageView) convertView.findViewById(R.id.listing_home_thumbnail_right);
                vh.price1 = (TextView) convertView.findViewById(R.id.listing_home_price_left);
                vh.price2 = (TextView) convertView.findViewById(R.id.listing_home_price_right);
            }


            vh.title1.setText(itemArray.get(position*2).getTitle());
            // new DownloadImageTask((ImageView) convertView.findViewById(R.id.listing_home_thumbnail))
            //         .execute(data_array[position*2].imageURL);
            vh.price1.setText(String.valueOf("$"+itemArray.get(position*2).getPrice()));

            vh.button1.setTag(itemArray.get(position*2));

            String imagePaths = itemArray.get(position*2).getImagePaths();
            if(imagePaths != null) {
                String thumbnailPath = imagePaths.replaceAll(";.*", "");
                thumbnailPath = new String("https://unishop.shop").concat(thumbnailPath).concat("_500x500.png");

                Picasso.with(getContext()).load(thumbnailPath).resize(200,200).centerCrop().into(vh.thumbnail1);
            }

            if(position*2+1 < itemArray.size()) {
                vh.title2.setText(itemArray.get(position*2+1).getTitle());
                //new DownloadImageTask((ImageView) convertView.findViewById(R.id.listing_home_thumbnails))
                //        .execute(data_array[position*2+1].imageURL);
                vh.button2.setTag(itemArray.get(position*2+1));
                vh.price2.setText("$"+String.valueOf(itemArray.get(position*2+1).getPrice()));
                imagePaths = itemArray.get(position*2+1).getImagePaths();
                if(imagePaths != null) {
                    String thumbnailPath = imagePaths.replaceAll(";.*", "");
                    thumbnailPath = new String("https://unishop.shop").concat(thumbnailPath).concat("_500x500.png");

                    Picasso.with(getContext()).load(thumbnailPath).resize(200,200).centerCrop().into(vh.thumbnail2);
                }
            }
            vh.button2.setTag(R.id.source, "home");
            vh.button1.setTag(R.id.source, "home");
            return convertView;
        }

        class ViewHolder {
            TextView title1, title2, price1, price2;
            Button button1, button2;
            ImageView thumbnail1, thumbnail2;
        }
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
