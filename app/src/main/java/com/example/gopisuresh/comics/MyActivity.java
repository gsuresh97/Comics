package com.example.gopisuresh.comics;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MyActivity extends Activity {
    public final static String COMIC_URL = "com.example.gopisuresh.comics.COMICURL";
    public final static String COMIC_NAME = "com.example.gopisuresh.comics.COMICNAME";

    ListView listView;
    CustomAdapter comics;
    ArrayList<ComicOption> list;
    BufferedReader in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        list = new ArrayList<>();
        in = null;
        try{
            in = new BufferedReader(new InputStreamReader(getAssets().open("icons.dat")));
            String line;
            while((line = in.readLine()) != null){
                String file_author = line;
                String file_title = in.readLine();
                String next = in.readLine();
                list.add(new ComicOption(this, file_author, file_title, next));
            }
        } catch (Exception e){
            String x = e.toString();
            Log.d("msg", e.getMessage());
            e.printStackTrace();
        }




        listView = (ListView) findViewById(R.id.comic_options);
        comics = new CustomAdapter(this, list);

        listView.setAdapter(comics);

    }

    public void open_comic(View view, ComicOption co) {
        Intent intent = new Intent(this, DisplayComicActivity.class);
        //get the url to the comic here by using findViewByID of the option that was chosen an put it in the string below
        String url = co.getURL();

        intent.putExtra(COMIC_URL, url);
        intent.putExtra(COMIC_NAME, co.getTitle());
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
