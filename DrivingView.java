package com.celeste.waroware;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class DrivingView extends View implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private DrivingListener local;

    private Paint mBackgroundPaint;
    private Paint mRoadPaint;
    private Paint mBlockersPaint;
    private Drawable mCar;
    private Drawable mExplosion;
    private Drawable mCrate1;
    private Rect mCrate1Cords;
    private Drawable mCrate2;
    private Rect mCrate2Cords;

    private long mStartTime;
    private Rect mCarCords;
    private float mCarMove;
    private int mBoxCounter;
    private double mDelay;

    private boolean isDrivingFailed;

    private float[] mRotationArray;

    public DrivingView(Context context) {
        super(context);
        whenCreated();
    }

    public DrivingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        whenCreated();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        mRotationArray = event.values;
        invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);
        canvas.drawRect(160, 0, 1120, 800, mRoadPaint);
        canvas.drawRect(120, 0, 160, 800, mBlockersPaint);
        canvas.drawRect(1120, 0, 1160, 800, mBlockersPaint);

        if (mBoxCounter == 50) {
            int mCrateCords = (int) ((Math.random() * 800) + 160);
            mCrate1Cords.set(mCrateCords, -250, mCrateCords + 250, 0);
            mCrate1.setBounds(mCrate1Cords);
            mBoxCounter = 0;
        }

        if (mBoxCounter == 25) {
            int mCrateCords = (int) ((Math.random() * 800) + 160);
            mCrate2Cords.set(mCrateCords, -250, mCrateCords + 250, 0);
            mCrate2.setBounds(mCrate1Cords);
        }

        mCarMove = (mRotationArray[1]);
        if ((mCarCords.left <= 160) || (mCarCords.right >= 1120)) {
            isDrivingFailed = true;
            mSensorManager.unregisterListener(this);
            if(getDrivingListener()!=null){
                getDrivingListener().onFinish(true);
            }
            //insert failed screen
        }

        if(System.currentTimeMillis() - mStartTime > 5000) {
            //insert win screen etc.
            mSensorManager.unregisterListener(this);
            if(getDrivingListener()!=null){
                getDrivingListener().onFinish(false);
            }
        }

        if (mCarCords.intersect(mCrate1Cords) || mCarCords.intersect(mCrate2Cords)) {
            isDrivingFailed = true;
            mSensorManager.unregisterListener(this);
            if(getDrivingListener()!=null){
                getDrivingListener().onFinish(true);
            }
        }

        if (!isDrivingFailed) {
            mCarCords.offset((int) (mCarMove * 15), 0);
            mCar.setBounds(mCarCords);
            mCar.draw(canvas);
            mCrate1Cords.offset(0, 16);
            mCrate1.setBounds(mCrate1Cords);
            mCrate1.draw(canvas);
            mCrate2Cords.offset(0, 16);
            mCrate2.setBounds(mCrate2Cords);
            mCrate2.draw(canvas);
            mBoxCounter++;
        } else {
            mCrate1.setBounds(0, 0, 0, 0);
            mCrate2.setBounds(0, 0, 0, 0);
            mCar.setBounds(0, 0, 0, 0);
            mExplosion.setBounds(0, 0, 1280, 800);
            mExplosion.draw(canvas);
        }
    }

    public void whenCreated() {
        mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mSensor, 0);

        Resources res = getResources();
        mCar = res.getDrawable(R.drawable.car);
        mCrate1 = res.getDrawable(R.drawable.crate);
        mCrate2 = res.getDrawable(R.drawable.crate);
        mExplosion = res.getDrawable(R.drawable.explosion);

        int mCrateCords = (int) ((Math.random() * 800) + 160);
        mCrate1Cords = new Rect(mCrateCords, -250, mCrateCords + 250, 0);
        mCrateCords = (int) ((Math.random() * 800) + 160);
        mCrate2Cords = new Rect(mCrateCords, -650, mCrateCords + 250, -400);

        mRotationArray = new float[4];
        mDelay = 0.1;
        mBoxCounter = 0;
        mStartTime = System.currentTimeMillis();

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xff00ff00);
        mRoadPaint = new Paint();
        mRoadPaint.setColor(0xffa88d7b);
        mBlockersPaint = new Paint();
        mBlockersPaint.setColor(0xff504438);
        mCarCords = new Rect(575, 500, 725, 800);
        mCar.setBounds(mCarCords);
    }

    public void setDrivingListener(DrivingListener Listener){
        local = Listener;
    }

    public DrivingListener getDrivingListener(){
        return local;
    }

    public interface DrivingListener{
        void onFinish(boolean res);
    }
}
