package com.manish.outlooksearch.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Created by manishdewan on 05/06/16.
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //starts Search Activity
        Intent intent = new Intent(this, WikiSearchActivity.class);
        startActivity(intent);
        finish();
    }
}
