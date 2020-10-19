package com.example.simplemusicplayapp.util;

import com.example.simplemusicplayapp.bean.Music;

import java.util.ArrayList;
import java.util.List;

public class AppConstant {
    public class PlayerMsg {
        public static final int PLAY_MSG = 1;		//播放
        public static final int PAUSE_MSG = 2;		//暂停
        public static final int STOP_MSG = 3;		//停止
        public static final int CONTINUE_MSG = 4;	//继续
        public static final int PRIVIOUS_MSG = 5;	//上一首
        public static final int NEXT_MSG = 6;		//下一首
        public static final int PROGRESS_CHANGE = 7;//进度改变
        public static final int PLAYING_MSG = 8;	//正在播放
        public static final int QUEUE_PLAY = 9;     //顺序播放
        public static final int RANDOM_PLAY = 10;   //随机播放
        public static final int SINGLE_PLAY = 11;   //单曲循环
        public static final int SWITCH_MUSIC = 12;   //切换歌曲
        public static final int NOT_MUSIC_SELECTED = 13;   //还未选择要播放的音乐
        public static final int CHANGE_MUSIC_PLAY_POSITION = 14;  //改变音乐的播放位置
        public static final int MY_LOVE_MUSIC = 15;  //我的最爱
        public static final int RECENT_PLAY_MUSIC = 16;     //最近播放
        public static final int MY_MUSIC_SONG = 17;     //我的音乐
        public static final int MY_SONG_LIST = 18;      //我的歌单
    }

    public static class MusicListMsg{
        //媒体音乐
        public static List<Music> musicList = new ArrayList<>();

        //我的最爱
        public static List<Integer> myLoveMusic = new ArrayList<>();

        //最近播放
        public static List<Integer> recentPlayMusicList = new ArrayList<>();
    }
}
