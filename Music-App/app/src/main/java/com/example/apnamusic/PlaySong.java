package com.example.apnamusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }

    TextView textView;
    ImageView prev,pause,next;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    String textContent;
    int position;
    SeekBar seekBar;
    Thread updateSeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        textView = findViewById(R.id.textView);
        pause = findViewById(R.id.pause);
        next = findViewById(R.id.next);
        prev = findViewById(R.id.prev);
        seekBar = findViewById(R.id.seek);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("songList");

        textContent =intent.getStringExtra("currentSong");
        textView.setText(textContent);
        textView.setSelected(true);
        position = intent.getIntExtra("position",0);
        Uri url = Uri.parse(songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this,url);
//        mediaPlayer.start();

        seekBar.setMax(mediaPlayer.getDuration());



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mediaPlayer.seekTo(seekBar.getProgress());



            }
        });

        updateSeek = new Thread(){
            @Override
            public  void  run(){
                int currentPosition = 0;
                try {
                    while (currentPosition<mediaPlayer.getDuration()){
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        sleep(800);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateSeek.start();


        pause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    pause.setImageResource(R.drawable.playe);
                    mediaPlayer.pause();
                }
                else {
                    pause.setImageResource(R.drawable.pauses);
                    mediaPlayer.start();
                }
            }


        });

        prev.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               mediaPlayer.stop();
               mediaPlayer.release();
               if (position!=0){
                   position=position-1;

               }
               else{
                   position =songs.size()-1;
                }
                Uri url = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),url);
                mediaPlayer.start();
                textContent =songs.get(position).getName().toString();
                textView.setText(textContent);
            }


        });

        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position!=songs.size()-1){
                    position=position+1;

                }
                else{
                    position = 0;
                }
                Uri url = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),url);
                mediaPlayer.start();
                textContent =songs.get(position).getName().toString();
                textView.setText(textContent);

            }


        });









    }
}