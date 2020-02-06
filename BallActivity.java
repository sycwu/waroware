package com.celeste.waroware;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Celeste on 2/25/2018.
 */

public class BallActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private float last_x, last_y, last_z;
    private MediaPlayer ding;
    private static final String ball_result = "ball result";
    private boolean toaster=true;

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, BallActivity.class);
        return intent;
    }

    public static boolean ball_result(Intent result) {
        return result.getBooleanExtra(ball_result, false);
    }

    private void setBallResult(boolean result) {
        Intent data = getIntent();
        data.putExtra(ball_result, result);
        setResult(RESULT_OK, data);
        ding.pause();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ball);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        music();

        new CountDownTimer(3000, 100) {
            int t=0;
            public void onTick(long millisUntilFinished) {
                ImageView lonk = (ImageView)findViewById(R.id.lonk);
                lonk.setImageResource(R.drawable.lonk2);
                Random k = new Random();
                int i3 = k.nextInt(4-1)-1;
                if(t!=0){
                    lonk.setRotation(lonk.getRotation()+i3*25);
                }
                if(t==0){
                    lonk.setRotation(0);
                }
                if (findViewById(R.id.lonk).getRotation() < 20 && findViewById(R.id.lonk).getRotation() > -20){
                    lonk.setImageResource(R.drawable.lonk);
                } else {
                    lonk.setImageResource(R.drawable.lonk2);
                }
                t++;
            }

            public void onFinish() {
                pass();
            }
        }.start();


    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            findViewById(R.id.lonk).setRotation(findViewById(R.id.lonk).getRotation()-(last_y-y)*100);

            last_x = x;
            last_y = y;
            last_z = z;

            Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

            if (findViewById(R.id.lonk).getRotation() <= -90 || findViewById(R.id.lonk).getRotation() >= 90) {
                // Start animation
                findViewById(R.id.lonk).startAnimation(slide_down);
                fail();
            }

            if (findViewById(R.id.lonk).getRotation() < 20 && findViewById(R.id.lonk).getRotation() > -20){
                ImageView lonk = (ImageView)findViewById(R.id.lonk);
                lonk.setImageResource(R.drawable.lonk);
            } else {
                ImageView lonk = (ImageView)findViewById(R.id.lonk);
                lonk.setImageResource(R.drawable.lonk2);
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void music(){
        ding = MediaPlayer.create(this,R.raw.ball);
        ding.setLooping(true);
        ding.start();
    }

    private void fail(){
        if(toaster) {
            Toast toast = Toast.makeText(this, "YOU FAILED!", Toast.LENGTH_SHORT);
            toast.show();
            toaster = false;
        }

        new CountDownTimer(1000, 100) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                setBallResult(false);
            }
        }.start();
    }

    private void pass(){
        if(toaster) {
            Toast toast = Toast.makeText(this, "YOU SUCCEEDED!", Toast.LENGTH_SHORT);
            toast.show();
            toaster = false;
        }

        new CountDownTimer(1000, 100) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                setBallResult(true);
            }
        }.start();
    }


}
