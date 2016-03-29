package com.zjk.zjkview.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.zjk.zjkview.R;
import com.zjk.zjkview.adapter.CountDownListAdapter;

import java.util.ArrayList;
import java.util.List;

public class CountDownActivity extends Activity {

    private ListView listView;
    private CountDownListAdapter adapter;
    private List<Integer> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down);
        initView();
    }

    private void initView(){
        listView = (ListView) findViewById(R.id.countdown_listView);
        initList();
        adapter = new CountDownListAdapter(CountDownActivity.this,list);
        listView.setAdapter(adapter);
    }

    private void initList(){
        list = new ArrayList<>();
        list.add(3700);
        list.add(560);
        list.add(50);
    }

}
