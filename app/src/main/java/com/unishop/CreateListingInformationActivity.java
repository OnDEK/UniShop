package com.unishop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unishop.models.ApiEndpointInterface;
import com.unishop.models.Create;
import com.unishop.models.CreateResponse;
import com.unishop.models.ErrorResponse;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Daniel on 1/17/17.
 */

public class CreateListingInformationActivity extends Activity {

    public static final String BASE_URL = "http://168.61.54.234/api/v1/";
    EditText titleEditText;
    EditText descriptionEditText;
    EditText priceEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_listing_information_activity);

        View.OnFocusChangeListener hideKeyboardListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        };

        titleEditText = (EditText)findViewById(R.id.createlisting_post_title);
        descriptionEditText = (EditText)findViewById(R.id.createlisting_description);
        priceEditText = (EditText)findViewById(R.id.createlisting_startingbid);

        titleEditText.setOnFocusChangeListener(hideKeyboardListener);
        descriptionEditText.setOnFocusChangeListener(hideKeyboardListener);
        priceEditText.setOnFocusChangeListener(hideKeyboardListener);

        descriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (descriptionEditText.getText().toString().length() > 0){
                    // position the text type in the left top corner
                    descriptionEditText.setGravity(Gravity.LEFT | Gravity.TOP);
                }else{
                    // no text entered. Center the hint text.
                    descriptionEditText.setGravity(Gravity.CENTER);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Intent intent = getIntent();
        Listing listing = (Listing)intent.getParcelableExtra("listing");
        if(listing != null) {
            titleEditText.setText(listing.getTitle().toString());
            descriptionEditText.setText(listing.getDescription().toString());
            priceEditText.setText(String.valueOf(listing.getOneStarBid()));
        }
    }

    public void onBackClick(View v){
        finish();
    }

    public void onCancelClick(View v){
        super.finish();
        finish();
    }

    public void onSubmitClick(View v){

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("com.unishop.preference_file", Context.MODE_PRIVATE);
        String token = new String("Bearer");
        String session = sharedPref.getString("session_token", "");
        token = token.concat(" " + session);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ApiEndpointInterface apiService =
                retrofit.create(ApiEndpointInterface.class);
        Create create = new Create(
                titleEditText.getText().toString(),
                descriptionEditText.getText().toString(),
                Integer.valueOf(priceEditText.getText().toString())*100,
                1);


        Call<CreateResponse> call = apiService.create(create, token);

        call.enqueue(new Callback<CreateResponse>() {
            @Override
            public void onResponse(Call<CreateResponse> call, Response<CreateResponse> response) {
                int statusCode = response.code();
                if(statusCode == 200) {
                    CreateResponse res = response.body();
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateListingInformationActivity.this);
                    builder.setMessage("Created item success.\nItem ID: " + res.getItemId()).setNegativeButton("Okay", null).create().show();
                }
                else{
                    Gson gson = new GsonBuilder().create();
                    ErrorResponse error = new ErrorResponse();
                    try {
                        error = gson.fromJson(response.errorBody().string(), ErrorResponse.class);

                    }catch (IOException e) {}

                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateListingInformationActivity.this);
                    builder.setMessage("error " + error.getCode() + ": " + error.getMessage()).setNegativeButton("Okay", null).create().show();
                }


            }

            @Override
            public void onFailure(Call<CreateResponse> call, Throwable t) {

            }
        });

        //Intent intent = new Intent(this, HomeActivity.class );
        //startActivity(intent);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
