package com.unishop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.unishop.models.ApiEndpointInterface;
import com.unishop.models.Create;
import com.unishop.models.CreateResponse;
import com.unishop.models.ErrorResponse;
import com.unishop.models.Item;
import com.unishop.models.ItemUpdate;
import com.unishop.utils.NetworkUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
    ArrayList<String> filePathList;
    File file, file2, file3;
    Map<String, RequestBody> map = new HashMap<>();
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

        filePathList =  getIntent().getStringArrayListExtra("photos");


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

        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra("item");
        Item item = gson.fromJson(strObj, Item.class);

        if(item != null) {
            final String itemID = item.getId().toString();
            titleEditText.setText(item.getTitle().toString());
            descriptionEditText.setText(item.getDescription().toString());
            priceEditText.setText(item.getPrice().toString());
            Button updateButton = (Button) findViewById(R.id.createlisting_publish);
            updateButton.setText("Update");
            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title = titleEditText.getText().toString();
                    String description = descriptionEditText.getText().toString();
                    Integer price = Integer.valueOf(priceEditText.getText().toString());

                    ItemUpdate itemUpdate = new ItemUpdate(title, description, price, 1);
                    String sessionToken = NetworkUtils.getSessionToken(getApplicationContext());
                    ApiEndpointInterface apiService = NetworkUtils.getApiService();
                    Call<ResponseBody> call = apiService.itemUpdate(itemUpdate, itemID, sessionToken);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Toast.makeText(getApplicationContext(), "Item Updated",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });

                }
            });
            //priceEditText.setText(String.valueOf(item.getItem().getOneStarBid()));
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

        final ProgressDialog dialog = ProgressDialog.show(CreateListingInformationActivity.this, "",
                "Uploading Listing", true);
        String sessionToken = NetworkUtils.getSessionToken(getApplicationContext());
        sessionToken = sessionToken.replaceAll("Bearer ", "");
        ApiEndpointInterface apiService = NetworkUtils.getFrontApiService();

        /*Create create = new Create(
                titleEditText.getText().toString(),
                descriptionEditText.getText().toString(),
                Integer.valueOf(priceEditText.getText().toString()),
                1);

        Uri fileUri = (android.net.Uri.parse(file.toURI().toString()));

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/jpg"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        */

        List<MultipartBody.Part> fileList = new ArrayList<>();

        for(String filePath : filePathList) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inJustDecodeBounds = true;
            File file = new File(filePath);
            Bitmap fileBitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;

            if(imageHeight > imageWidth) {
                //handle portrait resize
                try{
                    OutputStream fOut = new FileOutputStream(file);
                    fileBitmap.compress(Bitmap.CompressFormat.JPEG, 50, fOut);
                    fOut.close();
                }
                catch(java.io.FileNotFoundException e) {

                }
                catch(java.io.IOException e) {

                }

            }
            else {
                //landscape resize
                try{
                    OutputStream fOut = new FileOutputStream(file);
                    fileBitmap.compress(Bitmap.CompressFormat.JPEG, 95, fOut);
                    fOut.close();
                }
                catch(java.io.FileNotFoundException e) {

                }
                catch(java.io.IOException e) {

                }
            }
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("image[" + filePathList.indexOf(filePath) +"]", file.getName(), RequestBody.create(MediaType.parse("image/jpg"), file));
            fileList.add(filePart);
        }
       /* MultipartBody.Part filePart = MultipartBody.Part.createFormData("image[0]", file.getName(), RequestBody.create(MediaType.parse("image/jpg"), file));
        MultipartBody.Part filePart2 = MultipartBody.Part.createFormData("image[1]", file.getName(), RequestBody.create(MediaType.parse("image/jpg"), file2));
        MultipartBody.Part filePart3 = MultipartBody.Part.createFormData("image[2]", file.getName(), RequestBody.create(MediaType.parse("image/jpg"), file3));
        fileList.add(filePart);
        fileList.add(filePart2);
        fileList.add(filePart3);
        */
        RequestBody title = RequestBody.create(MediaType.parse("text/plain"), titleEditText.getText().toString());
        RequestBody price = RequestBody.create(MediaType.parse("text/plain"), priceEditText.getText().toString());
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), descriptionEditText.getText().toString());
        RequestBody catID = RequestBody.create(MediaType.parse("text/plain"), "1");

        map.put("title", title);
        map.put("price", price);
        map.put("description", description);
        map.put("category_id", catID);
        Call<CreateResponse> call = apiService.createWImage(title, price, description, catID, fileList, sessionToken);
        //Call<CreateResponse> call = apiService.create(create, sessionToken);


        call.enqueue(new Callback<CreateResponse>() {
            @Override
            public void onResponse(Call<CreateResponse> call, Response<CreateResponse> response) {
                int statusCode = response.code();
                if(statusCode == 200) {
                    CreateResponse res = response.body();
                    dialog.cancel();
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateListingInformationActivity.this);
                    builder.setMessage("Created item success.\nItemContainer ID: " + res.getItemId()).setNegativeButton("Okay", null).create().show();

                   // finish();
                }
                else{
                    dialog.cancel();
                    Gson gson = new GsonBuilder().create();
                    ErrorResponse error = new ErrorResponse();
                    try {
                        error = gson.fromJson(response.errorBody().string(), ErrorResponse.class);

                    }catch (IOException e) {}

                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateListingInformationActivity.this);
                    builder.setMessage("error " + error.getCode() + ": " + error.getMessage()).setNegativeButton("Okay", null).create().show();
                }


                for(String filePath : filePathList) {
                    File file = new File(filePath);
                    file.delete();
                }
            }

            @Override
            public void onFailure(Call<CreateResponse> call, Throwable t) {
                dialog.cancel();
                for(String filePath : filePathList) {
                    File file = new File(filePath);
                    file.delete();
                }
                String error =  t.getMessage();
                t.printStackTrace();
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
