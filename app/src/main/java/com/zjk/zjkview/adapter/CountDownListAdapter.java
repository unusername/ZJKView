package com.zjk.zjkview.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjk.zjkview.R;
import com.zjk.zjkview.myview.CountDownTextView;

import java.util.List;

/**
 * Created by denengjiankang on 16/3/29.
 */
public class CountDownListAdapter extends BaseAdapter implements CountDownTextView.CountDownListener {

    private Context context;
    private List<Integer> list;
    int i = 0;

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
        ViewHander viewHander;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.countdown_item,null);
            viewHander = new ViewHander();
            viewHander.textView = (CountDownTextView) convertView.
                    findViewById(R.id.countdown_textView);
            convertView.setTag(viewHander);
        }else {
            viewHander = (ViewHander) convertView.getTag();
        }
        ((ViewHander) convertView.getTag()).textView.setTime(list.get(position));
        viewHander.textView.setCountDownID(position);
        viewHander.textView.setOnCountDownListener(this);
        return convertView;
    }

    @Override
    public void onCountDownStop(int id, CountDownTextView textView) {
        textView.setText("倒计时结束");
    }

    class ViewHander{
        CountDownTextView textView;
    }


}
