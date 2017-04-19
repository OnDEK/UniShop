package com.unishop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.unishop.menu.ListingsFragment;
import com.unishop.models.ApiEndpointInterface;
import com.unishop.models.ErrorResponse;
import com.unishop.models.Item;
import com.unishop.models.Offer;
import com.unishop.models.OfferAcceptResponse;
import com.unishop.utils.NetworkUtils;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kaosp on 4/17/17.
 */

public class EditListingActivity extends Activity {

    ArrayList<Offer> offerArray = new ArrayList<>();
    List<String> url_list = new ArrayList<>();
    private Item item;
    CustomPagerAdapter customPagerAdapter;
    ViewPager pager;
    TextView title, description;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_edit);

        title = (TextView) findViewById(R.id.edit_listing_title);
        description = (TextView) findViewById(R.id.edit_listing_description);

        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra("item");
        item = gson.fromJson(strObj, Item.class);

        title.setText(item.getTitle());
        description.setText(item.getDescription());
        description.setMovementMethod(new ScrollingMovementMethod());
        String imagePaths = item.getImagePaths();
        List<String> pathList = Arrays.asList(imagePaths.split(";"));

        if(imagePaths != null) {
            int i = 0;
            for (String path : pathList) {
                String thumbnailPath = new String("https://unishop.shop").concat(path).concat(".png");
                url_list.add(thumbnailPath);
            }
        }
        customPagerAdapter = new CustomPagerAdapter(this);
        pager = (ViewPager) findViewById(R.id.edit_pager);
        pager.setAdapter(customPagerAdapter);


        String token = NetworkUtils.getSessionToken(this);
        ApiEndpointInterface apiService = NetworkUtils.getApiService();
        Call<List<Offer>> call = apiService.getItemOffers(String.valueOf(item.getId()),token);
        call.enqueue(new Callback<List<Offer>>() {
            @Override
            public void onResponse(Call<List<Offer>> call, Response<List<Offer>> response) {
                int statusCode = response.code();

                if(statusCode == 200) {
                    List<Offer> offerList = response.body();

                    for(Offer offer : offerList) {
                        offerArray.add(offer);
                    }
                    ListView ll = (ListView) findViewById(R.id.offers_list);
                    CustomAdapter cus = new CustomAdapter();
                    ll.setAdapter(cus);

                }
            }

            @Override
            public void onFailure(Call<List<Offer>> call, Throwable t) {

            }
        });
    }

    public void onAcceptOfferClick(View v) {
        Offer offer = (Offer)v.getTag();
        String token = NetworkUtils.getSessionToken(this);
        ApiEndpointInterface apiService = NetworkUtils.getApiService();
        Call<OfferAcceptResponse> call = apiService.acceptOffer(token, String.valueOf(offer.getId()), offer);

        call.enqueue(new Callback<OfferAcceptResponse>() {
            @Override
            public void onResponse(Call<OfferAcceptResponse> call, Response<OfferAcceptResponse> response) {
                int statusCode = response.code();

                if(statusCode == 200) {

                }
                Toast.makeText(EditListingActivity.this, "Offer Accepted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<OfferAcceptResponse> call, Throwable t) {

            }
        });
    }

    public void onDeleteListingClick(View v) {

        ApiEndpointInterface apiService = NetworkUtils.getApiService();
        String sessionToken = NetworkUtils.getSessionToken(getApplicationContext());

        Item item =(Item) v.getTag();
        Call<ResponseBody> call = apiService.itemDestroy(item.getId().toString(), sessionToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int statusCode = response.code();

                if(statusCode == 200) {
                    Toast.makeText(getApplicationContext(), "Item Deleted",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public class CustomAdapter extends BaseAdapter {
        LayoutInflater mInflater;


        public CustomAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                convertView = mInflater.inflate(R.layout.item_offer, parent, false);
                vh.amount= (TextView)convertView.findViewById(R.id.offer_amount_TV);
                vh.button = (Button)convertView.findViewById(R.id.offer_button);
                vh.thumbnail = (ImageView)convertView.findViewById(R.id.offer_IV);
                //inflate custom layour

            } else {
                convertView.setTag(vh);
                vh.amount= (TextView)convertView.findViewById(R.id.offer_amount_TV);
                vh.button = (Button)convertView.findViewById(R.id.offer_button);
                vh.thumbnail = (ImageView)convertView.findViewById(R.id.offer_IV);


            }
            vh.button.setTag(offerArray.get(position));

            vh.amount.setText("$" + String.valueOf(offerArray.get(position).getAmount()));

            Picasso.with(getApplicationContext()).load(R.drawable.ic_star_empty).into(vh.thumbnail);

            return convertView;
        }

        class ViewHolder {
            TextView amount;
            Button button;
            ImageView thumbnail;
        }
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
