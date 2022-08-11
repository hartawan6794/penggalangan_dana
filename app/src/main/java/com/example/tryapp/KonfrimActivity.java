package com.example.tryapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Calendar;

public class KonfrimActivity extends AppCompatActivity {

    final Calendar myCalendar= Calendar.getInstance();
    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfrim);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("id");
            //The key argument here must match that used in the other activity
        }
    }
}