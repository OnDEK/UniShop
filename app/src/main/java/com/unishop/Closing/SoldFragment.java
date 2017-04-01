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

/**
 * Created by Daniel on 3/30/17.
 */

public class SoldFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sold, container, false);
        ListView ll = (ListView) view.findViewById(R.id.sold_listview);
        SoldAdapter cus = new SoldAdapter();
        ll.setAdapter(cus);
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
            return 7;//listview item count.
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
                vh.title= (TextView)convertView.findViewById(R.id.sold_textview);
                vh.button = (Button)convertView.findViewById(R.id.sold_button);
                //inflate custom layour

            } else {
                convertView.setTag(vh);
                vh.title= (TextView)convertView.findViewById(R.id.sold_textview);
                vh.button = (Button)convertView.findViewById(R.id.sold_button);

            }

            vh.title.setText("This is a placeholder for bought/sold items\\nTapping here will initiate a chat");
            return convertView;
        }

        class ViewHolder {
            TextView title;
            Button button;
        }

    }
}
