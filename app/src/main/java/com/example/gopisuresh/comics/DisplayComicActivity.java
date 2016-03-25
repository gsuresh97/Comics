package com.example.gopisuresh.comics;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;

public class DisplayComicActivity extends AppCompatActivity {
    URL page;
    Panel p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_comic);

        Intent intent = getIntent();

        String url = intent.getStringExtra(MyActivity.EXTRA_MESSAGE);
        Bitmap pic = null;
        try{
            page = new URL(url);
            RetrieveOptions ro = new RetrieveOptions();
            p = ro.execute(page).get();
            pic = p.pic;
        } catch(Exception e){
            e.printStackTrace();
        }

        ImageView tv = (ImageView) findViewById(R.id.comic_response);
        tv.setImageBitmap(pic);

    }

    public void next(View v){
        if(p.next == null)
            return;

        Bitmap pic = null;
        try{
            page = new URL(p.next);
            p = new RetrieveOptions().execute(page).get();
            pic = p.pic;
        } catch(Exception e){
            e.printStackTrace();
        }

        ImageView tv = (ImageView) findViewById(R.id.comic_response);
        tv.setImageBitmap(pic);
    }

    public void prev(View v){
        Log.d("msg", "This point has reached");
        if(p.prev == null)
            return;

        Bitmap pic = null;
        try{
            page = new URL(p.prev);
            p = new RetrieveOptions().execute(page).get();
            pic = p.pic;
        } catch(Exception e){
            e.printStackTrace();
        }

        ImageView tv = (ImageView) findViewById(R.id.comic_response);
        tv.setImageBitmap(pic);
    }

    public void newest(View v){
        if(p.end == null)
            return;

        Bitmap pic = null;
        try{
            page = new URL(p.end);
            p = new RetrieveOptions().execute(page).get();
            pic = p.pic;
        } catch(Exception e){
            e.printStackTrace();
        }

        ImageView tv = (ImageView) findViewById(R.id.comic_response);
        tv.setImageBitmap(pic);
    }

    public void oldest(View v){
        if(p.beginning == null)
            return;

        Bitmap pic = null;
        try{
            page = new URL(p.beginning);
            p = new RetrieveOptions().execute(page).get();
            pic = p.pic;
        } catch(Exception e){
            e.printStackTrace();
        }

        ImageView tv = (ImageView) findViewById(R.id.comic_response);
        tv.setImageBitmap(pic);
    }

}
