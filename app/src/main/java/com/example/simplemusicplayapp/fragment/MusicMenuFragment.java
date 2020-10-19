package com.example.simplemusicplayapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.simplemusicplayapp.R;

import androidx.fragment.app.Fragment;

public class MusicMenuFragment extends Fragment implements View.OnClickListener {
    private View view;

    /**
     * Home页面底部菜单栏控件
     */
    private ImageView play_mode;        //播放模式按钮
    private ImageView previous_music;   //上一曲
    private ImageView play_music;       //播放暂停按钮
    private ImageView next_music;       //下一曲

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_music_menu,container,false);
        play_music = view.findViewById(R.id.play_music);
        play_mode = view.findViewById(R.id.play_mode);
        previous_music = view.findViewById(R.id.previous_music);
        next_music = view.findViewById(R.id.next_music);
        /**
         * 底部菜单栏按钮设置监听器
         */
        play_mode.setOnClickListener(this);
        previous_music.setOnClickListener(this);
        play_music.setOnClickListener(this);
        next_music.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }
}
