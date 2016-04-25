package com.zjk.zjkview.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zjk.zjkview.R;
import com.zjk.zjkview.been.LoopText;
import com.zjk.zjkview.myview.LoopChooseView;

import java.util.ArrayList;
import java.util.List;

public class LoopViewActivity extends Activity {

    private LoopChooseView loopChooseView;
    private List<LoopText> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loop_view);
        initView();
    }

    private void initView(){
        List<List<LoopText>> looplist = new ArrayList<>();
        loopChooseView = (LoopChooseView) findViewById(R.id.loopview);
        for (int j = 1;j<4;j++) {
            list = new ArrayList<>();
            for (int i = 100; i < 150; i++) {
                LoopText loopText = new LoopText();
                loopText.setText(i+j+"");
                list.add(loopText);
            }
            looplist.add(list);
        }
//        list = new ArrayList<>();
//        LoopText loopText = new LoopText();
//        loopText.setText("测试");
//        list.add(loopText);
//        looplist.add(list);
//        looplist.add(list);
//        looplist.add(list);
        loopChooseView.setLoopList(looplist);
        List<Integer> list1 = new ArrayList<>();
        list1.add(5);
        list1.add(1);
        list1.add(8);
        loopChooseView.setStartList(list1);
    }

}
