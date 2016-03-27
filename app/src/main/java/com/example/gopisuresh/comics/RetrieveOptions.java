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
    String comicURL = null;
    String nextURL = null;
    String prevURL = null;
    String beginURL = null;
    String endURL = null;
    URL comicList = null;
    @Override
    protected Panel doInBackground(URL... url){
        if(android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();


        try{
            comicList = url[0];
            connection = (HttpURLConnection)comicList.openConnection();
            connection.setDoInput(true);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2227.1 Safari/537.36");
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
            //Log.v("msg", "Comic URL: " + comicList);

            //Log.v("Internet: ", "This part reached\n");
            BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
            String line;
            comicURL = null;
            boolean found = false;
            int count = 0;
            while((line = in.readLine()) != null){
                //Log.v("msg", line);
                if(line.contains("adamathome/")){
                    Log.v("msg", "");
                }
                count++;
                if (line.contains("assets.amuni") && line.contains("src=")){
                    int start = line.indexOf("src=") + 5;
                    int end = line.substring(start).indexOf('"') + start;
                    comicURL = line.substring(start, end);
                    found = true;
                    break;
                    //Log.v("msg", "Start: " + start + "\tEnd: " + end + "\t" + comicURL);
                }
                if(line.contains("eginning") && line.contains("href=")){
                    int start = line.indexOf("=") + 2;
                    int end = line.substring(start).indexOf('"') + start;
                    beginURL = "http://www.gocomics.com" + line.substring(start, end);
                }
                if(line.contains("revious") && line.contains("href=")){
                    int start = line.indexOf("f=") + 3;
                    int end = line.substring(start).indexOf('"') + start;
                    prevURL = "http://www.gocomics.com" + line.substring(start, end);
                    //Log.v("msg", "Previous URL: " + prevURL);
                }
                if(line.contains("ext fea") && line.contains("href=")){
                    int start = line.indexOf("f=") + 3;
                    int end = line.substring(start).indexOf('"') + start;
                    nextURL = "http://www.gocomics.com" + line.substring(start, end);
                }
                if(line.contains("urrent") && line.contains("href=")){
                    int start = line.indexOf("=") + 2;
                    int end = line.substring(start).indexOf('"') + start;
                    endURL = "http://www.gocomics.com" + line.substring(start, end);
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
        p.current = comicList.toString();

        if(p.end == null)
            p.end = p.current;
        if(p.beginning == null)
            p.beginning = p.current;
        if(p.prev == null)
            p.prev = p.current;
        if(p.next == null)
            p.next = p.current;

        return p;
    }


}
