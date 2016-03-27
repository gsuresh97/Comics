package com.example.gopisuresh.comics;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.app.Activity;

import org.w3c.dom.Text;

public class DisplayComicActivity extends Activity {
    URL page;
    static Panel p;
    String prev = null;
    Display mDisplay;
    FileOutputStream out;
    FileInputStream in;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d("Oncreate is Running", "Oncreate is Running");
                setContentView(R.layout.activity_display_comic);
        Point screen = new Point();


        Intent intent = getIntent();

        String url = intent.getStringExtra(MyActivity.COMIC_URL);
        String name = intent.getStringExtra(MyActivity.COMIC_NAME);

        TextView v = (TextView)findViewById(R.id.title);
        char[] charName = name.toCharArray();
        v.setText(charName, 0, charName.length);


        Bitmap pic = null;
        try{
            in = openFileInput("info.dat");
            int x = in.available();
            String address = "";
            int bit;
            while((bit = in.read()) != -1)
                address += (char)bit;

            if(!address.contains("|") && address.contains(url))
                url = address;

        } catch (FileNotFoundException e){
            //e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        try{
            page = new URL(url);
            RetrieveOptions ro = new RetrieveOptions();
            p = ro.execute(page).get();
            pic = p.pic;



//            double aspectRatio = p.pic.getWidth()/p.pic.getHeight();
//            double fracWidthInScreen = 1/aspectRatio;
//            Point screen = new Point();
//            mDisplay.getSize(screen);
//            int scaledHeight = screen.x;
//            int scaledWidth = screen.x * pic.getWidth()/pic.getHeight();

            Log.v("Picture Data", String.format("Width: %d\tHeight: %d\t", p.pic.getWidth(), p.pic.getHeight()));
            prev = p.prev;
        } catch(Exception e){
            e.printStackTrace();
        }
        mDisplay = getWindowManager().getDefaultDisplay();

        ImageView tv = (ImageView) findViewById(R.id.comic_response);
        tv.setImageBitmap(pic);
        updatePos(p.current);

//        mDisplay.getSize(screen);
//
//        Matrix m = tv.getImageMatrix();
//        RectF picRect = new RectF(0, 0, pic.getWidth(), pic.getHeight());
//        RectF viewRect = new RectF(0, 0, screen.x, screen.x);
//        m.setRectToRect(picRect, viewRect, Matrix.ScaleToFit.CENTER);
//        tv.setImageMatrix(m);

    }

    public void next(View v){
        if(p.next.equals(p.current))
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
        updatePos(p.current);
    }

    public void prev(View v){
        Log.d("msg", "This point has reached");
        if(p.prev.equals(p.current))
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
        updatePos(p.current);
    }

    public void newest(View v){
        if(p.end.equals(p.current))
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
        updatePos(p.current);
    }

    public void oldest(View v){
        if(p.beginning.equals(p.current))
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
        updatePos(p.current);
    }

    public void calDate(int year, int month, int day){
        Bitmap pic = null;
        String calURL = null;
        month++;
        if(!Character.isDigit(p.end.charAt(p.end.length()-1))) {
            //calURL = p.current + "/" + year + "/" + month + "/" + day;
            calURL = String.format("%s/%d/%02d/%02d", p.current, year, month, day);
        } else {
            calURL = String.format("%s%d/%02d/%02d", p.end.substring(0, p.end.length() - 10), year, month, day);
        }
        try{
            Log.v("msg", calURL);
            page = new URL(calURL);
            p = new RetrieveOptions().execute(page).get();
            pic = p.pic;
        } catch(Exception e){
            e.printStackTrace();
        }
        if(p.prev.equals(prev)){
            Context context = getApplicationContext();
            CharSequence text = "Invalid date.";

            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.show();

            return;
        }


        ImageView tv = (ImageView) findViewById(R.id.comic_response);
        tv.setImageBitmap(pic);
        updatePos(p.current);
    }

    public void updatePos(String url){
        try {
            out = openFileOutput("info.dat", Context.MODE_PRIVATE);
            out.write(url.getBytes());
            out.close();
        } catch(Exception e){e.printStackTrace();}
        String date = "";
        Calendar c = Calendar.getInstance();
        if(!Character.isDigit(url.charAt(url.length() - 1)))
            date = String.format("%02d/%02d/%d", c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.YEAR));
        else{
            String messyDate = url.substring(url.length()-10);
            String year = messyDate.substring(0, 4);
            String month = messyDate.substring(5, 7);
            String day = messyDate.substring(8, 10);
            date = month + "/" + day + "/" + year;
        }

        TextView t = (TextView)findViewById(R.id.date);
        t.setText(date.toCharArray(), 0, 10);
    }

    public void pick_date(View view){
        showDialog(999);
    }

    @Override
    @SuppressWarnings("deprecations")
    protected Dialog onCreateDialog(int id){
        if(id == 999) {
            final Calendar c = Calendar.getInstance();

            DateFormat format = new SimpleDateFormat("yyyy/MM/dd");

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH)+1;
            int day = c.get(Calendar.DAY_OF_MONTH)+1;

            Date date = null;
            String d;
            if(p.end.equals(p.current))
                d = String.format("%s/%02d/%02d", year, month, day);
            else{
                d = p.current.substring(p.current.length() - 10);
            }

            try {
                date = format.parse(d);
            } catch (Exception e) {
                e.printStackTrace();
            }
            c.setTime(date);

            DatePickerDialog dp = new DatePickerDialog(this, dateListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

            Date date1 = null;
            try {
                d = String.format("%s/%02d/%02d", year, month, day);
                date1 = format.parse(d);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dp.getDatePicker().setMaxDate(date1.getTime());

            String firstDate = p.beginning.substring(p.beginning.length() - 10);

            Date date2 = null;
            try {
                date2 = format.parse(firstDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dp.getDatePicker().setMinDate(date2.getTime());
            return dp;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day){
            DisplayComicActivity.this.calDate(year, month, day);
        }
    };
}

