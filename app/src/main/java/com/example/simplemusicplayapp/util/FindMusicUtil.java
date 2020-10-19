package com.example.simplemusicplayapp.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.example.simplemusicplayapp.bean.Music;

import java.util.ArrayList;
import java.util.List;

public class FindMusicUtil {
    public List<Music> getMusicList(ContentResolver contentResolver){
        Cursor cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null
                ,null,null
                ,MediaStore.Audio.Media.IS_MUSIC);
        List<Music> musicList = new ArrayList<>();
        while (cursor.moveToNext()) {
            Music music = new Music();
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
            Long album_id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
            /**
             * 筛选出时长大于60秒的音乐
             */
            if (isMusic != 0 && duration/(1000 * 60) >= 1){
                music.setId(id);
                music.setAlbum_id(album_id);
                music.setAlbum(album);
                music.setTitle(title);
                music.setArtist(artist);
                music.setUrl(url);
                music.setSize(size);
                music.setDuration(duration);
                music.setIsMusic(isMusic);
                musicList.add(music);
            }
        }
        return musicList;
    }
}
