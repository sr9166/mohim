package com.example.ydk.ss55.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoticeHelper extends SQLiteOpenHelper {

    public NoticeHelper(Context context){
        super(context,"NoticeData.db",null,1);
    }

//    static String[] notice={"_id","ClubName","NoticeName","NoticeContent"};

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE NoticeData(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "ClubName TEXT,NoticeName TEXT,NoticeContent TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS NoticeData;");
        onCreate(sqLiteDatabase);
    }
}
