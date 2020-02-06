package com.celeste.waroware;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import static android.os.SystemClock.elapsedRealtime;


public class FruitActivity extends AppCompatActivity implements SensorEventListener{

    ImageView mBow;
    ImageView mApple;
    ImageView mCrosshairs;
    SensorManager mSensorManager;
    Sensor  mAccelerometer;
    private MediaPlayer ding;

    //////////////////////////////////////////////////

    float height = 618;
    float width = 1280;
    private float[] crosshairCenter = new float[2];
    private float[] crosshairTopLeft = new float[2];
    private double[] bowPivotPoint = {57.5, 209};
    float startTime;
    float currentTime;

    //////////////////////////////////////////////////

    //Intent:
    private static final String fruit_result = "fruit result";

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, FruitActivity.class);
        return intent;
    }

    public static boolean fruit_result(Intent result) {
        return result.getBooleanExtra(fruit_result, false);
    }

    private void setFruitResult(boolean result) {
        Intent data = getIntent();
        data.putExtra(fruit_result, result);
        setResult(RESULT_OK, data);
        ding.pause();
        finish();
    }

    //////////////////////////////////////////////////

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit);

        mBow = findViewById(R.id.bow_image);
        mApple = findViewById(R.id.apple_image);
        mCrosshairs = findViewById(R.id.crosshairs_image);
        setupAll();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this,  mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        ding = MediaPlayer.create(this,R.raw.fruit);
        ding.setLooping(true);
        ding.start();

        startTime = elapsedRealtime();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        currentTime = (elapsedRealtime() - startTime)/1000;

        Log.d("time", "Time:" + currentTime);

        if(currentTime <= 5) {
                moveCrosshairs(event);
                rotateBow();

        }
        else{
            CheckWin();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void setupAll(){
        mCrosshairs.setX(width-100);
        mCrosshairs.setY(height-100);

        crosshairTopLeft[0] = mCrosshairs.getX();
        crosshairTopLeft[1] = mCrosshairs.getY();

        crosshairCenter[0]= crosshairTopLeft[0]+50;
        crosshairCenter[1]= crosshairTopLeft[1]+50;

        mBow.setPivotX((float)bowPivotPoint[0]);
        mBow.setPivotY((float)bowPivotPoint[1]);

        mApple.setX((float)(Math.random()*(1180 - width/2) + width/2));
        mApple.setY((float)(Math.random()*(518)));

    }

    public void moveCrosshairs(SensorEvent event){

        float x = (float) event.values[1] *30;
        float y = (float) event.values[0] *30;

        // CHeck to make sure its on screen

        if (mCrosshairs.getX() + x > 1180) {
            crosshairTopLeft[0] = 1180;
        }
        else if (mCrosshairs.getX() + x < 640) {
            crosshairTopLeft[0] = 640;
        }
        else{
            crosshairTopLeft[0] = mCrosshairs.getX() + x;
        }

        if (mCrosshairs.getY() + y > 518) {
            crosshairTopLeft[1] = 518;
        }
        else if (mCrosshairs.getY() + y < 0) {
            crosshairTopLeft[1] = 0;
        }
        else{
            crosshairTopLeft[1] = mCrosshairs.getY() + (y);
        }

        crosshairCenter[0] = crosshairTopLeft[0]-50;
        crosshairCenter[1] = crosshairTopLeft[1]-50;

        mCrosshairs.setX(crosshairTopLeft[0]);
        mCrosshairs.setY(crosshairTopLeft[1]);

    }

    public void rotateBow() {
        double changeX = crosshairCenter[0] - bowPivotPoint[0];

        double changeY = crosshairCenter[1]- bowPivotPoint[1];

        double radRotation = Math.atan(changeY/changeX);

        double degreeRotation = Math.toDegrees(radRotation);

        mBow.setRotation((float) degreeRotation);
    }

    public void CheckWin(){

        if(crosshairCenter[0]>= mApple.getX()-100 && crosshairCenter[0]<= mApple.getX()
                && crosshairCenter[1]>= mApple.getY()-100 && crosshairCenter[1]<= mApple.getY()){
            Toast toast = Toast.makeText(this, "YOU SUCCEEDED!", Toast.LENGTH_SHORT);
            toast.show();
            new CountDownTimer(3000, 1000) {

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    setFruitResult(true);
                }
            }.start();
        }
        else{
            Toast toast = Toast.makeText(this, "YOU FAILED!", Toast.LENGTH_SHORT);
            toast.show();
            new CountDownTimer(3000, 1000) {

                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    setFruitResult(false);
                }
            }.start();
        }
    }
}
