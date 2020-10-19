package com.example.simplemusicplayapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simplemusicplayapp.R;
import com.example.simplemusicplayapp.bean.Music;
import com.example.simplemusicplayapp.util.AppConstant;


public class MyMusicSongAdapter extends BaseAdapter {

    class ViewContainer{
        /**
         * 歌曲名、歌手、持续时间、更多信息
         */
        private TextView musicName;
        private TextView musicSinger;
        private TextView musicDuration;
        private ImageView musicMoreMessage;
    }

    private Context context;
    private Music music;
    private ViewContainer vc;

    public MyMusicSongAdapter(Context context) {
        this.context = context;
    }

    public MyMusicSongAdapter() {
    }

    @Override
    public int getCount() {
        return AppConstant.MusicListMsg.musicList.size();
    }

    @Override
    public Object getItem(int i) {
        return AppConstant.MusicListMsg.musicList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        vc = null;
        if (view == null){
            vc = new ViewContainer();
            view = LayoutInflater.from(context)
                    .inflate(R.layout.item_my_music_song_listview, null);
            vc.musicName = view.findViewById(R.id.item_my_music_song_Name);
            vc.musicSinger = view.findViewById(R.id.item_my_music_song_singer);
            vc.musicDuration = view.findViewById(R.id.item_my_music_song_time);
            vc.musicMoreMessage = view.findViewById(R.id.item_my_music_song_more);
            view.setTag(vc);
        }else{
            vc = (ViewContainer) view.getTag();
        }
        music = AppConstant.MusicListMsg.musicList.get(i);
        Log.d("MyMusicSongAdapter",music.toString());
        vc.musicName.setText(music.getTitle());
        vc.musicSinger.setText(music.getArtist());
        vc.musicDuration.setText(String.valueOf(formatTime(music.getDuration())));

        vc.musicMoreMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"没有更多信息",Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }


    public static String formatTime(Long time){
        String min = time / (1000 * 60) + "";
        String sec = time % (1000 * 60) + "";
        if (min.length() < 2){
            min = "0" + min;
        }
        switch (sec.length()){
            case 4:
                sec = "0" + sec;
                break;
            case 3:
                sec = "00" + sec;
                break;
            case 2:
                sec = "000" + sec;
                break;
            case 1:
                sec = "0000" + sec;
                break;
        }
        return min + ":" + sec.trim().substring(0,2);
    }
}
