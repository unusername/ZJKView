package com.zjk.zjkview.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.zjk.zjkview.R;

public class CountDownTextView extends View {

    private final int INVALIDATE =1;

    private final int COUNTDOWN = 1;

    /*
    * 文本*/
    private String countdownText;
    /*
    * 文本颜色*/
    private int countdownTextColor = Color.BLACK;
    /*
    * 文本大小*/
    private int countdownTextSize;

    /*控件所占空间大小*/
    private Rect mBond;
    private Paint mPaint;

    private CountDownThread thread;
    private Handler mhanler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case INVALIDATE:
                    postInvalidate();
                    break;
            }
        }
    };

    public CountDownTextView(Context context) {
        this(context,null);
    }

    public CountDownTextView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public CountDownTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CountDownTextView
        ,defStyleAttr,0);
        int n = a.getIndexCount();
        for (int i= 0;i<n;i++){
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.CountDownTextView_countdownText:
                    countdownText = a.getString(attr);
                    break;
                case R.styleable.CountDownTextView_countdownTextColor:
                    countdownTextColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CountDownTextView_countdownTextSize:
                    countdownTextSize = a.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                                    16,getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();

        /*
        * 设置画笔以及控件大小*/
        mPaint = new Paint();
        mPaint.setTextSize(countdownTextSize);
        mPaint.setColor(countdownTextColor);
        mBond = new Rect();
        mPaint.getTextBounds(countdownText, 0, countdownText.length(), mBond);
//        Log.e("countdownText", countdownText);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 0;
        int height = 0;

        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (specMode){
            case MeasureSpec.EXACTLY:
                width = getPaddingLeft()+getPaddingRight()+specSize;
                break;
            case MeasureSpec.AT_MOST:
                int size = getPaddingLeft()+getPaddingRight()+mBond.width();
                width = Math.min(size,specSize);
                break;
        }
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (specMode){
            case MeasureSpec.EXACTLY:
                height = getPaddingTop()+getPaddingBottom()+specSize;
                break;
            case MeasureSpec.AT_MOST:
                int size = getPaddingBottom()+getPaddingTop() + mBond.height();
                height = Math.min(size,specSize);
                break;
        }
        setMeasuredDimension(width,height);
        Log.e(width+"",height+"");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(countdownTextColor);
        canvas.drawText(countdownText, getWidth() / 2 - mBond.width() / 2,
                getHeight() / 2 + mBond.height()/2, mPaint);
        Log.e(getHeight()+"", getWidth() + "");
    }

    public void setTime(int second){
        CountDownThread thread = new CountDownThread();
        thread.time = second;
        thread.start();
    }

    private boolean countDown(int second){
        Message message = new Message();
        message.what = INVALIDATE;
        if (second>3600){
            int hr = second/3600;
            int min = second%3600/60;
//            Log.e("分钟",min+"");
            int sec = second%3600%60;
//            Log.e("秒",sec+"");
            countdownText = hr +"小时"+min+"分钟"+sec+"秒";
            mhanler.sendMessage(message);
            return true;
        }else if (second>60){
            int min = second/60;
            int sec = second%60;
            countdownText = min + "分钟"+sec+"秒";
            mhanler.sendMessage(message);
            return true;
        }else if (second>30){
            countdownText = second+"秒";

            mhanler.sendMessage(message);
            return true;
        }else if (second>0){
            countdownText = second+"秒";
            countdownTextColor = Color.RED;
            mhanler.sendMessage(message);
            return true;
        }else
            return false;
    }

    class CountDownThread extends Thread{
        int time;
        @Override
        public void run() {
            while (true){
                try {
                    sleep(1000);
                    time--;
                    if (!countDown(time)){
                        continue;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
