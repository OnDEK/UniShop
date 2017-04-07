package com.unishop.menu;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.unishop.Listing;
import com.unishop.R;
import com.unishop.models.ApiEndpointInterface;
import com.unishop.models.Item;
import com.unishop.models.Offer;
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

public class BidsFragment extends android.app.Fragment {

    ArrayList<Offer> offerArray = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bids, container, false);
        return view;
    }

    @Override
    public void onResume() {
        offerArray.clear();
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
                "Loading listings", true);
        String sessionToken = NetworkUtils.getSessionToken(getActivity().getApplicationContext());
        ApiEndpointInterface apiService = NetworkUtils.getApiService();
        Call<List<Offer>> call = apiService.accountOffers(sessionToken);
        call.enqueue(new Callback<List<Offer>>() {
            @Override
            public void onResponse(Call<List<Offer>> call, Response<List<Offer>> response) {
                int statuscode = response.code();
                List<Offer> offerList = response.body();
                if(statuscode == 200) {
                    for(Offer offer: offerList) {
                        offerArray.add(offer);
                    }

                    ListView ll = (ListView) getActivity().findViewById(R.id.biddinglist);
                    CustomAdapter cus = new CustomAdapter();
                    ll.setAdapter(cus);
                    dialog.cancel();
                }
            }

            @Override
            public void onFailure(Call<List<Offer>> call, Throwable t) {
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
            return offerArray.size();//listview item count.
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
                convertView = mInflater.inflate(R.layout.item_bid_personal, parent, false);
                vh.title= (TextView)convertView.findViewById(R.id.bidding_personal_title);
                vh.yourBid = (TextView)convertView.findViewById(R.id.bidding_personal_yourbid);
                vh.otherBid = (TextView)convertView.findViewById(R.id.bidding_personal_topbid);
                vh.thumbnail = (ImageView)convertView.findViewById(R.id.bidding_personal_thumbnail);
                vh.button = (Button)convertView.findViewById(R.id.bidding_personal_button);

                //inflate custom layour

            } else {
                convertView.setTag(vh);
                vh.title= (TextView)convertView.findViewById(R.id.listing_personal_item_title);
                vh.title= (TextView)convertView.findViewById(R.id.bidding_personal_title);
                vh.yourBid = (TextView)convertView.findViewById(R.id.bidding_personal_yourbid);
                vh.otherBid = (TextView)convertView.findViewById(R.id.bidding_personal_topbid);
                vh.thumbnail = (ImageView)convertView.findViewById(R.id.bidding_personal_thumbnail);
                vh.button = (Button)convertView.findViewById(R.id.bidding_personal_button);

            }

            String imagePaths = offerArray.get(position).getItem().getImagePaths();
            if(imagePaths != null) {
                String thumbnailPath = imagePaths.replaceAll(";.*", "");
                thumbnailPath = new String("https://unishop.shop").concat(thumbnailPath).concat("_100x100.png");

                Picasso.with(getContext()).load(thumbnailPath).into(vh.thumbnail);
            }

            vh.title.setText("$" + offerArray.get(position).getItem().getTitle());
            vh.yourBid.setText(offerArray.get(position).getAmount().toString());
            if(offerArray.get(position).getHighestCompetingBid() != null) {
                vh.otherBid.setText("$" + offerArray.get(position).getHighestCompetingBid().toString());

            }
            else {
                vh.otherBid.setText("$0");
            }
            convertView.findViewById(R.id.bidding_personal_button).setTag(offerArray.get(position).getItem());
            convertView.findViewById(R.id.bidding_personal_button).setTag(R.id.source, "others");


            return convertView;
        }

        class ViewHolder {
            TextView title, yourBid, otherBid;
            Button button;
            ImageView thumbnail;
        }
    }
}
