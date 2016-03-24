package com.example.gopisuresh.comics;

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
public class RetrieveOptions extends AsyncTask<String, Object, ArrayList<ComicOption>> {

    @Override
    protected ArrayList<ComicOption> doInBackground(String... url){
        if(android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();

        URL comicList;
        ArrayList<ComicOption> list = new ArrayList<>();
        try{
            comicList = new URL(url[0]);
            HttpURLConnection connection = (HttpURLConnection)comicList.openConnection();
            connection.setDoInput(true);
            //connection.connect();
            InputStream inStream;
            int responseCode = connection.getResponseCode(); //can call this instead of con.connect()
            if (responseCode >= 400 && responseCode <= 499) {
                Log.e("Cannot Connect", "Bad authentication status: " + responseCode + "\n");
                throw new Exception("Bad authentication status: " + responseCode); //provide a more meaningful exception message
            }
            else {
                inStream = connection.getInputStream();
                //etc...
            }

            Log.v("Internet: ", "This part reached\n");
            BufferedReader in = new BufferedReader(new InputStreamReader(inStream));

            String line;
            String context;
            String author;
            while((line = in.readLine()) != null){
                //Log.v("msg", line);
                if (line.contains("height=\"60\"")){
                    context = line;
                    //Log.v("msg", "Image line found");
                    int count = 1;
                    while (!line.contains("author")){
                        //Log.v("msg", "Pass " + count + " of loop.");
                        line = in.readLine();
                        count++;
                        //Log.v("msg", line);
                    }
                    author = line;
                    list.add(new ComicOption(context, author));
                }
                //Log.v("msg", "Processed");
            }

        } catch (Exception e) {
            String s = e.getMessage();
            Log.d("msg", e.getMessage());
            e.printStackTrace();
        }
        return list;
    }


}
