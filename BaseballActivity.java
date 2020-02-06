package com.celeste.waroware;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

/**
 * Created by HambC on 2/26/2018.
 */

public class BaseballActivity extends Activity {

    BaseballView bc;
    private MediaPlayer ding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baseball);

        ding = MediaPlayer.create(this,R.raw.baseball);
        ding.setLooping(true);
        ding.start();

        bc = findViewById(R.id.baseballview);
        bc.setBaseballListener(new BaseballView.BaseballListener() {

            @Override
            public void onFinish(boolean res) {
                setBaseballResult(res);
            }
        });
    }

    private static final String baseball_result = "baseball result";

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, BaseballActivity.class);
        return intent;
    }

    public static boolean baseball_result(Intent result) {
        return result.getBooleanExtra(baseball_result, false);
    }

    public void setBaseballResult(boolean result) {
        Intent data = getIntent();
        data.putExtra(baseball_result, result);
        setResult(RESULT_OK, data);
        ding.pause();
        finish();
    }

}
