package com.example.simplemusicplayapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.simplemusicplayapp.R;

import androidx.fragment.app.Fragment;

/**
 * 我的歌单Fragment
 */
public class MySongListFragment extends Fragment {
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_song_list,container,false);
        return view;
    }
}