package com.example.simplemusicplayapp.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.simplemusicplayapp.R;
import com.example.simplemusicplayapp.adapter.MyMusicSongAdapter;
import com.example.simplemusicplayapp.util.AppConstant;


/**
 * 我的音乐Fragment
 */
public class MyMusicSongFragment extends Fragment {
    private View view;
    private View v;
    private MyMusicSongAdapter adapter;
    private Activity myActivity;
    private ListView listView;
    private LinearLayout itemLayout;
    private ImageView imageView;
    private ImageView isMyLoveMusic;
    private OnFragmentInteractionListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = getActivity();
        adapter = new MyMusicSongAdapter(myActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_music_song,container,false);
        v = LayoutInflater.from(myActivity).inflate(R.layout.item_my_music_song_listview,null);
        imageView = v.findViewById(R.id.item_my_music_song_more);
        itemLayout = v.findViewById(R.id.item_my_music_song_layout);
        listView = view.findViewById(R.id.my_music_song_listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if( AppConstant.MusicListMsg.musicList != null){
                    listener.onMyMusicFragmentInteraction(AppConstant.PlayerMsg.PLAY_MSG,i);
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnFragmentInteractionListener) context;
        }catch (ClassCastException e){
            e.printStackTrace();
            Log.d("类型转换异常","类型转换异常");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnFragmentInteractionListener{
        public void onMyMusicFragmentInteraction(int msg);
        public void onMyMusicFragmentInteraction(int msg,int position);
    }
}