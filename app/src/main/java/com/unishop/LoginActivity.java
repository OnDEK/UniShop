package com.unishop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void handleLogin(View v) {

        /**
         * This is where logging in will be handled
         */
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        return;
    }

    public void handleRegisterButton(View v) {

        /**
         * This is where logging in will be handled
         */
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        return;
    }
}
