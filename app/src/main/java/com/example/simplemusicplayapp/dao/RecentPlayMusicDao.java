package com.example.simplemusicplayapp.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.simplemusicplayapp.util.AppConstant;
import com.example.simplemusicplayapp.util.MyDatabaseHelper;


public class RecentPlayMusicDao {
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    public RecentPlayMusicDao(MyDatabaseHelper myDatabaseHelper) {
        this.myDatabaseHelper = myDatabaseHelper;
    }

    public void insert(int position){
        sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("position",position);
        sqLiteDatabase.insert("RecentPlayMusic",null
        ,contentValues);
        sqLiteDatabase.close();
    }

    public void query(){
        sqLiteDatabase = myDatabaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("RecentPlayMusic"
        ,null,null,null,null
        ,null,null);
        while (cursor.moveToNext()){
            AppConstant.MusicListMsg.recentPlayMusicList.add(
                    cursor.getInt(cursor.getColumnIndex("position")));
        }
        sqLiteDatabase.close();
    }

    public int delete(){
        sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
        return sqLiteDatabase.delete("RecentPlayMusic",null,null);
    }
}
