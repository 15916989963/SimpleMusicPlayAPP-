package com.example.simplemusicplayapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.simplemusicplayapp.R;
import com.example.simplemusicplayapp.adapter.RecentPlayMusicAdapter;
import com.example.simplemusicplayapp.util.AppConstant;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * 最近播放Fragment
 */
public class RecentPlayMusicFragment extends Fragment {
    private View view;
    private View v;
    private RecentPlayMusicAdapter adapter;

    private Activity myActivity;
    private ListView listView;
    private LinearLayout itemLayout;
    private ImageView imageView;
    private Button deleteAllRecord;
    private RecentPlayMusicListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = getActivity();
        adapter = new RecentPlayMusicAdapter(myActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recent_play_music,container,false);
        deleteAllRecord = view.findViewById(R.id.delete_all_record);
        deleteAllRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.deleteRecentPlayMusic(adapter);
            }
        });

        v = LayoutInflater.from(myActivity).inflate(R.layout.item_recent_play_music_listview,null);

        imageView = v.findViewById(R.id.item_recent_play_music_more);
        itemLayout = v.findViewById(R.id.item_recent_play_music_layout);
        listView = view.findViewById(R.id.recent_play_music_listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if( AppConstant.MusicListMsg.musicList != null){
                    listener.recentPlayMusicListener(AppConstant.PlayerMsg.PLAY_MSG, i);
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (RecentPlayMusicFragment.RecentPlayMusicListener) context;
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

    public void updateRecentPlayMusicList() {
        adapter = null;
        adapter = new RecentPlayMusicAdapter(myActivity);
        listView.setAdapter(adapter);
    }


    public interface RecentPlayMusicListener{
        public void recentPlayMusicListener(int msg,int position);
        public void deleteRecentPlayMusic(RecentPlayMusicAdapter recentPlayMusicAdapter);
    }
}