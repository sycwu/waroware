package com.celeste.waroware;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class BaseballView extends View implements SensorEventListener{

    private Sensor mSensor;
    private SensorManager mSensorManager;
    private Drawable mBackground;
    private Drawable mBaseball;
    private AnimationDrawable mSwingAnimation;
    private BaseballListener local;

    private Boolean mAnimationCompleted;
    private Boolean mBallHit;
    private int mFrameCounter;
    private int mBaseballCounter;
    private int mBaseballMax;
    private int mBaseballSpeed;
    private Rect mBaseballCords;
    private long mLastTime;
    private boolean mFirstFrame;

    private Drawable mSwingFrame1;
    private Drawable mSwingFrame2;
    private Drawable mSwingFrame3;
    private Drawable mSwingFrame4;
    private Drawable mSwingFrame5;
    private Drawable mSwingFrame6;
    private Drawable mSwingFrame7;
    private Drawable mSwingFrame8;
    private Drawable mSwingFrame9;
    private Drawable mSwingFrame10;
    private Drawable mSwingFrame11;
    private Drawable mSwingFrame12;

    private float mLastX;
    private float mLastY;
    private float mLastZ;

    //private Paint mPaint;


    public BaseballView(Context context) {
        super(context);
    }
    public BaseballView(Context context, AttributeSet attrs) {
        super(context, attrs);
        whenCreated();
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        long now = System.currentTimeMillis();
        if((now - mLastTime) > 100) {
            long diff = now - mLastTime;
            float speed = Math.abs(sensorEvent.values[0] + sensorEvent.values[1] + sensorEvent.values[2] - mLastX - mLastY - mLastZ) / diff * 10000;
            Log.d("rotation", "" + speed);
            if(speed > 2000 && mFrameCounter == 1) {
                Log.d("rotation", "sensor");
                mFrameCounter++;
            }
            mLastTime = now;
        }
        invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onDraw(Canvas canvas) {
        mBackground.setBounds(0, 0, 1280, 800);
        mBackground.draw(canvas);

        if(mFrameCounter == 1) {
            mSwingFrame1.setBounds(100, 300, 500, 580);
            mSwingFrame1.draw(canvas);
        }

        if(mBaseballCounter >= mBaseballMax && mBallHit == false) {
            mBaseballCords.offset(-20, 0);
        }
        if(mBallHit) {
            mBaseballCords.offset(100, 0);
        }
        if(mAnimationCompleted == false && mFrameCounter != 1) {
            swingAnimation(mFrameCounter, canvas);
            mFrameCounter++;
        }

        if(mFrameCounter == 13) {
            mSwingFrame12.setBounds(100, 300, 500, 580);
            mSwingFrame12.draw(canvas);
        }

        mBaseball.setBounds(mBaseballCords);
        mBaseball.draw(canvas);
        mBaseballCounter++;

        //Rect mHitbox = new Rect(300, 0, 600, 800);
        //canvas.drawRect(mHitbox, mPaint);
        if(mBaseballCords.left < 100) {
            mSensorManager.unregisterListener(this);
            if(getBaseballListener()!=null) {
                getBaseballListener().onFinish(false);
                //insert lose condition here
            }
            }
    }


    public void whenCreated() {
        mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensor, 0);

        Resources res = getResources();
        mBackground = res.getDrawable(R.drawable.baseballbackground);
        mBaseball = res.getDrawable(R.drawable.baseball);
        mBaseballCords = new Rect(1280, 430, 1300, 450);
        mBaseball.setBounds(mBaseballCords);
        mLastTime = System.currentTimeMillis();
        mFirstFrame = true;

        onCreateSwingAnimation();

        mAnimationCompleted = false;
        mFrameCounter = 1;
        mBaseballCounter = 0;
        mBaseballMax = (int) (Math.random() * 100);
        mBaseballSpeed = (int) ((Math.random() * 20) + 20);
        mBallHit = false;

        mLastX = -1.0f;
        mLastY = -1.0f;
        mLastZ = -1.0f;

        //mPaint = new Paint();
        //mPaint.setColor(0xffffffff);
    }

    public void onCreateSwingAnimation() {
        Resources res = getResources();
        mSwingFrame1 = res.getDrawable(R.drawable.swingframe1);
        mSwingFrame2 = res.getDrawable(R.drawable.swingframe2);
        mSwingFrame3 = res.getDrawable(R.drawable.swingframe3);
        mSwingFrame4 = res.getDrawable(R.drawable.swingframe4);
        mSwingFrame5 = res.getDrawable(R.drawable.swingframe5);
        mSwingFrame6 = res.getDrawable(R.drawable.swingframe6);
        mSwingFrame7 = res.getDrawable(R.drawable.swingframe7);
        mSwingFrame8 = res.getDrawable(R.drawable.swingframe8);
        mSwingFrame9 = res.getDrawable(R.drawable.swingframe9);
        mSwingFrame10 = res.getDrawable(R.drawable.swingframe10);
        mSwingFrame11 = res.getDrawable(R.drawable.swingframe11);
        mSwingFrame12 = res.getDrawable(R.drawable.swingframe12);
    }

    public void swingAnimation(int i, Canvas canvas) {
        switch(i) {
            case 2:
                mSwingFrame1.setBounds(0, 0, 0, 0);
                mSwingFrame2.setBounds(100, 300, 500, 580);
                Log.d("r", "swingAnimation");
                mSwingFrame1.draw(canvas);
                mSwingFrame2.draw(canvas);
                break;
            case 3:
                mSwingFrame2.setBounds(0, 0, 0, 0);
                mSwingFrame3.setBounds(100, 300, 500, 580);
                Log.d("r", "secondswing");
                mSwingFrame2.draw(canvas);
                mSwingFrame3.draw(canvas);
                break;
            case 4:
                mSwingFrame3.setBounds(0, 0, 0, 0);
                mSwingFrame4.setBounds(100, 300, 500, 580);
                mSwingFrame3.draw(canvas);
                mSwingFrame4.draw(canvas);
                break;
            case 5:
                mSwingFrame4.setBounds(0, 0, 0, 0);
                mSwingFrame5.setBounds(100, 300, 500, 580);
                mSwingFrame4.draw(canvas);
                mSwingFrame5.draw(canvas);
                break;
            case 6:
                mSwingFrame5.setBounds(0, 0, 0, 0);
                mSwingFrame6.setBounds(100, 300, 500, 580);
                mSwingFrame5.draw(canvas);
                mSwingFrame6.draw(canvas);
                isBallHit();
                break;
            case 7:
                mSwingFrame6.setBounds(0, 0, 0, 0);
                mSwingFrame7.setBounds(100, 300, 500, 580);
                mSwingFrame6.draw(canvas);
                mSwingFrame7.draw(canvas);
                isBallHit();
                break;
            case 8:
                mSwingFrame7.setBounds(0, 0, 0, 0);
                mSwingFrame8.setBounds(100, 300, 500, 580);
                mSwingFrame7.draw(canvas);
                mSwingFrame8.draw(canvas);
                isBallHit();
                break;
            case 9:
                mSwingFrame8.setBounds(0, 0, 0, 0);
                mSwingFrame9.setBounds(100, 300, 500, 580);
                mSwingFrame8.draw(canvas);
                mSwingFrame9.draw(canvas);
                isBallHit();
                break;
            case 10:
                mSwingFrame9.setBounds(0, 0, 0, 0);
                mSwingFrame10.setBounds(100, 300, 500, 580);
                mSwingFrame9.draw(canvas);
                mSwingFrame10.draw(canvas);
                isBallHit();
                break;
            case 11:
                mSwingFrame10.setBounds(0, 0, 0, 0);
                mSwingFrame11.setBounds(100, 300, 500, 580);
                mSwingFrame10.draw(canvas);
                mSwingFrame11.draw(canvas);
                break;
            case 12:
                mSwingFrame11.setBounds(0, 0, 0, 0);
                mSwingFrame12.setBounds(100, 300, 500, 580);
                Log.d("r", "lastase");
                mSwingFrame11.draw(canvas);
                mSwingFrame12.draw(canvas);
                mAnimationCompleted = true;
                break;
        }
    }

    private void isBallHit() {
        if(mBaseballCords.left < 550 && mBaseballCords.left > 300) {
            mBallHit = true;
            mSensorManager.unregisterListener(this);
            if(getBaseballListener()!=null){
                getBaseballListener().onFinish(true);
            }
            //insert win condition here
        }
    }

    public void setBaseballListener(BaseballListener Listener){
        local = Listener;
    }

    public BaseballListener getBaseballListener(){
        return local;
    }

    public interface BaseballListener{
        void onFinish(boolean res);
    }
}
