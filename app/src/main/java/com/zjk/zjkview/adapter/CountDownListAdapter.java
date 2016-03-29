package com.zjk.zjkview.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zjk.zjkview.R;
import com.zjk.zjkview.myview.CountDownTextView;

import java.util.List;

/**
 * Created by denengjiankang on 16/3/29.
 */
public class CountDownListAdapter extends BaseAdapter {

    private Context context;
    private List<Integer> list;

    public CountDownListAdapter (Context context,List<Integer> list){
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
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
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.countdown_item,null);
        CountDownTextView textView = (CountDownTextView) convertView.
                        findViewById(R.id.countdown_textView);
        textView.setTime(list.get(position));
        return convertView;
    }
}
