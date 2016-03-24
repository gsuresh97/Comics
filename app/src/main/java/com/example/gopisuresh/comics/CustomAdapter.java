package com.example.gopisuresh.comics;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Gopisuresh on 3/24/16.
 */
public class CustomAdapter extends BaseAdapter {
    ArrayList<ComicOption> list;
    MyActivity context;
    static LayoutInflater infl;
    public CustomAdapter(MyActivity act, ArrayList<ComicOption> l){
        context = act;
        list = l;
        infl = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class Holder{
        ImageView image;
        TextView author;
        TextView title;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        Holder cell = new Holder();
        final View rowView;
        rowView = infl.inflate(R.layout.custom_list, null);
        cell.image = (ImageView)rowView.findViewById(R.id.image_view_1);
        cell.author = (TextView)rowView.findViewById(R.id.author);
        cell.title = (TextView)rowView.findViewById(R.id.title);

        cell.image.setImageBitmap(list.get(position).getIcon());
        cell.title.setText(list.get(position).getTitle());
        cell.author.setText(list.get(position).getAuthor());

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.open_comic(rowView, list.get(position));
            }
        });

        return rowView;
    }

    @Override
    public int getCount(){
        return list.size();
    }
}
