package com.celeste.waroware;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by Celeste on 2/1/2018.
 */

public class FanDrawingView extends View {
    private static final String TAG = "FanDrawingView";
    private Paint mBoxPaint;
    private Paint mScorePaint;
    private Paint mBackgroundPaint;
    private Paint mTextPaint;
    private boolean game=false;
    private int rounds;
    private CountDownTimer time;
    private boolean shake=false;

    public FanDrawingView(Context context){
        this(context, null);

    }

    public FanDrawingView(Context context, AttributeSet attrs){
        super(context,attrs);
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(60);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        MediaPlayer ring= MediaPlayer.create(getContext(),R.raw.battle);
        ring.setLooping(true);
        ring.start();

            shake=FanActivity.isMshake();

            if(!shake) {
                Bitmap iv = decodeRes(getContext().getResources(),R.drawable.cliff);
                iv = scaleBitmap(iv,getWidth(),getHeight());
                canvas.drawBitmap(iv,0,0,null);

                Bitmap e1 = decodeRes(getContext().getResources(),R.drawable.hammer);
                e1 = scaleBitmap(e1,70,90);

                canvas.drawBitmap(e1,getWidth() / 4,getHeight() / 2,null);
                canvas.drawBitmap(e1,getWidth() / 4+130,getHeight() / 2-20,null);
                canvas.drawBitmap(e1,getWidth() / 4+250,getHeight() / 2,null);
                canvas.drawBitmap(e1,getWidth() / 4+490,getHeight() / 2-50,null);
                canvas.drawBitmap(e1,getWidth() / 4+510,getHeight() / 2+40,null);
                canvas.drawBitmap(e1,getWidth() / 4+690,getHeight() / 2,null);
            }
            else {
                ring.pause();
                ring.release();
                MediaPlayer poof= MediaPlayer.create(getContext(),R.raw.poof);
                poof.start();
                Bitmap iv = decodeRes(getContext().getResources(),R.drawable.cliff);
                iv = scaleBitmap(iv,getWidth(),getHeight());
                canvas.drawBitmap(iv,0,0,null);

                Bitmap e1 = decodeRes(getContext().getResources(),R.drawable.smoke);
                e1 = scaleBitmap(e1,70,90);

                canvas.drawBitmap(e1,getWidth() / 4,getHeight() / 2,null);
                canvas.drawBitmap(e1,getWidth() / 4+130,getHeight() / 2-20,null);
                canvas.drawBitmap(e1,getWidth() / 4+250,getHeight() / 2,null);
                canvas.drawBitmap(e1,getWidth() / 4+490,getHeight() / 2-50,null);
                canvas.drawBitmap(e1,getWidth() / 4+510,getHeight() / 2+40,null);
                canvas.drawBitmap(e1,getWidth() / 4+690,getHeight() / 2,null);
            }
    }

    public static Bitmap decodeRes(Resources res, int id) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        for (options.inSampleSize = 1; options.inSampleSize <= 32; options.inSampleSize++) {
            try {
                bitmap = BitmapFactory.decodeResource(res, id, options);
                Log.d(TAG, "Decoded successfully for sampleSize " + options.inSampleSize);
                break;
            } catch (OutOfMemoryError outOfMemoryError) {
// If an OutOfMemoryError occurred, we continue with for loop and next inSampleSize value
                Log.e(TAG, "outOfMemoryError while reading file for sampleSize " + options.inSampleSize
                        + " retrying with higher value");
            }
        }
        return bitmap;
    }

    /**
     * Scales the provided bitmap to have the height and width provided.
     * (Alternative method for scaling bitmaps
     * since Bitmap.createScaledBitmap(...) produces bad (blocky) quality bitmaps.)
     *
     * @param bitmap is the bitmap to scale.
     * @param newWidth is the desired width of the scaled bitmap.
     * @param newHeight is the desired height of the scaled bitmap.
     * @return the scaled bitmap.
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float scaleX = newWidth / (float) bitmap.getWidth();
        float scaleY = newHeight / (float) bitmap.getHeight();
        float pivotX = 0;
        float pivotY = 0;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }
}
