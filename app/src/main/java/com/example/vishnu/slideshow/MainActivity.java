package com.example.vishnu.slideshow;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    public static final String MODE = "MODE";


    ImageView imageView;

    /*MediaPlayer instance for the song*/
    static MediaPlayer mediaPlayer;
    /*Play/pause button*/
    Button button;

    /*To choose the soundtrack*/
    Spinner spinner;

    /*To play music*/
    Thread player;

    /*To enable or disable the sensor*/
    Button enable;
    Button disable;

    /*To pass on to the next activity with
    * info about whether the user wants to
    * use the sensor or not*/
    boolean mode = true;
    /*Tracker Variable that keeps track of
    * whether the music is playing or not*/
    boolean state=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageView = (ImageView) findViewById(R.id.imageView);

        imageView.setImageResource(R.drawable.image_1);

        /*Getting the handles for the buttons*/
        enable = (Button) findViewById(R.id.enable);
        disable = (Button) findViewById(R.id.disable);

        enable.setClickable(false);
        enable.setBackgroundColor(Color.YELLOW);
        enable.setTextColor(Color.BLACK);

        disable.setClickable(true);
        disable.setBackgroundColor(Color.BLACK);
        disable.setTextColor(Color.YELLOW);

        /*Gives toggle functionality to the buttons*/{
            enable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (enable.isClickable()) {

                        mode = true;

                        enable.setBackgroundColor(Color.YELLOW);
                        enable.setTextColor(Color.BLACK);
                        enable.setClickable(false);

                        disable.setBackgroundColor(Color.BLACK);
                        disable.setTextColor(Color.YELLOW);
                        disable.setClickable(true);

                        Toast.makeText(MainActivity.this,
                                "Proximity Sensor Activated",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this,
                                "Proximity Sensor is already Activated",
                                Toast.LENGTH_SHORT).show();

                    }
                }
            });

            disable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (disable.isClickable()) {

                        mode = false;

                        disable.setBackgroundColor(Color.YELLOW);
                        disable.setTextColor(Color.BLACK);
                        disable.setClickable(false);

                        enable.setBackgroundColor(Color.BLACK);
                        enable.setTextColor(Color.YELLOW);
                        enable.setClickable(true);

                        Toast.makeText(MainActivity.this,
                                "Proximity Sensor Deactivated",
                                Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(MainActivity.this,
                                "Proximity Sensor is already Deactivated",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }


        mediaPlayer = MediaPlayer.create(this, R.raw.vivalavida);

        button = (Button) findViewById(R.id.play);

        /*Takes care of soundtrack selection from spinner and handles interruptions caused by
        * selecting a second soundtrack while one is already playing*/{

            spinner = (Spinner) findViewById(R.id.spinner);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.songs, R.layout.my_spinner_item);

            adapter.setDropDownViewResource(R.layout.my_spinner_item);

            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    if (state) {
                        state = false;
                        button.setBackgroundResource(R.mipmap.ic_play);
                        mediaPlayer.release();

                    }

                    if (position == 0) {
                        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.vivalavida);
                    } else if (position == 1) {
                        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.speedofsound);
                    } else if (position == 2) {
                        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.godputasmile);
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.vivalavida);


                }
            });

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }

    public void play(View view) {

        //if it is not playing, i.e, user wants to play or resume a song
        if(!state)
        {
            //set state to true
            state = true;

            //start playing the song on a separate thread
            player = new Thread(){
                @Override
                public void run(){

                    //keep playing the music as long as the state is true
                    while(state) {
                        mediaPlayer.start();
                    }
                    //when state becomes false, it comes out of the loop and pauses music
                    mediaPlayer.pause();
                }
            };
            //start the thread
            player.start();
            //Set the button image to pause
            button.setBackgroundResource(R.mipmap.ic_pause);
        }

        //if music is already playing, then user wants to pause
        else{
            //As thread is already executing, setting state to false will make it come out of the
            //loop and pause the music
            state = false;
            //Set the button image to play
            button.setBackgroundResource(R.mipmap.ic_play);
        }



    }


    public void slideshow(View view) {

        Intent intent = new Intent(this, FlipperActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

        //if music is not playing, start playing it!
        if(!state)
            play(view);

        //Pass the value of mode to the next Activity
        intent.putExtra(MODE, mode);

        //To come back to this activity after execution
        startActivityForResult(intent, REQUEST_CODE);

        //Overriding default animation and replacing it with custom
        //fade in and out transition.
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

    }


}
