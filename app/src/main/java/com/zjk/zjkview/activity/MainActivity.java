package com.zjk.zjkview.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zjk.zjkview.R;
import com.zjk.zjkview.adapter.ListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private ListView listView;//自定义控件列表

    private List<String> list;

    private ListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        listView = (ListView) findViewById(R.id.listView);
        initList();
        adapter = new ListAdapter(list,MainActivity.this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this,CountDownActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent();
                        intent1.setClass(MainActivity.this, RattingBarActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent();
                        intent2.setClass(MainActivity.this,LoopViewActivity.class);
                        startActivity(intent2);
                        break;
                }
            }
        });
    }

    private void initList(){
        list = new ArrayList<>();
        list.add("倒计时TextView");
        list.add("自定义RattingBar");
        list.add("自定义滚轮选择器");
    }


}
