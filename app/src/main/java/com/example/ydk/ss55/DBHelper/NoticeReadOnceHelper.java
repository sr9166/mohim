package com.example.ydk.ss55.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoticeReadOnceHelper extends SQLiteOpenHelper{

    public NoticeReadOnceHelper(Context context) {
        super(context,"NoticeReadOnce",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE NoticeReadOnce(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ClubName TEXT, UserName TEXT, NoticeName TEXT, NoticeCheck boolean);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS NoticeReadOnce;");
        onCreate(sqLiteDatabase);
    }
}
