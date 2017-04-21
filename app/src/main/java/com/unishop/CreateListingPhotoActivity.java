package com.unishop;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Daniel on 1/17/17.
 */

public class CreateListingPhotoActivity extends Activity {

    static final int REQUEST_IMAGE_PICK = 2;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int FINISH_CREATE = 3;
    String mCurrentPhotoPath;
    int currentDefaultPosition = 5;
    ImageView imageView1, imageView2, imageView3, imageView4;
    ImageView imageCover1, imageCover2, imageCover3, imageCover4;
    ArrayList<ImageView> imageViewList = new ArrayList<>();
    ArrayList<String> fileArray = new ArrayList<>();
    ArrayList<ImageView> imageCoverList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_listing_photo_activity);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    1);
        }
        imageView1 = (ImageView)findViewById(R.id.create_image1);
        imageView2 = (ImageView)findViewById(R.id.create_image2);
        imageView3 = (ImageView)findViewById(R.id.create_image3);
        imageView4 = (ImageView)findViewById(R.id.create_image4);
        imageViewList.add(imageView1);
        imageViewList.add(imageView2);
        imageViewList.add(imageView3);
        imageViewList.add(imageView4);
        imageCover1 = (ImageView)findViewById(R.id.image1Cover);
        imageCover2 = (ImageView)findViewById(R.id.image2Cover);
        imageCover3 = (ImageView)findViewById(R.id.image3Cover);
        imageCover4 = (ImageView)findViewById(R.id.image4Cover);
        imageCoverList.add(imageCover1);
        imageCoverList.add(imageCover2);
        imageCoverList.add(imageCover3);
        imageCoverList.add(imageCover4);
    }

    public void onPickPhotoClick(View v) {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_IMAGE_PICK);
    }

    public void onTakePhotoClick(View v){
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(requestCode == FINISH_CREATE) {
            finish();
        }

        if(requestCode == REQUEST_IMAGE_PICK) {
            if (resultCode == RESULT_OK) {
                Uri selectedImageUri = data.getData();
                mCurrentPhotoPath = getRealPathFromURI(this, selectedImageUri);
                fileArray.add(mCurrentPhotoPath);
            }
        }

        if(mCurrentPhotoPath != null) {
            int photoPosition = fileArray.size()-1;

            Picasso.with(this).load(new File(mCurrentPhotoPath)).resize(800,800).centerCrop().into(imageViewList.get(photoPosition));
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        fileArray.add(mCurrentPhotoPath);
        return image;
    }

    public void cancelCreateListing(View v) {
        finish();
    }

    public void uploadPhotos(View v) {
        /*
        upload photos to the server here
         */

        if(fileArray.size() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateListingPhotoActivity.this);
            builder.setMessage("You must upload at least 1 image with your listing!").setNegativeButton("Okay", null).create().show();
        }
        Intent intent = new Intent(this, CreateListingInformationActivity.class);
        intent.putStringArrayListExtra("photos", fileArray);
        startActivityForResult(intent, FINISH_CREATE);
    }

    public void setAsThumbnail(View v) {

        Integer position = Integer.valueOf(v.getTag().toString());
        if(position != currentDefaultPosition && imageViewList.get(position).getDrawable() != null) {
            Picasso.with(this).load(R.drawable.set_default_image_forground).resize(imageCoverList.get(position).getWidth(),imageCoverList.get(position).getHeight()).into(imageCoverList.get(position));
            if (currentDefaultPosition != 5) {
                imageCoverList.get(currentDefaultPosition).setImageBitmap(null);
            }
            currentDefaultPosition = position;
        }
        else {
            return;
        }
        Toast.makeText(this, "Clicked cover: " + position.toString(), Toast.LENGTH_SHORT).show();

    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
