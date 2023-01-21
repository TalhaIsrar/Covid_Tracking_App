package com.buzzercode.covidmanagmentcenternust;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ExitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }
}