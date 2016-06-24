package com.example.vishnu.slideshow;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class FlipperActivity extends AppCompatActivity {

    public static final int STOP = 0;
    public Chronometer chronometer;
    public TextView timer;
    private long countUp;
    private static final int length=7;
//    public Handler handler = new Handler(){
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//
//            switch(msg.what){
//                case  STOP: setResult(RESULT_OK);
//                    finish();
//                    break;
//
//
//            }
//        }
//    };


    ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_flipper);

        chronometer = (Chronometer) findViewById(R.id.chronometer);
        timer=(TextView) findViewById(R.id.timer);


        chronometer.start();

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setVisibility(View.INVISIBLE);
        viewFlipper = (ViewFlipper) (findViewById(R.id.viewFlipper));

        new Background().execute();

    }


    public void flipNext(){
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.fadeout));
        viewFlipper.showNext();
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.fadein));
    }


    private class Background extends AsyncTask<Integer, Void, Integer>{

        @Override
        protected Integer doInBackground(Integer... params) {

            int i =0;

//            try {
//                Log.d("First thread", "Executing first sleep");
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }


            while(i<length){
                ++i;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                            @Override
                            public void onChronometerTick(final Chronometer chronometer) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        countUp = (SystemClock.elapsedRealtime()-chronometer.getBase()-5000)/1000;
                                        String string = countUp/60 + ":" + countUp%60;
                                        timer.setText(string);
                                    }
                                });
                            }
                        });

                        flipNext();
                    }
                });

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }




            }

            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if(integer.equals(1)){
                setResult(RESULT_OK);
                finish();
            }
        }
    }


}