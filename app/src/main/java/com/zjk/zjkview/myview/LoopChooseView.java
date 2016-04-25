package com.zjk.zjkview.myview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import com.zjk.zjkview.R;
import com.zjk.zjkview.been.LoopText;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by denengjiankang on 16/4/13.
 */
public class LoopChooseView extends View {

    private int textsize;
    private Paint paintOuterText;
    private Paint paintCenterText;
    private Paint paintline;
    private Rect bond;
    private MoveBySelf moveBySelf = new MoveBySelf();
    private float padding;

    // Timer mTimer;
    ScheduledExecutorService mExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> mFuture;

    private List<List<LoopText>> looplist;

    private List<LoopText> list = new ArrayList<>();

    private int height;

    private List<Integer> centerdexin = new ArrayList<>();


    float touchY;
    float downY;
    float touchX;
    float firstlocation;
    float firstline;
    float secondline;
    private int centercolor;
    private int outercolor;


    private VelocityTracker tracker = null;
    private int movefast;

    public LoopChooseView(Context context) {
        this(context,null);
    }

    public LoopChooseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoopChooseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        moveBySelf.start();
        TypedArray array = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.LoopChooseView,defStyleAttr,0
        );
        int n = array.getIndexCount();
        for (int i = 0;i<n;i++){
            int attr = array.getIndex(i);
            if (attr == R.styleable.LoopChooseView_LoopChooseViewTextSize){
                textsize = array.getDimensionPixelSize(attr,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                16,getResources().getDisplayMetrics()));
            }else if (attr == R.styleable.LoopChooseView_LoopChooseViewCenterColor){
                centercolor = array.getColor(attr,Color.BLACK);
            }else if (attr == R.styleable.LoopChooseView_LoopChooseViewOuterColor){
                outercolor = array.getColor(attr,Color.GRAY);
            }else if (attr == R.styleable.LoopChooseView_LoopChooseViewLinePadding){
                padding = array.getDimension(attr,TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        5,getResources().getDisplayMetrics()));
            }
        }
        array.recycle();

        paintCenterText = new Paint();
        paintOuterText = new Paint();
        paintCenterText.setTextSize(textsize);
        paintOuterText.setTextSize(textsize);
        paintline = new Paint();
        paintline.setColor(centercolor);
        paintCenterText.setColor(centercolor);
        paintOuterText.setColor(outercolor);
        bond = new Rect();
        paintCenterText.getTextBounds("sdf", 0, 0, bond);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setLocation();
    }

    private void setLocation(){
        for (int j = 0;j<looplist.size();j++) {
            for (int i = -(centerdexin.get(j)); i < looplist.get(j).size()
                    -centerdexin.get(j); i++) {
//            Log.e("location",getHeight()+"");
                looplist.get(j).get(i+centerdexin.get(j)).setLocation((float) (22.5 * i));
            }
        }
    }

    protected final void scrollBy(float velocityY) {
//        cancelFuture();
        // 修改这个值可以改变滑行速度
//        int velocityFling = 10;
//        mFuture = mExecutor.scheduleWithFixedDelay(new TimerTask(), 0, velocityFling, TimeUnit.MILLISECONDS);
    }
//
//    public void cancelFuture() {
//        if (mFuture!=null&&!mFuture.isCancelled()) {
//            mFuture.cancel(true);
//            mFuture = null;
//        }
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint.FontMetricsInt fontMetricsInt = paintCenterText.getFontMetricsInt();
        secondline = (getHeight() / 2) + ((fontMetricsInt.bottom-fontMetricsInt.top)/2) + padding;
        firstline = (getHeight() / 2) - ((fontMetricsInt.bottom-fontMetricsInt.top)/2) - padding;
        firstlocation = (float) Math.toRadians(Math.asin(((fontMetricsInt.bottom-fontMetricsInt.top)/2+padding)/(getHeight()/2)));
//        Log.e("firstlocation",(float)(((fontMetricsInt.bottom-fontMetricsInt.top)/2+20)/(getHeight()/2))+"");
        canvas.drawLine(0.0F, (getHeight() / 2) + ((fontMetricsInt.bottom-fontMetricsInt.top)/2) + padding, getWidth(),
                (getHeight() / 2) + ((fontMetricsInt.bottom-fontMetricsInt.top)/2) + padding, paintline);
//        Log.e("line",getHeight()/2-bond.height()/2+"");
        canvas.drawLine(0.0F, (getHeight() / 2) - ((fontMetricsInt.bottom-fontMetricsInt.top)/2) -padding, getWidth(),
                (getHeight() / 2) - ((fontMetricsInt.bottom-fontMetricsInt.top)/2) - padding, paintline);
//        canvas.drawLine(0.0F,getHeight()/2,getWidth(),getHeight()/2,paintline);
//        canvas.drawLine(0.0F,fontMetricsInt.,getWidth(),getHeight()/2,paintline);



//        int baseline = fontMetricsInt.top-bond.height()/2;

        for (int j = 0;j<looplist.size();j++) {
            for (int i = 0; i < looplist.get(j).size(); i++) {
                paintCenterText.getTextBounds(looplist.get(j).get(i).getText(),0
                ,0,bond);
                if (looplist.get(j).get(i).getLocation() < 90F && looplist.get(j).get(i).getLocation() > -90F) {
                    canvas.save();

                    float location;
                    float textY;
                    float topline;
                    float bottomline;
                    float centerline;
                    if (looplist.get(j).get(i).getLocation() > 0) {

                        location = looplist.get(j).get(i).getLocation();
                        textY = (float) Math.sin(Math.PI * location / 180) * getHeight() / 2 + getHeight() / 2
                                +(fontMetricsInt.bottom-fontMetricsInt.top)/2-fontMetricsInt.bottom;
                        topline = (float) Math.sin(Math.PI * location / 180) * getHeight() / 2 + getHeight() / 2
                                -(fontMetricsInt.bottom-fontMetricsInt.top)/2;
                        bottomline = (float) Math.sin(Math.PI * location / 180) * getHeight() / 2 + getHeight() / 2
                                +(fontMetricsInt.bottom-fontMetricsInt.top)/2;
                        centerline = (float) Math.sin(Math.PI * location / 180) * getHeight() / 2 + getHeight() / 2;

                    } else {

                        location = -looplist.get(j).get(i).getLocation();
                        textY = getHeight() / 2 - (float) (Math.sin(Math.PI * location / 180) * getHeight() / 2)
                        +(fontMetricsInt.bottom-fontMetricsInt.top)/2-fontMetricsInt.bottom;
                        topline = getHeight() / 2 - (float) (Math.sin(Math.PI * location / 180) * getHeight() / 2)
                                -(fontMetricsInt.bottom-fontMetricsInt.top)/2;
                        bottomline = getHeight() / 2 - (float) (Math.sin(Math.PI * location / 180) * getHeight() / 2)
                                +(fontMetricsInt.bottom-fontMetricsInt.top)/2;
                        centerline = getHeight() / 2 - (float) (Math.sin(Math.PI * location / 180) * getHeight() / 2);


                    }

                    looplist.get(j).get(i).setY(centerline);


//                    canvas.drawLine(0.0F,textY,getWidth(),textY,paintline);
                    paintCenterText.getTextBounds(looplist.get(j).get(i).getText(), 0,
                            looplist.get(j).get(i).getText().length(),
                            bond);
                    float textheight = (float) Math.cos(Math.PI * location / 180) * bond.height();

//                    Log.e(fontMetricsInt.bottom + "", fontMetricsInt.top + ":" + bond.height());
                    float textX = getWidth()/looplist.size()/2-bond.width()/2+
                            getWidth()/looplist.size()*j;

//            Log.e(looplist.get(j).get(i).getText()+"", textY+"");
//            canvas.clipRect(0,list.get(i).getLocation()-textheight/2,
//                    getWidth(),list.get(i).getLocation()+textheight/2);
                    canvas.translate(0.0F, textY);
                    canvas.scale(1.0F, textheight / bond.height());
                    if (topline<firstline&&bottomline>firstline){
//                        Log.e("变色", "上线变色");

                        canvas.save();
                        canvas.clipRect(0,fontMetricsInt.top, getWidth(),
                                firstline-textY);
                        canvas.drawText(looplist.get(j).get(i).getText(), textX,
                                0, paintOuterText);
                        canvas.restore();
                        canvas.save();
                        canvas.clipRect(0,firstline-textY,getWidth(),
                                fontMetricsInt.bottom);
                        canvas.drawText(looplist.get(j).get(i).getText(), textX,
                                0, paintCenterText);
                        canvas.restore();
                    }else if (topline<secondline&&bottomline>secondline){
//                        Log.e("变色","下线变色");

                        canvas.save();
                        canvas.clipRect(0, fontMetricsInt.top, getWidth(),
                                secondline - textY);
                        canvas.drawText(looplist.get(j).get(i).getText(), textX,
                                0, paintCenterText);
                        canvas.restore();
                        canvas.save();
                        canvas.clipRect(0,secondline-textY,getWidth(),
                                bottomline);
                        canvas.drawText(looplist.get(j).get(i).getText(),textX,
                                0,paintOuterText);
                        canvas.restore();
                    }else if (bottomline<secondline&&topline>firstline){
//                        Log.e("变色","两线中间");
                        canvas.drawText(looplist.get(j).get(i).getText(),textX,
                                0,paintCenterText);
                    }else {
//                            Log.e("变色","两线之外");
                        if (j == 0 && i == 0){
                            Log.e(""+textY,textX+":"+looplist.get(0).get(0).getLocation());
                        }
                            canvas.drawText(looplist.get(j).get(i).getText(),textX,
                                    0,paintOuterText);
                    }

//                    canvas.drawText(looplist.get(j).get(i).getText(), textX,
//                            0, paintCenterText);
//                    canvas.drawText();
//            Log.e("",textY+"");
                    canvas.restore();
                }
            }
        }
    }

    public void setLoopList(List<List<LoopText>> list){
        this.looplist = list;
        for (int i = 0;i<list.size();i++){
            centerdexin.add(1);
            this.list.add(list.get(i).get(0));
        }
//        getHeight();
//        postInvalidate();
//        postInvalidate();
    }

    public void setStartList(List<Integer> list){
        this.centerdexin = list;
        setLocation();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            touchY = event.getY();
            touchX  = event.getX();
            downY = event.getY();
            if (tracker == null){
                tracker = VelocityTracker.obtain();
            }else {
                tracker.clear();
            }
            tracker.addMovement(event);
        }else if (event.getAction() == MotionEvent.ACTION_MOVE){
            float moveY = event.getY()-touchY;
            touchY = event.getY();
            tracker.addMovement(event);
            tracker.computeCurrentVelocity(10);
            Log.e("滑动速度", "" + tracker.getYVelocity());
            if (tracker.getYVelocity()<-70){
                movefast = 1;
            }else if (tracker.getYVelocity()>70){
                movefast = 2;
            }

                for (int j = 0; j < looplist.size(); j++) {
                    if (touchX > getWidth() / looplist.size() * j &&
                            touchX < getWidth() / looplist.size() * (j + 1)) {
//                    if (looplist.get(j).get(0).getLocation()>0){
//                        looplist.get(j).get(0).setLocation(0);
//                    }else if(looplist.get(j).get(
//                            looplist.get(j).size()-1).getLocation()<0){
//                        looplist.get(j).get(
//                                looplist.get(j).size()-1).setLocation(0);
//                    }else {
//                    Log.e(getHeight()+"","dfgdfgdfgdfgdfgdfgdfgdfgdgf"+looplist.get(j).get(0).getLocation());
                        if (looplist.get(j).get(0).getLocation() + moveY * 0.2F <= 0 &&
                                looplist.get(j).get(
                                        looplist.get(j).size() - 1
                                ).getLocation() + moveY * 0.2F >= 0) {
                            for (int i = 0; i < looplist.get(j).size(); i++) {
                                looplist.get(j).get(i).setLocation(
                                        looplist.get(j).get(i).getLocation() +
                                                moveY * 0.2F);
//                        Log.e(i+"",looplist.get(j).get(i).getLocation()+"");

//                Log.e(getHeight() + "", "dfgdfgdfgdfgdfgdfgdfgdfgdgf" + looplist.get(j).get(0).getLocation());
                            }
                            for (int i = 0; i < looplist.get(j).size(); i++) {
                                if (looplist.get(j).get(i).getLocation() < 22.5 &&
                                        looplist.get(j).get(i).getLocation() > -22.5) {
//                            Log.e("dfsdfsdf"+i,"sdfsdfsdf");
                                    if (i > 0 && i < looplist.get(j).size() - 1) {
                                        if (event.getY() < downY) {
                                            if (getHeight() / 2 - looplist.get(j).get(i).getY() >
                                                    looplist.get(j).get(i + 1).getY() - getHeight() / 2) {
                                                centerdexin.set(j, i + 1);
                                                Log.e("上滑上", looplist.get(j).get(i).getText());
                                            } else {
                                                centerdexin.set(j, i);
                                                Log.e("上滑下", looplist.get(j).get(i).getText());
                                            }
                                        } else if (event.getY() > downY) {
                                            if (getHeight() / 2 - looplist.get(j).get(i - 1).getY() <
                                                    looplist.get(j).get(i).getY() - getHeight() / 2) {
                                                centerdexin.set(j, i - 1);
                                                Log.e("下滑下", looplist.get(j).get(i).getText());
                                            } else {
                                                centerdexin.set(j, i);
                                                Log.e("下滑上", looplist.get(j).get(i).getText());
                                            }
                                        }

                                    }
                                }

                            }
                        }
//                    }

                    }

                }
        } else if (event.getAction() == MotionEvent.ACTION_UP){
//            tracker.addMovement(event);
//            tracker.computeCurrentVelocity(1000000);
//            Log.e("滑动速度",""+tracker.getYVelocity());
            for (int i = 0;i<looplist.size();i++){
                setLocation();
//                if (movefast == 1) {
//
//                    if (touchX > getWidth() / looplist.size() * i &&
//                            touchX < getWidth() / looplist.size() * (i + 1)) {
//                        Message message = new Message();
//                        message.obj = i;
//                        message.arg1 = 1;
//                        moveBySelf.handler.sendMessage(message);
//                    }
//                }else if (movefast == 2){
//                    if (touchX > getWidth() / looplist.size() * i &&
//                            touchX < getWidth() / looplist.size() * (i + 1)) {
//                        Message message = new Message();
//                        message.obj = i;
//                        message.arg1 = 2;
//                        moveBySelf.handler.sendMessage(message);
//                    }
//                }
                for (int j = 0;j<looplist.get(i).size();j++){
                    if (looplist.get(i).get(j).getLocation()<22.5&&
                            looplist.get(i).get(j).getLocation()>-22.5){
                        centerdexin.set(i,j);
                        list.set(i,looplist.get(i).get(j));
                    }
                }

            }

        }
//
        postInvalidate();

        return true;
    }

    class MoveBySelf extends Thread{
        Handler handler;
        @Override
        public void run() {
            Looper.prepare();
            handler = new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    int j = (int) msg.obj;
                    int i = 10;
                    while (i > 0){
                        try {
                            sleep(100);
                                for (int k = 0; k < looplist.get(j).size(); k++) {
                                    if (msg.arg1 == 1){
                                        looplist.get(j).get(k).setLocation(
                                                looplist.get(j).get(k).getLocation()-k*0.2F);
                                    }else if (msg.arg1 == 2){
                                        looplist.get(j).get(k).setLocation(
                                                looplist.get(j).get(k).getLocation()+k*0.2F);
                                    }
                                    if (looplist.get(j).get(k).getLocation() < 22.5 &&
                                            looplist.get(j).get(k).getLocation() > -22.5) {
                                        Log.e("快速滑动","sdfsdf");
//                            Log.e("dfsdfsdf"+i,"sdfsdfsdf");
                                        if (k > 0 && k< looplist.get(j).size() - 1) {
                                            if (msg.arg1 == 1) {
                                                if (getHeight() / 2 - looplist.get(j).get(k).getY() >
                                                        looplist.get(j).get(k + 1).getY() - getHeight() / 2) {
                                                    centerdexin.set(j, k + 1);
//                                                    Log.e("上滑上", looplist.get(j).get(k).getText());
                                                } else {
                                                    centerdexin.set(j, k);
//                                                    Log.e("上滑下", looplist.get(j).get(k).getText());
                                                }
                                            } else if (msg.arg1 == 2) {
                                                if (getHeight() / 2 - looplist.get(j).get(k - 1).getY() <
                                                        looplist.get(j).get(k).getY() - getHeight() / 2) {
                                                    centerdexin.set(j, k - 1);
//                                                    Log.e("下滑下", looplist.get(j).get(k).getText());
                                                } else {
                                                    centerdexin.set(j, k);
//                                                    Log.e("下滑上", looplist.get(j).get(k).getText());
                                                }
                                            }

                                        }
                                }
                            }
                            postInvalidate();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            Looper.loop();
        }
    }
}
