package com.unishop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Daniel on 1/17/17.
 */

public class CreateListingInformationActivity extends Activity {

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
    }

    public void onBackClick(View v){
        finish();
    }

    public void onCancelClick(View v){
        super.finish();
        finish();
    }

    public void onSubmitClick(View v){
        Intent intent = new Intent(this, HomeActivity.class );
        startActivity(intent);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
