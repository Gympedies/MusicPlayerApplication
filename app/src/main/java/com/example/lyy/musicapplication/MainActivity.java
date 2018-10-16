package com.example.lyy.musicapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import java.lang.String;

import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Music/";
    private File mp3Dir = new File(path);
    private ListView list;
    private int listIndex;
    private int loopType = 1;
    private final int SINGLE_PLAY = 0;
    private final int LOOP_PLAY = 1;
    private List<String> musicList;
    private SeekBar musicSeekBar;
    private Button playButton;
    private Button loopButton;
    private Handler handler;
    private int currentPosition;
    public void setLoopType(int loopType) {
        this.loopType = loopType;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musicList = new ArrayList<>();
        list = findViewById(R.id.musicList);
        musicSeekBar = findViewById(R.id.musicSeekBar);
        playButton = findViewById(R.id.playMusicButton);
        loopButton = findViewById(R.id.loopButton);
        //使用Handler更新进度条
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                int musicProgress = msg.getData().getInt("d");
                musicSeekBar.setProgress(musicProgress);
            }
        };
        //获取存取权限
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        //拖动进度条改变歌曲进度
        musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = musicSeekBar.getProgress();
                mediaPlayer.seekTo(progress);
            }
        });
        //歌曲列表
        if (mp3Dir.isDirectory()){
            File[] fs = mp3Dir.listFiles();
            for (File f :fs){
                musicList.add(f.toString().substring(26));
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,musicList);
            list.setAdapter(arrayAdapter);

        }
        //循环播放实现
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                switch (loopType){
                    //单曲循环播放
                    case SINGLE_PLAY:
                        mediaPlayer.start();
                        mediaPlayer.setLooping(true);
                        break;
                    //列表循环播放
                    case LOOP_PLAY:
                        mediaPlayer.setLooping(false);
                        if (listIndex<musicList.size()-1){
                            try {
                                Play(path+list.getItemAtPosition(listIndex+1).toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            listIndex++;
                        }

                        else{
                            try {
                                Play(path+list.getItemAtPosition(0).toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                }

            }
        });
        //List View点击播放
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listIndex = position;
                Log.i("lyy",listIndex+"");
                try {
                    Play(path+list.getItemAtPosition(position).toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //实时更新进度条
        new Thread(){
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message msg = Message.obtain();
                    msg.getData().putInt("d", mediaPlayer.getCurrentPosition());
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void lastMusic(View view)throws Exception {
        if (listIndex==0){
            Play(path+list.getItemAtPosition(musicList.size()-1).toString());
        }
        else {
            Play(path+list.getItemAtPosition(listIndex-1).toString());
            listIndex--;
        }
    }
    //播放 暂停
    public void playMusic(View view) {
        switch (playButton.getText().toString()){
            case "Play":
                mediaPlayer.seekTo(currentPosition);
                mediaPlayer.start();
                playButton.setText("Pause");
                break;
            case "Pause":
                currentPosition = mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();
                playButton.setText("Play");
                break;
        }
    }

    public void nextMusic(View view) throws Exception {
        if (listIndex<musicList.size()-1)
        {
        Play(path+list.getItemAtPosition(listIndex+1).toString());
        listIndex++;
        }
        else{
            Play(path+list.getItemAtPosition(0).toString());
        }
    }

    public void Play(String path)throws Exception{
        mediaPlayer.reset();
        mediaPlayer.setDataSource(path);
        mediaPlayer.prepare();
        musicSeekBar.setMax(mediaPlayer.getDuration());
        mediaPlayer.start();
        playButton.setText("Pause");
    }
    //切换歌曲循环模式
    public void changeLoop(View view) {
        switch (loopButton.getText().toString()){
            case "SINPLAY":
                setLoopType(SINGLE_PLAY);
                loopButton.setText("LOOPLAY");
                break;
            case "LOOPLAY":
                setLoopType(LOOP_PLAY);
                loopButton.setText("SINPLAY");
                break;
        }
    }
}
