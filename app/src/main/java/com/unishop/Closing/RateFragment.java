package com.unishop.Closing;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.unishop.R;
import com.unishop.models.ApiEndpointInterface;
import com.unishop.models.Item;
import com.unishop.models.Transaction;
import com.unishop.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Daniel on 4/23/17.
 */

public class RateFragment extends Fragment {

    ArrayList<Transaction> transactionArray = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rate, container, false);

        ApiEndpointInterface apiService = NetworkUtils.getApiService();
        String sessionToken = NetworkUtils.getSessionToken(getContext());
        Call<List<Transaction>> call = apiService.getBuying(sessionToken, "completed");
        call.enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                int statusCode = response.code();
                List<Transaction> transactionList = response.body();

                if(statusCode == 200) {
                    for(Transaction transaction :transactionList) {
                        if(transaction.getRating() == null) {
                            transactionArray.add(transaction);
                        }
                    }
                    ApiEndpointInterface apiService = NetworkUtils.getApiService();
                    String sessionToken = NetworkUtils.getSessionToken(getContext());
                    Call<List<Transaction>> call2 = apiService.getSelling(sessionToken, "completed");
                    call2.enqueue(new Callback<List<Transaction>>() {
                        @Override
                        public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                            int statusCode = response.code();
                            List<Transaction> transactionList = response.body();

                            if(statusCode == 200) {
                                for(Transaction transaction :transactionList) {
                                    if(transaction.getRating() == null) {
                                        transactionArray.add(transaction);
                                    }
                                }
                                ListView ll = (ListView) getActivity().findViewById(R.id.rate_listview);
                                RateAdapter cus = new RateAdapter();
                                ll.setAdapter(cus);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Transaction>> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {

            }
        });
        return view;
    }

    public class RateAdapter extends BaseAdapter {
        LayoutInflater mInflater;


        public RateAdapter() {
            mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return transactionArray.size();//listview item count.
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
                convertView = mInflater.inflate(R.layout.item_listing_rate, parent, false);
                vh.title= (TextView)convertView.findViewById(R.id.rate_title);
                vh.one = (ImageButton)convertView.findViewById(R.id.one_star_rate);
                vh.two = (ImageButton)convertView.findViewById(R.id.two_star_rate);
                vh.three = (ImageButton)convertView.findViewById(R.id.three_star_rate);
                vh.four = (ImageButton)convertView.findViewById(R.id.four_star_rate);
                vh.five = (ImageButton)convertView.findViewById(R.id.five_star_rate);
                //inflate custom layour

            } else {
                convertView.setTag(vh);
                vh.title= (TextView)convertView.findViewById(R.id.rate_title);
                vh.one = (ImageButton)convertView.findViewById(R.id.one_star_rate);
                vh.two = (ImageButton)convertView.findViewById(R.id.two_star_rate);
                vh.three = (ImageButton)convertView.findViewById(R.id.three_star_rate);
                vh.four = (ImageButton)convertView.findViewById(R.id.four_star_rate);
                vh.five = (ImageButton)convertView.findViewById(R.id.five_star_rate);

            }

            ApiEndpointInterface apiService = NetworkUtils.getApiService();
            String sessionToken = NetworkUtils.getSessionToken(getContext());
            Call<Item> call = apiService.getItem(sessionToken, String.valueOf(transactionArray.get(position).getItemId()));
            call.enqueue(new Callback<Item>() {
                @Override
                public void onResponse(Call<Item> call, Response<Item> response) {
                    int statusCode = response.code();

                    if(statusCode == 200) {
                        Item item = response.body();
                        vh.title.setText(item.getTitle());
                    }
                }

                @Override
                public void onFailure(Call<Item> call, Throwable t) {

                }
            });
            vh.one.setTag(R.id.transaction, String.valueOf(transactionArray.get(position).getId()));
            vh.two.setTag(R.id.transaction, String.valueOf(transactionArray.get(position).getId()));
            vh.three.setTag(R.id.transaction, String.valueOf(transactionArray.get(position).getId()));
            vh.four.setTag(R.id.transaction, String.valueOf(transactionArray.get(position).getId()));
            vh.five.setTag(R.id.transaction, String.valueOf(transactionArray.get(position).getId()));
            return convertView;
        }

        class ViewHolder {
            TextView title;
            ImageButton one,two,three,four,five;

        }

    }
}
