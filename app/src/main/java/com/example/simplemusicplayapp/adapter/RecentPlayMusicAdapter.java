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

public class RecentPlayMusicAdapter extends BaseAdapter {

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

    public RecentPlayMusicAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return AppConstant.MusicListMsg.recentPlayMusicList.size();
    }

    @Override
    public Object getItem(int i) {
        return AppConstant.MusicListMsg.recentPlayMusicList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        vc = null;
        if (vc == null){
            vc = new ViewContainer();
            view = LayoutInflater.from(context)
                    .inflate(R.layout.item_recent_play_music_listview,null);
            vc.musicName = view.findViewById(R.id.item_recent_play_music_Name);
            vc.musicSinger = view.findViewById(R.id.item_recent_play_music_singer);
            vc.musicDuration = view.findViewById(R.id.item_recent_play_music_time);
            vc.musicMoreMessage = view.findViewById(R.id.item_recent_play_music_more);
            view.setTag(vc);
        }else {
            vc = (ViewContainer) view.getTag();
        }
        music = AppConstant.MusicListMsg.musicList.get(
                AppConstant.MusicListMsg.recentPlayMusicList.get(i));

        Log.d("RecentPlayMusic",music.toString());
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

    public void deleteNotify(){
        notifyDataSetChanged();
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
