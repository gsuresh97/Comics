package com.example.gopisuresh.comics;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
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


public class MyActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.gopisuresh.comics.MESSAGE";
    ListView listView;
    CustomAdapter comics;
    ArrayList<ComicOption> list;
    BufferedReader in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        /*RetrieveOptions ro = new RetrieveOptions();
        ro.execute("http://www.gocomics.com/explore/espanol");
        //ro.execute("http://gocomics.com/features");

        try {
            list = ro.get();
        } catch(Exception e){
            String x = e.toString();
            Log.d("msg", e.getMessage());
            e.printStackTrace();

        }*/

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



        //Button button = (Button) findViewById(R.id.comic_button);

    }

    public void open_comic(View view) {
        Intent intent = new Intent(this, DisplayComicActivity.class);
        //get the url to the comic here by using findViewByID of the option that was chosen an put it in the string below
        String url = "http://www.gocomics.com/pearlsbeforeswine";
        //URL chosen = null;
//        try {
//            chosen = new URL(url);
//        } catch(Exception e){
//            e.printStackTrace();
//        }
        intent.putExtra(EXTRA_MESSAGE, url);
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
