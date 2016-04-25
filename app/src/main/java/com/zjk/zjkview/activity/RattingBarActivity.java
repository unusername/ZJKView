package com.zjk.zjkview.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zjk.zjkview.R;
import com.zjk.zjkview.myview.CustomRatingBar;

public class RattingBarActivity extends Activity {

    private CustomRatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratting_bar);
        initView();
    }
    private void initView (){
        ratingBar = (CustomRatingBar) findViewById(R.id.my_ratting_bar);
        ratingBar.setListen(true);
        ratingBar.setScores((float) 3.5);
    }

}
