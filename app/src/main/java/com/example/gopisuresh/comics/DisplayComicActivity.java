package com.example.gopisuresh.comics;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.net.URL;

public class DisplayComicActivity extends AppCompatActivity {
    URL page;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_comic);

        Intent intent = getIntent();

        String url = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);

        try{
            page = new URL(url);
        } catch(Exception e){
            e.printStackTrace();
        }

        TextView tv = (TextView) findViewById(R.id.comic_response);
        tv.setText(url);

    }

}
