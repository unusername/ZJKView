package com.zjk.zjkview.myview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.zjk.zjkview.R;
import com.zjk.zjkview.been.Star;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by denengjiankang on 16/4/11.
 */
public class CustomRatingBar extends View {

    private Bitmap fullImage;
    private Bitmap halfImage;
    private Bitmap nullImage;

    private Paint paint;
    private int width;
    private int height;
    private int interval;

    private List<Star> list;

    private float scores = 0;
    private boolean islisten = false;

    private int lock = 1;
    private int drawstarindex;

    public CustomRatingBar(Context context) {
        this(context,null);
    }

    public CustomRatingBar(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public CustomRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setStarList();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomRatingBar,
                defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i=0;i<n;i++){
            int attr = a.getIndex(i);
            Resources resources = getResources();
            InputStream inputStream;
            if (attr == R.styleable.CustomRatingBar_CustomRatingBarFullImage){
                inputStream = resources.openRawResource(
                        a.getResourceId(attr,R.mipmap.ic_launcher));
                fullImage = BitmapFactory.decodeStream(inputStream);
            }else if (attr == R.styleable.CustomRatingBar_CustomRatingBarHalfImage){
                inputStream = resources.openRawResource(a.getResourceId(
                        attr,R.mipmap.ic_launcher
                ));
                halfImage = BitmapFactory.decodeStream(inputStream);
            }else if (attr == R.styleable.CustomRatingBar_CustomRatingBarNullImage){
                inputStream = resources.openRawResource(
                        a.getResourceId(attr,R.mipmap.ic_launcher)
                );
                nullImage = BitmapFactory.decodeStream(inputStream);
            }else if (attr == R.styleable.CustomRatingBar_CustomRatingBarinterval){
                interval = a.getDimensionPixelSize(attr,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                3,getResources().getDisplayMetrics()));
            }
        }

        paint = new Paint();
    }

    private void setStarList(){
        list = new ArrayList<>();
        Star star = new Star();
        star.setIndex(1);
        star.setIsanimation(true);
        list.add(star);
        star = new Star();
        star.setIndex(2);
        star.setIsanimation(false);
        list.add(star);
        star = new Star();
        star.setIndex(3);
        star.setIsanimation(false);
        list.add(star);
        star = new Star();
        star.setIndex(4);
        star.setIsanimation(false);
        list.add(star);
        star = new Star();
        star.setIndex(5);
        star.setIsanimation(false);
        list.add(star);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY){
            width = getPaddingLeft()+getPaddingRight()+specSize;
        }else if (specMode == MeasureSpec.AT_MOST){
            width = getPaddingLeft()+getPaddingRight()+fullImage.getWidth()+
                    interval*5;
        }
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY){
            height = getPaddingTop()+getPaddingBottom()+specSize;
        }else if (specMode == MeasureSpec.AT_MOST){
            height = getPaddingTop()+getPaddingBottom()+
                    fullImage.getHeight();
        }
        setMeasuredDimension(width, height);
        if (width/5-interval<height){
            fullImage = zoomImg(fullImage,width/5-interval,width/5-interval);
            halfImage = zoomImg(halfImage,width/5-interval,width/5-interval);
            nullImage = zoomImg(nullImage,width/5-interval,width/5-interval);
        }else {
            fullImage = zoomImg(fullImage,height,height);
            halfImage = zoomImg(halfImage,height,height);
            nullImage = zoomImg(nullImage,height,height);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//
        for (int j = -11;j<=1;j++) {
            for (int i = 1; i <= 5; i++) {
                if (!list.get(i).isanimation()) {
                    if (i > scores && scores > i - 1) {
                        drawStar(i, 2, canvas);
                    } else if (i == scores) {
                        drawStar(i, 1, canvas);
                    } else if (i > scores) {
                        drawStar(i, 3, canvas);
                    } else if (i < scores) {
                        drawStar(i, 1, canvas);
                    }
                }else {
                    drawStarByAnimation(list.get(i),canvas,fullImage.getHeight()/10,
                            11-j);
                }
            }
        }

    }

    private void drawStarByAnimation(Star star,Canvas canvas,int length,
                                     int index){
        canvas.drawBitmap(zoomImg(fullImage,length*index,length*index),
                star.getIndex()*width/5+interval+(10-index)*length,
                star.getIndex()*width/5+(10-index)*length,
                paint);
    }

    private void drawStar(int index,int type,Canvas canvas){

                if (type == 1) {
                    canvas.drawBitmap(fullImage,
                            interval / 2 + width / 5 * (index - 1),
                            0, paint);
//                    Log.e(index+"",fullImage.getWidth()/30*(-2-i)+"");
                } else if (type == 2) {
                    canvas.drawBitmap(halfImage,
                            interval / 2 + width / 5 * (index - 1),
                            0, paint);
                } else {
                    canvas.drawBitmap(nullImage,
                            interval / 2 + width / 5 * (index - 1),
                            0, paint);
                }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (islisten) {
            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction()
                    == MotionEvent.ACTION_MOVE) {
                Log.e("width", event.getX() + "");
                if (event.getX() > width / 5 * 4) {
                    scores = 5;
//                postInvalidate();
                } else if (event.getX() > width / 5 * 3) {
                    scores = 4;
//                postInvalidate();
                } else if (event.getX() > width / 5 * 2) {
                    scores = 3;
//                postInvalidate();
                } else if (event.getX() > width / 5 * 1) {
                    scores = 2;
//                postInvalidate();
                } else if (event.getX() > width / 5 * 0.5) {
                    scores = 1;
//                postInvalidate();
                } else if (event.getX() > 0) {
                    scores = 0;
//                postInvalidate();
                }
                postInvalidate();
            }
        }
        return true;

    }

    public void setScores(float scores){
        this.scores = scores;
        postInvalidate();
    }
/*
* 设置RattingBar是否可以点击滑动*/
    public void setListen(boolean b){
        this.islisten = b;
    }

    public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
//        Log.e("width",width+"");
        int height = bm.getHeight();
//        Log.e("height",height+"");
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }
}
