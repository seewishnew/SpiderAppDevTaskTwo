package com.example.vishnu.slideshow;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class FlipperActivity extends AppCompatActivity implements SensorEventListener {

    public Chronometer chronometer;
    public TextView timer;
    private long countUp;

    /*number of images*/
    private static final int length=7;

    /*For proximity sensor*/
    private SensorManager sensorManager;
    private Sensor sensor;
    private float maxRange;

    /*This mode is to switch the order of images
    * getting displayed*/
    private boolean mode = true;

    /*RelativeLayout handle for getting info about
    * touch to switch the order of images getting displayed*/
    public RelativeLayout relativeLayout;

    /*This Mode is to get what mode of slideshow the user
     *wants to see.
     *Its value is obtained from the previous activity */
    private boolean Mode;

    /*This is used to flip through the images*/
    ViewFlipper viewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_flipper);

        /*Getting value of Mode from previous activity*/
        Mode = getIntent().getBooleanExtra(MainActivity.MODE, true);

        viewFlipper = (ViewFlipper) (findViewById(R.id.viewFlipper));

        /*Setting the first image as invisible. The animation interfered
        * with the display of images - causing superposition of the first
        * two images*/
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setVisibility(View.INVISIBLE);

        /*If the proximity sensor is enabled*/

        if(Mode){

            Log.d("MODE", "Inside mode");

            /*Go to the next image (as the first image won't be visble)*/
            flipNext();

            /*Timer is redundant in this case - the user can take as long as
            * she wants*/
            timer = (TextView) findViewById(R.id.timer);
            timer.setVisibility(View.INVISIBLE);

            /*Assigning the proximity sensor to sensor*/
            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

            /*Part of a developmental stage. A different idea to increase the
            * distance between hand and proximity sensor. Not in use in this
             * version*/
            maxRange = sensor.getMaximumRange();
            Log.d("MaxRange", "" + maxRange);

            /*Getting the relativeLayout handle*/
            relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

            /*onTouchListener basically functions as a parity counter that sets
            * mode to true when the user has touched the screen an even number of times
            * and false otherwise. So the user can toggle between flipNext and flipPrevious
            */
            relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if(!mode)
                        mode = true;

                    else
                        mode=false;

                    return false;
                }
            });


        }

        else {

            /*Getting the chronometer handle. The chronometer gives the input to the TextView
            * to display the duration of the slideshow. Using chronometer is easier as it comes
            * with its own set of functions. The textView is updated in an instance of the class
            * Background, which extends AsyncTask
            */
            chronometer = (Chronometer) findViewById(R.id.chronometer);
            timer = (TextView) findViewById(R.id.timer);


            chronometer.start();



            new Background().execute();

        }

    }


    @Override
    protected void onPause() {
        super.onPause();

        /*Unregistering, only if Mode is true
        * Otherwise it causes null pointer error*/
        if(Mode)
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*Re-registering, again only if Mode is true*/
        if(Mode)
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    /* A method to flip to the next image with appropriate animation that gives the feeling of a smooth
    transition.
    */
    public void flipNext(){
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.fadeout));
        viewFlipper.showNext();
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.fadein));
    }

    /* A method to flip to the previous image with appropriate animation that gives the feeling of a
     smooth transition.
    */
    public void flipPrevious(){
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.fadeout));
        viewFlipper.showPrevious();
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.fadein));
    }

    /*When the user comes close to the proximity sensor, this method gets called. And based on the
    * number of times she's touched the screen, it either flips to the next image or it flips ot the
    * previous image.*/
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.values[0]==0)
            if(mode)
                flipNext();
            else
                flipPrevious();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    /*This is the class that handles the automatic animation*/
    private class Background extends AsyncTask<Integer, Void, Integer>{

        @Override
        protected Integer doInBackground(Integer... params) {

            int i =0;

            /*length is the preset number of images*/
            while(i<length){
                ++i;

                /*To update timer's count and to move ahead to the next image,
                 the process needs to run on UI thread*/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                            @Override
                            public void onChronometerTick(final Chronometer chronometer) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //An offset of 3000 is given as the transition from MainActivity
                                        //to FlipperActivity takes that much time. So if it were not present
                                        //the timer would appear to suddenly jump to 3 seconds.
                                        countUp = (SystemClock.elapsedRealtime()-chronometer.getBase()-3000)/1000;
                                        String string = countUp/60 + ":" + countUp%60;
                                        timer.setText(string);
                                    }
                                });
                            }
                        });

                        flipNext();
                    }
                });

                /*To pause so that each image is displayed*/
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
                //once all images are set, finish
                // and go back to MainActivity
                setResult(RESULT_OK);
                finish();
            }
        }
    }


}
