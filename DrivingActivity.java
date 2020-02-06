package com.celeste.waroware;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;


/**
 * Created by HambC on 2/22/2018.
 */

public class DrivingActivity extends Activity {

    DrivingView bc;
    private MediaPlayer ding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving);

        bc = findViewById(R.id.drivingview);
        bc.setDrivingListener(new DrivingView.DrivingListener() {

            @Override
            public void onFinish(boolean res) {
                setDrivingResult(res);
            }
        });

        ding = MediaPlayer.create(this,R.raw.driving);
        ding.setLooping(true);
        ding.start();
    }

    private static final String driving_result = "driving result";

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, DrivingActivity.class);
        return intent;
    }

    public static boolean driving_result(Intent result) {
        return result.getBooleanExtra(driving_result, false);
    }

    public void setDrivingResult(boolean result) {
        Intent data = getIntent();
        data.putExtra(driving_result, result);
        setResult(RESULT_OK, data);
        ding.pause();
        finish();
    }

}
