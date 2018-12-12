package com.example.ydk.ss55.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {

    //버전 2 : money 추가함. 업데이트 하였음.
    //버전 3 : 벌금 column 추가함.
    public MyDBHelper(Context context) {
        super(context,"UserInfo",null,3);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE UserInfo(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "email TEXT, password TEXT, Money INTEGER, Penalty INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS UserInfo;");
        onCreate(sqLiteDatabase);
    }
}
