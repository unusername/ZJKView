package com.zjk.zjkview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjk.zjkview.R;

import java.util.List;

/**
 * Created by denengjiankang on 16/3/28.
 */
public class ListAdapter extends BaseAdapter {

    private List<String> list;
    private Context context;

    public ListAdapter(List<String> list,Context context){
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

        TextView textView;

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.string_item,null);
        textView = (TextView) convertView.findViewById(R.id.string_item_string);
        textView.setText(list.get(position));
        return convertView;
    }
}
