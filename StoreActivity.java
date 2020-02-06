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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Celeste on 2/26/2018.
 */

public class StoreActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private MediaPlayer ding;
    private boolean open=true;
    private boolean rocket=false;
    private long lastUpdate = 0;
    private float last_z;
    private static final int SHAKE_THRESHOLD = 600;
    private static final String store_result = "store result";
    private boolean toaster=true;
    private Animation slide_up;

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, StoreActivity.class);
        return intent;
    }

    public static boolean store_result(Intent result) {
        return result.getBooleanExtra(store_result, false);
    }

    private void setStoreResult(boolean result) {
        Intent data = getIntent();
        data.putExtra(store_result, result);
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
        setContentView(R.layout.activity_store);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        final ArrayList<Integer> mSprite = new ArrayList<Integer>();
        mSprite.add(R.drawable.turtwig);
        mSprite.add(R.drawable.piplup);
        mSprite.add(R.drawable.torchic);
        mSprite.add(R.drawable.chimchar);
        mSprite.add(R.drawable.mudkip);
        mSprite.add(R.drawable.treecko);
        mSprite.add(R.drawable.rocket);

        ding = MediaPlayer.create(this,R.raw.thief);
        ding.setLooping(true);
        ding.start();

        ImageView door = (ImageView)findViewById(R.id.door);
        door.setVisibility(View.INVISIBLE);

        new CountDownTimer(30000, 1000) {

            int t =0;

            public void onTick(long millisUntilFinished) {
                ImageView spr = findViewById(R.id.sprite1);
                slide_up = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_up);
                int i3;
                if(open) {
                    if(t!=0){
                        Random k = new Random();
                        i3 = k.nextInt(7 - 1) + 1;
                    } else {
                        Random k = new Random();
                        i3 = k.nextInt(6 - 1) + 1;
                    }

                    if (i3 == 6) {
                        rocket=true;
                    }
                    Log.d("r", "before");
                    spr.startAnimation(slide_up);
                    Log.d("r", "after");
                    spr.setImageResource(mSprite.get(i3));
                } else {
                    spr.setY(450);
                }

                if(rocket){
                    new CountDownTimer(1000, 1000) {

                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            if(open){
                                fail();
                            }
                            if(!open){
                                pass();
                            }
                        }
                    }.start();
                }
                t++;
            }

            public void onFinish() {
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

                long curTime = System.currentTimeMillis();

                if ((curTime - lastUpdate) > 100) {
                    long diffTime = (curTime - lastUpdate);
                    lastUpdate = curTime;

                    float speed = (last_z - z)/ diffTime * 10000;

                    if (speed > SHAKE_THRESHOLD) {
                        ImageView door = (ImageView)findViewById(R.id.door);
                        door.setVisibility(View.VISIBLE);
                        open=false;
                        if (!rocket){
                            fail();
                        }
                    }
                    last_z = z;
                }
            }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void fail(){
        if(toaster) {
            Toast toast = Toast.makeText(this, "YOU FAILED!", Toast.LENGTH_SHORT);
            toast.show();
            toaster = false;
        }

        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                setStoreResult(false);
                //Intent intent = new Intent(StoreActivity.this,MainActivity.class);
                //startActivity(intent);
            }
        }.start();
    }

    private void pass(){
        if(toaster) {
            Toast toast = Toast.makeText(this, "YOU SUCCEEDED!", Toast.LENGTH_SHORT);
            toast.show();
            toaster = false;
        }

        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                setStoreResult(true);
                //Intent intent = new Intent(StoreActivity.this,MainActivity.class);
                //startActivity(intent);
            }
        }.start();
    }
}
