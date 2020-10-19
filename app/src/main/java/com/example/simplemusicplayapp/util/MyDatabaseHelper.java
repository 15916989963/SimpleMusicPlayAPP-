package com.example.simplemusicplayapp.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_RECENT_PLAY_MUSIC
            = "create table RecentPlayMusic (" +
            "id integer primary key autoincrement," +
            "position integer)";

    public static final String CREATE_MY_LOVE_MUSIC
            = "create table MyLoveMusic (" +
            "id integer primary key autoincrement," +
            "position integer)";

    public static final String CREATE_MY_SONG_LIST
            = "create table MySongList (" +
            "id integer primary key autoincrement," +
            "songListId integer," +
            "position integer)";

    private Context context;

    public MyDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_RECENT_PLAY_MUSIC);
        sqLiteDatabase.execSQL(CREATE_MY_LOVE_MUSIC);
        sqLiteDatabase.execSQL(CREATE_MY_SONG_LIST);
        Toast.makeText(context,"初始化完成",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
