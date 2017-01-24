package com.unishop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Daniel on 1/17/17.
 */

public class CreateListingPhotoActivity extends Activity {

    static final int REQUEST_IMAGE_CAPTURE = 2;
    static final int REQUEST_TAKE_PHOTO = 1;

    ImageView uploadOne, uploadTwo, uploadThree, uploadFour;
    ImageView imageArray[] = {uploadOne, uploadTwo, uploadThree, uploadFour};
    boolean photoFilled[]= {false,false,false,false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_listing_photo_activity);

        imageArray[0] = (ImageView)findViewById(R.id.createlisting_image_one);
        imageArray[1] = (ImageView)findViewById(R.id.createlisting_image_two);
        imageArray[2] = (ImageView)findViewById(R.id.createlisting_image_three);
        imageArray[3] = (ImageView)findViewById(R.id.createlisting_image_four);
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
        setPic();
    }


    private void setPic() {
        for(int i = 0; i < imageArray.length; i++) {
            if (photoFilled[i] != true) {
                // Get the dimensions of the View
                int targetW = imageArray[i].getWidth();
                int targetH = imageArray[i].getHeight();

                // Get the dimensions of the bitmap
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

                // Decode the image file into a Bitmap sized to fill the View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                imageArray[i].setImageBitmap(bitmap);
                photoFilled[i] = true;
                break;
            }
        }
    }

    String mCurrentPhotoPath;

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
        return image;
    }

    public void cancelCreateListing(View v) {
        finish();
    }

    public void uploadPhotos(View v) {
        /*
        upload photos to the server here
         */

        Intent intent = new Intent(this, CreateListingInformationActivity.class);
        startActivity(intent);
    }
}
