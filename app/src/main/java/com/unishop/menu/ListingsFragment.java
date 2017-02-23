package com.unishop.menu;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.unishop.HomeActivity;
import com.unishop.Listing;
import com.unishop.R;
import com.unishop.models.AccountItems;
import com.unishop.models.ApiEndpointInterface;
import com.unishop.models.Item;
import com.unishop.models.ItemContainer;
import com.unishop.utils.NetworkUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Daniel on 1/13/17.
 */

public class ListingsFragment extends Fragment {

    List<ItemContainer> itemsList;
    ArrayList<Item> itemArray = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listings, container, false);
        return view;
    }

    @Override
    public void onResume() {
        itemArray.clear();
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
                "Loading listings", true);
        String sessionToken = NetworkUtils.getSessionToken(getActivity().getApplicationContext());
        ApiEndpointInterface apiService = NetworkUtils.getApiService();
        Call<AccountItems> call = apiService.accountItems(sessionToken);
        call.enqueue(new Callback<AccountItems>() {
            @Override
            public void onResponse(Call<AccountItems> call, Response<AccountItems> response) {
                int statuscode = response.code();
                AccountItems items = response.body();
                if(statuscode == 200) {
                    itemsList = items.getItems();
                    for(ItemContainer item: itemsList) {
                        itemArray.add(item.getItem());
                    }

                    ListView ll = (ListView) getActivity().findViewById(R.id.listingList);
                    CustomAdapter cus = new CustomAdapter();
                    ll.setAdapter(cus);
                    dialog.cancel();
                }
            }

            @Override
            public void onFailure(Call<AccountItems> call, Throwable t) {
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
            // TODO Auto-generated method stub
            return itemArray.size();//listview item count.
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
                convertView = mInflater.inflate(R.layout.item_listing_personal, parent, false);
                vh.title= (TextView)convertView.findViewById(R.id.listing_personal_item_title);
                vh.noStarBid = (TextView)convertView.findViewById(R.id.zero_start_edittext);
                vh.oneStarBid = (TextView)convertView.findViewById(R.id.one_star_edittext);
                vh.twoStarBid = (TextView)convertView.findViewById(R.id.two_star_edittext);
                vh.threeStarBid = (TextView)convertView.findViewById(R.id.three_star_edittext);
                vh.fourStarBid = (TextView)convertView.findViewById(R.id.four_star_edittext);
                vh.fiveStarBid = (TextView)convertView.findViewById(R.id.five_star_edittext);
                vh.button = (Button)convertView.findViewById(R.id.listing_personal_button);

                //inflate custom layour

            } else {
                convertView.setTag(vh);
                vh.title= (TextView)convertView.findViewById(R.id.listing_personal_item_title);
                vh.noStarBid = (TextView)convertView.findViewById(R.id.zero_start_edittext);
                vh.oneStarBid = (TextView)convertView.findViewById(R.id.one_star_edittext);
                vh.twoStarBid = (TextView)convertView.findViewById(R.id.two_star_edittext);
                vh.threeStarBid = (TextView)convertView.findViewById(R.id.three_star_edittext);
                vh.fourStarBid = (TextView)convertView.findViewById(R.id.four_star_edittext);
                vh.fiveStarBid = (TextView)convertView.findViewById(R.id.five_star_edittext);
                vh.button = (Button)convertView.findViewById(R.id.listing_personal_button);

            }
            vh.title.setText(itemArray.get(position).getTitle());


            return convertView;
        }

        class ViewHolder {
            TextView title, noStarBid, oneStarBid, twoStarBid, threeStarBid, fourStarBid, fiveStarBid, description;
            Button button;
            ImageView thumbnail;
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
