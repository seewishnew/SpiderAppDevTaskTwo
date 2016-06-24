package com.example.vishnu.slideshow;

import android.content.Intent;
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
    ImageView imageView;
    static MediaPlayer mediaPlayer;
    Button button;
    Spinner spinner;
    Thread player;

    boolean state=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageView = (ImageView) findViewById(R.id.imageView);

        imageView.setImageResource(R.drawable.image_1);

        mediaPlayer = MediaPlayer.create(this, R.raw.vivalavida);

        button = (Button) findViewById(R.id.play);

        spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.songs, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                if(state){
                    state = false;
                    button.setBackgroundResource(R.mipmap.ic_play);
                    mediaPlayer.release();

                }

                if(position==0)
                {
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.vivalavida);
                }

                else if(position==1)
                {
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.speedofsound);
                }

                else if(position ==2){
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.godputasmile);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.vivalavida);


            }
        });




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

        if(!state)
        {
            state = true;
            player = new Thread(){
                @Override
                public void run(){
                    while(state) {
                        mediaPlayer.start();
                    }

                    mediaPlayer.pause();
                }
            };

            player.start();

            button.setBackgroundResource(R.mipmap.ic_pause);
        }

        else{

            state = false;
            button.setBackgroundResource(R.mipmap.ic_play);
        }



    }


    public void slideshow(View view) {

        Intent intent = new Intent(this, FlipperActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        if(!state)
            play(view);
        startActivityForResult(intent, REQUEST_CODE);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

    }

}
