package com.example.gopisuresh.comics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Gopisuresh on 3/18/16.
 */
public class RetrieveOptions extends AsyncTask<URL, Object, Panel> {
    HttpURLConnection connection;
    Bitmap comic;
    InputStream inStream;
    String comicURL;
    String nextURL = null;
    String prevURL = null;
    String beginURL = null;
    String endURL = null;
    @Override
    protected Panel doInBackground(URL... url){
        if(android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();

        URL comicList;
        try{
            comicList = url[0];
            connection = (HttpURLConnection)comicList.openConnection();
            connection.setDoInput(true);
            //connection.connect();

            int responseCode = connection.getResponseCode(); //can call this instead of con.connect()
            if (responseCode >= 400 && responseCode <= 499) {
                Log.e("Cannot Connect", "Bad authentication status: " + responseCode + "\n");
                throw new Exception("Bad authentication status: " + responseCode); //provide a more meaningful exception message
            }
            else {
                inStream = connection.getInputStream();
                //etc...
            }
            Log.v("msg", "Comic URL: " + comicList);

            Log.v("Internet: ", "This part reached\n");
            BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
            String line;
            comicURL = null;
            boolean found = false;
            int count = 0;
            while((line = in.readLine()) != null){
                Log.v("msg", line);
                if(line.contains("adamathome/")){
                    Log.v("msg", "");
                }
                count++;
                if (line.contains("assets.amuni") && line.contains("src=")){
                    int start = line.indexOf("src=") + 5;
                    int end = line.substring(start).indexOf('"') + start;
                    comicURL = line.substring(start, end);
                    found = true;
                    Log.v("msg", "Start: " + start + "\tEnd: " + end + "\t" + comicURL);
                }
                if(line.contains("eginning")){
                    int start = line.indexOf("=") + 2;
                    int end = line.substring(start).indexOf('"') + start;
                    beginURL = line.substring(start, end);
                }
                if(line.contains("older")){
                    int start = line.indexOf("f=") + 3;
                    int end = line.substring(start).indexOf('"') + start;
                    prevURL = "http://www.gocomics.com" + line.substring(start, end);
                    Log.v("msg", "Previous URL: " + prevURL);
                }
                if(line.contains("Newer")){
                    int start = line.indexOf("f=") + 3;
                    int end = line.substring(start).indexOf('"') + start;
                    nextURL = "http://www.gocomics.com" + line.substring(start, end);
                }
                if(line.contains("urrent")){
                    int start = line.indexOf("=") + 2;
                    int end = line.substring(start).indexOf('"') + start;
                    endURL = line.substring(start, end);
                }
            }

            //Log.v("msg", "Comic URL: " + comicURL);
            URL cURL = new URL(comicURL);
            connection = (HttpURLConnection)cURL.openConnection();
            connection.setDoInput(true);
            responseCode = connection.getResponseCode(); //can call this instead of con.connect()
            if (responseCode >= 400 && responseCode <= 499) {
                Log.e("Cannot Connect", "Bad authentication status: " + responseCode + "\n");
                throw new Exception("Bad authentication status: " + responseCode); //provide a more meaningful exception message
            }
            else {
                inStream = connection.getInputStream();
                comic = BitmapFactory.decodeStream(inStream);
            }

        } catch (Exception e) {
            String s = e.getMessage();
            //Log.d("msg", e.getMessage());
            e.printStackTrace();
        }
        Panel p = new Panel();
        p.pic = comic;
        p.url = comicURL;
        p.next = nextURL;
        p.prev = prevURL;
        p.beginning = beginURL;
        p.end = endURL;

        return p;
    }


}
