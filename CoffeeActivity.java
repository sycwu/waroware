package com.celeste.waroware;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import static android.os.SystemClock.elapsedRealtime;

public class CoffeeActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager mSensorManager;
    Sensor mAccelerometer;
    private MediaPlayer ding;

    /////////////////////////////////

    ObjectAnimator mPitcherAnimator;
    ObjectAnimator mPourAnimator;
    AnimatorSet mAnimatorSet;

    /////////////////////////////////

    ImageView mMug;
    ImageView mCover;
    ImageView mPitcher;
    ImageView mPour;

    /////////////////////////////////

    float height = 618;
    float width = 1360;
    float last_x = 0;

    boolean ANIM = false;

    float startTime;
    float currentTime;
    float PouringTime;

    /////////////////////////////////

    ArrayList<ImageView> CoffeeMeterArray = new ArrayList<>();

    /////////////////////////////////

    private static final String coffee_result = "coffee result";

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, CoffeeActivity.class);
        return intent;
    }

    public static boolean coffee_result(Intent result) {
        return result.getBooleanExtra(coffee_result, false);
    }

    private void setCoffeeResult(boolean result) {
        Intent data = getIntent();
        data.putExtra(coffee_result, result);
        setResult(RESULT_OK, data);
        ding.pause();
        finish();
    }

    /////////////////////////////////

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
        setContentView(R.layout.activity_coffee);


        mMug = findViewById(R.id.mug_image);
        mCover = findViewById(R.id.cover_image);
        mPitcher = findViewById(R.id.pitcher_image);
        mPour = findViewById(R.id.pour_image);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        setupAll();

        startTime = elapsedRealtime();

        ding = MediaPlayer.create(this,R.raw.coffee);
        ding.setLooping(true);
        ding.start();

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        MugTilt(event);
        PitcherAnimation();
        PourIn();
        settingCoffeeMeter();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void setupAll() {
        mMug.setX(680);
        mMug.setY(300);

        mCover.setX(680);
        mCover.setY(390);

        mPitcher.setX(680);
        mPitcher.setY(0);

        mPour.setX(630);
        mPour.setY(143);

        mAnimatorSet = new AnimatorSet();

        CoffeeMeterArray.add((ImageView) findViewById(R.id.CoffeeMeter0));
        CoffeeMeterArray.add((ImageView) findViewById(R.id.CoffeeMeter1));
        CoffeeMeterArray.add((ImageView) findViewById(R.id.CoffeeMeter2));
        CoffeeMeterArray.add((ImageView) findViewById(R.id.CoffeeMeter3));
        CoffeeMeterArray.add((ImageView) findViewById(R.id.CoffeeMeter4));
        CoffeeMeterArray.add((ImageView) findViewById(R.id.CoffeeMeter5));

        for (int i = 0; i < CoffeeMeterArray.size(); i++) {
            CoffeeMeterArray.get(i).setX(-1000);
            CoffeeMeterArray.get(i).setY(0);
        }
    }

    public void MugTilt(SensorEvent event) {

        float x = event.values[1];
        last_x = mMug.getX() + (x * 30);

        if (last_x > 1180) {
            last_x = 1180;
        } else if (last_x < 0) {
            last_x = 0;
        }

        mMug.setX(last_x);
        mCover.setX(last_x);

    }

    public void PitcherAnimation() {
        currentTime = (startTime - elapsedRealtime());

        if(ANIM) {
            mPitcherAnimator = ObjectAnimator.ofFloat(mPitcher, "x", 50, 1200);
            mPourAnimator = ObjectAnimator.ofFloat(mPour, "x",0,1150 );
        }
        else if(!ANIM) {
            mPitcherAnimator = ObjectAnimator.ofFloat(mPitcher, "x", 1200, 50);
            mPourAnimator = ObjectAnimator.ofFloat(mPour, "x",1150, 0);
        }


        mAnimatorSet.playTogether(mPourAnimator, mPitcherAnimator);
        mAnimatorSet.setDuration(4000);

        if(!mAnimatorSet.isRunning()) {
            mAnimatorSet.start();
            ANIM= !ANIM;
        }

    }

    public void PourIn() {
        if (mPour.getX() + 15 >= mMug.getX() && mPour.getX() - 15 <= (mMug.getX() + mMug.getWidth())) {
            PouringTime += 75;
        }

    }

    public void settingCoffeeMeter() {
        if (PouringTime < 600) {
            CoffeeMeterArray.get(0).setX(0);
        } else if (PouringTime >= 600 && PouringTime < 1200) {
            CoffeeMeterArray.get(0).setX(-1000);
            CoffeeMeterArray.get(1).setX(0);
        } else if (PouringTime >= 1200 && PouringTime < 1800) {
            CoffeeMeterArray.get(1).setX(-1000);
            CoffeeMeterArray.get(2).setX(0);
        } else if (PouringTime >= 1800 && PouringTime < 2400) {
            CoffeeMeterArray.get(2).setX(-1000);
            CoffeeMeterArray.get(3).setX(0);
        } else if (PouringTime >= 2400 && PouringTime < 3000) {
            CoffeeMeterArray.get(3).setX(-1000);
            CoffeeMeterArray.get(4).setX(0);
        } else if (PouringTime >= 3000) {
            CoffeeMeterArray.get(4).setX(-1000);
            CoffeeMeterArray.get(5).setX(0);
            setCoffeeResult(true);
        }
    }
}
