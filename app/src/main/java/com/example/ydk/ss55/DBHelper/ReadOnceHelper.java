package com.example.ydk.ss55.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReadOnceHelper extends SQLiteOpenHelper{

    public ReadOnceHelper(Context context) {
        super(context,"ReadOnce",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE ReadOnce(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ClubName TEXT, UserName TEXT, AttendName TEXT, NoticeName TEXT, AttendCheck boolean, NoticeCheck boolean);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS ReadOnce;");
        onCreate(sqLiteDatabase);
    }
}
