package com.example.gopisuresh.comics;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.ScriptGroup;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Gopisuresh on 3/18/16.
 */
public class ComicOption {
    String author;
    String file_author;
    String title;
    String file_title;
    String url;
    String file_url;
    String nextComic;
    Bitmap icon;

    public ComicOption(Context call, String file_aut, String file_tit, String nextCom){
        file_author = file_aut;
        file_title = file_tit;
        nextComic = nextCom;
        InputStream is = null;
        try{
            is = call.getAssets().open("icons/" + file_title.replaceAll("[^a-z0-9_]", "") + file_author.replaceAll("[^a-z0-9_]", "") + ".png");
        } catch (Exception e){
            e.printStackTrace();
        }
        icon = BitmapFactory.decodeStream(is);
    }

    public ComicOption(String line, String aut){
        //get Title
        int start = line.indexOf("<img alt=\"") + 10;
        int end = line.substring(start + 1).indexOf("\"") + start + 1;
        title = line.substring(start, end);
        file_title = title.replaceAll(" ", "_");

        //get author
        start = aut.indexOf(">") + 1;
        end = aut.length() - 7;
        author = aut.substring(start, end);
        file_author = author.replaceAll(" ", "_");
        if (title.contains("enny")){
            int x = 3;
        }
        //get url
        start = line.indexOf("\"60\" src=\"") + 10;
        end = line.indexOf(".png") + 6;
        url = line.substring(start, end);
        file_url = url.replaceAll(" ", "_");
        //Include internet permissions in Manifest
        //get icon

        try {
            URL iconURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) iconURL.openConnection();
            connection.setDoInput(true);
            int responseCode = connection.getResponseCode();
            InputStream in;
            if (responseCode >= 400 && responseCode <= 499) {
                Log.e("Cannot Connect", "Bad authentication status: " + responseCode + "\n");
                throw new Exception("Bad authentication status: " + responseCode); //provide a more meaningful exception message
            }
            else {
                in = connection.getInputStream();
            }
            icon = BitmapFactory.decodeStream(in);
            Bitmap bg1 = Bitmap.createScaledBitmap(icon, 120, 120, true);
            icon  = bg1;
        }catch (Exception e){
            e.printStackTrace();
        }

        Log.v("msg", toString());
    }

    public void setIcon(Bitmap ic){
        icon = ic;
    }

    public String getAuthor(){
        return file_author.replaceAll("_", " ");
    }

    public String getTitle(){
        return file_title.replaceAll("_", " ");
    }

    public String getURL(){
        return "http://www.gocomics.com" + nextComic;
    }

    public Bitmap getIcon(){
        return icon;
    }

    public String toString(){
        //Log.d("msg", title + "\nBy: " + author + "\n");
        return getTitle() + "\nBy: " + getAuthor();
    }
}

