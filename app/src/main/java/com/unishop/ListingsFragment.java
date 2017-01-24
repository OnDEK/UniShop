package com.unishop;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;

/**
 * Created by Daniel on 1/13/17.
 */

public class ListingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listings, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView ll = (ListView) getActivity().findViewById(R.id.listingList);
        CustomAdapter cus = new CustomAdapter();
        ll.setAdapter(cus);


    }

    Listing listing1 = new Listing("Used Underwear", 0.0, 2.0, 2.0, 5.0, 15.0, 55.0,
            "Selling my gf's used underwear", "http://mojosavings.com/wp-content/uploads/2014/07/underwear.jpg");
    Listing listing2 = new Listing("Toaster", 0.0, 2.0, 2.0, 5.0, 15.0, 55.0,
            "Selling my gf's used underwear", "http://c.shld.net/rpx/i/s/i/spin/-122/prod_1485509212?hei=245&wid=245&op_sharpen=1&qlt=85");
    Listing listing3 = new Listing("Lamborghini Aventador", 0.0, 2.0, 2.0, 5.0, 15.0, 55.0,
            "Selling my gf's used underwear", "http://o.aolcdn.com/dims-global/dims3/GLOB/legacy_thumbnail/750x422/quality/95/http://www.blogcdn.com/slideshows/images/slides/347/957/8/S3479578/slug/l/13-2015-lamborghini-aventador-roadster-review-1.jpg");
    Listing listing4 = new Listing("NEW Underwear (Elephant)", 0.0, 2.0, 2.0, 5.0, 15.0, 55.0,
            "Selling my gf's used underwear", "http://cdn.trendhunterstatic.com/thumbs/elephant-underwear.jpeg");
    Listing listing5 = new Listing("Empty Cup", 0.0, 2.0, 2.0, 5.0, 15.0, 55.0,
            "Selling my gf's used underwear", "http://www.randyhoexter.com/wp-content/uploads/2013/10/10645727_s.jpg");
    Listing listing6 = new Listing("Empty Cup", 0.0, 2.0, 2.0, 5.0, 15.0, 55.0,
            "Selling my gf's used underwear", "http://www.randyhoexter.com/wp-content/uploads/2013/10/10645727_s.jpg");
    Listing listing7 = new Listing("Empty Cup", 0.0, 2.0, 2.0, 5.0, 15.0, 55.0,
            "Selling my gf's used underwear", "http://www.randyhoexter.com/wp-content/uploads/2013/10/10645727_s.jpg");

    Listing[] data_array = {listing1, listing2, listing3, listing4, listing5, listing6, listing7} ;
    public class CustomAdapter extends BaseAdapter {
        LayoutInflater mInflater;


        public CustomAdapter() {
            mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return data_array.length;//listview item count.
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

            }
            vh.title.setText(data_array[position].title);
            vh.noStarBid.setText(String.valueOf(data_array[position].getZeroStarBid()));
            vh.oneStarBid.setText(String.valueOf(data_array[position].getOneStarBid()));
            vh.twoStarBid.setText(String.valueOf(data_array[position].getTwoStarBid()));
            vh.threeStarBid.setText(String.valueOf(data_array[position].getThreeStarBid()));
            vh.fourStarBid.setText(String.valueOf(data_array[position].getFourStarBid()));
            vh.fiveStarBid.setText(String.valueOf(data_array[position].getFiveStarBid()));
            new DownloadImageTask((ImageView) convertView.findViewById(R.id.listing_personal_item_thumbnail))
                    .execute(data_array[position].imageURL);

            return convertView;
        }

        class ViewHolder {
            TextView title, noStarBid, oneStarBid, twoStarBid, threeStarBid, fourStarBid, fiveStarBid, description;
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
