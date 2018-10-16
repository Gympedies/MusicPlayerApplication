package com.example.lyy.musicapplication;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            //convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.support_simple_spinner_dropdown_item,null);
            //TextView tv = findViewById(position);
            //tv.setBackgroundColor(Color.YELLOW);
        }
        return convertView;

    }
}
