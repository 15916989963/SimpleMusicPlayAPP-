package com.example.simplemusicplayapp.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.example.simplemusicplayapp.bean.Music;
import com.example.simplemusicplayapp.util.AppConstant;

import java.io.IOException;


public class PlayerService extends Service {

    private MyThread myThread;

    //正在播放的音乐
    private Music playingMusic;

    private static MediaPlayer mediaPlayer;

    private boolean threadIsWait = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * onStartCommand()方法就是启动Service时调用的方法
     * 里面的第一个参数intent就是Activity中开启服务传的intent，
     * 里面包含了被点击的Music信息
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        myThread = new MyThread();
        if(mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
        }
        int msg = intent.getIntExtra("MSG",0);
        playingMusic = (Music) intent.getSerializableExtra("music");
        if (msg == AppConstant.PlayerMsg.PLAY_MSG){
            playMusic();
        }
        myThread.start();
        return Service.START_STICKY_COMPATIBILITY;
    }

    //启动服务第一次播放调用的方法
    private void playMusic() {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(playingMusic.getUrl());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //点击播放按钮播放音乐调用的函数
    private void startMusic(){
        if (mediaPlayer != null){
            mediaPlayer.start();
        } else{
            //还未选择要播放的歌曲

        }
    }

    //点击暂停按钮暂停播放
    private void pauseMusic(){
        if (mediaPlayer != null){
            mediaPlayer.pause();
        } else{
            //还未选择要播放的歌曲

        }
    }


    //点击歌曲列表切换歌曲所调用的函数
    private void switchMusic(Intent intent){
        mediaPlayer.reset();
        try {
            playingMusic = (Music) intent.getSerializableExtra("music");
            mediaPlayer.setDataSource(playingMusic.getUrl());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //拖动进度条调用的方法
    private void changedMusicPlayPosition(int position){
        mediaPlayer.seekTo(position);
        Log.d("seekToPosition",String.valueOf(position));
    }


    @Override
    public void onDestroy() {
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    //设置是否单曲循环
    private void singlePlay(boolean isLooping) {
        if(mediaPlayer != null) {
            mediaPlayer.setLooping(isLooping);
        }else {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setLooping(isLooping);
        }
    }


    class MyThread extends Thread{
        @Override
        public void run() {
            Intent intent = new Intent("com.example.simplemusicplayapp.MusicHomeActivity$MusicHomeBroadcast");
            intent.setComponent(new ComponentName("com.example.simplemusicplayapp",
                    "com.example.simplemusicplayapp.MusicHomeActivity$MusicHomeBroadcast"));
            while (true) {
                try {
                    Thread.sleep(1000);
                    intent.putExtra("SeekBar",mediaPlayer.getCurrentPosition());
                    sendBroadcast(intent);
                    Log.d("SeekBar","发广播啦发广播啦");
                    Log.d("SeekBar",String.valueOf(mediaPlayer.getCurrentPosition()));
                    Log.d("SeekBar持续时间",String.valueOf(playingMusic.getDuration()));
                    if (mediaPlayer == null){
                        Log.d("mediaPlayer","mediaPlayer == null");
                        break;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.d("mediaPlayer","线程要结束了");
        }
    }

    public static class MusicBroadcastReceiver extends BroadcastReceiver{
        private PlayerService playerService = new PlayerService();
        @Override
        public void onReceive(Context context, Intent intent) {
            /**
             * 接受播放暂停广播
             */
            switch (intent.getIntExtra("play_music",AppConstant.PlayerMsg.PLAY_MSG)){
                case AppConstant.PlayerMsg.PLAY_MSG:
                    //开始或继续播放
                    playerService.startMusic();
                    break;
                case AppConstant.PlayerMsg.PAUSE_MSG:
                    //暂停播放
                    playerService.pauseMusic();
                    break;
                case AppConstant.PlayerMsg.SWITCH_MUSIC:
                    //切换歌曲
                    playerService.switchMusic(intent);
                case AppConstant.PlayerMsg.SINGLE_PLAY:
                    //单曲循环
                    playerService.singlePlay(true);
                    break;
                    //顺序播放
                case AppConstant.PlayerMsg.QUEUE_PLAY:
                    //随机播放
                case AppConstant.PlayerMsg.RANDOM_PLAY:
                    playerService.singlePlay(false);
                    break;
                case AppConstant.PlayerMsg.CHANGE_MUSIC_PLAY_POSITION:
                    playerService.changedMusicPlayPosition
                            (intent.getIntExtra("changedMusicPlayPosition",0));
                    break;
            }
        }

    }
}
