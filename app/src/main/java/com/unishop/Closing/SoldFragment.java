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
 * Created by Daniel on 3/30/17.
 */

public class SoldFragment extends Fragment {

    ArrayList<Transaction> transactionArray = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sold, container, false);

        ApiEndpointInterface apiService = NetworkUtils.getApiService();
        String sessionToken = NetworkUtils.getSessionToken(getContext());
        Call<List<Transaction>> call = apiService.getSelling(sessionToken, "initiated");
        call.enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                int statusCode = response.code();
                List<Transaction> transactionList = response.body();

                if(statusCode == 200) {
                    for(Transaction transaction :transactionList) {
                        transactionArray.add(transaction);
                    }
                    ListView ll = (ListView) getActivity().findViewById(R.id.sold_listview);
                    SoldAdapter cus = new SoldAdapter();
                    ll.setAdapter(cus);
                }
            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {

            }
        });
        return view;
    }

    public class SoldAdapter extends BaseAdapter {
        LayoutInflater mInflater;


        public SoldAdapter() {
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
                convertView = mInflater.inflate(R.layout.item_listing_sold, parent, false);
                vh.title= (TextView)convertView.findViewById(R.id.sold_title);
                vh.price= (TextView)convertView.findViewById(R.id.sold_price);
                vh.button = (Button)convertView.findViewById(R.id.sold_button);
                vh.cancel = (Button)convertView.findViewById(R.id.sold_cancel);
                vh.chat = (Button)convertView.findViewById(R.id.sold_chat);
                vh.finish = (Button)convertView.findViewById(R.id.sold_finish);

                //inflate custom layour

            } else {
                convertView.setTag(vh);
                vh.title= (TextView)convertView.findViewById(R.id.sold_title);
                vh.price= (TextView)convertView.findViewById(R.id.sold_price);
                vh.button = (Button)convertView.findViewById(R.id.sold_button);
                vh.cancel = (Button)convertView.findViewById(R.id.sold_cancel);
                vh.chat = (Button)convertView.findViewById(R.id.sold_chat);
                vh.finish = (Button)convertView.findViewById(R.id.sold_finish);

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
                        vh.button.setTag(R.id.title_extra, item.getTitle());
                        vh.chat.setTag(R.id.title_extra, item.getTitle());
                    }
                }

                @Override
                public void onFailure(Call<Item> call, Throwable t) {

                }
            });
            vh.cancel.setTag(String.valueOf(transactionArray.get(position).getId()));
            vh.chat.setTag(String.valueOf(transactionArray.get(position).getId()));
            vh.finish.setTag(String.valueOf(transactionArray.get(position).getId()));
            vh.button.setTag(String.valueOf(transactionArray.get(position).getId()));
            vh.price.setText("$" + String.valueOf(transactionArray.get(position).getAmount()));
            return convertView;
        }

        class ViewHolder {
            TextView title, price;
            Button button, cancel, chat, finish;
        }

    }
}
