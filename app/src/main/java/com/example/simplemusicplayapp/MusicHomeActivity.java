package com.example.simplemusicplayapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.simplemusicplayapp.adapter.MyMusicSongAdapter;
import com.example.simplemusicplayapp.adapter.RecentPlayMusicAdapter;
import com.example.simplemusicplayapp.bean.Music;
import com.example.simplemusicplayapp.dao.RecentPlayMusicDao;
import com.example.simplemusicplayapp.fragment.MusicMenuFragment;
import com.example.simplemusicplayapp.fragment.MyLoveMusicFragment;
import com.example.simplemusicplayapp.fragment.MyMusicSongFragment;
import com.example.simplemusicplayapp.fragment.MySongListFragment;
import com.example.simplemusicplayapp.fragment.RecentPlayMusicFragment;
import com.example.simplemusicplayapp.service.PlayerService;
import com.example.simplemusicplayapp.util.AppConstant;
import com.example.simplemusicplayapp.util.FindMusicUtil;
import com.example.simplemusicplayapp.util.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MusicHomeActivity extends AppCompatActivity implements View.OnClickListener,
        MyMusicSongFragment.OnFragmentInteractionListener,
        RecentPlayMusicFragment.RecentPlayMusicListener {

    private MyDatabaseHelper myDatabaseHelper;
    private RecentPlayMusicDao recentPlayMusicDao;

    /**
     * 我的最爱、最近播放、我的音乐、我的歌单
     */
    private MyLoveMusicFragment myLoveMusicFragment;
    private RecentPlayMusicFragment recentPlayMusicFragment;
    private MyMusicSongFragment myMusicSongFragment;
    private MySongListFragment mySongListFragment;

    //Fragment控件所在位置
    private LinearLayout fragmentLayout;

    //musicHome所在位置
    private LinearLayout musicHomeLayout;

    /**
     * 点击事件的控件
     */
    private LinearLayout myLoveMusicLayout;
    private LinearLayout recentPlayMusicLayout;
    private LinearLayout myMusicSongLayout;
    private LinearLayout mySongListLayout;

    private boolean isFirst;

    //音乐播放进度条
    private static SeekBar musicSeekBar;

    //找到本地音乐的工具类
    private FindMusicUtil findMusicUtil;
    //是否暂停
    private boolean isPause;
    //播放模式
    private int playMode;
    //音乐的位置
    private static int music_position;
    //是否正在播放
    private boolean playing;

    /**
     * 哪个Fragment点击的播放
     * MY_LOVE_MUSIC
     * RECENT_PLAY_MUSIC
     * MY_MUSIC_SONG
     * MY_SONG_LIST
     */
    private int isWhatFragment;

    private Music playingMusic;

    /**
     * Home页面底部菜单栏控件
     */
    private ImageView play_mode;        //播放模式按钮
    private ImageView previous_music;   //上一曲
    private ImageView play_music;       //播放暂停按钮
    private ImageView next_music;       //下一曲


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_home);

        /**
         * Android6.0之后需要动态申请权限
         */
        if(ContextCompat.checkSelfPermission(MusicHomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MusicHomeActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MusicHomeActivity.this,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            },1);
        }

        //初始化控件
        init();
        /**
         * 添加Fragment
         */
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_music,myLoveMusicFragment)
                .add(R.id.fragment_music,recentPlayMusicFragment)
                .add(R.id.fragment_music,myMusicSongFragment)
                .add(R.id.fragment_music,mySongListFragment)
                .hide(myLoveMusicFragment)
                .hide(recentPlayMusicFragment)
                .hide(myMusicSongFragment)
                .hide(mySongListFragment)
                .commit();
        Listener();
    }

    private void changeWeight(){
        fragmentLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,0,1));
        musicHomeLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,0,0));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.my_love_music:
                getSupportFragmentManager().beginTransaction()
                        .show(myLoveMusicFragment)
                        .hide(recentPlayMusicFragment)
                        .hide(myMusicSongFragment)
                        .hide(mySongListFragment)
                        .commit();
                changeWeight();
                break;
            case R.id.recent_play_music:
                getSupportFragmentManager().beginTransaction()
                        .hide(myLoveMusicFragment)
                        .show(recentPlayMusicFragment)
                        .hide(myMusicSongFragment)
                        .hide(mySongListFragment)
                        .commit();
                changeWeight();
                break;
            case R.id.my_music_song:
                getSupportFragmentManager().beginTransaction()
                        .hide(myLoveMusicFragment)
                        .hide(recentPlayMusicFragment)
                        .show(myMusicSongFragment)
                        .hide(mySongListFragment)
                        .commit();
                changeWeight();
                break;
            case R.id.my_song_list:
                getSupportFragmentManager().beginTransaction()
                        .hide(myLoveMusicFragment)
                        .hide(recentPlayMusicFragment)
                        .hide(myMusicSongFragment)
                        .show(mySongListFragment)
                        .commit();
                changeWeight();
                break;
            /**
             * 播放模式点击事件
             */
            case R.id.play_mode:
                if (playMode == AppConstant.PlayerMsg.QUEUE_PLAY){
                    playMode = AppConstant.PlayerMsg.RANDOM_PLAY;
                    play_mode.setImageResource(R.mipmap.random_play);
                    Intent intent = new Intent("com.example.simplemusicplayapp");
                    intent.putExtra("play_music",AppConstant.PlayerMsg.RANDOM_PLAY);
                    intent.setComponent(new ComponentName("com.example.simplemusicplayapp",
                            "com.example.simplemusicplayapp.service.PlayerService$MusicBroadcastReceiver"));
                    sendBroadcast(intent);
                }else if (playMode == AppConstant.PlayerMsg.RANDOM_PLAY){
                    playMode = AppConstant.PlayerMsg.SINGLE_PLAY;
                    play_mode.setImageResource(R.mipmap.single_play);
                    Intent intent = new Intent("com.example.simplemusicplayapp");
                    intent.putExtra("play_music",AppConstant.PlayerMsg.SINGLE_PLAY);
                    intent.setComponent(new ComponentName("com.example.simplemusicplayapp",
                            "com.example.simplemusicplayapp.service.PlayerService$MusicBroadcastReceiver"));
                    sendBroadcast(intent);
                }else if (playMode == AppConstant.PlayerMsg.SINGLE_PLAY){
                    playMode = AppConstant.PlayerMsg.QUEUE_PLAY;
                    play_mode.setImageResource(R.mipmap.queue_play);
                    Intent intent = new Intent("com.example.simplemusicplayapp");
                    intent.putExtra("play_music",AppConstant.PlayerMsg.QUEUE_PLAY);
                    intent.setComponent(new ComponentName("com.example.simplemusicplayapp",
                            "com.example.simplemusicplayapp.service.PlayerService$MusicBroadcastReceiver"));
                    sendBroadcast(intent);
                }
                break;
            /**
             * 上一曲点击事件
             */
            case R.id.previous_music:
                previousMusic();
                break;
            /**
             * 播放按钮点击事件
             */
            case R.id.play_music:
                if (isPause == true){
                    isPause = false;
                    play_music.setImageResource(R.mipmap.pause_music);
                    if(music_position == 0){
                        initService(music_position);
                    }else {
                        //发送广播:播放音乐
                        Intent intent = new Intent("com.example.simplemusicplayapp");
                        intent.putExtra("play_music",AppConstant.PlayerMsg.PLAY_MSG);
                        intent.setComponent(new ComponentName("com.example.simplemusicplayapp",
                                "com.example.simplemusicplayapp.service.PlayerService$MusicBroadcastReceiver"));
                        sendBroadcast(intent);
                    }
                }else {
                    isPause = true;
                    play_music.setImageResource(R.mipmap.play_music);
                    //发送广播:暂停播放音乐
                    Intent intent = new Intent("com.example.simplemusicplayapp");
                    intent.putExtra("play_music",AppConstant.PlayerMsg.PAUSE_MSG);
                    intent.setComponent(new ComponentName("com.example.simplemusicplayapp",
                            "com.example.simplemusicplayapp.service.PlayerService$MusicBroadcastReceiver"));
                    sendBroadcast(intent);
                }
                break;
            /**
             * 下一曲点击事件
             */
            case R.id.next_music:
                nextMusic();
                break;
        }
    }

    private void previousMusic() {
        if (playMode == AppConstant.PlayerMsg.QUEUE_PLAY || playMode == AppConstant.PlayerMsg.SINGLE_PLAY){
            music_position = music_position - 1;
            if (music_position < 0){
                switch (isWhatFragment){
                    case AppConstant.PlayerMsg.MY_MUSIC_SONG:
                        music_position = AppConstant.MusicListMsg.musicList.size() - 1;
                        break;
                    case AppConstant.PlayerMsg.RECENT_PLAY_MUSIC:
                        music_position = AppConstant.MusicListMsg.recentPlayMusicList.size() - 1;
                        break;
                }
            }
            if (isFirst == true){
                initService(music_position);
            }else {
                switchMusic(music_position);
            }
        }else if (playMode == AppConstant.PlayerMsg.RANDOM_PLAY){
            switch (isWhatFragment){
                case AppConstant.PlayerMsg.MY_MUSIC_SONG:
                    if (isFirst == true){
                        initService((int)(Math.random() * (AppConstant.MusicListMsg.musicList.size() - 1)));
                    }else {
                        switchMusic((int)(Math.random() * (AppConstant.MusicListMsg.musicList.size() - 1)));
                    }
                    break;
                case AppConstant.PlayerMsg.RECENT_PLAY_MUSIC:
                    if (isFirst == true){
                        initService((int)(Math.random() * (AppConstant.MusicListMsg.recentPlayMusicList.size() - 1)));
                    }else {
                        switchMusic((int)(Math.random() * (AppConstant.MusicListMsg.recentPlayMusicList.size() - 1)));
                    }
            }
        }
    }

    //下一曲
    private void nextMusic(){
        //顺序播放模式下，按下一曲
        if (playMode == AppConstant.PlayerMsg.QUEUE_PLAY || playMode == AppConstant.PlayerMsg.SINGLE_PLAY){
            //获取当前播放音乐的位置，使music_position + 1指向musicList中的下一曲
            music_position = music_position + 1;
            switch (isWhatFragment){
                case AppConstant.PlayerMsg.MY_MUSIC_SONG:
                    if (music_position > (AppConstant.MusicListMsg.musicList.size() - 1)){
                        music_position = 0;
                    }
                    break;
                case AppConstant.PlayerMsg.RECENT_PLAY_MUSIC:
                    Log.d("RECENT_PLAY_MUSIC","我是顺序播放");
                    if (music_position > (AppConstant.MusicListMsg.recentPlayMusicList.size() - 1)){
                        music_position = 0;
                    }
                    break;
            }
            if (isFirst == true){
                initService(music_position);
            }else {
                switchMusic(music_position);
            }
        }else if(playMode == AppConstant.PlayerMsg.RANDOM_PLAY){
            switch (isWhatFragment){
                case AppConstant.PlayerMsg.MY_MUSIC_SONG:
                    if (isFirst == true){
                        initService((int)(Math.random() * (AppConstant.MusicListMsg.musicList.size() - 1)));
                    }else {
                        switchMusic((int)(Math.random() * (AppConstant.MusicListMsg.musicList.size() - 1)));
                    }
                    break;
                case AppConstant.PlayerMsg.RECENT_PLAY_MUSIC:
                    if (isFirst == true){
                        initService((int)(Math.random() * (AppConstant.MusicListMsg.recentPlayMusicList.size() - 1)));
                    }else {
                        switchMusic((int)(Math.random() * (AppConstant.MusicListMsg.recentPlayMusicList.size() - 1)));
                    }
                    break;
            }
        }
    }

    @Override
    public void onMyMusicFragmentInteraction(int msg) {

    }

    @Override
    public void onMyMusicFragmentInteraction(int msg, int position) {
        if (msg == AppConstant.PlayerMsg.PLAY_MSG){
            if( AppConstant.MusicListMsg.musicList != null){
                if (playing == false){
                    isPause = false;
                    playing = true;
                    isWhatFragment = AppConstant.PlayerMsg.MY_MUSIC_SONG;
                    initService(position);
                }else{
                    isWhatFragment = AppConstant.PlayerMsg.MY_MUSIC_SONG;
                    switchMusic(position);
                }
            }
        }
    }

    @Override
    public void deleteRecentPlayMusic(RecentPlayMusicAdapter adapter) {
        int i = 0;
        i = recentPlayMusicDao.delete();
        Log.d("delete",String.valueOf(i));
        Log.d("deleteList",String.valueOf(AppConstant.MusicListMsg.recentPlayMusicList.size()));
        if(i == AppConstant.MusicListMsg.recentPlayMusicList.size()){
            Toast.makeText(MusicHomeActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
            AppConstant.MusicListMsg.recentPlayMusicList.clear();
            /**
             * 通知RecentPlayMusicAdapter数据源已更新notifyDataSetChanged()
             */
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void recentPlayMusicListener(int msg, int position) {
        if (msg == AppConstant.PlayerMsg.PLAY_MSG){
            if( AppConstant.MusicListMsg.musicList != null){
                if (playing == false){
                    isPause = false;
                    playing = true;
                    isWhatFragment = AppConstant.PlayerMsg.RECENT_PLAY_MUSIC;
                    initService(position);
                }else{
                    isWhatFragment = AppConstant.PlayerMsg.RECENT_PLAY_MUSIC;
                    switchMusic(position);
                }
            }
        }
    }

    /**
     * 修改系统返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK){
            getSupportFragmentManager().beginTransaction()
                    .hide(myLoveMusicFragment)
                    .hide(recentPlayMusicFragment)
                    .hide(myMusicSongFragment)
                    .hide(mySongListFragment)
                    .commit();
            fragmentLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,0,0));
            musicHomeLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,0,1));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 点击列表切换歌曲
     */
    private void switchMusic(int position){
        if (isFirst == true){
            isFirst = false;
        }
        music_position = position;
        //判断是哪个Fragment播放的音乐
        IsWhatFragmentPlay(position);

        //根据播放的音乐来设置SeekBar的长度
        int musicDuration = (int) playingMusic.getDuration();
        musicSeekBar.setMax(musicDuration);
        Log.d("MusicDuration",String.valueOf(musicSeekBar.getMax()));

        //将播放的歌曲添加到recentPlayMusicList中
        if (isWhatFragment != AppConstant.PlayerMsg.RECENT_PLAY_MUSIC) {
            addToRecentPlayList(position);
        }

        play_music.setImageResource(R.mipmap.pause_music);      //设置播放按钮为播放图标
        Intent intent = new Intent("com.example.simplemusicplayapp");
        intent.putExtra("play_music",AppConstant.PlayerMsg.SWITCH_MUSIC);
        intent.putExtra("music",playingMusic);

        intent.setComponent(new ComponentName("com.example.simplemusicplayapp",
                "com.example.simplemusicplayapp.service.PlayerService$MusicBroadcastReceiver"));
        sendBroadcast(intent);
    }
    //最近播放
    private void addToRecentPlayList(int position){
        if(AppConstant.MusicListMsg.recentPlayMusicList.contains(position)){
        }else {
            recentPlayMusicDao.insert(position);
            AppConstant.MusicListMsg.recentPlayMusicList.add(position);
            recentPlayMusicFragment.updateRecentPlayMusicList();
        }
    }

    /**
     * 启动服务
     */
    private void initService(int position){
        if (isFirst == true){
            isFirst = false;
        }
        music_position = position;
        //判断是哪个Fragment播放的音乐
        IsWhatFragmentPlay(position);
        //根据播放的音乐来设置SeekBar的长度
        musicSeekBar.setMax((int) playingMusic.getDuration());

        Log.d("MusicDuration",String.valueOf(playingMusic.getDuration()));

        //将播放的歌曲添加到recentPlayMusicList中
        if (isWhatFragment != AppConstant.PlayerMsg.RECENT_PLAY_MUSIC) {
            addToRecentPlayList(position);
        }
        Intent intent = new Intent("com.example.simplemusicplayapp.MSG_ACTION");
        play_music.setImageResource(R.mipmap.pause_music);
        intent.putExtra("MSG",AppConstant.PlayerMsg.PLAY_MSG);
        intent.putExtra("music",playingMusic);
        intent.setClass(MusicHomeActivity.this, PlayerService.class);
        startService(intent);
    }

    //判断是哪个Fragment播放的音乐
    private void IsWhatFragmentPlay(int position) {
        switch (isWhatFragment) {
            case AppConstant.PlayerMsg.MY_MUSIC_SONG:
                Log.d("RECENT_PLAY_MUSIC","现在播放的Fragment是MY_MUSIC_SONG");
                playingMusic = AppConstant.MusicListMsg.musicList.get(position);
                break;
            case AppConstant.PlayerMsg.RECENT_PLAY_MUSIC:
                Log.d("RECENT_PLAY_MUSIC","现在播放的Fragment是Recent_Play_Music");
                playingMusic = AppConstant.MusicListMsg.musicList.get(
                        AppConstant.MusicListMsg.recentPlayMusicList.get(position));
                break;
        }
    }

    private void Listener() {
        /**
         * 设置监听器
         */
        myLoveMusicLayout.setOnClickListener(this);
        recentPlayMusicLayout.setOnClickListener(this);
        myMusicSongLayout.setOnClickListener(this);
        mySongListLayout.setOnClickListener(this);

        /**
         * 底部菜单栏按钮设置监听器
         */
        play_mode.setOnClickListener(this);
        previous_music.setOnClickListener(this);
        play_music.setOnClickListener(this);
        next_music.setOnClickListener(this);
        musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //获取当前拖放的位置，并发送给playerService
                int position = musicSeekBar.getProgress();
                Log.d("com.example.simplemusicplayapp","发送广播啦");
                Intent intent = new Intent("com.example.simplemusicplayapp");
                intent.putExtra("play_music",AppConstant.PlayerMsg.CHANGE_MUSIC_PLAY_POSITION);
                intent.putExtra("changedMusicPlayPosition",position);
                intent.setComponent(new ComponentName("com.example.simplemusicplayapp",
                        "com.example.simplemusicplayapp.service.PlayerService$MusicBroadcastReceiver"));
                sendBroadcast(intent);
            }
        });
    }


    private void init() {
        //搜索本地音乐并添加到数组中
        findMusicUtil = new FindMusicUtil();
        AppConstant.MusicListMsg.musicList = findMusicUtil.getMusicList(MusicHomeActivity.this.getContentResolver());


        myLoveMusicFragment = new MyLoveMusicFragment();
        recentPlayMusicFragment = new RecentPlayMusicFragment();
        myMusicSongFragment = new MyMusicSongFragment();
        mySongListFragment = new MySongListFragment();


        myLoveMusicLayout = findViewById(R.id.my_love_music);
        recentPlayMusicLayout = findViewById(R.id.recent_play_music);
        myMusicSongLayout = findViewById(R.id.my_music_song);
        mySongListLayout = findViewById(R.id.my_song_list);

        fragmentLayout = findViewById(R.id.fragment_music);
        musicHomeLayout = findViewById(R.id.music_home);

        play_music = findViewById(R.id.play_music);
        play_mode = findViewById(R.id.play_mode);
        previous_music = findViewById(R.id.previous_music);
        next_music = findViewById(R.id.next_music);

        musicSeekBar = findViewById(R.id.music_seekBar);

        /**
         * 初始化播放模式为顺序播放
         */
        playMode = AppConstant.PlayerMsg.QUEUE_PLAY;
        play_mode.setImageResource(R.mipmap.queue_play);
        /**
         * 初始化播放暂停按钮为暂停
         */
        isPause = true;
        play_music.setImageResource(R.mipmap.play_music);

        /**
         * 初始化playing（正在播放）为false
         */
        playing = false;

        //初始化当前播放位置为0，歌曲列表的第一首歌
        music_position = 0;

        //初始化playingMusic
        playingMusic = new Music();

        //初始化myDatabaseHelper
        myDatabaseHelper = new MyDatabaseHelper(MusicHomeActivity.this,"MusicDatebase"
                ,null,1);

        //初始化RecentPlayMusicDao
        recentPlayMusicDao = new RecentPlayMusicDao(myDatabaseHelper);

        //初始化AppConstant.MusicListMsg.recentPlayMusicList
        recentPlayMusicDao.query();

        //初始化isWhatFragment
        isWhatFragment = AppConstant.PlayerMsg.MY_MUSIC_SONG;

        //判断是否第一次启动
        isFirst = true;
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(musicHomeBroadcast);
    }

    protected void registerBroadcastReceiver() {
        //注册广播
        musicHomeBroadcast = new MusicHomeBroadcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.simplemusicplayapp.MusicHomeActivity$MusicHomeBroadcast");
        registerReceiver(musicHomeBroadcast, intentFilter);
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDatabaseHelper.close();
    }

    public static class MusicHomeBroadcast extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("SeekBarSeekBar",String.valueOf(intent.getIntExtra("SeekBar",0)));
            musicSeekBar.setProgress(intent.getIntExtra("SeekBar",0));
        }
    }
}