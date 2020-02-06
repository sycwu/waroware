package com.celeste.waroware;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Celeste on 2/25/2018.
 */

public class MainActivity extends AppCompatActivity {
    private Button b1;
    private Button b2;
    private Button b3;
    private Button b4;
    private Button b5;
    private Button b6;
    private Boolean store_res;
    private Boolean ball_res;
    private Boolean fruit_res;
    private Boolean driving_res;
    private Boolean coffee_res;
    private Boolean baseball_res;
    private MediaPlayer ring;
    private static final int REQUEST_STORERES = 0;
    private static final int REQUEST_BALLRES = 1;
    private static final int REQUEST_FRUITRES = 2;
    private static final int REQUEST_DRIVINGRES = 3;
    private static final int REQUEST_COFFEERES = 4;
    private static final int REQUEST_BASEBALLRES = 5;
    private Boolean store_played=false;
    private Boolean ball_played=false;
    private Boolean fruit_played=false;
    private Boolean driving_played=false;
    private Boolean coffee_played=false;
    private Boolean baseball_played=false;

    @Override
    protected void onPause(){
        super.onPause();
        ring.release();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        ring = MediaPlayer.create(this,R.raw.title);
        ring.setLooping(true);
        ring.start();

        b1 = findViewById(R.id.button1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fruit_played) {
                    ring.release();
                    fruit_played=true;
                    Intent intent = FruitActivity.newIntent(MainActivity.this);
                    startActivityForResult(intent, REQUEST_FRUITRES);
                }
            }
        });

        b2 = findViewById(R.id.button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ball_played) {
                    ring.release();
                    ball_played = true;
                    Intent intent = BallActivity.newIntent(MainActivity.this);
                    startActivityForResult(intent, REQUEST_BALLRES);
                }
            }
        });

        b3 = findViewById(R.id.button3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!store_played) {
                    ring.release();
                    store_played = true;
                    Intent intent = StoreActivity.newIntent(MainActivity.this);
                    startActivityForResult(intent, REQUEST_STORERES);
                }
            }
        });

        b4 = findViewById(R.id.button4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!coffee_played) {
                    ring.release();
                    coffee_played = true;
                    Intent intent = CoffeeActivity.newIntent(MainActivity.this);
                    startActivityForResult(intent, REQUEST_COFFEERES);
                }
            }
        });

        b5 = findViewById(R.id.button5);
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!driving_played) {
                    ring.release();
                    driving_played = true;
                    Intent intent = DrivingActivity.newIntent(MainActivity.this);
                    startActivityForResult(intent, REQUEST_DRIVINGRES);
                }
            }
        });

        b6 = findViewById(R.id.button6);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!baseball_played) {
                    ring.release();
                    baseball_played = true;
                    Intent intent = BaseballActivity.newIntent(MainActivity.this);
                    startActivityForResult(intent, REQUEST_BASEBALLRES);
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        switch(requestCode) {
            case (REQUEST_BALLRES): {
                if (ball_played) {
                    ball_res = BallActivity.ball_result(data);
                    ring = MediaPlayer.create(this, R.raw.title);
                    ring.setLooping(true);
                    ring.start();
                    if (ball_res) {
                        b2.setText("BALANCE: PASSED");
                    }
                    if (!ball_res) {
                        b2.setText("BALANCE: FAILED");
                    }
                }
            }
            break;


            case (REQUEST_STORERES): {
                if (store_played) {
                    store_res = StoreActivity.store_result(data);
                    ring = MediaPlayer.create(this, R.raw.title);
                    ring.setLooping(true);
                    ring.start();
                    if (store_res) {
                        b3.setText("CLOSING UP: PASSED");
                    }
                    if (!store_res) {
                        b3.setText("CLOSING UP: FAILED");
                    }
                }
            }
            break;

            case (REQUEST_FRUITRES): {
                if (fruit_played) {
                    fruit_res = FruitActivity.fruit_result(data);
                    ring = MediaPlayer.create(this, R.raw.title);
                    ring.setLooping(true);
                    ring.start();
                    if (fruit_res) {
                        b1.setText("SHOOT THE FRUIT: PASSED");
                    }
                    if (!fruit_res) {
                        b1.setText("SHOOT THE FRUIT: FAILED");
                    }
                }
            }
            break;

            case (REQUEST_COFFEERES): {
                if (coffee_played) {
                    coffee_res = CoffeeActivity.coffee_result(data);
                    ring = MediaPlayer.create(this, R.raw.title);
                    ring.setLooping(true);
                    ring.start();
                    if (coffee_res) {
                        b4.setText("COFFEE BREAK: PASSED");
                    }
                    if (!coffee_res) {
                        b4.setText("COFFEE BREAK: FAILED");
                    }
                }
            }
            break;

            case (REQUEST_DRIVINGRES): {
                if (driving_played) {
                    driving_res = !DrivingActivity.driving_result(data);
                    ring = MediaPlayer.create(this, R.raw.title);
                    ring.setLooping(true);
                    ring.start();
                    if (driving_res) {
                        b5.setText("DRIVING: PASSED");
                    }
                    if (!driving_res) {
                        b5.setText("DRIVING: FAILED");
                    }
                }
            }
            break;

            case (REQUEST_BASEBALLRES): {
                if (baseball_played) {
                    baseball_res = BaseballActivity.baseball_result(data);
                    ring = MediaPlayer.create(this, R.raw.title);
                    ring.setLooping(true);
                    ring.start();
                    if (baseball_res) {
                        b6.setText("BASEBALL: PASSED");
                    }
                    if (!baseball_res) {
                        b6.setText("BASEBALL: FAILED");
                    }
                }
            }
            break;
            }
        }
}
