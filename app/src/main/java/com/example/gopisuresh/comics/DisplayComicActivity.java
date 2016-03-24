package com.example.gopisuresh.comics;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.net.URL;

public class DisplayComicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_comic);

        Intent intent = getIntent();

        String url = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);
        URL page;

    }

}
