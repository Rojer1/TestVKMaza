package com.example.makar.testvkmaza;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by makar on 31.03.16.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "musicTrackDB";
    public static final String TABLE_MUSIC_BAD = "musicBadTrack";
    public static final String TABLE_MUSIC_GOOD = "musicGoodTrack";

    public static final String KEY_ID = "_id";
    public static final String KEY_TRACK = "trackName";






    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_MUSIC_BAD + "(" + KEY_ID
        + " integer primary key," + KEY_TRACK + " text" + ")");
        db.execSQL("create table " + TABLE_MUSIC_GOOD + "(" + KEY_ID
        + " integer primary key," + KEY_TRACK + " text" + ")");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
